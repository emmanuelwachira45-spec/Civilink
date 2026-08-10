package com.example.civilink.ui.Screens.Onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.civilink.navigation.ROUT_LOGINSCREEN
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicWhite

@Composable
fun Onboarding1Screen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CivicNavy)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to CiviLink",
            color = CivicWhite,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Your platform for community improvement and engagement.",
            color = CivicWhite.copy(alpha = 0.8f),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = {
                navController.navigate(ROUT_LOGINSCREEN)
            },
            colors = ButtonDefaults.buttonColors(containerColor = CivicBlue),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Get Started", color = CivicWhite, fontSize = 18.sp)
        }
    }
}
