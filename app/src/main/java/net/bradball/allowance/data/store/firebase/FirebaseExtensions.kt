package net.bradball.allowance.data.store.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@ExperimentalCoroutinesApi
fun DocumentReference.asFlow(): Flow<DocumentSnapshot> = callbackFlow {
        val listener = addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            // Per the documentation for the Firestore EventListener,
            // either the firestore exception will be null, or the doc snapshot will be null,
            // but never both null or both non-null. We can use that to properly return
            // a non-null document or cancel with an error.
            // More info here: https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/EventListener.html
            when (firebaseFirestoreException) {
                null -> offer(documentSnapshot!!)
                else -> cancel("Firebase error retrieving document (${this@asFlow.id})", firebaseFirestoreException)
            }
        }
        awaitClose { listener.remove() }
}

@ExperimentalCoroutinesApi
fun <T: Any> DocumentReference.asFlow(valueType: Class<T>, serverTimestampBehavior: DocumentSnapshot.ServerTimestampBehavior? = null): Flow<T> {
    return this.asFlow().mapNotNull {
        when (serverTimestampBehavior) {
            null -> it.toObject(valueType)
            else -> it.toObject(valueType, serverTimestampBehavior)
        }
    }
}

@ExperimentalCoroutinesApi
fun CollectionReference.asFlow(): Flow<List<DocumentSnapshot>> = callbackFlow {
    val listener = addSnapshotListener { querySnapshot, firebaseFirestoreException ->
        // Per the documentation for the Firestore EventListener,
        // either the firestore exception will be null, or the doc snapshot will be null,
        // but never both null or both non-null. We can use that to properly return
        // a non-null document or cancel with an error.
        // More info here: https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/EventListener.html
        when (firebaseFirestoreException) {
            null -> offer(querySnapshot!!.documents)
            else -> cancel("Firebase error retrieving collection (${this@asFlow.id}", firebaseFirestoreException)
        }
    }
    awaitClose { listener.remove() }
}

@ExperimentalCoroutinesApi
fun <T: Any> CollectionReference.asFlow(valueType: Class<T>, serverTimestampBehavior: DocumentSnapshot.ServerTimestampBehavior? = null): Flow<List<T>> {
    return this.asFlow().mapLatest {
        it.mapNotNull { doc ->
            when (serverTimestampBehavior) {
                null -> doc.toObject(valueType)
                else -> doc.toObject(valueType, serverTimestampBehavior)
            }
        }
    }
}


@ExperimentalCoroutinesApi
fun Query.asFlow(): Flow<List<DocumentSnapshot>> = callbackFlow {
    val listener = addSnapshotListener { querySnapshot, firebaseFirestoreException ->
        // Per the documentation for the Firestore EventListener,
        // either the firestore exception will be null, or the doc snapshot will be null,
        // but never both null or both non-null. We can use that to properly return
        // a non-null document or cancel with an error.
        // More info here: https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/EventListener.html
        when (firebaseFirestoreException) {
            null -> offer(querySnapshot!!.documents)
            else -> cancel("Firebase error executing query (${this@asFlow.toString()}", firebaseFirestoreException)
        }
    }
    awaitClose { listener.remove() }
}

@ExperimentalCoroutinesApi
fun <T: Any> Query.asFlow(valueType: Class<T>, serverTimestampBehavior: DocumentSnapshot.ServerTimestampBehavior? = null): Flow<List<T>> {
    return this.asFlow().mapLatest {
        it.mapNotNull { doc ->
            when (serverTimestampBehavior) {
                null -> doc.toObject(valueType)
                else -> doc.toObject(valueType, serverTimestampBehavior)
            }
        }
    }
}
