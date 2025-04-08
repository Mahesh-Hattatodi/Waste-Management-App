package com.example.wastemanagementapp.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.presentation.UserinfoUiModel
import com.example.wastemanagementapp.ui.theme.WasteManagementAppTheme

@Composable
fun LeaderBoard(
    modifier: Modifier = Modifier,
    user: UserinfoUiModel
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "  ${user.ranking}. ",
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(end = 8.dp)
            )

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFDCEFD9), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = stringResource(R.string.user_image),
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    error = painterResource(id = R.drawable.error_user),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = user.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = user.email,
                    fontStyle = FontStyle.Italic,
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun LeaderBoardPreview() {
    WasteManagementAppTheme {
        LeaderBoard(
            user = UserinfoUiModel(
                displayName = "Mahesh H",
                email = "maheshhattatodi@gmail.com",
                ranking = "1"
            )
        )
    }
}