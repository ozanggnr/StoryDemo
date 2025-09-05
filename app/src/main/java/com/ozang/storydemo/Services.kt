package com.ozang.storydemo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ServiceItem(
    val id: String,
    val title: String,
    val iconRes: Int = R.drawable.ic_launcher_foreground // error android icon
)

val allServices = listOf(
    ServiceItem("1", "Alışkanlık Takibi",R.drawable.habit),
    ServiceItem("2", "Uzmanlar",R.drawable.challenges),
    ServiceItem("3", "Meydan Okumalar",R.drawable.challenges),
    ServiceItem("4", "Yazılar",R.drawable.writings),
    ServiceItem("5", "Kutlama Kartları",R.drawable.celebration),
    ServiceItem("6", "WellMarket",R.drawable.market),
    ServiceItem("7", "Şirket Kulüpleri",R.drawable.clubs),
    ServiceItem("8", "İkinci El Market",R.drawable.shand),
    ServiceItem("9", "Kulüpler",R.drawable.compclubs),
    ServiceItem("10", "Etkinlikler",R.drawable.writings),
    ServiceItem("11", "Programlar",R.drawable.challenges),
    ServiceItem("12", "Videolar",R.drawable.habit),
    ServiceItem("13", "Sesler",R.drawable.market),
    ServiceItem("14", "Topluluk Akışı",R.drawable.challenges),
    ServiceItem("15", "Duyuru Akışı",R.drawable.habit),
    ServiceItem("16", "Liderlik Tablosu",R.drawable.shand),
    ServiceItem("17", "Aramıza Yeni Katılanlar",R.drawable.compclubs)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesSection(
    onServiceClick: (ServiceItem) -> Unit,
    onViewAllClick: () -> Unit
) {
    Column {
        // Header with "Hizmetler" and "Tümünü Gör"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hizmetler",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            TextButton(
                onClick = onViewAllClick
            ) {
                Text(
                    text = "Tümünü Gör",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Single horizontally scrollable row with all services
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 25.dp)
        ) {
            items(allServices) { service ->
                ServiceCard(
                    service = service,
                    onClick = { onServiceClick(service) }
                )
            }
        }
    }
}

@Composable
fun ServiceCard(
    service: ServiceItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon on top
            Box(
                modifier = Modifier
                    .size(40.dp) // Icon container
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(service.iconRes),
                    contentDescription = service.title,
                    modifier = Modifier.size(24.dp), // Icon size
                    contentScale = ContentScale.Fit
                )
            }

            // Service name below icon
            Text(
                text = service.title,
                fontSize = 12.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllServicesBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    onServiceClick: (ServiceItem) -> Unit
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header
                Text(
                    text = "Tüm Servisler",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Services in a vertical scrollable list with left-aligned layout. Will be 2 row
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    allServices.chunked(3).forEach { rowServices -> // 3 items per row bo
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowServices.forEach { service ->
                                AllServicesCard(
                                    service = service,
                                    onClick = {
                                        onServiceClick(service)
                                        onDismiss()
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AllServicesCard(
    service: ServiceItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(100.dp)
            .height(120.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon on top
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(service.iconRes),
                    contentDescription = service.title,
                    modifier = Modifier.size(28.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Service name below icon
            Text(
                text = service.title,
                fontSize = 12.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                maxLines = 2,
                fontWeight = FontWeight.Medium
            )
        }
    }
}