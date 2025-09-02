package com.ozang.storydemo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image

@Composable
internal fun StoryOverlay(
    currentStoryGroup: StoryGroup,
    currentStoryIndex: Int,
    isPaused: Boolean,
    onClose: () -> Unit,
    onStoryComplete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent), endY = 200f
                )
            )
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            currentStoryGroup.stories.forEachIndexed { index, _ ->
                StoryProgressBar(
                    modifier = Modifier.weight(1f),
                    isCompleted = index < currentStoryIndex,
                    isActive = index == currentStoryIndex,
                    isPaused = isPaused,
                    onComplete = onStoryComplete
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = currentStoryGroup.profileImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = currentStoryGroup.userName,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onClose) {
                Text(text = "✕", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
private fun StoryProgressBar(
    modifier: Modifier,
    isCompleted: Boolean,
    isActive: Boolean,
    isPaused: Boolean,
    onComplete: () -> Unit
) {
    val storyDurationMs = 10_000
    val progress = remember { Animatable(0f) }

    LaunchedEffect(isCompleted, isActive) {
        when {
            isCompleted -> progress.snapTo(1f)
            !isActive -> progress.snapTo(0f)
            isActive -> { /* keep current value */
            }
        }
    }

    LaunchedEffect(isActive, isPaused) {
        if (!isActive) return@LaunchedEffect
        if (isPaused) return@LaunchedEffect

        val remaining = (1f - progress.value).coerceIn(0f, 1f)
        if (remaining == 0f) return@LaunchedEffect
        try {
            progress.animateTo(
                1f, animationSpec = tween(
                    durationMillis = (storyDurationMs * remaining).toInt(), easing = LinearEasing
                )
            )
            onComplete()
        } catch (_: Exception) {
        }
    }

    LinearProgressIndicator(
        progress = { progress.value },
        modifier = modifier.height(3.dp),
        trackColor = Color.White.copy(alpha = 0.3f),
        color = Color.White
    )
} 