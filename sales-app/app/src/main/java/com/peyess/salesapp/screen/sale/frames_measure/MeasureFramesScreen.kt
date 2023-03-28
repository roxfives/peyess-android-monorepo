package com.peyess.salesapp.screen.sale.frames_measure

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.screen.sale.frames_measure.animation.animation_positioning.AnimationPanel
import com.peyess.salesapp.screen.sale.frames_measure.animation.measuring_parameter.MeasuringParameter
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.Parameter
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.PositioningAnimationState
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isDoneVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isExpandVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isMoveDownVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isMoveLeftVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isMoveRightVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isMoveUpVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isNextVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isPlayVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isPreviousVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isRotateLeftVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isRotateRightVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.isShrinkVisible
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.lottieCompositionFor
import com.peyess.salesapp.screen.sale.frames_measure.state.FramesMeasureState
import com.peyess.salesapp.screen.sale.frames_measure.state.FramesMeasureViewModel
import com.peyess.salesapp.screen.sale.frames_measure.state.HelperZoomState
import com.peyess.salesapp.ui.holdable
import com.peyess.salesapp.utils.image.decodeAndRotateBitmapFrom
import timber.log.Timber

@Composable
fun MeasureFramesScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onCancel: () -> Unit = {},
    onDone: () -> Unit = {},
) {
    val viewModel: FramesMeasureViewModel = mavericksViewModel()

    val eye = remember { mutableStateOf<Eye>(Eye.None) }
    val eyeParameter = navHostController.currentBackStackEntry
        ?.arguments
        ?.getString("eye")

    val isEditingParameter = navHostController.currentBackStackEntry
        ?.arguments
        ?.getBoolean("isEditing")
        ?: false

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.updateStateStandardSize(context)
    }

    LaunchedEffect(eyeParameter) {
        Timber.i("Using eye $eyeParameter")
        eye.value = if (eyeParameter == "left") Eye.Left else Eye.Right
        viewModel.updateEye(eye.value)
    }

    LaunchedEffect(isEditingParameter) {
        viewModel.onIsEditing(isEditingParameter)
    }

    val measuringParameter by viewModel.collectAsState(FramesMeasureState::measuringParameters)
    val animationState by viewModel.collectAsState(FramesMeasureState::positioningAnimationState)

    val imagePicture by viewModel.collectAsState(FramesMeasureState::picture)
    val zoomHelperState by viewModel.collectAsState(FramesMeasureState::zoomHelperState)
    val isInvalidCheckMiddle by viewModel.collectAsState(FramesMeasureState::isCheckMiddleInvalid)

    val isMeasuringDone by viewModel.collectAsState(FramesMeasureState::isMeasuringDone)

    Timber.i("Uri available is $imagePicture")
    if (isMeasuringDone) {
        MeasuringDone(
            onTimeout = onDone
        )
    } else {
        MeasureFramesScreenImpl(
            modifier = modifier,

            eye = eye.value,
            imageUri = imagePicture,

            measuringParameters = measuringParameter,
            animationState = animationState,

            onBackFromCheckMiddle = viewModel::onBackFromCheckMiddle,
            isCheckMiddleInvalid = isInvalidCheckMiddle,

            onStart = viewModel::onNextState,
            onNext = viewModel::onNextState,
            onPrevious = viewModel::onPreviousState,
            onConfirm = {
                viewModel.onFinishMeasure()
            },
            onCancel = {
                viewModel.onCancelMeasure()
                // TODO: move to navigation function
//                navHostController.navigate(
//                    "${SalesAppScreens.FramesMeasureAnimation.name}/$eyeParameter"
//                ) {
//                    popUpTo("${SalesAppScreens.FramesMeasure.name}/{eye}") {
//                        inclusive = true
//                    }
//                }
                onCancel()
            },

            onMoveUp = viewModel::onMoveUp,
            onMoveDown = viewModel::onMoveDown,
            onMoveLeft = viewModel::onMoveLeft,
            onMoveRight = viewModel::onMoveRight,
            onRotateLeft = viewModel::onRotateLeft,
            onRotateRight = viewModel::onRotateRight,
            onExpand = viewModel::onExpand,
            onShrink = viewModel::onShrink,

            helperZoomState = zoomHelperState,
            onHelperClick = viewModel::onMeasuringHelperClicked,
        )
    }
}

@Composable
fun MeasuringDone(modifier: Modifier = Modifier, onTimeout: () -> Unit = {}) {
    val animationStart = remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (!animationStart.value) 0f else 1f,
        animationSpec = tween(2000)
    ) {
        onTimeout()
    }

    LaunchedEffect(true) {
        animationStart.value = true
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_measuring_complete)
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        LottieAnimation(
            modifier = modifier
                .padding(164.dp)
                .fillMaxSize(),
            composition = composition,
            progress = { progress },
        )
    }
}

@Composable
private fun MeasureFramesScreenImpl(
    modifier: Modifier = Modifier,
    imageUri: Uri = Uri.EMPTY,

    eye: Eye = Eye.None,

    measuringParameters: Map<Parameter, MeasuringParameter> = emptyMap(),
    animationState: PositioningAnimationState =
        PositioningAnimationState.PositioningOpticCenter,

    onBackFromCheckMiddle: () -> Unit = {},
    isCheckMiddleInvalid: Boolean = false,

    onStart: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},

    onMoveUp: () -> Unit = {},
    onMoveDown: () -> Unit = {},
    onMoveLeft: () -> Unit = {},
    onMoveRight: () -> Unit = {},
    onRotateLeft: () -> Unit = {},
    onRotateRight: () -> Unit = {},
    onExpand: () -> Unit = {},
    onShrink: () -> Unit = {},

    helperZoomState: HelperZoomState = HelperZoomState.ZoomNormal,
    onHelperClick: () -> Unit = {},
) {
    var hasDrawnAll by remember { mutableStateOf(false) }

    var isMoveLeftVisible by remember { mutableStateOf(isMoveLeftVisible(animationState)) }
    var isMoveDownVisible by remember { mutableStateOf(isMoveDownVisible(animationState)) }
    var isRotateLeftVisible by remember { mutableStateOf(isRotateLeftVisible(animationState)) }
    var isExpandVisible by remember { mutableStateOf(isExpandVisible(animationState)) }

    var isMoveRightVisible by remember { mutableStateOf(isMoveRightVisible(animationState)) }
    var isMoveUpVisible by remember { mutableStateOf(isMoveUpVisible(animationState)) }
    var isRotateRightVisible by remember { mutableStateOf(isRotateRightVisible(animationState)) }
    var isShrinkVisible by remember { mutableStateOf(isShrinkVisible(animationState)) }

    var isPreviousVisible by remember { mutableStateOf(isPreviousVisible(animationState)) }
    var isNextVisible by remember { mutableStateOf(isNextVisible(animationState)) }
    var isPlayVisible by remember { mutableStateOf(isPlayVisible(animationState)) }
    var isDoneVisible by remember { mutableStateOf(isDoneVisible(animationState)) }

    LaunchedEffect(animationState) {
        if (animationState == PositioningAnimationState.DrawAll) {
            hasDrawnAll = true
        }

        isMoveLeftVisible = isMoveLeftVisible(animationState)
        isMoveDownVisible = isMoveDownVisible(animationState)
        isRotateLeftVisible = isRotateLeftVisible(animationState)
        isExpandVisible = isExpandVisible(animationState)

        isMoveRightVisible = isMoveRightVisible(animationState)
        isMoveUpVisible = isMoveUpVisible(animationState)
        isRotateRightVisible = isRotateRightVisible(animationState)
        isShrinkVisible = isShrinkVisible(animationState)

        isPreviousVisible = isPreviousVisible(animationState)
        isNextVisible = isNextVisible(animationState)
        isPlayVisible = isPlayVisible(animationState)
        isDoneVisible = isDoneVisible(animationState) || hasDrawnAll
    }

//    FullScreen {
        lateinit var zoomPanel: AnimationPanel

        val transition = updateTransition(
            targetState = helperZoomState,
            "zoomTransitionLabel"
        )

        val helperWidth = transition.animateDp(
            transitionSpec = {
                tween()
            },
            label = "helperWidthAnimation"
        ) {
            when (it) {
                HelperZoomState.ZoomNormal -> 300.dp
                HelperZoomState.ZoomOut -> 600.dp
            }
        }

        val helperHeight = transition.animateDp(
            transitionSpec = {
                tween()
            },
            label = "helperHeightAnimation"
        ) {
            when (it) {
                HelperZoomState.ZoomNormal -> 120.dp
                HelperZoomState.ZoomOut -> 240.dp
            }
        }

        Box(modifier = modifier) {
            IconButton(
                modifier = Modifier
                    .padding(paddingValues = PaddingValues(32.dp))
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape,
                    )
                    .size(40.dp)
                    .align(alignment = Alignment.TopEnd)
                    .zIndex(1f),
                onClick = {
                    onCancel()
                }
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = Color.White,
                )
            }

            // TODO(Wait for issue to be solved :'( )
            // https://stackoverflow.com/questions/67975569/why-cant-i-use-animatedvisibility-in-a-boxscope
            // https://youtrack.jetbrains.com/issue/KT-48215
//            AnimatedVisibility(
//                visible = animationState != PositioningAnimationState.DrawAll &&
//                        animationState != PositioningAnimationState.Idle,
//                enter = fadeIn(),
//                exit = fadeOut(),
//            ) {
//
//            }
            if (animationState != PositioningAnimationState.DrawAll &&
                animationState != PositioningAnimationState.Idle
            ) {
                Column(
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .padding(32.dp)
                        .zIndex(2f)
                        .size(width = helperWidth.value, height = helperHeight.value)
                        .background(MaterialTheme.colors.secondaryVariant)
                        .border(
                            border = BorderStroke(2.dp, Color.White),
                            shape = RoundedCornerShape(8.dp),
                        )
                        .clickable {
                            onHelperClick()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    val composition by rememberLottieComposition(
                        lottieCompositionFor(animationState, eye)
                    )

                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        clipSpec = LottieClipSpec.Progress(0f, 1f),
                        modifier = modifier.fillMaxSize()
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(alignment = Alignment.CenterStart)
                    .zIndex(1f)
            ) {
                AnimatedVisibility(
                    visible = isMoveLeftVisible,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 4.dp)
                            .size(width = 80.dp, height = 80.dp)
                            .holdable(interactionSource = remember { MutableInteractionSource() },
                                onClick = { onMoveLeft() }),
                    ) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_icon_left)
                        )

                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            clipSpec = LottieClipSpec.Progress(0f, 1f),
                            modifier = modifier.fillMaxSize()
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isMoveDownVisible,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 4.dp)
                            .size(width = 80.dp, height = 80.dp)
                            .holdable(interactionSource = remember { MutableInteractionSource() },
                                onClick = { onMoveDown() }),
                    ) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_icon_down)
                        )

                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            clipSpec = LottieClipSpec.Progress(0f, 1f),
                            modifier = modifier.fillMaxSize()
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isRotateLeftVisible,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 4.dp)
                            .size(width = 80.dp, height = 80.dp)
                            .holdable(interactionSource = remember { MutableInteractionSource() },
                                onClick = { onRotateLeft() }),
                    ) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_icon_rotate_left)
                        )

                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            clipSpec = LottieClipSpec.Progress(0f, 1f),
                            modifier = modifier.fillMaxSize()
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isExpandVisible,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 4.dp)
                            .size(width = 80.dp, height = 80.dp)
                            .holdable(interactionSource = remember { MutableInteractionSource() },
                                onClick = { onExpand() }),
                    ) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_icon_expand)
                        )

                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            clipSpec = LottieClipSpec.Progress(0f, 1f),
                            modifier = modifier.fillMaxSize()
                        )
                    }
                }
            }

            if (imageUri != Uri.EMPTY) {
                AndroidView(
                    modifier = modifier
                        .align(alignment = Alignment.Center)
                        .zIndex(0f),
                    factory = { context ->
                        val bitmap = decodeAndRotateBitmapFrom(imageUri)
                        Timber.i("Using uri to load image: $imageUri")
                        Timber.i("Using parameters: $measuringParameters")

                        AnimationPanel(context, bitmap, measuringParameters)
                    }
                ) {
                    zoomPanel = it
                }
            }

            Column(
                modifier = Modifier
                    .align(alignment = Alignment.CenterEnd)
                    .zIndex(1f)
            ) {
                AnimatedVisibility(
                    visible = isMoveRightVisible,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 4.dp)
                            .size(width = 80.dp, height = 80.dp)
                            .holdable(interactionSource = remember { MutableInteractionSource() },
                                onClick = { onMoveRight() }),
                    ) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_icon_right)
                        )

                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            clipSpec = LottieClipSpec.Progress(0f, 1f),
                            modifier = modifier.fillMaxSize()
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isMoveUpVisible,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 4.dp)
                            .size(width = 80.dp, height = 80.dp)
                            .holdable(interactionSource = remember { MutableInteractionSource() },
                                onClick = { onMoveUp() }),
                    ) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_icon_up)
                        )

                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            clipSpec = LottieClipSpec.Progress(0f, 1f),
                            modifier = modifier.fillMaxSize()
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isRotateRightVisible,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 4.dp)
                            .size(width = 80.dp, height = 80.dp)
                            .holdable(interactionSource = remember { MutableInteractionSource() },
                                onClick = { onRotateRight() }),
                    ) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_icon_rotate_right)
                        )

                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            clipSpec = LottieClipSpec.Progress(0f, 1f),
                            modifier = modifier.fillMaxSize()
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isShrinkVisible,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 4.dp)
                            .size(width = 80.dp, height = 80.dp)
                            .holdable(interactionSource = remember { MutableInteractionSource() },
                                onClick = { onShrink() }),
                    ) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_icon_shrink)
                        )

                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            clipSpec = LottieClipSpec.Progress(0f, 1f),
                            modifier = modifier.fillMaxSize()
                        )
                    }
                }
            }

            if (isCheckMiddleInvalid) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 80.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colors.error,
                            shape = RoundedCornerShape(8.dp),
                        )
                        .background(
                            color = MaterialTheme.colors.background,
                            shape = RoundedCornerShape(8.dp),
                        )
                        .align(alignment = Alignment.BottomCenter),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ProvideTextStyle(value = MaterialTheme.typography.h5) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = stringResource(id = R.string.measure_invalid_middle_check),
                            textAlign = TextAlign.Center,
                        )
                    }

                    Row(modifier = Modifier.padding(8.dp)) {
                        OutlinedButton(
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f),
                            onClick = { onCancel() },
                        ) {
                            Text(
                                text = stringResource(
                                    id = R.string.measure_invalid_middle_check_cancel
                                )
                            )
                        }

                        OutlinedButton(
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f),
                            onClick = { onBackFromCheckMiddle() },
                        ) {
                            Text(
                                text = stringResource(
                                    id = R.string.measure_invalid_middle_check_retry
                                )
                            )
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .padding(bottom = 80.dp)
                        .zIndex(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    AnimatedVisibility(
                        visible = isPreviousVisible,
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .size(width = 100.dp, height = 100.dp)
                                .clickable { onPrevious() },
                        ) {
                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_backward)
                            )

                            LottieAnimation(
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                                clipSpec = LottieClipSpec.Progress(0f, 1f),
                                modifier = modifier.fillMaxSize()
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = isPlayVisible,
                    ) {
                        OutlinedButton(
                            modifier = Modifier
                                .size(width = 80.dp, height = 80.dp)
                                .padding(8.dp),
                            shape = CircleShape,
                            onClick = { onStart() }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = isDoneVisible,
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .size(width = 80.dp, height = 80.dp)
                                .clickable { onConfirm() },
                        ) {
                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_done)
                            )

                            LottieAnimation(
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                                clipSpec = LottieClipSpec.Progress(0f, 1f),
                                modifier = modifier.fillMaxSize()
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = isNextVisible,
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .size(width = 100.dp, height = 100.dp)
                                .clickable { onNext() },
                        ) {
                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_forward)
                            )

                            LottieAnimation(
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                                clipSpec = LottieClipSpec.Progress(0f, 1f),
                                modifier = modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
//    }
}