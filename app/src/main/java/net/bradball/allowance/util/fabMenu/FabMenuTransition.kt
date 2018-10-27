package net.bradball.allowance.util.fabMenu

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

open class FabMenuTransition(private val duration: Long = DEFAULT_ANIMATION_DURATION) {

    open fun getOpeningAnimation(element: View, itemOpenPosition: FabMenuItemPosition): AnimatorSet {
        val anim = AnimatorSet()
        val rotateMainFAB = ObjectAnimator.ofFloat<View>(element, View.ROTATION, 0f)
        rotateMainFAB.duration = duration
        anim.play(rotateMainFAB)
        return anim
    }

    open fun getClosingAnimation(element: View, itemClosedPosition: FabMenuItemPosition): AnimatorSet {
        val anim = AnimatorSet()
        val rotateMainFAB = ObjectAnimator.ofFloat<View>(element, View.ROTATION, 0f)
        rotateMainFAB.duration = duration
        anim.play(rotateMainFAB)
        return anim
    }
}