package com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time.state.SixthStepState
import com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time.state.SixthStepViewModel
import com.peyess.salesapp.feature.sale.anamnesis.utils.toTimeString
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import java.text.DecimalFormat

val chartHeight = 256.dp
val barChartHeight = 480.dp

@Composable
fun GraphsScreen(
    modifier: Modifier = Modifier,

    onNext: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
) {
    val viewModel: SixthStepViewModel = mavericksActivityViewModel()

    // TODO: remove this when refactoring the anamnesis feature
    LaunchedEffect(true) {
        viewModel.loadSale()
    }

    val serviceOrderId by viewModel.collectAsState(SixthStepState::serviceOrderId)
    val saleId by viewModel.collectAsState(SixthStepState::saleId)

    val farUsage by viewModel.collectAsState(SixthStepState::farUsage)
    val nearUsage by viewModel.collectAsState(SixthStepState::nearUsage)
    val intermediateUsage by viewModel.collectAsState(SixthStepState::intermediateUsage)

    val sunLightExposure by viewModel.collectAsState(SixthStepState::sunLightExposure)
    val artificialLightExposure by viewModel.collectAsState(SixthStepState::artificialLightExposure)
    val blueLightExposure by viewModel.collectAsState(SixthStepState::blueLightExposure)

    val reading by viewModel.collectAsState(SixthStepState::reading)
    val phone by viewModel.collectAsState(SixthStepState::phone)
    val computer by viewModel.collectAsState(SixthStepState::computer)
    val television by viewModel.collectAsState(SixthStepState::television)
    val driving by viewModel.collectAsState(SixthStepState::driving)
    val sports by viewModel.collectAsState(SixthStepState::sports)
    val externalArea by viewModel.collectAsState(SixthStepState::externalArea)
    val internalArea by viewModel.collectAsState(SixthStepState::internalArea)

    GraphsScreenImpl(
        modifier = modifier,

        farUsage = farUsage,
        nearUsage = nearUsage,
        intermediateUsage = intermediateUsage,

        sunLightExposure = sunLightExposure,
        artificialLightExposure = artificialLightExposure,
        blueLightExposure = blueLightExposure,

        reading = reading,
        phone = phone,
        computer = computer,
        television = television,
        driving = driving,
        sports = sports,
        externalArea = externalArea,
        internalArea = internalArea,

        onNext = { onNext(saleId, serviceOrderId) },
    )
}

@Composable
private fun GraphsScreenImpl(
    modifier: Modifier = Modifier,

    farUsage: Float = 0f,
    nearUsage: Float = 0f,
    intermediateUsage: Float = 0f,

    sunLightExposure: Float = 0f,
    artificialLightExposure: Float = 0f,
    blueLightExposure: Float = 0f,

    reading: Float = 0f,
    phone: Float = 0f,
    computer: Float = 0f,
    television: Float = 0f,
    driving: Float = 0f,
    sports: Float = 0f,
    externalArea: Float = 0f,
    internalArea: Float = 0f,

    onNext: () -> Unit = {},
) {
    val colorWhite = Color.White

    val colorRed = Color.hsv(1f,0.77f,0.9f)
    val colorGreen = Color.hsv(123f,0.58f,0.63f)
    val colorOrange = Color.hsv(33f,1f,0.98f)

    val colorWhiteArgb = colorWhite.toArgb()
    val colorRedArgb = colorRed.toArgb()
    val colorGreenArgb = colorGreen.toArgb()
    val colorOrangeArgb = colorOrange.toArgb()

    val colorSunOn = Color.hsv(33f,1f,0.98f)
    val colorSunOff = Color.hsv(26f,1f,0.74f)

    val colorArtificialOn = Color.hsv(201f,0.23f,0.50f)
    val colorArtificialOff = Color.hsv(182f,1f,0.39f)

    val colorBlueOn = Color.hsv(201f,0.23f,0.50f)
    val colorBlueOff = Color.hsv(198f,0.59f,0.15f)

    val colorSunOnArgb = colorSunOn.toArgb()
    val colorSunOffArgb = colorSunOff.toArgb()

    val colorArtificialOnArgb = colorArtificialOn.toArgb()
    val colorArtificialOffArgb = colorArtificialOff.toArgb()

    val colorBlueOnArgb = colorBlueOn.toArgb()
    val colorBlueOffArgb = colorBlueOff.toArgb()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PieChartView(
                modifier = Modifier
                    .height(chartHeight)
                    .weight(1f),
                farUsage = farUsage,
                nearUsage = nearUsage,
                intermediateUsage = intermediateUsage,
            )

            Spacer(modifier = Modifier.width(32.dp))

            Column(modifier = Modifier.weight(1f)) {
                if (farUsage > 0) {
                    Column {
                        Row {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(color = colorRed)
                            )

                            Spacer(modifier = Modifier.size(8.dp))

                            // TODO: use string  res
                            Text(
                                text = "Longe",
                                style = MaterialTheme.typography.subtitle1
                                    .copy(fontWeight = FontWeight.Bold),
                            )
                        }

                        Column(modifier = Modifier.padding(start = 32.dp)) {
                            if (sports > 0) {
                                Row {
                                   Text(text = "Esportes: ${toTimeString(sports)}")
                                }
                            }

                            if (driving > 0) {
                                Row {
                                    Text(text = "Dirigindo: ${toTimeString(driving)}")
                                }
                            }

                            if (externalArea > 0) {
                                Row {
                                    Text(text = "Em área externa: ${toTimeString(externalArea)}")
                                }
                            }
                        }
                    }
                }

                if (nearUsage > 0) {
                    Column {
                        Row {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(color = colorGreen)
                            )

                            Spacer(modifier = Modifier.size(8.dp))

                            // TODO: use string  res
                            Text(
                                text = "Perto",
                                style = MaterialTheme.typography.subtitle1
                                    .copy(fontWeight = FontWeight.Bold),
                            )
                        }

                        Column(modifier = Modifier.padding(start = 32.dp)) {
                            if (phone > 0) {
                                Row {
                                    Text(text = "Usando celular: ${toTimeString(phone)}")
                                }
                            }

                            if (reading > 0) {
                                Row {
                                    Text(text = "Lendo: ${toTimeString(reading)}")
                                }
                            }
                        }
                    }
                }

                if (intermediateUsage > 0) {
                    Column {
                        Row {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(color = colorOrange)
                            )

                            Spacer(modifier = Modifier.size(8.dp))

                            // TODO: use string  res
                            Text(
                                text = "Intermediário",
                                style = MaterialTheme.typography.subtitle1
                                    .copy(fontWeight = FontWeight.Bold),
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(start = 32.dp)) {
                        if (computer > 0) {
                            Row {
                                Text(text = "Computador: ${toTimeString(computer)}")
                            }
                        }

                        if (television > 0) {
                            Row {
                                Text(text = "Televisão: ${toTimeString(television)}")
                            }
                        }

                        if (driving > 0) {
                            Row {
                                Text(text = "Dirigindo: ${toTimeString(driving)}")
                            }
                        }

                        if (internalArea > 0) {
                            Row {
                                Text(text = "Área interna: ${toTimeString(internalArea)}")
                            }
                        }
                    }
                }
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Column {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier
                    .size(32.dp)
                    .background(color = colorSunOn))

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(id = R.string.bar_chart_sun_light_title),
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                BarChartView(
                    modifier = Modifier
                        .height(barChartHeight)
                        .weight(1f),

                    colorOn = colorSunOnArgb,
                    colorOff = colorSunOffArgb,

                    totalUsage = sunLightExposure,
                )

                Spacer(modifier = Modifier.width(32.dp))

                Column(
                    modifier = Modifier
                        .height(barChartHeight)
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (sports > 0) {
                        Text(text = "Esportes: ${toTimeString(sports)}")
                    }

                    if (externalArea > 0) {
                        Text(text = "Área externa: ${toTimeString(externalArea)}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier
                    .size(32.dp)
                    .background(color = colorArtificialOn))

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(id = R.string.bar_chart_artificial_light_title),
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                BarChartView(
                    modifier = Modifier
                        .height(barChartHeight)
                        .weight(1f),

                    colorOn = colorArtificialOnArgb,
                    colorOff = colorArtificialOffArgb,

                    totalUsage = artificialLightExposure,
                )

                Spacer(modifier = Modifier.width(32.dp))

                Column(
                    modifier = Modifier
                        .height(barChartHeight)
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (phone > 0) {
                        Text(text = "Celular: ${toTimeString(phone)}")
                    }

                    if (computer > 0) {
                        Text(text = "Computador: ${toTimeString(computer)}")
                    }

                    if (television > 0) {
                        Text(text = "Televisão: ${toTimeString(television)}")
                    }

                    if (internalArea > 0) {
                        Text(text = "Área interna: ${toTimeString(internalArea)}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
            )

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier
                    .size(32.dp)
                    .background(color = colorBlueOn))

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(id = R.string.bar_chart_blue_light_title),
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                BarChartView(
                    modifier = Modifier
                        .height(barChartHeight)
                        .weight(1f),

                    colorOn = colorBlueOnArgb,
                    colorOff = colorBlueOffArgb,

                    totalUsage = blueLightExposure,
                )

                Spacer(modifier = Modifier.width(32.dp))

                Column(
                    modifier = Modifier
                        .height(barChartHeight)
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (phone > 0) {
                        Text(text = "Celular: ${toTimeString(phone)}")
                    }

                    if (computer > 0) {
                        Text(text = "Computador: ${toTimeString(computer)}")
                    }

                    if (television > 0) {
                        Text(text = "Televisão: ${toTimeString(television)}")
                    }

                    if (sports > 0) {
                        Text(text = "Esportes: ${toTimeString(sports)}")
                    }

                    if (externalArea > 0) {
                        Text(text = "Área externa: ${toTimeString(externalArea)}")
                    }

                    if (internalArea > 0) {
                        Text(text = "Área interna: ${toTimeString(internalArea)}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        PeyessStepperFooter(onNext = onNext)
    }
}

@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun PieChartView(
    modifier: Modifier = Modifier,

    salesApplication: SalesApplication =
        LocalContext.current.applicationContext as SalesApplication,

    farUsage: Float = 0f,
    nearUsage: Float = 0f,
    intermediateUsage: Float = 0f,
) {
    val colorWhite = Color.White

    val colorRed = Color.hsv(1f,0.77f,0.9f)
    val colorGreen = Color.hsv(123f,0.58f,0.63f)
    val colorOrange = Color.hsv(33f,1f,0.98f)

    val colorWhiteArgb = colorWhite.toArgb()
    val colorRedArgb = colorRed.toArgb()
    val colorGreenArgb = colorGreen.toArgb()
    val colorOrangeArgb = colorOrange.toArgb()

    val colorSunOn = Color.hsv(33f,1f,0.98f)
    val colorSunOff = Color.hsv(26f,1f,0.74f)

    val colorArtificialOn = Color.hsv(201f,0.23f,0.50f)
    val colorArtificialOff = Color.hsv(182f,1f,0.39f)

    val colorBlueOn = Color.hsv(201f,0.23f,0.50f)
    val colorBlueOff = Color.hsv(198f,0.59f,0.15f)

    val colorSunOnArgb = colorSunOn.toArgb()
    val colorSunOffArgb = colorSunOff.toArgb()

    val colorArtificialOnArgb = colorArtificialOn.toArgb()
    val colorArtificialOffArgb = colorArtificialOff.toArgb()

    val colorBlueOnArgb = colorBlueOn.toArgb()
    val colorBlueOffArgb = colorBlueOff.toArgb()

    AndroidView(
        modifier = modifier,
        factory = { context ->
            val pieChart = PieChart(context)
            
            pieChart.setUsePercentValues(true)
            pieChart.setUsePercentValues(true)
            pieChart.description.setEnabled(false)
            pieChart.legend.setEnabled(false)

            pieChart.holeRadius = 0f
            pieChart.transparentCircleRadius = 0f

            pieChart.setEntryLabelColor(colorWhiteArgb)
            pieChart.setEntryLabelTextSize(16f)

            pieChart.setCenterTextColor(colorWhiteArgb)

            val entries = mutableListOf(
                PieEntry(farUsage, salesApplication.stringResource(id = R.string.pie_chart_far_label), null),
                PieEntry(nearUsage, salesApplication.stringResource(id = R.string.pie_chart_near_label), null),
                PieEntry(intermediateUsage, salesApplication.stringResource(id = R.string.pie_chart_middle_label), null),
            )

            val dataSet = PieDataSet(entries, salesApplication.stringResource(R.string.pie_chart_label))
            dataSet.setColors(colorRedArgb, colorGreenArgb, colorOrangeArgb)

            val pieData = PieData(dataSet)
            pieData.setValueTextSize(16f)
            pieData.setValueTextColor(colorWhiteArgb)
            pieData.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val df = DecimalFormat("#.##")
                    return df.format(value.toDouble()) + "%"
                }
            })

            pieChart.data = pieData

            pieChart

        }) {

        it.animateXY(3000, 3000)
    }
}

@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun BarChartView(
    modifier: Modifier = Modifier,

    colorOn: Int = 0,
    colorOff: Int = 0,

    totalUsage: Float = 0f,
) {
    val colorWhite = Color.White

    val colorRed = Color.hsv(1f,0.77f,0.9f)
    val colorGreen = Color.hsv(123f,0.58f,0.63f)
    val colorOrange = Color.hsv(33f,1f,0.98f)

    val colorWhiteArgb = colorWhite.toArgb()
    val colorRedArgb = colorRed.toArgb()
    val colorGreenArgb = colorGreen.toArgb()
    val colorOrangeArgb = colorOrange.toArgb()

    val colorSunOn = Color.hsv(33f,1f,0.98f)
    val colorSunOff = Color.hsv(26f,1f,0.74f)

    val colorArtificialOn = Color.hsv(201f,0.23f,0.50f)
    val colorArtificialOff = Color.hsv(182f,1f,0.39f)

    val colorBlueOn = Color.hsv(201f,0.23f,0.50f)
    val colorBlueOff = Color.hsv(198f,0.59f,0.15f)

    val colorSunOnArgb = colorSunOn.toArgb()
    val colorSunOffArgb = colorSunOff.toArgb()

    val colorArtificialOnArgb = colorArtificialOn.toArgb()
    val colorArtificialOffArgb = colorArtificialOff.toArgb()

    val colorBlueOnArgb = colorBlueOn.toArgb()
    val colorBlueOffArgb = colorBlueOff.toArgb()

    AndroidView(
        modifier = modifier,
        factory = { context ->
            val barChart = BarChart(context)

            barChart.setDrawBarShadow(false)
            barChart.setDrawValueAboveBar(false)

            barChart.description.isEnabled = false

            barChart.setPinchZoom(false)
            barChart.setDrawGridBackground(false)
            barChart.legend.isEnabled = false
            barChart.setDrawBorders(true)
            barChart.setDrawMarkers(false)

            barChart.xAxis.setDrawAxisLine(false)
            barChart.xAxis.setDrawGridLines(false)
            barChart.xAxis.setDrawLabels(false)
            barChart.xAxis.setDrawLimitLinesBehindData(false)

            barChart.axisRight.setDrawAxisLine(false)
            barChart.axisRight.setDrawGridLines(false)
            barChart.axisRight.setDrawLabels(false)
            barChart.axisRight.setDrawLimitLinesBehindData(false)

            barChart.isHighlightFullBarEnabled = false
            barChart.isHighlightPerDragEnabled = false
            barChart.isHighlightPerTapEnabled = false

            val exposure = 100f * totalUsage
            val nonExposed = 100f * (1f - totalUsage)

            val entries = mutableListOf(
                BarEntry(0f, floatArrayOf(exposure, nonExposed))
            )

            val dataSet = BarDataSet(entries, "")
            dataSet.setDrawIcons(false)
            dataSet.setColors(colorOn, colorOff)

            val barData = BarData(dataSet)
            barData.setValueTextSize(16f)
            barData.setValueTextColor(colorWhiteArgb)
            barData.barWidth = 1f
            barData.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val df = DecimalFormat("##.##")
                    val identifier = if (value.toFloat() == exposure) {
                        "de exposição"
                    } else {
                        "sem exposição"
                    }

                    return df.format(value.toDouble()) + "% $identifier"
                }
            })

            barChart.data = barData

            barChart

        }) {

        it.animateY(3000)
    }
}

