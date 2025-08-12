package com.ozang.storydemo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialStoryGroups = listOf(
            StoryGroup(
                id = "1", userName = "Alice", profileImage = R.drawable.photo1, stories = listOf(
                    StoryContent.Image(R.drawable.photo1),
                    StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
                )
            ), StoryGroup(
                id = "2", userName = "Bob", profileImage = R.drawable.photo, stories = listOf(
                    StoryContent.Image(R.drawable.photo3),
                    StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
                )
            ), StoryGroup(
                id = "3",
                userName = "Charlie",
                profileImage = R.drawable.ic_launcher_foreground,
                stories = listOf(
                    StoryContent.Image(R.drawable.photo1),
                    StoryContent.Image(R.drawable.photo4),
                )
            ), StoryGroup(
                id = "4",
                userName = "Jack",
                profileImage = R.drawable.ic_launcher_foreground,
                stories = listOf(
                    StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
                    StoryContent.Image(R.drawable.photo1),
                )
            ), StoryGroup(
                id = "5",
                userName = "Tim",
                profileImage = R.drawable.ic_launcher_foreground,
                stories = listOf(
                    StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
                    StoryContent.Image(R.drawable.photo4),
                )
            ), StoryGroup(
                id = "6",
                userName = "Ali",
                profileImage = R.drawable.ic_launcher_foreground,
                stories = listOf(
                    StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
                    StoryContent.Image(R.drawable.photo3),
                    StoryContent.Image(R.drawable.photo4),
                )
            ), StoryGroup(
                id = "7",
                userName = "Mehmet",
                profileImage = R.drawable.ic_launcher_foreground,
                stories = listOf(
                    StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
                    StoryContent.Image(R.drawable.photo1),
                )
            ), StoryGroup(
                id = "8",
                userName = "Aziz",
                profileImage = R.drawable.ic_launcher_foreground,
                stories = listOf(
                    StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
                    StoryContent.Image(R.drawable.photo3),
                )
            )
        )

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    StoriesApp(storyGroups = initialStoryGroups)
                }
            }
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun StoriesApp(storyGroups: List<StoryGroup>) {
    var selectedStoryGroup by remember { mutableStateOf<StoryGroup?>(null) }
    var selectedStoryIndex by remember { mutableIntStateOf(0) }


    var watchedStoryIds by remember { mutableStateOf(setOf<String>()) }

    // sort stories
    val sortedStoryGroups = remember(watchedStoryIds) {
        storyGroups.sortedBy { it.id in watchedStoryIds }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // ana ekran
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // top bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Stories",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // stories row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(sortedStoryGroups) { index, storyGroup ->
                    StoryPreview(
                        storyGroup = storyGroup.copy(isViewed = storyGroup.id in watchedStoryIds),
                        onClick = {
                            selectedStoryGroup = storyGroup
                            selectedStoryIndex = sortedStoryGroups.indexOfFirst { it.id == storyGroup.id }
                        })
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Wellbees", color = Color.Gray, fontSize = 16.sp
                )
            }
        }

        // player
        selectedStoryGroup?.let { storyGroup ->
            StoryPlayer(
                storyGroups = sortedStoryGroups,
                initialGroupIndex = selectedStoryIndex,
                onClose = { selectedStoryGroup = null },
                onStoryWatched = { storyId ->
                    watchedStoryIds = watchedStoryIds + storyId
                }
            )

        }
    }
}

@Composable
fun StoryPreview(
    storyGroup: StoryGroup, onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }) {
        Box {
            // Profil resimleri
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(
                        if (!storyGroup.isViewed) {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFffc42c), Color(0xFFffc42c)
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.Gray.copy(alpha = 0.5f), Color.Gray.copy(alpha = 0.5f)
                                )
                            )
                        }
                    )
                    .padding(3.dp)
            ) {
                Image(
                    painter = painterResource(id = storyGroup.profileImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = storyGroup.userName,
            fontSize = 12.sp,
            color = if (storyGroup.isViewed) Color.Gray else Color.Black,
            maxLines = 1
        )
    }
}