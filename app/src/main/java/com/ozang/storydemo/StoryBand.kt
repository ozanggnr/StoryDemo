package com.ozang.storydemo

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.launch
import kotlin.math.abs


@Composable
fun StoryPlayer(
    storyGroups: List<StoryGroup>,
    initialGroupIndex: Int,
    onClose: () -> Unit,
    onStoryWatched: (String) -> Unit
) {
    // state
    var currentGroupIndex by remember { mutableStateOf(initialGroupIndex) }
    var currentStoryIndex by remember { mutableStateOf(0) }
    var progressPaused by remember { mutableStateOf(false) }
    var isProgressBarVisible by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    var player: ExoPlayer? by remember { mutableStateOf(null) }

    // animation for horizontal slide
    val slideOffset = remember { Animatable(0f) }

    val currentStoryGroup = storyGroups[currentGroupIndex]
    val currentStory = currentStoryGroup.stories[currentStoryIndex]

    // handle video player lifecycle when story changes
    LaunchedEffect(currentGroupIndex, currentStoryIndex) {
        slideOffset.snapTo(0f)
        player?.release()
        player = null
        if (currentStory is StoryContent.Video) {
            player = ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri(currentStory.url)
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = !progressPaused
                seekTo(0)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player?.release()
        }
    }

    val touchSlopPx = with(LocalDensity.current) { 8.dp.toPx() }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .graphicsLayer { translationX = slideOffset.value }

            .pointerInput(currentGroupIndex, currentStoryIndex) {
                forEachGesture {
                    awaitPointerEventScope {
                        val down = awaitFirstDown()
                        var pastSlop = false
                        var isDragging = false
                        val startX = down.position.x
                        val startY = down.position.y
                        var lastX = startX
                        var totalDragY = 0f

                        var longPressTriggered = false
                        val longPressJob = coroutineScope.launch {
                            kotlinx.coroutines.delay(250)
                            if (!pastSlop && !isDragging) {
                                longPressTriggered = true
                                progressPaused = true
                                isProgressBarVisible = false
                                player?.playWhenReady = false
                            }
                        }

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull() ?: continue

                            val px = change.position.x
                            val py = change.position.y
                            val dxFromStart = px - startX
                            val dyFromStart = py - startY
                            val dx = px - lastX

                            if (!pastSlop && (abs(dxFromStart) > touchSlopPx || abs(
                                    dyFromStart
                                ) > touchSlopPx)
                            ) {
                                pastSlop = true
                                isDragging = abs(dxFromStart) > abs(dyFromStart)
                                longPressJob.cancel()
                                if (isDragging) {
                                    progressPaused = true
                                    isProgressBarVisible = false
                                    player?.playWhenReady = false
                                }
                            }

                            if (isDragging) {
                                change.consume()
                                val maxOffset = size.width * 0.6f
                                val newOffset =
                                    (slideOffset.value + dx).coerceIn(-maxOffset, maxOffset)

                                coroutineScope.launch { slideOffset.snapTo(newOffset) }
                            } else {
                                if (py - startY > 0f) {
                                    totalDragY = maxOf(totalDragY, py - startY)
                                }
                            }

                            lastX = px

                            if (!change.pressed) {
                                longPressJob.cancel()

                                if (isDragging) {
                                    val threshold = size.width * 0.3f
                                    val final = slideOffset.value
                                    if (final > threshold) {
                                        coroutineScope.launch {
                                            slideOffset.animateTo(
                                                size.width.toFloat(), animationSpec = spring()
                                            )
                                            if (currentStoryIndex > 0) {
                                                currentStoryIndex--
                                            } else if (currentGroupIndex > 0) {
                                                currentGroupIndex--
                                                currentStoryIndex =
                                                    storyGroups[currentGroupIndex].stories.size - 1
                                            }
                                            coroutineScope.launch {
                                                slideOffset.snapTo(-size.width.toFloat())
                                                slideOffset.animateTo(0f, animationSpec = spring())
                                            }
                                        }
                                    } else if (final < -threshold) {
                                        coroutineScope.launch {
                                            slideOffset.animateTo(
                                                -size.width.toFloat(), animationSpec = spring()
                                            )
                                            if (currentStoryIndex < currentStoryGroup.stories.size - 1) {
                                                currentStoryIndex++
                                            } else if (currentGroupIndex < storyGroups.size - 1) {
                                                currentGroupIndex++
                                                currentStoryIndex = 0
                                            } else {
                                                onStoryWatched(currentStoryGroup.id)
                                                onClose()
                                            }
                                            coroutineScope.launch {
                                                slideOffset.snapTo(size.width.toFloat())
                                                slideOffset.animateTo(0f, animationSpec = spring())
                                            }
                                        }
                                    } else {
                                        coroutineScope.launch {
                                            slideOffset.animateTo(
                                                0f, animationSpec = spring()
                                            )
                                        }
                                    }
                                } else if (longPressTriggered) {
                                    progressPaused = false
                                    isProgressBarVisible = true
                                    player?.playWhenReady = true
                                } else {
                                    val x = change.position.x
                                    val screenWidth = size.width
                                    if (x < screenWidth / 2) {
                                        coroutineScope.launch {
                                            if (currentStoryIndex > 0) {
                                                currentStoryIndex--
                                            } else if (currentGroupIndex > 0) {
                                                currentGroupIndex--
                                                currentStoryIndex =
                                                    storyGroups[currentGroupIndex].stories.size - 1
                                            }
                                        }
                                    } else {
                                        coroutineScope.launch {
                                            if (currentStoryIndex < currentStoryGroup.stories.size - 1) {
                                                currentStoryIndex++
                                            } else if (currentGroupIndex < storyGroups.size - 1) {
                                                currentGroupIndex++
                                                currentStoryIndex = 0
                                            } else {
                                                onStoryWatched(currentStoryGroup.id)
                                                onClose()
                                            }
                                        }
                                    }
                                }

                                val closeThreshold = size.height * 0.25f
                                if (!isDragging && totalDragY > closeThreshold) {
                                    onClose()
                                }

                                if (!longPressTriggered && progressPaused) {
                                    progressPaused = false
                                    isProgressBarVisible = true
                                    player?.playWhenReady = true
                                }

                                break
                            }
                        }
                    }
                }
            }


    ) {

        when (currentStory) {
            is StoryContent.Image -> {
                Image(
                    painter = rememberAsyncImagePainter(currentStory.resId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is StoryContent.Video -> {
                player?.let { exoPlayer ->
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                player = exoPlayer
                                useController = false
                                keepScreenOn = true
                                layoutParams = android.view.ViewGroup.LayoutParams(
                                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }
                        }, update = { playerView ->
                            playerView.player = exoPlayer
                            if (!progressPaused) {
                                exoPlayer.playWhenReady = true
                            }
                        }, modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        AnimatedVisibility(visible = isProgressBarVisible, enter = fadeIn(), exit = fadeOut()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
                            endY = 200f
                        )
                    )
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(36.dp))
                // progress bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (index in currentStoryGroup.stories.indices) {
                        LinearBar(
                            modifier = Modifier.weight(1f), progress = when {
                                index < currentStoryIndex -> 1f
                                index == currentStoryIndex -> 0f
                                else -> 0f
                            }, isActive = index == currentStoryIndex, isPaused = progressPaused
                        ) {
                            coroutineScope.launch {
                                if (currentStoryIndex < currentStoryGroup.stories.size - 1) {
                                    currentStoryIndex++
                                } else if (currentGroupIndex < storyGroups.size - 1) {
                                    currentGroupIndex++
                                    currentStoryIndex = 0
                                } else {
                                    onStoryWatched(currentStoryGroup.id)
                                    onClose()
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // user ve kapatma butonu
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
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
    }
}

@Composable
fun LinearBar(
    modifier: Modifier,
    progress: Float,
    isActive: Boolean = false,
    isPaused: Boolean = false,
    onAnimationEnd: () -> Unit
) {

    val progressAnim = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var fullWidthPx by remember { mutableStateOf(0f) }


    LaunchedEffect(progress, isActive) {
        if (!isActive) {
            progressAnim.snapTo(progress.coerceIn(0f, 1f))
            return@LaunchedEffect
        }


        progressAnim.snapTo(progress.coerceIn(0f, 1f))
        val remaining = ((1f - progressAnim.value) * 8000).toInt().coerceAtLeast(0)
        if (remaining > 0) {
            try {
                progressAnim.animateTo(1f, animationSpec = tween(remaining))
            } catch (_: Exception) { /* cancelled */
            }
        } else {
            progressAnim.snapTo(1f)
        }
        if (progressAnim.value >= 1f) onAnimationEnd()
    }


    LaunchedEffect(isPaused) {
        if (isPaused) {
            progressAnim.stop()
        } else {
            if (isActive && progressAnim.value < 1f) {
                val remaining = ((1f - progressAnim.value) * 8000).toInt().coerceAtLeast(0)
                if (remaining > 0) {
                    try {
                        progressAnim.animateTo(1f, animationSpec = tween(remaining))
                    } catch (_: Exception) { /* cancelled */
                    }
                }
                if (progressAnim.value >= 1f) onAnimationEnd()
            }
        }
    }


    Box(
        modifier = modifier
            .height(3.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(Color.White.copy(alpha = 0.18f))
            .onGloballyPositioned { coords ->
                fullWidthPx = coords.size.width.toFloat()
            }) {
        val fill = progressAnim.value.coerceIn(0f, 1f)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(with(LocalDensity.current) { (fullWidthPx * fill).toDp() })
                .clip(RoundedCornerShape(3.dp))
                .background(Color.White)
        )
    }
}

