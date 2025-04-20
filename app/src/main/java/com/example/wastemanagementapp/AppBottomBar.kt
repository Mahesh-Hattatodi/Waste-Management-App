package com.example.wastemanagementapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wastemanagementapp.core.util.Screen
import com.example.wastemanagementapp.core.presentation.BottomNavigationItem
import com.example.wastemanagementapp.ui.theme.Green40
import com.example.wastemanagementapp.ui.theme.LightGreenColor
import com.example.wastemanagementapp.ui.theme.WasteManagementAppTheme

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    selectedIndex: Int = 1,
    onScreenSelected: (Int, Screen) -> Unit = { index, screen ->}
) {
    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            iconSelected = painterResource(R.drawable.user_icon),
            name = stringResource(R.string.profile),
            route = Screen.ProfileScreen
        ),
        BottomNavigationItem(
            iconSelected = painterResource(R.drawable.home),
            name = stringResource(R.string.home),
            route = Screen.HomeScreen
        ),
        BottomNavigationItem(
            iconSelected = painterResource(R.drawable.support_icon),
            name = stringResource(R.string.support),
            route = Screen.SupportScreen
        ),
    )

    Box(
        modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightGreenColor, Color.White)
                )
            )
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            bottomNavigationItems.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                val animatedWeight by animateFloatAsState(
                    targetValue = if (isSelected) 1.5f else 1f
                )

                Box(
                    modifier = Modifier.weight(animatedWeight),
                    contentAlignment = Alignment.Center,
                ) {
                    val interactionSource = remember { MutableInteractionSource() }

                    BottomNavigationItemComponent(
                        bottomNavigationItem = item,
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                onScreenSelected(index, item.route)
                            },
                        isSelected = isSelected
                    )
                }
            }
        }
    }
}


@Composable
fun BottomNavigationItemComponent(
    bottomNavigationItem: BottomNavigationItem,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
) {
    val animatedElevation by animateDpAsState(targetValue = if (isSelected) 15.dp else 0.dp)
    val animatedAlpha by animateFloatAsState(targetValue = if (isSelected) 1f else .5f)
    val animatedIconSize by animateDpAsState(
        targetValue = if (isSelected) 28.dp else 24.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) Green40 else Color.Transparent
    )
    val animationRotation by animateFloatAsState(
        targetValue = if (isSelected) 180f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )

    Box(
        modifier = modifier
            .size(60.dp) // Make this a square for perfect circle
            .shadow(animatedElevation, shape = CircleShape, clip = false)
            .background(animatedColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer { rotationY = animationRotation },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = bottomNavigationItem.iconSelected,
                    contentDescription = bottomNavigationItem.name,
                    modifier = Modifier
                        .size(animatedIconSize)
                        .alpha(animatedAlpha),
                    tint = Color.Black
                )
            }

            AnimatedVisibility(visible = isSelected) {
                Text(
                    text = bottomNavigationItem.name,
                    color = Color.Black,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 2.dp, start = 2.dp, end = 2.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun AppBottomBarPreview() {
    WasteManagementAppTheme {
        AppBottomBar()
    }
}