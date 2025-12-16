package com.example.pm_mars_photos.fake

import com.example.pm_mars_photos.model.MarsPhoto
import com.example.pm_mars_photos.network.MarsApiService

/**
 * Created by Your name on 16/12/2025.
 */
class FakeMarsApiService : MarsApiService {
    override suspend fun getPhotos(): List<MarsPhoto> {
        return FakeDataSource.photosList
    }
}