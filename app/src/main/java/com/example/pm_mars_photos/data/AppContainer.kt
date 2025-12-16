package com.example.pm_mars_photos.data

import com.example.pm_mars_photos.network.MarsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * Created by Your name on 16/12/2025.
 */
interface AppContainer  {
    val marsPhotosRepository: MarsPhotosRepository
}

class DefaultAppContainer() : AppContainer {

    private val BASE_URL =
        "https://android-kotlin-fun-mars-server.appspot.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    /**
     * A lazy-initialized Retrofit service instance for making network requests.
     */
    private val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }


    /**
     * A repository for accessing Mars photos data.
     * Injected retrofit service to get mars photos
     */
    override val marsPhotosRepository: MarsPhotosRepository by lazy {
        NetworkMarsPhotosRepository(retrofitService)
    }

}