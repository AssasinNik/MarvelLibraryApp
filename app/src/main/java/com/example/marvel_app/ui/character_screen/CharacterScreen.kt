package com.example.marvel_app.ui.character_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import coil.request.ImageRequest
import com.example.marvel_app.ui.theme.BackGround
import com.example.marvel_app.ui.theme.Poppins
import com.example.marvel_app.ui.theme.RedColor
import com.example.marvel_app.ui.theme.SearchBorderColor

@Composable
fun CharacterScreen(
    navController: NavController,
    viewModel: CharacterScreenViewModel = hiltViewModel(),
    heroId: Int?
){
    LaunchedEffect(key1 = heroId) {
        viewModel.loadHeroInfo(heroId)
    }

    val comicsList by remember {
        viewModel.comicsList
    }
    val character by viewModel.character.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val loadError by viewModel.loadError.collectAsState()

    Surface(
        color = BackGround,
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier.fillMaxSize()
        ){
            IconButton(onClick = { navController.navigate("MarvelListScreen") }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back To MainScreen",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(top = 40.dp, start = 20.dp)
                        .size(35.dp),
                )
            }
            if(!isLoading){
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character?.imageUrl)
                        .build(),
                    contentDescription = character?.characterName,
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.None,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                        .border(
                            width = 2.dp,
                            color = RedColor,
                            shape = CircleShape
                        )
                        .clip(CircleShape) ,
                    loading = {
                        CircularProgressIndicator(
                            color = SearchBorderColor
                        )
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .background(RedColor)
                        .height(70.dp)
                        .width(180.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                ){
                    Text(
                        text = character?.characterName.toString(),
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Description",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 23.sp
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = character?.description.toString(),
                    style = TextStyle(
                        fontFamily = Poppins,
                        color = Color.White,
                        fontWeight = FontWeight.Thin,
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                )
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                   //items(comicsList.size)
                }
            }
            else{
                Spacer(modifier = Modifier.height(20.dp))
                CircularProgressIndicator(
                    color = SearchBorderColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally))
            }
        }
    }
}