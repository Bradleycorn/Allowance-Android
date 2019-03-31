package net.bradball.allowance.ui.editKid

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

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val birthDate = MutableLiveData<String>()


    private var kid: Kid = Kid()
    val kidLiveData = MediatorLiveData<Kid>()

    fun loadKid(id: String?) {
        if (kid.storeId != id) {
            val dataStore = kidsRepo.getKid(id)
            kidLiveData.addSource(dataStore) {
                kid = it
                kidLiveData.value = kid
                kidLiveData.removeSource(dataStore)
            }
        } else {
            kidLiveData.value = kid
        }
    }

    fun onBirthdateSelected(date: LocalDate) {
        val kid = kidLiveData.value
        kid?.birthdate = date
        kidLiveData.value = kid
    }

    fun updateKid(updatedKid: Kid) {
        kid = updatedKid
    }


    fun saveKid() {
        kidsRepo.saveKid(kid)
    }

    fun parseBirthDate(date: String): LocalDate = date.parseDate(DATE_FORMAT.LONG)

    fun formatBirthDate(input: LocalDate): String = input.format(DATE_FORMAT.LONG)

}