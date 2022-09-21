package com.peyess.salesapp.data.adapter.client

import com.peyess.salesapp.data.dao.cache.CacheCreateClientEntity
import com.peyess.salesapp.data.model.client.ClientModel

fun CacheCreateClientEntity.toClientModel(): ClientModel {
    return ClientModel(
        id = id,
        name = name,
        nameDisplay = nameDisplay,
        picture = picture,
        document = document,
        sex = sex,
        email = email,

        birthday = birthday.toZonedDateTime()
    )
}

fun ClientModel.toCacheCreateClientEntity(): CacheCreateClientEntity {
    return CacheCreateClientEntity(
        id = id,
        name = name,
        nameDisplay = nameDisplay,
        picture = picture,
        document = document,
        sex = sex,
        email = email,

        birthday = birthday.toOffsetDateTime()
    )
}