package com.peyess.salesapp.data.utils.query

sealed class PeyessQueryFunctionOperation {
    object MIN: PeyessQueryFunctionOperation()
    object MAX: PeyessQueryFunctionOperation()
}
