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

        val storyGroups = createStoryGroups()

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    StoriesApp(storyGroups = storyGroups)
                }
            }
        }
    }

    private fun createStoryGroups() = listOf(
        StoryGroup("1", "Alice", R.drawable.photo1, listOf(
            StoryContent.Image(R.drawable.photo1),
            StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        )),
        StoryGroup("2", "Bob", R.drawable.photo2, listOf(
            StoryContent.Image(R.drawable.photo3),
            StoryContent.Video("https:  commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
        )),
        StoryGroup("3", "Ozan", R.drawable.ic_launcher_foreground, listOf(
            StoryContent.Image(R.drawable.photo1),
            StoryContent.Image(R.drawable.photo4)
        )),
        StoryGroup("4", "Kenan", R.drawable.ic_launcher_foreground, listOf(
            StoryContent.Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
            StoryContent.Image(R.drawable.photo3)
        )),
        StoryGroup("5", "Beth", R.drawable.ic_launcher_foreground, listOf(
            StoryContent.Image(R.drawable.photo1),
            StoryContent.Image(R.drawable.photo2)
        )),
        StoryGroup("6", "Jack", R.drawable.ic_launcher_foreground, listOf(
            StoryContent.Image(R.drawable.photo4),
            StoryContent.Image(R.drawable.photo2)
        )),
        StoryGroup("7", "Aziz", R.drawable.ic_launcher_foreground, listOf(
            StoryContent.Image(R.drawable.photo3),
            StoryContent.Image(R.drawable.photo4)
        ))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(userName: String, onMenuClick: () -> Unit) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.wellbees),
                    contentDescription = "App Icon",
                    modifier = Modifier.size(50.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column {
                    Text("Hello,", fontSize = 16.sp, color = Color.Black)
                    Text(userName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, "Menu", tint = Color.Black, modifier = Modifier.size(30.dp))
            }
        },
        actions = {
            IconButton(onClick = { /* search */ }) {
                Icon(Icons.Default.Search, "Search")
            }
            IconButton(onClick = { /* notifications */ }) {
                Icon(Icons.Default.Notifications, "Notifications", Modifier.size(30.dp))
            }
            IconButton(onClick = { /* profile */ }) {
                Icon(Icons.Default.AccountCircle, "Profile", Modifier.size(30.dp))
            }
        }
    )
}

@Composable
fun StoriesApp(storyGroups: List<StoryGroup>) {
    var selectedStoryGroup by remember { mutableStateOf<StoryGroup?>(null) }
    var selectedStoryIndex by remember { mutableIntStateOf(0) }
    var showSettings by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    var showTerms by remember { mutableStateOf(false) }
    var showInviteFriend by remember { mutableStateOf(false) }


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var forTodayItems by remember {
        mutableStateOf(
            listOf(
                ForTodayItem("sleep", "How did you sleep?", ForTodayType.SLEEP_MOOD),
                ForTodayItem("mood", "How are you today?", ForTodayType.DAILY_MOOD),
                ForTodayItem("medical", "Take your medication", ForTodayType.MEDICAL_TASK)
            )
        )
    }

    when {
        showAbout -> {
            AboutScreen(
                onBackClick = { showAbout = false }
            )
        }
        showTerms -> {
            TermsOfServiceScreen(
                onBackClick = { showTerms = false }
            )
        }
        showInviteFriend -> {
            InviteFriendScreen(
                onBackClick = { showInviteFriend = false }
            )
        }
        showSettings -> {
            SettingsScreen(
                onBackClick = { showSettings = false },
                onAboutClick = {
                    showAbout = true
                },
                onTermsClick = {
                    showTerms= true
                },
                onInviteFriendClick = { showInviteFriend = true }
            )
        }
        else -> {
            MainContent(
                storyGroups = storyGroups,
                selectedStoryGroup = selectedStoryGroup,
                selectedStoryIndex = selectedStoryIndex,
                drawerState = drawerState,
                scope = scope,
                forTodayItems = forTodayItems,
                onStorySelect = { group, index ->
                    selectedStoryGroup = group
                    selectedStoryIndex = index
                },
                onStoryClose = { selectedStoryGroup = null },
                onSettingsClick = { showSettings = true },
                onForTodayUpdate = { updatedItem ->
                    forTodayItems = forTodayItems.map { item ->
                        if (item.id == updatedItem.id) updatedItem else item
                    }
                }
            )
        }
    }
}

@Composable
fun MainContent(
    storyGroups: List<StoryGroup>,
    selectedStoryGroup: StoryGroup?,
    selectedStoryIndex: Int,
    drawerState: DrawerState,
    scope: kotlinx.coroutines.CoroutineScope,
    forTodayItems: List<ForTodayItem>,
    onStorySelect: (StoryGroup, Int) -> Unit,
    onStoryClose: () -> Unit,
    onSettingsClick: () -> Unit,
    onForTodayUpdate: (ForTodayItem) -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HamburgerMenu(
                userName = "Ozan",
                userSurname = "Gungor",
                onSettingsClick = {
                    scope.launch {
                        drawerState.close()
                        onSettingsClick()
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
            ) {
                TopBar("Ozan") { scope.launch { drawerState.open() } }
                Spacer(modifier = Modifier.height(16.dp))

                StoriesRow(storyGroups, onStorySelect)
                Spacer(modifier = Modifier.height(16.dp))

                ForTodaySection(forTodayItems, onForTodayUpdate)
                Spacer(modifier = Modifier.height(24.dp))
            }

            selectedStoryGroup?.let {
                StoryPlayer(
                    storyGroups = storyGroups,
                    initialGroupIndex = selectedStoryIndex,
                    onClose = onStoryClose
                )
            }
        }
    }
}

@Composable
fun StoriesRow(
    storyGroups: List<StoryGroup>,
    onStorySelect: (StoryGroup, Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 25.dp)
    ) {
        itemsIndexed(storyGroups) { index, storyGroup ->
            StoryPreview(storyGroup) {
                onStorySelect(storyGroup, index)
            }
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
                painter = painterResource(storyGroup.profileImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(storyGroup.userName, fontSize = 12.sp, color = Color.Black, maxLines = 1)
    }
}