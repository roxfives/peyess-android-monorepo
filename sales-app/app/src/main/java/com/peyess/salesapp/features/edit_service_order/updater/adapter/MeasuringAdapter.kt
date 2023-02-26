package com.peyess.salesapp.features.edit_service_order.updater.adapter

import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.data.model.measuring.MeasuringUpdateDocument
import com.peyess.salesapp.feature.lens_suggestion.model.Measuring
import java.time.ZonedDateTime

fun Measuring.toMeasuringUpdateDocument(
    client: LocalClientDocument,
    collaboratorUid: String,
    updated: ZonedDateTime,
): MeasuringUpdateDocument {
    return MeasuringUpdateDocument(
        takenByUid = collaboratorUid,
        patientUid = client.id,
        patientDocument = client.document,
        patientName = client.name,
        baseSize = baseSize,
        baseHeight = baseHeight,
        topAngle = topAngle,
        topPoint = topPoint,
        bottomAngle = bottomAngle,
        bottomPoint = bottomPoint,
        bridge = bridge,
        diameter = diameter,
        verticalCheck = verticalCheck,
        verticalHoop = verticalHoop,
        horizontalBridgeHoop = horizontalBridgeHoop,
        horizontalCheck = horizontalCheck,
        horizontalHoop = horizontalHoop,
        middleCheck = middleCheck,
        eulerAngleX = eulerAngleX,
        eulerAngleY = eulerAngleY,
        eulerAngleZ = eulerAngleZ,
        ipd = ipd,
        he = he,
        ho = ho,
        ve = ve,
        vu = vu,
        fixedHorizontalBridgeHoop = fixedHorizontalBridgeHoop,
        fixedBridge = fixedBridge,
        fixedIpd = fixedIpd,
        fixedHHoop = fixedHHoop,
        fixedHe = fixedHe,
        fixedVHoop = fixedVHoop,
        fixedDiameter = fixedDiameter,
        updated = updated,
        updatedBy = collaboratorUid,
        updateAllowedBy = collaboratorUid,
    )
}
