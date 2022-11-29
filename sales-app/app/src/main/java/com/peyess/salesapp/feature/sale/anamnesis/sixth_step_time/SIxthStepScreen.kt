package com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time.state.SixthStepState
import com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time.state.SixthStepViewModel
import com.peyess.salesapp.feature.sale.anamnesis.utils.toTimeString
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import java.time.LocalTime

@Composable
fun SixthStepScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
) {

    val viewModel: SixthStepViewModel = mavericksActivityViewModel()

    val timeWakeUp by viewModel.collectAsState(SixthStepState::timeWakeUp)
    val timeGoToBed by viewModel.collectAsState(SixthStepState::timeGoToBed)

    val totalUsed by viewModel.collectAsState(SixthStepState::totalUsed)
    val totalAvailable by viewModel.collectAsState(SixthStepState::totalAvailable)

    val reading by viewModel.collectAsState(SixthStepState::reading)
    val readingSlider by viewModel.collectAsState(SixthStepState::readingSlider)
    val phone by viewModel.collectAsState(SixthStepState::phone)
    val phoneSlider by viewModel.collectAsState(SixthStepState::phoneSlider)
    val computer by viewModel.collectAsState(SixthStepState::computer)
    val computerSlider by viewModel.collectAsState(SixthStepState::computerSlider)
    val television by viewModel.collectAsState(SixthStepState::television)
    val televisionSlider by viewModel.collectAsState(SixthStepState::televisionSlider)
    val driving by viewModel.collectAsState(SixthStepState::driving)
    val drivingSlider by viewModel.collectAsState(SixthStepState::drivingSlider)
    val sports by viewModel.collectAsState(SixthStepState::sports)
    val sportsSlider by viewModel.collectAsState(SixthStepState::sportsSlider)
    val externalArea by viewModel.collectAsState(SixthStepState::externalArea)
    val externalAreaSlider by viewModel.collectAsState(SixthStepState::externalAreaSlider)
    val internalArea by viewModel.collectAsState(SixthStepState::internalArea)
    val internalAreaSlider by viewModel.collectAsState(SixthStepState::internalAreaSlider)

    SixthStepScreenImpl(
        modifier = modifier,

        timeWakeUp = timeWakeUp,
        timeGoToBed = timeGoToBed,
        onTimeWakeUpChanged = viewModel::onWakeUpChanged,
        onTimeSleepChanged = viewModel::onGoToBedChanged,

        totalUsed = totalUsed,
        totalAvailable = totalAvailable,

        totalReading = reading,
        totalReadingSlider = readingSlider,
        onTotalReadingChanged = viewModel::onReadingChanged,
        totalPhone = phone,
        totalPhoneSlider = phoneSlider,
        onTotalPhoneChanged = viewModel::onPhoneChanged,
        totalComputer = computer,
        totalComputerSlider = computerSlider,
        onTotalComputerChanged = viewModel::onComputerChanged,
        totalTelevision = television,
        totalTelevisionSlider = televisionSlider,
        onTotalTelevisionChanged = viewModel::onTelevisionChanged,
        totalDriving = driving,
        totalDrivingSlider = drivingSlider,
        onTotalDrivingChanged = viewModel::onDrivingChanged,
        totalSports = sports,
        totalSportsSlider = sportsSlider,
        onTotalSportsChanged = viewModel::onSportsChanged,
        totalExternalArea = externalArea,
        totalExternalAreaSlider = externalAreaSlider,
        onTotalExternalAreaChanged = viewModel::onExternalAreaChanged,
        totalInternalArea = internalArea,
        totalInternalAreaSlider = internalAreaSlider,
        onTotalInternalAreaChanged = viewModel::onInternalAreaChanged,

        onNext = onNext,
    )
}

@Composable
private fun SixthStepScreenImpl(
    modifier: Modifier = Modifier,

    timeWakeUp: Float = 360f,
    timeGoToBed: Float = 1320f,
    onTimeWakeUpChanged: (value: Float) -> Unit = {},
    onTimeSleepChanged: (value: Float) -> Unit = {},

    totalAvailable: Float = 0f,
    totalUsed: Float = 0f,

    totalReading: Float = 0f,
    totalReadingSlider: Float = 0f,
    onTotalReadingChanged: (value: Float) -> Unit = {},
    totalPhone: Float = 0f,
    totalPhoneSlider: Float = 0f,
    onTotalPhoneChanged: (value: Float) -> Unit = {},
    totalComputer: Float = 0f,
    totalComputerSlider: Float = 0f,
    onTotalComputerChanged: (value: Float) -> Unit = {},
    totalTelevision: Float = 0f,
    totalTelevisionSlider: Float = 0f,
    onTotalTelevisionChanged: (value: Float) -> Unit = {},
    totalDriving: Float = 0f,
    totalDrivingSlider: Float = 0f,
    onTotalDrivingChanged: (value: Float) -> Unit = {},
    totalSports: Float = 0f,
    totalSportsSlider: Float = 0f,
    onTotalSportsChanged: (value: Float) -> Unit = {},
    totalExternalArea: Float = 0f,
    totalExternalAreaSlider: Float = 0f,
    onTotalExternalAreaChanged: (value: Float) -> Unit = {},
    totalInternalArea: Float = 0f,
    totalInternalAreaSlider: Float = 0f,
    onTotalInternalAreaChanged: (value: Float) -> Unit = {},

    onNext: () -> Unit = {},
) {
    val density = LocalDensity.current
    val titleWidthModifierState = remember { MinimumWidthState() }
    val timeWidthModifierState = remember { MinimumWidthState() }

    val titleWidthModifier = Modifier
        .minimumWidthModifier(density = density,state = titleWidthModifierState)
    val timeWidthModifier = Modifier
        .minimumWidthModifier(density = density,state = timeWidthModifierState)

    val sleepDialogState = rememberMaterialDialogState(false)
    MaterialDialog(
        dialogState = sleepDialogState,
        buttons = {
            // TODO: Use string resource
            positiveButton("CONFIRMAR")
            negativeButton("CANCELAR")
        }
    ) {
        // TODO: Use string resource
        title("Selecione o horário em que costuma dormir")

        timepicker(
            title = "",
            initialTime =  LocalTime
                .of(timeGoToBed.toInt() / 60, timeGoToBed.toInt() % 60),
            is24HourClock = true,
        ) { time ->
            onTimeSleepChanged(time.hour * 60f + time.minute)
        }
    }

    val wakeUpDialogState = rememberMaterialDialogState(true)
    MaterialDialog(
        dialogState = wakeUpDialogState,
        buttons = {
            // TODO: Use string resource
            positiveButton("CONFIRMAR") { sleepDialogState.show() }
            negativeButton("CANCELAR")
        }
    ) {
        // TODO: Use string resource
        title("Selecione o horário em que costuma acordar")

        timepicker(
            title = "",
            initialTime = LocalTime
                .of(timeWakeUp.toInt() / 60, timeWakeUp.toInt() % 60),
            is24HourClock = true,
        ) { time ->
            onTimeWakeUpChanged(time.hour * 60f + time.minute)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.total_usage_string)
                .format(toTimeString(totalUsed), toTimeString(totalAvailable)),
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.SemiBold),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.user_profile_sixth_step)
        )
        Spacer(modifier = Modifier.height(8.dp))

        UsageCounter(
            modifier = Modifier.padding(horizontal = 32.dp),

            title = stringResource(id = R.string.anamnesis_reading),

            titleWidthModifier = titleWidthModifier,
            timeWidthModifier = timeWidthModifier,

            sliderValue = totalReadingSlider,
            totalUsed = totalReading,
            onTotalChanged = onTotalReadingChanged,
        )

        Spacer(modifier = Modifier.height(4.dp))

        UsageCounter(
            modifier = Modifier.padding(horizontal = 32.dp),

            title = stringResource(id = R.string.phone_or_tablet),

            titleWidthModifier = titleWidthModifier,
            timeWidthModifier = timeWidthModifier,

            sliderValue = totalPhoneSlider,
            totalUsed = totalPhone,
            onTotalChanged = onTotalPhoneChanged,
        )

        Spacer(modifier = Modifier.height(4.dp))

        UsageCounter(
            modifier = Modifier.padding(horizontal = 32.dp),

            title = stringResource(id = R.string.computer),

            titleWidthModifier = titleWidthModifier,
            timeWidthModifier = timeWidthModifier,

            sliderValue = totalComputerSlider,
            totalUsed = totalComputer,
            onTotalChanged = onTotalComputerChanged,
        )

        Spacer(modifier = Modifier.height(4.dp))

        UsageCounter(
            modifier = Modifier.padding(horizontal = 32.dp),

            title = stringResource(id = R.string.tv),

            titleWidthModifier = titleWidthModifier,
            timeWidthModifier = timeWidthModifier,

            sliderValue = totalTelevisionSlider,
            totalUsed = totalTelevision,
            onTotalChanged = onTotalTelevisionChanged,
        )

        Spacer(modifier = Modifier.height(4.dp))

        UsageCounter(
            modifier = Modifier.padding(horizontal = 32.dp),

            title = stringResource(id = R.string.driving),

            titleWidthModifier = titleWidthModifier,
            timeWidthModifier = timeWidthModifier,

            sliderValue = totalDrivingSlider,
            totalUsed = totalDriving,
            onTotalChanged = onTotalDrivingChanged,
        )

        Spacer(modifier = Modifier.height(4.dp))

        UsageCounter(
            modifier = Modifier.padding(horizontal = 32.dp),

            title = stringResource(id = R.string.sports),

            titleWidthModifier = titleWidthModifier,
            timeWidthModifier = timeWidthModifier,

            sliderValue = totalSportsSlider,
            totalUsed = totalSports,
            onTotalChanged = onTotalSportsChanged,
        )

        Spacer(modifier = Modifier.height(4.dp))

        UsageCounter(
            modifier = Modifier.padding(horizontal = 32.dp),

            title = stringResource(id = R.string.external_area),

            titleWidthModifier = titleWidthModifier,
            timeWidthModifier = timeWidthModifier,

            sliderValue = totalExternalAreaSlider,
            totalUsed = totalExternalArea,
            onTotalChanged = onTotalExternalAreaChanged,
        )

        Spacer(modifier = Modifier.height(4.dp))

        UsageCounter(
            modifier = Modifier.padding(horizontal = 32.dp),

            title = stringResource(id = R.string.internal_area),

            titleWidthModifier = titleWidthModifier,
            timeWidthModifier = timeWidthModifier,

            sliderValue = totalInternalAreaSlider,
            totalUsed = totalInternalArea,
            onTotalChanged = onTotalInternalAreaChanged,
        )

        Divider(
            modifier = Modifier.padding(horizontal = 120.dp, vertical = 32.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Row(
            modifier = Modifier.clickable { wakeUpDialogState.show() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = toTimeString(timeWakeUp),
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = stringResource(id = R.string.wake_up))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                modifier = Modifier.size(90.dp),
                painter = painterResource(id = R.drawable.ic_clock),
                tint = MaterialTheme.colors.primary,
                contentDescription = "",
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = toTimeString(timeGoToBed),
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = stringResource(id = R.string.sleep))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PeyessStepperFooter(
            onNext = onNext
        )
    }
}

@Composable
private fun UsageCounter(
    modifier: Modifier = Modifier,

    titleWidthModifier: Modifier = Modifier,
    timeWidthModifier: Modifier = Modifier,

    title: String = "",
    sliderValue: Float = 0f,
    totalUsed: Float = 0f,

    onTotalChanged: (value: Float) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = titleWidthModifier,
            text = title,
            style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
        )
        Spacer(modifier = Modifier.width(16.dp))

        Slider(
            modifier = Modifier.weight(6f),
            value =  sliderValue,
            onValueChange = onTotalChanged,
            valueRange = 0f..20f,
            steps = 21,
        )

        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = timeWidthModifier,
            text = toTimeString(totalUsed),
            style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
        )
    }
}

@Preview
@Composable
private fun UsageCounterPreview() {
    SalesAppTheme {
        UsageCounter()
    }
}