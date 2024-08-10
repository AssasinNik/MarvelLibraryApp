package com.example.marvel_app

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marvel_app.presentation.bottom_navigation_bar.BottomNavigationBar
import com.example.marvel_app.presentation.settings_screen.SettingsScreen
import com.example.marvel_app.presentation.character_screen.CharacterScreen
import com.example.marvel_app.presentation.hero_list.HeroListScreen
import com.example.marvel_app.presentation.main_screen.MainViewModel
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.Marvel_appTheme
import com.example.marvel_app.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @SuppressLint("SourceLockedOrientationActivity", "UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                !viewModel.isReady.value
            }
            setOnExitAnimationListener{screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }


                zoomX.start()
                zoomY.start()
            }
        }
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
                            BottomNavigationBar(
                                navController
                            )
                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "MarvelListScreen",
                            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))},
                            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))},
                            popEnterTransition =  { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))},
                            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))}
                        ){
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
                        }
                    }
                }
            }
        }
    }
}
