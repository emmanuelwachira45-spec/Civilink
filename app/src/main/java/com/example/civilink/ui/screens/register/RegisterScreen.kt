package com.example.civilink.ui.screens.register

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civilink.navigation.ROUT_ADMINDASHBOARD
import com.example.civilink.navigation.ROUT_HOMESCREEN
import com.example.civilink.navigation.ROUT_LOGINSCREEN
import com.example.civilink.navigation.ROUT_REGISTER
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicTeal
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun RegisterScreen(navController: NavController) {

    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    val auth = remember(isPreview) {
        if (isPreview) null else FirebaseAuth.getInstance()
    }

    val database = remember(isPreview) {
        if (isPreview) {
            null
        } else {
            FirebaseDatabase.getInstance()
        }
    }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(value = false) }
    var confirmPasswordVisible by remember { mutableStateOf(value = false) }
    var isLoading by remember { mutableStateOf(value = false) }

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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // CiviLink Logo
            AppLogo(
                size = 68.dp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Create your account",
                color = CivicWhite,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Join your community and make a difference.",
                color = CivicWhite.copy(alpha = 0.75f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Register",
                        color = CivicText,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Full name") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CivicText,
                            unfocusedTextColor = CivicText,
                            focusedBorderColor = CivicBlue,
                            unfocusedBorderColor = CivicGray.copy(alpha = 0.5f),
                            focusedLabelColor = CivicBlue,
                            unfocusedLabelColor = CivicGray,
                            focusedLeadingIconColor = CivicBlue,
                            unfocusedLeadingIconColor = CivicBlue,
                            focusedTrailingIconColor = CivicBlue,
                            unfocusedTrailingIconColor = CivicBlue,
                            cursorColor = CivicBlue,
                        ),
                    )

                    Spacer(modifier = Modifier.height(11.dp))

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Email address") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CivicText,
                            unfocusedTextColor = CivicText,
                            focusedBorderColor = CivicBlue,
                            unfocusedBorderColor = CivicGray.copy(alpha = 0.5f),
                            focusedLabelColor = CivicBlue,
                            unfocusedLabelColor = CivicGray,
                            focusedLeadingIconColor = CivicBlue,
                            unfocusedLeadingIconColor = CivicBlue,
                            focusedTrailingIconColor = CivicBlue,
                            unfocusedTrailingIconColor = CivicBlue,
                            cursorColor = CivicBlue,
                        ),
                    )

                    Spacer(modifier = Modifier.height(11.dp))

                    // Phone
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Phone number") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CivicText,
                            unfocusedTextColor = CivicText,
                            focusedBorderColor = CivicBlue,
                            unfocusedBorderColor = CivicGray.copy(alpha = 0.5f),
                            focusedLabelColor = CivicBlue,
                            unfocusedLabelColor = CivicGray,
                            focusedLeadingIconColor = CivicBlue,
                            unfocusedLeadingIconColor = CivicBlue,
                            focusedTrailingIconColor = CivicBlue,
                            unfocusedTrailingIconColor = CivicBlue,
                            cursorColor = CivicBlue,
                        ),
                    )

                    Spacer(modifier = Modifier.height(11.dp))

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisible = !passwordVisible
                                }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CivicText,
                            unfocusedTextColor = CivicText,
                            focusedBorderColor = CivicBlue,
                            unfocusedBorderColor = CivicGray.copy(alpha = 0.5f),
                            focusedLabelColor = CivicBlue,
                            unfocusedLabelColor = CivicGray,
                            focusedLeadingIconColor = CivicBlue,
                            unfocusedLeadingIconColor = CivicBlue,
                            focusedTrailingIconColor = CivicBlue,
                            unfocusedTrailingIconColor = CivicBlue,
                            cursorColor = CivicBlue,
                        ),
                    )

                    Spacer(modifier = Modifier.height(11.dp))

                    // Confirm Password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Confirm password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    confirmPasswordVisible =
                                        !confirmPasswordVisible
                                }
                            ) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CivicText,
                            unfocusedTextColor = CivicText,
                            focusedBorderColor = CivicBlue,
                            unfocusedBorderColor = CivicGray.copy(alpha = 0.5f),
                            focusedLabelColor = CivicBlue,
                            unfocusedLabelColor = CivicGray,
                            focusedLeadingIconColor = CivicBlue,
                            unfocusedLeadingIconColor = CivicBlue,
                            focusedTrailingIconColor = CivicBlue,
                            unfocusedTrailingIconColor = CivicBlue,
                            cursorColor = CivicBlue,
                        ),
                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    // Register Button
                    Button(
                        onClick = {

                            when {

                                fullName.isBlank() ||
                                        email.isBlank() ||
                                        phone.isBlank() ||
                                        password.isBlank() ||
                                        confirmPassword.isBlank() -> {

                                    Toast.makeText(
                                        context,
                                        "Please fill all fields",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                !android.util.Patterns.EMAIL_ADDRESS
                                    .matcher(email.trim())
                                    .matches() -> {

                                    Toast.makeText(
                                        context,
                                        "Please enter a valid email address",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                password.length < 6 -> {

                                    Toast.makeText(
                                        context,
                                        "Password must be at least 6 characters",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                password != confirmPassword -> {

                                    Toast.makeText(
                                        context,
                                        "Passwords do not match",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                (auth == null || database == null) -> {

                                    Toast.makeText(
                                        context,
                                        "Firebase is not available",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }

                                else -> {

                                    isLoading = true

                                    val cleanEmail = email.trim()
                                    val cleanName = fullName.trim()
                                    val cleanPhone = phone.trim()

                                    // Create Firebase Authentication account
                                    auth.createUserWithEmailAndPassword(
                                        cleanEmail,
                                        password
                                    ).addOnCompleteListener { task ->

                                        if (task.isSuccessful) {

                                            val userId =
                                                auth.currentUser?.uid

                                            if (userId != null) {

                                                // Determine role based on email
                                                val role =
                                                    if (cleanEmail == "admin@civilink.com") "admin" else "user"

                                                // User information to save
                                                val userData = mapOf(
                                                    "fullName" to cleanName,
                                                    "email" to cleanEmail,
                                                    "phone" to cleanPhone,
                                                    "role" to role,
                                                    "createdAt" to System.currentTimeMillis()
                                                )

                                                // Save user profile
                                                // to Realtime Database
                                                database
                                                    .getReference("users")
                                                    .child(userId)
                                                    .setValue(userData)
                                                    .addOnCompleteListener { databaseTask ->

                                                        isLoading = false

                                                        if (databaseTask.isSuccessful) {

                                                            Toast.makeText(
                                                                context,
                                                                "Account created successfully!",
                                                                Toast.LENGTH_SHORT
                                                            ).show()

                                                            // Navigate based on role
                                                            val destination =
                                                                if (role == "admin") ROUT_ADMINDASHBOARD else ROUT_HOMESCREEN

                                                            navController.navigate(
                                                                destination
                                                            ) {

                                                                popUpTo(
                                                                    ROUT_REGISTER
                                                                ) {
                                                                    inclusive = true
                                                                }

                                                                launchSingleTop = true
                                                            }

                                                        } else {

                                                            Toast.makeText(
                                                                context,
                                                                "Account created, but profile could not be saved.",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                    }

                                            } else {

                                                isLoading = false

                                                Toast.makeText(
                                                    context,
                                                    "Could not get user information.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }

                                        } else {

                                            isLoading = false

                                            Toast.makeText(
                                                context,
                                                "Registration failed: ${task.exception?.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CivicBlue
                        )
                    ) {

                        if (isLoading) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = CivicWhite,
                                strokeWidth = 2.dp
                            )

                        } else {

                            Text(
                                text = "Create Account",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Login link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Already have an account?",
                            color = CivicGray,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "Login",
                            modifier = Modifier.clickable {

                                navController.navigate(
                                    ROUT_LOGINSCREEN
                                )
                            },
                            color = CivicTeal,
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
fun RegisterScreenPreview() {

    RegisterScreen(
        navController = rememberNavController(),
    )
}
