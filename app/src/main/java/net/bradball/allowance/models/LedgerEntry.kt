package net.bradball.allowance.models

import net.bradball.allowance.data.DataStoreRecord
import java.util.*

data class LedgerEntry(val id: Int, val kidId: String, var date: Date, var description: String, var amount: Double) : DataStoreRecord {

    override
    val recordId: String
        get() = date.time.toString()
}