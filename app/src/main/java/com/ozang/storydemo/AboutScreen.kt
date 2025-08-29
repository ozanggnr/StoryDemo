package com.ozang.storydemo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.about_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Black)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        // Privacy Policy Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = stringResource(R.string.privacy_policy_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A997B),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Introduction
            Text(
                text = stringResource(R.string.privacy_policy_intro),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Information
            Text(
                text = stringResource(R.string.information_collected_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.information_collected_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cookies
            Text(
                text = stringResource(R.string.cookies_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.cookies_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Communications
            Text(
                text = stringResource(R.string.communications_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.communications_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Who We Give Information
            Text(
                text = stringResource(R.string.who_we_give_info_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.who_we_give_info_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Where We Store Information
            Text(
                text = stringResource(R.string.where_we_store_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.where_we_store_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Child Safety
            Text(
                text = stringResource(R.string.child_safety_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.child_safety_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Data Protection
            Text(
                text = stringResource(R.string.data_protection_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.data_protection_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Data Retention
            Text(
                text = stringResource(R.string.data_retention_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.data_retention_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Rights
            Text(
                text = stringResource(R.string.user_rights_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.user_rights_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Policy Changes
            Text(
                text = stringResource(R.string.policy_changes_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.policy_changes_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contact
            Text(
                text = stringResource(R.string.contact_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.contact_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Data Processing Table S
            Text(
                text = stringResource(R.string.data_table_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.data_table_intro),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            //
            DataProcessingTable()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun DataProcessingTable() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
    ) {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.3f))
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.table_header_purpose),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.table_header_type),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.table_header_lawful),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }

        // Table Rows
        TableRow(
            stringResource(R.string.table_row1_purpose),
            stringResource(R.string.table_row1_type),
            stringResource(R.string.table_row1_lawful)
        )

        TableRow(
            stringResource(R.string.table_row2_purpose),
            stringResource(R.string.table_row2_type),
            stringResource(R.string.table_row2_lawful)
        )

        TableRow(
            stringResource(R.string.table_row3_purpose),
            stringResource(R.string.table_row3_type),
            stringResource(R.string.table_row3_lawful)
        )

        TableRow(
            stringResource(R.string.table_row4_purpose),
            stringResource(R.string.table_row4_type),
            stringResource(R.string.table_row4_lawful)
        )

        TableRow(
            stringResource(R.string.table_row5_purpose),
            stringResource(R.string.table_row5_type),
            stringResource(R.string.table_row5_lawful)
        )

        TableRow(
            stringResource(R.string.table_row6_purpose),
            stringResource(R.string.table_row6_type),
            stringResource(R.string.table_row6_lawful)
        )

        TableRow(
            stringResource(R.string.table_row7_purpose),
            stringResource(R.string.table_row7_type),
            stringResource(R.string.table_row7_lawful)
        )

        TableRow(
            stringResource(R.string.table_row8_purpose),
            stringResource(R.string.table_row8_type),
            stringResource(R.string.table_row8_lawful)
        )

        TableRow(
            stringResource(R.string.table_row9_purpose),
            stringResource(R.string.table_row9_type),
            stringResource(R.string.table_row9_lawful)
        )

        TableRow(
            stringResource(R.string.table_row10_purpose),
            stringResource(R.string.table_row10_type),
            stringResource(R.string.table_row10_lawful)
        )

        TableRow(
            stringResource(R.string.table_row11_purpose),
            stringResource(R.string.table_row11_type),
            stringResource(R.string.table_row11_lawful),
            isLast = true
        )
    }
}

@Composable
fun TableRow(
    purpose: String,
    type: String,
    lawful: String,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = purpose,
            fontSize = 11.sp,
            color = Color.Black,
            lineHeight = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = type,
            fontSize = 11.sp,
            color = Color.Black,
            lineHeight = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = lawful,
            fontSize = 11.sp,
            color = Color.Black,
            lineHeight = 14.sp,
            modifier = Modifier.weight(1f)
        )
    }

    if (!isLast) {
        HorizontalDivider(
            color = Color.LightGray.copy(alpha = 0.3f),
            thickness = 0.5.dp
        )
    }
}