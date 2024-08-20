package com.example.marvel_app.presentation.reusable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvel_app.ui.theme.SearchBorderColor
import com.example.marvel_app.ui.theme.SearchColor
import com.example.marvel_app.ui.theme.SearchTextColor
import com.example.marvel_app.ui.theme.WhiteColor

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