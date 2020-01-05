package net.bradball.allowance.data.store.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import net.bradball.allowance.data.store.IDataStore

import net.bradball.allowance.models.NewKid
import net.bradball.allowance.util.map
import javax.inject.Inject

class FirebaseDataStore @Inject constructor(): IDataStore {
    companion object {
        private const val KIDS_COLLECTION = "kids"
        private const val KIDS_FIELD_FIRSTNAME = "firstname"
    }

    private val firestore: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    override suspend fun getKid(id: String?): NewKid {
        val kidDoc = firestore.collection(KIDS_COLLECTION).document(id).get().await()
        return kidDoc.toObject(NewKid::class.java) ?: NewKid()
    }

    override fun observeKid(id: String?): Flow<NewKid> {
        return firestore.collection(KIDS_COLLECTION).document(id).asFlow(NewKid::class.java)
    }

    override suspend fun getKids(): List<NewKid> {
        val query = firestore.collection(KIDS_COLLECTION).orderBy(KIDS_FIELD_FIRSTNAME).get().await()
        return query.toObjects(NewKid::class.java)
    }

    override fun observeKids(): Flow<List<NewKid>> {
        return firestore.collection(KIDS_COLLECTION).orderBy(KIDS_FIELD_FIRSTNAME).asFlow(NewKid::class.java)
    }

    override suspend fun saveKid(kid: NewKid) {
        firestore.collection(KIDS_COLLECTION).document(kid.dataStoreId).set(kid).await()
    }
}