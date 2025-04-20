package com.example.wastemanagementapp.home.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.ObserveAsEvents
import com.example.wastemanagementapp.home.presentation.components.FeatureSelectRowComponent
import com.example.wastemanagementapp.home.presentation.components.TrashPickedConfirmationDialog
import com.example.wastemanagementapp.home.presentation.components.UserInfoComponent
import com.example.wastemanagementapp.home.presentation.models.FeatureSelection
import com.example.wastemanagementapp.core.presentation.UserinfoUiModel
import com.example.wastemanagementapp.home.presentation.util.FeatureId
import kotlinx.coroutines.delay
import androidx.core.net.toUri
import com.example.wastemanagementapp.ui.theme.LightGreenColor
import com.example.wastemanagementapp.ui.theme.WasteManagementAppTheme

@Composable
fun HomeScreenContainer(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent.Navigate) -> Unit = {}
) {

    val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()

    val featureSelectionList = listOf(
        FeatureSelection(
            icon = painterResource(id = R.drawable.eco_collect),
            text = stringResource(R.string.feature_eco_collect),
            id = FeatureId.ECO_COLLECT
        ),

        FeatureSelection(
            icon = painterResource(id = R.drawable.tracking_feature_icon),
            text = stringResource(R.string.tracking),
            id = FeatureId.TRACKING
        ),
        FeatureSelection(
            icon = painterResource(id = R.drawable.schedule_feature_icon),
            text = stringResource(R.string.schedule),
            id = FeatureId.SCHEDULE
        )
    )

    ObserveAsEvents(flow = viewModel.navigationEvent) { event ->
        when (event) {
            is NavigationEvent.Navigate -> {
                onNavigate(event)
            }

            NavigationEvent.PopBackStack -> Unit
        }
    }

    Log.d("userInfo", "HomeContainer: $userInfo")

    HomeScreen(
        featureSelectionList = featureSelectionList,
        onEvent = viewModel::onEvent,
        userInfo = userInfo
    )
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(
    userInfo: UserinfoUiModel,
    onEvent: (HomeEvent) -> Unit = {},
    featureSelectionList: List<FeatureSelection> = emptyList(),
    isTruckArriving: Boolean = true,
    context: Context = LocalContext.current
) {
    var isRight by remember { mutableStateOf(false) }

    Log.d("userInfo", "HomeScreen: $userInfo")

    if (isRight) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            GifImage(LocalContext.current)
        }

        LaunchedEffect(Unit) {
            delay(3380L)
            isRight = false
        }
    } else {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val widthPx = constraints.maxWidth.toFloat()
            val heightPx = constraints.maxHeight.toFloat()

            val endX = widthPx * 0.75f
            val endY = heightPx * 0.5f

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(LightGreenColor, Color.White),
                            start = Offset(x = widthPx, y = 0f), // top-right
                            end = Offset(x = endX, y = endY)     // 1/4th diagonal
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserInfoComponent(
                        modifier = Modifier
                            .fillMaxHeight(0.2f),
                        userInfo = userInfo
                    )

                    Image(
                        painter = painterResource(id = R.drawable.hero_image),
                        contentDescription = stringResource(
                            R.string.hero_image
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.3f)
                            .padding(start = 20.dp, end = 20.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    FeatureSelectRowComponent(
                        featureSelectionList = featureSelectionList,
                        onEvent = onEvent,
                        onTrackClick = {
                            val intent = Intent(Intent.ACTION_VIEW,
                                "http://13.232.13.233/jsp/VehicleLiveTracking.jsp?username=MCC&password=Mcc@123&vehicle_no=KA19AE2075".toUri())
                            intent.setPackage("com.android.chrome") // Open in Chrome
                            context.startActivity(intent)
                        }
                    )

                    if (isTruckArriving) {
                        TrashPickedConfirmationDialog(
                            modifier = Modifier
                                .fillMaxHeight(0.7f),
                            onEvent = {
                                onEvent(it)
                                when (it) {
                                    HomeEvent.OnNotPickedClick -> {}
                                    HomeEvent.OnPickedClick -> {
                                        isRight = true
                                    }
                                    else -> Unit
                                }
                            }
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.trash_pickup_truck_is_not_scheduled_for_today),
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GifImage(context: Context) {
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(AnimatedImageDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    AsyncImage(
        model = R.raw.right,
        contentDescription = stringResource(R.string.picked_gif),
        modifier = Modifier.size(450.dp),
        imageLoader = imageLoader
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {

    val featureSelectionList = listOf(
        FeatureSelection(
            icon = painterResource(id = R.drawable.eco_collect),
            text = stringResource(R.string.feature_eco_collect),
            id = FeatureId.ECO_COLLECT
        ),

        FeatureSelection(
            icon = painterResource(id = R.drawable.tracking_feature_icon),
            text = stringResource(R.string.tracking),
            id = FeatureId.TRACKING
        ),
        FeatureSelection(
            icon = painterResource(id = R.drawable.schedule_feature_icon),
            text = stringResource(R.string.schedule),
            id = FeatureId.SCHEDULE
        )
    )

    WasteManagementAppTheme {
        HomeScreen(
            featureSelectionList = featureSelectionList,
            userInfo = UserinfoUiModel()
        )
    }
}