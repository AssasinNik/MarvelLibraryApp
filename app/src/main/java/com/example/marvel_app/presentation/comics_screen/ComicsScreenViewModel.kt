package com.example.marvel_app.presentation.comics_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.models.ComicsEntry
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ComicsScreenViewModel @Inject constructor(
    private val heroRepository: HeroRepository
): ViewModel(){

    private var _comics = MutableStateFlow<ComicsEntry?>(null)
    var comics: StateFlow<ComicsEntry?> = _comics

    private var _isLoading = MutableStateFlow(false)
    var isLoading: StateFlow<Boolean> = _isLoading

    private var _loadError = MutableStateFlow<String?>(null)
    var loadError: StateFlow<String?> = _loadError

    fun loadComicsInfo(comicsId: Int?){
        viewModelScope.launch {
            _isLoading.value = true

            val comicsRequest = async { heroRepository.getComics(comicsId) }
            when(val comicsInfo = comicsRequest.await()){
                is Resource.Success -> {
                    val result = comicsInfo.data!!.data.results[0]
                    val comicsEntry = ComicsEntry(
                        result.title.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                        result.description,
                        result.images[0].path+"."+result.images[0].extension,
                        result.id
                    )
                    _isLoading.value = false
                    _loadError.value = null
                    _comics.value = comicsEntry
                }
                is Resource.Error -> {
                    _loadError.value = comicsInfo.message
                    _isLoading.value = false
                }
            }
        }
    }

}