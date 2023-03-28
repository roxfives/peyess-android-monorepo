package com.peyess.salesapp.screen.settings_actions.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.products_table_state.ProductsTableStatus

data class SettingsAndActionState(
    val isUpdatingProductsTableAsync: Async<ProductsTableStatus?> = Uninitialized,
): MavericksState {
    val isUpdatingProductsTable = isUpdatingProductsTableAsync is Success
            && isUpdatingProductsTableAsync.invoke()?.isUpdating ?: false
}
