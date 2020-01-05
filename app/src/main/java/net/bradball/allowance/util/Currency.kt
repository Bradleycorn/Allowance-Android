package net.bradball.allowance.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

data class Currency(private val value: String): Comparable<Currency> {
    constructor(value: Number): this(value.toString())

    private val bigD: BigDecimal
    init {
        val numberString = value.replace("$", String.empty)
                .replace(" ", String.empty)
                .replace(",", String.empty)
            bigD = BigDecimal(numberString).setScale(2, RoundingMode.HALF_UP)
    }

    override fun toString(): String {
        return format(showSymbol = false, showCents = true, groupDollars = false)
    }

    fun toBigDecimal(): BigDecimal = bigD

    operator fun plus(currency: Currency): Currency {
        val newValue = bigD + currency.toBigDecimal()
        return Currency(newValue.toString())
    }
    operator fun plus (number: Number): Currency = this + Currency(number)

    operator fun minus(currency: Currency): Currency {
        val newValue = bigD - currency.toBigDecimal()
        return Currency(newValue.toString())
    }
    operator fun minus (number: Number): Currency = this - Currency(number)

    operator fun times(currency: Currency): Currency {
        val newValue = bigD * currency.toBigDecimal()
        return Currency(newValue.toString())
    }
    operator fun times (number: Number): Currency = this * Currency(number)

    override fun compareTo(other: Currency): Int {
        return bigD.compareTo(other.toBigDecimal())
    }

    /**
     * returns a new Currency value that is a whole dollar amount equal
     * to the un-rounded whole dollar amount of this Currency value.
     * For example, dollars on 6.73 is 6.00.
     * If you want the dollars rounded, see [rounded]
     */
    val dollars: Currency
        get() =  Currency(bigD.toBigInteger().toString())

    /**
     * returns a new Currency value that contains the cents from this
     * Currency value.
     * For example, cents on 6.73 is 0.73.
     */
    val cents: Currency
        get() = Currency(bigD.remainder(BigDecimal.ONE).toString())


    /**
     * rounds this Currency value to the nearest whole dollar amount
     * and returns it as a new currency.
     * For example, rounded on 6.73 is 7.00.
     * If you want the un-rounded dollar amount, see [dollars]
     */
    val rounded: Currency
        get() = Currency(format(false, false, false))


    /**
     * Returns this Currency value as a string, formatted according to the given parameters.
     *
     * @param showSymbol - true to prefix the output string with the currency symbol ($).
     *  Defaults to true
     *
     * @param showCents - true to include the cents value in the formatted string, false to omit
     *  and only show dollars. If false, the amount will be rounded to the nearest dollar.
     *  For example, 6.73 with showCents false will return: "$7"
     *
     * @param groupDollars - true to group hundreds of dollars with a comma.
     *  Defaults to true
     *  For example, using groupDollars, 6735.00 will return "$6,735.00"
     *
     *  @return a string representing this Currency value.
     */
    fun format(showSymbol: Boolean = true, showCents: Boolean = true, groupDollars: Boolean = true): String {
        val decimals = if (showCents) 2 else 0

        val formatter = NumberFormat.getCurrencyInstance(Locale.US).apply {
            roundingMode = RoundingMode.HALF_UP
            minimumFractionDigits = decimals
            maximumFractionDigits = decimals
            minimumIntegerDigits = 1
            isGroupingUsed = groupDollars
        }
        val symbol = formatter.currency.symbol

        var formattedString = formatter.format(bigD)

        if (!showSymbol) {
            formattedString = formattedString.replace(symbol, String.empty)
        }

        return formattedString
    }
}


/**
 * Parse a string or null to a BigDecimal representation of currency.
 * If the value is not a number, null will be returned.
 *
 * The returned BigDecimal will accurately represent a currency value
 * by having its scale (decimal places) set to 2, and it's rounding mode
 * set to "half up", so that values with more than 2 decimals will use
 * rounding to "truncate" down to 2 decimals.
 *
 * For example, "0.23587123" will become 0.24
 *      and     "0.23123456" will become 0.23
 *
 * @return BigDecimal containing the value, with scale and rounding set,
 *          or null if the value is not a number.
 */
fun String?.asCurrency(): BigDecimal? {
    if (this == null) return null
    return try {
        val numberString = this
                .replace("$", String.empty)
                .replace(" ", String.empty)
                .replace(",", String.empty)
        BigDecimal(numberString).setScale(2, RoundingMode.HALF_UP)
    } catch (ex: NumberFormatException) {
        null
    }
}


/**
 * Parse a string or null to a BigDecimal representation of currency,
 *  or use the passed in default value if the string is not a number.
 *
 * The returned BigDecimal will accurately represent a currency value
 * by having its scale (decimal places) set to 2, and it's rounding mode
 * set to "half up", so that values with more than 2 decimals will use
 * rounding to "truncate" down to 2 decimals.
 *
 * For example, "0.23587123" will become 0.24
 *      and     "0.23123456" will become 0.23
 *
 * @return BigDecimal containing the value, with scale and rounding set,
 *          or null if the value is not a number.
 */
fun String?.asCurrency(defaultVal: Number): BigDecimal {
    return when (this) {
        null -> defaultVal.asCurrency()
        else -> this.asCurrency() ?: defaultVal.asCurrency()
    }
}


/**
 * Convert a number to a BigDecimal representation of currency.
 *
 * The returned BigDecimal will accurately represent a currency value
 * by having its scale (decimal places) set to 2, and it's rounding mode
 * set to "half up", so that values with more than 2 decimals will use
 * rounding to "truncate" down to 2 decimals.
 *
 * For example, 0.23587123 will become 0.24
 *      and     0.23123456 will become 0.23
 *      and     0.2        will become 0.20
 *
 * @return BigDecimal containing the value. It will have a scale (decimal places) of 2
 *              and be set to round additional decimal places.
 */
fun Number.asCurrency(): BigDecimal {
    // BigDecimal works best with strings, so cast the value as a string,
    // and then get the currency value.
    // We'll pass 0 as a default value to use, though that should never happen
    // since we're starting with a valid number.
    return this.toString().asCurrency(0)
}

/**
 * Compares BigDecimal VALUES.
 *
 * The standard == operator { equals() } compares BigDecimal OBJECTS and not
 * their VALUES. This method compares values and returns a boolean to indicate
 * their equality.
 *
 * @param other BigDecimal to compare to this one.
 *
 * @return true if the value of the two BigDecimals are the same, false otherwise.
 */
fun BigDecimal.equalTo(other: BigDecimal?): Boolean = (other != null && this.compareTo(other) == 0)

/**
 *  Format a BigDecimal as a US currency string, or a default value if the BigDecimal is null.
 *
 *  The default string is "--".
 *
 *  @param showSymbol Boolean (default true) indicating whether the dollar sign ($) should be included in the formatted string.
 *  @param showNegativeValue Boolean (default false) indicating whether to format negative number values. If false, will return the default string.
 *  @param showCents Boolean (default true) indicating whether or not to include cents in the formatted string. If fallse, will round to the nearest whole dollar.
 *  @param groupDollars Boolean (default true) indicating whether to group dollar amounts by the thousand. true = $12,345  false = $12345
 *
 *  @return a formatted currency String, or the default value.
 */
fun BigDecimal?.formatCurrency(showSymbol: Boolean = true, showNegativeValue: Boolean = false, showCents: Boolean = true, groupDollars: Boolean = true): String {
    val defaultValue = "--"
    val decimals = if (showCents) 2 else 0

    if (this == null) return defaultValue

    // Make sure we are dealing with a proper currency value.
    val amount = this.setScale(2, RoundingMode.HALF_UP)

    if (!showNegativeValue && this < BigDecimal.ZERO) return defaultValue

    val formatter = NumberFormat.getCurrencyInstance(Locale.US).apply {
        roundingMode = RoundingMode.HALF_UP
        minimumFractionDigits = decimals
        maximumFractionDigits = decimals
        minimumIntegerDigits = 1
        isGroupingUsed = groupDollars
    }
    val symbol = formatter.currency.symbol

    var formattedString = formatter.format(amount)

    if (!showSymbol) {
        formattedString = formattedString.replace(symbol, String.empty)
    }

    return formattedString
}

/**
 * True if the BigDecimal has cents in it's value.
 */
val BigDecimal?.hasCents: Boolean
    get() = !(this?.asCurrency()?.remainder(BigDecimal.ONE)?.equalTo(BigDecimal.ZERO) ?: false)