package com.example.marvel_app.presentation.hero_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.local.heroes.Heroes
import com.example.marvel_app.data.local.heroes.HeroesDao
import com.example.marvel_app.data.models.HeroesListEntry
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Constants.PAGE_SIZE
import com.example.marvel_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@HiltViewModel
class HeroListScreenViewModel @Inject constructor(
    private val heroRepository: HeroRepository,
    private val dao: HeroesDao
): ViewModel() {
    private var curPage = 0

    var heroList = mutableStateOf<List<HeroesListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private lateinit var cachedCharacters: Flow<List<Heroes>>

    private var cachedHeroList = listOf<HeroesListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)


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

    fun getHeroList(){
        viewModelScope.launch {
            heroList.value = emptyList()
            var cachedHeroList2 = listOf<Heroes>()
            var flag: Boolean = true
            cachedCharacters = dao.selectHeroes()
            cachedCharacters.first { characters ->
                if (characters.isEmpty()) {
                    loadHeroPaginated(false)
                    flag = false
                    false
                } else {
                    cachedHeroList2 = characters
                    true
                }
            }
            if (flag){
                val heroEntries2 = mutableListOf<HeroesListEntry>()
                for (i in cachedHeroList2) {
                    isLoading.value=true
                    heroEntries2.add(
                        HeroesListEntry(
                            i.characterName,
                            i.imageUrl,
                            i.number
                        )
                    )
                }
                heroList.value+=heroEntries2
                isLoading.value=false
            }
        }
    }

    fun loadHeroPaginated(isRefresh: Boolean): Deferred<Unit> = viewModelScope.async(Dispatchers.IO){
        if(isRefresh){
            isLoading.value = false
        }
        else{
            isLoading.value = true
        }

            // Список запросов (используем список, чтобы потом получить результаты)
            val heroRequests = listOf(
                async { heroRepository.getHeroListLimit("sp", 50) },
                async { heroRepository.getHeroListLimit("hu", 50) },
                async { heroRepository.getHeroListLimit("wo", 50) },
                async { heroRepository.getHeroListLimit("ir", 50) },
                async { heroRepository.getHeroListLimit("do", 50) },
                async { heroRepository.getHeroListLimit("gh", 50) },
                async { heroRepository.getHeroListLimit("ca", 50) }
            )

            // Ожидаем завершения всех запросов
            val results = heroRequests.awaitAll()

            // Обрабатываем результаты
            val heroEntries = mutableListOf<HeroesListEntry>()
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
                                HeroesListEntry(entry.name.capitalize(Locale.ROOT).take(19)+"...",
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
            viewModelScope.launch(Dispatchers.IO) {
                dao.deleteHeroes()
                for (i in heroEntries){
                    dao.insertHero(
                        Heroes(
                            i.characterName,
                            i.imageUrl,
                            i.number,
                            i.number
                        )
                    )
                }
            }
            heroList.value = emptyList()
            // Обновляем список героев в UI
            heroList.value += heroEntries
            isLoading.value = false
        }
}