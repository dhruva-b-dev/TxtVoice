package com.dhruva.txtvoice.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhruva.txtvoice.R
import com.dhruva.txtvoice.ui.theme.DarkBackground
import com.dhruva.txtvoice.ui.theme.DarkGray
import com.dhruva.txtvoice.ui.theme.DarkSurface
import com.dhruva.txtvoice.ui.theme.LightGray
import com.dhruva.txtvoice.ui.theme.TxtVoiceTheme
import com.dhruva.txtvoice.ui.theme.YellowBorder
import com.dhruva.txtvoice.ui.theme.YellowPrimary

@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    val filters = listOf(
        stringResource(R.string.all_label),
        stringResource(R.string.spoken_label),
        stringResource(R.string.transcribed_label),
        stringResource(R.string.favorites_label)
    )
    var selectedFilter by remember { mutableStateOf(filters[0]) }

    val historyItems = listOf(
        HistoryItem(
            "\"I am looking for the nearest pharmacy. Could you please direct me\"",
            "2:45 PM",
            HistoryType.TRANSCRIBED
        ),
        HistoryItem(
            "\"Thank you very much for your help. Have a wonderful day!\"",
            "1:12 PM",
            HistoryType.SPOKEN
        ),
        HistoryItem(
            "", "", HistoryType.TRANSCRIBED, "YESTERDAY, OCT 24"
        ),
        HistoryItem(
            "\"The doctor said to take the medication twice a day after meals. Is\"",
            "10:05 PM",
            HistoryType.TRANSCRIBED
        ),
        HistoryItem(
            "\"I'd like to order a black coffee with no sugar, please.\"",
            "4:30 PM",
            HistoryType.SPOKEN
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(stringResource(R.string.search_placeholder), color = DarkGray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = DarkGray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LightGray,
                unfocusedBorderColor = YellowBorder,
                focusedTextColor = LightGray,
                unfocusedTextColor = LightGray,
                cursorColor = YellowPrimary
            ),
            shape = RoundedCornerShape(4.dp)
        )

        LazyRow(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                val isSelected = filter == selectedFilter
                AssistChip(
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isSelected) YellowPrimary else DarkSurface,
                        labelColor = if (isSelected) Color.Black else LightGray
                    ),
                    border = null,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(historyItems) { item ->
                if (item.date != null) {
                    Text(
                        text = item.date.uppercase(LocalLocale.current.platformLocale),
                        color = DarkGray,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                } else {
                    HistoryCard(item)
                }
            }
        }
    }
}

enum class HistoryType {
    SPOKEN, TRANSCRIBED
}

data class HistoryItem(
    val text: String,
    val timestamp: String,
    val type: HistoryType,
    val date: String? = null
)

@Composable
fun HistoryCard(item: HistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TypeTag(item.type)
                Text(
                    text = item.timestamp,
                    color = DarkGray,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.text,
                color = LightGray,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun TypeTag(type: HistoryType) {
    val containerColor = if (type == HistoryType.SPOKEN) Color(0xFF2D2A10) else Color(0xFF1B232C)
    val contentColor = if (type == HistoryType.SPOKEN) YellowPrimary else Color(0xFF8BA2B5)
    val icon = if (type == HistoryType.SPOKEN) Icons.Default.RecordVoiceOver else Icons.Default.Hearing
    val label = if (type == HistoryType.SPOKEN) stringResource(R.string.spoken_label) else stringResource(R.string.transcribed_label)

    Row(
        modifier = Modifier
            .background(containerColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistoryScreenPreview() {
    TxtVoiceTheme {
        HistoryScreen()
    }
}
