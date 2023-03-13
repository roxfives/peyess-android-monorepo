package com.peyess.salesapp.app.adapter

import android.net.Uri
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.model.client.ClientDocument

fun ClientDocument.toCacheCreateClientDocument(
    hasAcceptedPromotionalMessages: Boolean,
): CacheCreateClientDocument {
    return CacheCreateClientDocument(
        id = id,
        name = name,
        nameDisplay = nameDisplay,
        picture = Uri.EMPTY,
        birthday = birthday,
        document = document,
        sex = sex,
        zipCode = zipCode,
        street = street,
        houseNumber = houseNumber,
        complement = complement,
        neighborhood = neighborhood,
        city = city,
        state = state,
        email = email,
        phone = phone,
        cellphone = cellphone,
        whatsapp = whatsapp,
        isCreating = false,

        phoneHasWhatsApp = phone == whatsapp,
        hasPhoneContact = phone.isNotBlank(),
        hasAcceptedPromotionalMessages = hasAcceptedPromotionalMessages,
    )
}
