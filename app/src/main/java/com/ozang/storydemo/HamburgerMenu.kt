package com.ozang.storydemo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HamburgerMenu(
    userName: String, userSurname: String, onSettingsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(width = 300.dp)
            .background(Color.White)
            .padding(top = 80.dp, start = 35.dp, bottom = 120.dp)

    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.wellbees),
            contentDescription = "Logo",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ad Soyad
        Text(
            text = "$userName $userSurname", fontSize = 20.sp, color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))
        // See your profile text
        Text(
            text = "See your profile", fontSize = 14.sp, color = Color(0xFF1A997B)
        )

        Spacer(modifier = Modifier.weight(1f)) // spacer

        // Settings butonu
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onSettingsClick() }
                .padding(vertical = 8.dp)) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Settings", fontSize = 18.sp, color = Color.Black
            )
        }
    }
}