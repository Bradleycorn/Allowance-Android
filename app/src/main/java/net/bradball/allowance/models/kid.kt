package net.bradball.allowance.models

import net.bradball.allowance.data.DataStoreRecord
import java.util.*

data class Kid( var firstname: String = "", var lastname: String = "", var balance: Double = 0.0, var birthdate: Date = Date()) : DataStoreRecord {

    override
    val recordId: String
        get() = ("$firstname-$lastname").toLowerCase()

    fun credit(amount: Double) {
        balance += amount
    }

    fun debit(amount: Double) {
        if (balance > amount)
            balance -= amount
        else
            balance = 0.0
    }

    fun display
}