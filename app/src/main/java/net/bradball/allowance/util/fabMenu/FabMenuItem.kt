package net.bradball.allowance.util.fabMenu

import android.animation.AnimatorSet
import android.view.View

data class FabMenuItem(val miniFab: View, var openingAnimation: AnimatorSet? = null, var closingAnimation: AnimatorSet? = null) {
    fun setAnimations(pattern: FabMenuPattern, itemClosedPosition: FabMenuItemPosition, itemOpenPosition: FabMenuItemPosition) {
        closingAnimation = pattern.getClosingAnimation(miniFab, itemClosedPosition)
        openingAnimation = pattern.getOpeningAnimation(miniFab, itemOpenPosition)
    }
}
