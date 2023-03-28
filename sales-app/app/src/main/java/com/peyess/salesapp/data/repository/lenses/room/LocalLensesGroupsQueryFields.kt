package com.peyess.salesapp.data.repository.lenses.room

sealed class LocalLensesGroupsQueryFields {
    object Priority: LocalLensesGroupsQueryFields()

    fun name(): String {
        return when (this) {
            is Priority -> "priority"
        }
    }
}
