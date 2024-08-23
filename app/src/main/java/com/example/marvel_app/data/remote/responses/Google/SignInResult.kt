package com.example.marvel_app.data.remote.responses.Google


data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val userName: String?,
    val userImage: String?
)