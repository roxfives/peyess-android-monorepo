package com.peyess.salesapp.ui.component.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import com.peyess.salesapp.R
import com.peyess.salesapp.ui.component.text.PeyessOutlinedTextField
import com.peyess.salesapp.ui.component.text.PeyessPasswordInput
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CredentialsInput(
    modifier: Modifier = Modifier,

    usernameLabel: @Composable () -> Unit = {},
    usernamePlaceHolder: @Composable () -> Unit = {},

    username: String = stringResource(id = R.string.empty_string),
    hasUsernameError: Boolean = false,
    usernameErrorMessage: String = stringResource(id = R.string.empty_string),
    onUsernameChanged: (password: String) -> Unit = {},

    password: String = stringResource(id = R.string.empty_string),
    hasPasswordError: Boolean = false,
    passwordErrorMessage: String = stringResource(id = R.string.empty_string),
    onPasswordChanged: (password: String) -> Unit = {},

    hasError: Boolean = false,
    errorMessage: String = stringResource(id = R.string.empty_string),

    canSignIn: Boolean = true,
    attemptSignIn: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        PeyessOutlinedTextField(
            value = username,
            onValueChange = { onUsernameChanged(it) },
            isError = hasUsernameError,
            errorMessage = stringResource(id = R.string.empty_string),
            placeholder = usernamePlaceHolder,
            label = usernameLabel,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(focusDirection = FocusDirection.Down)
            })
        )

        PeyessPasswordInput(
            password = password,
            onValueChange = { onPasswordChanged(it) },
            isError = hasPasswordError,
            errorMessage = stringResource(id = R.string.empty_string),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                attemptSignIn()
            })
        )

        Spacer(
            modifier = Modifier
                .height(SalesAppTheme.dimensions.plane_5)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(SalesAppTheme.dimensions.minimum_touch_target),
            enabled = canSignIn,
            onClick = attemptSignIn,
        ) {
            Text(
                text = stringResource(id = R.string.btn_sign_in_store)
                    .capitalize(Locale.current)
            )
        }

        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            if (hasError) {
                SignInErrorMessage(errorMessage = errorMessage)
            }

            if (hasUsernameError) {
                SignInErrorMessage(errorMessage = usernameErrorMessage)
            }

            if (hasPasswordError) {
                SignInErrorMessage(errorMessage = passwordErrorMessage)
            }
        }
    }
}

@Composable
fun SignInErrorMessage(
    modifier: Modifier = Modifier,
    errorMessage: String = ""
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            tint = MaterialTheme.colors.error,
            imageVector = Icons.Filled.Error,
            contentDescription = "",
        )

        Spacer(
            modifier = Modifier
                .width(SalesAppTheme.dimensions.grid_1)
        )

        Text(
            modifier = Modifier
                .padding(vertical = SalesAppTheme.dimensions.grid_3),
            text = errorMessage
                .capitalize(Locale.current),
            color = MaterialTheme.colors.error,
        )
    }
}