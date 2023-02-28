package com.peyess.salesapp.typing.sale

sealed class PurchaseState {
    object PendingConfirmation: PurchaseState()
    object Confirmed: PurchaseState()
    object FinishedSuccessfully: PurchaseState()
    object Failed: PurchaseState()
    object Cancelled: PurchaseState()
    object Unknown: PurchaseState()

    fun toName() = toName(this)

    companion object {
        fun toName(state: PurchaseState): String {
            return when (state) {
                is PendingConfirmation -> "pending-confirmation"
                is Confirmed -> "confirmed"
                is FinishedSuccessfully -> "finished-successfully"
                is Failed -> "failed"
                is Cancelled -> "cancelled"
                is Unknown -> "unknown"
            }
        }

        fun fromName(state: String): PurchaseState {
            return when (state) {
                "pending-confirmation" -> PendingConfirmation
                "confirmed" -> Confirmed
                "finished-successfully" -> FinishedSuccessfully
                "failed" -> Failed
                "cancelled" -> Cancelled
                else -> Unknown
            }
        }
    }
}
