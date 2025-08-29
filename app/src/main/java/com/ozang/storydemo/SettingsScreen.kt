package com.ozang.storydemo

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onAboutClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var showRateDialog by remember { mutableStateOf(false) }

    val accountSettings = listOf("Kategori")
    val suggestions = listOf("Arkadaşını davet et", "Değerlendir", "Burayı nasıl buldun?")
    val wellbeesSettings = listOf(
        "Geri Bildirim", "Wellbees Hakkında", "SSS", "Bildirimler",
        "Hizmet Kullanım Koşulları", "Gizlilik Politikası", "Şifre Değiştir",
        "Dil Seçimi", "Uzman Dilini Seç", "Hesap Kapatma"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text("Ayarlar", fontSize = 25.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Black)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            SettingsGroup("HESAP AYARLARI", accountSettings) { }
            Spacer(modifier = Modifier.height(24.dp))

            SettingsGroup("ÖNERİLER", suggestions) { item ->
                if (item == "Değerlendir") {
                    showRateDialog = true
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            SettingsGroup("WELLBEES AYARLARI", wellbeesSettings) { item ->
                if (item == "Wellbees Hakkında") {
                    onAboutClick()
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Version
            Text(
                "V6.1.6",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp)
            )

            // Exit Button
            Spacer(modifier = Modifier.height(22.dp))
            Button(
                onClick = { /* Handle exit */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53E3E)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Çıkış", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }

    // AlertDialog with square corners and yellow buttons
    if (showRateDialog) {
        AlertDialog(
            onDismissRequest = { showRateDialog = false },
            title = {
                Text(
                    "Mağazada bizi değerlendirmek ve yorum bırakmak ister misiniz?",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRateDialog = false
                        val intent = Intent(Intent.ACTION_VIEW,
                            "https://play.google.com/store/apps/details?id=com.wellbees.android&hl=en".toUri())
                        try {
                            context.startActivity(intent)
                        } catch (_: ActivityNotFoundException) {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    "https://play.google.com/store/apps/details?id=com.wellbees.android&hl=en".toUri()
                                )
                            )
                        }
                    }
                ) {
                    Text(
                        "İZİN VER",
                        color = Color(0xFFffc42c),
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showRateDialog = false }) {
                    Text(
                        "REDDET",
                        color = Color(0xFFffc42c),
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            shape = RoundedCornerShape(0.dp), // Square corners
            containerColor = Color.White
        )
    }
}

@Composable
fun SettingsGroup(
    title: String,
    items: List<String>,
    onItemClick: (String) -> Unit = {}
) {
    Text(
        title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Gray,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))

    items.forEach { item ->
        SettingsItem(item) { onItemClick(item) }
    }
}

@Composable
fun SettingsItem(title: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Star,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            title,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            "Navigate",
            modifier = Modifier.size(20.dp),
            tint = Color.Gray
        )
    }
    HorizontalDivider(
        color = Color.LightGray.copy(alpha = 0.3f),
        thickness = 0.5.dp,
        modifier = Modifier.padding(start = 48.dp)
    )
}