package com.peyess.salesapp.typing.sale

sealed class SOState {
    object PendingConfirmation: SOState()
    object Confirmed: SOState()
    object Failed: SOState()
    object Cancelled: SOState()
    object Unknown: SOState()

    fun toName() = toName(this)

    companion object {
        fun toName(state: SOState): String {
            return when (state) {
                is PendingConfirmation -> "pending-confirmation"
                is Confirmed -> "confirmed"
                is Failed -> "failed"
                is Cancelled -> "cancelled"
                is Unknown -> "unknown"
            }
        }

        fun fromName(state: String): SOState {
            return when (state) {
                "pending-confirmation" -> PendingConfirmation
                "confirmed" -> Confirmed
                "failed" -> Failed
                "cancelled" -> Cancelled
                else -> Unknown
            }
        }
    }
}
