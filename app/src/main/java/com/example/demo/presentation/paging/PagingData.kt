package com.example.demo.presentation.paging

data class PagingData(
    val limit: Int,
    val offset: Int = 0
) {

    init {
        require(offset >= 0) { "Invalid offset: $offset" }
        require(limit > 0) { "Invalid limit: $limit" }
    }

    operator fun inc(): PagingData = copy(offset = offset + limit)
}