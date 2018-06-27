package net.bradball.allowance.data.repos

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import net.bradball.allowance.data.FirestoreLiveData
import net.bradball.allowance.models.Kid

object KidsRepo {
    private const val KIDS_COLLECTION = "kids"
    private const val KIDS_FIELD_FIRSTNAME = "firstname"

    private val kidsList = MutableLiveData<List<Kid>>()

    fun getKids(): LiveData<List<Kid>> {

        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val data = FirestoreLiveData(firestore.collection(KIDS_COLLECTION).orderBy(KIDS_FIELD_FIRSTNAME))

        return Transformations.switchMap(data) {
            kidsList.value = it.toObjects(Kid::class.java)
            return@switchMap kidsList
        }
   }

}