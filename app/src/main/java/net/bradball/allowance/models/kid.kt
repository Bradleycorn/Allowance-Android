package net.bradball.allowance.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import net.bradball.allowance.data.store.DataStoreRecord
import net.bradball.allowance.util.DATE_FORMAT
import net.bradball.allowance.util.format
import net.bradball.allowance.util.toDate
import net.bradball.allowance.util.toLocalDate
import java.time.LocalDate
import java.util.*

data class NewKid(
    var firstname: String = "",
    var lastname: String = "",
    var spendingBalance: Double = 0.0,
    var savingsBalance: Double = 0.0,
    var birthdate: Date = Date(), //TODO, this should be a LocalDate, but firebase can't handle it.
    @DocumentId val dataStoreId: String? = null
) {
    @get:Exclude
    val totalBalance: Double
        get() =  spendingBalance + savingsBalance

    @get:Exclude
    var dob: LocalDate
        get() = birthdate.toLocalDate()
        set(value) { birthdate = value.toDate() }

    @get:Exclude
    val birthday: String
        get() = dob.format(DATE_FORMAT.LONG)

    fun credit(amount: Double, account: ACCOUNT_TYPE) {
        when (account) {
            ACCOUNT_TYPE.SPENDING -> spendingBalance += amount
            ACCOUNT_TYPE.SAVINGS-> savingsBalance += amount
        }
    }

    fun debit(amount: Double, account: ACCOUNT_TYPE) {
        when (account) {
            ACCOUNT_TYPE.SPENDING ->  Math.max(spendingBalance - amount, 0.0)
            ACCOUNT_TYPE.SAVINGS->  Math.max(savingsBalance - amount, 0.0)
        }
    }

    companion object {
        enum class ACCOUNT_TYPE {
            SPENDING, SAVINGS
        }
    }
}
