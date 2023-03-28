package com.peyess.salesapp.data.adapter.local_client

import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.data.model.local_client.LocalClientEntity

fun LocalClientEntity.toLocalClientDocument(): LocalClientDocument {
    return LocalClientDocument(
        id = id,
        name = name,
        nameDisplay = nameDisplay,
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
        doc_version = doc_version,
        is_editable = is_editable,
        created = created,
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        remoteUpdated = remoteUpdated,
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
        hasBeenUploaded = hasBeenUploaded,
        hasBeenDownloaded = hasBeenDownloaded,
        localUpdated = localUpdated,
        downloadedAt = downloadedAt,
        uploadedAt = uploadedAt,
    )
}