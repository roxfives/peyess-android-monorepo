package com.peyess.salesapp.feature.prescription.utils

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.typing.lens.LensTypeCategoryName

fun messageFor(categoryName: LensTypeCategoryName?, application: SalesApplication): String {
    return when (categoryName) {
        LensTypeCategoryName.Far -> application.stringResource(R.string.mike_message_far)
        LensTypeCategoryName.Multi -> application.stringResource(R.string.mike_message_multi)
        LensTypeCategoryName.Near -> application.stringResource(R.string.mike_message_near)
        else -> application.stringResource(R.string.mike_message_default)
    }
}