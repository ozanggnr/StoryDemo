package com.ozang.storydemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storyGroups = listOf(
            StoryGroup(
                "1", "Alice", R.drawable.photo1, listOf(
                    StoryContent.Image(R.drawable.photo1),
                    StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                )
            ), StoryGroup(
                "2", "Bob", R.drawable.photo, listOf(
                    StoryContent.Image(R.drawable.photo3),
                    StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
                )
            ), StoryGroup(
                "3", "Ozan", R.drawable.ic_launcher_foreground, listOf(
                    StoryContent.Image(R.drawable.photo1), StoryContent.Image(R.drawable.photo4)
                )
            ), StoryGroup(
                "4", "Kenan", R.drawable.ic_launcher_foreground, listOf(
                    StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
                    StoryContent.Image(R.drawable.photo3)
                )
            ), StoryGroup(
                "5", "Beth", R.drawable.ic_launcher_foreground, listOf(
                    StoryContent.Image(R.drawable.photo1), StoryContent.Image(R.drawable.photo)
                )
            ), StoryGroup(
                "6", "Jack", R.drawable.ic_launcher_foreground, listOf(
                    StoryContent.Image(R.drawable.photo4), StoryContent.Image(R.drawable.photo)
                )
            ), StoryGroup(
                "7", "Aziz", R.drawable.ic_launcher_foreground, listOf(
                    StoryContent.Image(R.drawable.photo3), StoryContent.Image(R.drawable.photo4)
                )
            )
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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //HamburgerMenu drawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HamburgerMenu(userName = "Ozan",userSurname = "Gungor")
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    //Name
                    TopBar(
                        userName = "Ozan",
                        onMenuClick = { scope.launch { drawerState.open() } }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    //Stories LazyRow
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 25.dp)
                    ) {
                        itemsIndexed(storyGroups) { index, storyGroup ->
                            StoryPreview(storyGroup = storyGroup) {
                                selectedStoryGroup = storyGroup
                                selectedStoryIndex = index
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "For Today",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Wellbees", color = Color.Gray, fontSize = 16.sp)
                    }
                }

                selectedStoryGroup?.let {
                    StoryPlayer(
                        storyGroups = storyGroups,
                        initialGroupIndex = selectedStoryIndex,
                        onClose = { selectedStoryGroup = null }
                    )
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(userName: String, onMenuClick: () -> Unit) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.wellbees),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Column {
                    Text(text = "Hello,", fontSize = 16.sp, color = Color.Black)
                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* search */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(onClick = { /* notifications */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", Modifier.size(30.dp))
            }
            IconButton(onClick = { /* profile */ }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profile", Modifier.size(30.dp))
            }
        }
    )
}


@Composable
fun StoryPreview(storyGroup: StoryGroup, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }) {
        Box(
            modifier = Modifier
                .size(85.dp)
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
            text = storyGroup.userName, fontSize = 12.sp, color = Color.Black, maxLines = 1
        )
    }
}