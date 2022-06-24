package com.example.demo.data.di

import com.example.demo.data.BuildConfig
import com.example.demo.data.api.CountriesApi
import com.example.demo.data.api.GithubApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

internal val networkModule = module {
    single {
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor()
                        .apply { setLevel(HttpLoggingInterceptor.Level.BASIC) })
                }
            }
            .build()
    }

    single(qualifier(NetworkApiQualifier.GITHUB)) {
        provideRetrofit(BuildConfig.GITHUB_SERVER_URL, get())
    }

    single(qualifier(NetworkApiQualifier.COUNTRIES)) {
        provideRetrofit(BuildConfig.COUNTRIES_SERVER_URL, get())
    }

    single { get<Retrofit>(qualifier(NetworkApiQualifier.GITHUB)).create<GithubApi>() }

    single { get<Retrofit>(qualifier(NetworkApiQualifier.COUNTRIES)).create<CountriesApi>() }
}

private fun provideRetrofit(serverUrl: String, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(serverUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}