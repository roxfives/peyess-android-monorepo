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
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.peyess.salesapp.screen.sale.frames.landing.dialog.DiameterDifferenceTooBig
import com.peyess.salesapp.screen.sale.frames.landing.dialog.DisplayMeasureDialog
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.screen.sale.frames.landing.state.FramesLandingState
import com.peyess.salesapp.screen.sale.frames.landing.state.FramesLandingViewModel
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight
import com.peyess.salesapp.ui.theme.SalesAppTheme
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
    onAddFrames: (serviceOrderId: String) -> Unit = {},
    onAddMeasure: (eye: Eye) -> Unit = {},
    onAddPantoscopic: (eye: Eye) -> Unit = {},
    onNext: () -> Unit = {},
) {
    val viewModel: FramesLandingViewModel = mavericksViewModel()

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

    val isDiameterDiffAcceptable by viewModel
        .collectAsState(FramesLandingState::isDiameterDiffAcceptable)

    val hasFinishedSettingFramesType
            by viewModel.collectAsState(FramesLandingState::hasFinishedSettingFramesType)
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
        onAddPantocospic = onAddPantoscopic,

        mikeMessage = landingMikeMessage,

        pictureUriLeftEye = pictureUriLeftEye,
        pictureUriRightEye = pictureUriRightEye,

        needsDiameterDiffConfirmation = !isDiameterDiffAcceptable,
        diameterLeft = diameterLeft,
        diameterRight = diameterRight,

        displayMeasureLeft = displayMeasureLeft,
        displayMeasureRight = displayMeasureRight,

        onNext = onNext,
    )
}

@Composable
private fun FramesLandingScreenImpl(
    modifier: Modifier = Modifier,

    idealCurvatureMessage: String = "",
    idealCurvatureAnimationId: Int = R.raw.lottie_frames_curvature_4,

    hasSetFrames: Boolean = false,
    areFramesNew: Boolean = false,
    onAddFrames: (isNew: Boolean) -> Unit = {},
    onAddMeasure: (eye: Eye) -> Unit = {},
    onAddPantocospic: (eye: Eye) -> Unit = {},

    mikeMessage: String = "",

    pictureUriLeftEye: Uri = Uri.EMPTY,
    pictureUriRightEye: Uri = Uri.EMPTY,

    needsDiameterDiffConfirmation: Boolean = false,
    diameterLeft: String = "0.00",
    diameterRight: String = "0.00",

    displayMeasureLeft: DisplayMeasure = DisplayMeasure(),
    displayMeasureRight: DisplayMeasure = DisplayMeasure(),

    onNext: () -> Unit = {},
) {
//    val hasConfirmedDiameterDiff = remember { mutableStateOf(false) }

    val dialogState = rememberMaterialDialogState(true)
    MaterialDialog(
        dialogState = dialogState,
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
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FramesMeasureButton(
            modifier = Modifier
                .height(measureButtonHeight)
                .weight(1f)
                .clickable { onMeasureRight() },

            hasMeasured = hasMeasuredRight,
            diameter = diameterRight,

            title = stringResource(id = R.string.measure_right_eye),
            animationId = R.raw.lottie_frames_landing_right_eye,
        )

        FramesMeasureButton(
            modifier = Modifier
                .height(measureButtonHeight)
                .weight(1f)
                .clickable { onMeasureLeft() },

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

//        Row(
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Icon(
//                imageVector = Icons.Filled.Diameter,
//                contentDescription = "",
//            )
//
//            Text(
//                text = diameter,
//                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
//            )
//
//            Text(
//                text = stringResource(id = R.string.mm),
//                style = MaterialTheme.typography.body1
//            )
//        }
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