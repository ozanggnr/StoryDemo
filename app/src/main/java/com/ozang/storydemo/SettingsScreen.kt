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
import androidx.compose.material.icons.filled.Check
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

data class Language(
    val code: String,
    val name: String,
    val displayName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onAboutClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onInviteFriendClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var showRateDialog by remember { mutableStateOf(false) }
    var showLanguageBottomSheet by remember { mutableStateOf(false) }
    var showExpertLanguageBottomSheet by remember { mutableStateOf(false) }

    // Available languages
    val availableLanguages = listOf(
        Language("tr", "Türkçe", "Türkçe"),
        Language("en", "English", "English"),
        Language("it", "Italiano", "Italiano"),
        Language("ru", "Русский", "Русса"),
        Language("fr", "Français", "Français"),
        Language("pt", "Português", "Português"),
        Language("es", "Español", "Español"),
        Language("de", "Deutsch", "Deutsch"),
        Language("ar", "العربية", "العربية"),
        Language("ro", "Română", "Română")
    )

    // Selected languages
    var selectedLanguage by remember { mutableStateOf(availableLanguages.find { it.code == "en" } ?: availableLanguages[0]) }
    var selectedExpertLanguage by remember { mutableStateOf(availableLanguages.find { it.code == "en" } ?: availableLanguages[0]) }

    val bottomSheetState = rememberModalBottomSheetState()

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
                when (item) {
                    "Arkadaşını davet et" -> onInviteFriendClick()
                    "Değerlendir" -> showRateDialog = true
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Custom wellbees ayarları
            Text(
                "WELLBEES AYARLARI",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            wellbeesSettings.forEach { item ->
                when (item) {
                    "Dil Seçimi" -> {
                        LanguageSettingsItem(
                            title = item,
                            selectedLanguage = selectedLanguage.displayName,
                            onClick = { showLanguageBottomSheet = true }
                        )
                    }
                    "Uzman Dilini Seç" -> {
                        LanguageSettingsItem(
                            title = item,
                            selectedLanguage = selectedExpertLanguage.displayName,
                            onClick = { showExpertLanguageBottomSheet = true }
                        )
                    }
                    else -> {
                        SettingsItem(item) {
                            when (item) {
                                "Wellbees Hakkında" -> onAboutClick()
                                "Hizmet Kullanım Koşulları" -> onTermsClick()
                            }
                        }
                    }
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

    // Rate Dialog
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
            shape = RoundedCornerShape(0.dp),
            containerColor = Color.White
        )
    }

    // Language selection popup
    if (showLanguageBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLanguageBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = Color.White,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
        ) {
            LanguageSelectionContent(
                languages = availableLanguages,
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { language ->
                    selectedLanguage = language
                    showLanguageBottomSheet = false
                }
            )
        }
    }

    // Expert Language Selection popup
    if (showExpertLanguageBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showExpertLanguageBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = Color.White,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
        ) {
            LanguageSelectionContent(
                languages = availableLanguages,
                selectedLanguage = selectedExpertLanguage,
                onLanguageSelected = { language ->
                    selectedExpertLanguage = language
                    showExpertLanguageBottomSheet = false
                }
            )
        }
    }
}

@Composable
fun LanguageSettingsItem(
    title: String,
    selectedLanguage: String,
    onClick: () -> Unit = {}
) {
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
        Text(
            selectedLanguage,
            fontSize = 16.sp,
            color = Color(0xFF1A997B),
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(8.dp))
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

@Composable
fun LanguageSelectionContent(
    languages: List<Language>,
    selectedLanguage: Language,
    onLanguageSelected: (Language) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        // Language List
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            languages.forEach { language ->
                LanguageOption(
                    language = language,
                    isSelected = language.code == selectedLanguage.code,
                    onClick = { onLanguageSelected(language) }
                )
            }
        }
    }
}

@Composable
fun LanguageOption(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            language.name,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Selected",
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF1A997B)
            )
        }
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