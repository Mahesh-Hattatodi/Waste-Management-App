package com.example.wastemanagementapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.Screen
import com.example.wastemanagementapp.core.presentation.BottomNavigationItem
import com.example.wastemanagementapp.ui.theme.Green40
import com.example.wastemanagementapp.ui.theme.Green80
import com.example.wastemanagementapp.ui.theme.WasteManagementAppTheme

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    onClick: (NavigationEvent.Navigate) -> Unit = {}
) {

    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            iconSelected = painterResource(R.drawable.user_icon),
            name = stringResource(R.string.profile),
            route = Screen.ProfileScreen
        ),
        BottomNavigationItem(
            iconSelected = painterResource(R.drawable.support_icon),
            name = stringResource(R.string.support),
            route = Screen.SupportScreen
        ),
    )

    BottomAppBar(
        actions = {
            bottomNavigationItems.forEach { bottomNavigationItem ->
                BottomNavigationItemComponent(
                    bottomNavigationItem = bottomNavigationItem,
                    onClick = { screen ->
                        onClick(NavigationEvent.Navigate(screen))
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        },
        containerColor = Green80,
        contentColor = Color.Black,
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(
                    topEnd = 20.dp,
                    topStart = 20.dp
                )
            )
    )
}

@Composable
fun BottomNavigationItemComponent(
    bottomNavigationItem: BottomNavigationItem,
    modifier: Modifier = Modifier,
    onClick: (Screen) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        IconButton(
            onClick = { onClick(bottomNavigationItem.route) }
        ) {
            Icon(
                painter = bottomNavigationItem.iconSelected,
                contentDescription = bottomNavigationItem.name,
                modifier = Modifier
                    .size(30.dp)
            )
        }

        Text(
            text = bottomNavigationItem.name,
            color = Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Preview
@Composable
private fun AppBottomBarPreview() {
    WasteManagementAppTheme {
        AppBottomBar()
    }
}