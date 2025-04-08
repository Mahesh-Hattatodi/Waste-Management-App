package com.example.wastemanagementapp.home.presentation.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.home.presentation.HomeEvent
import com.example.wastemanagementapp.core.presentation.UserinfoUiModel
import com.example.wastemanagementapp.ui.theme.DarkGreen40

@Composable
fun UserInfoComponent(
    modifier: Modifier = Modifier,
    userInfo: UserinfoUiModel,
    onUserInfoClick: (HomeEvent) -> Unit = {},
    context: Context = LocalContext.current
) {

    Log.d("userInfo", "Image url: ${userInfo.photoUrl}")

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .logger(DebugLogger())
            .build()
    }

    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkGreen40
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 24.dp)
            .clickable { onUserInfoClick(HomeEvent.OnProfileClick) }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(userInfo.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.user_image),
                imageLoader = imageLoader,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp),
                error = painterResource(id = R.drawable.error_user)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 18.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.ranking, userInfo.ranking),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "${userInfo.displayName} | ${userInfo.email}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoComponentPreview() {
    UserInfoComponent(
        userInfo = UserinfoUiModel(
            ranking = "07",
            displayName = "Mahesh",
            email = "maheshhattatodi@gmail.com"
        )
    )
}