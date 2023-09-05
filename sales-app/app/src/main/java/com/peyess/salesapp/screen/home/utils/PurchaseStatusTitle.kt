package com.peyess.salesapp.screen.home.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peyess.salesapp.R
import com.peyess.salesapp.typing.sale.PurchaseState

@Composable
fun PurchaseState.displayName(): String {
    return when (this) {
        is PurchaseState.PendingConfirmation -> stringResource(
            R.string.purchase_state_display_name_pending
        )

        is PurchaseState.Confirmed -> stringResource(
            R.string.purchase_state_display_name_confirmed
        )

        is PurchaseState.Failed -> stringResource(
            R.string.purchase_state_display_name_failed
        )

        is PurchaseState.Cancelled -> stringResource(
            R.string.purchase_state_display_name_cancelled
        )

        is PurchaseState.Unknown -> stringResource(
            R.string.purchase_state_display_name_unknown
        )

        is PurchaseState.FinishedSuccessfully -> stringResource(
            R.string.purchase_state_display_name_finished
        )
    }
}

@Composable
fun PurchaseState.actionButtonTitle(): String {
    return when (this) {
        is PurchaseState.PendingConfirmation -> stringResource(
            R.string.purchase_action_button_edit
        )

        is PurchaseState.Confirmed,
        is PurchaseState.Failed,
        is PurchaseState.Cancelled,
        is PurchaseState.Unknown,
        is PurchaseState.FinishedSuccessfully, -> stringResource(
            R.string.purchase_action_button_view
        )
    }
}

@Composable
fun PurchaseState.PurchaseBadge() {
    return when (this) {
        is PurchaseState.PendingConfirmation ->
            PurchaseBadgeImpl(
                borderColor = Color(0, 150, 136),
                backgroundColor = Color(0, 150, 136),
            )

        is PurchaseState.Confirmed ->
            PurchaseBadgeImpl(
                borderColor = Color(89, 159, 197),
                backgroundColor = Color(89, 159, 197),
            )

        is PurchaseState.Failed ->
            PurchaseBadgeImpl(
                borderColor = MaterialTheme.colors.error,
                backgroundColor = MaterialTheme.colors.error,
            )

        is PurchaseState.Cancelled ->
            PurchaseBadgeImpl(
                borderColor = Color(160, 91, 17),
                backgroundColor = Color(160, 91, 17),
            )

        is PurchaseState.Unknown ->
            PurchaseBadgeImpl(
                borderColor = Color.DarkGray,
                backgroundColor = Color.DarkGray,
            )

        is PurchaseState.FinishedSuccessfully ->
            PurchaseBadgeImpl(
                borderColor = Color(41, 109, 43),
                backgroundColor = Color(41, 109, 43),
            )
    }
}

@Composable
private fun PurchaseBadgeImpl(
    borderColor: Color,
    backgroundColor: Color,
) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .border(
                width = 1.dp,
                shape = CircleShape,
                color = borderColor,
            )
            .background(
                color = backgroundColor,
                shape = CircleShape,
            )
    )
}


@Composable
fun PurchaseState.canFinishFromState(): Boolean {
    return when (this) {
        is PurchaseState.Confirmed,
        is PurchaseState.Cancelled,
        is PurchaseState.Unknown,
        is PurchaseState.FinishedSuccessfully, -> false
        else -> true
    }
}
