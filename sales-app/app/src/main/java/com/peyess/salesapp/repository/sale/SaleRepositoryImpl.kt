package com.peyess.salesapp.repository.sale

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
    val authenticationRepository: AuthenticationRepository,
    val activeSalesDao: ActiveSalesDao,
    val activeSODao: ActiveSODao,
): SaleRepository {
    val Context.dataStoreCurrentSale: DataStore<Preferences>
            by preferencesDataStore(currentSaleFileName)

    override fun startSale(): Flow<ActiveSalesEntity> {
        return authenticationRepository.currentUser().map {
            val activeSale = ActiveSalesEntity(
                id = firebaseManager.uniqueId(),
                collaboratorUid = it.id,
                active = true,
            )

            val activeSO = ActiveSOEntity(
                id = firebaseManager.uniqueId(),
                saleId = activeSale.id,
            )

            salesApplication.dataStoreCurrentSale.edit { prefs ->
                prefs[currentSaleKey] = activeSale.id
                prefs[currentSOKey] = activeSO.id
            }

            activeSalesDao.add(activeSale)
            activeSODao.add(activeSO)
            activeSale
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun activeSale(): Flow<ActiveSalesEntity> {
        return salesApplication
            .dataStoreCurrentSale
            .data
            .flatMapLatest { prefs ->
                val saleId = prefs[currentSaleKey]

                if (saleId != null) {
                    activeSalesDao.getById(saleId)
                } else {
                    error("Could not find sale id")
                }
            }
    }

    override fun activeSO(soId: String): Flow<ActiveSOEntity> {
        return salesApplication
            .dataStoreCurrentSale
            .data
            .flatMapLatest { prefs ->
                val saleId = prefs[currentSOKey]

                if (saleId != null) {
                    activeSODao.getById(saleId)
                } else {
                    error("Could not find sale id")
                }
            }
    }

    companion object {
        val currentSaleFileName =
            "com.peyess.salesapp.repository.sale.SaleRepositoryImpl__CurrnetSale"

        val currentSaleKey = stringPreferencesKey("current_sale")

        val currentSOKey = stringPreferencesKey("current_so")
    }
}