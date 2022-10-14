package com.peyess.salesapp.features.measuring_correction.internal

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.features.measuring_correction.CorrectionModel
import kotlin.math.ceil

internal class DiameterCorrectionModel: CorrectionModel() {
    override fun fixField(rawMeasuring: RawMeasuring): Double {
        val rounded = ceil(2.0 * rawMeasuring.diameter) / 2.0
        val safety = 3

        return rounded + safety
    }
}