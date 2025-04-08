package com.example.wastemanagementapp.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.presentation.UserinfoUiModel
import com.example.wastemanagementapp.profile.presentation.components.LeaderBoard
import com.example.wastemanagementapp.profile.presentation.components.UserContact
import com.example.wastemanagementapp.ui.theme.WasteManagementAppTheme

val userContactModifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .height(30.dp)
        .width(120.dp)

val allUserRankingModifier = Modifier
    .fillMaxWidth()
    .padding(bottom = 8.dp)

@Composable
fun ProfileScreenContainer(viewModel: ProfileViewModel = hiltViewModel()) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val users by viewModel.allUsers.collectAsStateWithLifecycle()

    ProfileScreen(
        currentUser = currentUser,
        allUsers = users
    )
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    currentUser: UserinfoUiModel = UserinfoUiModel(),
    allUsers: List<UserinfoUiModel> = emptyList()
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp)
    ) {
        AsyncImage(
            model = currentUser.photoUrl,
            contentDescription = stringResource(R.string.user_image),
            modifier = Modifier
                .padding(top = 40.dp, bottom = 12.dp)
                .size(120.dp)
                .clip(CircleShape),
            error = painterResource(id = R.drawable.error_user),
            contentScale = ContentScale.FillBounds
        )

        Text(
            text = stringResource(R.string.ranking, currentUser.ranking),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 32.sp
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            UserContact(
                modifier = userContactModifier
                    .background(MaterialTheme.colorScheme.inversePrimary),
                contact = currentUser.displayName
            )

            UserContact(
                modifier = userContactModifier
                    .background(MaterialTheme.colorScheme.inversePrimary),
                contact = currentUser.email
            )
        }

        Text(
            text = stringResource(R.string.leaderboard),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 26.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        LazyColumn {
            items(
                allUsers,
                key = { it.email }
            ) { user ->
                LeaderBoard(
                    modifier = allUserRankingModifier,
                    user = user
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    WasteManagementAppTheme {
        val allUsers = listOf(
            UserinfoUiModel(
                displayName = "Mahesh H",
                email = "maheshhattatodi@gmail.com",
                ranking = "1"
            ),
            UserinfoUiModel(
                displayName = "Bharath",
                email = "bharath@gmail.com",
                ranking = "2"
            ),
            UserinfoUiModel(
                displayName = "Shreyas",
                email = "shreyas@gmail.com",
                ranking = "3"
            ),
            UserinfoUiModel(
                displayName = "Shivraj",
                email = "shivraj@gmail.com",
                ranking = "4"
            )
        )
        ProfileScreen(
            currentUser = UserinfoUiModel(
                displayName = "Mahesh H",
                email = "maheshhattatodi@gmail.com",
                ranking = "1"
            ),
            allUsers = allUsers
        )
    }
}