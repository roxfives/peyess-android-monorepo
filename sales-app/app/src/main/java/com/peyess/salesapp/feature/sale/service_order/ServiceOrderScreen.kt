package com.peyess.salesapp.feature.sale.service_order

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
//import com.google.accompanist.placeholder.placeholder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.placeholder.material.placeholder
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.client.room.ClientEntity
import com.peyess.salesapp.dao.client.room.ClientRole
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_coloring.name
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_lens.name
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.products.room.local_treatment.name
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames.name
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.feature.sale.service_order.state.ServiceOrderState
import com.peyess.salesapp.feature.sale.service_order.state.ServiceOrderViewModel
import com.peyess.salesapp.ui.theme.SalesAppTheme

private val pictureSize = 60.dp
private val pictureSizePx = 60

private val prescriptionPictureSize = 60.dp
private val prescriptionPictureSizePx = 60

private val cardPadding = 16.dp
private val cardSpacerWidth = 2.dp
private val spacingBetweenCards = 8.dp
private val profilePicPadding = 8.dp

private val endingSpacerWidth = profilePicPadding

private val sectionPadding = 16.dp
private val subsectionSpacerSize = 16.dp

@Composable
fun ServiceOrderScreen(
    modifier: Modifier = Modifier,

    onChangeClient: (client: ClientEntity) -> Unit = {},
    onChangeLens: () -> Unit = {},
    onChangeFrames: () -> Unit = {},
    onConfirmMeasure: () -> Unit = {},

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

    ServiceOrderScreenImpl(
        modifier = modifier,

        onFinishSale = {},

        areUsersLoading = userIsLoading || responsibleIsLoading || witnessIsLoading,
        user = user,
        responsible = responsible,
        witness = witness,

        isPrescriptionLoading = isPrescriptionDataLoading || isPrescriptionPictureLoading,
        prescriptionData = prescriptionData,
        prescriptionPicture = prescriptionPicture,
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

    isPrescriptionLoading: Boolean = false,
    prescriptionData: PrescriptionDataEntity,
    prescriptionPicture: PrescriptionPictureEntity,

    isProductLoading: Boolean = false,
    lensEntity: LocalLensEntity = LocalLensEntity(),
    coloringEntity: LocalColoringEntity = LocalColoringEntity(),
    treatmentEntity: LocalTreatmentEntity = LocalTreatmentEntity(),
    framesEntity: FramesEntity = FramesEntity(),
) {
    val scrollableState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .scrollable(scrollableState, orientation = Orientation.Vertical),
    ) {
        ClientSection(
            isLoading = areUsersLoading,

            user = user,
            responsible = responsible,
            witness = witness,
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrescriptionSection(
            isLoading = isPrescriptionLoading,
            prescriptionData = prescriptionData,
            prescriptionPicture = prescriptionPicture,
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProductsSection(
            isLoading = isProductLoading,

            lensEntity = lensEntity,
            coloringEntity = coloringEntity,
            treatmentEntity = treatmentEntity,
            framesEntity = framesEntity,
        )
    }
}

@Composable
private fun ClientSection(
    modifier: Modifier = Modifier,

    isLoading: Boolean = false,

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

            IconButton(
                onClick = { /*TODO*/ },
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

        Column(modifier = Modifier.padding(subsectionSpacerSize)) {
            SubSectionTitle(
                title = stringResource(id = R.string.so_subsection_title_user)
            )

            ClientCard(client = user, isLoading = isLoading)

            Spacer(modifier = Modifier.height(16.dp))

            SubSectionTitle(
                title = stringResource(id = R.string.so_subsection_title_responsible)
            )

            ClientCard(client = responsible, isLoading = isLoading)

            if (witness != null) {
                Spacer(modifier = Modifier.height(16.dp))

                SubSectionTitle(
                    title = stringResource(id = R.string.so_subsection_title_witness)
                )

                ClientCard(client = user, isLoading = isLoading)
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
    onEditClient: (role: ClientRole) -> Unit = {},
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
                onClick = { onEditClient(client.clientRole) },
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
                onClick = { /*TODO*/ },
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

            Row(modifier = Modifier.weight(2f)) {
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

//        Spacer(modifier = Modifier.size(subsectionSpacerSize))
//        Divider(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
//        )
//        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        // TODO: add Prescription here
    }
}

@Composable
private fun ProductsSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,

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
        Row(modifier = Modifier.fillMaxWidth()) {
            SectionTitle(title = stringResource(id = R.string.so_section_title_products))

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { /*TODO*/ },
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
        LensCard(lensEntity = lensEntity)
        ColoringCard(coloringEntity = coloringEntity)
        TreatmentCard(treatmentEntity = treatmentEntity)

        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        // TODO: use string resource
        SubSectionTitle(title = "Olho direito")
        LensCard(lensEntity = lensEntity)
        ColoringCard(coloringEntity = coloringEntity)
        TreatmentCard(treatmentEntity = treatmentEntity)

        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        // TODO: use string resource
        SubSectionTitle(title = "Armação")
        FramesCard(framesEntity = framesEntity)
    }
}

@Composable
private fun LensCard(
    modifier: Modifier = Modifier,
    lensEntity: LocalLensEntity = LocalLensEntity(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // TODO: use string resource
        Text(text = "Lente", style = MaterialTheme.typography.body1
            .copy(fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = lensEntity.name(),
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = "R$ %.2f".format(lensEntity.price), style = MaterialTheme.typography.body1
            .copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
private fun ColoringCard(
    modifier: Modifier = Modifier,
    coloringEntity: LocalColoringEntity = LocalColoringEntity(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // TODO: use string resource
        Text(text = "Coloração", style = MaterialTheme.typography.body1
            .copy(fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = coloringEntity.name(),
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = "R$ %.2f".format(coloringEntity.price), style = MaterialTheme.typography.body1
            .copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
private fun TreatmentCard(
    modifier: Modifier = Modifier,
    treatmentEntity: LocalTreatmentEntity = LocalTreatmentEntity(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // TODO: use string resource
        Text(text = "Tratamento", style = MaterialTheme.typography.body1
            .copy(fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = treatmentEntity.name(),
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = "R$ %.2f".format(treatmentEntity.price), style = MaterialTheme.typography.body1
            .copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
private fun FramesCard(
    modifier: Modifier = Modifier,
    framesEntity: FramesEntity = FramesEntity(),
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // TODO: use string resource
        Text(text = "Armação", style = MaterialTheme.typography.body1
            .copy(fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = framesEntity.name(),
            style = MaterialTheme.typography.body1
                .copy(textAlign = TextAlign.Start)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = framesEntity.value, style = MaterialTheme.typography.body1
            .copy(fontWeight = FontWeight.Bold))
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
                value = "450.0",
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
