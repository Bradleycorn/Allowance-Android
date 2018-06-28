package net.bradball.allowance.data

import android.arch.lifecycle.LiveData
import com.google.firebase.firestore.*


class FirestoreDocumentLiveData(private val document: DocumentReference) : LiveData<DocumentSnapshot>() {

    private var queryListener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        queryListener = document.addSnapshotListener { snapshot, firestoreException ->
            value = snapshot
        }

    }

    override fun onInactive() {
        super.onInactive()
        queryListener?.remove()
    }
}