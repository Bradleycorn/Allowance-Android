package net.bradball.allowance.ui.KidList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import net.bradball.allowance.data.repos.KidsRepo
import net.bradball.allowance.models.Kid
import javax.inject.Inject

class KidListViewModel @Inject constructor(private val kidsRepo: KidsRepo): ViewModel() {

    fun getKidList(): LiveData<List<Kid>> {
        return kidsRepo.getKids()
    }
}