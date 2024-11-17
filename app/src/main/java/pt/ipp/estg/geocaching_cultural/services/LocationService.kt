package pt.ipp.estg.geocaching_cultural.services

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import java.util.Locale

class LocationUpdateService(application: Application, viewsModels: UsersViewsModels) :
    AndroidViewModel(application) {

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
            location: Location
        ): LiveData<String?> {
            val addressLiveData = MutableLiveData<String?>() // MutableLiveData to hold the result

            // Run the geocoding process on a background thread
            CoroutineScope(Dispatchers.IO).launch {
                val geocoder = Geocoder(context, Locale.getDefault())
                try {
                    // Get the list of addresses from the coordinates
                    val addresses: List<Address>? =
                        geocoder.getFromLocation(location.lat, location.lng, 1)

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
            val currentLocation: android.location.Location? = locationResult.lastLocation
            
            // Now handle the location update as needed
            if (currentLocation != null) {
                updateUserLocation(
                    application.applicationContext,
                    currentLocation.latitude,
                    currentLocation.longitude
                )
            }
        }

        private fun updateUserLocation(
            context: Context,
            currentLatitude: Double,
            currentLongitude: Double
        ) {
            val user = viewsModels.currentUser.value

            if (user != null) {
                val updatedUser = user.copy(
                    location = Location(
                        lat = currentLatitude,
                        lng = currentLongitude,
                    )
                )
                viewsModels.updateUser(updatedUser)
            } else {
                Toast.makeText(context, "There was an error updating Location", Toast.LENGTH_SHORT)
                    .show()
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

    fun checkLocationSettings(
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (ResolvableApiException?) -> Unit
    ) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            onSuccess()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                onFailure(exception)
            } else {
                onFailure(null)
            }
        }
    }


}

@Composable
fun EnableLocation(
    context: Context,
    locationUpdateService: LocationUpdateService,
    onSuccess: () -> Unit
) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onSuccess()
            } else {
                Toast.makeText(context, "Location services are required.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    LaunchedEffect(Unit) {
        locationUpdateService.checkLocationSettings(
            context = context,
            onSuccess = {
                // If the location is already enabled, proceed
                onSuccess()
            },
            onFailure = { resolvableException ->
                resolvableException?.let {
                    try {
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(it.resolution).build()
                        launcher.launch(intentSenderRequest)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Log.e("EnableLocation", "Error launching location dialog", sendEx)
                    }
                } ?: run {
                    Toast.makeText(
                        context,
                        "Unable to resolve location settings.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
}


