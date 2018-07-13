package net.bradball.allowance.models

import net.bradball.allowance.data.FirestoreDocument
import java.util.*

data class Kid(var firstname: String = "", var lastname: String = "", var balance: Double = 0.0, val birthdate: Date = Date()) : FirestoreDocument {

    override
    val docId: String
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
}