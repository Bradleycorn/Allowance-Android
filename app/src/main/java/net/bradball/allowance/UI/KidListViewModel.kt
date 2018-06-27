package net.bradball.allowance.UI

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import net.bradball.allowance.data.repos.KidsRepo
import net.bradball.allowance.models.Kid

class KidListViewModel: ViewModel() {

    fun getKidList(): LiveData<List<Kid>> {
        return KidsRepo.getKids()
    }
}