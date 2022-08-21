package com.peyess.salesapp.feature.sale.lens_pick

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import com.peyess.salesapp.feature.sale.lens_pick.model.name
import com.peyess.salesapp.feature.sale.lens_pick.state.LensPickViewModel
import com.peyess.salesapp.ui.component.card.ExpandableCard
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.peyess.salesapp.ui.theme.Shapes
import kotlinx.coroutines.flow.flow
import timber.log.Timber

@Composable
fun LensSuggestionScreen(
    modifier: Modifier = Modifier,
    onLensPicked: () -> Unit = {},
) {
    val viewModel: LensPickViewModel = mavericksViewModel()

    val lensList = viewModel.filteredLenses().collectAsLazyPagingItems()

    LensSuggestionScreenImpl(
        modifier = modifier,
        lensesSearch = lensList,
    )
}

@Composable
private fun LensSuggestionScreenImpl(
    modifier: Modifier = Modifier,
    suggestions: List<LensSuggestionModel> = listOf(),
    lensesSearch: LazyPagingItems<LensSuggestionModel>,
) {
    LensSuggestionList(
        lenses = lensesSearch,
    )
}

@Composable
private fun MainLensSuggestion(
    modifier: Modifier = Modifier,
    suggestions: List<LensSuggestionModel> = listOf(),
) {

}

@Composable
private fun MainLensSuggestionCard(
    modifier: Modifier = Modifier,
    suggestions: List<LensSuggestionModel> = listOf(),
) {

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LensSuggestionList(
    modifier: Modifier = Modifier,
    lenses: LazyPagingItems<LensSuggestionModel>,
) {
    val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    val headerHeight = 60.dp

    BackdropScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        frontLayerScrimColor = Color.Unspecified,

        appBar = {},

        backLayerContent = {
            Column(Modifier.fillMaxSize()) {
                Text("Testing field")
            }
        },
        frontLayerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                val listState = rememberLazyListState()

                Row(
                    modifier = Modifier
                        .height(headerHeight)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = stringResource(id = R.string.lens_suggestion_search_lenses),
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                ) {
                    items(lenses.itemCount) { index ->
                        Timber.i("Displaying $index for ${lenses.itemCount}")

                        lenses[index].let {
                            LensCard(lens = it!!)

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

//                    itemsIndexed(
//                        items = lenses,
//                        key = { _, item -> item.id },
//                    ) { _, item ->
//                        LensCard(lens = item)
//
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }
                }
            }
        },
        peekHeight = 40.dp,
        headerHeight = headerHeight,
        gesturesEnabled = true,
    )
}

@Composable
private fun LensCard(
    modifier: Modifier = Modifier,
    lens: LensSuggestionModel = LensSuggestionModel(),
) {
    ExpandableCard(
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
            shape = Shapes.medium,
        ),

        visibleContent = {
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LensCircle(
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
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
                    text = lens.name(),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.Gray
                    }
                )
            }
        },

        expandableContent = {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Divider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
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
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
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
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.Gray
                    }
                )


                Spacer(modifier = Modifier.height(24.dp))
                Divider(
                    modifier = Modifier.padding(horizontal = 120.dp),
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
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
                            color = if (lens.supportedDisponibilitites.isNotEmpty()) {
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
                            color = if (lens.supportedDisponibilitites.isNotEmpty()) {
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
                        color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                            MaterialTheme.colors.primary
                        } else {
                            Color.Gray
                        },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
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
                        installments = 10.0,
                        color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                            MaterialTheme.colors.primary.copy(alpha = 0.3f)
                        } else {
                            Color.Gray
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    TextButton(
                        enabled = lens.supportedDisponibilitites.isNotEmpty(),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            text = stringResource(id = R.string.lens_suggestion_select).uppercase(),
                            color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                                MaterialTheme.colors.primary.copy(alpha = 0.3f)
                            } else {
                                Color.Gray
                            }
                        )
                    }
                }

            }
        }
    )
}

@Preview
@Composable
private fun LensPrice(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    price: Double = 2500.0,
    installments: Double = 10.0,
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 28.dp,
                    vertical = 12.dp,
                )
                .align(Alignment.Center),
            text = "${price / installments}",
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

@Composable
private fun LensCircle(
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

@Preview
@Composable
private fun LensCirclePreview() {
    SalesAppTheme {
        LensCircle()
    }
}

@Preview
@Composable
private fun LensCardPreview() {
    SalesAppTheme {
        LensCard(
            modifier = Modifier.fillMaxWidth(),
            lens = LensSuggestionModel(
                supplier = "Zeiss",
                brand = "Acabadas",
                design = "DuraVIsion BlueProtect",
                material = "1.60",
                price = 10500.0,
                observation = "As lentes ZEISS EnergizeMe foram feitas para usuários de lentes de contato",
                explanations = listOf(
                    "Evitam o aumento da tensão ocular",
                    "Proteção premium contra os raios UV acima de 400nm",
                    "Alta tecnologia aliada a uma Visão mais nítida",
                )
            ),
        )
    }
}