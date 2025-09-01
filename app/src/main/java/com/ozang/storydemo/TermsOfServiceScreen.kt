package com.ozang.storydemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.terms_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Black)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Terms of Use Title
            Text(
                text = stringResource(R.string.terms_of_use_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Introduction
            Text(
                text = stringResource(R.string.terms_introduction_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.terms_introduction_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Access to Platform
            Text(
                text = stringResource(R.string.terms_access_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.terms_access_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // What you are allowed to do
            Text(
                text = stringResource(R.string.terms_allowed_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.terms_allowed_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // What you are not allowed to do
            Text(
                text = stringResource(R.string.terms_not_allowed_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.terms_not_allowed_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Intellectual Property Rights
            Text(
                text = stringResource(R.string.terms_ip_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.terms_ip_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Content
            Text(
                text = stringResource(R.string.terms_content_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.terms_content_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Personal Information
            Text(
                text = stringResource(R.string.terms_personal_info_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.terms_personal_info_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // User Content
            Text(
                text = stringResource(R.string.terms_user_content_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.terms_user_content_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Third Party Services
            Text(
                text = stringResource(R.string.terms_third_party_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.terms_third_party_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // General
            Text(
                text = stringResource(R.string.terms_general_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.terms_general_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Contact
            Text(
                text = stringResource(R.string.terms_contact_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.terms_contact_content),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}