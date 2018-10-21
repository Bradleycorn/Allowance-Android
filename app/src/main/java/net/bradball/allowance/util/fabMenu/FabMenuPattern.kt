package net.bradball.allowance.util.fabMenu

import android.view.View

/**
 * Interface that custom menu pattern must implement.
 */
interface FabMenuPattern : FabMenuTransition {
    fun getFinalPosition(subMenu: View, fabX: Float, fabY: Float, position: Int, menuSize: Int): FabMenuItemPosition
}

data class FabMenuItemPosition(val x: Float, val y: Float)