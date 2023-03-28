package com.peyess.salesapp.data.adapter.card_flag

import android.net.Uri
import com.peyess.salesapp.data.model.sale.card_flags.CardFlagDocument
import com.peyess.salesapp.data.model.sale.card_flags.FSCardFlag
import com.peyess.salesapp.utils.time.toZonedDateTime

fun FSCardFlag.toCardFlagDocument(id: String): CardFlagDocument {
    return CardFlagDocument(
        id = id,

        name = name,
        icon = Uri.parse(icon),

        docVersion = docVersion,
        isEditable = isEditable,

        created = created.toZonedDateTime(),
        updated = updated.toZonedDateTime(),

        createdBy = createdBy,
        updatedBy = updatedBy,
        createAllowedBy = createAllowedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
