package com.example.pm_mars_photos

import android.app.Application
import com.example.pm_mars_photos.data.AppContainer
import com.example.pm_mars_photos.data.DefaultAppContainer

/**
 * Application class for Mars Photos, responsible for initializing the
 * application-wide dependency injection container.
 */
class MarsPhotosApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}