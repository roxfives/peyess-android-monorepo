package com.peyess.salesapp.data.model.lens.room.repo

import android.net.Uri

data class LocalLensSupplierDocument(
    val id: String = "",
    val picture: Uri = Uri.EMPTY,
    val name: String = "",

    val priority: Int = 0,
    val storePriority: Int = 0,
)
