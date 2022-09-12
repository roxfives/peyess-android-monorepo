package com.peyess.salesapp.feature.sale.frames

import android.net.Uri
import androidx.annotation.RawRes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.frames.state.FramesState
import com.peyess.salesapp.feature.sale.frames.state.FramesViewModel
import com.peyess.salesapp.ui.component.footer.PeyessNextStep
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

private val measureButtonHeight = 240.dp

@Composable
fun FramesLandingScreen(
    modifier: Modifier = Modifier,
    onAddFrames: () -> Unit = {},
    onAddMeasure: (eye: Eye) -> Unit = {},
    onAddPantoscopic: (eye: Eye) -> Unit = {},
    onNext: () -> Unit = {},
) {
    val viewModel: FramesViewModel = mavericksViewModel()

    val idealCurvatureMessage by viewModel.collectAsState(FramesState::idealBaseMessageAsync)
    val idealCurvatureAnimationId by viewModel.collectAsState(FramesState::idealBaseAnimationResource)

    val landingMikeMessage by viewModel.collectAsState(FramesState::landingMikeMessage)

    val frames by viewModel.collectAsState(FramesState::_currentFramesData)
    val hasSetFrames by viewModel.collectAsState(FramesState::hasSetFrames)

    val pictureUriLeftEye by viewModel.collectAsState(FramesState::pictureUriLeftEye)
    val pictureUriRightEye by viewModel.collectAsState(FramesState::pictureUriRightEye)

    FramesLandingScreenImpl(
        modifier = modifier,

        idealCurvatureMessage = idealCurvatureMessage.invoke() ?: "",
        idealCurvatureAnimationId = idealCurvatureAnimationId.invoke()
            ?: R.raw.lottie_frames_curvature_4,

        hasSetFrames = hasSetFrames,
        frames = frames,
        onAddFrames = {
            viewModel.onFramesNewChanged(it)
            onAddFrames()
        },
        onAddMeasure = onAddMeasure,
        onAddPantocospic = onAddPantoscopic,

        mikeMessage = landingMikeMessage,

        pictureUriLeftEye = pictureUriLeftEye,
        pictureUriRightEye = pictureUriRightEye,

        onNext = onNext,
    )
}

@Composable
private fun FramesLandingScreenImpl(
    modifier: Modifier = Modifier,

    idealCurvatureMessage: String = "",
    idealCurvatureAnimationId: Int = R.raw.lottie_frames_curvature_4,

    hasSetFrames: Boolean = false,
    frames: FramesEntity? = null,
    onAddFrames: (isNew: Boolean) -> Unit = {},
    onAddMeasure: (eye: Eye) -> Unit = {},
    onAddPantocospic: (eye: Eye) -> Unit = {},

    mikeMessage: String = "",

    pictureUriLeftEye: Uri = Uri.EMPTY,
    pictureUriRightEye: Uri = Uri.EMPTY,

    onNext: () -> Unit = {},
) {
    val dialogState = rememberMaterialDialogState(true)
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            // TODO: Use string resource
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

    Column(modifier = modifier) {
        FramesInput(
            hasSetFrames = hasSetFrames,
            currentFrames = frames,
            onSetOwnFrames = {onAddFrames(false)},
            onSetNewFrames = {onAddFrames(true)},
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

            onMeasureLeft = {onAddMeasure(Eye.Left)},
            onMeasureRight = {onAddMeasure(Eye.Right)},
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
            
            // TODO: Use string resource
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
        PeyessNextStep(
            onNext = onNext
        )
    }
}


@Composable
private fun FramesInput(
    modifier: Modifier = Modifier,
    hasSetFrames: Boolean = false,
    currentFrames: FramesEntity? = null,
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
        if (hasSetFrames && currentFrames?.areFramesNew == true) {
            newFramesButtonModifier = newFramesButtonModifier
                .border(
                    border = BorderStroke(width = 4.dp, color = MaterialTheme.colors.primary)
                )
        }

        var newFramesButtonTextStyle = MaterialTheme.typography.body1
        if (hasSetFrames && currentFrames?.areFramesNew == true) {
            newFramesButtonTextStyle = newFramesButtonTextStyle
                .copy(textDecoration = TextDecoration.Underline)
        }

        var ownFramesButtonModifier = Modifier
            .height(SalesAppTheme.dimensions.minimum_touch_target)
            .weight(1f)
        if (hasSetFrames && currentFrames?.areFramesNew == false) {
            ownFramesButtonModifier = ownFramesButtonModifier
                .border(
                    border = BorderStroke(width = 4.dp, color = MaterialTheme.colors.primary)
                )
        }

        var ownFramesButtonTextStyle = MaterialTheme.typography.body1
        if (hasSetFrames && currentFrames?.areFramesNew == false) {
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

            title = stringResource(id = R.string.measure_right_eye),
            animationId = R.raw.lottie_frames_landing_right_eye,
        )

        FramesMeasureButton(
            modifier = Modifier
                .height(measureButtonHeight)
                .weight(1f)
                .clickable { onMeasureLeft() },

            hasMeasured = hasMeasuredLeft,

            title = stringResource(id = R.string.measure_left_eye),
            animationId = R.raw.lottie_frames_landing_left_eye,
        )
    }
}

@Composable
private fun FramesMeasureButton(
    modifier: Modifier = Modifier,
    title: String = "",
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
//            modifier = Modifier
//                .height(animationHeight)
//                .width(animationWidth),
            composition = composition,
            iterations = if (hasMeasured) 1 else LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )
//        Icon(
//            painter = painterResource(id = animationId),
//            contentDescription = title,
//            tint = MaterialTheme.colors.primary,
//        )
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