package net.bradball.allowance.data.store.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.firestore.FirebaseFirestore
import net.bradball.allowance.data.store.IDataStore
import net.bradball.allowance.models.Kid
import javax.inject.Inject

class FirebaseDataStore @Inject constructor(): IDataStore {
    companion object {
        private const val KIDS_COLLECTION = "kids"
        private const val KIDS_FIELD_FIRSTNAME = "firstname"
    }

    private val kidsList = MutableLiveData<List<Kid>>()

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override
    fun getKids(): LiveData<List<Kid>> {
        val data = FirestoreQueryLiveData(firestore.collection(KIDS_COLLECTION).orderBy(KIDS_FIELD_FIRSTNAME))
        return Transformations.map(data) {
            it.toObjects(Kid::class.java)
        }
    }

    override
    fun getKid(id: String): LiveData<Kid> {
        return Transformations.map(FirestoreDocumentLiveData(firestore.collection(KIDS_COLLECTION).document(id))) {
            it.toObject(Kid::class.java)
        }
    }

    fun addKid(kid: Kid) {

    }
}