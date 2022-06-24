package com.example.demo.data.api

import com.example.demo.data.model.countries.CountryDTO
import retrofit2.http.GET

internal interface CountriesApi {

    @GET("all")
    suspend fun getCountries(): List<CountryDTO>
}