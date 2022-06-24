package com.example.demo.data.model.countries

import com.google.gson.annotations.SerializedName

internal data class CountryDTO(
    val name: NameDTO,
    val capital: List<String>?,
    val altSpellings: List<String>,
    @SerializedName("latlng")
    val latLng: List<Double>,
    val population: Long,
    val flags: FlagsDTO,
    val coatOfArms: CoatOfArmsDTO,
    val capitalInfo: CapitalInfoDTO
)