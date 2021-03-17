package com.example.csvreader.domain

import com.example.csvreader.domain.model.UserModel

data class UserResponse(
    var result: List<UserModel> = listOf(),
    var error: Throwable? = null,
    var loading: Boolean = false
)
