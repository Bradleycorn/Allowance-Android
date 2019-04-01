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

    private var initialSpendingBalance: Double = 0.0
    private var initialSavingsBalance: Double = 0.0

    val currentBirthdate: LocalDate
        get() = kidLiveData.value?.birthdate ?: LocalDate.now()

    val temp = MutableLiveData<Float>()

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

    fun saveKid() {
        kidLiveData.value?.let { kid ->
            kidsRepo.saveKid(kid)
        }
    }

    fun onSpendingBalanceChanged(balance: Float) {
        //TODO - This will add on more and more as you edit ... not what we want.
        initialSpendingBalance = balance.toDouble()
    }

    fun parseBirthDate(date: String): LocalDate = date.parseDate(DATE_FORMAT.LONG)

    fun formatBirthDate(input: LocalDate): String = input.format(DATE_FORMAT.LONG)

}