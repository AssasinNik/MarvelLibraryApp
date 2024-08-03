package com.example.marvel_app.presentation.hero_list

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.marvel_app.R
import com.example.marvel_app.data.models.HeroesListEntry
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.GrayColor
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.RedColor
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.example.marvel_app.ui.theme.SearchColor
import com.example.marvel_app.ui.theme.SearchTextColor
import com.example.marvel_app.ui.theme.WhiteColor

@Composable
fun HeroListScreen(
    navController: NavController,
    viewModel: HeroListScreenViewModel = hiltViewModel()
){

    LaunchedEffect(key1 = true) {
        viewModel.getHeroList()
    }

    Surface(
        color = BackGround,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            IconButton(
                onClick = { navController.navigate("SettingsScreen") },
                modifier = Modifier
                    .padding(top = 40.dp, start = 20.dp, end = 10.dp)
                    .align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Go To SettingsScreen",
                    tint = SearchTextColor,
                    modifier = Modifier
                        .size(35.dp),
                )
            }
            Image(
                painter = painterResource(id = R.drawable.marvel_logo),
                contentDescription = "MarvelLogo",
                modifier = Modifier
                    .height(100.dp)
                    .width(200.dp)
            )
            Text(
                modifier = Modifier,
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
            Spacer(modifier = Modifier.height(16.dp))
            HeroList(navController = navController)
        }
    }
}
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
){
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        TextField(
            modifier = Modifier
                .border(2.dp, color = SearchBorderColor, shape = RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp)),
            value = text,
            placeholder = {
                Text(modifier = Modifier
                    .align(Alignment.Center),
                    text = hint,
                    style = TextStyle(
                        color = SearchTextColor,
                        fontSize = 18.sp)
                )
            },
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = SearchColor,
                textColor = SearchTextColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = SearchTextColor
            ),
            textStyle = TextStyle(color = WhiteColor, fontSize = 18.sp),
        )
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
    val endReached by remember{
        viewModel.endReached
    }
    val loadError by remember{
        viewModel.loadError
    }
    val isLoading by remember{
        viewModel.isLoading
    }
    val isSearching by remember {
        viewModel.isSearching
    }
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    /*
    PullToRefreshLazyColumn(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing=true
            viewModel.loadHeroPaginated()
            isRefreshing=false
                    },
        heroList = heroList,
        navController = navController
    )
     */
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if (heroList.size % 2 == 0) {
            heroList.size / 2
        } else {
            heroList.size / 2 + 1
        }
        items(itemCount) {
            HeroRow(rowIndex = it, list = heroList, navController = navController)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(isLoading){
            CircularProgressIndicator(color = SearchBorderColor)
        }
        if(loadError.isNotEmpty()){
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
                val encodedUrl = entry.imageUrl.replace("/", "%2F")
                navController.navigate(
                    "HeroDetailScreen/${entry.number}/${entry.characterName}/${encodedUrl}"
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
fun HeroRow(
    rowIndex: Int,
    list: List<HeroesListEntry>,
    navController: NavController
){
    Column {
        Row {
            MarvelEntry(
                entry = list[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(list.size >= rowIndex * 2 + 2){
                MarvelEntry(
                    entry = list[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            }else{
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
){
    Column {
        Text(text = error, color= Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {onRetry()}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Retry")
        }
    }
}


