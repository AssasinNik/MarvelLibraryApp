package com.example.marvel_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marvel_app.ui.hero_list.HeroListScreen
import com.example.marvel_app.ui.main_screen.MainViewModel
import com.example.marvel_app.ui.theme.Marvel_appTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition{
                !viewModel.isReady.value
            }
        }
        enableEdgeToEdge()
        setContent {
            Marvel_appTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "MarvelListScreen"){
                    composable(
                        route = "MarvelListScreen"
                    ){
                        HeroListScreen(navController = navController)
                    }
                    composable(
                        route = "HeroDetailScreen/{heroId}",
                        arguments = listOf(
                            navArgument("heroId"){
                                type = NavType.IntType
                            }
                        )
                    ){
                        val heroId = remember {
                            it.arguments?.getInt("heroName")
                        }
                    }
                }
            }
        }
    }
}
