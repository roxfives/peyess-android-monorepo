package com.peyess.salesapp.typing.sale

sealed class PurchaseSyncState {
    object NotSynced: PurchaseSyncState()
    object Syncing: PurchaseSyncState()
    object SyncFailed: PurchaseSyncState()
    object SyncRetry: PurchaseSyncState()
    object SyncSuccessful: PurchaseSyncState()
    object Unknown: PurchaseSyncState()

    fun toName() = toName(this)

    companion object {
        fun toName(state: PurchaseSyncState): String {
            return when (state) {
                is NotSynced -> "not-synced"
                is SyncFailed -> "sync-failed"
                is SyncRetry -> "sync-retry"
                is SyncSuccessful -> "sync-successful"
                is Syncing -> "syncing"
                is Unknown -> "unknown"
            }
        }

        fun fromName(state: String): PurchaseSyncState {
            return when (state) {
                "not-synced" -> NotSynced
                "sync-failed" -> SyncFailed
                "sync-retry" -> SyncRetry
                "sync-successful" -> SyncSuccessful
                "syncing" -> Syncing
                else -> Unknown
            }
        }
    }
}
