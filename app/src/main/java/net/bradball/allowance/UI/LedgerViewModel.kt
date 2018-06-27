package net.bradball.allowance.UI

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import net.bradball.allowance.models.LedgerEntry
import java.util.*

class LedgerViewModel: ViewModel() {

    fun getLedger(kidId: String): LiveData<List<LedgerEntry>> {

        val ledgerData = listOf(
                LedgerEntry(1, "braden-ball", Date(), "First Entry", 2.00),
                LedgerEntry(2, "braden-ball", Date(), "Did some work", 3.25),
                LedgerEntry(3, "braden-ball", Date(), "Left shoes out", -0.25),
                LedgerEntry(4, "braden-ball", Date(), "garbage cans", 0.50),
                LedgerEntry(5, "braden-ball", Date(), "Cleaned room", 0.25),
                LedgerEntry(6, "braden-ball", Date(), "Got in trouble", -1.00),
                LedgerEntry(7,  "charlie-ball", Date(), "First Entry", 1.00),
                LedgerEntry(8,  "charlie-ball", Date(), "Told a lie", -0.25),
                LedgerEntry(9,  "charlie-ball", Date(), "Video games at night", -5.00),
                LedgerEntry(10, "charlie-ball", Date(), "Tried new food", 0.50),
                LedgerEntry(11, "charlie-ball", Date(), "Cleaned room", 0.25),
                LedgerEntry(12, "charlie-ball", Date(), "Cleaned basement", 2.00)
        )
        val data: MutableLiveData<List<LedgerEntry>> = MutableLiveData()
        data.postValue(ledgerData.filter { it.kidId == kidId })
        return data
    }
}