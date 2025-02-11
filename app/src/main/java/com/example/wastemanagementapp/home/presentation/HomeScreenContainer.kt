package com.example.wastemanagementapp.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.ObserveAsEvents
import com.example.wastemanagementapp.home.domain.models.UserInfo
import com.example.wastemanagementapp.home.presentation.components.FeatureSelectRowComponent
import com.example.wastemanagementapp.home.presentation.components.TrashPickedConfirmationDialog
import com.example.wastemanagementapp.home.presentation.components.UserInfoComponent
import com.example.wastemanagementapp.home.presentation.models.FeatureSelection
import com.example.wastemanagementapp.home.presentation.util.FeatureId

@Composable
fun HomeScreenContainer(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent.Navigate) -> Unit = {}
) {
    val featureSelectionList = listOf(
        FeatureSelection(
            icon = painterResource(id = R.drawable.eco_collect),
            text = stringResource(R.string.feature_eco_collect),
            id = FeatureId.ECO_COLLECT
        ),

        FeatureSelection(
            icon = painterResource(id = R.drawable.feedback),
            text = stringResource(R.string.feedback),
            id = FeatureId.FEEDBACK
        ),
        FeatureSelection(
            icon = painterResource(id = R.drawable.complaint),
            text = stringResource(R.string.complaint),
            id = FeatureId.COMPLAINT
        )
    )

    val demoUserInfo = UserInfo(
        ranking = "07",
        name = "Mahesh",
        email = "maheshhattatodi@gmail.com",
        photoUrl = "https://plus.unsplash.com/premium_photo-1683121366070-5ceb7e007a97?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D"
    )

    ObserveAsEvents(flow = viewModel.navigationEvent) { event ->
        when (event) {
            is NavigationEvent.Navigate -> {
                onNavigate(event)
            }

            NavigationEvent.PopBackStack -> Unit
        }
    }

    HomeScreen(
        featureSelectionList = featureSelectionList,
        onEvent = viewModel::onEvent,
        userInfo = demoUserInfo
    )
}

@Composable
fun HomeScreen(
    userInfo: UserInfo = UserInfo(),
    onEvent: (HomeEvent) -> Unit = {},
    featureSelectionList: List<FeatureSelection> = emptyList(),
    isTruckArriving: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserInfoComponent(
            modifier = Modifier
                .fillMaxHeight(0.25f),
            userInfo = userInfo
        )

        Image(
            painter = painterResource(id = R.drawable.hero_image),
            contentDescription = stringResource(
                R.string.hero_image
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            contentScale = ContentScale.FillBounds
        )

        FeatureSelectRowComponent(
            featureSelectionList = featureSelectionList,
            onEvent = onEvent
        )

        if (isTruckArriving) {
            TrashPickedConfirmationDialog(
                modifier = Modifier
                .fillMaxHeight(0.7f),
                onEvent = onEvent
            )
        } else {
            Text(
                text = stringResource(R.string.trash_pickup_truck_is_not_scheduled_for_today),
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    val demoUserInfo = UserInfo(
        ranking = "07",
        name = "Mahesh",
        email = "maheshhattatodi@gmail.com",
        photoUrl = "https://plus.unsplash.com/premium_photo-1683121366070-5ceb7e007a97?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D"
    )

    val featureSelectionList = listOf(
        FeatureSelection(
            icon = painterResource(id = R.drawable.eco_collect),
            text = stringResource(R.string.feature_eco_collect),
            id = FeatureId.ECO_COLLECT
        ),

        FeatureSelection(
            icon = painterResource(id = R.drawable.feedback),
            text = stringResource(R.string.feedback),
            id = FeatureId.FEEDBACK
        ),
        FeatureSelection(
            icon = painterResource(id = R.drawable.complaint),
            text = stringResource(R.string.complaint),
            id = FeatureId.COMPLAINT
        )
    )

    HomeScreen(
        userInfo = demoUserInfo,
        featureSelectionList = featureSelectionList
    )
}