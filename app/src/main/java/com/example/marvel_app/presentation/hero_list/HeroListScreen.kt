package com.example.marvel_app.presentation.hero_list

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.marvel_app.R
import com.example.marvel_app.data.models.HeroesListEntry
import com.example.marvel_app.data.remote.responses.Google.UserData
import com.example.marvel_app.presentation.marvel_start_screen.GoogleAuthUiClient
import com.example.marvel_app.presentation.reusable.SearchBar
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.GrayColor
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.RedColor
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.example.marvel_app.ui.theme.SearchTextColor
import com.example.marvel_app.util.Routes
import timber.log.Timber

@Composable
fun HeroListScreen(
    navController: NavController,
    viewModel: HeroListScreenViewModel = hiltViewModel(),
    userData: UserData?,
){

    LaunchedEffect(key1 = true) {
        viewModel.getHeroList()
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(20.dp))
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 20.dp, end = 20.dp)
                .align(Alignment.CenterHorizontally)
            ,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userData?.userImage)
                    .build(),
                contentDescription = userData?.userName,
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.None,
                modifier = Modifier
                    .size(45.dp)
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = RedColor,
                        shape = CircleShape
                    )
                    .clickable {
                        navController.navigate(Routes.SETTINGS_SCREEN)
                    }
                ,
                loading = {
                    CircularProgressIndicator(color = SearchBorderColor)
                }
            )

            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { navController.navigate(Routes.SEARCH_SCREEN) },
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Go To SearchComicsScreen",
                        tint = SearchTextColor,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.marvel_logo),
                contentDescription = "MarvelLogo",
                modifier = Modifier
                    .height(100.dp)
                    .width(200.dp)
            )
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, "https://www.marvel.com")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)

            val context = LocalContext.current
            Text(
                modifier = Modifier
                    .clickable {
                        startActivity(context, shareIntent, null)
                    },
                text = "Data provided by Marvel. © 2024 MARVEL",
                style = TextStyle(
                    color = Color(0xFFB6B6B6),
                    fontSize = 15.sp
                )
            )
            SearchBar(
                hint = "Search",
                modifier = Modifier.padding(15.dp)
            ){
                viewModel.searchMarvelList(it)
            }
            Spacer(modifier = Modifier.height(8.dp))
            val isLoading by remember {
                viewModel.isLoading
            }
            Text(
                text = "Most Popular\uD83D\uDD25",
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
            HeroList(navController = navController)
        }
    }
}


@Composable
fun HeroList(
    navController: NavController,
    viewModel: HeroListScreenViewModel = hiltViewModel()
){
    val heroList by remember {
        viewModel.heroList
    }
    val loadError by remember{
        viewModel.loadError
    }
    val isLoading by remember {
        viewModel.isLoading
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(isLoading){
            CircularProgressIndicator(color = SearchBorderColor)
        }
        else if(!isLoading){
            PullToRefreshLazyGrid(
                viewModel = viewModel,
                heroList = heroList,
                navController = navController
            )
        }
        else if(loadError.isNotEmpty()){
            RetrySection(error = loadError) {
                viewModel.getHeroList()
            }
        }
    }

}


@Composable
fun MarvelEntry(
    entry: HeroesListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
){
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(GrayColor)
            .clickable {
                var encodedName: String
                if(entry.characterName.contains("/")){
                    encodedName = entry.characterName.replace("/", "%2F")
                }
                else{
                    encodedName = entry.characterName
                }
                val encodedUrl = entry.imageUrl.replace("/", "%2F")
                navController.navigate(
                    "${Routes.CHARACTER_SCREEN}/${entry.number}/${encodedName}/${encodedUrl}"
                )
            }
    ){
        Column(modifier = Modifier.align(Alignment.Center)) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.imageUrl)
                    .build(),
                contentDescription = entry.characterName,
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.None,
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.CenterHorizontally)
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .border(
                        width = 2.dp,
                        color = RedColor,
                        shape = CircleShape
                    )
                    .clip(CircleShape) ,
                loading = {
                    CircularProgressIndicator(color = SearchBorderColor)
                }
            )
            Text(
                text = entry.characterName,
                style = TextStyle(
                    fontFamily = Poppins,
                    color = Color.White,
                    fontSize = 20.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
){
    Column {
        Text(text = error, color= Color.White, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {onRetry()},
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = SearchBorderColor,
                contentColor = Color.White
            )
        ) {
            Text(text = "Retry")
        }
    }
}


