package net.bradball.allowance.data.store.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import net.bradball.allowance.data.store.IDataStore
import net.bradball.allowance.data.store.firebase.documents.KidDocument
import net.bradball.allowance.models.Kid
import net.bradball.allowance.util.map
import javax.inject.Inject

class FirebaseDataStore @Inject constructor(): IDataStore {
    companion object {
        private const val KIDS_COLLECTION = "kids"
        private const val KIDS_FIELD_FIRSTNAME = "firstname"
    }

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override
    fun getKids(): LiveData<List<Kid>> {
        return FirestoreQueryLiveData(firestore.collection(KIDS_COLLECTION).orderBy(KIDS_FIELD_FIRSTNAME)).map { snapshot ->
            snapshot.toDataStoreRecords(KidDocument::class.java).map { kidDocument -> kidDocument.toKidModel() }
        }
    }

    override
    fun getKid(id: String?): LiveData<Kid> {
        return FirestoreDocumentLiveData(firestore.collection(KIDS_COLLECTION).document(id)).map { snapshot ->
            snapshot.toDataStoreRecord(KidDocument::class.java).toKidModel()
        }
    }

    override fun saveKid(kid: Kid) {
        val kidDocument = KidDocument.fromKidModel(kid)
        val collection = firestore.collection(KIDS_COLLECTION)
        val document = when (kidDocument.recordId) {
            null -> collection.document().also { kid.setId(it.id) }
            else -> collection.document(kidDocument.recordId!!)
        }
        document.set(kid)
    }
}