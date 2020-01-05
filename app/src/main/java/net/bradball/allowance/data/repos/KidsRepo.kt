package net.bradball.allowance.data.repos

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import net.bradball.allowance.data.store.IDataStore
import net.bradball.allowance.models.NewKid
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KidsRepo @Inject constructor(private val dataStore: IDataStore) {
    fun getKids(): Flow<List<NewKid>> = dataStore.observeKids()

    suspend fun loadKid(id: String?): NewKid = dataStore.getKid(id)
    fun getKid(id: String?): Flow<NewKid> = dataStore.observeKid(id)

    suspend fun saveKid(kid: NewKid) = dataStore.saveKid(kid)

}