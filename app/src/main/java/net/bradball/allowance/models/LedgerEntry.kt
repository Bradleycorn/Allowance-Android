package net.bradball.allowance.models

import java.util.*

data class LedgerEntry(val id: Int, val kidId: Int, var date: Date, var description: String, var amount: Double)