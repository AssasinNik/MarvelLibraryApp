package com.example.marvel_app.presentation.tvShows_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.local.favourites.FavouriteDao
import com.example.marvel_app.data.local.favourites.FavouritesEntity
import com.example.marvel_app.data.models.FilmEntry
import com.example.marvel_app.data.models.TvShowEntry
import com.example.marvel_app.data.remote.responses.TvShows.TvShows
import com.example.marvel_app.repository.CinemaRepository
import com.example.marvel_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class TvShowsScreenViewModel @Inject constructor(
    private val cinemaRepository: CinemaRepository,
    private val dao: FavouriteDao
):ViewModel() {

    private var _tvShow = MutableStateFlow<TvShowEntry?>(null)
    var tvShow: StateFlow<TvShowEntry?> = _tvShow

    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var isFavorite = mutableStateOf(false)

    fun loadtvShowInfo(id: Int){
        viewModelScope.launch {
            isLoading.value = true

            val requestsTvShow = async { cinemaRepository.getTvShowsById(id)}
            val tvShowResults = requestsTvShow.await()
            val tvShowInfo = tvShowResults.data

            when (tvShowResults) {
                is Resource.Success -> {
                    val tvShowEntry: TvShowEntry
                    if (tvShowInfo != null) {
                        if(tvShowInfo.overview!=null && tvShowInfo.trailer_url!=null && tvShowInfo.directed_by!=null){
                            tvShowEntry= TvShowEntry(
                                tvShowInfo.id,
                                tvShowInfo.title,
                                tvShowInfo.season,
                                tvShowInfo.release_date,
                                tvShowInfo.directed_by,
                                tvShowInfo.overview,
                                "https://www.imdb.com/title/${tvShowInfo.imdb_id}/",
                                tvShowInfo.number_episodes,
                                tvShowInfo.cover_url,
                                tvShowInfo.trailer_url
                            )
                        } else{
                            tvShowEntry= TvShowEntry(
                                tvShowInfo.id,
                                tvShowInfo.title,
                                tvShowInfo.season,
                                tvShowInfo.release_date,
                                "",
                                "",
                                "https://www.imdb.com/title/${tvShowInfo.imdb_id}/",
                                tvShowInfo.number_episodes,
                                tvShowInfo.cover_url,
                                ""
                            )
                        }
                    }
                    else{
                        tvShowEntry= TvShowEntry(
                            0,
                           "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            ""
                        )
                    }

                    _tvShow.value = tvShowEntry
                    isLoading.value=false
                }
                is Resource.Error -> {
                    loadError.value = tvShowResults.message.toString()
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
        category : String ="tvShow"
    ){
        val notEmptyDescription = "There is no information about that in our database"
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
                dao.deleteFavourite(name, "tvShow")
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
                isFavorite.value = dao.existsFavourites(name, "tvShow")
            }
        }
        Timber.tag("Check").d("Check + ${name} + ${isFavorite.value}")
    }


}