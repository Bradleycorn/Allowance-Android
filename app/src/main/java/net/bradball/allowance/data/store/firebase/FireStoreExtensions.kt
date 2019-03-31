package net.bradball.allowance.data.store.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import net.bradball.allowance.data.store.DataStoreRecord

fun CollectionReference.document(path: String?): DocumentReference {
    return when (path) {
        null -> this.document()
        else -> this.document(path)
    }
}

fun <D: DataStoreRecord> DocumentSnapshot.toDataStoreRecord(valueType: Class<D>): D {
    val record = this.toObject(valueType) ?: valueType.newInstance()
    record.recordId = this.id
    return record
}

fun <D: DataStoreRecord> QuerySnapshot.toDataStoreRecords(valueType: Class<D>): List<D> {
    val records = this.toObjects(valueType)
    records.forEachIndexed { index, d ->
        d.recordId = this.documents[index].id
    }
    return records
}
