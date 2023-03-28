package com.peyess.salesapp.ui.component.action_bar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peyess.salesapp.R
import com.peyess.salesapp.ui.component.text.PeyessSimpleTextField
import com.peyess.salesapp.ui.theme.SalesAppTheme

private val searchBarBaseHeight = 12.dp

private val searchBarInactiveHeight = 48.dp
private val searchBarActiveHeight = 72.dp

private val searchIconInactiveSize = 24.dp
private val searchIconActiveSize = 48.dp

private val clientSearchSpacer = 16.dp
private val clientSearchRoundedCornerShape = 32.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ClientActions(
    modifier: Modifier = Modifier,

    onCreateNewClient: () -> Unit = {},

    clientSearchQuery: String = "",

    isSearchActive: Boolean = false,
    onSearchClient: (search: String) -> Unit = {},
    onStartSearching: () -> Unit = {},
    onStopSearching: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchFocusRequester = remember { FocusRequester() }

    val createClientBackgroundColor = MaterialTheme.colors.primary
    val cancelSearchBackgroundColor = Color.Gray.copy(alpha = 0.3f)
    val backgroundColor by animateColorAsState(
        targetValue = if (isSearchActive) {
            cancelSearchBackgroundColor
        } else {
            createClientBackgroundColor
        },
    )

    val createClientIconColor = MaterialTheme.colors.onPrimary
    val cancelSearchIconColor = Color.DarkGray
    val iconColor by animateColorAsState(
        targetValue = if (isSearchActive) {
            cancelSearchIconColor
        } else {
            createClientIconColor
        },
    )

    val searchBarHeight by animateDpAsState(
        targetValue = if (isSearchActive) {
            searchBarActiveHeight
        } else {
            searchBarInactiveHeight
        },
    )

    val searchIconSize by animateDpAsState(
        targetValue = if (isSearchActive) {
            searchIconActiveSize
        } else {
            searchIconInactiveSize
        },
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(clientSearchRoundedCornerShape),
            onClick = {
                onStartSearching()
                keyboardController?.show()
            },
        ) {
            Row(
                modifier = Modifier.height(searchBarHeight),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(searchIconSize),
                    imageVector = Icons.Filled.Search,
                    contentDescription = "",
                )

                Spacer(modifier = Modifier.width(clientSearchSpacer))

                if (isSearchActive) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        PeyessSimpleTextField(
                            modifier = Modifier
                                .background(color = Color.Transparent)
                                .focusRequester(searchFocusRequester),
                            value = clientSearchQuery,
                            onValueChange = onSearchClient,
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = stringResource(
                                        id = R.string.btn_client_action_search_client
                                    )
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Search,
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = { keyboardController?.hide() }
                            ),
                        )

                        Spacer(modifier = Modifier.height(searchBarBaseHeight))

                        LaunchedEffect(Unit) {
                            searchFocusRequester.requestFocus()
                        }
                    }
                } else {
                    Text(text = stringResource(id = R.string.btn_client_action_search_client))
                }
            }
        }

        Spacer(modifier = Modifier.width(clientSearchSpacer))

        IconButton(
            modifier = Modifier
                .background(
                    shape = CircleShape,
                    color = backgroundColor,
                )
                .size(SalesAppTheme.dimensions.minimum_touch_target),
            onClick = {
                if (isSearchActive) {
                    onStopSearching()
                } else {
                    onCreateNewClient()
                }
            },
        ) {
            Icon(
                imageVector = if (isSearchActive) {
                    Icons.Filled.Close
                } else {
                    Icons.Filled.PersonAdd
                },
                tint = iconColor,
                contentDescription = "",
            )
        }
    }
}

@Preview
@Composable
fun ClientActionsPreview(
    modifier: Modifier = Modifier,
) {
    SalesAppTheme {
        ClientActions()
    }
}