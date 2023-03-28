package com.peyess.salesapp.data.repository.card_flag

import com.peyess.salesapp.data.dao.card_flag.CardFlagDao
import com.peyess.salesapp.data.model.sale.card_flags.CardFlagDocument
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CardFlagRepositoryImpl @Inject constructor(
    private val cardFlagDao: CardFlagDao,
): CardFlagRepository {
    override fun listCards(): Flow<List<CardFlagDocument>> {
        return cardFlagDao.listCards()
    }
}