package com.peyess.salesapp.dao.auth.store.error

sealed interface OpticalStoreError {
    val description: String
    val error: Throwable
}

data class OpticalStoreUnexpected(
    override val description: String = "Store not found",
    override val error: Throwable = Throwable(description),
): OpticalStoreError