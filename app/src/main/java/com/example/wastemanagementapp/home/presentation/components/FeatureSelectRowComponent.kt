package com.example.wastemanagementapp.home.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.home.presentation.HomeEvent
import com.example.wastemanagementapp.home.presentation.models.FeatureSelection
import com.example.wastemanagementapp.home.presentation.util.FeatureId

@Composable
fun FeatureSelectRowComponent(
    modifier: Modifier = Modifier,
    onEvent: (HomeEvent) -> Unit = {},
    featureSelectionList: List<FeatureSelection> = emptyList(),
    onTrackClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        featureSelectionList.forEach { feature ->
            FeatureComponent(
                feature = feature,
                onClick = { id ->
                    when (id) {
                        FeatureId.ECO_COLLECT -> {
                            onEvent(HomeEvent.OnEcoCollectClick)
                            Log.i("feature", "FeatureSelectRowComponent: $id")
                        }
                        FeatureId.TRACKING -> {
                            onEvent(HomeEvent.OnTrackClick)
//                            onTrackClick()
                            Log.i("feature", "FeatureSelectRowComponent: $id")
                        }
                        FeatureId.SCHEDULE -> {
                            onEvent(HomeEvent.OnScheduleClick)
                            Log.i("feature", "FeatureSelectRowComponent: $id")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FeatureComponent(
    feature: FeatureSelection,
    modifier: Modifier = Modifier,
    onClick: (FeatureId) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onClick(feature.id) }
    ) {
        Image(
            painter = feature.icon,
            contentDescription = feature.text,
            modifier = Modifier
                .clip(CircleShape)
                .size(55.dp)
        )

        Text(
            text = feature.text,
            color = Color.Black,
            fontSize = 8.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeaturePreview() {
    val featureSelectionList = listOf(
        FeatureSelection(
            icon = painterResource(id = R.drawable.eco_collect),
            text = stringResource(R.string.feature_eco_collect),
            id = FeatureId.ECO_COLLECT
        ),

        FeatureSelection(
            icon = painterResource(id = R.drawable.feedback),
            text = stringResource(R.string.feedback),
            id = FeatureId.TRACKING
        ),
        FeatureSelection(
            icon = painterResource(id = R.drawable.complaint),
            text = stringResource(R.string.complaint),
            id = FeatureId.SCHEDULE
        )
    )
    FeatureSelectRowComponent(featureSelectionList = featureSelectionList)
}