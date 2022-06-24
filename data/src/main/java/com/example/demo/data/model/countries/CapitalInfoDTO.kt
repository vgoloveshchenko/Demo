package com.example.demo.data.model.countries

import com.google.gson.annotations.SerializedName

internal data class CapitalInfoDTO(
    @SerializedName("latlng")
    val latLng: List<Double>?
)