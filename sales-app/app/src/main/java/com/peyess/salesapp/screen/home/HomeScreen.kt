package com.peyess.salesapp.screen.home

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.EMobiledata
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.peyess.salesapp.R
import com.peyess.salesapp.app.state.MainAppState
import com.peyess.salesapp.app.state.MainViewModel
import com.peyess.salesapp.screen.home.dialog.ExistingClientDialog
import com.peyess.salesapp.screen.sale.anamnesis.fifth_step_sports.state.FifthStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.first_step_first_time.state.FirstTimeViewModel
import com.peyess.salesapp.screen.sale.anamnesis.fourth_step_pain.state.FourthStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.second_step_glass_usage.state.SecondStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.sixth_step_time.state.SixthStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.third_step_sun_light.state.ThirdStepViewModel
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.screen.home.model.UnfinishedSale
import com.peyess.salesapp.ui.component.button.HomeScreenButton
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.customView
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

private val dividerPadding = 16.dp

private val profileDataPadding = 16.dp

private val profilePicturePadding = 8.dp
private val profilePictureSize = 120.dp
private val profilePictureSizePx = 120

private val profileSpacerHeight = 16.dp
private val profilePictureSpacerWidth = 16.dp
private val localizationSpacerWidth = 8.dp

// small screen
// private val sectionSpacerHeight = 16.dp
private val sectionSpacerHeight = 32.dp

private val buttonPanelMinWidth = 0.dp
private val buttonPanelMaxWidth = 800.dp
private val buttonPanelSpacerHeight = 8.dp

// small screen
//private val buttonPanelSpacerWidth = 16.dp
private val buttonPanelSpacerWidth = 32.dp

private val buttonIconSize = 72.dp

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,

    navHostController: NavHostController = rememberNavController(),

    onSettings: () -> Unit = {},
    onSignOut: () -> Unit = {},

    onStartSale: () -> Unit = {},
    onAddClient: (clientId: String) -> Unit = {},
    onStartVisualAcuity: () -> Unit = {},
    onOpenProductsTable: () -> Unit = {},
) {
    val firstStepViewModel: FirstTimeViewModel = mavericksActivityViewModel()
    val secondStepViewModel: SecondStepViewModel = mavericksActivityViewModel()
    val thirdStepViewModel: ThirdStepViewModel = mavericksActivityViewModel()
    val fourthStepViewModel: FourthStepViewModel = mavericksActivityViewModel()
    val fifthStepViewModel: FifthStepViewModel = mavericksActivityViewModel()
    val sixthStepViewModel: SixthStepViewModel = mavericksActivityViewModel()

    val viewModel: MainViewModel = mavericksActivityViewModel()

    val createClient by viewModel.collectAsState(MainAppState::createClient)
    val createClientId by viewModel.collectAsState(MainAppState::createClientId)
    val creatingClientExists by viewModel.collectAsState(MainAppState::creatingClientExists)
    val hasLookedForExistingClient by viewModel.collectAsState(MainAppState::hasLookedForExistingClient)

    val collaborator by viewModel.collectAsState(MainAppState::collaboratorDocument)
    val isLoadingCollaborator by viewModel.collectAsState(MainAppState::isLoadingCollaborator)

    val store by viewModel.collectAsState(MainAppState::store)
    val isLoadingStore by viewModel.collectAsState(MainAppState::isLoadingStore)

    val isCreatingNewSale by viewModel.collectAsState(MainAppState::isCreatingNewSale)
    val hasCreatedSale by viewModel.collectAsState(MainAppState::hasCreatedSale)

    val canStartSale by viewModel.collectAsState(MainAppState::canStartSale)
    val isUpdatingProductsTable by viewModel.collectAsState(MainAppState::isUpdatingProducts)
    val hasProductsTableUpdateFailed by viewModel.collectAsState(MainAppState::hasProductUpdateFailed)

    val totalClients by viewModel.collectAsState(MainAppState::totalClients)

    val unfinishedSales by viewModel.collectAsState(MainAppState::unfinishedSales)
    val isSearchingForUnfinishedSales
        by viewModel.collectAsState(MainAppState::isSearchingForActiveSales)

    val dismissActiveSales = remember { mutableStateOf(false) }
    val hasShownActiveSales = remember { mutableStateOf(false) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationsPermissionState =
            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

        if (notificationsPermissionState.status != PermissionStatus.Granted) {
            LaunchedEffect(Unit) {
                notificationsPermissionState.launchPermissionRequest()
            }
        }
    }

    val createClientDialogState = rememberMaterialDialogState()
    ExistingClientDialog(
        dialogState = createClientDialogState,
        onCreateNewClient = viewModel::createNewClient,
        onUseExistingClient = viewModel::createClientFromCache,
    )

    if (hasLookedForExistingClient && creatingClientExists) {
        LaunchedEffect(Unit) {
            viewModel.checkedForExistingClient()
            createClientDialogState.show()
        }
    }

    if (hasLookedForExistingClient && !creatingClientExists) {
        LaunchedEffect(Unit) {
            viewModel.createNewClient()
        }
    }

    if (createClient) {
        LaunchedEffect(Unit) {
            viewModel.startedCreatingClient()
            Timber.i("Creating client with id: $createClientId")
            onAddClient(createClientId)
        }
    }

    if (hasCreatedSale) {
        LaunchedEffect(Unit) {
            firstStepViewModel.resetState()
            secondStepViewModel.resetState()
            thirdStepViewModel.resetState()
            fourthStepViewModel.resetState()
            fifthStepViewModel.resetState()
            sixthStepViewModel.resetState()

            viewModel.newSaleStarted()
            onStartSale()
        }
    }

    val dialogState = rememberMaterialDialogState(initialValue = unfinishedSales.isNotEmpty())
    LaunchedEffect(dismissActiveSales.value, unfinishedSales) {
        if (dismissActiveSales.value || unfinishedSales.isEmpty()) {
            dialogState.hide()
        } else if (!hasShownActiveSales.value && !hasCreatedSale) {
            hasShownActiveSales.value = true
            dialogState.show()
        }
    }

    ActiveSalesDialog(
        dialogState = dialogState,
        sales = unfinishedSales,
        onDismiss = { dismissActiveSales.value = true },
        onCancelSale = viewModel::cancelSale,
        onResumeSale = {
            viewModel.resumeSale(it)
            onStartSale()
        },
    )

    HomeScreenImpl(
        modifier = modifier,

        pictureForUser = viewModel::pictureForUser,
        collaboratorDocument = collaborator ?: CollaboratorDocument(),
        isLoadingCollaborator = isLoadingCollaborator,

        store = store,
        isLoadingStore = isLoadingStore,

        canStartSale = canStartSale,
        isUpdatingProductsTable = isUpdatingProductsTable,
        hasProductsTableUpdateFailed = hasProductsTableUpdateFailed,

        totalClients = totalClients,

        onSettings = onSettings,

        onSignOut = {
            viewModel.exit()
            onSignOut()
        },

        onStartSale = { viewModel.startNewSale() },

        onAddClient = viewModel::findActiveCreatingClient,

        onStartVisualAcuity = onStartVisualAcuity,

        canShowProductsTable = canStartSale,
        onOpenProductsTable = onOpenProductsTable,
    )
}

@Composable
private fun HomeScreenImpl(
    modifier: Modifier = Modifier,

    pictureForUser: suspend (uid: String) -> Uri = { Uri.EMPTY },
    collaboratorDocument: CollaboratorDocument = CollaboratorDocument(),
    isLoadingCollaborator: Boolean = false,

    store: OpticalStore = OpticalStore(),
    isLoadingStore: Boolean = false,

    isUpdatingProductsTable: Boolean = false,
    hasProductsTableUpdateFailed: Boolean = false,

    totalClients: Int = 0,

    onSettings: () -> Unit = {},
    onSignOut: () -> Unit = {},

    canStartSale: Boolean = false,
    onStartSale: () -> Unit = {},

    onAddClient: () -> Unit = {},

    onStartVisualAcuity: () -> Unit = {},

    canShowProductsTable: Boolean = false,
    onOpenProductsTable: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        ProfileData(
            isLoading = isLoadingCollaborator || isLoadingStore,
            collaboratorDocument = collaboratorDocument,
            localization = store.shortAddress,
            pictureForUser = pictureForUser,

            onSignOut = onSignOut,
        )

        Divider(
            modifier = Modifier.padding(horizontal = dividerPadding),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Spacer(modifier = Modifier.height(sectionSpacerHeight))

        ButtonsPanel(
            totalClients = totalClients,
            isUpdatingProductsTable = isUpdatingProductsTable,
            hasProductsTableUpdateFailed = hasProductsTableUpdateFailed,

            canStartSale = canStartSale,
            onStartSale = onStartSale,

            onAddClient = onAddClient,

            onStartVisualAcuity = onStartVisualAcuity,

            canShowProductsTable = canShowProductsTable,
            onOpenProductsTable = onOpenProductsTable,
        )

        Spacer(modifier = Modifier.height(sectionSpacerHeight))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = MaterialTheme.colors.primary.copy(0.2f),
                ),
                shape = MaterialTheme.shapes.small,
                onClick = onSettings,
            ) {
                Row {
                    Icon(imageVector = Icons.Filled.Settings, contentDescription = "")

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = stringResource(id = R.string.home_btn_settings_actions))
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.home_notifications),
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            )

            Divider(
                modifier = Modifier.padding(horizontal = dividerPadding),
                color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
            )
        }

        NotificationsPanel()
    }
}

@Composable
private fun ProfileData(
    modifier: Modifier = Modifier,

    isLoading: Boolean = false,
    pictureForUser: suspend (uid: String) -> Uri = { Uri.EMPTY },
    collaboratorDocument: CollaboratorDocument = CollaboratorDocument(),
    localization: String = "",

    onSignOut: () -> Unit = {},
) {
    if (isLoading) {
        CircularProgressIndicator()
    } else {
        val coroutineScope = rememberCoroutineScope()
        val pictureUri = remember { mutableStateOf(Uri.EMPTY) }
        LaunchedEffect(collaboratorDocument) {
            coroutineScope.launch(Dispatchers.IO) {
                val picture = pictureForUser(collaboratorDocument.id)

                pictureUri.value = picture
            }
        }

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier = modifier
                    .padding(profileDataPadding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .padding(profilePicturePadding)
                        .size(profilePictureSize)
                        // Clip image to be shaped as a circle
                        .border(
                            width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape
                        )
                        .clip(CircleShape),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(pictureUri.value)
                        .crossfade(true)
                        .size(width = profilePictureSizePx, height = profilePictureSizePx).build(),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "",
                    error = painterResource(id = R.drawable.ic_profile_placeholder),
                    fallback = painterResource(id = R.drawable.ic_profile_placeholder),
                    placeholder = painterResource(id = R.drawable.ic_profile_placeholder),
                )

                Spacer(modifier = Modifier.height(profilePictureSpacerWidth))

                Column(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.home_welcome).format(collaboratorDocument.nameDisplay),
                        style = MaterialTheme.typography.subtitle1,
                    )

                    Spacer(modifier = Modifier.height(profileSpacerHeight))

                    Row {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            tint = MaterialTheme.colors.primary,
                            contentDescription = "",
                        )

                        Spacer(modifier = Modifier.width(localizationSpacerWidth))

                        Text(
                            text = localization,
                            style = MaterialTheme.typography.subtitle1,
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    onClick = onSignOut,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = "",
                        )

                        Text(text = stringResource(id = R.string.home_btn_exit))
                    }
                }
            }
        }
    }
}

@Composable
private fun ButtonsPanel(
    modifier: Modifier = Modifier,

    totalClients: Int = 0,
    isUpdatingProductsTable: Boolean = false,
    hasProductsTableUpdateFailed: Boolean = false,

    canStartSale: Boolean = false,
    onStartSale: () -> Unit = {},

    onAddClient: () -> Unit = {},

    onStartVisualAcuity: () -> Unit = {},

    canShowProductsTable: Boolean = false,
    onOpenProductsTable: () -> Unit = {},
) {
    Column(
        modifier = modifier.widthIn(buttonPanelMinWidth, buttonPanelMaxWidth),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        val density = LocalDensity.current
        val minimumWidthState = remember { MinimumWidthState() }

        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeScreenButton(
                modifier = Modifier.minimumWidthModifier(
                    state = minimumWidthState,
                    density = density,
                ),
                title = if (hasProductsTableUpdateFailed) {
                    stringResource(id = R.string.home_btn_sale_title_failed)
                } else if (isUpdatingProductsTable) {
                    stringResource(id = R.string.home_btn_sale_title_updating)
                } else {
                    stringResource(id = R.string.home_btn_sale_title)
                },

                subtitle = if (hasProductsTableUpdateFailed) {
                    stringResource(id = R.string.home_btn_sale_subtitle_failed)
                } else if (isUpdatingProductsTable) {
                    stringResource(id = R.string.home_btn_sale_subtitle_updating)
                } else {
                    stringResource(id = R.string.home_btn_sale_subtitle)
                },

                icon = {
                    if (hasProductsTableUpdateFailed) {
                        Icon(
                            modifier = Modifier.size(buttonIconSize),
                            imageVector = Icons.Filled.Error,
                            contentDescription = "",
                        )
                    } else if (isUpdatingProductsTable) {
                        CircularProgressIndicator()
                    } else {
                        Icon(
                            modifier = Modifier.size(buttonIconSize),
                            imageVector = Icons.Filled.Sell,
                            contentDescription = "",
                        )
                    }
                },

                enabled = canStartSale,
                onClick = onStartSale,
            )

            Spacer(modifier = Modifier.width(buttonPanelSpacerWidth))

            HomeScreenButton(
                modifier = Modifier.minimumWidthModifier(
                    state = minimumWidthState,
                    density = density,
                ),
                title = stringResource(id = R.string.home_btn_clients_title),
                subtitle = stringResource(id = R.string.home_btn_clients_subtitle)
                    .format(totalClients),

                icon = {
                    Icon(
                        modifier = Modifier.size(buttonIconSize),
                        imageVector = Icons.Filled.PersonAdd,
                        contentDescription = "",
                    )
                },

                onClick = onAddClient,
            )
        }

        Spacer(modifier = Modifier.height(buttonPanelSpacerHeight))

        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeScreenButton(
                modifier = Modifier.minimumWidthModifier(
                    state = minimumWidthState,
                    density = density,
                ),
                title = stringResource(id = R.string.home_btn_visual_acuity_title),
                subtitle = stringResource(id = R.string.home_btn_visual_acuity_subtitle),

                icon = {
                    Icon(
                        modifier = Modifier.size(buttonIconSize),
                        imageVector = Icons.Filled.EMobiledata,
                        contentDescription = "",
                    )
                },

                onClick = onStartVisualAcuity,
            )

            Spacer(modifier = Modifier.width(buttonPanelSpacerWidth))

            HomeScreenButton(
                modifier = Modifier.minimumWidthModifier(
                    state = minimumWidthState,
                    density = density,
                ),
                title = if (hasProductsTableUpdateFailed) {
                    stringResource(id = R.string.home_btn_products_title_failed)
                } else if (isUpdatingProductsTable) {
                    stringResource(id = R.string.home_btn_products_title_updating)
                } else {
                    stringResource(id = R.string.home_btn_products_title)
                },

                subtitle = if (hasProductsTableUpdateFailed) {
                    stringResource(id = R.string.home_btn_products_subtitle_failed)
                } else if (isUpdatingProductsTable) {
                    stringResource(id = R.string.home_btn_products_subtitle_updating)
                } else {
                    stringResource(id = R.string.home_btn_products_subtitle)
                },

                icon = {
                    if (hasProductsTableUpdateFailed) {
                        Icon(
                            modifier = Modifier.size(buttonIconSize),
                            imageVector = Icons.Filled.Error,
                            contentDescription = "",
                        )
                    } else if (isUpdatingProductsTable) {
                        CircularProgressIndicator()
                    } else {
                        Icon(
                            modifier = Modifier.size(buttonIconSize),
                            imageVector = Icons.Filled.Inventory,
                            contentDescription = "",
                        )
                    }
                },

                enabled = canShowProductsTable,
                onClick = onOpenProductsTable,
            )
        }
    }
}

@Composable
private fun NotificationsPanel(
    modifier: Modifier = Modifier,
    total: Int = 0,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_simple_empty)
    )

    LottieAnimation(
        modifier = modifier
            .height(240.dp)
            .width(240.dp),
        composition = composition,
        iterations = LottieConstants.IterateForever,
        clipSpec = LottieClipSpec.Progress(0f, 1f),
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        // TODO: use string resource
        text = stringResource(id = R.string.home_no_notifications),
        style = MaterialTheme.typography.h6
            .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    )
}

@Composable
private fun ActiveSalesDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    sales: List<UnfinishedSale> = emptyList(),
    onCancelSale: (UnfinishedSale) -> Unit = {},
    onResumeSale: (UnfinishedSale) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(
                text = stringResource(id = R.string.home_dialog_btn_dismiss),
                onClick = { onDismiss() },
            )
        },
        autoDismiss = false,
    ) {
        title(stringResource(id = R.string.home_dialog_title_sale_found))

        customView {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                sales.forEach {
                    ActiveSale(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        sale = it,
                        onResumeSale = onResumeSale,
                        onCancelSale = onCancelSale,
                    )
                }
            }
        }
    }
}

@Composable
private fun ActiveSale(
    modifier: Modifier = Modifier,
    sale: UnfinishedSale = UnfinishedSale(),
    onCancelSale: (UnfinishedSale) -> Unit = {},
    onResumeSale: (UnfinishedSale) -> Unit = {},
) {
    Row(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.home_dialog_sale_found)
                .format(sale.clientName),
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { onResumeSale(sale) }) {
            Icon(
                imageVector = Icons.Filled.RestartAlt,
                contentDescription = "",
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { onCancelSale(sale) }) {
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = "",
            )
        }
    }
}

@Preview
@Composable
private fun ProfileDataPreview() {
    SalesAppTheme {
        ProfileData(
            modifier = Modifier.fillMaxWidth(),
            collaboratorDocument = CollaboratorDocument(nameDisplay = "vendedor"),
            localization = "São Carlos, SP",
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    SalesAppTheme {
        HomeScreenImpl()
    }
}