package com.example.civilink.ui.screens.notifications

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.civilink.data.ReportViewModel
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.civiPageBg
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicOrange
import com.example.civilink.ui.theme.StatusGreen
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite
import com.example.civilink.ui.theme.DeepNavy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.ui.graphics.Brush
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Notification(
    val reportId: String = "",
    val title: String,
    val message: String,
    val time: String,
    val icon: ImageVector,
    val status: String = "",
    val verificationStatus: String? = null,
    val accent: Color = CivicBlue
)

@Composable
fun NotificationScreen(
    navController: NavController,
    reportViewModel: ReportViewModel = viewModel()
) {
    val context = LocalContext.current
    val reports by reportViewModel.reports.collectAsState()
    
    val currentUser = try {
        FirebaseAuth.getInstance().currentUser
    } catch (_: Exception) {
        null
    }

    val notifications = reports
        .filter { it.userId == currentUser?.uid }
        .sortedByDescending { maxOf(it.lastUpdatedAt, it.timestamp) }
        .map { report ->
            val activityTime = maxOf(report.lastUpdatedAt, report.timestamp)
            Notification(
                reportId = report.id,
                title = when {
                    report.status.equals("Pending", ignoreCase = true) -> "Report received"
                    report.status.equals("In Progress", ignoreCase = true) -> "Crews are on it"
                    else -> "Issue resolved"
                },
                message = "'${report.title}' — ${statusSentence(report.status)}",
                time = relativeTime(activityTime),
                icon = when {
                    report.status.equals("Pending", ignoreCase = true) -> Icons.Default.Pending
                    report.status.equals("In Progress", ignoreCase = true) -> Icons.Default.Engineering
                    else -> Icons.Default.TaskAlt
                },
                status = report.status,
                verificationStatus = report.verificationStatus,
                accent = statusAccent(report.status)
            )
        }

    NotificationScreenContent(
        notifications = notifications,
        onBackClick = { navController.popBackStack() },
        onVerify = { id, isTrue ->
            reportViewModel.verifyReportResolution(
                reportId = id,
                isTrue = isTrue,
                onSuccess = {
                    val msg = if (isTrue) "Thank you for verifying!" else "Understood. We'll look into it."
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
                }
            )
        }
    )
}

@Composable
fun NotificationScreenContent(
    notifications: List<Notification>,
    onBackClick: () -> Unit,
    onVerify: (String, Boolean) -> Unit = { _, _ -> }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(civiPageBg())
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(DeepNavy, CivicBlue)))
                .padding(horizontal = 8.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = CivicWhite
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Notifications",
                color = CivicWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            AppLogo(size = 32.dp)
        }

        if (notifications.isEmpty()) {
            EmptyNotifications()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            ) {
                items(notifications) { notification ->
                    NotificationItem(
                        notification = notification,
                        onVerify = { isTrue -> onVerify(notification.reportId, isTrue) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun EmptyNotifications() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(CivicLightBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = null,
                tint = CivicBlue,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "You're all caught up!",
            color = CivicText,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Every report you file appears here with live status updates.",
            color = CivicGray,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
        )
    }
}

private fun statusAccent(status: String): Color = when {
    status.equals("Pending", ignoreCase = true) -> CivicOrange
    status.equals("In Progress", ignoreCase = true) -> CivicBlue
    status.equals("Resolved", ignoreCase = true) -> StatusGreen
    else -> CivicGray
}

private fun statusSentence(status: String): String = when {
    status.equals("Pending", ignoreCase = true) -> "received and awaiting review"
    status.equals("In Progress", ignoreCase = true) -> "currently being handled by field crews"
    status.equals("Resolved", ignoreCase = true) -> "has been marked as resolved. Is this correct?"
    else -> "status is being updated"
}

private fun relativeTime(millis: Long): String {
    if (millis <= 0L) return "—"
    val diff = System.currentTimeMillis() - millis
    val minutes = diff / 60000
    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m ago"
        minutes < 1440 -> "${minutes / 60}h ago"
        minutes < 10080 -> "${minutes / 1440}d ago"
        else -> SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(millis))
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    onVerify: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CivicWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(notification.accent.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = notification.icon,
                        contentDescription = null,
                        tint = notification.accent,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.title,
                        color = CivicText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = notification.message,
                        color = CivicGray,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
                Text(
                    text = notification.time,
                    color = CivicGray,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.Top)
                )
            }

            // Reaction UI for Resolved reports
            if (notification.status.equals("Resolved", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(12.dp))
                
                if (notification.verificationStatus == null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { onVerify(false) },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                        ) {
                            Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("False", fontSize = 12.sp)
                        }
                        Spacer(Modifier.width(8.dp))
                        TextButton(
                            onClick = { onVerify(true) },
                            colors = ButtonDefaults.textButtonColors(contentColor = StatusGreen)
                        ) {
                            Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("True", fontSize = 12.sp)
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        val isVerified = notification.verificationStatus == "Verified"
                        Icon(
                            imageVector = if (isVerified) Icons.Default.Verified else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (isVerified) StatusGreen else CivicOrange,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = if (isVerified) "Resolution Verified" else "Resolution Disputed",
                            color = if (isVerified) StatusGreen else CivicOrange,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NotificationScreenContent(
        notifications = listOf(
            Notification(
                title = "Crews are on it",
                message = "'Broken Pipe' — currently being handled by field crews",
                time = "2m ago",
                icon = Icons.Default.Engineering,
                status = "In Progress"
            ),
            Notification(
                title = "Issue resolved",
                message = "'Pothole' — has been marked as resolved. Is this correct?",
                time = "1h ago",
                icon = Icons.Default.TaskAlt,
                status = "Resolved"
            )
        ),
        onBackClick = {}
    )
}
