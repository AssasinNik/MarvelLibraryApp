package com.example.marvel_app.presentation.film_screen

import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.marvel_app.R
import com.example.marvel_app.data.models.FilmEntry
import com.example.marvel_app.presentation.reusable.BlurImageFromUrl
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.util.Routes
import kotlinx.coroutines.Dispatchers

@Composable
fun FilmScreen(
    navController: NavController,
    viewModel: FilmScreenViewModel = hiltViewModel(),
    filmId: Int?,
    filmName: String?,
    filmImage: String?
){
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGround)
    ) {
        val height = maxHeight
        val width = maxWidth

        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val rectHeight = screenHeight / 2
        val cornerRadius = 20.dp
        val imageSize = 120.dp
        val imageOffset = 20.dp

        val isFavorite by remember {
            viewModel.isFavorite
        }

        val film by viewModel.film.collectAsState()

        val icon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder

        val placeholder = R.drawable.gradient

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(filmImage)
                    .dispatcher(Dispatchers.IO)
                    .placeholder(placeholder)
                    .error(placeholder)
                    .fallback(placeholder)
                    .memoryCacheKey(filmImage)
                    .diskCacheKey(filmImage)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = filmName,
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.None,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = 10.dp)
            )
        } else {
            if (filmImage != null) {
                BlurImageFromUrl(
                    imageUrl = filmImage,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 170.dp)
        ){
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(filmImage)
                    .dispatcher(Dispatchers.IO)
                    .placeholder(placeholder)
                    .error(placeholder)
                    .fallback(placeholder)
                    .memoryCacheKey(filmImage)
                    .diskCacheKey(filmImage)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = filmName,
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.None,
                modifier = Modifier
                    .height(240.dp)
                    .width(180.dp)
                    .padding(start = 25.dp)
            )

            Spacer(modifier = Modifier.size(20.dp))

            OverviewBox(result = film)
        }
        
        val contentHeight = height + 250.dp + imageSize + imageOffset
        Box(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState(), true)
            .padding(top = 250.dp)
        ){
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(contentHeight)
            ) {
                drawRoundRect(
                    color = BackGround,
                    topLeft = Offset(0f, screenHeight.toPx() / 2 - rectHeight.toPx() / 2),
                    size = Size(size.width, contentHeight.toPx()*2),
                    cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
                )
            }
        }


        Box (
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
                .background(Color.Transparent)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .height(130.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 30.dp, start = 10.dp, bottom = 8.dp)
                        .size(45.dp)
                        .background(color = Color.DarkGray, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.HERO_LIST_SCREEN)
                        },
                        modifier = Modifier.size(35.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back-To-HeroListScreen",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                Column {
                    Box(
                        modifier = Modifier
                            .padding(top = 30.dp, end = 8.dp)
                            .size(45.dp)
                            .background(color = Color.DarkGray, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {

                            },
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Add-To-Favourites",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 10.dp, end = 8.dp)
                            .size(45.dp)
                            .background(color = Color.DarkGray, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {

                            },
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "Share",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun OverviewBox(
    result: FilmEntry?
) {
    Column(
        modifier = Modifier
            .padding(end = 15.dp)
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
    ) {
        Box(
            modifier = Modifier
                .background(BackGround)
                .height(120.dp)
                .width(170.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Overview",
                    style = TextStyle(
                        fontFamily = Poppins,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Date: ${result?.releaseDate}",
                    style = TextStyle(
                        fontFamily = Poppins,
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.align(Alignment.Start).padding(start = 5.dp)
                )
                Text(
                    text = "Director: ${result?.directedBy}",
                    style = TextStyle(
                        fontFamily = Poppins,
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.align(Alignment.Start).padding(start = 5.dp)
                )
                Text(
                    text = "Duration: ${result?.duration} min",
                    style = TextStyle(
                        fontFamily = Poppins,
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.align(Alignment.Start).padding(start = 5.dp)
                )
            }
        }
    }
}