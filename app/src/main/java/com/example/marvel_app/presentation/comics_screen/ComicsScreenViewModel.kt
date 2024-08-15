package com.example.marvel_app.presentation.comics_screen


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.local.favourites.Comics
import com.example.marvel_app.data.local.favourites.ComicsDao
import com.example.marvel_app.data.models.CharacterEntry
import com.example.marvel_app.data.models.ComicsItemEntry
import com.example.marvel_app.data.models.Creators
import com.example.marvel_app.repository.CinemaRepository
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.annotation.meta.When
import javax.inject.Inject

@HiltViewModel
class ComicsScreenViewModel @Inject constructor(
    private val heroRepository: HeroRepository,
    private val cinemaRepository: CinemaRepository,
    private val dao: ComicsDao
): ViewModel(){

    private var _comics = MutableStateFlow<ComicsItemEntry?>(null)
    var comics: StateFlow<ComicsItemEntry?> = _comics

    var heroList = mutableStateOf<List<CharacterEntry>>(listOf())


    private var _isLoading = MutableStateFlow(false)
    var isLoading: StateFlow<Boolean> = _isLoading

    private var _loadError = MutableStateFlow<String?>(null)
    var loadError: StateFlow<String?> = _loadError

    private var _isFavorite = MutableStateFlow(false)
    var isFavorite: StateFlow<Boolean> = _isFavorite

    fun loadComicsInfo(comicsId: Int?){
        viewModelScope.launch {
            _isLoading.value = true

            val comicsRequest = heroRepository.getComics(comicsId)
            when(comicsRequest){
                is Resource.Success -> {
                    val result = comicsRequest.data!!.data.results[0]
                    val creators: MutableList<Creators> = mutableListOf()
                    if(result.creators.available > 0){
                        result.creators.items.forEachIndexed { index, itemX ->
                            creators += Creators(
                                itemX.name,
                                itemX.role
                            )
                        }
                    }
                    if (result.characters.available > 0){
                        result.characters.items.forEachIndexed { index, item ->
                            val url = item.resourceURI
                            val lastSlashIndex = url.lastIndexOf('/')
                            val id = url.substring(lastSlashIndex + 1).toIntOrNull()
                            loadCharacterInfo(id).await()
                        }
                    }
                    if(result.description!=""){
                        val comicsEntry = ComicsItemEntry(
                            result.title.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                            result.description,
                            result.images[0].path+"."+result.images[0].extension,
                            result.id,
                            creators,
                            heroList.value
                        )
                        Timber.tag("Problem").d("Here")
                        _comics.value = comicsEntry
                    }
                    else{
                        val comicsEntry = ComicsItemEntry(
                            result.title.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                            result.textObjects[0].text,
                            result.thumbnail.path+"."+result.thumbnail.extension,
                            result.id,
                            creators,
                            heroList.value
                        )
                        _comics.value = comicsEntry
                    }
                    _isLoading.value = false
                    _loadError.value = null

                }
                is Resource.Error -> {
                    _loadError.value = comicsRequest.message
                    _isLoading.value = false
                }
            }
        }
    }

    fun loadCharacterInfo(characterId: Int?): Deferred<Unit> = viewModelScope.async {
            val characterRequest = async { heroRepository.getHeroInfo(characterId) }
            val characterInfo = characterRequest.await()

            val heroEntries = mutableListOf<CharacterEntry>()
            when(characterInfo){
                is Resource.Success -> {
                    heroEntries.addAll(characterInfo.data!!.data.results.mapIndexed { index, result ->
                        CharacterEntry(
                            result.name,
                            result.thumbnail.path+"."+result.thumbnail.extension,
                            result.description,
                            result.id
                        )
                    })
                }
                is Resource.Error -> {
                    _loadError.value = characterInfo.message
                    _isLoading.value = false
                }
            }
            heroList.value+=heroEntries
    }

    fun addToFavourites(
        comicsName: String?,
        imageUrl: String?,
        description: String?,
        number: Int?
    ){
        viewModelScope.launch {
            dao.upsertComics(Comics(comicsName, imageUrl, description, number, number))
            Timber.tag("Add").d("${comicsName}")
        }
        _isFavorite.value = true
    }
    fun deleteFavorite(
        comicsName: String?
    ){
        viewModelScope.launch {
            if (comicsName != null) {
                dao.deleteComics(comicsName)
                Timber.tag("Delete").d("${comicsName}")
            }
        }
        _isFavorite.value = false
    }
    fun checkFavourite(
        comicsName: String?
    ){
        viewModelScope.async {
            if (comicsName != null){
                _isFavorite.value = dao.existsComics(comicsName)
                Timber.tag("Check").d("${comicsName}")
            }
        }
    }
}