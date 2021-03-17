package com.example.csvreader.presentation.mapper

import com.example.csvreader.domain.UserResponse
import com.example.csvreader.presentation.UserState

fun UserResponse.mapToUIUserState() = UserState(
    result = this.result,
    error = this.error,
    loading = this.loading
)
