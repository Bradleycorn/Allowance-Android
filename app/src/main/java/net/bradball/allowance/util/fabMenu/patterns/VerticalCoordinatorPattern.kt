package net.bradball.allowance.util.fabMenu.patterns

import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import net.bradball.allowance.util.fabMenu.DEFAULT_ANIMATION_DURATION
import net.bradball.allowance.util.fabMenu.FabMenuItemPosition
import net.bradball.allowance.util.fabMenu.FabMenuPattern
import net.bradball.allowance.util.dpToPx

class VerticalCoordinatorPattern(private val animationDuration: Long = DEFAULT_ANIMATION_DURATION) : FabMenuPattern() {

    override fun getClosingAnimation(element: View, itemClosedPosition: FabMenuItemPosition): AnimatorSet {
        val anim = AnimatorSet()

        element.alpha = 1f
        val fabAlpha = ObjectAnimator.ofFloat(element, View.ALPHA, 0f)
        fabAlpha.duration = animationDuration

        when (itemClosedPosition.y != null) {
            true -> anim.play(fabAlpha).with(ObjectAnimator.ofFloat(element, View.Y, itemClosedPosition.y!!).apply { duration = animationDuration })
            else -> anim.play(fabAlpha)
        }
        return anim
    }

    override fun getOpeningAnimation(element: View, itemOpenPosition: FabMenuItemPosition): AnimatorSet {
        val anim = AnimatorSet()

        element.alpha = 0f
        val fabAlpha = ObjectAnimator.ofFloat(element, View.ALPHA, 1f)
        fabAlpha.duration = animationDuration

        when (itemOpenPosition.y != null) {
            true -> anim.play(fabAlpha).with(ObjectAnimator.ofFloat(element, View.Y, itemOpenPosition.y!!).apply { duration = animationDuration })
            else -> anim.play(fabAlpha)
        }
        return anim
    }

    override fun getClosedPosition(menuItem: View, anchor: FloatingActionButton, order: Int, menuSize: Int): FabMenuItemPosition {
        val top = getYPosition(anchor, order) + 16.dpToPx()
        val layoutParams = getLayoutParams()

        return FabMenuItemPosition(y = top, layoutParams = layoutParams)
    }

    override fun getOpenPosition(menuItem: View, anchor: FloatingActionButton, order: Int, menuSize: Int): FabMenuItemPosition {
        val top = getYPosition(anchor, order)
        val layoutParams = getLayoutParams()

        return FabMenuItemPosition(y = top, layoutParams = layoutParams)
    }

    private fun getYPosition(anchor: FloatingActionButton, order: Int): Float {
        return anchor.y - (order * 56.dpToPx())
    }

    private fun getLayoutParams(): CoordinatorLayout.LayoutParams {
        val params = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.END
        params.marginEnd = 39.dpToPx().toInt()
        return params
    }
}