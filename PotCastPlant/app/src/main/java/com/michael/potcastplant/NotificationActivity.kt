package com.michael.potcastplant

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
            .whereEqualTo("userId", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)
            .get().addOnSuccessListener {result ->
                val notifications = mutableListOf<NotificationClass>()
                val documentCount = result.size()

                for (document in result){
                    val plantName = document.getString("plantName") ?: ""
                    val message = document.getString("message") ?: ""
                    val timestamp = document.getTimestamp("timestamp")

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
