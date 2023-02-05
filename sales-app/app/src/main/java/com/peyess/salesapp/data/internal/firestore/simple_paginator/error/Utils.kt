package com.peyess.salesapp.data.internal.firestore.simple_paginator.error

import com.google.firebase.firestore.FirebaseFirestoreException

fun FirebaseFirestoreException.toFirestoreError(): FirestoreError {
    val detailMessage = this.message ?: "<Empty error message>"

    return when (code) {
        FirebaseFirestoreException.Code.OK ->
            Ok(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.CANCELLED ->
            Cancelled(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.UNKNOWN ->
            Unknown(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.INVALID_ARGUMENT ->
            InvalidArgument(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.DEADLINE_EXCEEDED ->
            DeadlineExceeded(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.NOT_FOUND ->
            NotFound(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.ALREADY_EXISTS ->
            AlreadyExists(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.PERMISSION_DENIED ->
            PermissionDenied(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED ->
            ResourceExhausted(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.FAILED_PRECONDITION ->
            FailedPrecondition(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.ABORTED ->
            Aborted(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.OUT_OF_RANGE ->
            OutOfRange(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.UNIMPLEMENTED ->
            Unimplemented(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.INTERNAL ->
            Internal(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.UNAVAILABLE ->
            Unavailable(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.DATA_LOSS ->
            DataLoss(description = detailMessage, error = cause)
        FirebaseFirestoreException.Code.UNAUTHENTICATED ->
            Unauthenticated(description = detailMessage, error = cause)
    }
}