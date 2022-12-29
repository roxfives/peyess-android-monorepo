package com.peyess.salesapp.features.disponibility.contants

sealed class ReasonUnsupported {
    object LensTypeShouldBeMono: ReasonUnsupported()
    object LensTypeShouldBeMulti: ReasonUnsupported()

    object MaxSphericalLeft: ReasonUnsupported()
    object MaxSphericalRight: ReasonUnsupported()
    object MinSphericalLeft: ReasonUnsupported()
    object MinSphericalRight: ReasonUnsupported()

    object MaxCylindricalLeft: ReasonUnsupported()
    object MaxCylindricalRight: ReasonUnsupported()
    object MinCylindricalLeft: ReasonUnsupported()
    object MinCylindricalRight: ReasonUnsupported()

    object MaxAdditionLeft: ReasonUnsupported()
    object MaxAdditionRight: ReasonUnsupported()
    object MinAdditionLeft: ReasonUnsupported()
    object MinAdditionRight: ReasonUnsupported()

    object PrismLeft: ReasonUnsupported()
    object PrismRight: ReasonUnsupported()

    object HeightLeft: ReasonUnsupported()
    object HeightRight: ReasonUnsupported()

    object DiameterLeft: ReasonUnsupported()
    object DiameterRight: ReasonUnsupported()

    object SumRuleLeft: ReasonUnsupported()
    object SumRuleRight: ReasonUnsupported()
}
