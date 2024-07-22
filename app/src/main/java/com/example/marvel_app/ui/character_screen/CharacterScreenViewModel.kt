package com.example.marvel_app.ui.character_screen

import androidx.lifecycle.ViewModel
import com.example.marvel_app.repository.HeroRepository
import javax.inject.Inject

class CharacterScreenViewModel @Inject constructor(
    private val heroRepository: HeroRepository
): ViewModel() {

}