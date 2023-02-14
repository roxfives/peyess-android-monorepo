package com.peyess.salesapp.navigation.sale

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.edit_service_order.buildEditServiceOrderNavGraph
import com.peyess.salesapp.navigation.edit_service_order.buildEditServiceOrderRoute
import com.peyess.salesapp.navigation.pick_client.buildPickClientNavGraph
import com.peyess.salesapp.navigation.sale.anamnesis.buildAnamnesisNavGraph
import com.peyess.salesapp.navigation.sale.frames.buildFramesNavGraph
import com.peyess.salesapp.navigation.sale.frames_measure.buildFramesMeasureNavGraph
import com.peyess.salesapp.navigation.sale.lens_pick.buildLensSuggestionNavGraph
import com.peyess.salesapp.navigation.sale.payment.buildPaymentNavGraph
import com.peyess.salesapp.navigation.sale.prescription.buildPrescriptionScreenNavGraph
import com.peyess.salesapp.navigation.sale.service_order.buildServiceOrderNavGraph
import com.peyess.salesapp.navigation.sale.welcome.buildWelcomeNavGraph

fun buildSaleNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    buildWelcomeNavGraph(modifier, navHostController, builder)
    buildPrescriptionScreenNavGraph(modifier, navHostController, builder)
    buildFramesNavGraph(modifier, navHostController, builder)
    buildFramesMeasureNavGraph(modifier, navHostController, builder)
    buildLensSuggestionNavGraph(modifier, navHostController, builder)
    buildPickClientNavGraph(modifier, navHostController, builder)
    buildServiceOrderNavGraph(modifier, navHostController, builder)
    buildPaymentNavGraph(modifier, navHostController, builder)
    buildAnamnesisNavGraph(modifier, navHostController, builder)
    buildEditServiceOrderNavGraph(modifier, navHostController, builder)
}
