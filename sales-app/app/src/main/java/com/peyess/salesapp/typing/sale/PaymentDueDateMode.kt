package com.peyess.salesapp.typing.sale

import java.time.LocalTime
import java.time.ZonedDateTime

sealed class PaymentDueDateMode {
    object Month : PaymentDueDateMode()
    object Day : PaymentDueDateMode()
    object None : PaymentDueDateMode()

    fun toName() = toName(this)

    fun dueDateAfter(period: Int): ZonedDateTime {
        return if (this is Day) {
            ZonedDateTime.now()
                .plusDays(period.toLong())
                .with(LocalTime.of(23, 59, 59))
        } else {
            ZonedDateTime.now()
                .plusMonths(period.toLong())
                .with(LocalTime.of(23, 59, 59))
        }
    }

    companion object {
        fun fromName(value: String): PaymentDueDateMode {
            return when (value) {
                "month" -> Month
                "day" -> Day
                else -> None
            }
        }

        fun toName(value: PaymentDueDateMode): String {
            return when (value) {
                is Day -> "day"
                is Month -> "month"
                is None -> "none"
            }
        }
    }
}
