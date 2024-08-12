package com.example.marvel_app.presentation.comics_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.marvel_app.data.models.CharacterEntry
import com.example.marvel_app.data.models.ComicsItemEntry
import com.example.marvel_app.data.models.Creators
import com.example.marvel_app.presentation.hero_list.RetrySection
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.RedColor
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.example.marvel_app.util.Routes
import kotlinx.coroutines.Dispatchers


@Composable
fun ComicsScreen(
    navController: NavController,
    viewModel: ComicsScreenViewModel = hiltViewModel(),
    comicsId: Int?,
    comicsName: String?,
    comicsImage: String?,
){
    LaunchedEffect(key1 = comicsId) {
        viewModel.loadComicsInfo(comicsId)
    }

    val comics by viewModel.comics.collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(RedColor)
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val rectHeight = screenHeight / 2
        val cornerRadius = 20.dp
        val imageSize = 120.dp
        val imageOffset = 20.dp

        val contentHeight = screenHeight + 250.dp + imageSize + imageOffset
        Box(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(), true)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                if (screenWidth <= 480.dp) {
                    Spacer(modifier = Modifier.height(screenHeight / 2 - imageSize / 2 - imageOffset - 250.dp))
                    val placeholder = R.drawable.gradient
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(comicsImage)
                            .dispatcher(Dispatchers.IO)
                            .placeholder(placeholder)
                            .error(placeholder)
                            .fallback(placeholder)
                            .memoryCacheKey(comicsImage)
                            .diskCacheKey(comicsImage)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = comicsName,
                        contentScale = ContentScale.Crop,
                        filterQuality = FilterQuality.None,
                        modifier = Modifier
                            .height(235.dp)
                            .width(150.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 8.dp)
                        ,
                        loading = {
                            CircularProgressIndicator(
                                color = SearchBorderColor
                            )
                        }
                    )
                } else {
                    Spacer(modifier = Modifier.height(screenHeight / 2 - imageSize / 2 - imageOffset - 400.dp))
                    val placeholder = R.drawable.gradient
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(comicsImage)
                            .dispatcher(Dispatchers.IO)
                            .placeholder(placeholder)
                            .error(placeholder)
                            .fallback(placeholder)
                            .memoryCacheKey(comicsImage)
                            .diskCacheKey(comicsImage)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = comicsName,
                        contentScale = ContentScale.Crop,
                        filterQuality = FilterQuality.None,
                        modifier = Modifier
                            .height(270.dp)
                            .width(180.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 8.dp)
                        ,
                        loading = {
                            CircularProgressIndicator(
                                color = SearchBorderColor
                            )
                        }
                    )
                }
                Text(
                    text = comicsName.toString(),
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 25.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.size(10.dp))
                ComicsInfo(
                    comics = comics,
                    viewModel = viewModel,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    navController = navController
                )
            }
        }
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
                .background(Color.Transparent)
        ){
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .height(100.dp)
            ){
                Box (
                    modifier = Modifier
                        .padding(top = 30.dp, start = 10.dp, bottom = 8.dp)
                        .size(40.dp)
                        .background(color = Color.DarkGray, shape = CircleShape)
                    ,
                    contentAlignment = Alignment.Center
                ){
                    IconButton(onClick = {
                        navController.navigate(Routes.HERO_LIST_SCREEN) },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back-To-HeroListScreen",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ComicsInfo(
    comics: ComicsItemEntry?,
    viewModel: ComicsScreenViewModel,
    modifier: Modifier,
    navController: NavController
){
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        val isLoading by viewModel.isLoading.collectAsState()
        val loadError by viewModel.loadError.collectAsState()
        if(!isLoading){
            if (comics != null) {
                Text(
                    text = comics.comicsDescription.toString(),
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 16.sp
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Creators",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontSize = 23.sp
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .align(Alignment.Start)
                )
                val creators = comics.creators
                if (creators != null) {
                    for(creator in creators){
                        CreatorEntity(creator = creator)
                    }
                }
                Text(
                    text = "Characters",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontSize = 23.sp
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .align(Alignment.Start)
                )
                val characters = comics.characterEntry
                if(characters!=null){
                    for(character in characters){
                        HeroEntry(hero = character) {
                            val encodedUrl = character.imageUrl.replace("/", "%2F")
                            navController.navigate(
                                "${Routes.CHARACTER_SCREEN}/${character.number}/${character.characterName}/${encodedUrl}"
                            )
                        }
                    }
                }
            }
        }
        else if(isLoading){
            Spacer(modifier = Modifier.height(20.dp))
            CircularProgressIndicator(
                color = SearchBorderColor,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        else if(loadError!!.isNotEmpty()){
            RetrySection(error = loadError!!) {
                viewModel.loadComicsInfo(comics!!.number)
            }
        }
    }
}

@Composable
fun CreatorEntity(
    creator: Creators
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 8.dp)
    ){
        Box (
            modifier = Modifier
                .size(8.dp)
                .background(color = RedColor, shape = CircleShape)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = creator.name + " " + "(${creator.role})",
            style = TextStyle(
                fontFamily = Poppins, 
                fontWeight = FontWeight.Normal, 
                color = Color.White, 
                fontSize = 17.sp
            ), 
            textAlign = TextAlign.Left, 
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun HeroEntry(
    hero: CharacterEntry,
    onHeroClick: () -> Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp)
            .clickable { onHeroClick() }
    ){
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(hero.imageUrl)
                .build(),
            contentDescription = hero.characterName,
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.None,
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.CenterVertically)
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
            text = hero.characterName,
            style = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                fontSize = 17.sp
            ),
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
        )
    }
}