package com.michael.potcastplant

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
        val FeedsPostClass = feeds[position]
        Glide.with(holder.itemView)
            .load(FeedsPostClass.profileUrl)
            .into(holder.profilePic)

        Glide.with(holder.itemView)
            .load(FeedsPostClass.imageUrl)
            .into(holder.postImage)

        holder.description.setText(FeedsPostClass.description)
        holder.firstname.setText(FeedsPostClass.firstName)
        holder.timestamp.setText(FeedsPostClass.timestamp)

    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timestamp: TextView = itemView.findViewById(R.id.text_view_timestamp)
        var profilePic: ImageView = itemView.findViewById(R.id.image_view_profile_pic)
        var firstname: TextView = itemView.findViewById(R.id.text_view_username)
        var postImage : ImageView = itemView.findViewById(R.id.image_view_post_img)
        var description: TextView = itemView.findViewById(R.id.text_view_description)
    }
}
