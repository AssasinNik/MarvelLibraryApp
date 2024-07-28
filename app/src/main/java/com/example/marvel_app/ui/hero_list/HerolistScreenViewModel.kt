package com.example.marvel_app.ui.hero_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.models.HeroesListEntry
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Constants.PAGE_SIZE
import com.example.marvel_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HeroListScreenViewModel @Inject constructor(
    private val heroRepository: HeroRepository
): ViewModel() {
    private var curPage = 0

    var heroList = mutableStateOf<List<HeroesListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedHeroList = listOf<HeroesListEntry>()
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


    fun loadHeroPaginated() {
        viewModelScope.launch {
            isLoading.value = true

            // Список запросов (используем список, чтобы потом получить результаты)
            val heroRequests = listOf(
                async { heroRepository.getHeroList("spider-man") },
                async { heroRepository.getHeroList("hulk") },
                async { heroRepository.getHeroList("wolver") },
                async { heroRepository.getHeroList("iron") },
                async { heroRepository.getHeroList("doctor") },
                async { heroRepository.getHeroList("ghost") },
                async { heroRepository.getHeroList("captain") }
            )

            // Ожидаем завершения всех запросов
            val results = heroRequests.awaitAll()

            // Обрабатываем результаты
            var heroEntries = mutableListOf<HeroesListEntry>()
            for (result in results) {
                when (result) {
                    is Resource.Success -> {
                        // Обновляем данные о количестве страниц
                        endReached.value = curPage* PAGE_SIZE >= result.data!!.data.count
                        curPage++

                        // Добавляем результаты в список
                        heroEntries.addAll(result.data.data.results.mapIndexed { _, entry ->
                            if(entry.name.capitalize(Locale.ROOT).length < 22){
                                HeroesListEntry(entry.name.capitalize(Locale.ROOT),
                                    entry.thumbnail.path+"."+entry.thumbnail.extension,
                                    entry.id)
                            }
                            else{
                                HeroesListEntry(entry.name.capitalize(Locale.ROOT).take(22)+"...",
                                    entry.thumbnail.path+"."+entry.thumbnail.extension,
                                    entry.id)
                            }
                        })
                    }
                    is Resource.Error -> {
                        loadError.value = result.message!!
                    }
                }
            }
            // Обновляем список героев в UI
            heroList.value += heroEntries
            isLoading.value = false
        }
    }

}