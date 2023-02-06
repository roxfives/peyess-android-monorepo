package com.peyess.salesapp.screen.sale.frames_measure

import android.media.Image
import android.net.Uri
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.headCompositionFor
import com.peyess.salesapp.R
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.screen.sale.frames_measure.state.CameraSetUpState
import com.peyess.salesapp.screen.sale.frames_measure.state.FramesMeasureState
import com.peyess.salesapp.screen.sale.frames_measure.state.FramesMeasureViewModel
import com.peyess.salesapp.screen.sale.frames_measure.state.HeadState
import com.peyess.salesapp.screen.sale.frames_measure.state.HelperZoomState
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.utils.device.infoAboutDevice
import com.peyess.salesapp.utils.extentions.activity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors

@Composable
fun TakePictureScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onNext: (eye: Eye) -> Unit = {}
) {
    val context = LocalContext.current

    val viewModel: FramesMeasureViewModel = mavericksViewModel()

    val eye = remember { mutableStateOf<Eye>(Eye.None) }
    val eyeParameter = navHostController.currentBackStackEntry
        ?.arguments
        ?.getString("eye")

    LaunchedEffect(eyeParameter) {
        Timber.i("Using eye $eyeParameter")
        eye.value = if (eyeParameter == "left") Eye.Left else Eye.Right
        viewModel.updateEye(eye.value)
    }

    val headState by viewModel.collectAsState(FramesMeasureState::takingHeadState)
    val helperZoomState by viewModel.collectAsState(FramesMeasureState::takingHeadZoomState)

    when (eye.value) {
        is Eye.None ->
            PeyessProgressIndicatorInfinite()
        else ->
            TakePictureScreenImpl(
                modifier = modifier,
                eye = eye.value,

                headState = headState,
                helperZoomState = helperZoomState,
                onHelperClick = viewModel::onTakingPictureHelperClicked,
                onHeadTaken = viewModel::onHeadTaken,

//                cameraSetUpState = viewState.value.cameraSetUpState,
                onCancelImageCapture = {
                    // TODO: Move to navigation
                    navHostController.popBackStack()
                },
                onImageSaved = { uri, rotation ->
                    Timber.i("Using uri $uri with rotation $rotation")

                    if (uri != null) {
                        val info = infoAboutDevice(context.activity())

                        viewModel.onImageCaptured(uri, rotation.toDouble(), info)
                        onNext(eye.value)
                    } else {
                        Timber.e("Failed while taking picture, Uri is null")
                    }
                },
            )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TakePictureScreenImpl(
    modifier: Modifier = Modifier,

    eye: Eye = Eye.None,

    onImageSaved: (Uri?, Int) -> Unit = { _, _ -> },
    onCancelImageCapture: () -> Unit = {},

    helperZoomState: HelperZoomState = HelperZoomState.ZoomNormal,
    onHelperClick: () -> Unit = {},

    cameraSetUpState: CameraSetUpState = CameraSetUpState.Idle,

    headState: HeadState = HeadState.Idle,
    onHeadTaken: (people: Int, eulerX: Double, eulerY: Double, eulerZ: Double) -> Unit =
        { _, _, _, _ -> },
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
            .build()
    }

    var cameraProvider: ProcessCameraProvider? = null
    var camera: Camera? = null
    var preview: Preview? = null
    val cameraSelector = remember {
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }

    val transition = updateTransition(
        targetState = helperZoomState,
        "zoomTransitionLabel"
    )

    val helperWidth = transition.animateDp(
        transitionSpec = {
            tween()
        },
        label = "helperPictureWidthAnimation"
    ) {
        when (it) {
            HelperZoomState.ZoomNormal -> 160.dp
            HelperZoomState.ZoomOut -> 320.dp
        }
    }

    val helperHeight = transition.animateDp(
        transitionSpec = {
            tween()
        },
        label = "helperPictureHeightAnimation"
    ) {
        when (it) {
            HelperZoomState.ZoomNormal -> 180.dp
            HelperZoomState.ZoomOut -> 360.dp
        }
    }

    val animationTransition = updateTransition(
        targetState = headState,
        "animationState"
    )
    val borderColor = animationTransition.animateColor(
        transitionSpec = {
            tween()
        },
        label = "helperWidthAnimation",
    ) {
        when (it) {
            HeadState.LookingForward ->
                Color.Green
            else ->
                MaterialTheme.colors.secondaryVariant
        }
    }
    val borderWidth = animationTransition.animateDp(
        transitionSpec = {
            tween()
        },
        label = "helperWidthAnimation",
    ) {
        when (it) {
            HeadState.LookingForward ->
                6.dp
            else ->
                2.dp
        }
    }

//    FullScreen {
        Box(modifier = modifier) {
            if (cameraSetUpState == CameraSetUpState.Idle) {
                Surface(
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .padding(32.dp)
                        .zIndex(2f)
                        .size(width = helperWidth.value, height = helperHeight.value),
                    color = MaterialTheme.colors.background,
                    border = BorderStroke(borderWidth.value, borderColor.value),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        onHelperClick()
                    }
                ) {
                    val composition by rememberLottieComposition(
                        headCompositionFor(headState)
                    )

                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        clipSpec = LottieClipSpec.Progress(0f, 1f),
                        modifier = modifier.fillMaxSize()
                    )
                }
            }

            IconButton(
                modifier = Modifier
                    .padding(paddingValues = PaddingValues(32.dp))
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape,
                    )
                    .size(40.dp)
                    .align(alignment = Alignment.TopEnd)
                    .zIndex(2f),
                onClick = {
                    cameraProviderFuture.get().unbindAll()
                    onCancelImageCapture()
                }
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = Color.White,
                )
            }

            // TODO: Refactor hardcoded values
            Image(
                modifier = Modifier
                    .size(
                        width = 333.dp,
                        height = 756.dp,
                    )
                    .align(alignment = Alignment.Center)
                    .zIndex(1f),
                painter = if (eye == Eye.Left) {
                    painterResource(id = R.drawable.ic_measure_picture_helper_left)
                } else {
                    painterResource(id = R.drawable.ic_measure_picture_helper_right)
                },
                contentDescription = null,
            )

            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_STOP) {
                        cameraProviderFuture.get().unbindAll()
                        onCancelImageCapture()
                    }
                }

                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
            val coroutineScope = rememberCoroutineScope()
            AndroidView(
                modifier = modifier
                    .fillMaxSize()
                    .zIndex(0f),
                factory = { ctx ->
                    val executor = ContextCompat.getMainExecutor(ctx)
                    val analysisExecutor = Executors.newSingleThreadExecutor()

                    val previewView = PreviewView(ctx)
                    previewView.scaleType = PreviewView.ScaleType.FIT_CENTER
                    previewView.setOnTouchListener(
                        View.OnTouchListener {
                                _, event: MotionEvent ->
                            when (event.action) {
                                MotionEvent.ACTION_DOWN -> return@OnTouchListener true
                                MotionEvent.ACTION_UP -> {
                                    val factory = previewView.getMeteringPointFactory()
                                    val point = factory.createPoint(event.x, event.y)
                                    val action = FocusMeteringAction.Builder(point).build()

                                    camera?.cameraControl?.startFocusAndMetering(action)
                                    return@OnTouchListener true
                                }
                                else -> return@OnTouchListener false
                            }
                        }
                    )

                    val options = FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                        .setMinFaceSize(0.6f)
                        .build()
                    val detector = FaceDetection.getClient(options)

                    imageAnalysis.setAnalyzer(
                        analysisExecutor
                    ) { image ->
                        val rotationDegrees = image.imageInfo.rotationDegrees

                        when (val imageProxy = image.image) {
                            is Image -> {
                                val analyzedImage = InputImage
                                    .fromMediaImage(imageProxy, rotationDegrees)

                                coroutineScope.launch {
                                    // TODO: remove hardcoded value
                                    delay(500L)

                                    detector.process(analyzedImage)
                                        .addOnSuccessListener { faces ->
                                            faces.sortBy { faceA ->
                                                -faceA.boundingBox.width()
                                            }

                                            if (faces.size > 0) {
                                                // TODO: remove hardcoded value
                                                onHeadTaken(
                                                    1, // faces.size,
                                                    faces[0].headEulerAngleX.toDouble(),
                                                    faces[0].headEulerAngleY.toDouble(),
                                                    faces[0].headEulerAngleZ.toDouble(),
                                                )
                                            } else {
                                                onHeadTaken(
                                                    faces.size,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                )
                                            }

                                            image.close()
                                        }
                                        .addOnFailureListener {
                                            Timber.i("Failed to get image")
                                            image.close()
                                        }
                                        .addOnCanceledListener {
                                            Timber.i("Canceled taking image")
                                            image.close()
                                        }
                                }
                            }
                        }
                    }

                    cameraProviderFuture.addListener(
                        {
                            cameraProvider = cameraProviderFuture.get()
                            preview = Preview.Builder()
                                .build().also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                            cameraProvider?.unbindAll()
                            camera = cameraProvider?.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview!!,
                                imageCapture,
                                imageAnalysis,
                            )
                            camera?.cameraControl?.enableTorch(
                                camera?.cameraInfo?.hasFlashUnit() == true
                            )
                        },
                        executor
                    )

                    previewView
                },
            ) {}

            if (cameraSetUpState == CameraSetUpState.Idle) {
                IconButton(
                    modifier = Modifier
                        .padding(paddingValues = PaddingValues(16.dp))
                        .background(
                            color = Color.Transparent,
                            shape = CircleShape,
                        )
                        .size(72.dp)
                        .align(alignment = Alignment.BottomCenter)
                        .zIndex(2f),
                    enabled = true, // takingHeadState == HeadState.LookingForward,
                    onClick = {
                        Timber.i("Starting to take picture")
                        val timeStamp = DateTimeFormatter
                            .ofPattern("yyyyMMdd_HHmmss")
                            .format(ZonedDateTime.now())

                        val fileName = "${timeStamp}_glasses_picture"
                        val fileImage = File.createTempFile(
                            fileName,
                            ".jpg",
                            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        )

                        Timber.i("Taking picture")
                        imageCapture.takePicture(
                            ImageCapture.OutputFileOptions
                                .Builder(fileImage)
                                .build(),
                            ContextCompat.getMainExecutor(context),
                            object: ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(
                                    outputFileResults: ImageCapture.OutputFileResults
                                ) {
                                    cameraProviderFuture.get().unbindAll()
//                                    cameraProviderFuture.get().shutdown()

                                    Timber.i("On image save at ${outputFileResults.savedUri}")
                                    onImageSaved(outputFileResults.savedUri, 0)
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    exception.printStackTrace()
                                }
                            }
                        )
                    }
                ) {
                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.lottie_measuring_take_picture)
                    )

                    LottieAnimation(
                        modifier = modifier.fillMaxSize(),
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        clipSpec = LottieClipSpec.Progress(0f, 1f),
                        speed = 0.1f,
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = cameraSetUpState == CameraSetUpState.SettingUp,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.primary)
                    .zIndex(10f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.h2) {
                    Text(
                        text = "Mantenha o aparelho firme",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
//        }
    }
}