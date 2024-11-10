package pt.ipp.estg.geocaching_cultural.services


import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels

class LocationUpdateService(application: Application, viewsModels: UsersViewsModels) :
    AndroidViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .setMinUpdateIntervalMillis(5000) // Fastest update every 5 seconds
        .build()

    /*TODO: mudar código para ficar mais fácil de ler */
    private val locationCallback = object : LocationCallback() {
        private var lastLocation: pt.ipp.estg.geocaching_cultural.database.classes.Location? = null
        private var lastUpdateTime: Long = 0
        private val STOPPED_THRESHOLD = 10 * 1000 // 10 seconds in milliseconds

        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { currentLocation ->
                // Only calculate if we have a previous location
                lastLocation?.let { prevLocation ->
                    // Convert your custom Location to android.location.Location
                    val prevAndroidLocation = android.location.Location("previous")
                    prevAndroidLocation.latitude = prevLocation.latitude
                    prevAndroidLocation.longitude = prevLocation.longitude

                    val currentAndroidLocation = android.location.Location("current")
                    currentAndroidLocation.latitude = currentLocation.latitude
                    currentAndroidLocation.longitude = currentLocation.longitude

                    // Calculate distance between last and current location
                    val distance = prevAndroidLocation.distanceTo(currentAndroidLocation)

                    // Check if user has stopped moving for 10 seconds
                    if (distance < 1) { // You can adjust this threshold (1 meter)
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastUpdateTime > STOPPED_THRESHOLD) {
                            updateWalkingStatus(
                                false,
                                currentLocation.latitude,
                                currentLocation.longitude
                            )
                        }
                    } else {
                        updateWalkingStatus(
                            true,
                            currentLocation.latitude,
                            currentLocation.longitude
                        )
                        lastUpdateTime = System.currentTimeMillis() // Reset timer
                    }
                }

                // Update last known location
                lastLocation = pt.ipp.estg.geocaching_cultural.database.classes.Location(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    "" // You can pass the other data you need for Location
                )
            }
        }

        private fun updateWalkingStatus(
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

