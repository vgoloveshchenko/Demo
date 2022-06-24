package com.example.demo.presentation.paging

sealed class PagingLceState<out T> {

    open val data: List<T>? get() = null

    object FirstLoading : PagingLceState<Nothing>()

    data class FirstLoadingError(
        val throwable: Throwable
    ) : PagingLceState<Nothing>()

    data class Content<T>(
        override val data: List<T>,
        val hasMoreData: Boolean
    ) : PagingLceState<T>()

    data class ContentWithError<T>(
        override val data: List<T>,
        val throwable: Throwable
    ) : PagingLceState<T>()
}