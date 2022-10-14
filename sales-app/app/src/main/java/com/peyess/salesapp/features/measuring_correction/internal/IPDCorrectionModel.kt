package com.peyess.salesapp.features.measuring_correction.internal

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.features.measuring_correction.CorrectionModel
import kotlin.math.round

internal class IPDCorrectionModel: CorrectionModel() {
    override fun fixField(rawMeasuring: RawMeasuring): Double {
        val baseSize = rawMeasuring.baseSize * 0.04225225
        val bridge = rawMeasuring.bridge * -0.32824007
        val eulerAngleX = rawMeasuring.eulerAngleX * 0.01248284
        val ve = rawMeasuring.ve * -0.70619725
        val horizontalBridgeHoop = rawMeasuring.horizontalBridgeHoop * -0.0414469
        val intercept = 0.03830439014680679

        val diff = baseSize + bridge + eulerAngleX + ve + horizontalBridgeHoop + intercept
        val prediction = rawMeasuring.ipd + diff
        val rounded = round(prediction * 2.0) / 2.0

        return rounded
    }
}