package net.bradball.allowance.util.currencyEditText

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.adapters.Converters
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.google.android.material.textfield.TextInputEditText
import net.bradball.allowance.R
import net.bradball.allowance.util.empty
import java.math.BigDecimal

import java.util.Currency
import java.util.Locale
import kotlin.math.pow

allowNegativeValues = array.getBoolean(R.styleable.CurrencyEditText_allow_negative_values, false)
decimalDigits = array.getInteger(R.styleable.CurrencyEditText_decimal_digits, decimalDigits)
currencyValue = array.getFloat(R.styleable.CurrencyEditText_currency_value, currencyValue)

@BindingMethods(value = [
    BindingMethod(
            type = CurrencyEditText::class,
            attribute = "app:currencyWatcher",
            method = "addCurrencyWatcher")])
/**
 * A view for showing an edit text with some opinions on currency formatting.
 */
class CurrencyEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    /**
     * The currently held default Locale to fall back on in the event of a failure with the currentLocale field (typically
     * due to the locale being set to a non-standards-compliant value.
     *
     * Override the locale to be used in the event that the users device locale is not ISO 3166 compliant.
     * Defaults to Locale.US.
     * NOTE: Be absolutely sure that this value is supported by ISO 3166. See
     * Java.util.Locale.getISOCountries() for a list of currently supported ISO 3166 locales (note that this list
     * may not be identical on all devices)
     * @param locale The fallback locale used to recover gracefully in the event of the current locale value failing.
     */
    var defaultLocale = Locale.US

    /**
     * The current locale used by this instance of CurrencyEditText. By default, will be the users
     * device locale unless that locale is not ISO 3166 compliant, in which case the defaultLocale will
     * be used.
     *
     * When setting, will also update the hint text if a custom hint was not provided.
     *
     * IMPORTANT - This setter does NOT update the currently set Currency object used by
     * this CurrencyEditText instance. If your use case dictates that Currency and Locale
     * should never break from their default pairing, use 'configureViewForLocale(locale)' instead
     * of this method.
     * @param locale The deviceLocale to set the CurrencyEditText box to adhere to.
     * @return the Locale object for the given users configuration
     */
    var locale: Locale = CurrencyUtil.retrieveLocale(context, defaultLocale)
        set(locale) {
            field = locale
            refreshView()
        }


    /**
     * The number of decimal digits this CurrencyEditText instance is currently configured
     * to use. This value will be based on the current Currency object by default
     *
     * Allows  overriding
     * the number of digits specified by the current currency. Note, however,
     * that calls to setCurrency() and configureViewForLocale() will override this value.
     *
     * Note that this method will also invoke the formatter to update the current view if the current
     * value is not null/empty.
     *
     * @param digits The number of digits to be shown following the decimal in the formatted text.
     * Value must be between 0 and 340 (inclusive).
     * @throws IllegalArgumentException If provided value does not fall within the range (0, 340) inclusive.
     */
    var decimalDigits = 0
        private set(digits) {
            if (digits < 0 || digits > 340) {
                throw IllegalArgumentException("Decimal Digit value must be between 0 and 340")
            }
            field = digits

            refreshView()
        }


    /**
     * Whether or not to allow negative input values
     */
    var allowNegativeValues = false


    /**
     * Whether or not to show cents
     */
    var showCents = true

    /**
     * Retrieve the raw value that was input by the user in their currencies lowest denomination (e.g. pennies).
     *
     * IMPORTANT: Remember that the location of the decimal varies by currentCurrency/Locale. This method
     * returns the raw given value, and does not account for locality of the user. It is up to the
     * calling application to handle that level of conversion.
     * For example, if the text of the field is $13.37, this method will return a long with a
     * value of 1337, as penny is the lowest denomination for USD. It will be up to the calling
     * application to know that it needs to handle this value as pennies and not some other denomination.
     *
     * @return The raw value that was input by the user, in the lowest denomination of that users
     * deviceLocale.
     */
    var rawValue = 0L
        internal set

    val currencyValue: BigDecimal
        get() {
            val unscaledValue = BigDecimal(rawValue.toString()).asCurrency()
            val scale = BigDecimal((10.0).pow(decimalDigits)).asCurrency()
            return (unscaledValue / scale).asCurrency()
        }


    /**
     * An instance of CurrencyTextWatcher that is used to update the value on user input.
     */
    private var textWatcher: CurrencyTextWatcher = CurrencyTextWatcher(this)



    /**
     * Convenience method to get the current Hint back as a string rather than a CharSequence
     */
    private val hintString: String
        get() {
            return super.getHint()?.toString() ?: String.empty
        }

    /**
     * Cache the original hintString, so that we know if we need to update it when changing locales.
     *
     * If the user has entered a custom hint, we won't update on locale change.
     */
    private val hintCache = hintString

    /**
     * Setup a default hint value based on the current locale.
     */
    private val defaultHintValue: String
        get() {
            try {
                return Currency.getInstance(locale).symbol
            } catch (e: Exception) {
                Log.w("CurrencyEditText", String.format("An error occurred while getting currency symbol for hint using locale '%s', falling back to defaultLocale", locale))
                try {
                    return Currency.getInstance(defaultLocale).symbol
                } catch (e1: Exception) {
                    Log.w("CurrencyEditText", String.format("An error occurred while getting currency symbol for hint using default locale '%s', falling back to USD", defaultLocale))
                    return Currency.getInstance(Locale.US).symbol
                }

            }

        }


    init {
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED

        // process attributes
        val array = context.obtainStyledAttributes(attrs, R.styleable.CurrencyEditText)
        allowNegativeValues = array.getBoolean(R.styleable.CurrencyEditText_allow_negative_values, false)
        showCents = array.getBoolean(R.styleable.CurrencyEditText_show_cents, true)
        array.recycle()

        val currentCurrency = getCurrencyForLocale(locale)
        decimalDigits =  if (showCents) currentCurrency.defaultFractionDigits else 0

        addTextChangedListener(textWatcher)
        updateHint()
    }


    /**
     * Sets the value to be formatted and displayed in the CurrencyEditText view.
     *
     * @param value - The value to be converted, represented in the target currencies lowest denomination (e.g. pennies).
     */
    fun setValue(value: BigDecimal) {
        val formattedText = format(value)
        setText(formattedText)
    }



    /**
     * Sets up the CurrencyEditText view to be configured for a given locale, using that
     * locales default currency (so long as the locale is ISO-3166 compliant). If there is
     * an issue retrieving the locales currency, the defaultLocale field will be used.
     *
     * This is the most 'fool proof' way of configuring a CurrencyEditText view when not
     * relying on the default implementation, and is the recommended approach for handling
     * locale/currency setup if you choose not to rely on the default behavior.
     *
     * Note that this method will set the decimalDigits field, potentially overriding
     * values from previous setDecimalDigits calls.
     */
    fun configureViewForLocale(newLocale: Locale) {
        locale = newLocale
        val currentCurrency = getCurrencyForLocale(locale)
        decimalDigits = currentCurrency.defaultFractionDigits
        refreshView()
    }


    /*
    PRIVATE HELPER METHODS
     */

    private fun refreshView() {
        setText(format(rawValue))
        updateHint()
    }

    private fun format(value: Long): String {
        return format(value.toString())
    }

    private fun format(value: BigDecimal): String {
        return format(value.asCurrency().toString())
    }

    private fun format(value: String): String {
        return CurrencyTextFormatter.formatText(value, locale, defaultLocale, decimalDigits)
    }

    private fun updateHint() {
        if (hintCache == String.empty) {
            hint = defaultHintValue
        }
    }

    private fun getCurrencyForLocale(someLocale: Locale?): Currency {
        var currency: Currency
        try {
            currency = Currency.getInstance(someLocale)
        } catch (e: Exception) {
            try {
                Log.w("CurrencyEditText", String.format("Error occurred while retrieving currency information for locale '%s'. Trying default locale '%s'...", locale, defaultLocale))
                currency = Currency.getInstance(defaultLocale)
            } catch (e1: Exception) {
                Log.e("CurrencyEditText", "Both device and configured default locales failed to report currentCurrency data. Defaulting to USD.")
                currency = Currency.getInstance(Locale.US)
            }

        }

        return currency
    }
}