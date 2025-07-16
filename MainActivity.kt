package com.example.rescuenow

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {

    private lateinit var editPhone: EditText
    private lateinit var btnSendAlert: Button
    private lateinit var btnPolice: Button
    private lateinit var btnFire: Button
    private lateinit var btnAmbulance: Button
    private lateinit var btnSaveContact: Button
    private lateinit var btnUseSaved: Button

    private val allPermissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAllPermissions()

        editPhone = findViewById(R.id.editPhone)
        btnSendAlert = findViewById(R.id.btnSendAlert)
        btnPolice = findViewById(R.id.btnPolice)
        btnFire = findViewById(R.id.btnFire)
        btnAmbulance = findViewById(R.id.btnAmbulance)
        btnSaveContact = findViewById(R.id.btnSaveContact)
        btnUseSaved = findViewById(R.id.btnUseSaved)

        btnSendAlert.setOnClickListener {
            val number = editPhone.text.toString()
            if (number.isNotEmpty()) {
                sendSms(number, "🚨 Emergency! I need help. Please reach out immediately.")
                val serviceIntent = Intent(this, EmergencyService::class.java).apply {
                    putExtra("emergency_number", number)
                }
                ContextCompat.startForegroundService(this, serviceIntent)
                showToast("Emergency service started!")
            } else {
                showToast("Please enter a valid phone number.")
            }
        }

        btnSaveContact.setOnClickListener {
            val number = editPhone.text.toString()
            if (number.isNotEmpty()) {
                getSharedPreferences("RescueNowPrefs", MODE_PRIVATE).edit {
                    putString("emergency_contact", number)
                    apply()
                }
                showToast("Contact saved!")
            } else {
                showToast("Enter a number to save.")
            }
        }

        btnUseSaved.setOnClickListener {
            val savedNumber = getSharedPreferences("RescueNowPrefs", MODE_PRIVATE)
                .getString("emergency_contact", "")
            if (!savedNumber.isNullOrEmpty()) {
                editPhone.setText(savedNumber)
                showToast("Loaded saved contact.")
            } else {
                showToast("No saved contact found.")
            }
        }

        btnPolice.setOnClickListener { callEmergencyNumber("100") }
        btnFire.setOnClickListener { callEmergencyNumber("101") }
        btnAmbulance.setOnClickListener { callEmergencyNumber("108") }
    }

    private fun sendSms(phoneNumber: String, message: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getSystemService(SmsManager::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    SmsManager.getDefault()
                }
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                showToast("Emergency SMS sent to $phoneNumber")
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Failed to send SMS.")
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 1000)
        }
    }

    private fun callEmergencyNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$number".toUri()
        }
        startActivity(intent)
    }

    private fun requestAllPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= 34) {
            permissions.add(Manifest.permission.FOREGROUND_SERVICE_LOCATION)
            permissions.add(Manifest.permission.FOREGROUND_SERVICE_PHONE_CALL)
        }

        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), allPermissionCode)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
