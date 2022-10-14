package com.peyess.salesapp.features.measuring_correction.internal

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.features.measuring_correction.CorrectionModel
import kotlin.math.round

internal class BridgeCorrectionModel : CorrectionModel() {
    override fun fixField(rawMeasuring: RawMeasuring): Double {
        val bridge = rawMeasuring.bridge * -0.69311305
        val horizontalCheck = rawMeasuring.horizontalCheck * -0.2746577
        val verticalCheck = rawMeasuring.verticalCheck * -0.0840033
        val ve = rawMeasuring.ve * 0.2447102
        val bottomPoint = rawMeasuring.bottomPoint * -0.12043775
        val eulerX = rawMeasuring.eulerAngleX * 0.11192217
        val intercept = 0.1367223274938127

        val diff = bridge + horizontalCheck + verticalCheck + ve + bottomPoint + eulerX + intercept
        val prediction = rawMeasuring.bridge + diff
        val rounded = round(prediction * 2.0) / 2.0

        return rounded
    }
}