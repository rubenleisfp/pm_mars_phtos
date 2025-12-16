package com.example.pm_mars_photos.fake

import com.example.pm_mars_photos.data.MarsPhotosRepository
import com.example.pm_mars_photos.model.MarsPhoto

class FakeNetworkMarsPhotosRepository : MarsPhotosRepository {
   override suspend fun getMarsPhotos(): List<MarsPhoto> {
      return FakeDataSource.photosList
   }
}