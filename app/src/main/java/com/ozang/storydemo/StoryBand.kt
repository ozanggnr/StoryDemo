package com.ozang.storydemo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun StoryPlayer(
    storyGroups: List<StoryGroup>, initialGroupIndex: Int, onClose: () -> Unit
) {
    var currentGroupIndex by remember { mutableIntStateOf(initialGroupIndex) }
    var currentStoryIndex by remember { mutableIntStateOf(0) }
    var progressPaused by remember { mutableStateOf(false) }
    var isProgressBarVisible by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var player: ExoPlayer? by remember { mutableStateOf(null) }
    val slideOffset = remember { Animatable(0f) }

    val currentStoryGroup = storyGroups[currentGroupIndex]
    val currentStory = currentStoryGroup.stories[currentStoryIndex]

    // Enable/disable swipe navigation
    val canSwipePrevGroup = currentGroupIndex > 0
    val canSwipeNextGroup = currentGroupIndex < storyGroups.size - 1

    // Update player when story changes
    LaunchedEffect(currentGroupIndex, currentStoryIndex) {
        slideOffset.snapTo(0f)
        player?.release()
        player = null

        if (currentStory is StoryContent.Video) {
            player = ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(currentStory.url))
                prepare()
                playWhenReady = !progressPaused
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { player?.release() }
    }

    // Navigation functions
    fun navigatePrevious() {
        scope.launch {
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
        scope.launch {
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
                var totalDragX = 0f
                var totalDragY = 0f
                detectDragGestures(onDragStart = {
                    totalDragX = 0f
                    totalDragY = 0f
                    pauseStory()
                }, onDragEnd = {
                    scope.launch {
                        val widthF = size.width.toFloat()
                        val thresholdX = size.width * 0.3f
                        when {
                            // Right swipe -> previous group
                            slideOffset.value > thresholdX && canSwipePrevGroup -> {
                                slideOffset.animateTo(widthF)
                                currentGroupIndex--
                                currentStoryIndex = storyGroups[currentGroupIndex].stories.size - 1
                                slideOffset.snapTo(-widthF)
                                slideOffset.animateTo(0f)
                            }
                            // Left swipe -> next group
                            slideOffset.value < -thresholdX && canSwipeNextGroup -> {
                                slideOffset.animateTo(-widthF)
                                currentGroupIndex++
                                currentStoryIndex = 0
                                slideOffset.snapTo(widthF)
                                slideOffset.animateTo(0f)
                            }

                            else -> slideOffset.animateTo(0f)
                        }
                        resumeStory()
                    }
                }) { _, dragAmount ->
                    totalDragX += dragAmount.x
                    totalDragY += dragAmount.y
                    val isVerticalDominant = abs(totalDragY) > abs(totalDragX) * 1.2f
                    val verticalThreshold = size.height * 0.2f
                    if (isVerticalDominant && abs(totalDragY) > verticalThreshold) {
                        onClose()
                        return@detectDragGestures
                    }

                    // Horizontal drag
                    val rawOffset = slideOffset.value + dragAmount.x
                    val maxOffset = size.width * 0.6f
                    val minOffset = -maxOffset

                    val adjusted = when {
                        dragAmount.x > 0 && !canSwipePrevGroup -> {
                            (slideOffset.value + dragAmount.x * 0.2f).coerceIn(
                                0f, size.width * 0.15f
                            )
                        }

                        dragAmount.x < 0 && !canSwipeNextGroup -> {
                            (slideOffset.value + dragAmount.x * 0.2f).coerceIn(
                                -size.width * 0.15f, 0f
                            )
                        }

                        else -> rawOffset.coerceIn(minOffset, maxOffset)
                    }

                    scope.launch { slideOffset.snapTo(adjusted) }
                }
            }
            .pointerInput(currentStoryIndex, currentGroupIndex) {
                detectTapGestures(onPress = {
                    pauseStory()
                    tryAwaitRelease()
                    resumeStory()
                }, onTap = { offset ->
                    // Tap next , previous
                    if (offset.x < size.width / 2) {
                        navigatePrevious()
                    } else {
                        navigateNext()
                    }
                })
            }) {
        // Story content
        StoryContentView(currentStory = currentStory, player = player)

        // Story overlay , progress bars
        AnimatedVisibility(
            visible = isProgressBarVisible, enter = fadeIn(), exit = fadeOut()
        ) {
            key(currentGroupIndex, currentStoryIndex) {
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
}