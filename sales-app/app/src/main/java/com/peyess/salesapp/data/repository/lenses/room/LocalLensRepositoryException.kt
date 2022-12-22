package com.peyess.salesapp.data.repository.lenses.room

sealed interface LocalLensRepositoryException {
    val description: String
    val error: Throwable?
}

data class Unexpected(
    override val description: String = "",
    override val error: Throwable? = null,
): LocalLensRepositoryException