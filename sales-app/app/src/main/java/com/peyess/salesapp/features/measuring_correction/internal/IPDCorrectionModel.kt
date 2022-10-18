package com.peyess.salesapp.features.measuring_correction.internal

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.features.measuring_correction.CorrectionModel
import kotlin.math.round

//['base_size', 'bridge', 'euler_angle_x', 've', 'horizontal_bridge_hoop']
//[ 1.71939080e-03 -1.98344702e-01  5.44432203e-04 -5.46729793e-01
//-2.02262816e-02]
//17.335961398836012

internal class IPDCorrectionModel: CorrectionModel() {
    override fun fixField(rawMeasuring: RawMeasuring): Double {
        val baseSize = rawMeasuring.baseSize * 1.71939080e-03 //0.04225225
        val bridge = rawMeasuring.bridge * -1.98344702e-01 // -0.32824007
        val eulerAngleX = rawMeasuring.eulerAngleX * 5.44432203e-04 //0.01248284
        val ve = rawMeasuring.ve * -5.46729793e-01 //-0.70619725
        val horizontalBridgeHoop = rawMeasuring.horizontalBridgeHoop * -2.02262816e-02 //-0.0414469
        val intercept = 17.335961398836012 //0.03830439014680679

        val diff = baseSize + bridge + eulerAngleX + ve + horizontalBridgeHoop + intercept
        val prediction = rawMeasuring.ipd + diff
        val rounded = round(prediction * 2.0) / 2.0

        return rounded
    }
}