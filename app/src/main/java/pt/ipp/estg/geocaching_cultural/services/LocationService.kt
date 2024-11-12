package pt.ipp.estg.geocaching_cultural.services


import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import java.util.Locale

class LocationUpdateService(application: Application, viewsModels: UsersViewsModels) :
    AndroidViewModel(application) {

    val stationaryInterval = 15000L  // 15 seconds for stationary
    val movingInterval = 5000L       // 5 seconds for movement
    val user = viewsModels.currentUser.value

    companion object {
        fun getDistanceToGeocache(userLocation: Location, geocacheLocation: Location): Double {
            val userAndroidLocation = android.location.Location("user")
            userAndroidLocation.latitude = userLocation.lat
            userAndroidLocation.longitude = userLocation.lng

            val geocacheAndroidLocation = android.location.Location("geocache")
            geocacheAndroidLocation.latitude = geocacheLocation.lat
            geocacheAndroidLocation.longitude = geocacheLocation.lng

            return userAndroidLocation.distanceTo(geocacheAndroidLocation).toDouble()
        }

        fun getAddressFromCoordinates(
            context: Context,
            latitude: Double,
            longitude: Double
        ): LiveData<String?> {
            val addressLiveData = MutableLiveData<String?>() // MutableLiveData to hold the result

            // Run the geocoding process on a background thread
            CoroutineScope(Dispatchers.IO).launch {
                val geocoder = Geocoder(context, Locale.getDefault())
                try {
                    // Get the list of addresses from the coordinates
                    val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

                    // Check if we received any result
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0] // Take the first address result

                        // Format the address to "City, Country" format
                        val city = address.locality
                        val country = address.countryName

                        val formattedAddress = if (city != null && country != null) {
                            "$city, $country"
                        } else {
                            null
                        }

                        // Post the formatted address to LiveData
                        addressLiveData.postValue(formattedAddress)
                    } else {
                        // Post null if no address found
                        addressLiveData.postValue(null)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    addressLiveData.postValue(null) // Post null in case of an error
                }
            }

            return addressLiveData // Return the LiveData object
        }
    }

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        .setMinUpdateIntervalMillis(2500) // Fastest update every 5 seconds
        .build()


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val currentLocation = locationResult.lastLocation
            val hasMoved = user?.isWalking ?: false

            if (hasMoved) {
                // Update to faster interval when user is moving
                locationRequest.setInterval(movingInterval)
            } else {
                // Switch to slower interval when stationary
                locationRequest.setInterval(stationaryInterval)
            }

            // Now handle the location update as needed
            if (currentLocation != null) {
                updateUserLocation(currentLocation.latitude, currentLocation.longitude)
            }
        }

        private fun updateUserLocation(
            currentLatitude: Double,
            currentLongitude: Double
        ) {
            if (user != null) {
                // Update user's walking status and other user details
                val updatedUser = user.copy(
                    location =
                    Location(
                        lat = currentLatitude,
                        lng = currentLongitude,
                    )
                )

                // Update user in ViewModel
                viewsModels.updateUser(updatedUser)
            }
        }
    }

    fun startLocationUpdates(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}


