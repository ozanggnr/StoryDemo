package com.ozang.storydemo

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ForTodayItem(
    val id: String,
    val title: String,
    val type: ForTodayType,
    val isCompleted: Boolean = false,
    val completionPercentage: Int = 0,
    val selectedMood: Int = -1 // no mood selected
)

enum class ForTodayType {
    SLEEP_MOOD, DAILY_MOOD, MEDICAL_TASK
}

val moodImages = listOf(
    R.drawable.mood1, R.drawable.mood2, R.drawable.mood3, R.drawable.mood4, R.drawable.mood5
)

@Composable
fun ForTodaySection(
    items: List<ForTodayItem>, onItemUpdate: (ForTodayItem) -> Unit
) {
    Column {
        Text(
            text = "For Today",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 25.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        val lazyListState = rememberLazyListState()
        val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)

        LazyRow(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            contentPadding = PaddingValues(horizontal = 25.dp),
            flingBehavior = snapFlingBehavior
        ) {
            items(items, key = { it.id }) { item ->
                Box(
                    modifier = Modifier.fillParentMaxWidth()
                ) {
                    UniformForTodayCard(item = item, onItemClick = {
                        when (item.type) {
                            ForTodayType.SLEEP_MOOD, ForTodayType.DAILY_MOOD -> {
                            }

                            ForTodayType.MEDICAL_TASK -> {
                                onItemUpdate(item.copy(isCompleted = !item.isCompleted))
                            }
                        }
                    }, onMoodSelected = { moodIndex ->
                        onItemUpdate(item.copy(selectedMood = moodIndex, isCompleted = true))
                    })
                }
            }
        }
    }
}

@Composable
fun UniformForTodayCard(
    item: ForTodayItem, onItemClick: () -> Unit, onMoodSelected: (Int) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            // Content section
            when (item.type) {
                ForTodayType.SLEEP_MOOD, ForTodayType.DAILY_MOOD -> {
                    // Mood cards
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MoodSelectionRow(
                            selectedMood = item.selectedMood, onMoodSelected = onMoodSelected
                        )

                        Text(
                            text = item.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }

                ForTodayType.MEDICAL_TASK -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MedicalTaskRow(
                            isCompleted = item.isCompleted, onTaskClick = onItemClick
                        )

                        Text(
                            text = item.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MoodSelectionRow(
    selectedMood: Int, onMoodSelected: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentWidth()
    ) {
        moodImages.forEachIndexed { index, moodRes ->
            val isSelected = selectedMood == index
            val alpha = if (selectedMood >= 0 && !isSelected) 0.3f else 1f

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onMoodSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = moodRes),
                    contentDescription = "Mood $index",
                    modifier = Modifier
                        .size(70.dp)
                        .graphicsLayer(alpha = alpha),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
fun MedicalTaskRow(
    isCompleted: Boolean, onTaskClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.med),
            contentDescription = "Medical Task",
            modifier = Modifier.size(50.dp),
            contentScale = ContentScale.Fit
        )

        // Completion button
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    brush = if (isCompleted) {
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF4CAF50), Color(0xFF45A049))
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(Color(0xFFffc42c), Color(0xFFffb000))
                        )
                    }, shape = CircleShape
                )
                .clip(CircleShape)
                .clickable { onTaskClick() }, contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Text(
                    text = "✓", fontSize = 20.sp, color = Color.White, textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "◦", fontSize = 16.sp, color = Color.White, textAlign = TextAlign.Center
                )
            }
        }
    }
}