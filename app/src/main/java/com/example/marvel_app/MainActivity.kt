package com.example.marvel_app

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marvel_app.presentation.bottom_navigation_bar.BottomNavBar
import com.example.marvel_app.presentation.settings_screen.SettingsScreen
import com.example.marvel_app.presentation.character_screen.CharacterScreen
import com.example.marvel_app.presentation.comics_screen.ComicsScreen
import com.example.marvel_app.presentation.favourite_list_screen.FavouriteListScreen
import com.example.marvel_app.presentation.favourites_screen.FavouritesScreen
import com.example.marvel_app.presentation.film_screen.FilmScreen
import com.example.marvel_app.presentation.marvel_start_screen.GoogleAuthUiClient
import com.example.marvel_app.presentation.hero_list.HeroListScreen
import com.example.marvel_app.presentation.marvel_start_screen.MarvelStartScreen
import com.example.marvel_app.presentation.marvel_start_screen.MarvelStartViewModel
import com.example.marvel_app.presentation.search_screen.SearchScreen
import com.example.marvel_app.presentation.tvShows_screen.TvShowsScreen
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.Marvel_appTheme
import com.example.marvel_app.util.Routes
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private fun getStartScreen(): String {
        if (googleAuthUiClient.getSignedInUser() != null) {
            return Routes.HERO_LIST_SCREEN
        }
        else {
            return Routes.MARVEL_START_SCREEN
        }
    }

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
                            startDestination = getStartScreen(),
                            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))},
                            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))},
                            popEnterTransition =  { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))},
                            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))}
                        ){
                            composable(
                                route = Routes.MARVEL_START_SCREEN
                            ){
                                val viewModel = viewModel<MarvelStartViewModel>()
                                val state by viewModel.state.collectAsState()
                                LaunchedEffect(key1 = Unit) {
                                    if(googleAuthUiClient.getSignedInUser() != null) {
                                        navController.navigate(Routes.HERO_LIST_SCREEN)
                                    }
                                }

                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = {result ->
                                        if(result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult = googleAuthUiClient.signInWithIntent(
                                                    intent = result.data ?: return@launch
                                                )
                                                viewModel.onSignInResult(signInResult)
                                            }
                                        }
                                    }
                                )

                                LaunchedEffect(key1 = state.isSignInSuccessful) {
                                    if(state.isSignInSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Sign in is successful",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate(Routes.HERO_LIST_SCREEN)
                                        viewModel.resetState()
                                    }
                                }

                                MarvelStartScreen(
                                    navController = navController,
                                    state = state,
                                    onGetStartedClick = {
                                        lifecycleScope.launch {
                                            val signInIntentSender = googleAuthUiClient.signIn()
                                            launcher.launch(
                                                IntentSenderRequest.Builder(
                                                    signInIntentSender ?: return@launch
                                                ).build()
                                            )
                                        }
                                    }
                                )

                            }
                            composable(
                                route = Routes.HERO_LIST_SCREEN
                            ){
                                HeroListScreen(
                                    navController = navController,
                                    userData = googleAuthUiClient.getSignedInUser(),
                                    googleAuthUiClient = googleAuthUiClient
                                )
                            }
                            composable(
                                route = Routes.SETTINGS_SCREEN
                            ){
                                SettingsScreen(navController = navController)
                            }
                            composable(
                                route = Routes.SEARCH_SCREEN
                            ){
                                SearchScreen(navController = navController)
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
                            composable(
                                route = "${Routes.FILM_SCREEN}/{filmId}/{filmName}/{filmImage}",
                                arguments = listOf(
                                    navArgument("filmId"){
                                        type = NavType.IntType
                                    },
                                    navArgument("filmName"){
                                        type = NavType.StringType
                                    },
                                    navArgument("filmImage"){
                                        type = NavType.StringType
                                    }
                                )
                            ){
                                val filmId = remember {
                                    it.arguments?.getInt("filmId")
                                }
                                val filmName = remember {
                                    it.arguments?.getString("filmName")
                                }
                                val filmImage = remember {
                                    it.arguments?.getString("filmImage")
                                }
                                FilmScreen(
                                    navController = navController,
                                    filmId = filmId,
                                    filmName = filmName,
                                    filmImage = filmImage
                                )
                            }
                            composable(
                                route = "${Routes.TVSHOW_SCREEN}/{tvShowId}/{tvShowName}/{tvShowImage}",
                                arguments = listOf(
                                    navArgument("tvShowId"){
                                        type = NavType.IntType
                                    },
                                    navArgument("tvShowName"){
                                        type = NavType.StringType
                                    },
                                    navArgument("tvShowImage"){
                                        type = NavType.StringType
                                    }
                                )
                            ){
                                val tvShowId = remember {
                                    it.arguments?.getInt("tvShowId")
                                }
                                val tvShowName = remember {
                                    it.arguments?.getString("tvShowName")
                                }
                                val tvShowImage = remember {
                                    it.arguments?.getString("tvShowImage")
                                }
                                TvShowsScreen(
                                    navController = navController,
                                    tvShowId = tvShowId,
                                    tvShowName = tvShowName,
                                    tvShowImage = tvShowImage
                                )
                            }
                            composable(
                                route = "${Routes.FAVOURITE_LIST_SCREEN}/{category}",
                                arguments = listOf(
                                    navArgument("category"){
                                        type = NavType.StringType
                                    }
                                )
                            ){
                                val category = remember {
                                    it.arguments?.getString("category")
                                }
                                if (category != null) {
                                    FavouriteListScreen(
                                        navController = navController,
                                        category = category
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
