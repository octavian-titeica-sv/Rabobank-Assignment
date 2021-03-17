package com.example.csvreader.presentation.viewModel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csvreader.domain.usecase.GetUsersUseCase
import com.example.csvreader.presentation.UserState
import com.example.csvreader.presentation.mapper.mapToUIUserState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(val getUsersUseCase: GetUsersUseCase) : ViewModel() {

    val usersState = MutableLiveData<UserState>()

    init {
        getUsers()
    }

    @VisibleForTesting
    fun getUsers() {
        viewModelScope.launch {
            getUsersUseCase().collect { result ->
                usersState.value = result.mapToUIUserState()
            }
        }
    }
}
