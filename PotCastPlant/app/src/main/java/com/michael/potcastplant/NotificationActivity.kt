package com.michael.potcastplant

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.potcastplant.databinding.ActivityNotificationBinding

class NotificationActivity : Fragment() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"}

    @RequiresApi(Build.VERSION_CODES.O)
    fun onCreateView(
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

        // How to tell the user that something has happened in the background.
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(context?.getResources(), R.drawable.ic_launcher_background))
                .setContentTitle("Hello")
                .setContentText("Your plants need attention")
        }
        else {
            builder = Notification.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentTitle("Hello")
                .setContentText("Your plants need attention")
        }
        notificationManager.notify(1234, builder.build())

        return binding.root
    }
