package com.peyess.salesapp.ui.component.mike

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peyess.salesapp.R
import com.peyess.salesapp.ui.theme.SalesAppTheme

@Composable
fun MikeBubbleRight(
    modifier: Modifier = Modifier,
    text: String = "",
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min).fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(3f)) {
            ChatBubble(text = text)
        }

        Spacer(modifier = Modifier.width(SalesAppTheme.dimensions.grid_0_25))

        Mike(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun Mike(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_mike_upper_body),
        contentDescription = "",
    )
}

@Composable
private fun ChatBubble(
    modifier: Modifier = Modifier,
    text: String = "",
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Column(modifier = Modifier
            .background(color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(4.dp, 4.dp, 0.dp, 4.dp))
            .fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = text,
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h6,
            )
        }

//        Column(
//            modifier = Modifier
//                .background(
//                    color = MaterialTheme.colors.primary,
//                    shape = TriangleEdgeShape(10)
//                )
//                .width(8.dp)
//                .fillMaxHeight()) {}
    }
}

@Preview
@Composable
private fun MikeBubbleRightPreview() {
    SalesAppTheme {
        MikeBubbleRight()
    }
}