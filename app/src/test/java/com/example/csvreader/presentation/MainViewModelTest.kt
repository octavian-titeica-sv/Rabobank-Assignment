package com.example.csvreader.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.csvreader.domain.UserResponse
import com.example.csvreader.domain.model.UserModel
import com.example.csvreader.domain.usecase.GetUsersUseCase
import com.example.csvreader.presentation.mapper.mapToUIUserState
import com.example.csvreader.presentation.viewModel.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instantRuleExecutor = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    private lateinit var getUsersUseCase: GetUsersUseCase

    @MockK(relaxed = true)
    private lateinit var observer: Observer<UserState>

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        // given
        coEvery { getUsersUseCase() } returns flowOf()

        mainViewModel = MainViewModel(getUsersUseCase)
        mainViewModel.usersState.observeForever(observer)
    }

    @After
    fun afterEach() {
        mainViewModel.usersState.removeObserver(observer)
    }

    @Test
    fun `given main screen is opened, when getUsers returns a loading response, the loading state will be emitted`() {
        runBlockingTest {
            // given
            val expectedState = UserResponse(loading = true)
            coEvery { getUsersUseCase() } returns flowOf(expectedState)

            // when
            mainViewModel.getUsers()

            // then
            verify {
                observer.onChanged(expectedState.mapToUIUserState())
            }
        }
    }

    @Test
    fun `given main screen is opened, when getUsers returns a list of users, the corresponding state will be emitted`() {
        runBlockingTest {
            // given
            val usersList = mockk<List<UserModel>>()
            val expectedState = UserResponse(result = usersList)
            coEvery { getUsersUseCase() } returns flowOf(expectedState)

            // when
            mainViewModel.getUsers()

            // then
            verify {
                observer.onChanged(expectedState.mapToUIUserState())
            }
        }
    }

    @Test
    fun `given main screen is opened, when getUsers throws an exception, the error state will be emitted`() {
        runBlockingTest {
            // given
            val exception = Exception()
            val expectedState = UserResponse(error = exception)
            coEvery { getUsersUseCase() } returns flowOf(expectedState)

            // when
            mainViewModel.getUsers()

            // then
            verify {
                observer.onChanged(expectedState.mapToUIUserState())
            }
        }
    }
}
