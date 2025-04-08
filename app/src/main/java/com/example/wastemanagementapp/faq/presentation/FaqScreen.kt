package com.example.wastemanagementapp.faq.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wastemanagementapp.R

@Composable
fun FAQContainer() {
    FAQScreen()
}

@Composable
fun FAQScreen() {
    val faqModifier = remember {
        Modifier
            .fillMaxWidth()
            .padding(6.dp)
    }

    val themeColor = Color(0xFF386641)
    val faqs = listOf(
        "How does waste collection work?" to "Waste is collected based on your locality's schedule. Check the app under 'Collection Schedule.'",
        "What items can be recycled?" to "Common recyclables include paper, plastic, glass, and metal. Check our guidelines for details.",
        "How do I report missed pickups?" to "If your waste was not collected, go to 'Report Issue' and submit a complaint.",
        "Are there any fees for bulk waste disposal?" to "Yes, bulk waste disposal may have additional fees. Please check our pricing section for details.",
        "How can I reduce my household waste?" to "Reduce waste by composting organic waste, using reusable bags, and recycling responsibly.",
        "Can I schedule special waste pickup?" to "Yes, you can schedule pickups for hazardous waste, electronics, and large appliances via the app.",
        "How do I find my nearest recycling center?" to "Use the 'Locate Center' feature in the app to find the nearest recycling facility.",
        "What should I do with electronic waste?" to "Electronic waste should be taken to certified e-waste recycling centers listed in our app.",
        "Is composting available in my area?" to "Check the 'Composting Services' section in the app to see if your locality offers compost collection.",
        "How do I track my waste management impact?" to "Our dashboard provides insights on how much waste you've recycled and reduced over time."
    )

    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.frequently_asked_questions),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = themeColor,
                modifier = Modifier
                    .padding(top = 6.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        itemsIndexed(faqs) { index, faq ->
            FAQItem(
                modifier = faqModifier,
                question = faq.first,
                answer = faq.second,
                isExpanded = expandedIndex == index,
                onClick = { expandedIndex = if (expandedIndex == index) null else index },
                themeColor = themeColor
            )
        }
    }
}

@Composable
fun FAQItem(
    modifier: Modifier,
    question: String,
    answer: String,
    isExpanded: Boolean,
    onClick: () -> Unit,
    themeColor: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = themeColor,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand/Collapse",
                    tint = themeColor
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Text(
                    text = answer,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}


//@Composable
//fun FAQScreen(
//    modifier: Modifier = Modifier
//) {
//    LazyColumn (
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        item {
//            Spacer(modifier = Modifier.height(50.dp))
//
//            Text(
//                text = stringResource(R.string.faq),
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black
//            )
//
//            Image(
//                painter = painterResource(id = R.drawable.truck_delivery_service),
//                contentDescription = stringResource(R.string.faq_image),
//                modifier = Modifier
//                    .size(180.dp)
//                    .padding(vertical = 10.dp)
//            )
//
//            Text(
//                text = stringResource(R.string.how_can_we_help),
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black
//            )
//            Spacer(modifier = Modifier.height(20.dp))
//            Text(
//                text = stringResource(R.string.welcome_to_our_app_support_ask_anything_our_support_community_can_help_you_find_answers_to_all_your_queries),
//                fontSize = 14.sp,
//                fontStyle = FontStyle.Italic,
//                textAlign = TextAlign.Center,
//                color = Color.DarkGray
//            )
//
//            Spacer(modifier = Modifier.height(40.dp))
//
//            // FAQ Buttons
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                FAQButton(title = stringResource(R.string.app), icon = R.drawable.baseline_app_settings)
//                FAQButton(title = stringResource(R.string.general), icon = R.drawable.baseline_general)
//            }
//            Spacer(modifier = Modifier.height(40.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                FAQButton(title = stringResource(R.string.usage), icon = R.drawable.baseline_data_usage)
//                FAQButton(title = stringResource(R.string.troubleshooting), icon = R.drawable.baseline_troubleshoot)
//            }
//        }
//    }
//}
//
//@Composable
//fun FAQButton(title: String, icon: Int) {
//    Card(
//        modifier = Modifier
//            .width(160.dp)
//            .height(140.dp)
//            .clickable { /* Navigate to detailed FAQ */ },
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Surface(
//                modifier = Modifier.size(64.dp),
//                shape = RoundedCornerShape(32.dp),
//                color = Color(0xFFDCEFD9)
//            ) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Image(
//                        painter = painterResource(id = icon),
//                        contentDescription = title,
//                        modifier = Modifier.size(32.dp)
//                    )
//                }
//            }
//            Spacer(modifier = Modifier.height(12.dp))
//            Text(
//                text = title,
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp,
//                color = Color.Black
//            )
//        }
//    }
//}

@Preview
@Composable
private fun FaqScreenPreview() {
    FAQScreen()
}
