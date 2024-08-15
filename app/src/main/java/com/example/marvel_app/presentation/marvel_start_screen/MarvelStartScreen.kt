package com.example.marvel_app.presentation.marvel_start_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.imageLoader
import com.example.marvel_app.R
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.RedColor
import com.example.marvel_app.ui.theme.SearchColor
import com.example.marvel_app.util.Routes
import kotlinx.coroutines.delay


@Composable
fun MarvelStartScreen(navController: NavController) {
    val color: Brush = Brush.verticalGradient(
        colors = listOf(
            BackGround,
            RedColor
        ),
        startY = 50f
    )

    var showHello by remember { mutableStateOf(false) }
    var showDescription by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        delay(500)
        showHello = true
        delay(1200)
        showDescription = true
        delay(1200)
        showButton = true
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(BackGround)
    ) {
        val height = maxHeight
        Box(modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .background(color)
                .padding(start = 16.dp, end = 16.dp)
                .systemBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(height / 2))

            AnimatedVisibility(
                visible = showHello,
                enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(animationSpec = tween(1000)) { it / 2 }
            ) {
                Text(
                    text = "Hello\uD83D\uDC4B",
                    style = TextStyle(
                        fontFamily = Poppins,
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = showDescription,
                enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(animationSpec = tween(1000)) { it / 3 }
            ) {
                Text(
                    text = "Welcome for our app about different heroes from Marvel Comics",
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

            Spacer(modifier = Modifier.height(height / 8))

            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(animationSpec = tween(1000)) { it / 4 }
            ) {
                Button(
                    onClick = {
                        navController.navigate(Routes.HERO_LIST_SCREEN)
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
