package com.peyess.salesapp.typing.sale

sealed class PurchaseReasonSyncFailure {
    object None: PurchaseReasonSyncFailure()
    object Unknown: PurchaseReasonSyncFailure()
    object EnqueueFailedSettingsDocumentNotFound: PurchaseReasonSyncFailure()
    object EnqueueFailedEnqueueFailed: PurchaseReasonSyncFailure()
    object EnqueueFailedFirestoreError: PurchaseReasonSyncFailure()
    object EnqueueFailedSettingsEntryNotFound: PurchaseReasonSyncFailure()
    object EnqueueFailedNotificationFailed: PurchaseReasonSyncFailure()
    object EnqueueFailedPurchaseDocumentNotFound: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedMalformedMessage: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedFirestoreError: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedPreconditionFailed: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedPurchaseNotFound: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedClientNotFound: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedProductSettingsNotFound: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedPaymentSettingsNotFound: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedSettingNotFoundForPayment: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedStoreNotFound: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedLegalStoreInfoNotFound: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedIncompatiblePrescriptionType: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedMissingConfig: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedPrescriptionNotFound: PurchaseReasonSyncFailure()
    object BuildDwRequestDocumentFailedUserNotFound: PurchaseReasonSyncFailure()
    object PushDwDocumentToSyncQueueFailedFirestoreError: PurchaseReasonSyncFailure()
    object PushDwDocumentToSyncQueueFailedPreconditionFailed: PurchaseReasonSyncFailure()
    object RequestDwDocumentSyncFailedFirestoreError: PurchaseReasonSyncFailure()
    object RequestDwDocumentSyncFailedDwRequestFailed: PurchaseReasonSyncFailure()
    object RequestDwDocumentSyncFailedDwSyncRequestNotFound: PurchaseReasonSyncFailure()
    object RequestDwDocumentSyncFailedMalformedMessage: PurchaseReasonSyncFailure()
    object RequestDwDocumentSyncFailedMissingConfig: PurchaseReasonSyncFailure()
    object RequestDwDocumentSyncFailedStoreLegalInfoNotFound: PurchaseReasonSyncFailure()
    object RequestDwDocumentSyncFailedPreconditionFailed: PurchaseReasonSyncFailure()

    fun toName() = toName(this)

    companion object {
        fun toName(state: PurchaseReasonSyncFailure): String {
            return when (state) {
                is BuildDwRequestDocumentFailedClientNotFound -> "build-dw-request-document-failed-client-not-found"
                is BuildDwRequestDocumentFailedFirestoreError -> "build-dw-request-document-failed-firestore-error"
                is BuildDwRequestDocumentFailedIncompatiblePrescriptionType -> "build-dw-request-document-failed-incompatible-prescription-type"
                is BuildDwRequestDocumentFailedLegalStoreInfoNotFound -> "build-dw-request-document-failed-legal-store-info-not-found"
                is BuildDwRequestDocumentFailedMalformedMessage -> "build-dw-request-document-failed-malformed-message"
                is BuildDwRequestDocumentFailedMissingConfig -> "build-dw-request-document-failed-missing-config"
                is BuildDwRequestDocumentFailedPaymentSettingsNotFound -> "build-dw-request-document-failed-payment-settings-not-found"
                is BuildDwRequestDocumentFailedPreconditionFailed -> "build-dw-request-document-failed-precondition-failed"
                is BuildDwRequestDocumentFailedPrescriptionNotFound -> "build-dw-request-document-failed-prescription-not-found"
                is BuildDwRequestDocumentFailedProductSettingsNotFound -> "build-dw-request-document-failed-product-settings-not-found"
                is BuildDwRequestDocumentFailedPurchaseNotFound -> "build-dw-request-document-failed-purchase-not-found"
                is BuildDwRequestDocumentFailedSettingNotFoundForPayment -> "build-dw-request-document-failed-setting-not-found-for-payment"
                is BuildDwRequestDocumentFailedStoreNotFound -> "build-dw-request-document-failed-store-not-found"
                is BuildDwRequestDocumentFailedUserNotFound -> "build-dw-request-document-failed-user-not-found"
                is EnqueueFailedEnqueueFailed -> "enqueue-failed-enqueue-failed"
                is EnqueueFailedFirestoreError -> "enqueue-failed-firestore-error"
                is EnqueueFailedNotificationFailed -> "enqueue-failed-notification-failed"
                is EnqueueFailedPurchaseDocumentNotFound -> "enqueue-failed-purchase-document-not-found"
                is EnqueueFailedSettingsDocumentNotFound -> "enqueue-failed-settings-document-not-found"
                is EnqueueFailedSettingsEntryNotFound -> "enqueue-failed-settings-entry-not-found"
                is PushDwDocumentToSyncQueueFailedFirestoreError -> "push-dw-document-to-sync-queue-failed-firestore-error"
                is PushDwDocumentToSyncQueueFailedPreconditionFailed -> "push-dw-document-to-sync-queue-failed-precondition-failed"
                is RequestDwDocumentSyncFailedDwRequestFailed -> "request-dw-document-sync-failed-dw-request-failed"
                is RequestDwDocumentSyncFailedDwSyncRequestNotFound -> "request-dw-document-sync-failed-dw-sync-request-not-found"
                is RequestDwDocumentSyncFailedFirestoreError -> "request-dw-document-sync-failed-firestore-error"
                is RequestDwDocumentSyncFailedMalformedMessage -> "request-dw-document-sync-failed-malformed-message"
                is RequestDwDocumentSyncFailedMissingConfig -> "request-dw-document-sync-failed-missing-config"
                is RequestDwDocumentSyncFailedPreconditionFailed -> "request-dw-document-sync-failed-precondition-failed"
                is RequestDwDocumentSyncFailedStoreLegalInfoNotFound -> "request-dw-document-sync-failed-store-legal-info-not-found"
                is None -> "none"
                is Unknown -> "unknown"
            }
        }

        fun fromName(state: String): PurchaseReasonSyncFailure {
            return when (state) {
                "build-dw-request-document-failed-client-not-found" -> BuildDwRequestDocumentFailedClientNotFound
                "build-dw-request-document-failed-firestore-error" -> BuildDwRequestDocumentFailedFirestoreError
                "build-dw-request-document-failed-incompatible-prescription-type" -> BuildDwRequestDocumentFailedIncompatiblePrescriptionType
                "build-dw-request-document-failed-legal-store-info-not-found" -> BuildDwRequestDocumentFailedLegalStoreInfoNotFound
                "build-dw-request-document-failed-malformed-message" -> BuildDwRequestDocumentFailedMalformedMessage
                "build-dw-request-document-failed-missing-config" -> BuildDwRequestDocumentFailedMissingConfig
                "build-dw-request-document-failed-payment-settings-not-found" -> BuildDwRequestDocumentFailedPaymentSettingsNotFound
                "build-dw-request-document-failed-precondition-failed" -> BuildDwRequestDocumentFailedPreconditionFailed
                "build-dw-request-document-failed-prescription-not-found" -> BuildDwRequestDocumentFailedPrescriptionNotFound
                "build-dw-request-document-failed-product-settings-not-found" -> BuildDwRequestDocumentFailedProductSettingsNotFound
                "build-dw-request-document-failed-purchase-not-found" -> BuildDwRequestDocumentFailedPurchaseNotFound
                "build-dw-request-document-failed-setting-not-found-for-payment" -> BuildDwRequestDocumentFailedSettingNotFoundForPayment
                "build-dw-request-document-failed-store-not-found" -> BuildDwRequestDocumentFailedStoreNotFound
                "build-dw-request-document-failed-user-not-found" -> BuildDwRequestDocumentFailedUserNotFound
                "enqueue-failed-enqueue-failed" -> EnqueueFailedEnqueueFailed
                "enqueue-failed-firestore-error" -> EnqueueFailedFirestoreError
                "enqueue-failed-notification-failed" -> EnqueueFailedNotificationFailed
                "enqueue-failed-purchase-document-not-found" -> EnqueueFailedPurchaseDocumentNotFound
                "enqueue-failed-settings-document-not-found" -> EnqueueFailedSettingsDocumentNotFound
                "enqueue-failed-settings-entry-not-found" -> EnqueueFailedSettingsEntryNotFound
                "push-dw-document-to-sync-queue-failed-firestore-error" -> PushDwDocumentToSyncQueueFailedFirestoreError
                "push-dw-document-to-sync-queue-failed-precondition-failed" -> PushDwDocumentToSyncQueueFailedPreconditionFailed
                "request-dw-document-sync-failed-dw-request-failed" -> RequestDwDocumentSyncFailedDwRequestFailed
                "request-dw-document-sync-failed-dw-sync-request-not-found" -> RequestDwDocumentSyncFailedDwSyncRequestNotFound
                "request-dw-document-sync-failed-firestore-error" -> RequestDwDocumentSyncFailedFirestoreError
                "request-dw-document-sync-failed-malformed-message" -> RequestDwDocumentSyncFailedMalformedMessage
                "request-dw-document-sync-failed-missing-config" -> RequestDwDocumentSyncFailedMissingConfig
                "request-dw-document-sync-failed-precondition-failed" -> RequestDwDocumentSyncFailedPreconditionFailed
                "request-dw-document-sync-failed-store-legal-info-not-found" -> RequestDwDocumentSyncFailedStoreLegalInfoNotFound
                "none" -> None
                else -> Unknown
            }
        }
    }
}
