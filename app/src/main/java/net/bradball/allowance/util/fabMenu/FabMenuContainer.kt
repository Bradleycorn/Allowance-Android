package net.bradball.allowance.util.fabMenu

import android.view.Menu
import android.view.MenuInflater
import android.widget.PopupMenu
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.support.DaggerAppCompatActivity

interface FabMenuContainer {

    private var fabMenu: Menu?

    private var anchor: FloatingActionButton? = null


    protected  fun setAnchor(view: FloatingActionButton) {
        anchor = view
    }

}

abstract class FabActivity: DaggerAppCompatActivity() {

    private var fabMenu: Menu? = null
    private var fabMenuIsValid = true
    private var fabMenuAnchor: FloatingActionButton? = null


    protected fun setFabMenuAnchor(view: FloatingActionButton) {
        fabMenuAnchor = view
        fabMenuAnchor?.setOnClickListener {
            handleFabClick()
        }
    }

    private fun handleFabClick() {
        //TODO: handle fab open vs close click

        var show = false
        if (fabMenu == null || !fabMenuIsValid) {
            show = dispatchCreateFabMenu()
            val fragmentList = this.supportFragmentManager.fragments
            fragmentList.forEach {
                 if (it.isAdded && !it.isHidden) {
                    show |= it.dispatchCreateFabMenu(fabMenu!!)
                }
            }
        }

        if (show) {
            buildMenu()
        }
    }

    fun dispatchCreateFabMenu(): Boolean {
        fabMenu = fabMenu?.apply { clear() } ?: PopupMenu(this, fabMenuAnchor).menu
        fabMenuIsValid = true
        return onCreateFabMenu(fabMenu!!)
    }

    abstract fun onCreateFabMenu(fabMenu: Menu): Boolean

    fun invalidateFabMenu() {
        fabMenuIsValid = false
    }

    private fun buildMenu() {

    }
}

class BaseFragment: Fragment() {


    @CallSuper
    fun onCreateFabMenu(fabMenu: Menu) {
        this.fragmentManager?.
    }



    internal fun performCreateOptionsMenu(menu: Menu, inflater: MenuInflater): Boolean {
        var show = false
        if (!this.isHidden) {
            if (mHasMenu && mMenuVisible) {
                show = true
                onCreateOptionsMenu(menu, inflater)
            }
            if (mChildFragmentManager != null) {
                show = show or mChildFragmentManager.dispatchCreateOptionsMenu(menu, inflater)
            }
        }
        return show
    }

}