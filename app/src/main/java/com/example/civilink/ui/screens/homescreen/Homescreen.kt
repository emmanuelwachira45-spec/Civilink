package com.example.civilink.ui.screens.homescreen

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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextOverflow
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import com.example.civilink.navigation.ROUT_PROFILESCREEN
import com.example.civilink.navigation.ROUT_SETTINGSCREEN
import com.example.civilink.navigation.ROUT_CREATEREPORT
import com.example.civilink.navigation.ROUT_NOTIFICATIONS
import com.example.civilink.navigation.ROUT_EDITREPORT
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.theme.civiPageBg
import com.example.civilink.ui.theme.civiNavBar
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicOrange
import com.example.civilink.ui.theme.CivicTeal
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite
import com.google.firebase.auth.FirebaseAuth


import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.civilink.ui.components.AnimatedCounter
import com.example.civilink.ui.components.AppImages
import com.example.civilink.ui.components.PhotoImage
import com.example.civilink.ui.components.ShimmerReportCard
import com.example.civilink.ui.components.bounceClick
import com.example.civilink.ui.theme.BrightBlue
import com.example.civilink.ui.theme.DeepNavy
import com.example.civilink.ui.theme.StatusGreen
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.text.TextStyle

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
    var userName by remember { mutableStateOf("Citizen") }
    var userProfilePhoto by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentUser?.uid) {
        if (!isPreview && currentUser?.uid != null) {
            FirebaseDatabase.getInstance().getReference("users")
                .child(currentUser.uid).get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        userName = user.fullName.split(" ")[0]
                        userProfilePhoto = user.profileImageUrl
                    }
                }
        }
    }

    // Get reports from ViewModel
    val reports by reportViewModel.reports.collectAsState()
    val isLoading by reportViewModel.isLoading.collectAsState()
    val notificationCount by reportViewModel.notificationCount.collectAsState()

    HomeScreenContent(
        navController = navController,
        reports = reports,
        isLoading = isLoading,
        currentUserUid = currentUser?.uid,
        userName = userName,
        userProfilePhoto = userProfilePhoto,
        notificationCount = notificationCount
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
    currentUserUid: String?,
    userName: String = "Citizen",
    userProfilePhoto: String? = null,
    notificationCount: Int = 0,
    onLoadReports: () -> Unit = {}
) {

    val context = LocalContext.current
    var searchText by remember {
        mutableStateOf("")
    }

    // Time-aware greeting – makes the home screen feel alive
    val greeting = remember {
        when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..20 -> "Good evening"
            else -> "Still up"
        }
    }

    val categories = listOf(
        "Roads" to Icons.Default.Build,
        "Water" to Icons.Default.WaterDrop,
        "Garbage" to Icons.Default.Delete,
        "Electricity" to Icons.Default.ElectricBolt,
    )

    // Real-time notification when a new report is added to the community
    // Initialized to -1 to distinguish between "not yet loaded" and "empty"
    var lastReportCount by remember { mutableStateOf(-1) }
    
    LaunchedEffect(reports.size) {
        // If we previously had reports (count > 0) and the new count is higher, show toast
        if (lastReportCount > 0 && reports.size > lastReportCount) {
            Toast.makeText(context, "New civic issue reported nearby!", Toast.LENGTH_SHORT).show()
        }
        // Update tracking count
        lastReportCount = reports.size
    }

    // Report statistics (still specific to the logged-in user)
    val myReports = reports.filter { it.userId == currentUserUid }
    
    val pendingCount = myReports.count {
        it.status.equals("Pending", ignoreCase = true)
    }

    val progressCount = myReports.count {
        it.status.equals("In Progress", ignoreCase = true)
    }

    val resolvedCount = myReports.count {
        it.status.equals("Resolved", ignoreCase = true)
    }

    // Search filtering (now shows ALL community reports for a connected experience)
    val filteredReports = reports.filter { report ->

        searchText.isBlank() ||
                report.title.contains(searchText, ignoreCase = true) ||
                report.category.contains(searchText, ignoreCase = true) ||
                report.location.contains(searchText, ignoreCase = true) ||
                report.status.contains(searchText, ignoreCase = true)
    }


    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = civiNavBar(),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already here */ },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CivicBlue,
                        selectedTextColor = CivicBlue,
                        unselectedIconColor = CivicGray,
                        unselectedTextColor = CivicGray,
                        indicatorColor = CivicLightBlue
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(ROUT_CREATEREPORT) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Create") },
                    label = { Text("Create", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = CivicGray,
                        unselectedTextColor = CivicGray
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(ROUT_PROFILESCREEN) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = CivicGray,
                        unselectedTextColor = CivicGray
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(ROUT_SETTINGSCREEN) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = CivicGray,
                        unselectedTextColor = CivicGray
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ROUT_CREATEREPORT)
                },
                containerColor = CivicTeal,
                contentColor = CivicWhite,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Report", modifier = Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(civiPageBg())
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
                                text = "$greeting, $userName",
                                color = CivicWhite.copy(alpha = 0.75f),
                                fontSize = 14.sp
                            )

                            Spacer(
                                modifier = Modifier.height(3.dp)
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (userProfilePhoto != null) {
                                    AsyncImage(
                                        model = userProfilePhoto,
                                        contentDescription = "Profile",
                                        modifier = Modifier.size(36.dp).clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    AppLogo(size = 36.dp)
                                }
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
                            if (notificationCount > 0) {
                                Badge {
                                    Text(notificationCount.toString())
                                }
                            }
                        }
                    ) {

                        IconButton(
                            onClick = {
                                navController.navigate(ROUT_NOTIFICATIONS)
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
                    trailingIcon = {
                        // Manual community sync – spins while refreshing
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .size(20.dp),
                                strokeWidth = 2.dp,
                                color = CivicGray
                            )
                        } else {
                            IconButton(
                                onClick = onLoadReports,
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh reports",
                                    tint = CivicGray
                                )
                            }
                        }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceClick(pressedScale = 0.97f) {
                        navController.navigate(ROUT_CREATEREPORT)
                    },
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Cityscape backdrop – tap anywhere to report an issue
                    PhotoImage(
                        url = AppImages.HOME_BANNER,
                        modifier = Modifier.matchParentSize(),
                        fallbackIcon = Icons.Default.Add,
                        fallbackIconSize = 40.dp
                    )

                    // Navy scrim so white overlay text stays crisp
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        DeepNavy.copy(alpha = 0.93f),
                                        BrightBlue.copy(alpha = 0.55f)
                                    )
                                )
                            )
                    )

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
                        icon = category.second,
                        onClick = {
                            searchText = category.first
                        }
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
                    number = pendingCount,
                    label = "Pending",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        searchText = "Pending"
                    }
                )

                ReportStatusCard(
                    number = progressCount,
                    label = "In Progress",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        searchText = "In Progress"
                    }
                )

                ReportStatusCard(
                    number = resolvedCount,
                    label = "Resolved",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        searchText = "Resolved"
                    }
                )
            }


            Spacer(
                modifier = Modifier.height(25.dp)
            )


            // ─────────────────────────────
            // Community Milestone Card
            // ─────────────────────────────

            val totalResolved = reports.count { it.status.equals("Resolved", ignoreCase = true) }
            
            if (totalResolved > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CivicWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(CivicTeal.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = CivicTeal,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Community Impact",
                                color = CivicText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$totalResolved issues resolved in your city!",
                                color = CivicGray,
                                fontSize = 11.sp
                            )
                        }
                        
                        Text(
                            text = "🎊",
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }


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
                    modifier = Modifier.clickable {
                        searchText = ""
                        Toast.makeText(context, "Showing all reports", Toast.LENGTH_SHORT).show()
                    },
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
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(3) { ShimmerReportCard() }
                }
            }


            // No reports
            if (!isLoading && filteredReports.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = CivicGray.copy(alpha = 0.5f),
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (searchText.isEmpty()) "Your community is clear! No issues reported." else "No reports match your search.",
                        color = CivicGray,
                        fontSize = 14.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
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
                    imageUrl = report.imageUrl,
                    isOwner = report.userId == currentUserUid,
                    onEditClick = {
                        navController.navigate("EditReport/${report.id}")
                    },
                    onClick = {
                        navController.navigate("ReportDetail/${report.id}")
                    }
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
}


// ─────────────────────────────────────────────
// Category Card
// ─────────────────────────────────────────────

@Composable
fun CategoryCard(
    name: String,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier.size(
            width = 100.dp,
            height = 105.dp
        ).bounceClick(pressedScale = 0.90f) { onClick() },
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
    number: Int,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = modifier.bounceClick { onClick() },
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

            AnimatedCounter(
                value = number,
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                color = CivicBlue
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
    imageUrl: String? = null,
    isOwner: Boolean = false,
    onEditClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {

    val statusColor = when {
        status.equals("Pending", ignoreCase = true) -> CivicOrange
        status.equals("In Progress", ignoreCase = true) -> CivicBlue
        status.equals("Resolved", ignoreCase = true) -> StatusGreen
        else -> CivicGray
    }

    Card(
        modifier = Modifier.fillMaxWidth().bounceClick { onClick() },
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
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = CivicBlue
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

            if (isOwner && status.equals("Pending", ignoreCase = true)) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = CivicBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Box(
                modifier = Modifier
                    .background(statusColor.copy(alpha = 0.14f), RoundedCornerShape(50))
                    .padding(horizontal = 9.dp, vertical = 4.dp)
            ) {
                Text(
                    text = status,
                    color = statusColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
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
            Report(id = "1", title = "Pothole", category = "Roads", status = "Pending", userId = "test_user", imageUrl = null),
            Report(id = "2", title = "Broken Pipe", category = "Water", status = "In Progress", userId = "test_user", imageUrl = null)
        ),
        isLoading = false,
        currentUserUid = "test_user",
        userName = "Preview",
        notificationCount = 1
    )
}

