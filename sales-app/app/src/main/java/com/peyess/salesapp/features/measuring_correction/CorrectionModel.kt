package com.peyess.salesapp.features.measuring_correction

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring

abstract class CorrectionModel {
    abstract fun fixField(rawMeasuring: RawMeasuring): Double
}