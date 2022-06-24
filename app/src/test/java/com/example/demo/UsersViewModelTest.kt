package com.example.demo

import com.example.demo.domain.model.users.User
import com.example.demo.domain.model.users.UserId
import com.example.demo.domain.usecase.GetUsersUseCase
import com.example.demo.presentation.paging.PagingLceState
import com.example.demo.presentation.ui.users.UsersViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UsersViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `first emit loading`() = runTest {
        val mockGetUsersUseCase = mockGetUsersUseCase()
        val viewModel = UsersViewModel(mockGetUsersUseCase)
        val lceState = viewModel.usersPagingData.first()
        Assert.assertEquals(PagingLceState.FirstLoading, lceState)
    }

    @Test
    fun `load initial page`() = runTest {
        val mockGetUsersUseCase = mockGetUsersUseCase()
        val viewModel = UsersViewModel(mockGetUsersUseCase)
        val lceState = viewModel.usersPagingData.drop(1).first()
        Assert.assertTrue(lceState is PagingLceState.Content)
        Assert.assertEquals(List(50) { it.toLong() }, lceState.data?.map { it.id })
    }

    @Test
    fun `test first loading error`() = runTest {
        val mockGetUsersUseCase = mockGetUsersUseCase(sendResult = false)
        val viewModel = UsersViewModel(mockGetUsersUseCase)
        val lceState = viewModel.usersPagingData.drop(1).first()
        Assert.assertTrue(lceState is PagingLceState.FirstLoadingError)
    }

    @Test
    fun `test load next page`() = runTest {
        val mockGetUsersUseCase = mockGetUsersUseCase()
        val viewModel = UsersViewModel(mockGetUsersUseCase)
        advanceUntilIdle()
        viewModel.onLoadMore()
        advanceUntilIdle()
        val lceState = viewModel.usersPagingData.first()
        Assert.assertTrue(lceState is PagingLceState.Content)
        Assert.assertEquals(List(100) { it.toLong() }, lceState.data?.map { it.id })
    }

    private fun mockGetUsersUseCase(sendResult: Boolean = true) = mockk<GetUsersUseCase> {
        coEvery { this@mockk.invoke(any(), any()) } coAnswers {
            delay(1) // emulate work

            if (sendResult) {
                val since = arg<Int>(0)
                val perPage = arg<Int>(1)
                val users = List(perPage) {
                    val id = since + it.toLong()
                    User(
                        id = UserId(id),
                        login = "user $id",
                        avatarUrl = ""
                    )
                }
                Result.success(users)
            } else {
                Result.failure(Exception("test error"))
            }
        }
    }
}