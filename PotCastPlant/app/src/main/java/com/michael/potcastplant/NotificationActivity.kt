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
import com.google.firebase.firestore.Query
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
    ): View {
        binding = ActivityNotificationBinding.inflate(inflater, container, false)
        return binding.root
     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        fetchNotifications { notifications ->
            bind(notifications)
        }
    }

    private fun fetchNotifications(callback: (MutableList<NotificationClass>) -> Unit) {
        val uid = sharedPreferences.getString("uid", null) ?: ""
        val document = firestore.collection("notifications")

        document
            .whereEqualTo("uid", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {result ->
                val notifications = mutableListOf<NotificationClass>()
                val documentCount = result.size()

                for (document in result){
                    val plantName = document.getString("plantName") ?: ""
                    val message = document.getString("message") ?: ""
                    val timestamp = document.getString("timestamp") ?: ""

                    val notification = NotificationClass(plantName, message, timestamp!!)
                    notifications.add(notification)
                }

                if (notifications.size == documentCount) {
                    callback(notifications)
                }
            }
    }

    private fun bind(notifications: MutableList<NotificationClass>) {
        val adapterNotification = NotificationAdapter(notifications)
        binding.rvNotification.adapter = adapterNotification
        adapterNotification.notifyDataSetChanged()
    }
}
