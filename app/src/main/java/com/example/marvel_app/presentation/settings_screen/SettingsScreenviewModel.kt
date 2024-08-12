package com.example.marvel_app.presentation.settings_screen

import androidx.lifecycle.ViewModel
import com.example.marvel_app.data.local.heroes.HeroesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val dao: HeroesDao
): ViewModel() {

    fun deleteAll(){
        dao.deleteHeroes()
    }

}