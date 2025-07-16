package com.example.rescuenow

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class EmergencyService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private var emergencyNumber: String? = null
    private var locationSent = false  // ✅ This is now reset per session

    companion object {
        const val CHANNEL_ID = "EmergencyLocationChannel"
        const val NOTIFICATION_ID = 101
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        emergencyNumber = intent?.getStringExtra("emergency_number")
        locationSent = false  // 🔥 This is what resets sending again
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000L
        ).setMinUpdateIntervalMillis(5000L).build()

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("EmergencyService", "Location permission not granted.")
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            mainLooper
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            lastKnownLocation = result.lastLocation
            if (!locationSent && emergencyNumber != null && lastKnownLocation != null) {
                val message = "📍 I am at https://maps.google.com/?q=${lastKnownLocation!!.latitude},${lastKnownLocation!!.longitude}"
                sendSms(emergencyNumber!!, message)
                locationSent = true // ✅ Only once per session
            }
        }
    }

    private fun sendSms(phoneNumber: String, message: String) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getSystemService(SmsManager::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    SmsManager.getDefault()
                }
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Log.d("EmergencyService", "Location SMS sent to $phoneNumber")
            } catch (e: Exception) {
                Log.e("EmergencyService", "Failed to send SMS", e)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Emergency Location Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Tracks location in the background during emergencies."
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Emergency Service Running")
            .setContentText("Sharing your location...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
