package net.bradball.allowance.util.fabMenu

import android.animation.AnimatorSet
import android.view.View

data class FabMenuItem(val miniFab: View, var openingAnimation: AnimatorSet? = null, var closingAnimation: AnimatorSet? = null) {
    fun setAnimations(pattern: FabMenuPattern, itemClosedPosition: FabMenuItemPosition, itemOrder: Int, menuSize: Int) {
        val openPosition = pattern.getFinalPosition(miniFab, itemClosedPosition.x, itemClosedPosition.y, itemOrder, menuSize)
        closingAnimation = pattern.getClosingAnimation(miniFab, itemClosedPosition.y, itemClosedPosition.x)
        openingAnimation = pattern.getOpeningAnimation(miniFab, openPosition!!.x, openPosition.y)
    }
}
