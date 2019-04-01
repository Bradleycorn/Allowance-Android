package net.bradball.allowance.models

import com.google.firebase.firestore.Exclude
import net.bradball.allowance.data.store.DataStoreRecord
import net.bradball.allowance.util.DATE_FORMAT
import net.bradball.allowance.util.format
import java.time.LocalDate
import java.util.*

class Kid(
        var firstname: String = "",
        var lastname: String = "",
        spendingBalance: Double = 0.0,
        savingsBalance: Double = 0.0,
        var birthdate: LocalDate = LocalDate.now(),
        id: String? = null) {

    companion object {
        enum class ACCOUNT_TYPE {
            SPENDING, SAVINGS
        }
    }

    var storeId: String? = id
        private set

    var spendingBalance: Double = spendingBalance
        //private set

    var savingsBalance: Double = savingsBalance
        private set

    val birthday: String
        get() = birthdate.format(DATE_FORMAT.LONG)

    val fullName: String
        get() = "$firstname $lastname"

    fun setId(id: String) {
        if (storeId != null) {
            throw IllegalStateException("Cannot change Kid model id")
        }
        storeId = id
    }

    fun credit(amount: Double, account: ACCOUNT_TYPE) {
        when (account) {
            Companion.ACCOUNT_TYPE.SPENDING -> spendingBalance += amount
            Companion.ACCOUNT_TYPE.SAVINGS-> savingsBalance += amount
        }
    }

    fun debit(amount: Double, account: ACCOUNT_TYPE) {
        when (account) {
            Companion.ACCOUNT_TYPE.SPENDING ->  Math.max(spendingBalance - amount, 0.0)
            Companion.ACCOUNT_TYPE.SAVINGS->  Math.max(savingsBalance - amount, 0.0)
        }
    }

}