package com.peyess.salesapp.data.adapter.client

import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import java.time.ZonedDateTime

fun ClientDocument.toLocalClientDocument(
    hasBeenUploaded: Boolean = false,
    hasBeenDownloaded: Boolean = false,
    downloadedAt: ZonedDateTime = ZonedDateTime.now(),
    uploadedAt: ZonedDateTime = ZonedDateTime.now(),
): LocalClientDocument {
    return LocalClientDocument(
        id = id,

        name = name,
        nameDisplay = nameDisplay,
        picture = picture,
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
        storesIds = storesIds,
        doc_version = doc_version,
        is_editable = is_editable,
        created = created,
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated,
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,

        hasBeenUploaded = hasBeenUploaded,
        hasBeenDownloaded = hasBeenDownloaded,
        downloadedAt = downloadedAt,
        uploadedAt = uploadedAt,
    )
}