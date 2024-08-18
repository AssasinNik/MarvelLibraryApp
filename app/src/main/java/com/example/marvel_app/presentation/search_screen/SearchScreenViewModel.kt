package com.example.marvel_app.presentation.search_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.models.SearchResultEntry
import com.example.marvel_app.repository.CinemaRepository
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val heroRepository: HeroRepository,
    private val cinemaRepository: CinemaRepository
) : ViewModel() {

    var resultList = mutableStateOf<List<SearchResultEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    private var searchJob: Job? = null

    fun loadInfo(name: String) {
        searchJob?.cancel()

        if(name != ""){
            searchJob = viewModelScope.launch {
                isLoading.value = true
                val resultEntries = mutableListOf<SearchResultEntry>()

                // Выполнение асинхронных запросов
                val requestsCinema = async { cinemaRepository.getMovies(10, name) }
                val requestsTvShows = async { cinemaRepository.getTvShows(10, name) }
                val requestComics = async { heroRepository.getComicsList(name, 3) }
                val requestHeroes = async { heroRepository.getHeroListLimit(name, 3) }

                // Ожидание и обработка результатов запросов
                val movieResults = requestsCinema.await()
                val tvShowResults = requestsTvShows.await()
                val comicsResults = requestComics.await()
                val heroResults = requestHeroes.await()

                when (heroResults) {
                    is Resource.Success -> {
                        resultEntries.addAll(heroResults.data?.data?.results?.map { entry ->
                            SearchResultEntry(
                                entry.name,
                                entry.description,
                                entry.thumbnail.path + "." + entry.thumbnail.extension,
                                "hero",
                                entry.id
                            )
                        } ?: emptyList())
                    }
                    is Resource.Error -> {
                        loadError.value = heroResults.message ?: "Unknown error"
                    }
                }

                when (comicsResults) {
                    is Resource.Success -> {
                        resultEntries.addAll(comicsResults.data?.data?.results?.map { entry ->
                            SearchResultEntry(
                                entry.title,
                                entry.description,
                                entry.thumbnail.path + "." + entry.thumbnail.extension,
                                "comics",
                                entry.id
                            )
                        } ?: emptyList())
                    }
                    is Resource.Error -> {
                        loadError.value = comicsResults.message ?: "Unknown error"
                    }
                }

                when (movieResults) {
                    is Resource.Success -> {
                        resultEntries.addAll(movieResults.data?.data?.map { data ->
                            SearchResultEntry(
                                data.title,
                                data.overview,
                                data.cover_url,
                                "film",
                                data.id
                            )
                        } ?: emptyList())
                    }
                    is Resource.Error -> {
                        loadError.value = movieResults.message ?: "Unknown error"
                    }
                }

                when (tvShowResults) {
                    is Resource.Success -> {
                        resultEntries.addAll(tvShowResults.data?.data?.map { data ->
                            SearchResultEntry(
                                data.title,
                                data.overview,
                                data.cover_url,
                                "tvShow",
                                data.id
                            )
                        } ?: emptyList())
                    }
                    is Resource.Error -> {
                        loadError.value = tvShowResults.message ?: "Unknown error"
                    }
                }

                resultList.value = resultEntries
                isLoading.value = false
            }
        }
    }
}
