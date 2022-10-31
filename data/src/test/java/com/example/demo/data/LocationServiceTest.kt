package com.example.demo.data

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.test.core.app.ApplicationProvider
import com.example.demo.data.service.location.LocationService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowLooper

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    shadows = [ShadowLocationServices::class]
)
class LocationServiceTest {

    private lateinit var locationService: LocationService

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        locationService = LocationService(context)
    }

    @Test
    fun `test current location`() = runTest(UnconfinedTestDispatcher()) {
        val locationAsync = async { locationService.getLocation() }
        ShadowLooper.shadowMainLooper().idle()
        Assert.assertEquals(CURRENT_LOCATION, locationAsync.await())
    }

    @Test
    fun `test location flow`() = runTest {
        val locations = locationService.locationFlow.take(3).toList()
        Assert.assertEquals(FLOW_LOCATION, locations)
    }

    companion object {
        val CURRENT_LOCATION = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 20.0
            longitude = 20.0
        }
        val FLOW_LOCATION = List(3) { i ->
            Location(LocationManager.GPS_PROVIDER).apply {
                latitude = 20.0 + i
                longitude = 20.0 + i
            }
        }
    }
}

@Implements(LocationServices::class)
object ShadowLocationServices {

    @Implementation
    @JvmStatic
    fun getFusedLocationProviderClient(context: Context): FusedLocationProviderClient {
       return mockk(relaxed = true) {
           every { lastLocation } returns Tasks.forResult(LocationServiceTest.CURRENT_LOCATION)

           every { requestLocationUpdates(any(), any(), any()) } answers {
               val locationCallback = arg<LocationCallback>(1)
               LocationServiceTest.FLOW_LOCATION.forEach { location ->
                   locationCallback.onLocationResult(LocationResult.create(listOf(location)))
               }
               Tasks.forResult(null)
           }
       }
    }
}