package com.example.marvel_app

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marvel_app.presentation.bottom_navigation_bar.BottomNavBar
import com.example.marvel_app.presentation.settings_screen.SettingsScreen
import com.example.marvel_app.presentation.character_screen.CharacterScreen
import com.example.marvel_app.presentation.comics_screen.ComicsScreen
import com.example.marvel_app.presentation.favourites_screen.FavouritesScreen
import com.example.marvel_app.presentation.hero_list.HeroListScreen
import com.example.marvel_app.presentation.marvel_start_screen.MarvelStartScreen
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.Marvel_appTheme
import com.example.marvel_app.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("SourceLockedOrientationActivity", "UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            Marvel_appTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackGround
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        backgroundColor = BackGround,
                        bottomBar = {
                            BottomNavBar(
                                navController
                            )
                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Routes.MARVEL_START_SCREEN,
                            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))},
                            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))},
                            popEnterTransition =  { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))},
                            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))}
                        ){
                            composable(
                                route = Routes.MARVEL_START_SCREEN
                            ){
                                MarvelStartScreen(navController = navController)
                            }
                            composable(
                                route = Routes.HERO_LIST_SCREEN
                            ){
                                HeroListScreen(navController = navController)
                            }
                            composable(
                                route = Routes.SETTINGS_SCREEN
                            ){
                                SettingsScreen(navController = navController)
                            }
                            composable(
                                route = Routes.FAVOURITES_SCREEN
                            ){
                                FavouritesScreen(navController = navController)
                            }
                            composable(
                                route = "${Routes.CHARACTER_SCREEN}/{heroId}/{heroName}/{heroImage}",
                                arguments = listOf(
                                    navArgument("heroId"){
                                        type = NavType.IntType
                                    },
                                    navArgument("heroName"){
                                        type = NavType.StringType
                                    },
                                    navArgument("heroImage"){
                                        type = NavType.StringType
                                    }
                                )
                            ){
                                val heroId = remember {
                                    it.arguments?.getInt("heroId")
                                }
                                val heroName = remember {
                                    it.arguments?.getString("heroName")
                                }
                                val heroImage = remember {
                                    it.arguments?.getString("heroImage")
                                }
                                CharacterScreen(
                                    navController = navController,
                                    heroId = heroId,
                                    heroName = heroName,
                                    heroImage = heroImage
                                )
                            }
                            composable(
                                route = "${Routes.COMICS_SCREEN}/{comicsId}/{comicsName}/{comicsImage}",
                                arguments = listOf(
                                    navArgument("comicsId"){
                                        type = NavType.IntType
                                    },
                                    navArgument("comicsName"){
                                        type = NavType.StringType
                                    },
                                    navArgument("comicsImage"){
                                        type = NavType.StringType
                                    }
                                )
                            ){
                                val comicsId = remember {
                                    it.arguments?.getInt("comicsId")
                                }
                                val comicsName = remember {
                                    it.arguments?.getString("comicsName")
                                }
                                val comicsImage = remember {
                                    it.arguments?.getString("comicsImage")
                                }
                                ComicsScreen(
                                    navController = navController,
                                    comicsId = comicsId,
                                    comicsName = comicsName,
                                    comicsImage = comicsImage
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
