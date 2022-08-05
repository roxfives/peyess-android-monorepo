package com.peyess.salesapp.auth

import com.google.firebase.auth.FirebaseUser

fun storeUserAdapter(user: FirebaseUser): PeyessStore {
    return PeyessStore(
        user.displayName ?: "",
        user.displayName ?: "",
        user.displayName ?: "",
    )
}