package net.bradball.allowance.data.store.firebase

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class FirestoreQueryLiveData(private val query: Query) : LiveData<QuerySnapshot>() {

    private var queryListener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        queryListener = query.addSnapshotListener { snapshot, firestoreException ->
            value = snapshot
        }

    }

    override fun onInactive() {
        super.onInactive()
        queryListener?.remove()
    }
}