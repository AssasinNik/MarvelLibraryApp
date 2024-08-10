package com.example.marvel_app.presentation.bottom_navigation_bar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController


@Composable
fun BottomNavigation(
    navController: NavController
) {
    var selectedTabIndex by remember { mutableIntStateOf(1) }
}
