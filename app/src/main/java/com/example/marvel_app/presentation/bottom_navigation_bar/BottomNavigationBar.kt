package com.example.marvel_app.presentation.bottom_navigation_bar

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.example.marvel_app.ui.theme.SearchColor
import com.example.marvel_app.util.BottomBarTab
import com.example.marvel_app.util.Routes
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBarsPadding

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val tabs = listOf(
        BottomBarTab.Home,
        BottomBarTab.Search,
        BottomBarTab.Favourites,
        BottomBarTab.Settings
    )

    // Show the BottomNavBar only on specific screens
    if (currentDestination?.route in setOf(Routes.HERO_LIST_SCREEN, Routes.SETTINGS_SCREEN, Routes.FAVOURITES_SCREEN, Routes.SEARCH_SCREEN)) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding() // Padding for status and navigation bars
        ) {
            val adaptiveHeight: Dp = with(LocalDensity.current) {
                // Dynamic height adjustment based on screen width
                if (maxWidth <= 360.dp) {
                    56.dp
                } else {
                    70.dp
                }
            }

            NavigationBar(
                modifier = Modifier
                    .height(adaptiveHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.navigationBars), // Padding only if navigation bar is present
                containerColor = SearchColor
            ) {
                tabs.forEachIndexed { index, bottomNavBarItem ->
                    NavigationBarItem(
                        modifier = Modifier.padding(top = 10.dp),
                        selected = checkCurrentDestination(currentDestination?.route) == index,
                        onClick = {
                            navController.navigate(tabs[index].route) {
                                // Avoid multiple copies of the same destination
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == checkCurrentDestination(currentDestination?.route)) {
                                    tabs[index].icon
                                } else tabs[index].unselected_icon,
                                contentDescription = tabs[index].route,
                                tint = if (index == checkCurrentDestination(currentDestination?.route)) {
                                    Color.White
                                } else SearchBorderColor,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        colors = NavigationBarItemColors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = SearchBorderColor,
                            selectedTextColor = Color.Transparent,
                            unselectedTextColor = Color.Transparent,
                            disabledIconColor = Color.Transparent,
                            disabledTextColor = Color.Transparent,
                            selectedIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

fun checkCurrentDestination(currentScreen: String?): Int {
    return when (currentScreen) {
        Routes.HERO_LIST_SCREEN -> 0
        Routes.SEARCH_SCREEN -> 1
        Routes.FAVOURITES_SCREEN -> 2
        Routes.SETTINGS_SCREEN -> 3
        else -> 0
    }
}
