package com.example.civilink.ui.screens.admindashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civilink.data.Report
import com.example.civilink.data.ReportViewModel
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.civiPageBg
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicOrange
import com.example.civilink.ui.theme.CivicTeal
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite
import com.example.civilink.ui.theme.CivilinkTheme
import com.google.firebase.auth.FirebaseAuth
import com.example.civilink.navigation.ROUT_LOGINSCREEN


import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AdminDashboardScreen(
    navController: NavController,
    reportViewModel: ReportViewModel = viewModel()
) {

    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    val reports by reportViewModel.reports.collectAsState()
    val isLoading by reportViewModel.isLoading.collectAsState()
    val error by reportViewModel.error.collectAsState()

    // Real-time synchronization
    LaunchedEffect(Unit) {
        if (!isPreview) {
            reportViewModel.loadReports()
        }
    }

    AdminDashboardContent(
        reports = reports,
        isLoading = isLoading,
        error = error,
        onBackClick = {
            navController.popBackStack()
        },
        onLogoutClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate(ROUT_LOGINSCREEN) {
                popUpTo(0) { inclusive = true }
            }
        },
        onStatusChanged = { reportId, newStatus ->
            reportViewModel.updateReportStatus(
                reportId = reportId,
                newStatus = newStatus,
                onSuccess = {
                    Toast.makeText(context, "Status updated to $newStatus", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(context, "Failed to update: $it", Toast.LENGTH_SHORT).show()
                }
            )
        },
        onDeleteReport = { reportId ->
            reportViewModel.deleteReport(
                reportId = reportId,
                onSuccess = {
                    Toast.makeText(context, "Report deleted successfully", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(context, "Delete failed: $it", Toast.LENGTH_SHORT).show()
                }
            )
        }
    )
}

@Composable
fun AdminDashboardContent(
    reports: List<Report>,
    isLoading: Boolean,
    error: String?,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onStatusChanged: (String, String) -> Unit,
    onDeleteReport: (String) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }

    // ─────────────────────────────────────
    // Statistics
    // ─────────────────────────────────────

    val filteredReports = reports.filter { report ->
        report.title.contains(searchText, ignoreCase = true) ||
                report.category.contains(searchText, ignoreCase = true) ||
                report.location.contains(searchText, ignoreCase = true)
    }

    val totalReports = reports.size
    val pendingReports = reports.count { it.status.equals("Pending", ignoreCase = true) }
    val inProgressReports = reports.count { it.status.equals("In Progress", ignoreCase = true) }
    val resolvedReports = reports.count { it.status.equals("Resolved", ignoreCase = true) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(civiPageBg())
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
                onClick = onBackClick
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = CivicWhite
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Admin Dashboard",
                    color = CivicWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "CiviLink Management",
                    color = CivicWhite.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }

            IconButton(
                onClick = onLogoutClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = CivicWhite
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            AppLogo(size = 36.dp)
        }


        // ─────────────────────────────
        // Dashboard Content
        // ─────────────────────────────

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(18.dp)
        ) {

            // Welcome Card

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicNavy
                )
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    CivicNavy,
                                    CivicBlue
                                )
                            )
                        )
                        .padding(20.dp)
                ) {

                    Column {

                        Text(
                            text = "Good morning, Admin",
                            color = CivicWhite,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "Monitor and manage citizen reports.",
                            color = CivicWhite.copy(alpha = 0.75f),
                            fontSize = 12.sp
                        )
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // Search Bar for Admin
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search all reports...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CivicWhite,
                    unfocusedContainerColor = CivicWhite,
                    focusedBorderColor = CivicTeal,
                    unfocusedBorderColor = CivicWhite.copy(alpha = 0.5f)
                )
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // ─────────────────────────────
            // Overview
            // ─────────────────────────────

            Text(
                text = "Overview",
                color = CivicText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                AdminStat(
                    icon = Icons.AutoMirrored.Filled.Assignment,
                    number = totalReports.toString(),
                    label = "Reports",
                    modifier = Modifier.weight(1f)
                )

                AdminStat(
                    icon = Icons.Default.Pending,
                    number = pendingReports.toString(),
                    label = "Pending",
                    modifier = Modifier.weight(1f)
                )

                AdminStat(
                    icon = Icons.Default.CheckCircle,
                    number = resolvedReports.toString(),
                    label = "Resolved",
                    modifier = Modifier.weight(1f)
                )
            }


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // ─────────────────────────────
            // In Progress
            // ─────────────────────────────

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(CivicLightBlue),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = CivicBlue
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Reports in progress",
                            color = CivicText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = "Currently being handled",
                            color = CivicGray,
                            fontSize = 11.sp
                        )
                    }

                    Text(
                        text = inProgressReports.toString(),
                        color = CivicBlue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // ─────────────────────────────
            // Reports
            // ─────────────────────────────

            Text(
                text = "Recent reports",
                color = CivicText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )


            // Loading

            if (isLoading) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = CivicBlue
                    )
                }
            }


            // Error

            if (error != null) {

                Text(
                    text = "Error: $error",
                    color = CivicOrange,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(
                        vertical = 10.dp
                    )
                )
            }


            // No reports

            if (!isLoading && filteredReports.isEmpty()) {

                Text(
                    text = if (reports.isEmpty()) "No reports have been submitted yet." else "No reports match your search.",
                    color = CivicGray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(
                        vertical = 15.dp
                    )
                )
            }


            // Real Firebase reports

            filteredReports.forEach { report ->

                AdminReportCard(
                    report = report,
                    onStatusChanged = { newStatus ->
                        onStatusChanged(report.id, newStatus)
                    },
                    onDelete = {
                        onDeleteReport(report.id)
                    }
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // ─────────────────────────────
            // Quick Actions
            // ─────────────────────────────

            Text(
                text = "Quick actions",
                color = CivicText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                )
            ) {

                AdminActionItem(
                    icon = Icons.AutoMirrored.Filled.Assignment,
                    title = "Manage all reports",
                    subtitle = "View and update citizen reports"
                ) {
                    coroutineScope.launch {
                        // Scroll to the "Recent reports" section roughly
                        scrollState.animateScrollTo(1000)
                    }
                    Toast.makeText(context, "Showing all community reports", Toast.LENGTH_SHORT).show()
                }

                AdminActionItem(
                    icon = Icons.Default.People,
                    title = "View Community Members",
                    subtitle = "Citizens registered in the system"
                ) {
                    Toast.makeText(context, "Members: ${reports.map { it.userId }.distinct().size} active reporters", Toast.LENGTH_LONG).show()
                }

                AdminActionItem(
                    icon = Icons.Default.LocationOn,
                    title = "View Hotspots",
                    subtitle = "Most reported locations"
                ) {
                    val topLocation = reports.groupBy { it.location }.maxByOrNull { it.value.size }?.key ?: "None"
                    Toast.makeText(context, "Most active area: $topLocation", Toast.LENGTH_LONG).show()
                }
            }


            Spacer(
                modifier = Modifier.height(25.dp)
            )


            Text(
                text = "CiviLink Admin",
                modifier = Modifier.fillMaxWidth(),
                color = CivicBlue,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Connect. Report. Improve.",
                modifier = Modifier.fillMaxWidth(),
                color = CivicGray,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}



// ─────────────────────────────────────────────
// Admin Statistic
// ─────────────────────────────────────────────

@Composable
fun AdminStat(
    icon: ImageVector,
    number: String,
    label: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = CivicWhite
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CivicBlue,
                modifier = Modifier.size(20.dp)
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = number,
                color = CivicText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = label,
                color = CivicGray,
                fontSize = 10.sp
            )
        }
    }
}


// ─────────────────────────────────────────────
// Admin Report Card
// ─────────────────────────────────────────────

@Composable
fun AdminReportCard(
    report: Report,
    onStatusChanged: (String) -> Unit,
    onDelete: () -> Unit
) {

    var menuExpanded by remember {
        mutableStateOf(false)
    }

    var showDeleteConfirm by remember {
        mutableStateOf(false)
    }

    if (showDeleteConfirm) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete Report?") },
            text = { Text("This action cannot be undone. Are you sure you want to remove this report?") }
        )
    }

    val statusColor = when {

        report.status.equals(
            "Pending",
            ignoreCase = true
        ) -> CivicOrange

        report.status.equals(
            "In Progress",
            ignoreCase = true
        ) -> CivicBlue

        report.status.equals(
            "Resolved",
            ignoreCase = true
        ) -> CivicTeal

        else -> CivicGray
    }


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = CivicWhite
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CivicLightBlue),
                    contentAlignment = Alignment.Center
                ) {
                    if (report.imageUrl != null) {
                        AsyncImage(
                            model = report.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = when {
                                report.category.contains("Water", ignoreCase = true) -> Icons.Default.WaterDrop
                                report.category.contains("Electricity", ignoreCase = true) -> Icons.Default.ElectricBolt
                                report.category.contains("Road", ignoreCase = true) -> Icons.Default.Build
                                report.category.contains("Waste", ignoreCase = true) -> Icons.Default.Delete
                                else -> Icons.Default.Report
                            },
                            contentDescription = null,
                            tint = CivicBlue,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.width(12.dp)
                )


                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = report.title,
                        color = CivicText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = report.location,
                        color = CivicGray,
                        fontSize = 11.sp
                    )
                }


                Text(
                    text = report.status,
                    color = statusColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = CivicOrange,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            Text(
                text = report.description,
                color = CivicGray,
                fontSize = 12.sp,
                maxLines = 2
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            Box {

                Button(
                    onClick = {
                        menuExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CivicLightBlue,
                        contentColor = CivicBlue
                    )
                ) {

                    Text(
                        text = "Change Status"
                    )
                }


                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = {
                        menuExpanded = false
                    }
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("Pending")
                        },
                        onClick = {

                            menuExpanded = false

                            onStatusChanged(
                                "Pending"
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text("In Progress")
                        },
                        onClick = {

                            menuExpanded = false

                            onStatusChanged(
                                "In Progress"
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text("Resolved")
                        },
                        onClick = {

                            menuExpanded = false

                            onStatusChanged(
                                "Resolved"
                            )
                        }
                    )
                }
            }
        }
    }
}


// ─────────────────────────────────────────────
// Admin Action Item
// ─────────────────────────────────────────────

@Composable
fun AdminActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 18.dp,
                vertical = 15.dp
            ),
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

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = CivicText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = subtitle,
                color = CivicGray,
                fontSize = 11.sp
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = CivicGray
        )
    }
}


// ─────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun AdminDashboardScreenPreview() {
    CivilinkTheme {
        AdminDashboardContent(
            reports = listOf(
                Report(
                    id = "1",
                    title = "Pothole on Main St",
                    category = "Road",
                    location = "Main St, City Center",
                    description = "Large pothole causing traffic delays.",
                    status = "Pending",
                    imageUrl = null
                ),
                Report(
                    id = "2",
                    title = "Water Leak",
                    category = "Water",
                    location = "2nd Ave, North District",
                    description = "Water main burst near the park.",
                    status = "In Progress",
                    imageUrl = null
                ),
                Report(
                    id = "3",
                    title = "Street Light Out",
                    category = "Electricity",
                    location = "Park Rd, South Side",
                    description = "The light at the corner is flickering.",
                    status = "Resolved",
                    imageUrl = null
                )
            ),
            isLoading = false,
            error = null,
            onBackClick = {},
            onLogoutClick = {},
            onStatusChanged = { _, _ -> },
            onDeleteReport = {}
        )
    }
}
