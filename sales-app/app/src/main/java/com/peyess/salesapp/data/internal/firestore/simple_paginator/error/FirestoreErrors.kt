package com.peyess.salesapp.data.internal.firestore.simple_paginator.error

sealed interface FirestoreError {
    val description: String
    val error: Throwable?
}

data class NotFound(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class PermissionDenied(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class Unauthenticated(
    override val description: String,
    override val error: Throwable?,
): FirestoreError

data class Aborted(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class Cancelled(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class DataLoss(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class DeadlineExceeded(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class FailedPrecondition(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class Internal(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class InvalidArgument(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class Ok(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class OutOfRange(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class ResourceExhausted(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class Unavailable(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class Unimplemented(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
data class Unknown(
    override val description: String,
    override val error: Throwable?,
): FirestoreError

sealed interface WriteError: FirestoreError
data class AlreadyExists(
    override val description: String,
    override val error: Throwable?): WriteError

data class Unexpected(
    override val description: String,
    override val error: Throwable?,
): FirestoreError
