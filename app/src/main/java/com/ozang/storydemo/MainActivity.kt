package com.ozang.storydemo

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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storyGroups = listOf(
            StoryGroup("1", "Alice", R.drawable.photo1, listOf(
                StoryContent.Image(R.drawable.photo1),
                StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
            )),
            StoryGroup("2", "Bob", R.drawable.photo, listOf(
                StoryContent.Image(R.drawable.photo3),
                StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
            )),
            StoryGroup("3", "Ozan", R.drawable.ic_launcher_foreground, listOf(
                StoryContent.Image(R.drawable.photo1),
                StoryContent.Image(R.drawable.photo4)
            ))
            ,
            StoryGroup("4", "Kenan", R.drawable.ic_launcher_foreground, listOf(
                StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
                StoryContent.Image(R.drawable.photo3)
            ))
            ,
            StoryGroup("5", "Beth", R.drawable.ic_launcher_foreground, listOf(
                StoryContent.Image(R.drawable.photo1),
                StoryContent.Image(R.drawable.photo)
            ))
            ,
            StoryGroup("6", "Jack", R.drawable.ic_launcher_foreground, listOf(
                StoryContent.Image(R.drawable.photo4),
                StoryContent.Image(R.drawable.photo)
            ))
            ,
            StoryGroup("7", "Aziz", R.drawable.ic_launcher_foreground, listOf(
                StoryContent.Image(R.drawable.photo3),
                StoryContent.Image(R.drawable.photo4)
            ))
        )

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    StoriesApp(storyGroups = storyGroups)
                }
            }
        }
    }
}

@Composable
fun StoriesApp(storyGroups: List<StoryGroup>) {
    var selectedStoryGroup by remember { mutableStateOf<StoryGroup?>(null) }
    var selectedStoryIndex by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        // ana ekran
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
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
                itemsIndexed(storyGroups) { index, storyGroup ->
                    StoryPreview(storyGroup = storyGroup) {
                        selectedStoryGroup = storyGroup
                        selectedStoryIndex = index
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Wellbees", color = Color.Gray, fontSize = 16.sp)
            }
        }

        selectedStoryGroup?.let {
            // player
            StoryPlayer(
                storyGroups = storyGroups,
                initialGroupIndex = selectedStoryIndex,
                onClose = { selectedStoryGroup = null }
            )
        }
    }
}

@Composable
fun StoryPreview(storyGroup: StoryGroup, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFffc42c), Color(0xFFffc42c))
                    )
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

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = storyGroup.userName,
            fontSize = 12.sp,
            color = Color.Black,
            maxLines = 1
        )
    }
}