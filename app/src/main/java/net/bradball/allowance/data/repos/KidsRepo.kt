package net.bradball.allowance.data.repos

import androidx.lifecycle.LiveData
import net.bradball.allowance.data.store.IDataStore
import net.bradball.allowance.models.Kid
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KidsRepo @Inject constructor(private val dataStore: IDataStore) {
    fun getKids(): LiveData<List<Kid>> = dataStore.getKids()

    fun getKid(id: String?): LiveData<Kid> = dataStore.getKid(id)

    fun saveKid(kid: Kid) = dataStore.saveKid(kid)

}