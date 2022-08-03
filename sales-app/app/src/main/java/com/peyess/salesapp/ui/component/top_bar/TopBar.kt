package com.peyess.salesapp.ui.component.top_bar

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.peyess.salesapp.R

@Composable
fun TopBar(modifier: Modifier = Modifier, canNavigateBack: Boolean = false) {
    TopAppBar(
        modifier = modifier,
        title = { TopBarTitle() },
        navigationIcon = { NavigationIcon(canNavigateBack = canNavigateBack) }
    )
}

@Composable
fun NavigationIcon(canNavigateBack: Boolean = true) {
    if (canNavigateBack) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.desc_navigate_back)
        )
    }
}

@Composable
fun TopBarTitle() {
    Text(stringResource(id = R.string.title))
}