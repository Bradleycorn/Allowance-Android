package net.bradball.allowance.models

import net.bradball.allowance.data.FirestoreDocument
import java.util.*

data class LedgerEntry(val id: Int, val kidId: Int, var date: Date, var description: String, var amount: Double) : FirestoreDocument {

    override
    val docId: String
        get() = date.time.toString()
}