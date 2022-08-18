package com.peyess.salesapp.navigation.sale

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.sale.frames.buildFramesNavGraph
import com.peyess.salesapp.navigation.sale.prescription.buildPrescriptionScreenNavGraph
import com.peyess.salesapp.navigation.sale.welcome.buildWelcomeNavGraph

fun buildSaleNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    buildWelcomeNavGraph(modifier, navHostController, builder)
    buildPrescriptionScreenNavGraph(modifier, navHostController, builder)
    buildFramesNavGraph(modifier, navHostController, builder)
}