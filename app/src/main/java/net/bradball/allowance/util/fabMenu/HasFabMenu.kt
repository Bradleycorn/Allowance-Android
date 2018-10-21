package net.bradball.allowance.util.fabMenu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.support.DaggerAppCompatActivity
import net.bradball.allowance.util.fabMenu.patterns.VerticalPattern

const val DEFAULT_ANIMATION_DURATION = 200L

interface HasFabMenu {
    fun onCreateFabMenu(menu: Menu): Boolean = false
}

abstract class FabActivity: DaggerAppCompatActivity(), HasFabMenu {

    private var fabMenu: Menu? = null
    private var fabMenuIsValid = false
    private var fabMenuAnchor: FloatingActionButton? = null
    protected var fabMenuAnchorTransition = FabMenuTransition()
    private var isFabMenuOpen = false
    private var fabMenuContainer: ViewGroup? = null
    private val fabMenuItems: MutableList<FabMenuItem> = mutableListOf()
    protected var fabMenuPattern = VerticalPattern()

    protected fun setFabMenuAnchor(view: FloatingActionButton) {
        fabMenuAnchor = view
        fabMenuAnchor?.setOnClickListener {
            handleFabClick()
        }

        if (fabMenuContainer == null) {
            fabMenuContainer = view.parent as ViewGroup
        }
    }

    protected fun setFabMenuContainer(viewGroup: ViewGroup) {
        fabMenuContainer = viewGroup
        //TODO = If we change the menu container, we should probably clear out any existing menu
    }

    private fun handleFabClick() {
        //TODO: handle fab open vs close click
        when (isFabMenuOpen) {
            false -> openMenu()
            true -> closeMenu()
        }
    }

    private fun openMenu() {
        if (validateAndCreateFabMenu()) {
            buildMenu(fabMenu!!)
        }
        if (fabMenuItems.isNotEmpty()) {
            val anim = AnimatorSet()
            val animBuilder = anim.play(fabMenuAnchorTransition.getOpeningAnimation(fabMenuAnchor!!, fabMenuAnchor!!.x, fabMenuAnchor!!.y))
            fabMenuItems.forEach {
                animBuilder.with(it.openingAnimation)
            }
            anim.start()
            isFabMenuOpen = true
        }
    }

    fun closeMenu() {
        if (fabMenuItems.isNotEmpty()) {
            //Do the animation before removing the view
            val anim = AnimatorSet()
            val animBuilder = anim.play(fabMenuAnchorTransition.getClosingAnimation(fabMenuAnchor!!, fabMenuAnchor!!.x, fabMenuAnchor!!.y))
            for (fabMenuItem in fabMenuItems) {
                animBuilder.with(fabMenuItem.closingAnimation)
            }
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    fabMenuItems.forEach { fabMenuContainer?.removeView(it.miniFab) }
                }
            })
            anim.start()
        }
        isFabMenuOpen = false
    }

    private fun validateAndCreateFabMenu(): Boolean {
        var show = (fabMenu != null && fabMenuIsValid)
        if (fabMenu == null || !fabMenuIsValid) {
            show = dispatchCreateFabMenu()
            val fragmentList = this.supportFragmentManager.fragments
            show = show.or(dispatchFragmentCreateFabMenu(fragmentList, fabMenu!!))
        }
        return show
    }

    fun invalidateFabMenu() {
        fabMenuIsValid = false
    }


    private fun dispatchCreateFabMenu(): Boolean {
        fabMenu = fabMenu?.apply { clear() } ?: PopupMenu(this, fabMenuAnchor).menu
        fabMenuIsValid = true
        return onCreateFabMenu(fabMenu!!)
    }

    private fun dispatchFragmentCreateFabMenu(fragments: List<Fragment>, menu: Menu): Boolean {
        var show = false
        fragments.forEach {
            if (it is HasFabMenu && it.isAdded && !it.isHidden) {
                show = show.or(handleFragmentFabMenu(it, fabMenu!!))
            }
        }
        return show
    }

    private fun handleFragmentFabMenu(fragment: Fragment, menu: Menu): Boolean {
        val show = when (fragment) {
            is HasFabMenu -> fragment.onCreateFabMenu(menu)
            else -> false
        }
        return show.or(dispatchFragmentCreateFabMenu(fragment.childFragmentManager.fragments, menu))
    }


    private fun buildMenu(menu: Menu) {
        fabMenuItems.clear()
        for (i in 0.until(menu.size())) {
            val item = menu.getItem(i)
            val button = createMenuItemFab(item)
            val menuItem: FabMenuItem

            val closedPosition = FabMenuItemPosition(button.x, button.y)
            menuItem = FabMenuItem(button)
            menuItem.setAnimations(fabMenuPattern, closedPosition, i, menu.size())

            fabMenuContainer?.addView(button)
            fabMenuItems.add(menuItem)
        }
    }

    private fun createMenuItemFab(menuItem: MenuItem): FloatingActionButton {
        val newButton = FloatingActionButton(this)
        newButton.size = FloatingActionButton.SIZE_MINI
        newButton.setImageDrawable(menuItem.icon)
        newButton.x = fabMenuAnchor!!.x
        newButton.y = fabMenuAnchor!!.y

        newButton.setOnClickListener {
            onOptionsItemSelected(menuItem)
//                if (fragment == null) {
//                    activity.onOptionsItemSelected(menuItem)
//                } else {
//                    fragment?.onOptionsItemSelected(menuItem)
//                }
        }

        return newButton
    }


}


