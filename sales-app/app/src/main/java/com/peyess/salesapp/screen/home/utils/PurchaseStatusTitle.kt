package com.peyess.salesapp.screen.home.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
                borderColor = Color.Yellow,
                backgroundColor = Color.Yellow,
            )

        is PurchaseState.Confirmed ->
            PurchaseBadgeImpl(
                borderColor = Color.Blue,
                backgroundColor = Color.Blue,
            )

        is PurchaseState.Failed ->
            PurchaseBadgeImpl(
                borderColor = Color.Red,
                backgroundColor = Color.Red,
            )

        is PurchaseState.Cancelled ->
            PurchaseBadgeImpl(
                borderColor = Color.DarkGray,
                backgroundColor = Color.DarkGray,
            )

        is PurchaseState.Unknown ->
            PurchaseBadgeImpl(
                borderColor = Color.Gray,
                backgroundColor = Color.Gray,
            )

        is PurchaseState.FinishedSuccessfully ->
            PurchaseBadgeImpl(
                borderColor = Color.Green,
                backgroundColor = Color.Green,
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
                shape = RoundedCornerShape(100),
                border = BorderStroke(
                    width = 1.dp,
                    color = borderColor,
                ),
            )
            .background(color = backgroundColor)
    )
}
