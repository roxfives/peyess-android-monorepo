package com.peyess.salesapp.repository.payments

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.payment_method.toPaymentMethodDocument
import com.peyess.salesapp.data.dao.payment_method.PaymentMethodDao
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.repository.payments.error.Unexpected
import javax.inject.Inject

class PaymentMethodRepositoryImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val paymentMethodDao: PaymentMethodDao,
): PaymentMethodRepository {

    override suspend fun payments(): PaymentMethodsResponse {
        val priorityField = salesApplication
            .stringResource(R.string.fs_field_payment_method_priority)

        val query = PeyessQuery(
            queryFields = emptyList(),
            orderBy = listOf(
                PeyessOrderBy(
                    field = priorityField,
                    order = Order.ASCENDING,
                ),
            ),
        )

        return paymentMethodDao.fetchCollection(query)
            .map { list ->
                list.map { it.second.toPaymentMethodDocument(it.first) }
            }.mapLeft {
                Unexpected(
                    description = it.description,
                    error = it.error,
                )
            }
    }
}