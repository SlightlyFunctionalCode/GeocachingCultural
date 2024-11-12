package pt.ipp.estg.geocaching_cultural.services

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import kotlin.math.sqrt

class SensorService(context: Context, viewsModels: UsersViewsModels) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    private var isWalking = false
    private val accelerationThreshold: Float = 10f

    private var lastAccelValues = floatArrayOf(0f, 0f, 0f)
    private val alpha = 0.8f // Smoothing factor

    /* TODO:add Permissions*/

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> handleAccelerometer(event)
                Sensor.TYPE_STEP_DETECTOR -> isWalking = true // Confirmed step detected
            }
            updateUserWalkingState(isWalking, viewsModels)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    private var lastUpdateTimestamp = System.currentTimeMillis()
    private val updateInterval = 1000L // 1 second

    private fun updateUserWalkingState(isWalking: Boolean, viewsModels: UsersViewsModels) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTimestamp >= updateInterval) {
            val user = viewsModels.currentUser.value ?: throw NotFoundException()

            val updatedUser = user.copy(isWalking = isWalking)

            viewsModels.updateUser(updatedUser)
            lastUpdateTimestamp = currentTime
        }
    }

    private var movementCounter = 0
    private val movementThresholdCounter = 5 // Number of movements to confirm walking
    private fun handleAccelerometer(event: SensorEvent) {
        val (x, y, z) = event.values

        // Apply low-pass filter
        lastAccelValues[0] = alpha * lastAccelValues[0] + (1 - alpha) * x
        lastAccelValues[1] = alpha * lastAccelValues[1] + (1 - alpha) * y
        lastAccelValues[2] = alpha * lastAccelValues[2] + (1 - alpha) * z

        // Calculate smoothed acceleration magnitude
        val accelerationMagnitude = sqrt(
            lastAccelValues[0] * lastAccelValues[0] +
                    lastAccelValues[1] * lastAccelValues[1] +
                    lastAccelValues[2] * lastAccelValues[2]
        )

        if (accelerationMagnitude > accelerationThreshold) {
            movementCounter++
        } else {
            movementCounter = maxOf(0, movementCounter - 1) // Gradually reset counter when idle
        }

        // Update walking status based on sustained movement
        isWalking = movementCounter >= movementThresholdCounter
    }


    fun startSensorUpdates() {
        accelerometer?.let {
            sensorManager.registerListener(
                sensorEventListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
        stepDetector?.let {
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