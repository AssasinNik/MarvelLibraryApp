package com.example.marvel_app.presentation.bottom_navigation_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.example.marvel_app.ui.theme.SearchColor
import com.example.marvel_app.ui.theme.SearchTextColor
import com.example.marvel_app.util.BottomBarTab
import com.example.marvel_app.util.Routes


@Composable
fun BottomNavigationBar(
    navController: NavController
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val tabs = listOf(
        BottomBarTab.Profile,
        BottomBarTab.Home,
        BottomBarTab.Settings,
    )
    if (currentDestination?.route in setOf(Routes.HERO_LIST_SCREEN, Routes.SETTINGS_SCREEN)) {
        NavigationBar(
            modifier = Modifier
                .shadow(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color.Black,
                    spotColor = Color.Black
                )
                .height(70.dp)
                .clip(RoundedCornerShape(12.dp)),
            containerColor = SearchColor
        ) {
            tabs.forEachIndexed { index, bottomNavBarItem ->
                NavigationBarItem(
                    modifier = Modifier.padding(top = 10.dp),
                    selected = checkCurrentDestination(currentDestination?.route) == index,
                    onClick = {
                        navController.navigate(tabs[index].route)
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
fun checkCurrentDestination(currentScreen: String?): Int {
    return when (currentScreen){
        Routes.HERO_LIST_SCREEN -> 1
        Routes.SETTINGS_SCREEN -> 2
        else -> 0
    }
}
