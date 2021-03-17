package com.example.csvreader.ui.dagger

import com.example.csvreader.data.FileParserRepositoryImpl
import com.example.csvreader.domain.usecase.FileParserRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    abstract fun bindFileParserRepository(fileParserRepositoryImpl: FileParserRepositoryImpl): FileParserRepository
}
