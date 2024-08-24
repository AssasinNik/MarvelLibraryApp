package com.example.marvel_app.presentation.film_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.marvel_app.R
import com.example.marvel_app.data.models.FilmEntry
import com.example.marvel_app.presentation.reusable.BlurImageFromUrl
import com.example.marvel_app.presentation.reusable.BrightcovePlayer
import com.example.marvel_app.presentation.reusable.YoutubeVideoPlayer
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.example.marvel_app.util.Routes
import com.kevinnzou.web.AccompanistWebViewClient
import com.kevinnzou.web.LoadingState
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

@Composable
fun FilmScreen(
    navController: NavController,
    viewModel: FilmScreenViewModel = hiltViewModel(),
    filmId: Int?,
    filmName: String?,
    filmImage: String?
){

    LaunchedEffect(key1 = 1) {
        if (filmName != null) {
            viewModel.loadFilmInfo(filmName)
        }
        viewModel.checkFavourite(filmName)
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGround)
    ) {
        val height = maxHeight
        val screenHeight = maxHeight
        val rectHeight = screenHeight / 2
        val cornerRadius = 20.dp
        val imageSize = 120.dp
        val imageOffset = 20.dp

        val isFavorite by remember {
            viewModel.isFavorite
        }
        val isLoading by remember {
            viewModel.isLoading
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

            OverviewBox(result = film, viewModel = viewModel)
        }
        
        val contentHeight = height + 250.dp + imageSize + imageOffset
        Box(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center)
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
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 250.dp)
                    .align(Alignment.TopCenter)
            ) {
                Text(
                    text = filmName.toString(),
                    style = TextStyle(
                        fontFamily = Poppins,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 30.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                )
                Spacer(modifier = Modifier.height(25.dp))
                if (!isLoading){
                    if (film?.description!=""){
                        Text(
                            text = "Description",
                            style = TextStyle(
                                fontFamily = Poppins,
                                color = Color.White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 15.dp, bottom = 8.dp)
                        )
                        Text(
                            text = film?.description.toString(),
                            style = TextStyle(
                                fontFamily = Poppins,
                                color = Color.White,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp
                            ),
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 13.dp, end = 13.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    if(film?.trailerUrl != ""){
                        Text(
                            text = "Trailer",
                            style = TextStyle(
                                fontFamily = Poppins,
                                color = Color.White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 20.dp, bottom = 8.dp)
                        )
                        TrailerVideo(videoUrl = film?.trailerUrl)
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
                else{
                    Spacer(modifier = Modifier.height(20.dp))
                    CircularProgressIndicator(
                        color = SearchBorderColor,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
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
                            navController.popBackStack()
                        },
                        modifier = Modifier.size(35.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back-To-Previous",
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
                                if(isFavorite){
                                    viewModel.deleteFavorite(filmName)
                                }
                                else{
                                    viewModel.addToFavourites(
                                        filmName,
                                        filmImage,
                                        film?.description,
                                        filmId
                                    )
                                }
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
                    if(!isLoading){
                        if (film?.shareLink != null || film?.shareLink != ""){
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, film?.shareLink)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)

                            val context = LocalContext.current

                            Box (
                                modifier = Modifier
                                    .padding(top = 10.dp, end = 8.dp)
                                    .size(45.dp)
                                    .background(color = Color.DarkGray, shape = CircleShape)
                                ,
                                contentAlignment = Alignment.Center
                            ){
                                IconButton(onClick = {
                                    startActivity(context, shareIntent, null)
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

    }
}


@Composable
fun OverviewBox(
    result: FilmEntry?,
    viewModel: FilmScreenViewModel
) {
    val isLoading by remember {
        viewModel.isLoading
    }
    if(!isLoading){
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
                            fontSize = 15.sp
                        ),
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 5.dp)
                    )
                    Text(
                        text = "Director: ${result?.directedBy}",
                        style = TextStyle(
                            fontFamily = Poppins,
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp
                        ),
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 5.dp)
                    )
                    Text(
                        text = "Duration: ${result?.duration} min",
                        style = TextStyle(
                            fontFamily = Poppins,
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp
                        ),
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 5.dp)
                    )
                }
            }
        }
    }
    else{
        Spacer(modifier = Modifier.height(20.dp))
        CircularProgressIndicator(
            color = SearchBorderColor,
            modifier = Modifier
                .padding(top = 10.dp)
        )
    }
}

@Composable
fun TrailerVideo(
    videoUrl:String?
){
    if (videoUrl != null) {
        if(videoUrl.contains("brightcove")){
            BrightcovePlayer(initialUrl = videoUrl, context = LocalContext.current)
        }
        else if (videoUrl.contains("youtu")){
            val lastSlashIndex = videoUrl.lastIndexOf('/')
            val id = videoUrl.substring(lastSlashIndex + 1)
            YoutubeVideoPlayer(videoId = id, context = LocalContext.current)
        }
    }
}