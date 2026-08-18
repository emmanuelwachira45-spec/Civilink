package com.example.civilink.ui.screens.createreport

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Report
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CreateReportScreen(
    navController: NavController,
    reportViewModel: ReportViewModel = viewModel()
) {

    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    val auth = remember(isPreview) {
        if (isPreview) {
            null
        } else {
            FirebaseAuth.getInstance()
        }
    }

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("General")
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }

    val isLoading by reportViewModel.isLoading.collectAsState()

    val categories = listOf(
        "Water Issue" to Icons.Default.WaterDrop,
        "Electricity" to Icons.Default.ElectricBolt,
        "Road Damage" to Icons.Default.Build,
        "Waste/Trash" to Icons.Default.Delete,
        "General" to Icons.Default.Report
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        CivicNavy,
                        CivicBlue
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(24.dp)
        ) {

            // ─────────────────────────────
            // Header
            // ─────────────────────────────

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.background(
                        CivicWhite.copy(alpha = 0.2f),
                        RoundedCornerShape(12.dp)
                    )
                ) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = CivicWhite
                    )
                }

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                Text(
                    text = "Submit Report",
                    color = CivicWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                AppLogo(
                    size = 32.dp
                )
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )


            // ─────────────────────────────
            // Report Form
            // ─────────────────────────────

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                ),
                elevation = CardDefaults.cardElevation(
                    8.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        text = "Report Details",
                        color = CivicText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    // Title

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Issue Title")
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CivicBlue,
                            focusedLabelColor = CivicBlue
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    // Category

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text("Category")
                            },
                            trailingIcon = {

                                IconButton(
                                    onClick = {
                                        isExpanded = !isExpanded
                                    }
                                ) {

                                    Icon(
                                        imageVector =
                                            categories.find {
                                                it.first == category
                                            }?.second
                                                ?: Icons.Default.Report,
                                        contentDescription = null,
                                        tint = CivicBlue
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CivicBlue,
                                focusedLabelColor = CivicBlue
                            )
                        )

                        DropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = {
                                isExpanded = false
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .background(CivicWhite)
                        ) {

                            categories.forEach { (name, icon) ->

                                DropdownMenuItem(
                                    text = {

                                        Row(
                                            verticalAlignment =
                                                Alignment.CenterVertically
                                        ) {

                                            Icon(
                                                imageVector = icon,
                                                contentDescription = null,
                                                tint = CivicBlue,
                                                modifier = Modifier.size(20.dp)
                                            )

                                            Spacer(
                                                modifier = Modifier.width(12.dp)
                                            )

                                            Text(name)
                                        }
                                    },
                                    onClick = {

                                        category = name
                                        isExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    // Location

                    OutlinedTextField(
                        value = location,
                        onValueChange = {
                            location = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Location")
                        },
                        leadingIcon = {

                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = CivicBlue
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CivicBlue,
                            focusedLabelColor = CivicBlue
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    // Description

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        label = {
                            Text("Describe the issue...")
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CivicBlue,
                            focusedLabelColor = CivicBlue
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )


                    // ─────────────────────────
                    // Submit
                    // ─────────────────────────

                    Button(
                        onClick = {

                            if (
                                title.isBlank() ||
                                description.isBlank() ||
                                location.isBlank()
                            ) {

                                Toast.makeText(
                                    context,
                                    "Please fill all fields",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }

                            val userId =
                                auth?.currentUser?.uid

                            if (
                                userId == null &&
                                !isPreview
                            ) {

                                Toast.makeText(
                                    context,
                                    "You must be logged in",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }

                            val report = Report(

                                id = "",

                                userId = userId ?: "preview-user",

                                title = title,

                                category = category,

                                location = location,

                                description = description,

                                status = "Pending",

                                timestamp =
                                    System.currentTimeMillis()
                            )

                            reportViewModel.createReport(
                                report = report,

                                onSuccess = {

                                    Toast.makeText(
                                        context,
                                        "Report submitted successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.popBackStack()
                                }
                            )
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),

                        enabled = !isLoading,

                        shape = RoundedCornerShape(16.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = CivicBlue
                        )
                    ) {

                        if (isLoading) {

                            CircularProgressIndicator(
                                color = CivicWhite,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )

                        } else {

                            Text(
                                text = "Submit Report",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Reports are reviewed by community administrators to ensure prompt action.",
                color = CivicWhite.copy(alpha = 0.7f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreateReportScreenPreview() {

    CreateReportScreen(
        navController = rememberNavController()
    )
}
