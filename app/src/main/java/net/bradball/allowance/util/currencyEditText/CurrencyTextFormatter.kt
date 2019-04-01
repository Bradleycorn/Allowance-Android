package net.bradball.allowance.util.currencyEditText


import android.util.Log
import net.bradball.allowance.util.empty

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyTextFormatter {
    private const val TAG = "CurrencyTextFormatter"

    val defaultLocale = Locale.US

    @JvmOverloads
    fun formatText(value: String, locale: Locale, decimalDigits: Int? = null): String {

        //special case for the start of a negative number
        if (value == "-") return value

        val currencyDecimalDigits = decimalDigits ?: getDecimalDigits(locale)

        //retain information about the negativity of the value before stripping all non-digits
        val isNegative = (value.contains("-"))

        //strip all non-digits so the formatter always has a 'clean slate' of numbers to work with
        var formattedValue = value.replace("[^\\d]".toRegex(), "")
        //if there's nothing left, that means we were handed an empty string. Also, cap the raw input so the formatter doesn't break.
        if (formattedValue != String.empty) {

            //if we're given a value that's smaller than our decimal location, pad the value.
            if (formattedValue.length <= currencyDecimalDigits) {
                val formatString = "%" + currencyDecimalDigits + "s"
                formattedValue = String.format(formatString, formattedValue).replace(' ', '0')
            }

            //place the decimal in the proper location to construct a double which we will give the formatter.
            //This is NOT the decimal separator for the currency value, but for the double which drives it.
            val preparedVal = StringBuilder(formattedValue).insert(formattedValue.length - currencyDecimalDigits, '.').toString()

            //Convert the string into a double, which will be passed into the currency formatter
            var newTextValue = preparedVal.toDoubleOrNull() ?: 0.0

            //reapply the negativity
            newTextValue *= (if (isNegative) -1 else 1).toDouble()

            //finally, do the actual formatting
            formattedValue = formatNumber(newTextValue, locale, decimalDigits)
        } else {
            throw IllegalArgumentException("Invalid amount of digits found (either zero or too many) in argument `value`")
        }
        return formattedValue
    }

    private fun getCurrencyFormatter(locale: Locale, decimalDigits: Int): DecimalFormat {
        val currencyFormatter: DecimalFormat = try {
            DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
        } catch (e: Exception) {
            try {
                Log.e(TAG, "Error detected for locale: $locale, falling back to default value: $defaultLocale")
                DecimalFormat.getCurrencyInstance(defaultLocale) as DecimalFormat
            } catch (e1: Exception) {
                Log.e(TAG, "Error detected for defaultLocale: $defaultLocale, falling back to USD.")
                DecimalFormat.getCurrencyInstance(Locale.US) as DecimalFormat
            }

        }

        currencyFormatter.minimumFractionDigits = decimalDigits
        return currencyFormatter
    }

    fun formatNumber(currency: Double, locale: Locale, decimalDigits: Int? = null): String {
        val currencyDecimalDigits = decimalDigits ?: getDecimalDigits(locale)
        val formatter = getCurrencyFormatter(locale, currencyDecimalDigits)
        return formatter.format(currency)
    }

    private fun getDecimalDigits(locale: Locale): Int {
        val currency = Currency.getInstance(locale)
        return try {
            currency.defaultFractionDigits
        } catch (e: Exception) {
            Log.e(TAG, "Illegal argument detected for currency: $currency, using currency from defaultLocale: $defaultLocale")
            Currency.getInstance(defaultLocale).defaultFractionDigits
        }

    }

}
