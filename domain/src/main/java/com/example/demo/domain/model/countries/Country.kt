package com.example.demo.domain.model.countries

data class Country(
    val name: String,
    val capital: String?,
    val latitude: Double,
    val longitude: Double,
    val altSpellings: List<String>,
    val population: Long,
    val flagUrl: String,
    val coatOfArmsUrl: String?
)