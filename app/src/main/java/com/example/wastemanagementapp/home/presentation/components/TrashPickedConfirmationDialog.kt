package com.example.wastemanagementapp.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.home.presentation.HomeEvent
import com.example.wastemanagementapp.ui.theme.Black10

@Composable
fun TrashPickedConfirmationDialog(
    modifier: Modifier = Modifier,
    onEvent: (HomeEvent) -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(22.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 0.dp,
                spotColor = Black10,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.have_they_collected_the_waste_today),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.trash_not_picked),
                    contentDescription = stringResource(
                        R.string.trash_not_picked
                    ),
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { onEvent(HomeEvent.OnNotPickedClick) }
                )

                Image(
                    painter = painterResource(id = R.drawable.trash_picked),
                    contentDescription = stringResource(R.string.trash_picked),
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { onEvent(HomeEvent.OnPickedClick) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun TrashPickedConfirmationDialogPreview() {
    TrashPickedConfirmationDialog()
}