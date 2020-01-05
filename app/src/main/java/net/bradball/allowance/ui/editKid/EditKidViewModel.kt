package net.bradball.allowance.ui.editKid

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import net.bradball.allowance.data.repos.KidsRepo
import net.bradball.allowance.models.NewKid
import net.bradball.allowance.util.DATE_FORMAT
import net.bradball.allowance.util.format
import net.bradball.allowance.util.parseDate
import net.bradball.allowance.util.toDate
import java.time.LocalDate
import javax.inject.Inject

class EditKidViewModel @Inject constructor(private val kidsRepo: KidsRepo): ViewModel() {

    val kidLiveData = MediatorLiveData<NewKid>()

    private val _showBirthDatePicker = MutableLiveData<LocalDate>()
    val showBirthDatePicker: LiveData<LocalDate>
        get() = _showBirthDatePicker

    private val _kidSaved = MediatorLiveData<Boolean>()
    val kidSaved: LiveData<Boolean>
        get() = _kidSaved

    fun loadKid(id: String?) {
        viewModelScope.launch {
            kidLiveData.value = kidsRepo.loadKid(id)
        }
    }

    fun onBirthdateClicked() {
         _showBirthDatePicker.value = kidLiveData.value?.dob ?: LocalDate.now()
    }

    fun onBirthdateSelected(date: LocalDate) {
        val kid = kidLiveData.value
        kid?.dob = date
        kidLiveData.value = kid
    }

    fun saveKid(spendingBalance: Float, savingsBalance: Float): Boolean {
        kidLiveData.value?.let { kid ->

            if (kid.dataStoreId.isNullOrBlank()) {
                kid.credit(spendingBalance.toDouble(), NewKid.Companion.ACCOUNT_TYPE.SPENDING)
                kid.credit(savingsBalance.toDouble(), NewKid.Companion.ACCOUNT_TYPE.SAVINGS)
            }

            viewModelScope.launch {
                kidsRepo.saveKid(kid)
                _kidSaved.value = true
            }
        }
        return true
    }

}