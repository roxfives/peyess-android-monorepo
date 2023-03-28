package com.peyess.salesapp.data.dao.card_flag

import com.peyess.salesapp.data.model.sale.card_flags.CardFlagDocument
import kotlinx.coroutines.flow.Flow

interface CardFlagDao {
    fun listCards(): Flow<List<CardFlagDocument>>
}