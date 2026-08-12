package com.example.civilink.ui.screens.reportdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicTeal
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite

@Composable
fun ReportDetailScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CivicLightBlue)
    ) {

        // ─────────────────────────────
        // Top Bar
        // ─────────────────────────────

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CivicNavy)
                .padding(
                    start = 8.dp,
                    end = 20.dp,
                    top = 18.dp,
                    bottom = 18.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    navController.popBackStack()
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = CivicWhite,
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "Report Details",
                color = CivicWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            AppLogo(size = 32.dp)
        }

        // ─────────────────────────────
        // Content
        // ─────────────────────────────

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(18.dp)
        ) {

            // Report Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(
                        RoundedCornerShape(22.dp),
                    )
                    .background(
                        brushPlaceholder(),
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Report,
                    contentDescription = "Report image",
                    tint = CivicWhite,
                    modifier = Modifier.size(55.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // Title
            Text(
                text = "Large Pothole on Main Road",
                color = CivicText,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // Report ID
            Text(
                text = "Report #CVL-2026-00124",
                color = CivicGray,
                fontSize = 12.sp
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {

                            Text(
                                text = "Current status",
                                color = CivicGray,
                                fontSize = 12.sp
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "In Progress",
                                color = CivicBlue,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(
                                    CivicLightBlue
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Pending,
                                contentDescription = null,
                                tint = CivicBlue
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    LinearProgressIndicator(
                        progress = { 0.65f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(CircleShape),
                        color = CivicTeal,
                        trackColor = CivicLightBlue
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Your report is being handled by the relevant department.",
                        color = CivicGray,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // Description
            ReportSectionTitle(
                title = "Description"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                )
            ) {

                Text(
                    text = "There is a large pothole on the main road that is making it difficult for vehicles and pedestrians to pass safely.",
                    modifier = Modifier.padding(18.dp),
                    color = CivicText,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // Report Information
            ReportSectionTitle(
                title = "Report information"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    ReportInfoRow(
                        icon = Icons.Default.LocationOn,
                        title = "Location",
                        value = "Main Road, Kajiado"
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    ReportInfoRow(
                        icon = Icons.Default.Report,
                        title = "Category",
                        value = "Roads"
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    ReportInfoRow(
                        icon = Icons.Default.CalendarToday,
                        title = "Reported",
                        value = "11 August 2026"
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // Progress Timeline
            ReportSectionTitle(
                title = "Progress"
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    TimelineItem(
                        title = "Report submitted",
                        description = "Your report was received.",
                        completed = true
                    )

                    TimelineItem(
                        title = "Report reviewed",
                        description = "The issue was verified.",
                        completed = true
                    )

                    TimelineItem(
                        title = "Work in progress",
                        description = "The relevant team is handling the issue.",
                        completed = true
                    )

                    TimelineItem(
                        title = "Issue resolved",
                        description = "Waiting for completion.",
                        completed = false
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(25.dp)
            )
        }
    }
}


// ─────────────────────────────────────────────
// Section Title
// ─────────────────────────────────────────────

@Composable
fun ReportSectionTitle(
    title: String,
) {

    Text(
        text = title,
        color = CivicText,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold
    )
}


// ─────────────────────────────────────────────
// Report Information Row
// ─────────────────────────────────────────────

@Composable
fun ReportInfoRow(
    icon: ImageVector,
    title: String,
    value: String,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(CivicLightBlue),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CivicBlue,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column {

            Text(
                text = title,
                color = CivicGray,
                fontSize = 11.sp
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = value,
                color = CivicText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


// ─────────────────────────────────────────────
// Timeline Item
// ─────────────────────────────────────────────

@Composable
fun TimelineItem(
    title: String,
    description: String,
    completed: Boolean,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 18.dp),
        verticalAlignment = Alignment.Top
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    if (completed) {
                        CivicTeal
                    } else {
                        CivicLightBlue
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = if (completed) {
                    Icons.Default.CheckCircle
                } else {
                    Icons.Default.Schedule
                },
                contentDescription = null,
                tint = if (completed) {
                    CivicWhite
                } else {
                    CivicGray
                },
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column {

            Text(
                text = title,
                color = CivicText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = description,
                color = CivicGray,
                fontSize = 11.sp
            )
        }
    }
}


// ─────────────────────────────────────────────
// Image Placeholder
// ─────────────────────────────────────────────

fun brushPlaceholder(): Brush {

    return Brush.linearGradient(
        colors = listOf(
            CivicNavy,
            CivicBlue,
        ),
    )
}


@Preview(showBackground = true)
@Composable
fun ReportDetailScreenPreview() {

    ReportDetailScreen(
        navController = rememberNavController()
    )
}