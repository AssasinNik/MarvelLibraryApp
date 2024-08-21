package com.example.marvel_app.presentation.film_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.local.favourites.FavouriteDao
import com.example.marvel_app.data.local.favourites.FavouritesEntity
import com.example.marvel_app.data.models.CharacterEntry
import com.example.marvel_app.data.models.ComicsEntry
import com.example.marvel_app.data.models.FilmEntry
import com.example.marvel_app.repository.CinemaRepository
import com.example.marvel_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class FilmScreenViewModel @Inject constructor(
    private val cinemaRepository: CinemaRepository,
    private val dao: FavouriteDao
):ViewModel() {

    private var _film = MutableStateFlow<FilmEntry?>(null)
    var film: StateFlow<FilmEntry?> = _film

    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var isFavorite = mutableStateOf(false)

    fun loadFilmInfo(filmName: String){
        viewModelScope.launch {
            isLoading.value = true

            val requestsCinema = async { cinemaRepository.getMoviesByTitle(filmName)}
            val movieResults = requestsCinema.await()
            val filmInfo = movieResults.data!!.data[0] // Get the first result

            when (movieResults) {
                is Resource.Success -> {
                    val filmEntry: FilmEntry
                    if(filmInfo.overview!=null && filmInfo.trailer_url!=null){
                        filmEntry= FilmEntry(
                            filmInfo.id,
                            filmInfo.title,
                            filmInfo.cover_url,
                            filmInfo.overview,
                            filmInfo.trailer_url,
                            filmInfo.duration,
                            filmInfo.directed_by,
                            "https://www.imdb.com/title/${filmInfo.imdb_id}/",
                            filmInfo.release_date
                        )
                    }
                    else{
                        filmEntry= FilmEntry(
                            filmInfo.id,
                            filmInfo.title,
                            filmInfo.cover_url,
                            "",
                            "",
                            filmInfo.duration,
                            filmInfo.directed_by,
                            "https://www.imdb.com/title/${filmInfo.imdb_id}/",
                            filmInfo.release_date
                        )
                    }

                    _film.value = filmEntry
                    isLoading.value=false
                }
                is Resource.Error -> {
                    loadError.value = movieResults.message.toString()
                    isLoading.value = false
                }
            }

        }



    }


    fun addToFavourites(
        name: String?,
        imageUrl: String?,
        description: String?,
        number: Int?,
        category : String ="films"
    ){
        val notEmptyDescription = "There is no information about comics in our database"
        if (description == "" || description == null){
            viewModelScope.launch {
                dao.upsertFavourite(
                    FavouritesEntity(
                        name,
                        imageUrl,
                        notEmptyDescription,
                        number,
                        category,
                        number)
                )
            }
        }
        else{
            viewModelScope.launch {
                dao.upsertFavourite(
                    FavouritesEntity(
                        name,
                        imageUrl,
                        description,
                        number,
                        category,
                        number)
                )
            }
        }
        isFavorite.value = true
        Timber.tag("ADD").d("Add + ${name}")
    }
    fun deleteFavorite(
        name: String?
    ){
        viewModelScope.launch {
            if (name != null) {
                dao.deleteFavourite(name, "films")
            }
        }
        isFavorite.value = false
        Timber.tag("Delete").d("Delete + ${name}")
    }
    fun checkFavourite(
        name: String?
    ){
        viewModelScope.async {
            if (name != null){
                isFavorite.value = dao.existsFavourites(name, "films")
            }
        }
        Timber.tag("Check").d("Check + ${name} + ${isFavorite.value}")
    }


}