package com.example.demo

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.demo.domain.model.users.UserDetails
import com.example.demo.domain.model.users.UserId
import com.example.demo.presentation.model.LceState
import com.example.demo.presentation.ui.users.details.UserDetailsFragment
import com.example.demo.presentation.ui.users.details.UserDetailsFragmentArgs
import com.example.demo.presentation.ui.users.details.UserDetailsViewModel
import com.example.demo.screens.UserDetailsScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.kakao.screen.Screen
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.ClosingKoinTest
import org.koin.test.mock.declare
import kotlin.random.Random

class UserDetailsFragmentTest : TestCase(), ClosingKoinTest {

    @Test
    fun startScreenLoading() = run {
        initKoin(
            stateAfterLoading = LceState.Content(
                UserDetails(
                    id = UserId(Random.nextLong()),
                    login = LOGIN,
                    avatarUrl = "",
                    followers = 20,
                    following = 50
                )
            )
        )
        launchFragment()

        UserDetailsScreen {
            progress {
                isDisplayed()
            }
            Screen.idle(1000)
            progress {
                isNotDisplayed()
            }
            toolbar {
                hasTitle("user login")
            }
            login {
                hasText("user login")
            }
        }
    }

    @Test
    fun failedResponse() = run {
        initKoin(
            stateAfterLoading = LceState.Error(Exception(ERROR_MESSAGE))
        )
        launchFragment()

        UserDetailsScreen {
            progress {
                isDisplayed()
            }
            Screen.idle(1000)
            progress {
                isNotDisplayed()
            }
            snackbar {
                isDisplayed()
                text {
                    hasText(ERROR_MESSAGE)
                }
            }
        }
    }

    // https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
    private fun launchFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        launchFragmentInContainer(
            themeResId = R.style.Theme_Demo,
            fragmentArgs = UserDetailsFragmentArgs.Builder(LOGIN).build().toBundle()
        ) {
            UserDetailsFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        navController.setGraph(R.navigation.navigation_users)
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }
    }

    private fun initKoin(stateAfterLoading: LceState<UserDetails>) {
        startKoin {  }
        declare {
            mockk<UserDetailsViewModel>(relaxUnitFun = true) {
                every { userDetailsFlow } returns flow {
                    emit(LceState.Loading)
                    delay(1000)
                    emit(stateAfterLoading)
                }
            }
        }
    }

    companion object {
        private const val LOGIN = "user login"
        private const val ERROR_MESSAGE = "error message"
    }
}