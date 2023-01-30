package com.peyess.salesapp.data.adapter.local_client

import com.peyess.salesapp.data.model.client.FSClient
import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.data.model.local_client.LocalClientEntity
import com.peyess.salesapp.utils.time.toTimestamp

fun LocalClientDocument.toLocalClientEntity(): LocalClientEntity {
    return LocalClientEntity(
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

fun LocalClientDocument.toFSClient(): FSClient {
    return FSClient(
        name = name,
        nameDisplay = nameDisplay,
        picture = picture.toString(),
        birthday = birthday.toTimestamp(),
        document = document,
        sex = sex.toName(),
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
        created = created.toTimestamp(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
