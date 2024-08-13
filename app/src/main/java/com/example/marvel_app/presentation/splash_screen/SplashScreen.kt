package com.example.marvel_app.presentation.splash_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.marvel_app.R
import com.example.marvel_app.ui.theme.BackGround

@Composable
fun SplashScreen(onPush : () -> Unit){
    Box(
        Modifier
            .fillMaxSize()
            .background(BackGround)
    ) {
        Image(
            painter = painterResource(id = R.drawable.marvel_logo),
            contentDescription = "MarvelLogo",
            modifier = Modifier
                .height(100.dp)
                .width(200.dp)
                .align(Alignment.Center)
                .clickable {
                    onPush()
                }
        )
    }
}