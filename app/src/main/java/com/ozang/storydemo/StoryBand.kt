package com.ozang.storydemo

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun StoryPlayer(
    storyGroups: List<StoryGroup>,
    initialGroupIndex: Int,
    onClose: () -> Unit
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
                setMediaItem(MediaItem.fromUri(currentStory.url))
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

    // Navigation
    fun navigatePrevious() {
        coroutineScope.launch {
            when {
                currentStoryIndex > 0 -> currentStoryIndex--
                currentGroupIndex > 0 -> {
                    currentGroupIndex--
                    currentStoryIndex = storyGroups[currentGroupIndex].stories.size - 1
                }
            }
        }
    }

    fun navigateNext() {
        coroutineScope.launch {
            when {
                currentStoryIndex < currentStoryGroup.stories.size - 1 -> currentStoryIndex++
                currentGroupIndex < storyGroups.size - 1 -> {
                    currentGroupIndex++
                    currentStoryIndex = 0
                }
                else -> onClose()
            }
        }
    }

    fun pauseStory() {
        progressPaused = true
        isProgressBarVisible = false
        player?.playWhenReady = false
    }

    fun resumeStory() {
        progressPaused = false
        isProgressBarVisible = true
        player?.playWhenReady = true
    }

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

                        // gesture handgling
                        fun handleSwipeGesture(): Unit {
                            val threshold = size.width * 0.3f
                            val final = slideOffset.value
                            coroutineScope.launch {
                                when {
                                    final > threshold -> {
                                        slideOffset.animateTo(size.width.toFloat(), animationSpec = spring())
                                        navigatePrevious()
                                        slideOffset.snapTo(-size.width.toFloat())
                                        slideOffset.animateTo(0f, animationSpec = spring())
                                    }
                                    final < -threshold -> {
                                        slideOffset.animateTo(-size.width.toFloat(), animationSpec = spring())
                                        navigateNext()
                                        slideOffset.snapTo(size.width.toFloat())
                                        slideOffset.animateTo(0f, animationSpec = spring())
                                    }
                                    else -> slideOffset.animateTo(0f, animationSpec = spring())
                                }
                            }
                        }

                        fun handleTapGesture(tapX: Float): Unit {
                            if (tapX < size.width / 2) navigatePrevious() else navigateNext()
                        }

                        val longPressJob = coroutineScope.launch {
                            kotlinx.coroutines.delay(250)
                            if (!pastSlop && !isDragging) {
                                longPressTriggered = true
                                pauseStory()
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

                            // Check if movement exceeds touch slop
                            if (!pastSlop && (abs(dxFromStart) > touchSlopPx || abs(dyFromStart) > touchSlopPx)) {
                                pastSlop = true
                                isDragging = abs(dxFromStart) > abs(dyFromStart)
                                longPressJob.cancel()
                                if (isDragging) pauseStory()
                            }

                            // Handle horizontal dragging
                            if (isDragging) {
                                change.consume()
                                val maxOffset = size.width * 0.6f
                                val newOffset = (slideOffset.value + dx).coerceIn(-maxOffset, maxOffset)
                                coroutineScope.launch { slideOffset.snapTo(newOffset) }
                            } else if (py > startY) {
                                totalDragY = maxOf(totalDragY, py - startY)
                            }

                            lastX = px

                            // Handle touch release
                            if (!change.pressed) {
                                longPressJob.cancel()

                                if (isDragging) {
                                    handleSwipeGesture()
                                } else if (longPressTriggered) {
                                    resumeStory()
                                } else if (totalDragY > size.height * 0.25f) {
                                    onClose()
                                } else {
                                    handleTapGesture(px)
                                }

                                if (!longPressTriggered && progressPaused) resumeStory()
                                break
                            }
                        }
                    }
                }
            }
    ) {
        // Story Content
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
                        },
                        update = { playerView ->
                            playerView.player = exoPlayer
                            if (!progressPaused) exoPlayer.playWhenReady = true
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // UI Overlay
        AnimatedVisibility(
            visible = isProgressBarVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            StoryOverlay(
                currentStoryGroup = currentStoryGroup,
                currentStoryIndex = currentStoryIndex,
                isPaused = progressPaused,
                onClose = onClose,
                onStoryComplete = ::navigateNext
            )
        }
    }
}

@Composable
private fun StoryOverlay(
    currentStoryGroup: StoryGroup,
    currentStoryIndex: Int,
    isPaused: Boolean,
    onClose: () -> Unit,
    onStoryComplete: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
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
            currentStoryGroup.stories.forEachIndexed { index, _ ->
                LinearBar(
                    modifier = Modifier.weight(1f),
                    progress = when {
                        index < currentStoryIndex -> 1f
                        index == currentStoryIndex -> 0f
                        else -> 0f
                    },
                    isActive = index == currentStoryIndex,
                    isPaused = isPaused,
                    onAnimationEnd = { coroutineScope.launch { onStoryComplete() } }
                )
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

@Composable
private fun LinearBar(
    modifier: Modifier,
    progress: Float,
    isActive: Boolean = false,
    isPaused: Boolean = false,
    onAnimationEnd: () -> Unit
) {
    val progressAnim = remember { Animatable(0f) }
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
                if (progressAnim.value >= 1f) onAnimationEnd()
            } catch (_: Exception) { /* Animation cancelled */ }
        } else {
            progressAnim.snapTo(1f)
            onAnimationEnd()
        }
    }

    LaunchedEffect(isPaused) {
        if (isPaused) {
            progressAnim.stop()
        } else if (isActive && progressAnim.value < 1f) {
            val remaining = ((1f - progressAnim.value) * 8000).toInt().coerceAtLeast(0)
            if (remaining > 0) {
                try {
                    progressAnim.animateTo(1f, animationSpec = tween(remaining))
                    if (progressAnim.value >= 1f) onAnimationEnd()
                } catch (_: Exception) { }
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
            }
    ) {
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