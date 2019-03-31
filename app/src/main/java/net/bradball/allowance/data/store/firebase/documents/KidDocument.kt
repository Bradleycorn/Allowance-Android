package net.bradball.allowance.data.store.firebase.documents

import net.bradball.allowance.data.store.DataStoreRecord
import net.bradball.allowance.models.Kid
import net.bradball.allowance.util.toDate
import net.bradball.allowance.util.toLocalDate
import java.util.*

data class KidDocument(
        var firstname: String = "",
        var lastname: String = "",
        var spendingBalance: Double = 0.0,
        var savingsBalance: Double = 0.0,
        var birthdate: Date = Date()) : DataStoreRecord() {

    fun toKidModel(): Kid {
        return Kid(firstname, lastname, spendingBalance, savingsBalance, birthdate.toLocalDate(), recordId)
    }

    companion object {
        fun fromKidModel(kid: Kid): KidDocument {
            return KidDocument(kid.firstname,
                    kid.lastname,
                    kid.spendingBalance,
                    kid.savingsBalance,
                    kid.birthdate.toDate()).apply {
                recordId = kid.storeId
            }
        }
    }
}
