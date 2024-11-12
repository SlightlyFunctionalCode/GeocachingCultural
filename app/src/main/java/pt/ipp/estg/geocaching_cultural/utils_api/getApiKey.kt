package pt.ipp.estg.geocaching_cultural.utils_api

import android.content.Context
import android.content.pm.PackageManager

fun getApiKey(context: Context): String? {
    return try {
        val info = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        info.metaData?.getString("com.google.android.geo.API_KEY")
    } catch (e: PackageManager.NameNotFoundException) {
        null // Lidere com o erro
    }
}