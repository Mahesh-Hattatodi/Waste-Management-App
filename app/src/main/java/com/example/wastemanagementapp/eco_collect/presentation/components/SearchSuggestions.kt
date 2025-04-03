package com.example.wastemanagementapp.eco_collect.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wastemanagementapp.eco_collect.domain.models.SearchResponse

@Composable
fun LocationSuggestionItem(
    modifier: Modifier = Modifier,
    result: SearchResponse,
    onSelect: (SearchResponse) -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onSelect(result) },
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.dp, brush = Brush.sweepGradient(listOf(Color.Black, Color.Black))),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = result.displayName,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}