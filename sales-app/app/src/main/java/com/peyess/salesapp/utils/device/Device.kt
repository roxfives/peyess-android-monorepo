package com.peyess.salesapp.utils.device

import android.content.Context
import android.content.res.Configuration
import android.graphics.Insets
import android.graphics.Point
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.text.TextUtils
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import com.peyess.salesapp.BuildConfig
import timber.log.Timber
import java.util.Enumeration

/**
 * These functions were base from this repository:
 * https://gist.github.com/hendrawd/01f215fd332d84793e600e7f82fc154b
 */

fun getConnectivityManager(context: Context): ConnectivityManager {
    return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

fun isConnectedToMobileInternet(c: Context): Boolean {
    val connectivityManager: ConnectivityManager = getConnectivityManager(c)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        network?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
    } else {
        val mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)?.state

        mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING
    }
}

fun isInternetAvailable(c: Context): Boolean {
    val connectivityManager = getConnectivityManager(c)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        network?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false ||
            network?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false ||
            network?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ?: false ||
            network?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: false
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        activeNetworkInfo != null
    }
}

fun infoAboutDevice(a: ComponentActivity?): String {
    if (a == null) {
        Timber.d("infoAboutDevice: Activity is null")
        return ""
    }
    val sb = StringBuilder()

    // application info
    sb.append("\n APP Package Name: ").append(BuildConfig.APPLICATION_ID)
        .append("\n App Version Name: ").append(BuildConfig.VERSION_NAME)
        .append("\n App Version Code: ").append(BuildConfig.VERSION_CODE)
        .append("\n")
    sb.append("\n OS Version: ").append(System.getProperty("os.version")).append(" (")
        .append(Build.VERSION.INCREMENTAL).append(")")
        .append("\n OS API Level: ").append(Build.VERSION.SDK)
        .append("\n Device: ").append(Build.DEVICE)
        .append("\n Model (and Product): ").append(Build.MODEL).append(" (").append(Build.PRODUCT)
        .append(")")

    // more from
    // http://developer.android.com/reference/android/os/Build.html:
    val screenSize = getScreenSize(a)
    sb.append("\n Manufacturer: ").append(Build.MANUFACTURER)
        .append("\n Other TAGS: ").append(Build.TAGS)
        .append("\n screenWidth: ").append(screenSize.x)
        .append("\n screenHeight: ").append(screenSize.y)
        .append("\n Keyboard available: ")
        .append(a.resources.configuration.keyboard != Configuration.KEYBOARD_NOKEYS)
        .append("\n Trackball available: ")
        .append(a.resources.configuration.navigation == Configuration.NAVIGATION_TRACKBALL)
        .append("\n SD Card state: ").append(Environment.getExternalStorageState())

    val p = System.getProperties()
    val keys: Enumeration<*> = p.keys()
    var key: String
    while (keys.hasMoreElements()) {
        key = keys.nextElement() as String
        sb.append("\n > ").append(key).append(" = ").append(p[key])
    }

    return sb.toString()
}

fun isPositioningViaWifiEnabled(context: Context): Boolean {
    val cr = context.contentResolver
    val enabledProviders: String = Settings.Secure.getString(
        cr,
        Settings.Secure.LOCATION_PROVIDERS_ALLOWED
    )
    if (!TextUtils.isEmpty(enabledProviders)) {
        // not the fastest way to do that :)
        val providersList = TextUtils.split(enabledProviders, ",")
        for (provider in providersList) {
            if (LocationManager.NETWORK_PROVIDER == provider) {
                return true
            }
        }
    }
    return false
}

fun isScreenOn(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    return powerManager.isScreenOn
}

fun isConnectedToWifi(c: Context): Boolean {
    val connectivityManager = getConnectivityManager(c)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        network?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    } else {
        val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state

        wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING
    }
}

/**
 * @return true if the current thread is the UI thread
 */
fun isUiThread(): Boolean {
    return Looper.getMainLooper().thread === Thread.currentThread()
}

/**
 * @param a _root_ide_package_.androidx.appcompat.app.ComponentActivity
 * @return the size with size.x=width and size.y=height
 */
fun getScreenSize(a: ComponentActivity?): Point {
    if (a == null) {
        return Point(0, 0)
    }

    // https://stackoverflow.com/questions/63719160/getsize-deprecated-in-api-level-30
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val currentWindowMetrics = a.windowManager.currentWindowMetrics

        val windowInsets = currentWindowMetrics.windowInsets
        var insets: Insets = windowInsets.getInsets(WindowInsets.Type.navigationBars())
        windowInsets.displayCutout?.run {
            insets = Insets.max(
                insets,
                Insets.of(safeInsetLeft, safeInsetTop, safeInsetRight, safeInsetBottom)
            )
        }

        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom

        Point(
            currentWindowMetrics.bounds.width() - insetsWidth,
            currentWindowMetrics.bounds.height() - insetsHeight
        )
    } else {
        Point().apply {
            a.windowManager.defaultDisplay.getSize(this)
        }
    }
}
