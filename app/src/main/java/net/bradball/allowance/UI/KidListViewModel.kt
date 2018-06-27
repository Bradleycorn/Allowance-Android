package net.bradball.allowance.UI

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import net.bradball.allowance.models.Kid

class KidListViewModel: ViewModel() {

    fun getKidList(): LiveData<List<Kid>> {
        val data: MutableLiveData<List<Kid>> = MutableLiveData()
        data.postValue(listOf(Kid(1, "Braden", "Ball"), Kid(2, "Charlie", "Ball")))


        return data
    }
}