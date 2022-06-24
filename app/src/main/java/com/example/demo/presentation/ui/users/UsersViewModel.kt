package com.example.demo.presentation.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.domain.usecase.GetUsersUseCase
import com.example.demo.presentation.paging.PagingDataSource

class UsersViewModel(private val getUsersUseCase: GetUsersUseCase) : ViewModel() {

    private val pagingDataSource = PagingDataSource(viewModelScope, PAGE_SIZE) {
        getUsersUseCase(it.offset, it.limit)
    }

    val usersPagingData = pagingDataSource.subscribePagingData()

    fun onLoadMore() {
        pagingDataSource.loadMore()
    }

    fun onRefresh() {
        pagingDataSource.refresh()
    }

    fun onRetry() {
        pagingDataSource.retry()
    }

    companion object {
        private const val PAGE_SIZE = 50
    }
}