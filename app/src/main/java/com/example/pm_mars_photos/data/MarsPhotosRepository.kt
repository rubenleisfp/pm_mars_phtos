package com.example.pm_mars_photos.data

/**
 * Created by Your name on 16/12/2025.
 */
import com.example.pm_mars_photos.model.MarsPhoto
import com.example.pm_mars_photos.network.MarsApiService

/**
 * Repository that fetch mars photos list from marsApi.
 */
interface MarsPhotosRepository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun getMarsPhotos(): List<MarsPhoto>
}

/**
 * Network Implementation of Repository that fetch mars photos list from marsApi.
 */
class NetworkMarsPhotosRepository(
    private val marsApiService: MarsApiService
) : MarsPhotosRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun getMarsPhotos(): List<MarsPhoto> = marsApiService.getPhotos()
}