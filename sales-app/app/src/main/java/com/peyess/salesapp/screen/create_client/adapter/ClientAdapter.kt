package com.peyess.salesapp.screen.create_client.adapter

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedEntity
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.screen.create_client.model.Client
import com.peyess.salesapp.typing.sale.ClientRole
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun Client.toCacheCreateClientDocument(): CacheCreateClientDocument {
    return CacheCreateClientDocument(
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
        isCreating = isCreating,
        phoneHasWhatsApp = phoneHasWhatsApp,
        hasPhoneContact = hasPhoneContact,
        hasAcceptedPromotionalMessages = hasAcceptedPromotionalMessages,
    )
}

fun Client.toClientPickedEntity(
    serviceOrderId: String,
    clientRole: ClientRole,
): ClientPickedEntity {
    return ClientPickedEntity(
        id = id,
        soId = serviceOrderId,
        clientRole = clientRole,
        nameDisplay = nameDisplay,
        name = name,
        sex = sex.toName(),
        email = email,
        document = document,
        shortAddress = "$street, $houseNumber",
    )
}

fun Client.toClientModel(): ClientModel {
    return ClientModel(
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
    )
}

fun Client.toLocalClientDocument(collaboratorId: String): LocalClientDocument {
    val now = ZonedDateTime.now()
    val epoch = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault())

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
        doc_version = 0,
        is_editable = true,
        created = now,
        createdBy = collaboratorId,
        createAllowedBy = collaboratorId,
        updatedBy = collaboratorId,
        updateAllowedBy = collaboratorId,
        hasBeenUploaded = true,
        hasBeenDownloaded = true,
        remoteUpdated = epoch,
        localUpdated = epoch,
        downloadedAt = epoch,
        uploadedAt = epoch,
    )
}

fun Client.toPictureUploadDocument(
    salesApplication: SalesApplication,
    clientId: String,
): PictureUploadDocument {
    val storagePath = salesApplication
        .getString(R.string.storage_client_profile)
        .format(clientId)
    val storageFilename = salesApplication
        .getString(R.string.storage_client_profile_filename)

    return PictureUploadDocument(
        id = 0L,
        picture = picture,
        storagePath = storagePath,
        storageName = storageFilename,
        hasBeenUploaded = false,
        hasBeenDeleted = false,
        attemptCount = 0,
    )
}