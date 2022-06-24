package com.example.demo.domain.usecase

import com.example.demo.domain.model.countries.Country
import com.example.demo.domain.repository.CountryRepository

class GetCountriesUseCase(private val countryRepository: CountryRepository) {

    suspend operator fun invoke(): Result<List<Country>> {
        return countryRepository.getCountries()
    }
}