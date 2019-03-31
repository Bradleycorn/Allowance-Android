package net.bradball.allowance.data.store

import com.google.firebase.firestore.Exclude

abstract class DataStoreRecord {
    var recordId: String? = null
        @Exclude get() = field


}