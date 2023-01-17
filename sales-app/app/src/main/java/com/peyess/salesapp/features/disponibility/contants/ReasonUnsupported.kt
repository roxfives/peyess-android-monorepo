package com.peyess.salesapp.features.disponibility.contants

sealed class ReasonUnsupported {
    object LensTypeShouldBeMono: ReasonUnsupported()
    object LensTypeShouldBeMulti: ReasonUnsupported()

    object SphericalLeft: ReasonUnsupported()
    object SphericalRight: ReasonUnsupported()

    object CylindricalLeft: ReasonUnsupported()
    object CylindricalRight: ReasonUnsupported()

    object AdditionLeft: ReasonUnsupported()
    object AdditionRight: ReasonUnsupported()

    object PrismLeft: ReasonUnsupported()
    object PrismRight: ReasonUnsupported()

    object HeightLeft: ReasonUnsupported()
    object HeightRight: ReasonUnsupported()

    object DiameterLeft: ReasonUnsupported()
    object DiameterRight: ReasonUnsupported()

    object SumRuleLeft: ReasonUnsupported()
    object SumRuleRight: ReasonUnsupported()
}
