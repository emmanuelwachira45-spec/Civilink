package com.example.civilink.ui.Screens.Onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun Onboarding2Screen(navController: NavController){

    Column(
        modifier = Modifier.fillMaxSize()
    ) {


    }


}

@Preview(showBackground = true)
@Composable
fun Onboarding2ScreenPreview(){
    Onboarding2Screen(navController= rememberNavController())
}