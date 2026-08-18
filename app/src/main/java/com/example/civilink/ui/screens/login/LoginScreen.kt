package com.example.civilink.ui.screens.login

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
import androidx.compose.material.icons.filled.Visibility
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
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    val auth = remember(isPreview) {
        if (isPreview) null else FirebaseAuth.getInstance()
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(value = false)
    }

    var isLoading by remember {
        mutableStateOf(value = false)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        CivicNavy,
                        CivicBlue,
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

            AppLogo(
                size = 82.dp
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Welcome back",
                color = CivicWhite,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Sign in to make your community heard.",
                color = CivicWhite.copy(alpha = 0.75f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        text = "Login",
                        color = CivicText,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )


                    // Email

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = {
                            Text("Email address")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CivicText,
                            unfocusedTextColor = CivicText,
                            focusedBorderColor = CivicBlue,
                            unfocusedBorderColor = CivicGray.copy(alpha = 0.5f),
                            focusedLabelColor = CivicBlue,
                            unfocusedLabelColor = CivicGray,
                            focusedLeadingIconColor = CivicBlue,
                            unfocusedLeadingIconColor = CivicBlue,
                            cursorColor = CivicBlue,
                        ),
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    // Password

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = {
                            Text("Password")
                        },
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
                                    imageVector =
                                        if (passwordVisible) {
                                            Icons.Default.VisibilityOff
                                        } else {
                                            Icons.Default.Visibility
                                        },
                                    contentDescription =
                                        if (passwordVisible) {
                                            "Hide password"
                                        } else {
                                            "Show password"
                                        }
                                )
                            }
                        },
                        visualTransformation =
                            if (passwordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        shape = RoundedCornerShape(16.dp),
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

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )


                    // Forgot Password

                    Text(
                        text = "Forgot password?",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {

                                Toast.makeText(
                                    context,
                                    "Password reset will be available soon.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            },
                        color = CivicBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )


                    // LOGIN

                    Button(
                        onClick = {

                            if (
                                email.isBlank() ||
                                password.isBlank()
                            ) {

                                Toast.makeText(
                                    context,
                                    "Please enter email and password",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }


                            isLoading = true


                            auth?.signInWithEmailAndPassword(
                                email.trim(),
                                password
                            )?.addOnCompleteListener { task ->

                                if (task.isSuccessful) {

                                    val user = FirebaseAuth.getInstance().currentUser

                                    if (user != null) {

                                        val uid = user.uid

                                        FirebaseDatabase.getInstance()
                                            .getReference("admins")
                                            .child(uid)
                                            .get()
                                            .addOnSuccessListener { snapshot ->
                                                isLoading = false
                                                if (snapshot.exists()) {

                                                    // User is an admin
                                                    navController.navigate(ROUT_ADMINDASHBOARD) {
                                                        popUpTo(ROUT_LOGINSCREEN) {
                                                            inclusive = true
                                                        }
                                                    }

                                                } else {

                                                    // Normal user
                                                    navController.navigate(ROUT_HOMESCREEN) {
                                                        popUpTo(ROUT_LOGINSCREEN) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                            }
                                            .addOnFailureListener {
                                                isLoading = false
                                                Toast.makeText(
                                                    context,
                                                    "Could not verify account",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                } else {
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Login failed: ${task.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

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
                                modifier = Modifier.size(24.dp),
                                color = CivicWhite,
                                strokeWidth = 2.dp
                            )

                        } else {

                            Text(
                                text = "Login",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }


                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )


                    // Register

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Don't have an account?",
                            color = CivicGray,
                            fontSize = 13.sp
                        )

                        Spacer(
                            modifier = Modifier.width(5.dp)
                        )

                        Text(
                            text = "Create one",
                            modifier = Modifier.clickable {

                                navController.navigate(
                                    ROUT_REGISTER
                                )
                            },
                            color = CivicTeal,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            Text(
                text = "Your voice. Your community. Your impact.",
                color = CivicWhite.copy(alpha = 0.65f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {

    LoginScreen(
        navController = rememberNavController(),
    )
}
