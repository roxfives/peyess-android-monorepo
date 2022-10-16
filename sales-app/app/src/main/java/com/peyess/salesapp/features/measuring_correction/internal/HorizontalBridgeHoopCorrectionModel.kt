package com.peyess.salesapp.features.measuring_correction.internal

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.features.measuring_correction.CorrectionModel
import kotlin.math.ceil
import kotlin.math.round

internal class HorizontalBridgeHoopCorrectionModel : CorrectionModel() {
    override fun fixField(rawMeasuring: RawMeasuring): Double {
        val rounded = ceil(2.0 * rawMeasuring.horizontalBridgeHoop) / 2.0

        return rounded
    }
}