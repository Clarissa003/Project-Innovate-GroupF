package com.michael.potcastplant

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(private var notification: MutableList<NotificationClass>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return notification.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val NotificationClass = notification[position]
        holder.title.setText(NotificationClass.plantName)
        holder.text.setText(NotificationClass.message)
        holder.date.setText(NotificationClass.timestamp)
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cardView: CardView = itemView.findViewById(R.id.cvNotifications)
        val title : TextView = itemView.findViewById(R.id.tvTitle)
        val text : TextView = itemView.findViewById(R.id.tvText)
        val date : TextView = itemView.findViewById(R.id.tvDate)
    }
}