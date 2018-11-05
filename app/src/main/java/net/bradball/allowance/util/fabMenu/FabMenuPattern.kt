package net.bradball.allowance.util.fabMenu

import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Interface that custom menu pattern must implement.
 */
abstract class FabMenuPattern : FabMenuTransition() {
    abstract fun getClosedPosition(menuItem: View, anchor: FloatingActionButton, order: Int, menuSize: Int): FabMenuItemPosition
    abstract fun getOpenPosition(menuItem: View, anchor: FloatingActionButton, order: Int, menuSize: Int): FabMenuItemPosition
}

data class FabMenuItemPosition(
        val x: Float? = null,
        val y: Float? = null,
        val layoutParams: ViewGroup.LayoutParams? = null)