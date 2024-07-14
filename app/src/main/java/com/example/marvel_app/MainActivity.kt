package com.example.marvel_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marvel_app.data.remote.MarvelAuthentication
import com.example.marvel_app.ui.hero_list.HeroListScreen
import com.example.marvel_app.ui.theme.Marvel_appTheme
import com.example.marvel_app.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val auth = MarvelAuthentication(Constants.PRIVATE_KEY, Constants.PUBLIC_KEY)
        val testUrl = auth.generateAuthUrl("/v1/public/characters")
        Timber.tag("MainActivity").d("Test URL: %s", testUrl)
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
