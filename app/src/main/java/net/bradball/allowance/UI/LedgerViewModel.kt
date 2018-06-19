package net.bradball.allowance.UI

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import net.bradball.allowance.models.LedgerEntry
import java.util.*

class LedgerViewModel: ViewModel() {

    fun getLedger(kidId: Int): LiveData<List<LedgerEntry>> {

        val ledgerData = listOf(
                LedgerEntry(1, 1, Date(), "First Entry", 2.00),
                LedgerEntry(2, 1, Date(), "Did some work", 3.25),
                LedgerEntry(3, 1, Date(), "Left shoes out", -0.25),
                LedgerEntry(4, 1, Date(), "garbage cans", 0.50),
                LedgerEntry(5, 1, Date(), "Cleaned room", 0.25),
                LedgerEntry(6, 1, Date(), "Got in trouble", -1.00),
                LedgerEntry(7, 2, Date(), "First Entry", 1.00),
                LedgerEntry(8, 2, Date(), "Told a lie", -0.25),
                LedgerEntry(9, 2, Date(), "Video games at night", -5.00),
                LedgerEntry(10, 2, Date(), "Tried new food", 0.50),
                LedgerEntry(11, 2, Date(), "Cleaned room", 0.25),
                LedgerEntry(12, 2, Date(), "Cleaned basement", 2.00)
        )
        val data: MutableLiveData<List<LedgerEntry>> = MutableLiveData()
        data.postValue(ledgerData.filter { it.kidId == kidId })
        return data
    }
}