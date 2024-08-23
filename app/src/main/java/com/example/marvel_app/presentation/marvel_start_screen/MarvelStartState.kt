package com.example.marvel_app.presentation.marvel_start_screen

data class MarvelStartState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)