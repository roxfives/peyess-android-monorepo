package com.peyess.salesapp.ui.component.text

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.peyess.salesapp.R

@Composable
fun PeyessPasswordInput(
    modifier: Modifier = Modifier,
    password: String,
    isError: Boolean = false,
    errorMessage: String = "",
    onValueChange: (update: String) -> Unit,
) {
    var hidePassword by rememberSaveable { mutableStateOf(true) }

    PeyessOutlinedTextField(
        modifier = modifier,
        value = password,
        onValueChange = { onValueChange(it) },
        isError = isError,
        errorMessage = errorMessage,
        label = { Text(stringResource(R.string.password_input)) },
        placeholder = { Text(stringResource(R.string.password_input)) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            PasswordIcon(
                hidePassword = hidePassword,
                onClick = { hidePassword = !hidePassword },
            )
        },
    )

//    TextField(
//        modifier = modifier,
//        value = password,
//        onValueChange = { onValueChange(it) },
//        label = { Text(stringResource(R.string.password_input)) },
//        visualTransformation = PasswordVisualTransformation(),
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//        trailingIcon = {
//            PasswordIcon(
//                hidePassword = hidePassword,
//                onClick = { hidePassword = !hidePassword },
//            )
//        }
//    )
}

@Composable
fun PasswordIcon(
    hidePassword: Boolean = true,
    onClick: () -> Unit = {},
) {
    if (hidePassword) {
        IconButton(onClick = onClick) {
            Icon(Icons.Filled.Visibility, stringResource(id = R.string.show_password))
        }
    } else {
        IconButton(onClick = onClick) {
            Icon(Icons.Filled.VisibilityOff, stringResource(id = R.string.hide_password))
        }
    }
}

@Preview
@Composable
fun PasswordInputPreview() {
    MaterialTheme {
        TextField(value = "super_password", onValueChange = {})
    }
}