package com.peyess.salesapp.feature.sale.lens_pick.ui

import android.icu.text.NumberFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.lens_pick.model.LensPickModel
import com.peyess.salesapp.ui.component.card.ExpandableCard
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.peyess.salesapp.ui.theme.Shapes
import java.math.BigDecimal

private val noFilterColor = Color.hsv(353f, 0.99f, 0.48f)
private val withFilterColor = Color.hsv(79f, 1f, 0.77f)

private val lensCardBorderWidth = 1.dp
private val lensCardBorderColor = @Composable { MaterialTheme.colors.primary.copy(alpha = 0.1f) }
private val lensCardBorderShape = Shapes.medium

@Composable
internal fun LensCard(
    modifier: Modifier = Modifier,
    lens: LensPickModel = LensPickModel(),
    onPickLens: (lensId: String) -> Unit = {},
) {
    ExpandableCard(
        modifier = modifier.border(
            width = lensCardBorderWidth,
            color = lensCardBorderColor(),
            shape = lensCardBorderShape,
        ),

        visibleContent = {
            LensCardMainContent(lens = lens)
        },

        expandableContent = {
            LensCardDetails(
                lens = lens,
                onPickLens = onPickLens,
            )
        }
    )
}

@Composable
private fun LensCardMainContent(
    modifier: Modifier = Modifier,
    lens: LensPickModel = LensPickModel(),
) {
    Column(
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LensStatus(
                color = if (lens.isAvailable) {
                    if (lens.needsCheck) {
                        Color.Yellow
                    } else {
                        Color.Green
                    }
                } else {
                    Color.Gray
                }
            )

            Spacer(modifier = Modifier.width(32.dp))

            Text(
                modifier = Modifier.weight(1f),
                text = lens.name,
                style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                color = if (lens.isAvailable) {
                    MaterialTheme.colors.primary
                } else {
                    Color.Gray
                }
            )
        }
    }
}

@Composable
private fun LensCardDetails(
    modifier: Modifier = Modifier,
    lens: LensPickModel = LensPickModel(),
    onPickLens: (lensId: String) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Divider(
            modifier = Modifier.padding(horizontal = 12.dp),
            color = if (lens.isAvailable) {
                MaterialTheme.colors.primary.copy(alpha = 0.3f)
            } else {
                Color.Gray
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = lens.supplier,
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold),
            color = if (lens.isAvailable) {
                MaterialTheme.colors.primary
            } else {
                Color.Gray
            },
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = lens.observation,
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Center),
            color = if (lens.isAvailable) {
                MaterialTheme.colors.primary
            } else {
                Color.Gray
            }
        )


        Spacer(modifier = Modifier.height(24.dp))
        Divider(
            modifier = Modifier.padding(horizontal = 120.dp),
            color = if (lens.isAvailable) {
                MaterialTheme.colors.primary.copy(alpha = 0.3f)
            } else {
                Color.Gray
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (lens.explanations.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    text = lens.explanations[0],
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                    color = if (lens.isAvailable) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.Gray
                    },
                )
            }

            if (lens.explanations.size >= 2) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    text = lens.explanations[1],
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                    color = if (lens.isAvailable) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.Gray
                    },
                )
            }
        }

        if (lens.explanations.size >= 3) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = lens.explanations[2],
                style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                color = if (lens.isAvailable) {
                    MaterialTheme.colors.primary
                } else {
                    Color.Gray
                },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider(
            modifier = Modifier.padding(horizontal = 120.dp),
            color = if (lens.isAvailable) {
                MaterialTheme.colors.primary.copy(alpha = 0.3f)
            } else {
                Color.Gray
            }
        )
        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (lens.hasFilterUv) {
                    Icons.Filled.WbTwilight
                } else {
                    Icons.Filled.WbSunny
                },
                tint = if (
                    lens.isNotAvailable
                    || !lens.hasFilterUv
                ) {
                    Color.Gray
                } else {
                    withFilterColor
                },
                contentDescription = "",
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = if (lens.hasFilterBlue) {
                    Icons.Filled.FlashlightOn
                } else {
                    Icons.Filled.FlashlightOff
                },
                tint = if (
                    lens.isNotAvailable
                    || !lens.hasFilterBlue
                ) {
                    Color.Gray
                } else {
                    withFilterColor
                },
                contentDescription = "",
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(
            modifier = Modifier.padding(horizontal = 12.dp),
            color = if (lens.isAvailable) {
                MaterialTheme.colors.primary.copy(alpha = 0.3f)
            } else {
                Color.Gray.copy(alpha = 0.3f)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // TODO: add installments from store config data
            LensPrice(
                price = lens.price,
                installments = BigDecimal(10.0),
                color = if (lens.isAvailable) {
                    MaterialTheme.colors.primary
                } else {
                    Color.Gray
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                enabled = lens.isAvailable,
                onClick = { onPickLens(lens.id) }
            ) {
                Text(
                    text = stringResource(id = R.string.lens_suggestion_select).uppercase(),
                    color = if (lens.isAvailable) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.Gray
                    }
                )
            }
        }

        if (
            lens.isNotAvailable
            && lens.reasonsUnavailable.isNotEmpty()
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = lens.reasonsUnavailable[0],
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}

@Composable
internal fun FilterButton(
    modifier: Modifier = Modifier,
    title: String = "",
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    OutlinedButton(
        modifier = modifier,
        border = BorderStroke(width = 4.dp, color = MaterialTheme.colors.onPrimary),
        colors = ButtonDefaults
            .buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                disabledBackgroundColor = Color.Gray.copy(alpha = 0.5f),
            ),
        enabled = enabled,
        onClick = onClick,
    ) {
        Text(text = title, color = MaterialTheme.colors.onPrimary)
    }
}

@Composable
private fun LensStatus(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
) {
    Box(
        modifier = modifier
            .background(color = color, shape = CircleShape)
            .border(
                border = BorderStroke(width = 2.dp, color = color),
                shape = CircleShape,
            )
            .width(10.dp)
            .height(10.dp),
    )
}

@Composable
private fun LensPrice(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    style: TextStyle = MaterialTheme.typography.h6,
    price: BigDecimal = BigDecimal(2500.0),
    installments: BigDecimal = BigDecimal(10.0),
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 28.dp,
                    vertical = 12.dp,
                )
                .align(Alignment.Center),
            // TODO: localize price symbol
            text = NumberFormat.getCurrencyInstance().format(price / installments),
            style = MaterialTheme.typography.h6,
            color = color,
        )

        Text(
            modifier = Modifier.align(Alignment.BottomEnd),
            text = "$installments X",
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
            color = color,
        )
    }
}

@Preview
@Composable
private fun FilterButtonPreview() {
    SalesAppTheme {
        FilterButton(modifier = Modifier.width(120.dp), title = "Fornecedor")
    }
}

@Preview
@Composable
private fun LensCardPreview() {
    SalesAppTheme {
        LensCard()
    }
}

@Preview
@Composable
private fun LensStatusPreview() {
    SalesAppTheme {
        LensStatus()
    }
}