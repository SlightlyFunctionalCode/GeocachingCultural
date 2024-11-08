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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipp.estg.geocaching_cultural.database.AppDatabase
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.repositories.UserRepository
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels

class LocationUpdateService(application: Application, viewsModels: UsersViewsModels) :
    AndroidViewModel(application) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private val locationRequest = LocationRequest.create().apply {
        interval = 10000 // Update every 10 seconds
        fastestInterval = 5000 // Fastest update every 5 seconds
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                    updateUserLocation(
                        viewsModels,
                        location.latitude,
                        location.longitude
                    )
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

    private fun updateUserLocation(
        viewsModels: UsersViewsModels,
        lat: Double,
        lng: Double
    ) {
        // Retrieve the current user ID from SharedPreferences
        val sharedPreferences = getApplication<Application>()
            .getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("current_user_id", -1)

        if (userId != -1) {
            // Create a User object with the updated location
            val location = Location(lat, lng, "")

            val user = viewsModels.currentUser.value ?: throw NotFoundException()

            val updatedUser = User(
                userId,
                user.name,
                user.email,
                user.password,
                user.points,
                user.profileImageUrl,
                location
            )

            viewsModels.updateUser(user)
        }
    }
}
