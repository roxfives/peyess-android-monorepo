package com.peyess.salesapp.data.utils.query

sealed class PeyessQueryOperation {
    object Noop: PeyessQueryOperation()

    object Equal: PeyessQueryOperation()
    object Different: PeyessQueryOperation()

    object GreaterThan: PeyessQueryOperation()
    object GreaterThanOrEqual: PeyessQueryOperation()

    object LessThan: PeyessQueryOperation()
    object LessThanOrEqual: PeyessQueryOperation()

    object Like: PeyessQueryOperation()

    object ArrayContains: PeyessQueryOperation()
}
