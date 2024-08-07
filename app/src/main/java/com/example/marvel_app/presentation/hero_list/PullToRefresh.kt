package com.example.marvel_app.presentation.hero_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.marvel_app.data.models.HeroesListEntry
import com.example.marvel_app.ui.theme.SearchBorderColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PullToRefreshLazyColumn(
    viewModel: HeroListScreenViewModel,
    modifier: Modifier = Modifier,
    heroList: List<HeroesListEntry>,
    navController: NavController,
    lazyListState: LazyListState = rememberLazyListState()
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
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(16.dp),
            modifier = modifier
        ) {
            val itemCount = if(heroList.size%2 == 0){
                heroList.size / 2
            }else{
                heroList.size / 2 +1
            }
            items(itemCount){
                HeroRow(rowIndex = it, list = heroList, navController = navController)
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