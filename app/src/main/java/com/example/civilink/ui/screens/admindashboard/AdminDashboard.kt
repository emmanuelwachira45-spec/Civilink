package com.example.civilink.ui.screens.admindashboard

import android.content.Intent
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civilink.data.Report
import com.example.civilink.data.ReportViewModel
import com.example.civilink.data.User
import com.google.firebase.database.FirebaseDatabase
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
import com.example.civilink.navigation.ROUT_ONBOARDING1
import com.example.civilink.navigation.ROUT_PROFILESCREEN
import androidx.compose.material.icons.filled.Person


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

    val auth = remember(isPreview) { if (isPreview) null else FirebaseAuth.getInstance() }
    val currentUser = auth?.currentUser
    var adminName by remember { mutableStateOf("Admin") }
    var adminPhoto by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentUser?.uid) {
        if (!isPreview && currentUser?.uid != null) {
            FirebaseDatabase.getInstance().getReference("users")
                .child(currentUser.uid).get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        adminName = user.fullName.split(" ")[0]
                        adminPhoto = user.profileImageUrl
                    }
                }
        }
    }

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
        adminName = adminName,
        adminPhoto = adminPhoto,
        onBackClick = {
            navController.popBackStack()
        },
        onLogoutClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate(ROUT_ONBOARDING1) {
                popUpTo(0) { inclusive = true }
            }
        },
        onProfileClick = {
            navController.navigate(ROUT_PROFILESCREEN)
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
        },
        onEditReport = { updated ->
            reportViewModel.updateReport(
                reportId = updated.id,
                title = updated.title,
                category = updated.category,
                location = updated.location,
                description = updated.description,
                status = updated.status,
                onSuccess = {
                    Toast.makeText(context, "Report updated successfully", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(context, "Update failed: $it", Toast.LENGTH_SHORT).show()
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
    adminName: String = "Admin",
    adminPhoto: String? = null,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    onStatusChanged: (String, String) -> Unit,
    onDeleteReport: (String) -> Unit,
    onEditReport: (Report) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }

    // ─────────────────────────────────────
    // Quick-action dialog state
    // ─────────────────────────────────────

    var showMembersDialog by remember { mutableStateOf(false) }
    var showHotspotsDialog by remember { mutableStateOf(false) }

    var members by remember { mutableStateOf<List<User>>(emptyList()) }
    var membersLoading by remember { mutableStateOf(false) }
    var membersError by remember { mutableStateOf<String?>(null) }

    val hotspots = reports
        .filter { it.location.isNotBlank() }
        .groupBy { it.location.trim().lowercase() }
        .map { (location, list) -> location.replaceFirstChar { it.uppercase() } to list.size }
        .sortedByDescending { it.second }
        .take(5)

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

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CivicWhite.copy(alpha = 0.2f))
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                if (adminPhoto != null) {
                    AsyncImage(
                        model = adminPhoto,
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = CivicWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
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
                            text = "Good morning, $adminName",
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
                    },
                    onEdit = onEditReport
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
                    // Real behaviour: lift every filter and dive to the list
                    searchText = ""
                    coroutineScope.launch {
                        scrollState.animateScrollTo(1500)
                    }
                    Toast.makeText(context, "Showing all community reports", Toast.LENGTH_SHORT).show()
                }

                AdminActionItem(
                    icon = Icons.Default.People,
                    title = "View Community Members",
                    subtitle = "Registered citizens, live from the database"
                ) {
                    showMembersDialog = true
                }

                AdminActionItem(
                    icon = Icons.Default.LocationOn,
                    title = "View Hotspots",
                    subtitle = "Top reported locations, ranked"
                ) {
                    showHotspotsDialog = true
                }

                AdminActionItem(
                    icon = Icons.Default.Share,
                    title = "Export summary",
                    subtitle = "Share stats & hotspots with the team"
                ) {
                    val topSpots = hotspots.take(3)
                        .joinToString(", ") { (location, count) -> "$location ($count)" }
                        .ifBlank { "No hotspot data yet" }

                    val summary = buildString {
                        appendLine("CiviLink Admin Summary")
                        appendLine(
                            SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.getDefault())
                                .format(Date())
                        )
                        appendLine()
                        appendLine("Total reports: $totalReports")
                        appendLine("Pending: $pendingReports  •  In Progress: $inProgressReports  •  Resolved: $resolvedReports")
                        appendLine("Active reporters: ${reports.map { it.userId }.distinct().size}")
                        appendLine()
                        appendLine("Top hotspots: $topSpots")
                    }

                    context.startActivity(
                        Intent.createChooser(
                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, summary)
                                putExtra(Intent.EXTRA_TITLE, "CiviLink Admin Summary")
                            },
                            "Share CiviLink summary"
                        )
                    )
                }

                AdminActionItem(
                    icon = Icons.Default.Person,
                    title = "My Account",
                    subtitle = "Manage your profile and settings"
                ) {
                    onProfileClick()
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

        // ─────────────────────────────
        // Quick-action dialogs
        // ─────────────────────────────

        if (showMembersDialog) {

            // Fetch registered citizens live from Firebase
            LaunchedEffect(Unit) {
                membersLoading = true
                membersError = null
                FirebaseDatabase.getInstance().getReference("users")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        members = snapshot.children.mapNotNull { it.getValue(User::class.java) }
                        membersLoading = false
                    }
                    .addOnFailureListener {
                        membersLoading = false
                        membersError = it.message ?: "Could not reach the database"
                    }
            }

            AlertDialog(
                onDismissRequest = { showMembersDialog = false },
                confirmButton = {
                    TextButton(onClick = { showMembersDialog = false }) {
                        Text("Done", color = CivicBlue)
                    }
                },
                title = { Text("Community members") },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 380.dp)
                    ) {
                        when {
                            membersLoading -> {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = CivicBlue)
                                }
                            }

                            membersError != null -> {
                                Text(
                                    text = "Load failed: $membersError",
                                    color = CivicOrange,
                                    fontSize = 12.sp
                                )
                            }

                            members.isEmpty() -> {
                                Text(
                                    text = "No registered members found yet.",
                                    color = CivicGray,
                                    fontSize = 13.sp
                                )
                            }

                            else -> {
                                Text(
                                    text = "${members.size} registered citizen(s)",
                                    color = CivicGray,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Column(
                                    modifier = Modifier.verticalScroll(rememberScrollState())
                                ) {
                                    members.forEach { member ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 7.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Brand letter avatar
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        Brush.linearGradient(
                                                            listOf(CivicBlue, CivicNavy)
                                                        )
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = member.fullName.firstOrNull()?.uppercase() ?: "?",
                                                    color = CivicWhite,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(11.dp))

                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = member.fullName.ifBlank { "Unnamed citizen" },
                                                    color = CivicText,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                Text(
                                                    text = member.email.ifBlank { "No email on file" },
                                                    color = CivicGray,
                                                    fontSize = 11.sp
                                                )
                                            }

                                            Text(
                                                text = member.role.ifBlank { "citizen" },
                                                color = CivicBlue,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                                    .background(
                                                        CivicLightBlue,
                                                        RoundedCornerShape(50)
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }

        if (showHotspotsDialog) {
            AlertDialog(
                onDismissRequest = { showHotspotsDialog = false },
                confirmButton = {
                    TextButton(onClick = { showHotspotsDialog = false }) {
                        Text("Close", color = CivicBlue)
                    }
                },
                title = { Text("Report hotspots") },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 360.dp)
                    ) {
                        if (hotspots.isEmpty()) {
                            Text(
                                text = "No location data yet – hotspots appear once citizens file reports.",
                                color = CivicGray,
                                fontSize = 13.sp
                            )
                        } else {
                            val maxCount = hotspots.first().second.coerceAtLeast(1)
                            val rankColors = listOf(CivicOrange, CivicBlue, CivicTeal, CivicGray, CivicGray)

                            hotspots.forEachIndexed { index, hotspot ->
                                val location = hotspot.first
                                val count = hotspot.second

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Rank badge
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(CircleShape)
                                            .background(rankColors[index].copy(alpha = 0.16f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            color = rankColors[index],
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(11.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = location,
                                            color = CivicText,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        LinearProgressIndicator(
                                            progress = { count.toFloat() / maxCount },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(6.dp)
                                                .clip(CircleShape),
                                            color = rankColors[index],
                                            trackColor = CivicLightBlue
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(11.dp))

                                    Text(
                                        text = "$count",
                                        color = CivicBlue,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
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
    onDelete: () -> Unit,
    onEdit: (Report) -> Unit
) {

    var menuExpanded by remember {
        mutableStateOf(false)
    }

    var showDeleteConfirm by remember {
        mutableStateOf(false)
    }

    var showEditDialog by remember {
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

    // ─────────────────────────────
    // Full edit dialog – content + status in one place
    // ─────────────────────────────

    if (showEditDialog) {

        var eTitle by remember(report.id) { mutableStateOf(report.title) }
        var eLocation by remember(report.id) { mutableStateOf(report.location) }
        var eDescription by remember(report.id) { mutableStateOf(report.description) }
        var eCategory by remember(report.id) { mutableStateOf(report.category) }
        var eStatus by remember(report.id) { mutableStateOf(report.status) }
        var categoryMenu by remember { mutableStateOf(false) }
        var statusMenu by remember { mutableStateOf(false) }

        val categories = listOf("Roads", "Water", "Garbage", "Electricity", "Other")
        val statusOptions = listOf("Pending", "In Progress", "Resolved")
        val isValid = eTitle.trim().length >= 3 &&
                eLocation.isNotBlank() &&
                eDescription.trim().length >= 5

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            confirmButton = {
                TextButton(
                    enabled = isValid,
                    onClick = {
                        onEdit(
                            report.copy(
                                title = eTitle.trim(),
                                category = eCategory,
                                location = eLocation.trim(),
                                description = eDescription.trim(),
                                status = eStatus,
                                lastUpdatedAt = System.currentTimeMillis()
                            )
                        )
                        showEditDialog = false
                    }
                ) {
                    Text("Save changes", color = if (isValid) CivicBlue else CivicGray)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = CivicGray)
                }
            },
            title = { Text("Edit report") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = eTitle,
                        onValueChange = { eTitle = it },
                        label = { Text("Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Category picker
                    Box {
                        OutlinedTextField(
                            value = eCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { categoryMenu = true }
                        )
                        DropdownMenu(
                            expanded = categoryMenu,
                            onDismissRequest = { categoryMenu = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        eCategory = category
                                        categoryMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = eLocation,
                        onValueChange = { eLocation = it },
                        label = { Text("Location") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status picker – lets the admin return a report to
                    // any earlier stage right from the edit sheet
                    Box {
                        OutlinedTextField(
                            value = eStatus,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Status") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { statusMenu = true }
                        )
                        DropdownMenu(
                            expanded = statusMenu,
                            onDismissRequest = { statusMenu = false }
                        ) {
                            statusOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        eStatus = option
                                        statusMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = eDescription,
                        onValueChange = { eDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
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
                    onClick = { showEditDialog = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit report",
                        tint = CivicBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

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
            onProfileClick = {},
            onStatusChanged = { _, _ -> },
            onDeleteReport = {},
            onEditReport = {}
        )
    }
}
