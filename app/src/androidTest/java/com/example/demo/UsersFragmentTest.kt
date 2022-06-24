package com.example.demo

import androidx.fragment.app.testing.launchFragmentInContainer
import com.example.demo.domain.model.users.User
import com.example.demo.domain.model.users.UserId
import com.example.demo.domain.usecase.GetUsersUseCase
import com.example.demo.presentation.ui.users.UsersFragment
import com.example.demo.screens.UsersScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.kakao.screen.Screen
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.ClosingKoinTest
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

class UsersFragmentTest : TestCase(), ClosingKoinTest {

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz -> mockkClass(clazz) }

    @Before
    fun setUp() {
        declareMock<GetUsersUseCase> {
            coEvery { this@declareMock.invoke(any(), any()) } coAnswers {
                delay(1000)

                val since = arg<Int>(0)
                val perPage = arg<Int>(1)

                Result.success(
                    List(perPage) {
                        val id = since + it
                        User(
                            id = UserId(id.toLong()),
                            login = "User $id",
                            avatarUrl = ""
                        )
                    }
                )
            }
        }
    }

    @Test
    fun startScreenLoading() = run {
        launchFragmentInContainer<UsersFragment>(
            themeResId = R.style.Theme_Demo
        )
        UsersScreen {
            progress {
                isDisplayed()
            }
            Screen.idle(1000)
            progress {
                isNotDisplayed()
            }
            recyclerView {
                lastChild<UsersScreen.LoadingItem> {
                    isDisplayed()
                }
            }
        }
    }
}