package com.example.marvel_app.presentation.marvel_start_screen

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.RedColor
import com.example.marvel_app.ui.theme.SearchColor
import com.example.marvel_app.util.Routes
import kotlinx.coroutines.delay

@Composable
fun MarvelStartScreen(
    navController: NavController,
    viewModel: MarvelStartViewModel = hiltViewModel()
) {
    var showHello by remember { mutableStateOf(false) }
    var showDescription by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }
    var gradientAlpha by remember { mutableStateOf(0f) }
    val context = LocalContext.current

    val isSignInSuccessful by viewModel.isSignInSuccessful.observeAsState(false)
    val signInError by viewModel.signInError.observeAsState(null)

    val animatedGradientAlpha by animateFloatAsState(targetValue = gradientAlpha, animationSpec = tween(durationMillis = 3000))

    LaunchedEffect(signInError) {
        signInError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(isSignInSuccessful) {
        if (isSignInSuccessful) {
            Toast.makeText(context, "Sign in is successful", Toast.LENGTH_LONG).show()
            navController.navigate(Routes.HERO_LIST_SCREEN)
            viewModel.resetState()
        }
    }

    LaunchedEffect(Unit) {
        delay(500)
        gradientAlpha = 1f
        delay(1200)
        showHello = true
        delay(1200)
        showDescription = true
        delay(1200)
        showButton = true
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.handleSignInResult(result.data)
            }
        }
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGround)
    ) {
        val height = maxHeight
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = animatedGradientAlpha)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(BackGround, RedColor)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(height / 2))

            AnimatedVisibility(
                visible = showHello,
                enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(animationSpec = tween(1000)) { it / 2 }
            ) {
                WavingEmojiText()
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = showDescription,
                enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(animationSpec = tween(1000)) { it / 3 }
            ) {
                Text(
                    text = "Welcome to our app about different heroes from Marvel Comics",
                    style = TextStyle(
                        fontFamily = Poppins,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))

            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(animationSpec = tween(1000)) { it / 4 }
            ) {
                Button(
                    onClick = {
                        viewModel.signIn(launcher)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = SearchColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(65.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Getting Started",
                        style = TextStyle(
                            fontFamily = Poppins,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun WavingEmojiText() {
    var startWaving by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (startWaving) 0f else 1f,
        animationSpec = keyframes {
            durationMillis = 1800
            0f at 0 with LinearEasing
            60f at 800 with LinearEasing
            0f at 1800 with LinearEasing
        }
    )

    LaunchedEffect(Unit) {
        delay(700)
        startWaving = true
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Hello",
            style = TextStyle(
                fontFamily = Poppins,
                fontSize = 40.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "\uD83D\uDC4B",
            modifier = Modifier.rotate(rotation),
            style = TextStyle(
                fontFamily = Poppins,
                fontSize = 40.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}
