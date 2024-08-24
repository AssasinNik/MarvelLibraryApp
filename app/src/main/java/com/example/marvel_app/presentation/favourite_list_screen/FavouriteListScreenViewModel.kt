package com.example.marvel_app.presentation.favourite_list_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.local.favourites.FavouriteDao
import com.example.marvel_app.data.local.favourites.FavouritesEntity
import com.example.marvel_app.data.models.SearchResultEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteListScreenViewModel @Inject constructor(
    private val dao: FavouriteDao
) : ViewModel() {

    private lateinit var cahcedResult: Flow<List<FavouritesEntity>>

    var heroList = mutableStateOf<List<SearchResultEntry>>(listOf())


    var isLoading = mutableStateOf(false)

    fun getByCategory(category: String) {
        viewModelScope.launch {
            isLoading.value = true
            heroList.value = emptyList()
            var cachedHeroList2 = listOf<FavouritesEntity>()

            cahcedResult = dao.selectFavouritesCategory(category)
            cahcedResult.first { result ->
                cachedHeroList2 = result
                true
            }

            val heroEntries2 = mutableListOf<SearchResultEntry>()
            for (i in cachedHeroList2){
                heroEntries2.add(
                    SearchResultEntry(
                        i.name,
                        i.description,
                        i.imageUrl,
                        i.category,
                        i.id
                    )
                )
            }
            heroList.value+=heroEntries2
            isLoading.value=false
        }
    }
}