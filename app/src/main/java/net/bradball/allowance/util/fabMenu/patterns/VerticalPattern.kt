package net.bradball.allowance.util.fabMenu.patterns

import android.view.View.Y
import android.animation.ObjectAnimator
import android.view.View.X
import android.animation.AnimatorSet
import android.view.View
import net.bradball.allowance.util.fabMenu.DEFAULT_ANIMATION_DURATION
import net.bradball.allowance.util.fabMenu.FabMenuItemPosition
import net.bradball.allowance.util.fabMenu.FabMenuPattern

class VerticalPattern(private val duration: Long = DEFAULT_ANIMATION_DURATION) : FabMenuPattern {

    override fun getClosingAnimation(element: View, destX: Float, destY: Float): AnimatorSet {
        val anim = AnimatorSet()
        val fabX = ObjectAnimator.ofFloat(element, View.X, destX)
        fabX.duration = duration
        val fabY = ObjectAnimator.ofFloat(element, View.Y, destY)
        fabY.duration = duration

        anim.play(fabX).with(fabY)
        return anim
    }

    override fun getOpeningAnimation(element: View, destX: Float, destY: Float): AnimatorSet {
        val anim = AnimatorSet()
        val fabX = ObjectAnimator.ofFloat(element, View.X, destX)
        fabX.duration = duration
        val fabY = ObjectAnimator.ofFloat(element, View.Y, destY)
        fabY.duration = duration

        anim.play(fabX).with(fabY)
        return anim
    }

    override fun getFinalPosition(subMenu: View, fabX: Float, fabY: Float, position: Int, menuSize: Int): FabMenuItemPosition {
        return FabMenuItemPosition(fabX, fabY - (position + 1) * 200)
    }
}