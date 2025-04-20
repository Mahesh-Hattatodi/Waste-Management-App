package com.example.wastemanagementapp.feedback.presentation

import android.content.Context
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.presentation.TopHeadingComponent
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.ObserveAsEvents
import com.example.wastemanagementapp.ui.theme.MainColor
import com.example.wastemanagementapp.ui.theme.WasteManagementAppTheme

@Composable
fun FeedbackContainer(
    viewModel: FeedbackViewModel = hiltViewModel(),
    popBackStack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = viewModel.navEvent) { event ->
        when (event) {
            NavigationEvent.PopBackStack -> popBackStack()
            else -> Unit
        }
    }

    FeedbackScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    state: FeedbackScreenState = FeedbackScreenState(),
    onEvent: (FeedbackEvent) -> Unit = {}
) {
    val topics = context.resources.getStringArray(R.array.list_of_feedback_topic)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopHeadingComponent(
            heading = stringResource(R.string.feedback)
        )

        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = stringResource(R.string.share_your_feedback),
            fontSize = 20.sp,
            textAlign = TextAlign.Start,
            color = DarkGray,
            modifier = Modifier
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.please_select_a_topic_below_and_let_us_know_about_your_concern),
            fontSize = 20.sp,
            textAlign = TextAlign.Start,
            color = DarkGray,
            modifier = Modifier
                .padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Emoji Feedback
        val emojis = listOf("😡", "🙁", "😐", "🙂", "😁")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            emojis.forEach { emoji ->
                AnimatedEmoji(
                    emoji = emoji,
                    isSelected = state.emoji == emoji,
                    onClick = { onEvent(FeedbackEvent.OnEmojiChange(emoji)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown for selecting feedback topic
        ExposedDropdownMenuBox(
            expanded = state.isExpanded,
            onExpandedChange = { onEvent(FeedbackEvent.OnToggle(state.isExpanded)) },
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            OutlinedTextField(
                value = state.selectedTopic,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = {
                    Text(
                        text = stringResource(R.string.select_topic),
                        color = Black
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isExpanded)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF386641),
                    unfocusedBorderColor = Color(0xFF386641),
                    focusedTextColor = Black,
                    unfocusedTextColor = Black
                )
            )

            ExposedDropdownMenu(
                expanded = state.isExpanded,
                onDismissRequest = { onEvent(FeedbackEvent.OnDismiss) },
                modifier = Modifier.background(Color(0xFFDCEFD9)) // Light green dropdown background
            ) {
                topics.forEach { topic ->
                    DropdownMenuItem(
                        text = { Text(topic, color = Black) },
                        onClick = {
                            onEvent(FeedbackEvent.OnSelectFeedbackTopic(topic))
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Feedback Input Field
        OutlinedTextField(
            value = state.feedback,
            onValueChange = {
                onEvent(FeedbackEvent.OnFeedbackChange(it))
            },
            label = { Text(text = stringResource(R.string.enter_feedback), color = Black) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(150.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedTextColor = Black,
                unfocusedTextColor = Black
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                onEvent(FeedbackEvent.OnSubmitFeedback)
            },
            modifier = Modifier
                .width(250.dp)
                .padding(16.dp)
                .height(50.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = MainColor,
                disabledContainerColor = Color(0xFFD3D3D3)
            ),
            enabled = !state.isSubmitting
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (state.isSubmitting) Arrangement.SpaceBetween else Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.submit),
                    color = Color.White
                )

                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedEmoji(
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedFontSize by animateFloatAsState(
        targetValue = if (isSelected) 40f else 32f,
        label = "FontSizeAnimation",
        animationSpec = keyframes {
            durationMillis = 500
            if (isSelected) {
                42f at 300 using FastOutLinearInEasing
                40f at 500
            }
        }
    )

    Text(
        text = emoji,
        fontSize = animatedFontSize.sp,
        modifier = Modifier.clickable { onClick() }
    )
}


@Preview
@Composable
private fun FeedbackScreenPreview() {
    WasteManagementAppTheme {
        FeedbackScreen()
    }
}
