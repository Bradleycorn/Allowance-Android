package net.bradball.allowance.util.fabMenu

import android.animation.AnimatorSet
import android.content.res.ColorStateList
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

interface HasFabMenu {
    fun onCreateFabMenu(menu: Menu): Boolean = false
}

abstract class FabActivity: DaggerAppCompatActivity(), HasFabMenu {

    private var fabMenu: Menu? = null
    private var fabMenuIsValid = false
    private var fabMenuAnchor: FloatingActionButton? = null


    protected fun setFabMenuAnchor(view: FloatingActionButton) {
        fabMenuAnchor = view
        fabMenuAnchor?.setOnClickListener {
            handleFabClick()
        }
    }

    private fun handleFabClick() {
        //TODO: handle fab open vs close click

        if (validateAndCreateFabMenu()) {
            buildMenu(fabMenu!!)
        }
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
        var show = when (fragment) {
            is HasFabMenu -> fragment.onCreateFabMenu(menu)
            else -> false
        }
        return show.or(dispatchFragmentCreateFabMenu(fragment.childFragmentManager.fragments, menu))
    }


    private fun buildMenu(menu: Menu) {
        for (i in 0.until(menu.size())) {
            val item = menu.getItem(i)
            val subItem: SubFABMenu
            val newButton = FloatingActionButton(this)
            newButton.setImageDrawable(item.icon)
            newButton.x = fab!!.x
            newButton.y = fab!!.y

            newButton.setOnClickListener {
                onOptionsItemSelected(item)
//                if (fragment == null) {
//                    activity.onOptionsItemSelected(item)
//                } else {
//                    fragment?.onOptionsItemSelected(item)
//                }
            }
            subItem = positionButton(newButton, i, menu.size())
            menuViews.add(subItem)
        }
        showFabOverlay()
        isOpen = true
    }
}

data class SubFABMenu(val subFAB: View, var openingAnimation: AnimatorSet? = null, var closingAnimation: AnimatorSet? = null)

