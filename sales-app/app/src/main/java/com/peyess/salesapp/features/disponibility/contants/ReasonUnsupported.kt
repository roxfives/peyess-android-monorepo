package com.peyess.salesapp.features.disponibility.contants

sealed class ReasonUnsupported {
    object LensTypeShouldBeMono: ReasonUnsupported()
    object LensTypeShouldBeMulti: ReasonUnsupported()

    object Height: ReasonUnsupported()
    object Diameter: ReasonUnsupported()
    object Spherical: ReasonUnsupported()
    object Cylindrical: ReasonUnsupported()
    object Addition: ReasonUnsupported()
    object Prism: ReasonUnsupported()
    object SumRule: ReasonUnsupported()
}
