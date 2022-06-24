package com.example.demo.domain.repository

import com.example.demo.domain.model.countries.Country

interface CountryRepository {

    suspend fun getCountries(): Result<List<Country>>
}