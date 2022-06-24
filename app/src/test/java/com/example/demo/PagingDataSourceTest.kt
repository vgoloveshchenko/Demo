package com.example.demo

import com.example.demo.presentation.paging.PagingData
import com.example.demo.presentation.paging.PagingDataSource
import com.example.demo.presentation.paging.PagingLceState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PagingDataSourceTest {

    @Test
    fun `test initial value`() = runTest {
        val pagingDataSource = PagingDataSource(
            loadingScope = TestScope(UnconfinedTestDispatcher(testScheduler)),
            loadSize = 20,
            loadData = loadData
        )
        val lceState = pagingDataSource.subscribePagingData().first()
        Assert.assertEquals(PagingLceState.FirstLoading, lceState)
    }

    @Test
    fun `test content value`() = runTest {
        val pagingDataSource = PagingDataSource(
            loadingScope = TestScope(UnconfinedTestDispatcher(testScheduler)),
            loadSize = 20,
            loadData = loadData
        )
        val lceState = pagingDataSource.subscribePagingData().drop(1).first()
        Assert.assertTrue(lceState is PagingLceState.Content)
        Assert.assertEquals(List(20) { it }, lceState.data)
    }

    @Test
    fun `test load next page`() = runTest {
        val pagingDataSource = PagingDataSource(
            loadingScope = TestScope(UnconfinedTestDispatcher(testScheduler)),
            loadSize = 20,
            loadData = loadData
        )
        advanceUntilIdle()
        pagingDataSource.loadMore()
        advanceUntilIdle()
        val lceState = pagingDataSource.subscribePagingData().first()
        Assert.assertTrue(lceState is PagingLceState.Content)
        Assert.assertEquals(List(40) { it }, lceState.data)
    }

    @Test
    fun `test first loading error`() = runTest {
        val pagingDataSource = PagingDataSource<Int>(
            loadingScope = TestScope(UnconfinedTestDispatcher(testScheduler)),
            loadSize = 20,
            loadData = {
                delay(1) // emulate work
                Result.failure(Exception("test error"))
            }
        )
        val lceState = pagingDataSource.subscribePagingData().drop(1).first()
        Assert.assertTrue(lceState is PagingLceState.FirstLoadingError)
    }

    companion object {
        private val loadData: suspend (PagingData) -> Result<List<Int>> = { (limit, offset) ->
            delay(1) // emulate work
            Result.success(
                List(limit) {
                    it + offset
                }
            )
        }
    }
}