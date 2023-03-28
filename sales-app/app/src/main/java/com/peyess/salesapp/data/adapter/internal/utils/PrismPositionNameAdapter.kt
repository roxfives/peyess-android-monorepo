package com.peyess.salesapp.data.adapter.internal.utils

import com.peyess.salesapp.typing.prescription.PrismPosition

fun prismPositionName(prismPosition: PrismPosition): String {
    return when(prismPosition) {
        is PrismPosition.Nasal -> "nasal"
        is PrismPosition.Temporal -> "temporal"
        is PrismPosition.Superior -> "superior"
        is PrismPosition.Inferior -> "inferior"
        is PrismPosition.Axis -> "axis"
        else -> "none"
    }
}