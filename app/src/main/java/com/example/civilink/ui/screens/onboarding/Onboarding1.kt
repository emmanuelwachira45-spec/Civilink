package com.example.civilink.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.civilink.navigation.ROUT_ONBOARDING2
import com.example.civilink.ui.components.AppLogo
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
        AppLogo(size = 100.dp)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome to CiviLink",
            color = CivicWhite,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Your platform for community improvement and engagement.",
            color = CivicWhite.copy(alpha = 0.8f),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(48.dp)
        )

        Button(
            onClick = {
                navController.navigate(ROUT_ONBOARDING2)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = CivicBlue
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {

            Text(
                text = "Continue",
                color = CivicWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Onboarding1ScreenPreview() {

    Onboarding1Screen(
        navController = rememberNavController()
    )
}