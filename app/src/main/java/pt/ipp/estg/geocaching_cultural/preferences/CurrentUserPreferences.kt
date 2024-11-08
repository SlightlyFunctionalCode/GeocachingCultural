package pt.ipp.estg.geocaching_cultural.preferences

import android.content.Context
import android.content.SharedPreferences

// Function to save the current user ID in SharedPreferences
fun saveCurrentUserId(context: Context, userId: Int) {
    // Get the SharedPreferences instance
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    // Edit the SharedPreferences to save the user ID
    sharedPreferences.edit().putInt("current_user_id", userId).apply()
}

fun getCurrentUserId(context: Context): Int {
    // Get the SharedPreferences instance
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    // Retrieve the user ID with a default value of -1 if it doesn't exist
    return sharedPreferences.getInt("current_user_id", -1)
}
