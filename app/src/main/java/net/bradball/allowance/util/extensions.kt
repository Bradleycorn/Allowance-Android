package net.bradball.allowance.util

import android.content.res.Resources
import android.util.TypedValue



val String.Companion.empty: String
    get() = ""

fun Int.pxToDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.dpToPx(): Float = (this * Resources.getSystem().displayMetrics.density)

fun Int.spToPx(): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics)

