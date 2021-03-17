package com.example.csvreader.presentation

import com.example.csvreader.domain.model.UserModel

data class UserState(
    var result: List<UserModel> = listOf(),
    var error: Throwable? = null,
    var loading: Boolean = false
)
