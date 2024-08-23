package com.example.marvel_app.presentation.marvel_start_screen

import androidx.lifecycle.ViewModel
import com.example.marvel_app.data.remote.responses.Google.SignInResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MarvelStartViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(MarvelStartState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { MarvelStartState() } // Usage of default state
    }
}