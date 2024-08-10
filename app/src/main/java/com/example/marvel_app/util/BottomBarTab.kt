package com.example.marvel_app.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarTab(
    val title: String,
    val icon: ImageVector,
    val unselected_icon: ImageVector,
    val route: String)
{
    data object Profile : BottomBarTab(
        title = "Search",
        icon = Icons.Filled.Search,
        unselected_icon = Icons.Outlined.Search,
        route = Routes.SETTINGS_SCREEN
    )
    data object Home : BottomBarTab(
        title = "Home",
        icon = Icons.Filled.Home,
        unselected_icon = Icons.Outlined.Home,
        route = Routes.HERO_LIST_SCREEN
    )
    data object Settings : BottomBarTab(
        title = "Settings",
        icon = Icons.Filled.Settings,
        unselected_icon = Icons.Outlined.Settings,
        route = Routes.SETTINGS_SCREEN
    )
}