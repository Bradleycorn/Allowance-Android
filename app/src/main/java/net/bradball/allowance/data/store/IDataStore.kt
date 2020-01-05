package net.bradball.allowance.data.store

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

import net.bradball.allowance.models.NewKid

interface IDataStore {
    suspend fun getKids(): List<NewKid>
    fun observeKids(): Flow<List<NewKid>>

    suspend fun getKid(id: String?): NewKid
    fun observeKid(id: String?): Flow<NewKid>

    suspend fun saveKid(kid: NewKid)
}