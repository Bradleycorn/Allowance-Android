package net.bradball.allowance.util.fabMenu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.support.DaggerAppCompatActivity
import net.bradball.allowance.R
import net.bradball.allowance.util.dpToPx
import net.bradball.allowance.util.fabMenu.patterns.VerticalCoordinatorPattern

abstract class FabMenuActivity: DaggerAppCompatActivity() {

    private var fabMenu: Menu? = null
    private var fabMenuIsValid = false
    private var fabMenuAnchor: FloatingActionButton? = null
    protected var fabMenuAnchorTransition = FabMenuTransition()
    private var isFabMenuOpen = false
    private var fabMenuContainer: ViewGroup? = null
    private val fabMenuItems: MutableList<FabMenuItem> = mutableListOf()
    protected var fabMenuPattern = VerticalCoordinatorPattern()
    private var fabMenuOverlay: View? = null

    protected fun showFabMenu() = fabMenuAnchor?.show()
    protected fun hideFabMenu() = fabMenuAnchor?.hide()

    protected fun setFabMenuAnchor(view: FloatingActionButton) {
        fabMenuAnchor = view
        fabMenuAnchor?.setOnClickListener {
            handleFabClick()
        }

        if (fabMenuContainer == null) {
            fabMenuContainer = view.parent as ViewGroup
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, true)
    }

    protected fun setFabMenuContainer(viewGroup: ViewGroup) {
        fabMenuContainer = viewGroup
        //TODO = If we change the menu container, we should probably clear out any existing menu
    }

    protected fun setFabMenuOverlay(view: View?) {
        if (fabMenuOverlay != null) {
            fabMenuOverlay!!.setOnClickListener(null)
        }

        fabMenuOverlay = view

        if (fabMenuOverlay != null) {
            fabMenuOverlay!!.setOnClickListener {
                if (it.visibility == View.VISIBLE) {
                    closeMenu()
                }
            }
        }
    }

    fun onCreateFabMenu(menu: Menu): Boolean {
        return false
    }

    private fun handleFabClick() {
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
            val animBuilder = anim.play(fabMenuAnchorTransition.getOpeningAnimation(fabMenuAnchor!!, FabMenuItemPosition(x=fabMenuAnchor!!.x, y=fabMenuAnchor!!.y)))
            fabMenuItems.forEach {
                it.miniFab.alpha = 0F
                it.miniFab.visibility = View.VISIBLE
                animBuilder.with(it.openingAnimation)
            }
            showFabOverlay()
            anim.start()
            isFabMenuOpen = true
        }
    }

    fun closeMenu() {
        if (fabMenuItems.isNotEmpty()) {
            //Do the animation before removing the view
            val anim = AnimatorSet()
            val animBuilder = anim.play(fabMenuAnchorTransition.getClosingAnimation(fabMenuAnchor!!,  FabMenuItemPosition(x=fabMenuAnchor!!.x, y=fabMenuAnchor!!.y)))
            for (fabMenuItem in fabMenuItems) {
                animBuilder.with(fabMenuItem.closingAnimation)
            }
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    fabMenuItems.forEach { fabMenuContainer?.removeView(it.miniFab) }
                }
            })
            hideFabOverlay()
            anim.start()
        }
        isFabMenuOpen = false
    }

    private val fragmentCallbacks = object: FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is IHasFabMenu) {
                invalidateFabMenu()
                showFabMenu()
            } else {
                hideFabMenu()
            }
        }

        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentViewDestroyed(fm, f)
            if (f is IHasFabMenu) {
                invalidateFabMenu()
            }
        }

    }

    private fun validateAndCreateFabMenu(): Boolean {
        var show = (fabMenu != null && fabMenuIsValid)
        if (fabMenu == null || !fabMenuIsValid) {
            show = dispatchCreateFabMenu()
            val fragmentList = this.supportFragmentManager.fragments
            val showFragments = dispatchFragmentCreateFabMenu(fragmentList, fabMenu!!)
            if (showFragments) {
                supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
                supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, true)
            }
            show = show.or(showFragments)
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
            if (it.isAdded && !it.isHidden) {
                show = show.or(handleFragmentFabMenu(it, fabMenu!!))
            }
        }
        return show
    }

    private fun handleFragmentFabMenu(fragment: Fragment, menu: Menu): Boolean {
        val show = when (fragment) {
            is IHasFabMenu -> {
                fragment.onCreateFabMenu(menu, menuInflater)
            }
            else -> false
        }
        return show.or(dispatchFragmentCreateFabMenu(fragment.childFragmentManager.fragments, menu))
    }


    private fun buildMenu(menu: Menu) {
        fabMenuItems.clear()
        for (i in 0.until(menu.size())) {
            val item = menu.getItem(i)
            val button = createMenuItemFab(item, i+1)
            val menuItem: FabMenuItem

            button.alpha = 0F
            (button as View).visibility = View.GONE
            fabMenuContainer!!.addView(button)

            val closedPosition = fabMenuPattern.getClosedPosition(button, fabMenuAnchor!!, i+1, menu.size())
            val openPosition = fabMenuPattern.getOpenPosition(button, fabMenuAnchor!!, i + 1, menu.size())

            with(closedPosition) {
                if (x != null) button.x = x
                if (y != null) button.y = y
                if (layoutParams != null) {
                    button.layoutParams = layoutParams
                }
            }

            menuItem = FabMenuItem(button)
            menuItem.setAnimations(fabMenuPattern, closedPosition, openPosition)

            fabMenuItems.add(menuItem)
        }
    }

    private fun createMenuItemFab(menuItem: MenuItem, itemOrder: Int): View {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.HORIZONTAL
        layout.setVerticalGravity(Gravity.CENTER_VERTICAL)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layout.layoutParams = layoutParams

        if (!menuItem.title.isNullOrEmpty()) {
            val chip = Chip(this, null, R.style.Widget_MaterialComponents_Chip_Action)
            val chipLayout = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            chipLayout.height = 20.dpToPx().toInt()
            chipLayout.marginEnd = 16.dpToPx().toInt()
            chip.layoutParams = chipLayout
            chip.isCheckable = false
            chip.chipCornerRadius = 2.dpToPx()
            chip.text = menuItem.title
            chip.textEndPadding = 0F
            chip.textStartPadding = 0F

            layout.addView(chip)
        }


        val newButton = FloatingActionButton(this)
        newButton.id = menuItem.itemId
        newButton.size = FloatingActionButton.SIZE_MINI
        newButton.setImageDrawable(menuItem.icon)

        if (Build.VERSION.SDK_INT >= 26 && menuItem.iconTintList != null) {
            newButton.imageTintList = menuItem.iconTintList
        }

        newButton.setOnClickListener {

            onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, menuItem)
            closeMenu()
        }
        layout.addView(newButton)

        return layout
    }

    /**
     * Hide fab overlay with animation
     */
    private fun hideFabOverlay() {
        if (fabMenuOverlay != null) {
            fabMenuOverlay!!.animate().alpha(0.0f).withLayer().setDuration(300).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    fabMenuOverlay?.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    fabMenuOverlay?.visibility = View.GONE
                }
            }).start()
        }
    }

    /**
     * Show fab overlay with animation
     */
    private fun showFabOverlay() {
        if (fabMenuOverlay != null) {
            fabMenuOverlay!!.animate().alpha(1.0f).withLayer().setDuration(300).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    fabMenuOverlay!!.visibility = View.VISIBLE
                }
            }).start()
        }
    }
}
