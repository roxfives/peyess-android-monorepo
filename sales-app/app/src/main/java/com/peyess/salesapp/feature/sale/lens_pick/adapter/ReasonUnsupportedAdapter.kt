package com.peyess.salesapp.feature.sale.lens_pick.adapter

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.features.disponibility.contants.ReasonUnsupported

fun ReasonUnsupported.toStringResource(
    salesApplication: SalesApplication,
): String {
    return when (this) {
        ReasonUnsupported.DiameterLeft,
        ReasonUnsupported.DiameterRight ->
            salesApplication.getString(R.string.lens_incompatible_reason_diameter)

        ReasonUnsupported.HeightLeft,
        ReasonUnsupported.HeightRight ->
            salesApplication.getString(R.string.lens_incompatible_reason_height)

        ReasonUnsupported.LensTypeShouldBeMono ->
            salesApplication.getString(R.string.lens_incompatible_reason_required_mono)

        ReasonUnsupported.LensTypeShouldBeMulti ->
            salesApplication.getString(R.string.lens_incompatible_reason_required_multi)

        ReasonUnsupported.MaxAdditionLeft,
        ReasonUnsupported.MaxAdditionRight,
        ReasonUnsupported.MinAdditionLeft,
        ReasonUnsupported.MinAdditionRight ->
            salesApplication.getString(R.string.lens_incompatible_reason_addition)

        ReasonUnsupported.MaxCylindricalLeft,
        ReasonUnsupported.MaxCylindricalRight,
        ReasonUnsupported.MinCylindricalLeft,
        ReasonUnsupported.MinCylindricalRight ->
            salesApplication.getString(R.string.lens_incompatible_reason_cylindrical)

        ReasonUnsupported.MaxSphericalLeft,
        ReasonUnsupported.MaxSphericalRight,
        ReasonUnsupported.MinSphericalLeft,
        ReasonUnsupported.MinSphericalRight ->
            salesApplication.getString(R.string.lens_incompatible_reason_spherical)

        ReasonUnsupported.PrismLeft,
        ReasonUnsupported.PrismRight ->
            salesApplication.getString(R.string.lens_incompatible_reason_prism)

        ReasonUnsupported.SumRuleLeft,
        ReasonUnsupported.SumRuleRight ->
            salesApplication.getString(R.string.lens_incompatible_reason_sum_rule_failed)
    }
}