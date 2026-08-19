package com.example.civilink.ui.screens.createreport

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Title
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicTeal
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CreateReportScreen(
    navController: NavController,
    reportViewModel: ReportViewModel = viewModel()
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Roads") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isCategoryMenuExpanded by remember { mutableStateOf(false) }

    val isLoading by reportViewModel.isLoading.collectAsState()
    val categories = listOf("Roads", "Water", "Garbage", "Electricity", "Other")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CivicLightBlue)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CivicNavy)
                .padding(horizontal = 8.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = CivicWhite
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Report an Issue",
                color = CivicWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            AppLogo(size = 32.dp)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = CivicWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Title") },
                        leadingIcon = { Icon(Icons.Default.Title, null) },
                        shape = RoundedCornerShape(16.dp),
                        colors = outlinedTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Category Dropdown
                    Box {
                        OutlinedTextField(
                            value = category,
                            onValueChange = { },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Category") },
                            leadingIcon = { Icon(Icons.Default.Build, null) },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { isCategoryMenuExpanded = true }) {
                                    Icon(Icons.Default.Build, null)
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = outlinedTextFieldColors()
                        )
                        DropdownMenu(
                            expanded = isCategoryMenuExpanded,
                            onDismissRequest = { isCategoryMenuExpanded = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        category = cat
                                        isCategoryMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Location
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Location") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                        shape = RoundedCornerShape(16.dp),
                        colors = outlinedTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Description") },
                        leadingIcon = { Icon(Icons.Default.Description, null) },
                        minLines = 3,
                        shape = RoundedCornerShape(16.dp),
                        colors = outlinedTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (title.isBlank() || location.isBlank() || description.isBlank()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (currentUser == null) {
                                Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val report = Report(
                                userId = currentUser.uid,
                                title = title,
                                category = category,
                                location = location,
                                description = description,
                                timestamp = System.currentTimeMillis()
                            )

                            reportViewModel.createReport(
                                report = report,
                                onSuccess = {
                                    Toast.makeText(context, "Report submitted successfully", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                },
                                onError = {
                                    Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CivicBlue)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = CivicWhite,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Submit Report", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CivicBlue,
    unfocusedBorderColor = CivicGray.copy(alpha = 0.5f),
    focusedLabelColor = CivicBlue,
    unfocusedLabelColor = CivicGray,
    focusedLeadingIconColor = CivicBlue,
    unfocusedLeadingIconColor = CivicGray
)

@Preview(showBackground = true)
@Composable
fun CreateReportScreenPreview() {
    CreateReportScreen(navController = rememberNavController())
}
