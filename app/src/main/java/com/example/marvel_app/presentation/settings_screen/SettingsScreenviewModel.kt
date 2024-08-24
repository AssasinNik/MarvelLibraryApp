package com.example.marvel_app.presentation.settings_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.local.heroes.HeroesDao
import com.example.marvel_app.presentation.marvel_start_screen.GoogleAuthUiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val dao: HeroesDao,
    private val googleAuthUiClient: GoogleAuthUiClient
): ViewModel() {

    fun deleteAll(){
        dao.deleteHeroes()
    }

    fun signOut(){
        viewModelScope.launch {
            googleAuthUiClient.signOut()
        }
    }

}