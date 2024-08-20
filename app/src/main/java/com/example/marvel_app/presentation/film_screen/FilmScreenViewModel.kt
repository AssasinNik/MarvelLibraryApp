package com.example.marvel_app.presentation.film_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.marvel_app.data.local.favourites.FavouriteDao
import com.example.marvel_app.data.models.CharacterEntry
import com.example.marvel_app.data.models.FilmEntry
import com.example.marvel_app.repository.CinemaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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




}