package net.bradball.allowance.util

import android.widget.EditText
import androidx.databinding.InverseMethod

object Converters {
    @InverseMethod("floatToDouble")
    @JvmStatic fun doubleToFloat(value: Double) = value.toFloat()
    @JvmStatic fun floatToDouble(value: Float) = value.toDouble()
}