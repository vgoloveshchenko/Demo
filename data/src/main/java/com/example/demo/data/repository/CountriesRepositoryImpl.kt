package com.example.demo.data.repository

import com.example.demo.data.api.CountriesApi
import com.example.demo.data.mapper.toDomainModel
import com.example.demo.domain.repository.CountryRepository

internal class CountriesRepositoryImpl(
    private val countriesApi: CountriesApi
) : CountryRepository {

    override suspend fun getCountries() = runCatching {
        countriesApi.getCountries()
    }.map { countries ->
        countries.map { it.toDomainModel() }
    }
}