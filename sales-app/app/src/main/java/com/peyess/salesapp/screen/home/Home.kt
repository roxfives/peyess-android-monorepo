package com.peyess.salesapp.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Home(modifier: Modifier = Modifier, navHostController: NavHostController) {
    Column {
        Text(text = "Thats home")

        Button(onClick = { Firebase.auth.signOut() }) {
            Text(text = "touch aqui")
        }

        OutlinedTextField(value = "", onValueChange = {})
    }
}