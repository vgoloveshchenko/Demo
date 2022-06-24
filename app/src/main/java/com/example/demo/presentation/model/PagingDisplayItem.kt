package com.example.demo.presentation.model

sealed class PagingDisplayItem<out T> {
    data class Content<T>(val data: T) : PagingDisplayItem<T>()

    object Error : PagingDisplayItem<Nothing>()

    object Loading : PagingDisplayItem<Nothing>()
}