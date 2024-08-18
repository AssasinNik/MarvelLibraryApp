package com.example.marvel_app.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarTab(
    val icon: ImageVector,
    val unselected_icon: ImageVector,
    val route: String)
{
    data object Home : BottomBarTab(
        icon = Icons.Filled.Home,
        unselected_icon = Icons.Outlined.Home,
        route = Routes.HERO_LIST_SCREEN
    )
    data object Search : BottomBarTab(
        icon = Icons.Filled.Search,
        unselected_icon = Icons.Outlined.Search,
        route = Routes.SEARCH_SCREEN
    )
    data object Favourites : BottomBarTab(
        icon = Icons.Filled.Favorite,
        unselected_icon = Icons.Outlined.FavoriteBorder,
        route = Routes.FAVOURITES_SCREEN
    )
    data object Settings : BottomBarTab(
        icon = Icons.Filled.Settings,
        unselected_icon = Icons.Outlined.Settings,
        route = Routes.SETTINGS_SCREEN
    )
}