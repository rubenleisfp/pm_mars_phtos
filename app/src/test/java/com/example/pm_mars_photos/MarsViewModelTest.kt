package com.example.pm_mars_photos

import com.example.pm_mars_photos.fake.FakeDataSource
import com.example.pm_mars_photos.fake.FakeNetworkMarsPhotosRepository
import com.example.pm_mars_photos.rules.TestDispatcherRule
import com.example.pm_mars_photos.ui.screens.MarsUiState
import com.example.pm_mars_photos.ui.screens.MarsViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Created by Your name on 16/12/2025.
 */
class MarsViewModelTest {


    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun marsViewModel_getMarsPhotos_verifyMarsUiStateSuccess() =
        runTest {
            val marsViewModel = MarsViewModel(
                marsPhotosRepository = FakeNetworkMarsPhotosRepository()
            )
            assertEquals(
                MarsUiState.Success("Success: ${FakeDataSource.photosList.size} Mars " +
                        "photos retrieved"),
                marsViewModel.marsUiState
            )
        }
 }
