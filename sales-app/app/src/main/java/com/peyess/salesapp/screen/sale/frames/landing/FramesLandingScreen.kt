package com.peyess.salesapp.screen.sale.frames.landing

import android.Manifest
import android.net.Uri
import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.peyess.salesapp.R
import com.peyess.salesapp.screen.sale.frames.data.model.DisplayMeasure
import com.peyess.salesapp.screen.sale.frames.landing.dialog.CreateOrUpdateMeasureDialog
import com.peyess.salesapp.screen.sale.frames.landing.dialog.DiameterDifferenceTooBig
import com.peyess.salesapp.screen.sale.frames.landing.dialog.DisplayMeasureDialog
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.screen.sale.frames.landing.state.FramesLandingState
import com.peyess.salesapp.screen.sale.frames.landing.state.FramesLandingViewModel
import com.peyess.salesapp.screen.sale.frames.landing.util.ParseParameters
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight
import com.peyess.salesapp.ui.component.modifier.MinimumHeightState
import com.peyess.salesapp.ui.component.modifier.minimumHeightModifier
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.peyess.salesapp.utils.screen.isHighResolution
import com.peyess.salesapp.utils.screen.isScreenSizeLarge
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

private val measureButtonHeight = 240.dp

private val viewMeasuringButtonSize = 48.dp
private val viewMeasuringButtonPadding = 12.dp
private val viewMeasuringButtonSpacer = 2.dp

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FramesLandingScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onAddFrames: (serviceOrderId: String) -> Unit = {},
    onAddMeasure: (eye: Eye) -> Unit = {},
    onEditMeasure: (eye: Eye) -> Unit = {},
    onAddPantoscopic: (eye: Eye) -> Unit = {},
    onNext: (isEditing: Boolean) -> Unit = {},
) {
    val viewModel: FramesLandingViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateIsEditing = viewModel::onIsEditingParamChanged,
    )

    val isEditing by viewModel.collectAsState(FramesLandingState::isEditing)
    val serviceOrderId by viewModel.collectAsState(FramesLandingState::serviceOrderId)

    val idealCurvatureMessage by viewModel.collectAsState(FramesLandingState::idealBaseMessage)
    val idealCurvatureAnimationId by viewModel.collectAsState(FramesLandingState::idealBaseAnimationResource)

    val landingMikeMessage by viewModel.collectAsState(FramesLandingState::landingMikeMessage)

    val areFramesNew by viewModel.collectAsState(FramesLandingState::areFramesNew)
    val hasSetFrames by viewModel.collectAsState(FramesLandingState::hasSetFrames)

    val pictureUriLeftEye by viewModel.collectAsState(FramesLandingState::pictureUriLeftEye)
    val pictureUriRightEye by viewModel.collectAsState(FramesLandingState::pictureUriRightEye)

    val diameterLeft by viewModel.collectAsState(FramesLandingState::diameterLeft)
    val diameterRight by viewModel.collectAsState(FramesLandingState::diameterRight)

    val displayMeasureLeft by viewModel.collectAsState(FramesLandingState::displayMeasureLeft)
    val displayMeasureRight by viewModel.collectAsState(FramesLandingState::displayMeasureRight)

    val observation by viewModel.collectAsState(FramesLandingState::prescriptionObservationInput)

    val isDiameterDiffAcceptable by viewModel
        .collectAsState(FramesLandingState::isDiameterDiffAcceptable)

    val hasFinishedSettingFramesType by viewModel
        .collectAsState(FramesLandingState::hasFinishedSettingFramesType)
    if (hasFinishedSettingFramesType) {
        LaunchedEffect(Unit) {
            viewModel.onNavigateToSetFrames()
            onAddFrames(serviceOrderId)
        }
    }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    FramesLandingScreenImpl(
        modifier = modifier,

        idealCurvatureMessage = idealCurvatureMessage,
        idealCurvatureAnimationId = idealCurvatureAnimationId,

        hasSetFrames = hasSetFrames,
        areFramesNew = areFramesNew,
        onAddFrames = viewModel::onFramesNewChanged,
        onAddMeasure = {
            if (cameraPermissionState.status != PermissionStatus.Granted) {
                cameraPermissionState.launchPermissionRequest()
            } else {
                onAddMeasure(it)
            }
        },
        onEditMeasure = { onEditMeasure(it) },
        onAddPantocospic = onAddPantoscopic,

        mikeMessage = landingMikeMessage,

        pictureUriLeftEye = pictureUriLeftEye,
        pictureUriRightEye = pictureUriRightEye,

        needsDiameterDiffConfirmation = !isDiameterDiffAcceptable,
        diameterLeft = diameterLeft,
        diameterRight = diameterRight,

        displayMeasureLeft = displayMeasureLeft,
        displayMeasureRight = displayMeasureRight,

        observation = observation,
        onObservationUpdate = viewModel::onObservationUpdate,

        onNext = { onNext(isEditing) },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun FramesLandingScreenImpl(
    modifier: Modifier = Modifier,

    idealCurvatureMessage: String = "",
    idealCurvatureAnimationId: Int = R.raw.lottie_frames_curvature_4,

    hasSetFrames: Boolean = false,
    areFramesNew: Boolean = false,
    onAddFrames: (isNew: Boolean) -> Unit = {},
    onAddMeasure: (eye: Eye) -> Unit = {},
    onEditMeasure: (eye: Eye) -> Unit = {},
    onAddPantocospic: (eye: Eye) -> Unit = {},

    mikeMessage: String = "",

    pictureUriLeftEye: Uri = Uri.EMPTY,
    pictureUriRightEye: Uri = Uri.EMPTY,

    needsDiameterDiffConfirmation: Boolean = false,
    diameterLeft: String = "0.00",
    diameterRight: String = "0.00",

    displayMeasureLeft: DisplayMeasure = DisplayMeasure(),
    displayMeasureRight: DisplayMeasure = DisplayMeasure(),

    observation: String = stringResource(id = R.string.empty_string),
    onObservationUpdate: (observation: String) -> Unit = {},

    onNext: () -> Unit = {},
) {
    val dialogState = rememberMaterialDialogState(true)
    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(
            usePlatformDefaultWidth = !isHighResolution() || isScreenSizeLarge(),
        ),
        buttons = {
            //TODO: Use string resource
            positiveButton("Vamos lá!")
        }
    ) {
        MikeBubbleRight(
            modifier = Modifier
                .height(360.dp)
                .padding(16.dp),
            text = mikeMessage,
        )
    }

    val diameterDiffDialogState = rememberMaterialDialogState(false)
    DiameterDifferenceTooBig(
        dialogState = diameterDiffDialogState,
        onConfirm = onNext,
    )

    val viewMeasureDialogState = rememberMaterialDialogState(false)
    DisplayMeasureDialog(
        dialogState = viewMeasureDialogState,

        showMeasureLeft = pictureUriLeftEye != Uri.EMPTY,
        showMeasureRight = pictureUriRightEye != Uri.EMPTY,

        measureLeft = displayMeasureLeft,
        measureRight = displayMeasureRight,
    )


    Column(modifier = modifier) {
        FramesInput(
            hasSetFrames = hasSetFrames,
            areFramesNew = areFramesNew,
            onSetOwnFrames = { onAddFrames(false) },
            onSetNewFrames = { onAddFrames(true) },
        )

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )
        Spacer(modifier = Modifier.height(32.dp))

        FramesRecommendation(
            message = idealCurvatureMessage,
            animationId = idealCurvatureAnimationId,
        )

        Spacer(modifier = Modifier.height(32.dp))
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )
        Spacer(modifier = Modifier.height(32.dp))

        FramesMeasure(
            hasMeasuredLeft = pictureUriLeftEye != Uri.EMPTY,
            hasMeasuredRight = pictureUriRightEye != Uri.EMPTY,

            diameterLeft = diameterLeft,
            diameterRight = diameterRight,

            onMeasureLeft = { onAddMeasure(Eye.Left) },
            onMeasureRight = { onAddMeasure(Eye.Right) },

            onEditMeasureLeft = { onEditMeasure(Eye.Left) },
            onEditMeasureRight = { onEditMeasure(Eye.Right) },
        )

        val materialDialogState = rememberMaterialDialogState()
        MaterialDialog(
            dialogState = materialDialogState,
            buttons = {
                positiveButton("OK")
            }
        ) {
            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(R.raw.lottie_coming_soon)
            )

            LottieAnimation(
                modifier = Modifier
                    .fillMaxSize(),
                composition = composition,
                iterations = LottieConstants.IterateForever,
                clipSpec = LottieClipSpec.Progress(0f, 1f),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { materialDialogState.show() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(R.raw.lottie_frames_landing_pantoscopic)
            )
            
            //TODO: Use string resource
            Text(text = "Ângulo Pantoscópico e Dist. Vértice")

            Spacer(modifier = Modifier.height(8.dp))

            LottieAnimation(
                modifier = Modifier
                    .height(240.dp)
                    .width(240.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever,
                clipSpec = LottieClipSpec.Progress(0f, 1f),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.title_observation),
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        PrescriptionObservation(
            observation = observation,
            onObservationUpdate = onObservationUpdate,
        )

        Spacer(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.weight(1f))
        PeyessStepperFooter(
            middle = {
                 Column(
                     horizontalAlignment = Alignment.CenterHorizontally,
                     verticalArrangement = Arrangement.Center,
                 ) {
                     IconButton(
                         modifier = Modifier
                             .size(viewMeasuringButtonSize)
                             .padding(viewMeasuringButtonPadding)
                             .background(
                                 color = MaterialTheme.colors.primary,
                                 shape = CircleShape,
                             ),
                         onClick = { viewMeasureDialogState.show() },
                     ) {
                         Icon(
                             imageVector = Icons.Default.Straighten,
                             contentDescription = "",
                             tint = MaterialTheme.colors.onPrimary,
                         )
                     }

                     Spacer(modifier = Modifier.height(viewMeasuringButtonSpacer))
                     
                     Text(
                        text = stringResource(id = R.string.view_measure_button).lowercase(),
                        style = MaterialTheme.typography.caption
                            .copy(fontWeight = FontWeight.Bold),
                     )
                 }
            },
            onNext = {
                if (!needsDiameterDiffConfirmation) {
                    onNext()
                } else {
                    diameterDiffDialogState.show()
                }
            },
        )
    }
}

@Composable
private fun FramesInput(
    modifier: Modifier = Modifier,
    hasSetFrames: Boolean = false,
    areFramesNew: Boolean = false,
    onSetOwnFrames: () -> Unit = {},
    onSetNewFrames: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        var newFramesButtonModifier = Modifier
            .height(SalesAppTheme.dimensions.minimum_touch_target)
            .weight(1f)
        if (hasSetFrames && areFramesNew) {
            newFramesButtonModifier = newFramesButtonModifier
                .border(
                    border = BorderStroke(width = 4.dp, color = MaterialTheme.colors.primary)
                )
        }

        var newFramesButtonTextStyle = MaterialTheme.typography.body1
        if (hasSetFrames && areFramesNew) {
            newFramesButtonTextStyle = newFramesButtonTextStyle
                .copy(textDecoration = TextDecoration.Underline)
        }

        var ownFramesButtonModifier = Modifier
            .height(SalesAppTheme.dimensions.minimum_touch_target)
            .weight(1f)
        if (hasSetFrames && !areFramesNew) {
            ownFramesButtonModifier = ownFramesButtonModifier
                .border(
                    border = BorderStroke(width = 4.dp, color = MaterialTheme.colors.primary)
                )
        }

        var ownFramesButtonTextStyle = MaterialTheme.typography.body1
        if (hasSetFrames && !areFramesNew) {
            ownFramesButtonTextStyle = ownFramesButtonTextStyle
                .copy(textDecoration = TextDecoration.Underline)
        }

        OutlinedButton(
            modifier = ownFramesButtonModifier,
            onClick = { onSetOwnFrames() },
        ) {
            Text(
                text = stringResource(id = R.string.own_frames),
                style = ownFramesButtonTextStyle,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedButton(
            modifier = newFramesButtonModifier,
            onClick = { onSetNewFrames() },
        ) {
            Text(
                text = stringResource(id = R.string.new_frames),
                style = newFramesButtonTextStyle,
            )
        }
    }
}

@Composable
private fun FramesRecommendation(
    modifier: Modifier = Modifier,
    message: String = "",
    animationId: Int = R.raw.lottie_frames_curvature_4,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(animationId)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stringResource(id = R.string.frames_title_optical_curvature),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )

        LottieAnimation(
            modifier = modifier
                .height(169.dp)
                .width(338.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Text(
            text = message,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
private fun FramesMeasure(
    modifier: Modifier = Modifier,

    hasMeasuredLeft: Boolean = false,
    hasMeasuredRight: Boolean = false,

    diameterLeft: String = "0.00mm",
    diameterRight: String = "0.00mm",

    onMeasureLeft: () -> Unit = {},
    onMeasureRight: () -> Unit = {},

    onEditMeasureLeft: () -> Unit = {},
    onEditMeasureRight: () -> Unit = {},
) {
    val dialogState = rememberMaterialDialogState()
    val activeDialogEye = remember { mutableStateOf<Eye>(Eye.None) }
    CreateOrUpdateMeasureDialog(
        dialogState = dialogState,

        onConfirmOverwrite = {
            when (activeDialogEye.value) {
                is Eye.Left -> { onMeasureLeft() }
                is Eye.Right -> { onMeasureRight() }
                is Eye.None -> Unit
            }
        },

        onConfirmUpdate = {
            when (activeDialogEye.value) {
                is Eye.Left -> { onEditMeasureLeft() }
                is Eye.Right -> { onEditMeasureRight() }
                is Eye.None -> Unit
            }
        },
    )


    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FramesMeasureButton(
            modifier = Modifier
                .height(measureButtonHeight)
                .weight(1f)
                .clickable {
                    if (hasMeasuredRight) {
                        activeDialogEye.value = Eye.Right
                        dialogState.show()
                    } else {
                        onMeasureRight()
                    }
                },

            hasMeasured = hasMeasuredRight,
            diameter = diameterRight,

            title = stringResource(id = R.string.measure_right_eye),
            animationId = R.raw.lottie_frames_landing_right_eye,
        )

        FramesMeasureButton(
            modifier = Modifier
                .height(measureButtonHeight)
                .weight(1f)
                .clickable {
                    if (hasMeasuredLeft) {
                        activeDialogEye.value = Eye.Left
                        dialogState.show()
                    } else {
                        onMeasureLeft()
                    }
                },

            hasMeasured = hasMeasuredLeft,
            diameter = diameterLeft,

            title = stringResource(id = R.string.measure_left_eye),
            animationId = R.raw.lottie_frames_landing_left_eye,
        )
    }
}

@Composable
private fun FramesMeasureButton(
    modifier: Modifier = Modifier,
    title: String = "",
    diameter: String = "0.00",
    hasMeasured: Boolean = false,
    @RawRes animationId: Int = 0,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(animationId)
        )

        Text(text = title)

        Spacer(modifier = Modifier.height(8.dp))

        LottieAnimation(
            modifier = Modifier
                .height(169.dp)
                .width(338.dp),
            composition = composition,
            iterations = if (hasMeasured) 1 else LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = hasMeasured,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Text(
                text = "Ø = ${diameter}mm",
                style = MaterialTheme.typography.body1,
            )
        }
    }
}


@Composable
private fun PrescriptionObservation(
    modifier: Modifier = Modifier,
    observation: String = "",
    onObservationUpdate: (observation: String) -> Unit = {},
) {
    val minimumHeightState = remember { MinimumHeightState(24.dp) }
    val density = LocalDensity.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            TextField(
                modifier = modifier
                    .minimumHeightModifier(minimumHeightState, density)
                    .weight(0.8f)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(horizontal = 24.dp),
                value = observation,
                onValueChange = onObservationUpdate,

                textStyle = MaterialTheme.typography.body1,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),

                maxLines = 5,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.observation_placeholder),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.None,
                ),
            )

            AnimatedVisibility(
                visible = observation.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                IconButton(
                    modifier = Modifier.weight(0.2f),
                    onClick = { onObservationUpdate("") },
                ) {
                    Icon(
                        modifier = Modifier.border(
                            width = 2.dp,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                            shape = CircleShape,
                        ),
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FramesMeasureButtonPreview() {
    SalesAppTheme {
        FramesMeasureButton()
    }
}

@Preview
@Composable
private fun FramesInputPreview(
    modifier: Modifier = Modifier,
) {
    SalesAppTheme {
        FramesInput()
    }
}


@Preview
@Composable
private fun PrescriptionObservationFilledPreview() {
    SalesAppTheme {
        PrescriptionObservation(
            modifier = Modifier.fillMaxSize(),
            observation = """
                lorem ipsum dolor sit amet, consectetur adipiscing elit.
                sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
                Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
            """.trimIndent()
        )
    }
}