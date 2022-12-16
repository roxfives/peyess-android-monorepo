package com.peyess.salesapp.data.adapter.disponibilities

import com.peyess.salesapp.data.model.lens.disponibility.DispManufacturerDocument
import com.peyess.salesapp.data.model.lens.disponibility.FSDispManufacturer

fun FSDispManufacturer.toDispManufacturerDocument(): DispManufacturerDocument {
    return DispManufacturerDocument(
        name = name,
        shippingTime = shippingTime,
    )
}