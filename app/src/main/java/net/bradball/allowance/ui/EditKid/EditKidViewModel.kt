package net.bradball.allowance.ui.EditKid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.bradball.allowance.data.repos.KidsRepo
import net.bradball.allowance.models.Kid
import java.util.*
import javax.inject.Inject

class EditKidViewModel @Inject constructor(private val kidsRepo: KidsRepo): ViewModel() {
    fun getKid(id: String): LiveData<Kid> = kidsRepo.getKid(id)
}