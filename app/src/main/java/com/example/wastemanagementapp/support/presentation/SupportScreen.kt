package com.example.wastemanagementapp.support.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.util.Screen

@Composable
fun SupportScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Image
        Image(
            painter = painterResource(id = R.drawable.banner_intro),
            contentDescription = "Support Image",
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 5.dp)
        )

        Text(text = "Support", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        // Call Us & Mail Us Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SupportOption(
                icon = R.drawable.baseline_local_phone,
                title = "Call Us",
                subtitle = "Talk to our executive")
            SupportOption(
                icon = R.drawable.baseline_email,
                title = "Mail Us",
                subtitle = "Mail to our executive")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // FAQs & Feedback Buttons
        SupportButton(
            icon = R.drawable.baseline_question_mark,
            title = "FAQs",
            subtitle = "Discover App Information",
            onClick = { navController.navigate(Screen.FeedbackScreen) } //faq screen
        )

        SupportButton(
            icon = R.drawable.baseline_comment,
            title = "Feedback",
            subtitle = "Tell us what you think of our App",
            onClick = { navController.navigate(Screen.FeedbackScreen) }
        )

        SupportButton(
            icon = R.drawable.baseline_activity,
            title = "Raise a Concern",
            subtitle = "Raise a Complaint & Help Keep Our Community Clean!",
            onClick = { navController.navigate(Screen.ComplaintScreen) }
        )
    }
}

// Call & Mail Buttons
@Composable
fun SupportOption(icon: Int, title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .size(140.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color(0xFFDCEFD9), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* Handle Click */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDCEFD9))
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, color = Color.Black)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
    }
}

// FAQs, Feedback & Complaint Buttons with Cards
@Composable
fun SupportButton(icon: Int, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFDCEFD9), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = subtitle, fontStyle = FontStyle.Italic, fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewSupportScreen() {
    SupportScreen(rememberNavController())
}

