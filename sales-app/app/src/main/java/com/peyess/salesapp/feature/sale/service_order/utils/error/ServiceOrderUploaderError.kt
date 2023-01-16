package com.peyess.salesapp.feature.sale.service_order.utils.error

sealed interface ServiceOrderUploaderError {
    val description: String
    val error: Throwable?
}

sealed interface AddClientError: ServiceOrderUploaderError
data class UserNotFound(
    override val description: String,
    override val error: Throwable? = null,
): AddClientError
data class ResponsibleNotFound(
    override val description: String,
    override val error: Throwable? = null,
): AddClientError
data class WitnessNotFound(
    override val description: String,
    override val error: Throwable? = null,
): AddClientError

sealed interface AddProductError: ServiceOrderUploaderError
data class LensNotFound(
    override val description: String,
    override val error: Throwable? = null,
): AddProductError
data class TreatmentNotFound(
    override val description: String,
    override val error: Throwable? = null,
): AddProductError
data class ColoringNotFound(
    override val description: String,
    override val error: Throwable? = null,
): AddProductError
data class FramesNotFound(
    override val description: String,
    override val error: Throwable? = null,
): AddProductError

data class ServiceOrderUnexpected(
    override val description: String,
    override val error: Throwable? = null,
): ServiceOrderUploaderError, AddClientError, AddProductError