package net.bradball.allowance.data.store.firebase

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
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
        queryListener = null
    }
}

class FirestoreTaskLiveData(private val task: Task<Void>) : LiveData<Boolean>() {

    private var successHandler: Task<Void>? = null
    private var errorHandler: Task<Void>? = null

    override fun onActive() {
        super.onActive()
        successHandler = task.addOnSuccessListener {
            value = true
        }

        errorHandler = task.addOnFailureListener {
            value = false
        }

    }

    override fun onInactive() {
        super.onInactive()
        successHandler = null
        errorHandler = null
    }
}

