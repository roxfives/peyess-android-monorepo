package com.peyess.salesapp.data.model.lens.room.repo

data class LocalLensMaterialDocument(
    val id: String = "",
    val name: String = "",

    val priority: Int = 0,
    val storePriority: Int = 0,

    val n: Double = 0.0,

    val observation: String = "",

    val categoryId: String = "",
    val category: String = "",
)
