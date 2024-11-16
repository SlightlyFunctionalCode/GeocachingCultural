package pt.ipp.estg.geocaching_cultural.services

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import kotlin.math.sqrt

class SensorService(context: Context, viewsModels: UsersViewsModels) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val linearAccelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    private val accelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var isWalking = false
    private val accelerationThreshold: Float = 10f // Adjust for sensitivity
    private val linearAccelerationThreshold: Float = 0.5f // Adjust for sensitivity
    private val noMovementTimeout = 1000L // 2 seconds
    private var lastMovementTime = System.currentTimeMillis()

    private val updateInterval = 1000L // 1 second for database updates
    private var lastUpdateTimestamp = System.currentTimeMillis()

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                handleLinearAcceleration(event)
            } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                // Fallback if linear accelerometer isn't available
                handleAcceleration(event)
            }
            updateWalkingState(viewsModels)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    /**
     * Handles linear acceleration sensor events.
     */
    private fun handleLinearAcceleration(event: SensorEvent) {
        val (x, y, z) = event.values
        val magnitude = sqrt(x * x + y * y + z * z)

        if (magnitude > linearAccelerationThreshold && isPeakDetected(magnitude)) {
            isWalking = true
            lastMovementTime = System.currentTimeMillis() // Reset timeout
        }
    }

    private fun handleAcceleration(event: SensorEvent) {
        val (x, y, z) = event.values
        val magnitude = sqrt(x * x + y * y + z * z)

        if (magnitude > accelerationThreshold && isPeakDetected(magnitude)) {
            isWalking = true
            lastMovementTime = System.currentTimeMillis() // Reset timeout
        }
    }

    /**
     * Detects peaks in the magnitude to indicate significant movement.
     */
    private var lastPeakTime = 0L
    private val peakInterval = 300L // Minimum time between peaks (ms)
    private fun isPeakDetected(magnitude: Float): Boolean {
        val currentTime = System.currentTimeMillis()
        val isPeak =
            magnitude > accelerationThreshold && (currentTime - lastPeakTime > peakInterval)
        if (isPeak) {
            lastPeakTime = currentTime
        }
        return isPeak
    }

    /**
     * Updates the walking state in the database periodically.
     */
    private fun updateWalkingState(viewsModels: UsersViewsModels) {
        val currentTime = System.currentTimeMillis()

        // Reset walking state if no movement detected within timeout
        if (isWalking && (currentTime - lastMovementTime > noMovementTimeout)) {
            isWalking = false
        }

        // Periodically update the walking state in the database
        if (currentTime - lastUpdateTimestamp >= updateInterval) {
            val user = viewsModels.currentUser.value ?: throw NotFoundException()
            val updatedUser = user.copy(isWalking = isWalking)
            viewsModels.updateUser(updatedUser)
            lastUpdateTimestamp = currentTime
        }
    }

    /**
     * Starts listening to sensor updates.
     */
    fun startSensorUpdates(context: Context) {
        // Register the appropriate sensor based on availability
        if (linearAccelerometer != null) {
            sensorManager.registerListener(sensorEventListener, linearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        } else if (accelerometer != null) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("SensorService", "Using accelerometer as fallback.")
        } else {
            Log.e("SensorService", "No suitable sensor available. Walking detection cannot be performed.")
            Toast.makeText(context, "Walking detection is not supported on this device.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Stops listening to sensor updates.
     */
    fun stopSensorUpdates() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}
