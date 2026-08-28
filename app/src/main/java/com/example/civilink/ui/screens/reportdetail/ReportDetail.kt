package com.example.civilink.ui.screens.reportdetail

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.civilink.data.Report
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicOrange
import com.example.civilink.ui.theme.CivicTeal
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite
import com.example.civilink.ui.theme.StatusGreen
import com.example.civilink.ui.theme.civiPageBg
import com.example.civilink.navigation.ROUT_EDITREPORT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.material.icons.filled.Edit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReportDetailScreen(navController: NavController, reportId: String) {
    val context = LocalContext.current
    var report by remember { mutableStateOf<Report?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    val currentUser = try { FirebaseAuth.getInstance().currentUser } catch (_: Exception) { null }

    LaunchedEffect(reportId) {
        FirebaseDatabase.getInstance().getReference("reports").child(reportId).get()
            .addOnSuccessListener { snapshot ->
                report = snapshot.getValue(Report::class.java)
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
                Toast.makeText(context, "Failed to load report details", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(civiPageBg())
    ) {

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CivicNavy)
                .padding(start = 8.dp, end = 20.dp, top = 18.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = CivicWhite,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Report Details",
                color = CivicWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            if (report?.userId == currentUser?.uid && report?.status == "Pending") {
                IconButton(onClick = { navController.navigate("EditReport/${reportId}") }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = CivicWhite
                    )
                }
            }

            AppLogo(size = 32.dp)
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = CivicBlue)
            }
        } else if (report == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Report not found", color = CivicGray)
            }
        } else {
            val currentReport = report!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {

                // Report Image Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(brushPlaceholder()),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentReport.imageUrl != null) {
                        AsyncImage(
                            model = currentReport.imageUrl,
                            contentDescription = "Report image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Report,
                            contentDescription = "Report image",
                            tint = CivicWhite,
                            modifier = Modifier.size(55.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Title
                Text(
                    text = currentReport.title,
                    color = CivicText,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Report ID
                Text(
                    text = "Report ID: ${currentReport.id}",
                    color = CivicGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Status Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CivicWhite)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Current status", color = CivicGray, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                val statusColor = when(currentReport.status) {
                                    "Pending" -> CivicOrange
                                    "In Progress" -> CivicBlue
                                    "Resolved" -> CivicTeal
                                    else -> CivicGray
                                }
                                
                                Text(
                                    text = currentReport.status,
                                    color = statusColor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(CivicLightBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (currentReport.status == "Resolved") Icons.Default.CheckCircle else Icons.Default.Pending,
                                    contentDescription = null,
                                    tint = CivicBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        val progress = when(currentReport.status) {
                            "Pending" -> 0.2f
                            "In Progress" -> 0.6f
                            "Resolved" -> 1.0f
                            else -> 0f
                        }

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(7.dp).clip(CircleShape),
                            color = CivicTeal,
                            trackColor = CivicLightBlue
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = getStatusDescription(currentReport.status),
                            color = CivicGray,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Progress timeline – three friendly milestones
                val stage = when(currentReport.status) {
                    "Resolved" -> 2
                    "In Progress" -> 1
                    else -> 0
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CivicWhite)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Progress timeline",
                            color = CivicText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ReportStepItem(Icons.Default.Inbox, "Received", stepAccent(0), stage >= 0)
                            ReportStepItem(Icons.Default.Engineering, "In repair", stepAccent(1), stage >= 1)
                            ReportStepItem(Icons.Default.TaskAlt, "Fixed", stepAccent(2), stage >= 2)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                ReportSectionTitle(title = "Description")
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CivicWhite)
                ) {
                    Text(
                        text = currentReport.description,
                        modifier = Modifier.padding(18.dp),
                        color = CivicText,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                ReportSectionTitle(title = "Report information")
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CivicWhite)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        ReportInfoRow(Icons.Default.LocationOn, "Location", currentReport.location)
                        Spacer(modifier = Modifier.height(16.dp))
                        ReportInfoRow(Icons.Default.Report, "Category", currentReport.category)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(currentReport.timestamp))
                        ReportInfoRow(Icons.Default.CalendarToday, "Reported", date)
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
}

fun getStatusDescription(status: String): String {
    return when(status) {
        "Pending" -> "Your report has been received and is waiting for review."
        "In Progress" -> "The relevant department is currently handling this issue."
        "Resolved" -> "This issue has been successfully resolved. Thank you for your report!"
        else -> "Report status is being updated."
    }
}

@Composable
fun ReportSectionTitle(title: String) {
    Text(text = title, color = CivicText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun ReportInfoRow(icon: ImageVector, title: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(42.dp).clip(CircleShape).background(CivicLightBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(21.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, color = CivicGray, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = CivicText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

/**
 * Accent colour for each milestone of a report's journey.
 */
fun stepAccent(step: Int): Color = when(step) {
    0 -> CivicOrange
    1 -> CivicBlue
    else -> StatusGreen
}

@Composable
fun ReportStepItem(icon: ImageVector, label: String, accent: Color, reached: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (reached) accent else CivicLightBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (reached) CivicWhite else CivicGray,
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            color = if (reached) CivicText else CivicGray,
            fontSize = 10.sp,
            fontWeight = if (reached) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun brushPlaceholder(): Brush = Brush.linearGradient(colors = listOf(CivicNavy, CivicBlue))

@Preview(showBackground = true)
@Composable
fun ReportDetailScreenPreview() {
    ReportDetailScreen(navController = rememberNavController(), "test_id")
}
