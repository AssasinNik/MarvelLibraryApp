package com.example.marvel_app.presentation.comics_screen


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.local.favourites.FavouriteDao
import com.example.marvel_app.data.local.favourites.FavouritesEntity
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
import javax.inject.Inject

@HiltViewModel
class ComicsScreenViewModel @Inject constructor(
    private val heroRepository: HeroRepository,
    private val dao: FavouriteDao
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
                    val comicsEntry: ComicsItemEntry
                    if(result.description!=""){
                        comicsEntry = ComicsItemEntry(
                            result.title.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                            result.description,
                            result.images[0].path+"."+result.images[0].extension,
                            result.id,
                            result.urls[0].url,
                            creators,
                            heroList.value
                        )
                        Timber.tag("Problem").d("Here")
                        _comics.value = comicsEntry
                    }
                    else{
                        val notEmptyDescription = "There is no information about this comics in our database"
                        if(result.textObjects[0].text == "" || result.textObjects[0].text == null){
                            comicsEntry = ComicsItemEntry(
                                result.title.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                                notEmptyDescription,
                                result.thumbnail.path+"."+result.thumbnail.extension,
                                result.id,
                                result.urls[0].url,
                                creators,
                                heroList.value
                            )
                        }
                        else{
                            comicsEntry = ComicsItemEntry(
                                result.title.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                                result.textObjects[0].text,
                                result.thumbnail.path+"."+result.thumbnail.extension,
                                result.id,
                                result.urls[0].url,
                                creators,
                                heroList.value
                            )
                        }

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

    private fun loadCharacterInfo(characterId: Int?): Deferred<Unit> = viewModelScope.async {
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
                            result.urls[2].url,
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
        number: Int?,
        category : String ="comics"
    ){
        val notEmptyDescription = "There is no information about that in our database"
        if (description == "" || description == null){
            viewModelScope.launch {
                dao.upsertFavourite(
                    FavouritesEntity(
                        comicsName,
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
                        comicsName,
                        imageUrl,
                        description,
                        number,
                        category,
                        number)
                )
            }
        }
        _isFavorite.value = true
        Timber.tag("ADD").d("Add + ${comicsName}")
    }
    fun deleteFavorite(
        comicsName: String?
    ){
        viewModelScope.launch {
            if (comicsName != null) {
                dao.deleteFavourite(comicsName, "comics")
            }
        }
        _isFavorite.value = false
        Timber.tag("Delete").d("Delete + ${comicsName}")
    }
    fun checkFavourite(
        comicsName: String?
    ){
        viewModelScope.async {
            if (comicsName != null){
                _isFavorite.value = dao.existsFavourites(comicsName, "comics")
            }
        }
        Timber.tag("Check").d("Check + ${comicsName} + ${_isFavorite.value}")
    }
}