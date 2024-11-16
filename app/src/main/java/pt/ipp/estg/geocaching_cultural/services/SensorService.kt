package pt.ipp.estg.geocaching_cultural.services

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels

class SensorService(context: Context, viewsModels: UsersViewsModels) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var isWalking = false
    private var gravity = floatArrayOf(0f, 0f, 0f)
    private val alpha = 0.8f // Low-pass filter factor
    private val accelerationThreshold: Float = 0.5f // Adjust for sensitivity
    private val noMovementTimeout = 2000L // 2 seconds
    private var lastMovementTime = System.currentTimeMillis()
    private var lastPeakTime = 0L

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                handleAccelerometer(event)
            }
            updateWalkingState(viewsModels)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    private var lastUpdateTimestamp = System.currentTimeMillis()
    private val updateInterval = 1000L // 1 second

    private fun handleAccelerometer(event: SensorEvent) {
        val (x, y, z) = event.values

        // Low-pass filter to isolate gravity
        gravity[0] = alpha * gravity[0] + (1 - alpha) * x
        gravity[1] = alpha * gravity[1] + (1 - alpha) * y
        gravity[2] = alpha * gravity[2] + (1 - alpha) * z

        // Remove gravity to get vertical acceleration
        val verticalAcceleration = z - gravity[2]
        Log.d("SensorService", "Vertical acceleration: $verticalAcceleration")

        // Detect peaks in vertical acceleration
        if (verticalAcceleration > accelerationThreshold && isPeakDetected(verticalAcceleration)) {
            isWalking = true
            Log.d("SensorService", "Is walking: $isWalking") // Log walking state

            lastMovementTime = System.currentTimeMillis() // Reset timeout
        }

        // Update walking state

    }


    private fun isPeakDetected(magnitude: Float): Boolean {
        val currentTime = System.currentTimeMillis()
        val isPeak =
            magnitude > accelerationThreshold && (currentTime - lastPeakTime > 300) // Debounce

        Log.d("SensorService", "Checking peak: magnitude = $magnitude, isPeak = $isPeak")

        if (isPeak) lastPeakTime = currentTime
        return isPeak
    }

    private fun updateWalkingState(viewsModels: UsersViewsModels) {
        val currentTime = System.currentTimeMillis()

        // Reset walking state if no steps detected within timeout
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

    fun startSensorUpdates() {
        accelerometer?.let {
            sensorManager.registerListener(
                sensorEventListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stopSensorUpdates() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}

