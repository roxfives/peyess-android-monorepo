package com.peyess.salesapp.ui.component.text

import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.peyess.salesapp.R

@Composable
fun PeyessPasswordInput(
    modifier: Modifier = Modifier,
    password: String,
    label: @Composable () -> Unit = { DefaultLabel() },
    placeholder: @Composable () -> Unit = { DefaultPlaceholder() },
    isError: Boolean = false,
    errorMessage: String = "",
    onValueChange: (update: String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var hidePassword by remember { mutableStateOf(true) }

    PasswordInput(
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        password = password,
        hidePassword = hidePassword,
        onToggleHidePassword = { hidePassword = !hidePassword },
        isError = isError,
        errorMessage = errorMessage,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit = { DefaultLabel() },
    placeholder: @Composable () -> Unit = { DefaultPlaceholder() },
    password: String,
    hidePassword: Boolean = true,
    onToggleHidePassword: () -> Unit = {},
    isError: Boolean = false,
    errorMessage: String = "",
    onValueChange: (update: String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    ) {
    val transformation = if (hidePassword) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    PeyessOutlinedTextField(
        modifier = modifier,
        value = password,
        onValueChange = { onValueChange(it) },
        isError = isError,
        errorMessage = errorMessage,
        label = label,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions.copy(autoCorrect = false),
        keyboardActions = keyboardActions,
        visualTransformation = transformation,
        trailingIcon = {
            PasswordIcon(
                hidePassword = hidePassword,
                onClick = onToggleHidePassword,
            )
        },
    )
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

@Composable
fun DefaultLabel(modifier: Modifier = Modifier) {
    Text(stringResource(R.string.password_input))
}

@Composable
fun DefaultPlaceholder(modifier: Modifier = Modifier) {
    Text(stringResource(R.string.password_input))
}

@Preview
@Composable
fun PasswordInputPreview() {
    MaterialTheme {
        TextField(value = "super_password", onValueChange = {})
    }
}