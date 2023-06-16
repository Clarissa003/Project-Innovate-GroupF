package com.michael.potcastplant

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FeedsAdapter(private var feeds: MutableList<FeedsPostClass>) : RecyclerView.Adapter<FeedsAdapter.viewHolder>() {


    override fun getItemCount(): Int {
        return feeds.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feeds, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val feedsPost = feeds[position]
        Glide.with(holder.itemView)
            .load(feedsPost.profileUrl)
            .circleCrop()
            .placeholder(R.drawable.baseline_person_24)
            .into(holder.profilePic)

        Glide.with(holder.itemView)
            .load(feedsPost.imageUrl)
            .into(holder.postImage)

        holder.description.setText(feedsPost.description)
        holder.firstname.setText(feedsPost.firstName)

        val currentTimeMillis = System.currentTimeMillis()
        val timestampMillis = feedsPost.timestamp.toDate().time
        val timeDifference = currentTimeMillis - timestampMillis

        val relativeTime = DateUtils.getRelativeTimeSpanString(timestampMillis, currentTimeMillis, DateUtils.MINUTE_IN_MILLIS)

        holder.timestamp.setText(relativeTime)
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timestamp: TextView = itemView.findViewById(R.id.text_view_timestamp)
        var profilePic: ImageView = itemView.findViewById(R.id.image_view_profile_pic)
        var firstname: TextView = itemView.findViewById(R.id.text_view_username)
        var postImage : ImageView = itemView.findViewById(R.id.image_view_post_img)
        var description: TextView = itemView.findViewById(R.id.text_view_description)
    }
}
