package net.bradball.allowance.ui.editKid

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.bradball.allowance.data.repos.KidsRepo
import net.bradball.allowance.models.Kid
import net.bradball.allowance.util.DATE_FORMAT
import net.bradball.allowance.util.format
import net.bradball.allowance.util.parseDate
import java.time.LocalDate
import javax.inject.Inject

class EditKidViewModel @Inject constructor(private val kidsRepo: KidsRepo): ViewModel() {

    val kidLiveData = MediatorLiveData<Kid>()

    private val _showBirthDatePicker = MutableLiveData<LocalDate>()
    val showBirthDatePicker: LiveData<LocalDate>
        get() = _showBirthDatePicker

    private val _kidSaved = MediatorLiveData<Boolean>()
    val kidSaved: LiveData<Boolean>
        get() = _kidSaved

    fun loadKid(id: String?) {
        if (id.isNullOrBlank()) {
            kidLiveData.value = Kid()
        } else {
            val dataStore = kidsRepo.getKid(id)
            kidLiveData.addSource(dataStore) { kid ->
                kidLiveData.value = kid
                kidLiveData.removeSource(dataStore)
            }
        }
    }

    fun onBirthdateClicked() {
         _showBirthDatePicker.value = kidLiveData.value?.birthdate ?: LocalDate.now()
    }

    fun void() {}

    fun onBirthdateSelected(date: LocalDate) {
        val kid = kidLiveData.value
        kid?.birthdate = date
        kidLiveData.value = kid
    }

    fun saveKid(spendingBalance: Float, savingsBalance: Float): Boolean {
        kidLiveData.value?.let { kid ->

            if (kid.storeId.isNullOrBlank()) {
                kid.credit(spendingBalance.toDouble(), Kid.Companion.ACCOUNT_TYPE.SPENDING)
                kid.credit(savingsBalance.toDouble(), Kid.Companion.ACCOUNT_TYPE.SAVINGS)
            }

            val saveLiveData: LiveData<Boolean> = kidsRepo.saveKid(kid)
            _kidSaved.addSource(saveLiveData) { kidSaved ->
                _kidSaved.value = kidSaved
                _kidSaved.removeSource(saveLiveData)
            }
        }
        return true
    }

}