package net.bradball.allowance.data.store

import androidx.lifecycle.LiveData
import net.bradball.allowance.models.Kid

interface IDataStore {
    fun getKids(): LiveData<List<Kid>>

    fun getKid(id: String?): LiveData<Kid>

    fun saveKid(kid: Kid): LiveData<Boolean>
}