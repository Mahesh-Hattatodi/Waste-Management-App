package com.example.wastemanagementapp.faq.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wastemanagementapp.R

@Composable
fun FAQContainer(modifier: Modifier = Modifier) {
    FAQScreen(
        modifier = modifier
    )
}

@Composable
fun FAQScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(50.dp))

            Text(text = "FAQs", fontSize = 32.sp, fontWeight = FontWeight.Bold)

            Image(
                painter = painterResource(id = R.drawable.truck_delivery_service),
                contentDescription = "FAQ Image",
                modifier = Modifier
                    .size(180.dp)
                    .padding(vertical = 10.dp)
            )

            Text(
                text = "How Can We Help?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Welcome to our App Support. Ask anything. Our Support Community can help you find answers to all your queries.",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(40.dp))

            // FAQ Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FAQButton(title = "App", icon = R.drawable.baseline_app_settings)
                FAQButton(title = "General", icon = R.drawable.baseline_general)
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FAQButton(title = "Usage", icon = R.drawable.baseline_data_usage)
                FAQButton(title = "Troubleshooting", icon = R.drawable.baseline_troubleshoot)
            }
        }
    }
}

@Composable
fun FAQButton(title: String, icon: Int) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(140.dp)
            .clickable { /* Navigate to detailed FAQ */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(32.dp),
                color = Color(0xFFDCEFD9)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = title,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
private fun FaqScreenPreview() {
    FAQScreen()
}
