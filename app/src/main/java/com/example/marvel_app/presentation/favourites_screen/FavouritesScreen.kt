package com.example.marvel_app.presentation.favourites_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.marvel_app.R
import com.example.marvel_app.ui.theme.GrayColor
import com.example.marvel_app.ui.theme.IconsColor
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.util.Routes


@Composable
fun FavouritesScreen(
    navController: NavController
) {
    BoxWithConstraints {
        val height = maxHeight
        val width = maxWidth
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Text(
                text = "Favourites❤\uFE0F",
                style = TextStyle(
                    fontFamily = Poppins,
                    color = Color.White,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 15.dp, bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            CategoryList(width, navController)
        }
    }
}

@Composable
fun CategoryList(width: Dp, navController: NavController) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            CategoryBox(nameCategory = "Films", width, R.drawable.ticket){
                navController.navigate("${Routes.FAVOURITE_LIST_SCREEN}/films")
            }
            Spacer(modifier = Modifier.weight(1f))
            CategoryBox(nameCategory = "Comics", width, R.drawable.book){
                navController.navigate("${Routes.FAVOURITE_LIST_SCREEN}/comics")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            CategoryBox(nameCategory = "TvShows", width, R.drawable.video){
                navController.navigate("${Routes.FAVOURITE_LIST_SCREEN}/tvShow")
            }
            Spacer(modifier = Modifier.weight(1f))
            CategoryBox(nameCategory = "Heroes", width, R.drawable.group){
                navController.navigate("${Routes.FAVOURITE_LIST_SCREEN}/heroes")
            }
        }
    }
}

@Composable
fun CategoryBox(nameCategory: String, width: Dp, icon: Int, onTap:() -> Unit) {
    Column(
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(GrayColor)
            .padding(8.dp)
            .width(width / 3 + width / 15)
            .clickable {
                onTap()
            }
    ) {
        Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Icon",
                tint = IconsColor,
                modifier = Modifier
                    .align(Alignment.End)
                    .size(70.dp)
            )
            Text(
                text = nameCategory,
                style = TextStyle(
                    fontFamily = Poppins,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                ),
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}