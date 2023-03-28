package com.peyess.salesapp.data.model.lens.categories.error

sealed interface LensCategoryDaoError {
    val description: String
    val error: Throwable
}

sealed class LensCategoryDaoReadError: LensCategoryDaoError {
    data class LensCategoryDaoFetchError(
        override val description: String,
        val throwable: Throwable? = null,
    ): LensCategoryDaoReadError() {
        override val error: Throwable
            get() = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): LensCategoryDaoReadError() {
        override val error: Throwable
            get() = throwable ?: Throwable(description)
    }
}