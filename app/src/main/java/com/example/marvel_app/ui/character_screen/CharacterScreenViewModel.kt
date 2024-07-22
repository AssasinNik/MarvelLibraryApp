package com.example.marvel_app.ui.character_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.models.CharacterEntry
import com.example.marvel_app.data.models.ComicsEntry
import com.example.marvel_app.data.models.HeroesListEntry
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CharacterScreenViewModel @Inject constructor(
    private val heroRepository: HeroRepository
): ViewModel() {

    var comicsList = mutableStateOf<List<ComicsEntry>>(listOf())

    private var _character = MutableStateFlow<CharacterEntry?>(null) // Changed to MutableStateFlow<CharacterEntry?>
    var character: StateFlow<CharacterEntry?> = _character

    private var _isLoading = MutableStateFlow(false)
    var isLoading: StateFlow<Boolean> = _isLoading

    private var _loadError = MutableStateFlow<String?>(null)
    var loadError: StateFlow<String?> = _loadError

    fun loadHeroInfo(heroId: Int?) {
        viewModelScope.launch {
            _isLoading.value = true

            val characterInfo = heroRepository.getHeroInfo(heroId)
            val comicsInfo = heroRepository.getHeroComics(heroId)

            when (characterInfo) {
                is Resource.Success -> {
                    when (comicsInfo) {
                        is Resource.Success -> {
                            val heroInfo = characterInfo.data!!.data.results[0] // Get the first result
                            val characterEntry = CharacterEntry(
                                heroInfo.name.capitalize(Locale.ROOT),
                                heroInfo.thumbnail.path + "." + heroInfo.thumbnail.extension,
                                heroInfo.description
                            )
                            val heroComicsInfo = comicsInfo.data!!.data.results.mapIndexed { index, entry ->
                                ComicsEntry(
                                    entry.title,
                                    entry.description,
                                    entry.thumbnail.path + "." + entry.thumbnail.extension
                                )
                            }

                            _loadError.value = null
                            _isLoading.value = false
                            comicsList.value = heroComicsInfo
                            _character.value = characterEntry
                        }
                        is Resource.Error -> {
                            _loadError.value = comicsInfo.message
                            _isLoading.value = false
                        }
                    }
                }
                is Resource.Error -> {
                    _loadError.value = characterInfo.message
                    _isLoading.value = false
                }
            }
        }
    }
}