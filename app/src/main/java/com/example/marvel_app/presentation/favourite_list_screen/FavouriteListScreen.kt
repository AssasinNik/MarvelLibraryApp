package com.example.marvel_app.presentation.favourite_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.marvel_app.R
import com.example.marvel_app.data.models.SearchResultEntry
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.RedColor
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.example.marvel_app.util.Routes
import kotlinx.coroutines.Dispatchers

@Composable
fun FavouriteListScreen(
    navController: NavController,
    viewModel: FavouriteListScreenViewModel = hiltViewModel(),
    category: String
){

    LaunchedEffect(key1 = 1) {
        viewModel.getByCategory(category)
    }

    val isLoading = remember {
        viewModel.isLoading
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row {
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                IconButton(
                    onClick = { navController.navigate(Routes.FAVOURITES_SCREEN) },
                    modifier = Modifier
                        .padding(top = 40.dp, start = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back To Favourite Screen",
                        tint = Color.White,
                        modifier = Modifier
                            .size(35.dp),
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Favourites",
                    style = TextStyle(
                        fontFamily = Poppins,
                        color = Color.White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 40.dp)
                )
            }
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
        ){
            Spacer(modifier = Modifier.height(20.dp))
            if(isLoading.value){
                Spacer(modifier = Modifier.height(20.dp))
                CircularProgressIndicator(
                    color = SearchBorderColor,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            else{
                ResultSection(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}



@Composable
fun ResultSection(
    navController: NavController,
    viewModel: FavouriteListScreenViewModel
){
    val result by remember {
        viewModel.heroList
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(result) { index, result ->
            ResultEntry(result = result){
                when (result.type) {
                    "heroes" -> {
                        val encodedUrl = result.imageUrl?.replace("/", "%2F")
                        navController.navigate(
                            "${Routes.CHARACTER_SCREEN}/${result.number}/${result.name}/${encodedUrl}"
                        )
                    }
                    "comics" -> {
                        val encodedUrl = result.imageUrl?.replace("/", "%2F")
                        navController.navigate(
                            "${Routes.COMICS_SCREEN}/${result.number}/${result.name}/${encodedUrl}"
                        )
                    }
                    "films" -> {
                        val encodedUrl = result.imageUrl?.replace("/", "%2F")
                        navController.navigate(
                            "${Routes.FILM_SCREEN}/${result.number}/${result.name}/${encodedUrl}"
                        )
                    }
                    "tvShow" -> {
                        val encodedUrl = result.imageUrl?.replace("/", "%2F")
                        navController.navigate(
                            "${Routes.TVSHOW_SCREEN}/${result.number}/${result.name}/${encodedUrl}"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultEntry(
    result: SearchResultEntry,
    onTap:() -> Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 10.dp)
            .clickable {
                onTap()
            }
    ){
        val placeholder = R.drawable.gradient
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(result.imageUrl)
                .dispatcher(Dispatchers.IO)
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .memoryCacheKey(result.imageUrl)
                .diskCacheKey(result.imageUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = result.name,
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.None,
            modifier = Modifier
                .height(170.dp)
                .width(110.dp)
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(10.dp)) // Применение закругленных углов
                .border(
                    width = 2.dp,
                    color = RedColor,
                    shape = RoundedCornerShape(10.dp) // Соответствие закругленной форме
                )
                .align(Alignment.CenterVertically),
            loading = {
                CircularProgressIndicator(
                    color = SearchBorderColor
                )
            }
        )
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(RedColor)

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = result.name.toString(),
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 17.sp
                ),
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
            result.description?.let {
                if (it.length>100){
                    Text(
                        text = it.take(100)+"...",
                        style = TextStyle(
                            fontFamily = Poppins,
                            color = Color.White,
                            fontWeight = FontWeight.Thin,
                            fontSize = 15.sp
                        ),
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    )
                }
                else{
                    Text(
                        text = it,
                        style = TextStyle(
                            fontFamily = Poppins,
                            color = Color.White,
                            fontWeight = FontWeight.Thin,
                            fontSize = 15.sp
                        ),
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    )
                }
            }
        }
    }
}