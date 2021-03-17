package com.example.csvreader.usecase

import com.example.csvreader.domain.UserResponse
import com.example.csvreader.domain.model.UserModel
import com.example.csvreader.domain.repository.FileParserRepository
import com.example.csvreader.domain.usecase.GetUsersUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetUsersUseCaseTest {

    @MockK(relaxed = true)
    private lateinit var fileParserRepository: FileParserRepository

    private lateinit var getUsersUseCase: GetUsersUseCase

    private val userModel = UserModel("Theo", "Jansen", 5, "1978-01-02T00:00:00")

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getUsersUseCase = GetUsersUseCase(fileParserRepository)
    }

    @Test
    fun `given a valid input stream, when the use case is executed successfully, then a list of users is emitted`() {
        runBlockingTest {
            // given
            val initialUserState = UserResponse()
            coEvery { fileParserRepository.parseFile() } returns flowOf(listOf(userModel))

            // when
            val result = getUsersUseCase().toList()

            // then
            assert(
                result == listOf(
                    initialUserState.copy(loading = true),
                    initialUserState.copy(result = listOf(userModel), loading = false)
                )
            )
        }
    }

    @Test
    fun `given a valid in put stream, when the use case fails with an exception, then an error state is emitted`() {
        runBlockingTest {
            // given
            val initialUserState = UserResponse()
            val exception = Exception("test exception message")
            coEvery { fileParserRepository.parseFile() } returns flow { throw exception }

            // when
            val result = getUsersUseCase().toList()
            val errorState = result[1]

            // then
            assert(result[0] == initialUserState.copy(loading = true))
            assert(!errorState.loading)
            assert(errorState.error?.message == exception.message)
        }
    }
}
