package com.peyess.salesapp.data.model.local_sale.positioning

import android.net.Uri
import com.peyess.salesapp.feature.sale.frames.state.Eye

data class LocalPositioningDocument(
    val soId: String = "",
    val eye: Eye = Eye.None,

    val picture: Uri = Uri.EMPTY,

    val rotation: Double = 0.0,
    val device: String = "",

    val baseLeft: Double = 0.0,
    val baseLeftRotation: Double = 0.0,

    val baseRight: Double = 0.0,
    val baseRightRotation: Double = 0.0,

    val baseTop: Double = 0.0,
    val baseBottom: Double = 0.0,

    val topPointLength: Double =  0.0,
    val topPointRotation: Double = 0.0,

    val bottomPointLength: Double =  0.0,
    val bottomPointRotation: Double = 0.0,

    val bridgePivot: Double = 0.0,

    val checkBottom: Double = 0.0,
    val checkTop: Double = 0.0,
    val checkLeft: Double = 0.0,
    val checkLeftRotation: Double = 0.0,
    val checkMiddle: Double = 0.0,
    val checkRight: Double = 0.0,
    val checkRightRotation: Double = 0.0,

    val framesBottom: Double = 0.0,
    val framesLeft: Double = 0.0,
    val framesRight: Double = 0.0,
    val framesTop: Double = 0.0,

    val opticCenterRadius: Double = 0.0,
    val opticCenterX: Double = 0.0,
    val opticCenterY: Double = 0.0,

    val realParamHeight: Double = 0.0,
    val realParamWidth: Double = 0.0,

    val proportionToPictureHorizontal: Double = 0.0,
    val proportionToPictureVertical: Double = 0.0,

    val eulerAngleX: Double = 0.0,
    val eulerAngleY: Double = 0.0,
    val eulerAngleZ: Double = 0.0,
)
