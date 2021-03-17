package com.example.csvreader.domain.usecase

import com.example.csvreader.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface FileParserRepository {

    suspend fun parseFile(): Flow<List<UserModel>>
}
