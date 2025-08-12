package com.ozang.storydemo

sealed class StoryContent {
    data class Image(val resId: Int) : StoryContent()
    data class Video(val url: String) : StoryContent()
}

data class StoryGroup(
    val id: String,
    val userName: String,
    val profileImage: Int,
    val stories: List<StoryContent>,
    val isViewed: Boolean = false
)