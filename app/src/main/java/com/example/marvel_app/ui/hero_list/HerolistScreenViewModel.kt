package com.example.marvel_app.ui.hero_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.models.MarvelListEntry
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Constants.PAGE_SIZE
import com.example.marvel_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HerolistScreenViewModel @Inject constructor(
    private val heroRepository: HeroRepository
): ViewModel() {
    private var curPage = 0

    var heroList = mutableStateOf<List<MarvelListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedHeroList = listOf<MarvelListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init{
        loadHeroPaginated()
    }

    fun searchMarvelList(query: String){
        val listToSearch = if(isSearchStarting){
            heroList.value
        }else{
            cachedHeroList
        }
        viewModelScope.launch (Dispatchers.Default){
            if(query.isEmpty()){
                heroList.value = cachedHeroList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.characterName.contains(query.trim(), ignoreCase = true) || it.number.toString() == query.trim()
            }
            if(isSearchStarting){
                cachedHeroList = heroList.value
                isSearchStarting = false
            }
            heroList.value = results
            isSearching.value = true
        }
    }


    fun loadHeroPaginated(){
        viewModelScope.launch {
            isLoading.value = true
            val result =heroRepository.getHeroList(PAGE_SIZE, curPage * PAGE_SIZE)
            when(result){
                is Resource.Success -> {
                    endReached.value = curPage* PAGE_SIZE >= result.data!!.data.count
                    curPage++

                    val heroEntries = result.data.data.results.mapIndexed { index, entry ->
                        MarvelListEntry(entry.name.capitalize(Locale.ROOT), entry.thumbnail.path+".jpg", entry.id)
                    }
                    loadError.value = ""
                    isLoading.value = false
                    heroList.value += heroEntries
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }
}