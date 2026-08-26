package com.example.civilink.ui.screens.createreport

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoLibrary
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.civilink.data.Report
import com.example.civilink.data.ReportViewModel
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite
import com.google.firebase.auth.FirebaseAuth
import java.io.File

@Composable
fun CreateReportScreen(
    navController: NavController,
    reportViewModel: ReportViewModel = viewModel()
) {
    val context = LocalContext.current
    val isLoading by reportViewModel.isLoading.collectAsState()

    CreateReportContent(
        isLoading = isLoading,
        onBackClick = { navController.popBackStack() },
        onSubmit = { title, category, location, description, imageUri ->
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser

            if (currentUser == null) {
                Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
                return@CreateReportContent
            }

            val submitReport = { url: String? ->
                val report = Report(
                    userId = currentUser.uid,
                    title = title.trim(),
                    category = category,
                    location = location.trim(),
                    description = description.trim(),
                    timestamp = System.currentTimeMillis(),
                    imageUrl = url
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
            }

            if (imageUri != null) {
                reportViewModel.uploadImage(
                    uri = imageUri,
                    onSuccess = { url -> submitReport(url) },
                    onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                )
            } else {
                submitReport(null)
            }
        }
    )
}

@Composable
fun CreateReportContent(
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onSubmit: (String, String, String, String, Uri?) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Roads") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isCategoryMenuExpanded by remember { mutableStateOf(false) }

    // Camera states
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    // Validation states
    var titleError by remember { mutableStateOf<String?>(null) }
    var locationError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Roads", "Water", "Garbage", "Electricity", "Other")

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = tempImageUri
        }
    }

    val isFormValid = title.isNotBlank() && title.length >= 5 &&
                      location.isNotBlank() && location.length >= 3 &&
                      description.isNotBlank() && description.length >= 10

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
            IconButton(onClick = onBackClick) {
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
                    
                    // Image Picker
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CivicLightBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = imageUri,
                                    contentDescription = "Selected Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                IconButton(
                                    onClick = { imageUri = null },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                        .size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = CivicWhite,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { imagePickerLauncher.launch("image/*") }
                                ) {
                                    Icon(Icons.Default.PhotoLibrary, null, tint = CivicBlue, modifier = Modifier.size(32.dp))
                                    Spacer(Modifier.height(8.dp))
                                    Text("Gallery", color = CivicBlue, fontWeight = FontWeight.SemiBold)
                                }

                                Box(modifier = Modifier.width(1.dp).height(40.dp).background(CivicGray.copy(alpha = 0.3f)))

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        val uri = context.createTempPictureUri()
                                        tempImageUri = uri
                                        cameraLauncher.launch(uri)
                                    }
                                ) {
                                    Icon(Icons.Default.CameraAlt, null, tint = CivicBlue, modifier = Modifier.size(32.dp))
                                    Spacer(Modifier.height(8.dp))
                                    Text("Camera", color = CivicBlue, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { 
                            title = it 
                            titleError = if (it.length < 5 && it.isNotBlank()) "Title too short (min 5)" else null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Title") },
                        leadingIcon = { Icon(Icons.Default.Title, null) },
                        isError = titleError != null,
                        supportingText = { titleError?.let { Text(it) } },
                        shape = RoundedCornerShape(16.dp),
                        colors = outlinedTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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
                        onValueChange = { 
                            location = it
                            locationError = if (it.length < 3 && it.isNotBlank()) "Invalid location" else null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Location") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                        isError = locationError != null,
                        supportingText = { locationError?.let { Text(it) } },
                        shape = RoundedCornerShape(16.dp),
                        colors = outlinedTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { 
                            description = it 
                            descriptionError = if (it.length < 10 && it.isNotBlank()) "Description too short (min 10)" else null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Description") },
                        leadingIcon = { Icon(Icons.Default.Description, null) },
                        minLines = 3,
                        isError = descriptionError != null,
                        supportingText = { descriptionError?.let { Text(it) } },
                        shape = RoundedCornerShape(16.dp),
                        colors = outlinedTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (isFormValid) {
                                onSubmit(title, category, location, description, imageUri)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        enabled = !isLoading && isFormValid,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CivicBlue,
                            disabledContainerColor = CivicBlue.copy(alpha = 0.5f)
                        )
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
    focusedTextColor = CivicText,
    unfocusedTextColor = CivicText,
    focusedBorderColor = CivicBlue,
    unfocusedBorderColor = CivicGray.copy(alpha = 0.5f),
    focusedLabelColor = CivicBlue,
    unfocusedLabelColor = CivicGray,
    focusedLeadingIconColor = CivicBlue,
    unfocusedLeadingIconColor = CivicGray,
    errorBorderColor = Color.Red,
    errorLabelColor = Color.Red,
    errorLeadingIconColor = Color.Red,
    errorSupportingTextColor = Color.Red
)

@Preview(showBackground = true)
@Composable
fun CreateReportScreenPreview() {
    CreateReportContent(
        isLoading = false,
        onBackClick = {},
        onSubmit = { _, _, _, _, _ -> }
    )
}

fun Context.createTempPictureUri(
    providerAuthority: String = "${packageName}.fileprovider"
): Uri {
    val tempFile = File.createTempFile(
        "picture_${System.currentTimeMillis()}",
        ".jpg",
        cacheDir
    ).apply {
        createNewFile()
        deleteOnExit()
    }

    return FileProvider.getUriForFile(applicationContext, providerAuthority, tempFile)
}
