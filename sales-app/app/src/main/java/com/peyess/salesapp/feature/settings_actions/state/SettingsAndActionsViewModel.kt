package com.peyess.salesapp.feature.settings_actions.state

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.products_table_state.ProductsTableStateRepository
import com.peyess.salesapp.workmanager.UpdateProductsWorker
import com.peyess.salesapp.workmanager.forceUpdateKey
import com.squareup.okhttp.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class SettingsAndActionViewModel @AssistedInject constructor(
    @Assisted initialState: SettingsAndActionState,
    private val salesApplication: SalesApplication,
    private val productsTableStateRepository: ProductsTableStateRepository,
): MavericksViewModel<SettingsAndActionState>(initialState) {

    init {
        listenToProductTableStatus()


    }

    private fun listenToProductTableStatus() {
        productsTableStateRepository
            .observeState()
            .execute(
                dispatcher = Dispatchers.IO,
                retainValue = SettingsAndActionState::isUpdatingProductsTableAsync,
            ) {
                copy(isUpdatingProductsTableAsync = it)
            }
    }

    private fun createWorker() {
        Timber.i("Creating worker")
        val context = salesApplication as Context

        val dataBuilder = Data.Builder()
        val inputData = dataBuilder
            .putBoolean(forceUpdateKey, true)
            .build()

        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<UpdateProductsWorker>()
                .setInputData(inputData)
                .build()

        WorkManager
            .getInstance(context)
            .enqueue(uploadWorkRequest)
    }

    fun updateProductsTable() {
        createWorker()
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<SettingsAndActionViewModel, SettingsAndActionState> {
        override fun create(state: SettingsAndActionState): SettingsAndActionViewModel
    }

    companion object: MavericksViewModelFactory<SettingsAndActionViewModel, SettingsAndActionState>
    by hiltMavericksViewModelFactory()
}