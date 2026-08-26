package com.example.civilink.ui.screens.profile

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import com.example.civilink.data.User
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val currentUser = auth.currentUser

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    LaunchedEffect(currentUser?.uid) {
        if (currentUser?.uid != null) {
            database.getReference("users").child(currentUser.uid).get().addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    fullName = user.fullName
                    email = user.email
                    phone = user.phone
                    location = user.location
                    profileImageUrl = user.profileImageUrl
                }
                isLoading = false
            }.addOnFailureListener {
                isLoading = false
                Toast.makeText(context, "Failed to load user info", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
                text = "Edit Profile",
                color = CivicWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            AppLogo(size = 32.dp)
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = CivicBlue)
            }
        } else {
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
                        Text(
                            text = "Personal Information",
                            color = CivicText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Profile Photo Picker
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(CivicLightBlue)
                                .clickable { imagePickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "New Profile Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else if (profileImageUrl != null) {
                                AsyncImage(
                                    model = profileImageUrl,
                                    contentDescription = "Current Profile Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Upload Photo",
                                    modifier = Modifier.size(40.dp),
                                    tint = CivicBlue
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Text(
                                    "EDIT",
                                    color = CivicWhite,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Full Name
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, null) },
                            shape = RoundedCornerShape(16.dp),
                            colors = editTextFieldColors()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Email Address") },
                            leadingIcon = { Icon(Icons.Default.Email, null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = RoundedCornerShape(16.dp),
                            colors = editTextFieldColors()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Phone
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Phone Number") },
                            leadingIcon = { Icon(Icons.Default.Phone, null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            shape = RoundedCornerShape(16.dp),
                            colors = editTextFieldColors()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Location
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Location") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                            shape = RoundedCornerShape(16.dp),
                            colors = editTextFieldColors()
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        // Save Button
                        Button(
                            onClick = {
                                if (fullName.isBlank() || phone.isBlank()) {
                                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (currentUser?.uid != null) {
                                    isSaving = true
                                    
                                    val performUpdate = { imageUrl: String? ->
                                        val updates = mutableMapOf<String, Any?>(
                                            "fullName" to fullName.trim(),
                                            "email" to email.trim(),
                                            "phone" to phone.trim(),
                                            "location" to location.trim()
                                        )
                                        if (imageUrl != null) {
                                            updates["profileImageUrl"] = imageUrl
                                        }

                                        database.getReference("users").child(currentUser.uid).updateChildren(updates)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful && email.trim() != currentUser.email) {
                                                    currentUser.updateEmail(email.trim())
                                                        .addOnCompleteListener { authTask ->
                                                            isSaving = false
                                                            Toast.makeText(context, "Profile and Email updated", Toast.LENGTH_SHORT).show()
                                                            navController.popBackStack()
                                                        }
                                                } else {
                                                    isSaving = false
                                                    if (task.isSuccessful) {
                                                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                                                        navController.popBackStack()
                                                    } else {
                                                        Toast.makeText(context, "Update failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                    }

                                    if (selectedImageUri != null) {
                                        val storageRef = com.google.firebase.storage.FirebaseStorage.getInstance().reference
                                            .child("profiles/${currentUser.uid}.jpg")
                                        
                                        storageRef.putFile(selectedImageUri!!)
                                            .addOnSuccessListener {
                                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                                    performUpdate(uri.toString())
                                                }
                                            }
                                            .addOnFailureListener {
                                                isSaving = false
                                                Toast.makeText(context, "Image upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    } else {
                                        performUpdate(null)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            enabled = !isSaving,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CivicBlue)
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = CivicWhite, strokeWidth = 2.dp)
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Save, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Save Changes", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun editTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = CivicText,
    unfocusedTextColor = CivicText,
    focusedBorderColor = CivicBlue,
    unfocusedBorderColor = CivicGray.copy(alpha = 0.5f),
    focusedLabelColor = CivicBlue,
    unfocusedLabelColor = CivicGray,
    focusedLeadingIconColor = CivicBlue,
    unfocusedLeadingIconColor = CivicGray
)

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(navController = rememberNavController())
}
