package com.example.marvel_app.presentation.settings_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.example.marvel_app.R
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.RedColor
import com.example.marvel_app.util.Routes

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsScreenViewModel = hiltViewModel(),
){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()

            ){
                IconButton(
                    onClick = { navController.navigate(Routes.HERO_LIST_SCREEN) },
                    modifier = Modifier.padding(top = 40.dp, start = 20.dp)

                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Go To Home",
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            val context = LocalContext.current
            Button(
                onClick = {
                    context.imageLoader.diskCache?.clear()
                    context.imageLoader.memoryCache?.clear()
                    viewModel.deleteAll()
                          },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = RedColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp), // Задает закругленные углы
                modifier = Modifier.padding(100.dp) // Задает отступы
            ) {
                Text(
                    text = "Clear Cache",
                    style = TextStyle(
                        fontFamily = Poppins,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
        }
}