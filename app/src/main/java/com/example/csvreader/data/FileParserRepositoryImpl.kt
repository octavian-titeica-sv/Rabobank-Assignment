package com.example.csvreader.data

import android.content.Context
import com.example.csvreader.domain.usecase.FileParserRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FileParserRepositoryImpl @Inject constructor(val context: Context) : FileParserRepository {

    override suspend fun parseFile() = flow {
        val csvFileReader = CSVFileReader(context)
        emit(csvFileReader.readUsers())
    }
}
