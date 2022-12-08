package com.peyess.salesapp.data.model.lens.material

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

data class LensMaterialDocument(
    val id: String = "",
    val name: String = "",

    val docVersion: Int = 0,
    val is_editable: Boolean = false,
    val created: Timestamp = Timestamp.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: Timestamp = Timestamp.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
