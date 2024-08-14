package com.example.marvel_app.presentation.comics_screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
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
import coil.compose.rememberImagePainter
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
import kotlinx.coroutines.withContext


@Composable
fun ComicsScreen(
    navController: NavController,
    viewModel: ComicsScreenViewModel = hiltViewModel(),
    comicsId: Int?,
    comicsName: String?,
    comicsImage: String?,
){
    LaunchedEffect(key1 = comicsId, key2 = comicsName) {
        viewModel.loadComicsInfo(comicsId)
        viewModel.checkFavourite(comicsName)
    }

    val comics by viewModel.comics.collectAsState()

    val isFavorite by viewModel.isFavorite.collectAsState()

    val icon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
    val placeholder = R.drawable.gradient

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
                    .height(maxHeight / 3)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .blur(radius = 10.dp)
            )
        } else {
            if (comicsImage != null) {
                BlurImageFromUrl(
                    imageUrl = comicsImage,
                    modifier = Modifier
                        .height(maxHeight / 3)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        }


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
                    .height(130.dp)
            ){
                Box (
                    modifier = Modifier
                        .padding(top = 30.dp, start = 10.dp, bottom = 8.dp)
                        .size(45.dp)
                        .background(color = Color.DarkGray, shape = CircleShape)
                    ,
                    contentAlignment = Alignment.Center
                ){
                    IconButton(onClick = {
                        navController.navigate(Routes.HERO_LIST_SCREEN) },
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
                    Box (
                        modifier = Modifier
                            .padding(top = 30.dp, end = 8.dp)
                            .size(45.dp)
                            .background(color = Color.DarkGray, shape = CircleShape)
                        ,
                        contentAlignment = Alignment.Center
                    ){
                        IconButton(onClick = {
                            if(isFavorite){
                                viewModel.deleteFavorite(comics?.comicsName)
                            }
                            else{
                                viewModel.addToFavourites(
                                    comics?.comicsName,
                                    comics?.comicsImage,
                                    comics?.comicsDescription,
                                    comics?.number
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
                    Box (
                        modifier = Modifier
                            .padding(top = 10.dp, end = 8.dp)
                            .size(45.dp)
                            .background(color = Color.DarkGray, shape = CircleShape)
                        ,
                        contentAlignment = Alignment.Center
                    ){
                        IconButton(onClick = {

                        },
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "Download",
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

@Composable
fun BlurImageFromUrl(
    imageUrl: String,
    modifier: Modifier = Modifier,
    blurRadius: Float = 10f // Adjust blur radius here
) {
    val context = LocalContext.current
    val painter = rememberImagePainter(data = imageUrl)

    val originalBitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val blurredBitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(painter) {
        withContext(Dispatchers.IO) {
            val drawable = painter.imageLoader.execute(painter.request).drawable
            if (drawable is BitmapDrawable) {
                originalBitmap.value = drawable.bitmap

                // Apply blur if the API level is less than 31
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    blurredBitmap.value = originalBitmap.value?.let { bitmap ->
                        blurBitmap(context, bitmap, blurRadius)
                    }
                } else {
                    blurredBitmap.value = originalBitmap.value
                }
            }
        }
    }

    blurredBitmap.value?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

fun blurBitmap(context: Context, bitmap: Bitmap, radius: Float): Bitmap {
    // Convert the bitmap to ARGB_8888 if it's in HARDWARE config
    val safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    val outputBitmap = Bitmap.createBitmap(safeBitmap)

    val renderScript = RenderScript.create(context)
    val input = Allocation.createFromBitmap(renderScript, safeBitmap)
    val output = Allocation.createFromBitmap(renderScript, outputBitmap)

    val blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    blurScript.setRadius(radius)
    blurScript.setInput(input)
    blurScript.forEach(output)

    output.copyTo(outputBitmap)
    renderScript.destroy()

    return outputBitmap
}
