package com.example.csvreader.domain.usecase

import com.example.csvreader.domain.UserResponse
import com.example.csvreader.domain.repository.FileParserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val fileParserRepository: FileParserRepository) {

    suspend operator fun invoke(): Flow<UserResponse> = flow {
        fileParserRepository.parseFile()
            .flowOn(Dispatchers.Default)
            .onStart {
                emit(UserResponse(loading = true))
            }
            .catch { error ->
                error.printStackTrace()
                emit(UserResponse(loading = false, error = error))
            }
            .collect { response ->
                emit(UserResponse(loading = false, result = response))
            }
    }
}
