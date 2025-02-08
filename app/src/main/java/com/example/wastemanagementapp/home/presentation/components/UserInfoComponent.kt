package com.example.wastemanagementapp.home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.home.domain.models.UserInfo
import com.example.wastemanagementapp.home.presentation.HomeEvent
import com.example.wastemanagementapp.ui.theme.DarkGreen40

@Composable
fun UserInfoComponent(
    userInfo: UserInfo,
    onUserInfoClick: (HomeEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkGreen40
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp)
            .clickable { onUserInfoClick(HomeEvent.OnProfileClick) }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp,end = 12.dp, top = 10.dp, bottom = 10.dp)
        ) {
            AsyncImage(
                model = userInfo.photoUrl,
                contentDescription = stringResource(R.string.user_image),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(100.dp),
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
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "${userInfo.name} | ${userInfo.email}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoComponentPreview() {
    UserInfoComponent(
        userInfo = UserInfo(
            ranking = "07",
            name = "Mahesh",
            email = "maheshhattatodi@gmail.com"
        )
    )
}