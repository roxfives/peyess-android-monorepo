package com.peyess.salesapp.data.repository.card_flag

import com.peyess.salesapp.data.model.sale.card_flags.CardFlagDocument
import kotlinx.coroutines.flow.Flow

interface CardFlagRepository {
    fun listCards(): Flow<List<CardFlagDocument>>
}