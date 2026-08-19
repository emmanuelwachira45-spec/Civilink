package com.example.civilink.ui.screens.homescreen

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.example.civilink.data.Report
import com.example.civilink.data.ReportViewModel
import com.example.civilink.navigation.ROUT_CREATEREPORT
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicTeal
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite
import com.google.firebase.auth.FirebaseAuth


@Composable
fun HomeScreen(
    navController: NavController,
    reportViewModel: ReportViewModel = viewModel(),
) {
    val isPreview = LocalInspectionMode.current

    val auth = remember(isPreview) {
        if (isPreview) null else FirebaseAuth.getInstance()
    }

    val currentUser = auth?.currentUser

    // Get reports from ViewModel
    val reports by reportViewModel.reports.collectAsState()
    val isLoading by reportViewModel.isLoading.collectAsState()
    val error by reportViewModel.error.collectAsState()

    HomeScreenContent(
        navController = navController,
        reports = reports,
        isLoading = isLoading,
        error = error,
        currentUserUid = currentUser?.uid,
    ) {
        if (!isPreview) {
            reportViewModel.loadReports()
        }
    }
}

@Composable
fun HomeScreenContent(
    navController: NavController,
    reports: List<Report>,
    isLoading: Boolean,
    error: String?,
    currentUserUid: String?,
    onLoadReports: () -> Unit = {}
) {

    var searchText by remember {
        mutableStateOf("")
    }

    val categories = listOf(
        "Roads" to Icons.Default.Build,
        "Water" to Icons.Default.WaterDrop,
        "Garbage" to Icons.Default.Delete,
        "Electricity" to Icons.Default.ElectricBolt,
    )

    // Load reports
    LaunchedEffect(Unit) {
        onLoadReports()
    }

    // Filter reports for current user
    val myReports = reports.filter {
        it.userId == currentUserUid
    }

    // Report statistics
    val pendingCount = myReports.count {
        it.status.equals("Pending", ignoreCase = true)
    }

    val progressCount = myReports.count {
        it.status.equals("In Progress", ignoreCase = true)
    }

    val resolvedCount = myReports.count {
        it.status.equals("Resolved", ignoreCase = true)
    }

    // Search filtering
    val filteredReports = myReports.filter { report ->

        searchText.isBlank() ||
                report.title.contains(searchText, ignoreCase = true) ||
                report.category.contains(searchText, ignoreCase = true) ||
                report.location.contains(searchText, ignoreCase = true)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CivicLightBlue)
    ) {

        // ─────────────────────────────
        // Top Header
        // ─────────────────────────────

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            CivicNavy,
                            CivicBlue
                        )
                    )
                )
                .padding(
                    start = 22.dp,
                    end = 22.dp,
                    top = 22.dp,
                    bottom = 28.dp
                )
        ) {

            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {

                        Text(
                            text = "Good evening ",
                            color = CivicWhite.copy(alpha = 0.75f),
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AppLogo(size = 36.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "CiviLink",
                                color = CivicWhite,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    BadgedBox(
                        badge = {
                            Badge {
                                Text("3")
                            }
                        }
                    ) {

                        IconButton(
                            onClick = {
                                // Notifications
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = CivicWhite
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // Search
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Search community issues..."
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CivicWhite,
                        unfocusedContainerColor = CivicWhite,
                        focusedBorderColor = CivicTeal,
                        unfocusedBorderColor = CivicWhite.copy(alpha = 0.5f)
                    )
                )
            }
        }


        // ─────────────────────────────
        // Main Content
        // ─────────────────────────────

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 18.dp,
                    vertical = 18.dp
                )
        ) {

            // ─────────────────────────────
            // Report Issue Card
            // ─────────────────────────────

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicBlue
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                CivicWhite.copy(alpha = 0.18f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = CivicWhite,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(15.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "See something wrong?",
                            color = CivicWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = "Report a civic issue in your area.",
                            color = CivicWhite.copy(alpha = 0.78f),
                            fontSize = 12.sp
                        )
                    }

                    FloatingActionButton(
                        onClick = {
                            navController.navigate(ROUT_CREATEREPORT)
                        },
                        modifier = Modifier.size(48.dp),
                        containerColor = CivicTeal,
                        contentColor = CivicWhite
                    ) {

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Report Issue"
                        )
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            // ─────────────────────────────
            // Categories
            // ─────────────────────────────

            Text(
                text = "Report by category",
                color = CivicText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(categories) { category ->

                    CategoryCard(
                        name = category.first,
                        icon = category.second
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(25.dp)
            )


            // ─────────────────────────────
            // Report Summary
            // ─────────────────────────────

            Text(
                text = "My reports",
                color = CivicText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                ReportStatusCard(
                    number = pendingCount.toString(),
                    label = "Pending",
                    modifier = Modifier.weight(1f)
                )

                ReportStatusCard(
                    number = progressCount.toString(),
                    label = "In Progress",
                    modifier = Modifier.weight(1f)
                )

                ReportStatusCard(
                    number = resolvedCount.toString(),
                    label = "Resolved",
                    modifier = Modifier.weight(1f)
                )
            }


            Spacer(
                modifier = Modifier.height(25.dp)
            )


            // ─────────────────────────────
            // Recent Reports
            // ─────────────────────────────

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Recent reports",
                    color = CivicText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "View all",
                    color = CivicBlue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )


            // Loading state
            if (isLoading) {

                Text(
                    text = "Loading reports...",
                    color = CivicGray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 15.dp)
                )
            }


            // Error state
            if (error != null) {
                Text(
                    text = "Error: $error",
                    color = CivicGray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 15.dp),
                )
            }


            // No reports
            if (!isLoading && error == null && filteredReports.isEmpty()) {
                Text(
                    text = "You haven't submitted any reports yet.",
                    color = CivicGray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 15.dp),
                )
            }


            // Display reports
            filteredReports.forEach { report ->

                Spacer(
                    modifier = Modifier.height(10.dp),
                )

                RecentReportCard(
                    title = report.title,
                    category = report.category,
                    status = report.status,
                )
            }


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            // ─────────────────────────────
            // Community Message
            // ─────────────────────────────

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                )
            ) {

                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = CivicTeal,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Column {

                        Text(
                            text = "Your voice matters",
                            color = CivicText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = "Together we can build a better community.",
                            color = CivicGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(80.dp)
            )
        }
    }
}


// ─────────────────────────────────────────────
// Category Card
// ─────────────────────────────────────────────

@Composable
fun CategoryCard(
    name: String,
    icon: ImageVector,
) {

    Card(
        modifier = Modifier.size(
            width = 100.dp,
            height = 105.dp
        ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = CivicWhite
        )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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
                    contentDescription = name,
                    tint = CivicBlue,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = name,
                color = CivicText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


// ─────────────────────────────────────────────
// Report Status Card
// ─────────────────────────────────────────────

@Composable
fun ReportStatusCard(
    number: String,
    label: String,
    modifier: Modifier = Modifier,
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

            Text(
                text = number,
                color = CivicBlue,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(3.dp)
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
// Recent Report Card
// ─────────────────────────────────────────────

@Composable
fun RecentReportCard(
    title: String,
    category: String,
    status: String,
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = CivicWhite
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(CivicLightBlue),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = CivicBlue
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
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = category,
                    color = CivicGray,
                    fontSize = 11.sp
                )
            }

            Text(
                text = status,
                color = CivicTeal,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// ─────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        navController = rememberNavController(),
        reports = listOf(
            Report(id = "1", title = "Pothole", category = "Roads", status = "Pending", userId = "test_user"),
            Report(id = "2", title = "Broken Pipe", category = "Water", status = "In Progress", userId = "test_user")
        ),
        isLoading = false,
        error = null,
        currentUserUid = "test_user"
    )
}
