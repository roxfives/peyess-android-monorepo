package com.peyess.salesapp.screen.settings_actions.state

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingWorkPolicy
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.products_table_state.ProductsTableStateRepository
import com.peyess.salesapp.workmanager.products.cancelAnyProductUpdateWorker
import com.peyess.salesapp.workmanager.products.enqueueProductUpdateWorker
import com.peyess.salesapp.workmanager.products.notificationId
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsAndActionViewModel @AssistedInject constructor(
    @Assisted initialState: SettingsAndActionState,
    private val salesApplication: SalesApplication,
    private val productsTableStateRepository: ProductsTableStateRepository,
): MavericksViewModel<SettingsAndActionState>(initialState) {

    init {
        streamProductTableStatus()
    }

    private fun streamProductTableStatus() {
        productsTableStateRepository.observeState().execute(
            dispatcher = Dispatchers.IO,
            retainValue = SettingsAndActionState::isUpdatingProductsTableAsync,
        ) {
            copy(isUpdatingProductsTableAsync = it)
        }
    }

    private suspend fun createWorker() {
        Timber.i("Creating worker")

        enqueueProductUpdateWorker(
            context = salesApplication as Context,
            workPolicy = ExistingWorkPolicy.REPLACE,
            forceExecution = true,
        )
    }

    private fun removeUpdateNotification() {
        with(NotificationManagerCompat.from(salesApplication as Context)) {
            cancel(notificationId)
        }
    }

    fun updateProductsTable() {
        viewModelScope.launch { createWorker() }
    }

    fun cancelProductsUpdate() {
        cancelAnyProductUpdateWorker(salesApplication as Context)
        viewModelScope.launch(Dispatchers.IO) {
            productsTableStateRepository.cancelUpdate()
            removeUpdateNotification()
        }
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<SettingsAndActionViewModel, SettingsAndActionState> {
        override fun create(state: SettingsAndActionState): SettingsAndActionViewModel
    }

    companion object: MavericksViewModelFactory<SettingsAndActionViewModel, SettingsAndActionState>
    by hiltMavericksViewModelFactory()
}