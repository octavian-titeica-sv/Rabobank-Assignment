package com.example.csvreader.domain.repository

import com.example.csvreader.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface FileParserRepository {

    suspend fun parseFile(): Flow<List<UserModel>>
}
