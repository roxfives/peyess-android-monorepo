package com.peyess.salesapp.data.repository.local_sale.positioning.typing

import com.peyess.salesapp.data.model.local_sale.positioning.LocalPositioningDocument

data class PositioningPair(
    val left: LocalPositioningDocument = LocalPositioningDocument(),
    val right: LocalPositioningDocument = LocalPositioningDocument(),
)
