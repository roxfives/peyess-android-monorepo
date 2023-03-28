package com.peyess.salesapp.utils.cloud.storage.error

import com.google.firebase.storage.StorageException


const val errorUnknown = StorageException.ERROR_UNKNOWN
const val errorObjectNotFound = StorageException.ERROR_OBJECT_NOT_FOUND
const val errorBucketNotFound = StorageException.ERROR_BUCKET_NOT_FOUND
const val errorProjectNotFound = StorageException.ERROR_PROJECT_NOT_FOUND
const val errorQuotaExceeded = StorageException.ERROR_QUOTA_EXCEEDED
const val errorNotAuthenticated = StorageException.ERROR_NOT_AUTHENTICATED
const val errorNotAuthorized = StorageException.ERROR_NOT_AUTHORIZED
const val errorRetryLimitExceeded = StorageException.ERROR_RETRY_LIMIT_EXCEEDED
const val errorInvalidChecksum = StorageException.ERROR_INVALID_CHECKSUM
const val errorCanceled = StorageException.ERROR_CANCELED