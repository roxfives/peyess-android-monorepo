package com.peyess.salesapp.screen.sale.lens_pick.adapter

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.features.disponibility.contants.ReasonUnsupported

fun ReasonUnsupported.toStringResource(
    salesApplication: SalesApplication,
): String {
    return when (this) {
        ReasonUnsupported.LensTypeShouldBeMono ->
            salesApplication.getString(R.string.lens_incompatible_reason_required_multi)
        ReasonUnsupported.LensTypeShouldBeMulti ->
            salesApplication.getString(R.string.lens_incompatible_reason_required_mono)

        ReasonUnsupported.Diameter ->
            salesApplication.getString(R.string.lens_incompatible_reason_diameter)

        ReasonUnsupported.Height ->
            salesApplication.getString(R.string.lens_incompatible_reason_height)

        ReasonUnsupported.Addition ->
            salesApplication.getString(R.string.lens_incompatible_reason_addition)

        ReasonUnsupported.Cylindrical ->
            salesApplication.getString(R.string.lens_incompatible_reason_cylindrical)

        ReasonUnsupported.Spherical ->
            salesApplication.getString(R.string.lens_incompatible_reason_spherical)

        ReasonUnsupported.Prism ->
            salesApplication.getString(R.string.lens_incompatible_reason_prism)

        ReasonUnsupported.SumRule ->
            salesApplication.getString(R.string.lens_incompatible_reason_sum_rule_failed)
    }
}