package com.example.demo.data

import com.example.demo.data.api.GithubApi
import com.example.demo.data.model.github.UserDTO
import com.example.demo.data.repository.UserRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UserRepositoryTest {

    private val api = mockk<GithubApi> {
        coEvery { getUsers(any(), any()) } coAnswers {
            val since = arg<Int>(0)
            if (since == 0) {
                STUB_USERS
            } else {
                error("Some error")
            }
        }
    }

    private val repository = UserRepositoryImpl(api)

    @Test
    fun `test success loading`() = runTest {
        val result = repository.getUsers(0, 50)
        Assert.assertTrue(result.isSuccess)
        result.getOrThrow()
            .zip(STUB_USERS)
            .forEach { (repositoryUser, stubUser) ->
                Assert.assertEquals(stubUser.id, repositoryUser.id)
                Assert.assertEquals(stubUser.login, repositoryUser.login)
                Assert.assertEquals(stubUser.avatarUrl, repositoryUser.avatarUrl)
            }
    }

    @Test
    fun `test failed loading`() = runTest {
        val result = repository.getUsers(1, 50)
        Assert.assertTrue(result.isFailure)
    }

    companion object {
        private val STUB_USERS = List(10) {
            UserDTO(it.toLong(), "User $it", "avatarUrl $it")
        }
    }
}