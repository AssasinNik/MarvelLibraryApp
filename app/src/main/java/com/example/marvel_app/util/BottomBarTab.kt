package com.example.marvel_app.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.marvel_app.ui.theme.RedColor

sealed class BottomBarTab(val title: String, val icon: ImageVector, val color: Color, val route: String) {
    data object Profile : BottomBarTab(
        title = "Search",
        icon = Icons.Rounded.Search,
        color = RedColor,
        route = Routes.HERO_LIST_SCREEN
    )
    data object Home : BottomBarTab(
        title = "Home",
        icon = Icons.Rounded.Home,
        color = RedColor,
        route = Routes.HERO_LIST_SCREEN
    )
    data object Settings : BottomBarTab(
        title = "Settings",
        icon = Icons.Rounded.Settings,
        color = RedColor,
        route = Routes.SETTINGS_SCREEN
    )
}