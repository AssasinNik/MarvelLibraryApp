package com.example.marvel_app.presentation.marvel_start_screen

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.remote.responses.Google.SignInResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarvelStartViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient
) : ViewModel() {

    private val _isSignInSuccessful = MutableLiveData<Boolean>()
    val isSignInSuccessful: LiveData<Boolean> = _isSignInSuccessful

    private val _signInError = MutableLiveData<String?>()
    val signInError: LiveData<String?> = _signInError

    fun signIn(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        viewModelScope.launch {
            val signInIntentSender = googleAuthUiClient.signIn()
            launcher.launch(
                IntentSenderRequest.Builder(signInIntentSender ?: return@launch).build()
            )
        }
    }

    fun handleSignInResult(intent: Intent?) {
        viewModelScope.launch {
            val result = googleAuthUiClient.signInWithIntent(intent ?: return@launch)
            _isSignInSuccessful.value = result.data != null
            _signInError.value = result.errorMessage
        }
    }

    fun resetState() {
        _isSignInSuccessful.value = false
        _signInError.value = null
    }
}