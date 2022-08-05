package com.peyess.salesapp.auth

import com.google.firebase.auth.FirebaseUser

fun salesPersonUserAdapter(user: FirebaseUser): PeyessUser {
    return PeyessUser(
        user.displayName ?: "",
        user.displayName ?: "",
        user.displayName ?: "",
    )
}