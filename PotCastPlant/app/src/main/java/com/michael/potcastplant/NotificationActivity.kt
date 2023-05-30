package com.michael.potcastplant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.potcastplant.databinding.ActivityNotificationBinding

class NotificationActivity : Fragment() {

    private lateinit var binding: ActivityNotificationBinding
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

        return binding.root
    }
}