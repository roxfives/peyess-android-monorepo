package com.peyess.salesapp.feature.sale.service_order

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material.Button
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.placeholder.material.placeholder
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.client.room.ClientEntity
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_coloring.name
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_lens.name
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.products.room.local_treatment.name
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames.name
import com.peyess.salesapp.dao.sale.payment.SalePaymentEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrismPosition
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.feature.sale.lens_pick.model.Measuring
import com.peyess.salesapp.feature.sale.service_order.state.ServiceOrderState
import com.peyess.salesapp.feature.sale.service_order.state.ServiceOrderViewModel
import com.peyess.salesapp.ui.annotated_string.pluralResource
import com.peyess.salesapp.ui.component.footer.PeyessNextStep
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.theme.SalesAppTheme
import java.lang.Double.max
import java.text.NumberFormat

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

@Composable
fun ServiceOrderScreen(
    modifier: Modifier = Modifier,

    onChangeUser: () -> Unit = {},
    onChangeResponsible: () -> Unit = {},
    onChangeWitness: () -> Unit = {},

    onEditPrescription: () -> Unit = {},

    onEditProducts: () -> Unit = {},

    onConfirmMeasure: () -> Unit = {},

    onAddPayment: (paymentId: Long) -> Unit = {},
    onEditPayment: (paymentId: Long, clientId: String) -> Unit = { _, _ -> },

    onGenerateBudget: () -> Unit = {},
    onFinishSale: () -> Unit = {},
) {
    val viewModel: ServiceOrderViewModel = mavericksViewModel()

    val user by viewModel.collectAsState(ServiceOrderState::userClient)
    val responsible by viewModel.collectAsState(ServiceOrderState::responsibleClient)
    val witness by viewModel.collectAsState(ServiceOrderState::witnessClient)

    val userIsLoading by viewModel.collectAsState(ServiceOrderState::isUserLoading)
    val responsibleIsLoading by viewModel.collectAsState(ServiceOrderState::isResponsibleLoading)
    val witnessIsLoading by viewModel.collectAsState(ServiceOrderState::isWitnessLoading)

    val prescriptionPicture by viewModel.collectAsState(ServiceOrderState::prescriptionPicture)
    val isPrescriptionPictureLoading by viewModel.collectAsState(ServiceOrderState::isPrescriptionPictureLoading)

    val prescriptionData by viewModel.collectAsState(ServiceOrderState::prescriptionData)
    val isPrescriptionDataLoading by viewModel.collectAsState(ServiceOrderState::isPrescriptionDataLoading)

    val lensEntity by viewModel.collectAsState(ServiceOrderState::lensEntity)
    val coloringEntity by viewModel.collectAsState(ServiceOrderState::coloringEntity)
    val treatmentEntity by viewModel.collectAsState(ServiceOrderState::treatmentEntity)
    val framesEntity by viewModel.collectAsState(ServiceOrderState::framesEntity)

    val isLensLoading by viewModel.collectAsState(ServiceOrderState::isLensLoading)
    val isColoringLoading by viewModel.collectAsState(ServiceOrderState::isColoringLoading)
    val isTreatmentLoading by viewModel.collectAsState(ServiceOrderState::isTreatmentLoading)
    val isFramesLoading by viewModel.collectAsState(ServiceOrderState::isFramesLoading)

    val isPositioningLeftLoading by viewModel.collectAsState(ServiceOrderState::isPositioningLeftLoading)
    val measureLeft by viewModel.collectAsState(ServiceOrderState::measureLeft)
    val isPositioningRightLoading by viewModel.collectAsState(ServiceOrderState::isPositioningRightLoading)
    val measureRight by viewModel.collectAsState(ServiceOrderState::measureRight)

    val isPaymentsLoading by viewModel.collectAsState(ServiceOrderState::isPaymentLoading)
    val payments by viewModel.collectAsState(ServiceOrderState::payments)

    val canAddNewPayment by viewModel.collectAsState(ServiceOrderState::canAddNewPayment)
    val isTotalPaidLoading by viewModel.collectAsState(ServiceOrderState::isTotalPaidLoading)
    val totalPaid by viewModel.collectAsState(ServiceOrderState::totalPaid)
    val totalToPay by viewModel.collectAsState(ServiceOrderState::totalToPay)

    ServiceOrderScreenImpl(
        modifier = modifier,

        onFinishSale = {
            viewModel.generateSale()
            onFinishSale()
        },

        onChangeResponsible = onChangeResponsible,
        onChangeUser = onChangeUser,
        onChangeWitness = onChangeWitness,

        areUsersLoading = userIsLoading || responsibleIsLoading || witnessIsLoading,
        user = user,
        responsible = responsible,
        witness = witness,

        isPrescriptionLoading = isPrescriptionDataLoading || isPrescriptionPictureLoading,
        prescriptionData = prescriptionData,
        prescriptionPicture = prescriptionPicture,
        onEditPrescription = onEditPrescription,

        isProductLoading = isLensLoading
                || isColoringLoading
                || isTreatmentLoading
                || isFramesLoading,
        lensEntity = lensEntity,
        coloringEntity = coloringEntity,
        treatmentEntity = treatmentEntity,
        framesEntity = framesEntity,
        onEditProducts = {
            viewModel.onEditProducts()
            onEditProducts()
        },

        isMeasureLoading = isPositioningLeftLoading || isPositioningRightLoading,
        measureLeft = measureLeft,
        measureRight = measureRight,

        canAddNewPayment = canAddNewPayment,
        isTotalPaidLoading = isTotalPaidLoading,
        totalPaid = totalPaid,
        totalToPay = totalToPay,
        isPaymentLoading = isPaymentsLoading,
        payments = payments,
        onAddPayment = {
            viewModel.createPayment {
                onAddPayment(it)
            }
        },
        onDeletePayment = viewModel::deletePayment,
        onEditPayment = { onEditPayment(it.id, it.clientId) },
    )
}

@Composable
private fun ServiceOrderScreenImpl(
    modifier: Modifier = Modifier,

    onFinishSale: () -> Unit = {},

    areUsersLoading: Boolean = false,
    user: ClientEntity = ClientEntity(),
    responsible: ClientEntity = ClientEntity(),
    witness: ClientEntity? = null,
    onChangeResponsible: () -> Unit = {},
    onChangeUser: () -> Unit = {},
    onChangeWitness: () -> Unit = {},

    isPrescriptionLoading: Boolean = false,
    prescriptionData: PrescriptionDataEntity = PrescriptionDataEntity(),
    prescriptionPicture: PrescriptionPictureEntity = PrescriptionPictureEntity(),
    onEditPrescription: () -> Unit = {},

    isProductLoading: Boolean = false,
    lensEntity: LocalLensEntity = LocalLensEntity(),
    coloringEntity: LocalColoringEntity = LocalColoringEntity(),
    treatmentEntity: LocalTreatmentEntity = LocalTreatmentEntity(),
    framesEntity: FramesEntity = FramesEntity(),
    onEditProducts: () -> Unit = {},

    isMeasureLoading: Boolean = false,
    measureLeft: Measuring = Measuring(),
    measureRight: Measuring = Measuring(),

    canAddNewPayment: Boolean = false,
    isTotalPaidLoading: Boolean = false,
    totalPaid: Double = 0.0,
    totalToPay: Double = 0.0,
    isPaymentLoading: Boolean = false,
    payments: List<SalePaymentEntity> = emptyList(),
    onAddPayment: () -> Unit = {},
    onDeletePayment: (payment: SalePaymentEntity) -> Unit = {},
    onEditPayment: (payment: SalePaymentEntity) -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(4.dp)
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

                onChangeResponsible = onChangeResponsible,
                onChangeUser = onChangeUser,
                onChangeWitness = onChangeWitness,

                user = user,
                responsible = responsible,
                witness = witness,
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrescriptionSection(
                isLoading = isPrescriptionLoading,

                onEdit = onEditPrescription,

                prescriptionData = prescriptionData,
                prescriptionPicture = prescriptionPicture,
            )

            Spacer(modifier = Modifier.height(16.dp))

            MeasuresSection(
                isLoading = isMeasureLoading,
                measureLeft = measureLeft,
                measureRight = measureRight,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProductsSection(
                isLoading = isProductLoading,

                onEditProducts = onEditProducts,

                lensEntity = lensEntity,
                coloringEntity = coloringEntity,
                treatmentEntity = treatmentEntity,
                framesEntity = framesEntity,
            )

            Spacer(modifier = Modifier.height(16.dp))

            PaymentSection(
                isLoading = isPaymentLoading,

                canAddNewPayment = canAddNewPayment,
                isTotalPaidLoading = isTotalPaidLoading, 
                totalPaid = totalPaid,
                totalToPay = totalToPay, 

                payments = payments,
                onAddPayment = onAddPayment,
                onDeletePayment = onDeletePayment,
                onEditPayment = onEditPayment,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: use string resource
            PeyessNextStep(
                nextTitle = "Gerar O.S.",
                onNext = onFinishSale,
            )
        }
    }
}

@Composable
private fun ClientSection(
    modifier: Modifier = Modifier,

    isLoading: Boolean = false,

    onChangeResponsible: () -> Unit = {},
    onChangeUser: () -> Unit = {},
    onChangeWitness: () -> Unit = {},

    user: ClientEntity = ClientEntity(),
    responsible: ClientEntity = ClientEntity(),
    witness: ClientEntity? = null,
) {
    Column(
        modifier = modifier
            .padding(sectionPadding),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SectionTitle(
                title = stringResource(id = R.string.so_section_title_clients)
            )

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
                client = user,
                isLoading = isLoading,
                onEditClient = onChangeUser,
            )

            Spacer(modifier = Modifier.height(16.dp))

            SubSectionTitle(
                title = stringResource(id = R.string.so_subsection_title_responsible)
            )

            ClientCard(
                client = responsible,
                isLoading = isLoading,
                onEditClient = onChangeResponsible,
            )

            if (witness != null) {
                Spacer(modifier = Modifier.height(16.dp))

                SubSectionTitle(
                    title = stringResource(id = R.string.so_subsection_title_witness)
                )

                ClientCard(
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
    isLoading: Boolean = false,
    client: ClientEntity,
    onEditClient: () -> Unit = {},
) {
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
                    .data(client.picture)
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

            Spacer(modifier = Modifier.width(endingSpacerWidth))
        }
    }
}

@Composable
private fun PrescriptionSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,

    onEdit: () -> Unit = {},

    prescriptionPicture: PrescriptionPictureEntity = PrescriptionPictureEntity(),
    prescriptionData: PrescriptionDataEntity = PrescriptionDataEntity(),
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

            IconButton(
                onClick = onEdit,
                enabled = !isLoading,
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                AsyncImage(
                    modifier = Modifier
                        .padding(profilePicPadding)
                        .size(prescriptionPictureSize)
                        // Clip image to be shaped as a circle
                        .border(width = 2.dp,
                            color = MaterialTheme.colors.primary,
                            shape = CircleShape)
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(prescriptionPicture.pictureUri)
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

            if (prescriptionPicture.isCopy) {
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
                        Text(
                            modifier = Modifier.width(IntrinsicSize.Max),
                            text = prescriptionPicture.prescriptionDate.toString(),
                            style = MaterialTheme.typography.body1
                                .copy(textAlign = TextAlign.Start),
                        )

                        Text(
                            modifier = Modifier.width(IntrinsicSize.Max),
                            text = prescriptionPicture.professionalName,
                            style = MaterialTheme.typography.body1
                                .copy(textAlign = TextAlign.Start),
                        )

                        Text(
                            modifier = Modifier.width(IntrinsicSize.Max),
                            text = prescriptionPicture.professionalId,
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
                    text = "%.2f".format(prescriptionData.sphericalRight),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = "%.2f".format(prescriptionData.cylindricalRight),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = "%.2f".format(prescriptionData.axisRight),
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
                    text = "%.2f".format(prescriptionData.sphericalLeft),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = "%.2f".format(prescriptionData.cylindricalLeft),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Text(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density,
                    ),
                    text = "%.2f".format(prescriptionData.axisLeft),
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
                    text = if (prescriptionData.hasAddition) {
                        "%.2f".format(prescriptionData.additionRight)
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
                    text = if (prescriptionData.hasPrism) {
                        "%.2f".format(prescriptionData.prismDegreeRight)
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
                    text = if (prescriptionData.hasPrism) {
                        PrismPosition.toName(prescriptionData.prismPositionRight)
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
                        prescriptionData.hasPrism
                        && prescriptionData.prismPositionRight == PrismPosition.Axis
                    ) {
                        "%.2fº".format(prescriptionData.prismAxisRight)
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
                    text = if (prescriptionData.hasAddition) {
                        "%.2f".format(prescriptionData.additionLeft)
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
                    text = if (prescriptionData.hasPrism) {
                        "%.2f".format(prescriptionData.prismDegreeLeft)
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
                    text = if (prescriptionData.hasPrism) {
                        PrismPosition.toName(prescriptionData.prismPositionLeft)
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
                        prescriptionData.hasPrism
                        && prescriptionData.prismPositionLeft == PrismPosition.Axis
                    ) {
                        "%.2fº".format(prescriptionData.prismAxisLeft)
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

        Row(modifier = Modifier.fillMaxWidth()) {
            SectionTitle(title = stringResource(id = R.string.so_section_title_measure))

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
                    Text(text = "%.2f".format(max(measureLeft.diameter, measureRight.diameter)))
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.so_bridge_hoop))
                    Spacer(modifier = Modifier.width(spaceBetweenMeasureValue))
                    Text(
                        text = "%.2f".format(
                            max(measureLeft.horizontalBridgeHoop, measureRight.horizontalBridgeHoop)
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
                            max(measureLeft.verticalHoop, measureRight.verticalHoop)
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
                            max(measureLeft.horizontalHoop, measureRight.horizontalHoop)
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
                            max(measureLeft.bridge, measureRight.bridge)
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
    isLoading: Boolean = false,

    onEditProducts: () -> Unit = {},

    lensEntity: LocalLensEntity = LocalLensEntity(),
    coloringEntity: LocalColoringEntity = LocalColoringEntity(),
    treatmentEntity: LocalTreatmentEntity = LocalTreatmentEntity(),
    framesEntity: FramesEntity = FramesEntity(),
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

        Row(modifier = Modifier.fillMaxWidth()) {
            SectionTitle(title = stringResource(id = R.string.so_section_title_products))

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onEditProducts,
                enabled = !isLoading,
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
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
            lensEntity = lensEntity,
            minPriceModifier = minimumPriceModifier,
            minTitleModifier = minimumTitleModifier,
        )
        Spacer(modifier = Modifier.size(productSpacerSize))
        ColoringCard(
            coloringEntity = coloringEntity,
            minPriceModifier = minimumPriceModifier,
            minTitleModifier = minimumTitleModifier,
        )
        Spacer(modifier = Modifier.size(productSpacerSize))
        TreatmentCard(
            treatmentEntity = treatmentEntity,
            minPriceModifier = minimumPriceModifier,
            minTitleModifier = minimumTitleModifier,
        )

        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        // TODO: use string resource
        SubSectionTitle(title = "Olho direito")
        Spacer(modifier = Modifier.size(productSpacerSize))
        LensCard(
            lensEntity = lensEntity,
            minPriceModifier = minimumPriceModifier,
            minTitleModifier = minimumTitleModifier,
        )
        Spacer(modifier = Modifier.size(productSpacerSize))
        ColoringCard(
            coloringEntity = coloringEntity,
            minPriceModifier = minimumPriceModifier,
            minTitleModifier = minimumTitleModifier,
        )
        Spacer(modifier = Modifier.size(productSpacerSize))
        TreatmentCard(
            treatmentEntity = treatmentEntity,
            minPriceModifier = minimumPriceModifier,
            minTitleModifier = minimumTitleModifier,
        )

        if (framesEntity.areFramesNew) {
            Spacer(modifier = Modifier.size(subsectionSpacerSize))

            // TODO: use string resource
            SubSectionTitle(title = "Armação")
            FramesCard(framesEntity = framesEntity)
        }
    }
}

@Composable
private fun LensCard(
    modifier: Modifier = Modifier,
    minTitleModifier: Modifier = Modifier,
    minPriceModifier: Modifier = Modifier,
    lensEntity: LocalLensEntity = LocalLensEntity(),
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
            text = lensEntity.name(),
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
    coloringEntity: LocalColoringEntity = LocalColoringEntity(),
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
            text = coloringEntity.name(),
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // TODO: change suggestedPrice to price
        Text(
            modifier = minPriceModifier,
            text = NumberFormat.getCurrencyInstance().format(coloringEntity.suggestedPrice / 2f),
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
    treatmentEntity: LocalTreatmentEntity = LocalTreatmentEntity(),
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
            text = treatmentEntity.name(),
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // TODO: change suggestedPrice to price
        Text(
            modifier = minPriceModifier,
            text = NumberFormat.getCurrencyInstance().format(treatmentEntity.suggestedPrice / 2f),
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
    framesEntity: FramesEntity = FramesEntity(),
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
            text = framesEntity.name(),
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = minPriceModifier,
            text = NumberFormat.getCurrencyInstance().format(framesEntity.value),
            style = MaterialTheme.typography.body1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Start),
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PaymentSection(
    modifier: Modifier = Modifier,

    canAddNewPayment: Boolean = false,
    isTotalPaidLoading: Boolean = false,
    totalPaid: Double = 0.0,
    totalToPay: Double = 0.0,
    
    isLoading: Boolean = false,
    payments: List<SalePaymentEntity> = emptyList(),
    onAddPayment: () -> Unit = {},
    onDeletePayment: (payment: SalePaymentEntity) -> Unit = {},
    onEditPayment: (payment: SalePaymentEntity) -> Unit = {},
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

            IconButton(
                onClick = onAddPayment,
                enabled = !isLoading && canAddNewPayment,
            ) {
                Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "")
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        AnimatedVisibility(
            visible = !isTotalPaidLoading,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
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
    payment: SalePaymentEntity = SalePaymentEntity(),
    onEditPayment: (payment: SalePaymentEntity) -> Unit = {},
    onDeletePayment: (payment: SalePaymentEntity) -> Unit = {},
) {
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
                .data(payment.clientPicture)
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

@Preview
@Composable
private fun PaymentCardPreview() {
    SalesAppTheme {
        PaymentCard(
            modifier = Modifier.fillMaxWidth(),
            payment = SalePaymentEntity(
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
            lensEntity =  LocalLensEntity(
                brand = "Família",
                design = "Descrição",
                tech = "Tecnologia",
                material = "Material",
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
            coloringEntity = LocalColoringEntity(
                brand = "Família",
                design = "Descrição",
                price = 1250.0,
                suggestedPrice = 1250.0,
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
            treatmentEntity = LocalTreatmentEntity(
                brand = "Família",
                design = "Descrição",
                price = 1250.0,
                suggestedPrice = 1250.0,
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
            framesEntity = FramesEntity(
                description = "Descrição",
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
        ProductsSection()
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
            user = ClientEntity(name = "João da Silva", shortAddress = "São Paulo, SP"),
            responsible = ClientEntity(name = "João da Silva", shortAddress = "São Paulo, SP"),
        )
    }
}
