package com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import timber.log.Timber

@Composable
fun GraphsScreen(
    modifier: Modifier = Modifier,

    onNext: () -> Unit = {},
) {
    GraphsScreenImpl()
}

@Composable
private fun GraphsScreenImpl(
    modifier: Modifier = Modifier,

//    totalFar:
) {
    PieChart(modifier = Modifier.fillMaxWidth())
}



@Composable
fun PieChart(
    modifier: Modifier = Modifier,
) {
    AndroidView(modifier = modifier,
        factory = { context ->
            val lineChart = LineChart(context)
            val entries = mutableListOf(Entry(1f,1f))

            val dataSet = LineDataSet(entries, "Label").apply { color = Color.Red.toArgb() }

            val lineData = LineData(dataSet)
            lineChart.data = lineData
            lineChart.invalidate()

            lineChart
        }){

        try {
            it.data.dataSets[0].addEntry(Entry(2f, 2f))
            it.lineData.notifyDataChanged()
            it.notifyDataSetChanged()
            it.invalidate()
        } catch(ex: Exception) {
            Timber.d( "MyLineChart: $ex")
        }
    }
}

