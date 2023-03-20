package com.peyess.salesapp.feature.service_order

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.placeholder.material.placeholder
import com.peyess.salesapp.R
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument
import com.peyess.salesapp.feature.service_order.model.Client
import com.peyess.salesapp.feature.lens_suggestion.model.Measuring
import com.peyess.salesapp.feature.service_order.model.Coloring
import com.peyess.salesapp.feature.service_order.model.Frames
import com.peyess.salesapp.feature.service_order.model.Lens
import com.peyess.salesapp.feature.service_order.model.Payment
import com.peyess.salesapp.feature.service_order.model.Prescription
import com.peyess.salesapp.feature.service_order.model.Treatment
import com.peyess.salesapp.typing.prescription.PrismPosition
import com.peyess.salesapp.ui.annotated_string.pluralResource
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import kotlin.math.max

private val pictureSize = 60.dp
private val pictureSizePx = 60

private val prescriptionPictureSize = 160.dp
private val prescriptionPictureSizePx = 160

private val cardPadding = 16.dp
private val cardSpacerWidth = 2.dp
private val profilePicPadding = 8.dp

private val endingSpacerWidth = profilePicPadding

private val sectionPadding = 16.dp
private val subsectionSpacerSize = 16.dp

private val productSpacerSize = 8.dp

private val spaceBetweenMeasureValue = 8.dp

private val dialogSpacerHeight = 32.dp
private val infoTextPadding = 32.dp

@Composable
fun ServiceOrderUI(
    modifier: Modifier = Modifier,

    isUpdating: Boolean = false,

    canUpdate: Boolean = false,
    canUpdateMeasuring: Boolean = false,

    onFinishSale: () -> Unit = {},

    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },

    areUsersLoading: Boolean = false,
    user: Client = Client(),
    responsible: Client = Client(),
    hasWitness: Boolean = false,
    witness: Client = Client(),
    onChangeResponsible: () -> Unit = {},
    onChangeUser: () -> Unit = {},
    onChangeWitness: () -> Unit = {},

    isPrescriptionLoading: Boolean = false,
    prescription: Prescription = Prescription(),
    onEditPrescription: () -> Unit = {},

    onEditMeasure: () -> Unit = {},

    isProductLoading: Boolean = false,
    lens: Lens = Lens(),
    coloring: Coloring = Coloring(),
    treatment: Treatment = Treatment(),
    frames: Frames = Frames(),
    onEditProducts: () -> Unit = {},
    onAddDiscount: () -> Unit = {},

    isMeasureLoading: Boolean = false,
    measureLeft: Measuring = Measuring(),
    measureRight: Measuring = Measuring(),

    canAddNewPayment: Boolean = false,
    totalPaid: Double = 0.0,
    finalPrice: Double = 0.0,
    isPaymentLoading: Boolean = false,
    payments: List<Payment> = emptyList(),
    onAddPayment: () -> Unit = {},
    onAddPaymentFee: () -> Unit = {},
    onDeletePayment: (payment: Payment) -> Unit = {},
    onEditPayment: (payment: Payment) -> Unit = {},

    isGeneratingPdfDocument: Boolean = false,
    onGenerateServiceOrderPdf: () -> Unit = {},

    confirmationMessage: String = "",
) {
    val scrollState = rememberScrollState()

    val confirmationDialogState = rememberMaterialDialogState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(SalesAppTheme.dimensions.screen_offset)
            .verticalScroll(
                state = scrollState,
                enabled = true,
            ),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            ClientSection(
                isLoading = areUsersLoading,
                canUpdate = canUpdate,

                onChangeResponsible = onChangeResponsible,
                onChangeUser = onChangeUser,
                onChangeWitness = onChangeWitness,

                pictureForClient = pictureForClient,

                user = user,
                responsible = responsible,
                hasWitness = hasWitness,
                witness = witness,
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrescriptionSection(
                isLoading = isPrescriptionLoading,
                canUpdate = canUpdate,
                prescription = prescription,

                onEdit = onEditPrescription,
            )

            Spacer(modifier = Modifier.height(16.dp))

            MeasuresSection(
                isLoading = isMeasureLoading,

                canUpdate = canUpdateMeasuring,
                onEditMeasure = onEditMeasure,

                measureLeft = measureLeft,
                measureRight = measureRight,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProductsSection(
                isLoading = isProductLoading,
                canUpdate = canUpdate,

                onAddDiscount = onAddDiscount,
                onEditProducts = onEditProducts,

                lens = lens,
                coloring = coloring,
                treatment = treatment,
                frames = frames,
            )

            Spacer(modifier = Modifier.height(16.dp))

            PaymentSection(
                isLoading = isPaymentLoading,
                canUpdate = canUpdate,

                canAddNewPayment = canAddNewPayment,
                totalPaid = totalPaid,
                totalToPay = finalPrice,

                pictureForClient = pictureForClient,

                payments = payments,
                onAddPayment = onAddPayment,
                onAddPaymentFee = onAddPaymentFee,
                onDeletePayment = onDeletePayment,
                onEditPayment = onEditPayment,
            )

            Spacer(modifier = Modifier.height(16.dp))
            // TODO: use string resource
            PeyessStepperFooter(
                middle = {
                    ActionButtons(
                        isGeneratingDocument = isGeneratingPdfDocument,
                        onPrintServiceOrder = onGenerateServiceOrderPdf,
                    )
                },

                canGoNext = canUpdate,
                nextTitle = stringResource(
                    id = if (isUpdating) {
                        R.string.btn_update_sale
                    } else {
                        R.string.btn_finish_sale
                    }
                ),
                onNext = {
                     if (canUpdate) {
                         confirmationDialogState.show()
                     }
                },
            )


            ConfirmationDialog(
                dialogState = confirmationDialogState,
                confirmationMessage = confirmationMessage,
                onConfirm = onFinishSale,
            )
        }
    }
}

@Composable
private fun ConfirmationDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    confirmationMessage: String,
    onConfirm: () -> Unit = {},
) {
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            negativeButton(text = stringResource(id = R.string.so_confirmation_dialog_negative_btn))
            positiveButton(
                text = stringResource(id = R.string.so_confirmation_dialog_positive_btn),
                onClick = onConfirm,
            )
        }
    ) {
        title(stringResource(id = R.string.so_confirmation_dialog_title))
        message(confirmationMessage)
    }
}

@Composable
private fun ActionButtons(
    modifier: Modifier = Modifier,

    isGeneratingDocument: Boolean = false,
    canPrint: Boolean = true,
    onPrintServiceOrder: () -> Unit = {},
) {
    if (isGeneratingDocument) {
        CircularProgressIndicator()
    } else {
        val backgroundColor = if (canPrint) {
            MaterialTheme.colors.primary
        } else {
            Color.Gray
        }

        val iconColor = if (canPrint) {
            MaterialTheme.colors.onPrimary
        } else {
            Color.White
        }

        IconButton(
            modifier = modifier
                .background(
                    color = backgroundColor,
                    shape = CircleShape,
                ),
            onClick = onPrintServiceOrder,
        ) {
            Icon(
                imageVector = Icons.Filled.PictureAsPdf,
                contentDescription = "",
                tint = iconColor
            )
        }
    }
}

@Composable
private fun ClientSection(
    modifier: Modifier = Modifier,
    canUpdate: Boolean = false,

    isLoading: Boolean = false,

    onChangeResponsible: () -> Unit = {},
    onChangeUser: () -> Unit = {},
    onChangeWitness: () -> Unit = {},

    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },
    user: Client = Client(),
    responsible: Client = Client(),
    hasWitness: Boolean = false,
    witness: Client = Client(),
) {
    Column(modifier = modifier.padding(sectionPadding)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            SectionTitle(title = stringResource(id = R.string.so_section_title_clients))

            Spacer(modifier = Modifier.weight(1f))

//            IconButton(
//                onClick = { /*TODO*/ },
//                enabled = !isLoading,
//            ) {
//                Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
//            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        Column(modifier = Modifier.padding(subsectionSpacerSize)) {
            SubSectionTitle(
                title = stringResource(id = R.string.so_subsection_title_user)
            )

            ClientCard(
                pictureForClient = pictureForClient,
                canUpdate = canUpdate,
                client = user,
                isLoading = isLoading,
                onEditClient = onChangeUser,
            )

            Spacer(modifier = Modifier.height(16.dp))

            SubSectionTitle(
                title = stringResource(id = R.string.so_subsection_title_responsible)
            )

            ClientCard(
                pictureForClient = pictureForClient,
                canUpdate = canUpdate,
                client = responsible,
                isLoading = isLoading,
                onEditClient = onChangeResponsible,
            )

            if (hasWitness) {
                Spacer(modifier = Modifier.height(16.dp))

                SubSectionTitle(
                    title = stringResource(id = R.string.so_subsection_title_witness)
                )

                ClientCard(
                    pictureForClient = pictureForClient,
                    client = witness,
                    isLoading = isLoading,
                    onEditClient = onChangeWitness,
                )
            }
        }
    }
}

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String = "",
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
    )
}

@Composable
fun SubSectionTitle(
    modifier: Modifier = Modifier,
    title: String = "",
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
    )
}

@Preview
@Composable
fun SectionTitlePreview() {
    SalesAppTheme {
        SectionTitle(title = stringResource(id = R.string.so_section_title_clients))
    }
}

@Preview
@Composable
fun SubSectionTitlePreview() {
    SalesAppTheme {
        SubSectionTitle(title = stringResource(id = R.string.so_subsection_title_frames))
    }
}

@Composable
private fun ClientCard(
    modifier: Modifier = Modifier,
    canUpdate: Boolean = false,
    isLoading: Boolean = false,
    client: Client = Client(),
    onEditClient: () -> Unit = {},
    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },
) {
    val coroutineScope = rememberCoroutineScope()
    val pictureUri = remember { mutableStateOf(Uri.EMPTY) }
    LaunchedEffect(client) {
        coroutineScope.launch(Dispatchers.IO) {
            val picture = pictureForClient(client.id)

            pictureUri.value = picture
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(profilePicPadding)
                    .size(pictureSize)
                    // Clip image to be shaped as a circle
                    .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pictureUri.value)
                    .crossfade(true)
                    .size(width = pictureSizePx, height = pictureSizePx)
                    .build(),
                contentScale = ContentScale.FillBounds,
                contentDescription = "",
                error = painterResource(id = R.drawable.ic_profile_placeholder),
                fallback = painterResource(id = R.drawable.ic_profile_placeholder),
                placeholder = painterResource(id = R.drawable.ic_profile_placeholder),
            )

            Spacer(modifier = Modifier.width(cardSpacerWidth))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(cardPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .placeholder(visible = isLoading),
                    text = client.name,
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold)
                )

                Row(
                    Modifier.height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxHeight()
                            .placeholder(visible = isLoading),
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "",
                    )

                    Text(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .placeholder(visible = isLoading),
                        text = client.shortAddress,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = canUpdate,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                OutlinedButton(
                    modifier = Modifier.height(SalesAppTheme.dimensions.minimum_touch_target),
                    enabled = !isLoading,
                    onClick = onEditClient,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.btn_change_client),
                    )
                }
            }

            Spacer(modifier = Modifier.width(endingSpacerWidth))
        }
    }
}

@Composable
private fun PrescriptionSection(
    modifier: Modifier = Modifier,
    canUpdate: Boolean = false,
    isLoading: Boolean = false,

    onEdit: () -> Unit = {},

    prescription: Prescription = Prescription(),
) {
    Column(
        modifier = modifier
            .padding(sectionPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SectionTitle(
                title = stringResource(id = R.string.so_section_title_prescription)
            )

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = canUpdate,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                IconButton(
                    onClick = onEdit,
                    enabled = !isLoading,
                ) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
                }
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                AsyncImage(
                    modifier = Modifier
                        .padding(profilePicPadding)
                        .size(prescriptionPictureSize)
                        // Clip image to be shaped as a circle
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colors.primary,
                            shape = CircleShape,
                        ).clip(CircleShape)
                        .align(Alignment.Center),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(prescription.pictureUri)
                        .crossfade(true)
                        .size(
                            width = prescriptionPictureSizePx,
                            height = prescriptionPictureSizePx
                        )
                        .build(),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "",
                    error = painterResource(id = R.drawable.ic_default_placeholder),
                    fallback = painterResource(id = R.drawable.ic_default_placeholder),
                    placeholder = painterResource(id = R.drawable.ic_default_placeholder),
                )
            }

            if (prescription.isCopy) {
                // TOOD: use string resource
                Text(
                    modifier = Modifier.weight(2f),
                    text = "Cópia a partir do lensômetro",
                    style = MaterialTheme.typography.h6
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )
            } else {
                Row(
                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            modifier = Modifier.width(IntrinsicSize.Max),
                            text = stringResource(id = R.string.so_prescription_emission),
                            style = MaterialTheme.typography.body1
                                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.End),
                        )

                        Text(
                            modifier = Modifier.width(IntrinsicSize.Max),
                            text = stringResource(id = R.string.so_prescription_name),
                            style = MaterialTheme.typography.body1
                                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.End),
                        )

                        Text(
                            modifier = Modifier.width(IntrinsicSize.Max),
                            text = stringResource(id = R.string.so_prescription_id),
                            style = MaterialTheme.typography.body1
                                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.End),
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        val date = remember {
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                .format(prescription.prescriptionDate)
                        }

                        Text(
                            modifier = Modifier.width(IntrinsicSize.Max),
                            text = date,
                            style = MaterialTheme.typography.body1
                                .copy(textAlign = TextAlign.Start),
                        )

                        Text(
                            modifier = Modifier.width(IntrinsicSize.Max),
                            text = prescription.professionalName,
                            style = MaterialTheme.typography.body1
                                .copy(textAlign = TextAlign.Start),
                        )

                        Text(
                            modifier = Modifier.width(IntrinsicSize.Max),
                            text = prescription.professionalId,
                            style = MaterialTheme.typography.body1
                                .copy(textAlign = TextAlign.Start),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(subsectionSpacerSize))
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )
        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        Column(modifier = Modifier.fillMaxWidth()) {
            val density = LocalDensity.current
            val minimumWidthState = remember { MinimumWidthState() }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    )
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = stringResource(id = R.string.degree_spherical),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = stringResource(id = R.string.degree_cylindrical),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = stringResource(id = R.string.degree_axis),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Spacer(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = stringResource(id = R.string.right_eye),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = "%.2f".format(prescription.sphericalRight),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = "%.2f".format(prescription.cylindricalRight),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    // TODO: create a property 'hasAxisRight' in the data class returned by the repository when it's built
                    text = if (prescription.cylindricalRight < 0) {
                        "%.2f".format(prescription.axisRight)
                    } else {
                        "-"
                    },
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Spacer(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = stringResource(id = R.string.left_eye),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = "%.2f".format(prescription.sphericalLeft),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = "%.2f".format(prescription.cylindricalLeft),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    // TODO: create a property 'hasAxisLeft' in the data class returned by the repository when it's built
                    text = if (prescription.cylindricalLeft < 0) {
                        "%.2f".format(prescription.axisLeft)
                    } else {
                        "-"
                    },
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Spacer(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    )
                )
            }

            Spacer(modifier = Modifier.size(subsectionSpacerSize))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    )
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = stringResource(id = R.string.title_addition),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = stringResource(id = R.string.title_prism),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = stringResource(id = R.string.prism_position),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = stringResource(id = R.string.prism_axis),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = stringResource(id = R.string.right_eye),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = if (prescription.hasAddition) {
                        "%.2f".format(prescription.additionRight)
                    } else {
                        // TODO: use string resource
                        "-"
                    },
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = if (prescription.hasPrism) {
                        "%.2f".format(prescription.prismDegreeRight)
                    } else {
                        // TODO: use string resource
                        "-"
                    },
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = if (prescription.hasPrism) {
                        PrismPosition.toName(prescription.prismPositionRight)
                    } else {
                        // TODO: use string resource
                        "-"
                    },
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = if (
                        prescription.hasPrism
                        && prescription.prismPositionRight == PrismPosition.Axis
                    ) {
                        "%.2fº".format(prescription.prismAxisRight)
                    } else {
                        // TODO: use string resource
                        "-"
                    },
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = stringResource(id = R.string.left_eye),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = if (prescription.hasAddition) {
                        "%.2f".format(prescription.additionLeft)
                    } else {
                        // TODO: use string resource
                        "-"
                    },
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = if (prescription.hasPrism) {
                        "%.2f".format(prescription.prismDegreeLeft)
                    } else {
                        // TODO: use string resource
                        "-"
                    },
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = if (prescription.hasPrism) {
                        PrismPosition.toName(prescription.prismPositionLeft)
                    } else {
                        // TODO: use string resource
                        "-"
                    },
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = if (
                        prescription.hasPrism
                        && prescription.prismPositionLeft == PrismPosition.Axis
                    ) {
                        "%.2fº".format(prescription.prismAxisLeft)
                    } else {
                        // TODO: use string resource
                        "-"
                    },
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )
            }
        }
    }
}

@Composable
private fun MeasuresSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,

    canUpdate: Boolean = true,
    onEditMeasure: () -> Unit = {},

    measureLeft: Measuring = Measuring(),
    measureRight: Measuring = Measuring(),
) {
    Column(
        modifier = modifier
            .padding(sectionPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        val density = LocalDensity.current
        val minimumWidthState = remember { MinimumWidthState() }

        val infoDialogState = rememberMaterialDialogState()
        InfoDialog(
            title = stringResource(id = R.string.so_measure_info_title),
            dialogState = infoDialogState,
            infoContent = stringResource(id = R.string.so_measure_info_content),
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            SectionTitle(title = stringResource(id = R.string.so_section_title_measure))

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { infoDialogState.show() }) {
                Icon(imageVector = Icons.Filled.Info, contentDescription = "")
            }

            IconButton(
                onClick = onEditMeasure,
                enabled = !isLoading && canUpdate,
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Spacer(
                        modifier= Modifier
                            .minimumWidthModifier(state = minimumWidthState, density = density)
                    )

                    Text(
                        modifier = Modifier
                            .minimumWidthModifier(state = minimumWidthState, density = density),
                        text = stringResource(id = R.string.so_ipd),
                        style = MaterialTheme.typography.body1
                            .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                    )

                    Text(
                        modifier = Modifier
                            .minimumWidthModifier(state = minimumWidthState, density = density),
                        text = stringResource(id = R.string.so_height),
                        style = MaterialTheme.typography.body1
                            .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(
                        modifier = Modifier
                            .minimumWidthModifier(state = minimumWidthState, density = density),
                        text = stringResource(id = R.string.right_eye),
                        style = MaterialTheme.typography.body1
                            .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                    )

                    Text(
                        modifier = Modifier
                            .minimumWidthModifier(state = minimumWidthState, density = density),
                        text = "%.2f".format(measureRight.fixedIpd),
                        style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                    )

                    Text(
                        modifier = Modifier
                            .minimumWidthModifier(state = minimumWidthState, density = density),
                        text = "%.2f".format(measureRight.fixedHe),
                        style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(
                        modifier = Modifier
                            .minimumWidthModifier(state = minimumWidthState, density = density),
                        text = stringResource(id = R.string.left_eye),
                        style = MaterialTheme.typography.body1
                            .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                    )

                    Text(
                        modifier = Modifier
                            .minimumWidthModifier(state = minimumWidthState, density = density),
                        text = "%.2f".format(measureLeft.fixedIpd),
                        style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                    )

                    Text(
                        modifier = Modifier
                            .minimumWidthModifier(state = minimumWidthState, density = density),
                        text = "%.2f".format(measureLeft.fixedHe),
                        style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.so_diameter))
                    Spacer(modifier = Modifier.width(spaceBetweenMeasureValue))
                    Text(text = "%.2f".format(max(measureLeft.fixedDiameter, measureRight.fixedDiameter)))
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.so_bridge_hoop))
                    Spacer(modifier = Modifier.width(spaceBetweenMeasureValue))
                    Text(
                        text = "%.2f".format(
                            max(measureLeft.fixedHorizontalBridgeHoop, measureRight.fixedHorizontalBridgeHoop)
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.so_vertical_hoop))
                    Spacer(modifier = Modifier.width(spaceBetweenMeasureValue))
                    Text(
                        text = "%.2f".format(
                            max(measureLeft.fixedVHoop, measureRight.fixedVHoop)
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.so_horizontal_hoop))
                    Spacer(modifier = Modifier.width(spaceBetweenMeasureValue))
                    Text(
                        text = "%.2f".format(
                            max(measureLeft.fixedHHoop, measureRight.fixedHHoop)
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.so_bridge))
                    Spacer(modifier = Modifier.width(spaceBetweenMeasureValue))
                    Text(
                        text = "%.2f".format(
                            max(measureLeft.fixedBridge, measureRight.fixedBridge)
                        )
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    text = stringResource(id = R.string.so_measure_status_title),
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                )

                Spacer(modifier = Modifier.height(subsectionSpacerSize))

                Text(
                    text = stringResource(id = R.string.so_measure_status_pending),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Spacer(modifier = Modifier.height(subsectionSpacerSize))

                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.so_btn_measure_confirm))
                }
            }
        }
    }
}

@Composable
private fun ProductsSection(
    modifier: Modifier = Modifier,
    canUpdate: Boolean = false,
    isLoading: Boolean = false,

    onEditProducts: () -> Unit = {},
    onAddDiscount: () -> Unit = {},

    lens: Lens = Lens(),
    coloring: Coloring = Coloring(),
    treatment: Treatment = Treatment(),
    frames: Frames = Frames(),
) {
    Column(
        modifier = modifier
            .padding(sectionPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        val density = LocalDensity.current

        val minimumTitleWidthState = remember { MinimumWidthState() }
        val minimumPriceWidthState = remember { MinimumWidthState() }

        val minimumTitleModifier = Modifier
            .minimumWidthModifier(state = minimumTitleWidthState, density = density)
        val minimumPriceModifier = Modifier
            .minimumWidthModifier(state = minimumPriceWidthState, density = density)

        val coloringPrice = if (lens.isColoringDiscounted || lens.isColoringIncluded) {
            0.0
        } else {
            coloring.price
        }

        val treatmentPrice = if (lens.isTreatmentDiscounted || lens.isTreatmentIncluded) {
            0.0
        } else {
            treatment.price
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            SectionTitle(title = stringResource(id = R.string.so_section_title_products))

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = canUpdate,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = onAddDiscount,
                        enabled = !isLoading,
                    ) {
                        Icon(imageVector = Icons.Filled.Discount, contentDescription = "")
                    }

                    IconButton(
                        onClick = onEditProducts,
                        enabled = !isLoading,
                    ) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
                    }
                }
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        // TODO: use string resource
        SubSectionTitle(title = "Olho esquerdo")
        Spacer(modifier = Modifier.size(productSpacerSize))
        LensCard(
            lensEntity = lens,
            minPriceModifier = minimumPriceModifier,
            minTitleModifier = minimumTitleModifier,
        )

        if (!lens.isColoringIncluded) {
            Spacer(modifier = Modifier.size(productSpacerSize))

            ColoringCard(
                coloringEntity = coloring,
                minPriceModifier = minimumPriceModifier,
                minTitleModifier = minimumTitleModifier,
            )
        }

        if (!lens.isTreatmentIncluded) {
            Spacer(modifier = Modifier.size(productSpacerSize))
            TreatmentCard(
                treatmentEntity = treatment,
                minPriceModifier = minimumPriceModifier,
                minTitleModifier = minimumTitleModifier,
            )
        }

        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        // TODO: use string resource
        SubSectionTitle(title = "Olho direito")
        Spacer(modifier = Modifier.size(productSpacerSize))
        LensCard(
            lensEntity = lens,
            minPriceModifier = minimumPriceModifier,
            minTitleModifier = minimumTitleModifier,
        )

        if (!lens.isColoringIncluded) {
            Spacer(modifier = Modifier.size(productSpacerSize))
            ColoringCard(
                coloringEntity = coloring,
                minPriceModifier = minimumPriceModifier,
                minTitleModifier = minimumTitleModifier,
            )
        }

        if (!lens.isTreatmentIncluded) {
            Spacer(modifier = Modifier.size(productSpacerSize))
            TreatmentCard(
                treatmentEntity = treatment,
                minPriceModifier = minimumPriceModifier,
                minTitleModifier = minimumTitleModifier,
            )
        }

        if (frames.areFramesNew) {
            Spacer(modifier = Modifier.size(subsectionSpacerSize))

            // TODO: use string resource
            SubSectionTitle(title = "Armação")
            FramesCard(frames = frames)
        }

        if (
            (lens.priceAddColoring > 0 && coloringPrice > 0)
            || (lens.priceAddTreatment > 0 && treatmentPrice > 0)
        ) {
            Spacer(modifier = Modifier.size(subsectionSpacerSize))

            // TODO: use string resource
            SubSectionTitle(title = "Outros")

            if (lens.priceAddColoring > 0) {
                MiscCard(
                    miscProduct = ProductSoldDescriptionDocument(
                        nameDisplay = "Adicional por coloração",
                        price = lens.priceAddColoring,
                    )
                )
            }
            if (lens.priceAddTreatment > 0) {
                MiscCard(
                    miscProduct = ProductSoldDescriptionDocument(
                        nameDisplay = "Adicional por tratamento",
                        price = lens.priceAddTreatment,
                    )
                )
            }
        }
    }
}

@Composable
private fun LensCard(
    modifier: Modifier = Modifier,
    minTitleModifier: Modifier = Modifier,
    minPriceModifier: Modifier = Modifier,
    lensEntity: Lens = Lens(),
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // TODO: use string resource
        Text(
            modifier = minTitleModifier,
            text = "Lente",
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = lensEntity.name,
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = minPriceModifier,
            text = "R$ %.2f".format(lensEntity.price / 2f),
            style = MaterialTheme.typography.body1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Start),
        )
    }
}

@Composable
private fun ColoringCard(
    modifier: Modifier = Modifier,
    minTitleModifier: Modifier = Modifier,
    minPriceModifier: Modifier = Modifier,
    coloringEntity: Coloring = Coloring(),
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // TODO: use string resource
        Text(
            modifier = minTitleModifier,
            text = "Coloração",
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = coloringEntity.name,
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // TODO: change suggestedPrice to price
        Text(
            modifier = minPriceModifier,
            text = NumberFormat.getCurrencyInstance().format(coloringEntity.price / 2f),
            style = MaterialTheme.typography.body1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Start),
        )
    }
}

@Composable
private fun TreatmentCard(
    modifier: Modifier = Modifier,
    minTitleModifier: Modifier = Modifier,
    minPriceModifier: Modifier = Modifier,
    treatmentEntity: Treatment = Treatment(),
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // TODO: use string resource
        Text(
            modifier = minTitleModifier,
            text = "Tratamento", style = MaterialTheme.typography.body1
                .copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = treatmentEntity.name,
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = minPriceModifier,
            text = NumberFormat.getCurrencyInstance().format(treatmentEntity.price / 2f),
            style = MaterialTheme.typography.body1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Start),
        )
    }
}

@Composable
private fun FramesCard(
    modifier: Modifier = Modifier,
    minTitleModifier: Modifier = Modifier,
    minPriceModifier: Modifier = Modifier,
    frames: Frames = Frames(),
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // TODO: use string resource
        Text(
            modifier = minTitleModifier,
            text = "Armação",
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = frames.name,
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = minPriceModifier,
            text = NumberFormat.getCurrencyInstance().format(frames.value),
            style = MaterialTheme.typography.body1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Start),
        )
    }
}

@Composable
private fun MiscCard(
    modifier: Modifier = Modifier,
    minTitleModifier: Modifier = Modifier,
    minPriceModifier: Modifier = Modifier,
    miscProduct: ProductSoldDescriptionDocument = ProductSoldDescriptionDocument(),
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // TODO: use string resource
//        Text(
//            modifier = minTitleModifier,
//            text = "Outro",
//            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
//        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = miscProduct.nameDisplay,
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = minPriceModifier,
            text = NumberFormat.getCurrencyInstance().format(miscProduct.price),
            style = MaterialTheme.typography.body1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Start),
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PaymentSection(
    modifier: Modifier = Modifier,
    canUpdate: Boolean = false,

    canAddNewPayment: Boolean = false,
    totalPaid: Double = 0.0,
    totalToPay: Double = 0.0,

    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },

    isLoading: Boolean = false,
    payments: List<Payment> = emptyList(),
    onAddPayment: () -> Unit = {},
    onAddPaymentFee: () -> Unit = {},
    onDeletePayment: (payment: Payment) -> Unit = {},
    onEditPayment: (payment: Payment) -> Unit = {},
) {
    val paid = NumberFormat.getCurrencyInstance().format(totalPaid)
    val price = NumberFormat.getCurrencyInstance().format(totalToPay)

    Column(
        modifier = modifier.padding(sectionPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            SectionTitle(title = stringResource(id = R.string.so_section_title_payments))

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = canUpdate,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onAddPaymentFee) {
                        Icon(imageVector = Icons.Filled.Paid, contentDescription = "")
                    }

                    IconButton(
                        onClick = onAddPayment,
                        enabled = !isLoading && canAddNewPayment,
                    ) {
                        Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "")
                    }
                }
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "$paid / $price",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            )

            Divider(
                modifier = Modifier.padding(horizontal = 120.dp),
                color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
            )
        }

        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        AnimatedVisibility(
            visible = isLoading,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            CircularProgressIndicator()
        }

        AnimatedVisibility(
            visible = !isLoading && payments.isEmpty(),
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            NoPaymentsYet(modifier = Modifier.fillMaxWidth())
        }

        AnimatedVisibility(
            visible = !isLoading && payments.isNotEmpty(),
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                for (payment in payments) {
                    PaymentCard(
                        modifier = Modifier.fillMaxWidth(),
                        canUpdate = canUpdate,

                        pictureForClient = pictureForClient,

                        payment = payment,
                        onDeletePayment = onDeletePayment,
                        onEditPayment = onEditPayment,
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentCard(
    modifier: Modifier = Modifier,
    canUpdate: Boolean = false,
    payment: Payment = Payment(),
    onEditPayment: (payment: Payment) -> Unit = {},
    onDeletePayment: (payment: Payment) -> Unit = {},
    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },
) {
    val coroutineScope = rememberCoroutineScope()
    val pictureUri = remember { mutableStateOf(Uri.EMPTY) }
    LaunchedEffect(payment) {
        coroutineScope.launch(Dispatchers.IO) {
            val picture = pictureForClient(payment.clientId)

            pictureUri.value = picture
        }
    }

    Row(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(profilePicPadding)
                .size(pictureSize)
                // Clip image to be shaped as a circle
                .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(pictureUri.value)
                .crossfade(true)
                .size(width = pictureSizePx, height = pictureSizePx)
                .build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = "",
            error = painterResource(id = R.drawable.ic_profile_placeholder),
            fallback = painterResource(id = R.drawable.ic_profile_placeholder),
            placeholder = painterResource(id = R.drawable.ic_profile_placeholder),
        )

        Spacer(modifier = Modifier.width(cardSpacerWidth))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(cardPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 4.dp),
                text = payment.clientName,
                style = MaterialTheme.typography.body1
                    .copy(fontWeight = FontWeight.Bold)
            )

            Row(
                Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxHeight(),
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "",
                )

                Text(
                    modifier = Modifier
                        .padding(vertical = 4.dp),
                    text = payment.clientAddress,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.width(120.dp),
            text = pluralResource(
                resId = R.plurals.so_payment_description,
                quantity = payment.installments,
                formatArgs = listOf(
                    NumberFormat.getCurrencyInstance().format(payment.value),
                    payment.methodName,
                    payment.installments,
                ).toTypedArray()
            ),
            style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
        )

        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            visible = canUpdate,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { onEditPayment(payment) }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { onDeletePayment(payment) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        tint = MaterialTheme.colors.error,
                        contentDescription = "",
                    )
                }
            }
        }
    }
}

@Composable
private fun NoPaymentsYet(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_empty)
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        LottieAnimation(
            modifier = modifier
                .height(169.dp)
                .width(338.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.so_no_payments_added_yet),
            style = MaterialTheme.typography.h6
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
        )
    }
}

@Composable
private fun InfoDialog(
    modifier: Modifier = Modifier,

    dialogState: MaterialDialogState = rememberMaterialDialogState(),

    title: String = "",
    infoContent: String = "",
    infoObs: String = "",
) {
    // TODO: use string resource
    MaterialDialog(
        dialogState = dialogState,
        buttons = { positiveButton("Legal!") },
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            title(title)

            Text(
                modifier = Modifier.padding(horizontal = infoTextPadding),
                text = infoContent,
                style = MaterialTheme.typography.body1
                    .copy(textAlign = TextAlign.Center),
            )

            if (infoObs.isNotBlank()) {
                Spacer(modifier = Modifier.height(dialogSpacerHeight))

                Text(
                    modifier = Modifier.padding(horizontal = infoTextPadding),
                    text = infoObs,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PaymentCardPreview() {
    SalesAppTheme {
        PaymentCard(
            modifier = Modifier.fillMaxWidth(),
            payment = Payment(
                clientName = "Nome do Ciente",
                clientAddress = "Mora aqui, mesmo",
                installments = 2,
                methodName = "Débito",
            )
        )
    }
}

@Preview
@Composable
private fun PaymentSectionPreview() {
    SalesAppTheme {
        PaymentSection()
    }
}

@Preview
@Composable
private fun MeasuresSectionPreview() {
    SalesAppTheme {
        MeasuresSection(modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun LensCardPreview() {
    SalesAppTheme {
        LensCard(
            modifier = Modifier.fillMaxWidth(),
            lensEntity =  Lens(
                brandName = "Família",
                designName = "Descrição",
                techName = "Tecnologia",
                materialName = "Material",
                price = 1250.0,
            )
        )
    }
}

@Preview
@Composable
private fun ColoringCardPreview() {
    SalesAppTheme {
        ColoringCard(
            modifier = Modifier.fillMaxWidth(),
            coloringEntity = Coloring(
                brand = "Família",
                design = "Descrição",
                price = 1250.0,
            )
        )
    }
}

@Preview
@Composable
private fun TreatmentCardPreview() {
    SalesAppTheme {
        TreatmentCard(
            modifier = Modifier.fillMaxWidth(),
            treatmentEntity = Treatment(
                brand = "Família",
                design = "Descrição",
                price = 1250.0,
            )
        )
    }
}

@Preview
@Composable
private fun FramesCardPreview() {
    SalesAppTheme {
        FramesCard(
            modifier = Modifier.fillMaxWidth(),
            frames = Frames(
                design = "Descrição",
                reference = "Referência",
                tagCode = "XXXXXX",
                value = 450.0,
            )
        )
    }
}

@Preview
@Composable
private fun ProductsSectionPreview() {
    SalesAppTheme {
        ProductsSection(
            lens = Lens(
                priceAddColoring = 20.0,
                priceAddTreatment = 20.0,
            ),

            frames = Frames(
                areFramesNew = true,
            )
        )
    }
}

@Preview
@Composable
private fun PrescriptionSectionPreview() {
    SalesAppTheme {
        PrescriptionSection()
    }
}

@Preview
@Composable
private fun ClientSectionPreview() {
    SalesAppTheme {
        ClientSection(
            user = Client(name = "João da Silva", shortAddress = "São Paulo, SP"),
            responsible = Client(name = "João da Silva", shortAddress = "São Paulo, SP"),
        )
    }
}

