package com.peyess.salesapp.features.measuring_correction.internal

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.features.measuring_correction.CorrectionModel
import kotlin.math.ceil
import kotlin.math.roundToInt

internal class DiameterCorrectionModel: CorrectionModel() {
    override fun fixField(rawMeasuring: RawMeasuring): Double {
        val rounded = (2.0 * rawMeasuring.diameter).roundToInt() / 2.0
        val safety = 1

        return rounded + safety
    }
}