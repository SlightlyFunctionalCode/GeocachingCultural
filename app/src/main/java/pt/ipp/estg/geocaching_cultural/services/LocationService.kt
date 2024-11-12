package pt.ipp.estg.geocaching_cultural.services


import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
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
import kotlinx.coroutines.withContext
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import java.util.Locale

class LocationUpdateService(application: Application, viewsModels: UsersViewsModels) :
    AndroidViewModel(application) {

    var lastKnownLocation: android.location.Location? = null
    val stationaryInterval = 15000L  // 15 seconds for stationary
    val movingInterval = 5000L       // 5 seconds for movement

    companion object {
        fun getDistanceToGeocache(userLocation: Location, geocacheLocation: Location): Double {
            val userAndroidLocation = android.location.Location("user")
            userAndroidLocation.latitude = userLocation.latitude
            userAndroidLocation.longitude = userLocation.longitude

            val geocacheAndroidLocation = android.location.Location("geocache")
            geocacheAndroidLocation.latitude = geocacheLocation.latitude
            geocacheAndroidLocation.longitude = geocacheLocation.longitude

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
            val hasMoved = (currentLocation?.let { lastKnownLocation?.distanceTo(it) }
                ?: Float.MAX_VALUE) > 5f

            if (hasMoved) {
                // Update to faster interval when user is moving
                locationRequest.setInterval(movingInterval)
                lastKnownLocation = currentLocation
            } else {
                // Switch to slower interval when stationary
                locationRequest.setInterval(stationaryInterval)
            }

            // Now handle the location update as needed
            if (currentLocation != null) {
                updateUserLocation(hasMoved, currentLocation.latitude, currentLocation.longitude)
            }
        }


        private fun updateUserLocation(
            isWalking: Boolean,
            currentLatitude: Double,
            currentLongitude: Double
        ) {
            // Retrieve the current user ID from SharedPreferences
            val sharedPreferences = getApplication<Application>()
                .getSharedPreferences("user_prefs", MODE_PRIVATE)
            val userId = sharedPreferences.getInt("current_user_id", -1)

            if (userId != -1) {
                val user = viewsModels.currentUser.value ?: throw NotFoundException()

                // Update user's walking status and other user details
                val updatedUser = User(
                    userId,
                    user.name,
                    user.email,
                    user.password,
                    user.points,
                    user.profileImageUrl,
                    user.profilePictureDefault,
                    isWalking, // Update walking status
                    Location(latitude = currentLatitude, longitude = currentLongitude, address = "")
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
            // Request permissions if not granted
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


