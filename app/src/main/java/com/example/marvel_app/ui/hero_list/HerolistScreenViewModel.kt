package com.example.marvel_app.ui.hero_list

import androidx.lifecycle.ViewModel
import com.example.marvel_app.repository.HeroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HerolistScreenViewModel @Inject constructor(
    private val heroRepository: HeroRepository
): ViewModel(

) {

}