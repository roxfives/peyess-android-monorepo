package com.peyess.salesapp.screen.sale.frames.data.adapter

import com.peyess.salesapp.feature.lens_suggestion.model.Measuring
import com.peyess.salesapp.screen.sale.frames.data.model.DisplayMeasure

fun Measuring.toDisplayMeasure(): DisplayMeasure {
    return DisplayMeasure(
        diameter = fixedDiameter,
        ipd = fixedIpd,
        he = fixedHe,
        bridge = fixedBridge,
        hHoop = fixedHHoop,
        vHoop = fixedVHoop,
        horizontalBridgeHoop = fixedHorizontalBridgeHoop,
    )
}