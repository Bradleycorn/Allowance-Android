package net.bradball.allowance.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import net.bradball.allowance.data.store.DataStoreRecord
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


val String.Companion.empty: String
    get() = ""

fun Int.pxToDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.dpToPx(): Float = (this * Resources.getSystem().displayMetrics.density)

fun Int.spToPx(): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics)

fun <T : View> Activity.lazyView(@IdRes idRes: Int): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) }
}

fun <T : View> View.lazyView(@IdRes idRes: Int): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) }
}


fun Activity.lazyNavController(@IdRes idRes: Int): Lazy<NavController> {
    return lazy(LazyThreadSafetyMode.NONE) { findNavController(idRes) }
}

fun Fragment.lazyNavController(): Lazy<NavController> {
    return lazy(LazyThreadSafetyMode.NONE) { NavHostFragment.findNavController(this) }
}

enum class DATE_FORMAT(val value: String) {
    SHORT("MM/dd/yyyy"),
    LONG("MMMM dd, yyyy")
}

fun String.parseDate(format: DATE_FORMAT): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(format.value)
    return LocalDate.parse(this, formatter)
}
fun LocalDate.format(format: DATE_FORMAT = DATE_FORMAT.SHORT): String {
    return format(DateTimeFormatter.ofPattern(format.value))
}

fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant())
}

fun Date.toLocalDate(): LocalDate {
    return this.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
}



fun TextInputEditText.getValue(): String = this.text.toString()

fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}