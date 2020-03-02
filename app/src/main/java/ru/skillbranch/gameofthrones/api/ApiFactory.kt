package ru.skillbranch.gameofthrones.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.skillbranch.gameofthrones.AppConfig.BASE_URL

object ApiFactory {
    private lateinit var retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    fun getApiService(): ApiService = retrofit.create(ApiService::class.java)
}