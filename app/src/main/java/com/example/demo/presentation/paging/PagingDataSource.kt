package com.example.demo.presentation.paging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PagingDataSource<T>(
    loadingScope: CoroutineScope,
    loadSize: Int,
    private val loadData: suspend (PagingData) -> Result<List<T>>
) {

    private val fetchingFlow = MutableSharedFlow<LoadType>(
        extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val dataFlow = MutableStateFlow<PagingLceState<T>>(PagingLceState.FirstLoading)

    private val firstPage = PagingData(loadSize, 0)
    private var lastLoadedPage: PagingData? = null

    private var isLoading = false

    private var hasMoreData = true

    private val canLoadNextPage: Boolean get() = !isLoading && hasMoreData

    private val lastLoadedData: List<T>? get() = dataFlow.value.data

    init {
        loadingScope.launch {
            fetchingFlow.collectLatest { loadType ->

                // wait until user initiate retry action if previous state was error
                if (dataFlow.value.isErrorState() && loadType == LoadType.LOAD_MORE) {
                    return@collectLatest
                }

                isLoading = true

                setInitialState(loadType)

                val pageToLoad = getNextPageToLoad(loadType)

                loadData(pageToLoad)
                    .onSuccess { newData ->
                        hasMoreData = newData.size == pageToLoad.limit
                        lastLoadedPage = pageToLoad
                        val state = PagingLceState.Content(
                            lastLoadedData.orEmpty() + newData,
                            hasMoreData
                        )
                        dataFlow.tryEmit(state)
                    }
                    .onFailure { throwable ->
                        val state =
                            lastLoadedData?.let { PagingLceState.ContentWithError(it, throwable) }
                                ?: PagingLceState.FirstLoadingError(throwable)
                        dataFlow.tryEmit(state)
                    }

                isLoading = false
            }
        }

        refresh()
    }

    fun subscribePagingData(): Flow<PagingLceState<T>> {
        return dataFlow.asStateFlow()
    }

    fun loadMore() {
        if (canLoadNextPage) {
            fetchingFlow.tryEmit(LoadType.LOAD_MORE)
        }
    }

    fun refresh() {
        fetchingFlow.tryEmit(LoadType.REFRESH)
    }

    fun retry() {
        fetchingFlow.tryEmit(LoadType.RETRY)
    }

    private fun getNextPageToLoad(loadType: LoadType): PagingData {
        return when (loadType) {
            LoadType.REFRESH -> firstPage
            LoadType.LOAD_MORE,
            LoadType.RETRY -> lastLoadedPage?.inc() ?: firstPage
        }
    }

    private fun setInitialState(loadType: LoadType) {
        when (loadType) {
            LoadType.REFRESH -> {
                hasMoreData = true
                lastLoadedPage = null
                dataFlow.tryEmit(
                    PagingLceState.FirstLoading
                )
            }
            LoadType.RETRY -> {
                setInitialStateIfRetry()
            }
            LoadType.LOAD_MORE -> {
                /* do nothing */
            }
        }
    }

    private fun setInitialStateIfRetry() {
        when (val state = dataFlow.value) {
            is PagingLceState.ContentWithError -> {
                dataFlow.tryEmit(
                    PagingLceState.Content(state.data, hasMoreData = true)
                )
            }
            is PagingLceState.FirstLoadingError -> {
                dataFlow.tryEmit(
                    PagingLceState.FirstLoading
                )
            }
            else -> {
                /* do nothing */
            }
        }
    }

    private fun PagingLceState<T>.isErrorState(): Boolean {
        return this is PagingLceState.FirstLoadingError || this is PagingLceState.ContentWithError
    }

    private enum class LoadType {
        LOAD_MORE, REFRESH, RETRY
    }
}