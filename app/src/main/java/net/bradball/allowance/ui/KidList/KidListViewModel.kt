package net.bradball.allowance.ui.KidList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import net.bradball.allowance.data.repos.KidsRepo
import net.bradball.allowance.models.NewKid
import javax.inject.Inject

class KidListViewModel @Inject constructor(private val kidsRepo: KidsRepo): ViewModel() {

    fun getKidList(): LiveData<List<NewKid>> {
        return kidsRepo.getKids().asLiveData()
    }
}