package com.example.marvel_app.presentation.hero_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.marvel_app.data.models.HeroesListEntry
import com.example.marvel_app.ui.theme.SearchBorderColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PullToRefreshLazyGrid(
    viewModel: HeroListScreenViewModel,
    modifier: Modifier = Modifier,
    heroList: List<HeroesListEntry>,
    navController: NavController,
    lazyGridState: LazyGridState = rememberLazyGridState()
) {

    var isRefreshing by remember {
        mutableStateOf(false)
    }

    val refreshScope = rememberCoroutineScope()
    fun onRefresh() = refreshScope.launch {
        isRefreshing = true
        try {
            viewModel.loadHeroPaginated(true).await()
        }finally {
            isRefreshing = false
        }
    }

    val pullToRefreshState = rememberPullRefreshState(isRefreshing, ::onRefresh)
    Box(
        modifier = Modifier.pullRefresh(state = pullToRefreshState, enabled = true)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
            state = lazyGridState,
            modifier = Modifier.fillMaxHeight()
        ) {
            items(heroList.size){
                MarvelEntry(entry = heroList[it], navController = navController)
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = SearchBorderColor
        )
    }
}