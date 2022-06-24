package com.example.demo.data.model.github

import com.google.gson.annotations.SerializedName

internal data class SearchedUsersDTO(
    @SerializedName("total_count")
    val totalCount: Int,
    val items: List<UserDTO>
)