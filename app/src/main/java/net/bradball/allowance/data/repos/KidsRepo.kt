package net.bradball.allowance.data.repos

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.google.firebase.firestore.FirebaseFirestore
import net.bradball.allowance.data.FirestoreDocumentLiveData
import net.bradball.allowance.data.FirestoreQueryLiveData
import net.bradball.allowance.models.Kid

object KidsRepo {
    private const val KIDS_COLLECTION = "kids"
    private const val KIDS_FIELD_FIRSTNAME = "firstname"

    private val kidsList = MutableLiveData<List<Kid>>()

    fun getKids(): LiveData<List<Kid>> {

        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val data = FirestoreQueryLiveData(firestore.collection(KIDS_COLLECTION).orderBy(KIDS_FIELD_FIRSTNAME))

        return Transformations.switchMap(data) {
            val temp = it.toObjects(Kid::class.java)
            kidsList.value = it.toObjects(Kid::class.java)
            return@switchMap kidsList
        }
   }

   fun getKid(id: String): LiveData<Kid> {
       val kidData = MutableLiveData<Kid>()
       val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
       return Transformations.map(FirestoreDocumentLiveData(firestore.collection(KIDS_COLLECTION).document(id))) {
           it.toObject(Kid::class.java)
       }
   }

}