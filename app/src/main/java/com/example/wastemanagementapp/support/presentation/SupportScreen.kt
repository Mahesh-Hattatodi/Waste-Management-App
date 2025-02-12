package com.example.wastemanagementapp.support.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.ObserveAsEvents

@Composable
fun SupportContainer(
    modifier: Modifier = Modifier,
    viewModel: SupportViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent.Navigate) -> Unit = {}
) {

    ObserveAsEvents(flow = viewModel.navigationEvent) { event ->
        when (event) {
            is NavigationEvent.Navigate -> {
                onNavigate(event)
            }

            NavigationEvent.PopBackStack -> Unit
        }
    }

    SupportScreen(
        modifier = modifier,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun SupportScreen(
    modifier: Modifier = Modifier,
    onEvent: (SupportScreenEvent) -> Unit = {}
) {
    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Header Image
            Image(
                painter = painterResource(id = R.drawable.banner_intro),
                contentDescription = stringResource(R.string.support_image),
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 5.dp)
            )

            Text(text = stringResource(R.string.support), fontSize = 28.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(20.dp))

            // Call Us & Mail Us Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SupportOption(
                    icon = R.drawable.baseline_local_phone,
                    title = stringResource(R.string.call_us),
                    subtitle = stringResource(R.string.talk_to_our_executive),
                    onClick = {
                        onEvent(SupportScreenEvent.OnCallUsRequest)
                    }
                )
                SupportOption(
                    icon = R.drawable.baseline_email,
                    title = stringResource(R.string.mail_us),
                    subtitle = stringResource(R.string.mail_to_our_executive),
                    onClick = {
                        onEvent(SupportScreenEvent.OnEmailUsRequest)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // FAQs & Feedback Buttons
            SupportButton(
                icon = R.drawable.baseline_question_mark,
                title = stringResource(R.string.faqs),
                subtitle = stringResource(R.string.discover_app_information),
                onClick = { onEvent(SupportScreenEvent.OnFaqClick) } //faq screen
            )

            SupportButton(
                icon = R.drawable.baseline_comment,
                title = stringResource(R.string.feedback),
                subtitle = stringResource(R.string.tell_us_what_you_think_of_our_app),
                onClick = { onEvent(SupportScreenEvent.OnFeedbackClick) }
            )

            SupportButton(
                icon = R.drawable.baseline_activity,
                title = stringResource(R.string.raise_a_concern),
                subtitle = stringResource(R.string.raise_a_complaint_help_keep_our_community_clean),
                onClick = { onEvent(SupportScreenEvent.OnComplaintClick) }
            )
        }
    }
}

// Call & Mail Buttons
@Composable
fun SupportOption(
    icon: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
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
            onClick = onClick,
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
    SupportScreen()
}

