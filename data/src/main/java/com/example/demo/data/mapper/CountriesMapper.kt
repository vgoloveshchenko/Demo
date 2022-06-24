package com.example.demo.data.mapper

import com.example.demo.data.model.countries.CountryDTO
import com.example.demo.domain.model.countries.Country

internal fun CountryDTO.toDomainModel(): Country {
    return Country(
        name = name.common,
        capital = capital?.first(),
        latitude = capitalInfo.latLng?.get(0) ?: latLng[0],
        longitude = capitalInfo.latLng?.get(1) ?: latLng[1],
        altSpellings = altSpellings,
        population = population,
        flagUrl = flags.png,
        coatOfArmsUrl = coatOfArms.png
    )
}