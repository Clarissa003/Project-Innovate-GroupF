package com.michael.potcastplant

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.Constraints
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.work.NetworkType
//import androidx.work.OneTimeWorkRequestBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityNotificationBinding

class NotificationActivity : Fragment() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private var threshold = 20
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val sharedPreferences: SharedPreferences by lazy { requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE) }


    @RequiresApi(Build.VERSION_CODES.O)
     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityNotificationBinding.inflate(inflater, container, false)

        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())

        val notifications = arrayOf(
            NotificationClass("Bay Leaves", "The water level in the reservoir is low, fill it up.", "30.05.2023"),
            NotificationClass("Rose Flowers", "Your plants needs more sunlight, the average sunlight level is low, you might want to place your plant in a position to receive more sunlight", "28.05.2023"),
            NotificationClass("Mahogany", "Your plant moisture level is low, please water your plant or turn on automatic watering feature for your plant", "25.05.2023"),
        )

        val adapterNotification = NotificationAdapter(notifications)
        binding.rvNotification.adapter = adapterNotification

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val uid = sharedPreferences.getString("uid", null) ?: ""

        val document = firestore.collection("pots").document(uid)
        document.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                document.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {

                        val waterLevel = documentSnapshot.getLong("waterlevel")
                        val moistureLevel = documentSnapshot.getLong("moisture")

                        // How to tell the user that something has happened in the background.
                        val notificationManager =
                            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notificationChannel = NotificationChannel(
                                channelId,
                                description,
                                NotificationManager.IMPORTANCE_HIGH
                            )
                            notificationChannel.enableLights(true)
                            notificationChannel.lightColor = Color.GREEN
                            notificationChannel.enableVibration(false)
                            notificationManager.createNotificationChannel(notificationChannel)

                            if (waterLevel != null && waterLevel.compareTo(threshold) < 0) {
                                val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                                val notificationId = 1  // Unique identifier for the notification

                                // Create the notification
                                val notification = builder.build()

                                // Build your notification using NotificationCompat.Builder
                                builder = Notification.Builder(this.context, channelId)
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setLargeIcon(
                                        BitmapFactory.decodeResource(
                                            this.resources,
                                            R.drawable.ic_launcher_background
                                        )
                                    )
                                    .setContentTitle("Critical water level")
                                    .setContentText("Your water tank is almost empty, please fill it up")

                                // Send the notification
                                notificationManager.notify(notificationId, notification)
                            }

                            if (moistureLevel != null && moistureLevel.compareTo(threshold) < 0) {
                                val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                                val notificationId = 1  // Unique identifier for the notification

                                // Create the notification
                                val notification = builder.build()

                                // Build your notification using NotificationCompat.Builder
                                builder = Notification.Builder(this.context, channelId)
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setLargeIcon(
                                        BitmapFactory.decodeResource(
                                            this.resources,
                                            R.drawable.ic_launcher_background
                                        )
                                    )
                                    .setContentTitle("Critical moisture Level")
                                    .setContentText("Your plant needs water")

                                // Send the notification
                                notificationManager.notify(notificationId, notification)
                            }
                        }
                        notificationManager.notify(1234, builder.build())
                    }
                }
            }
        }
        return binding.root
     }
}
