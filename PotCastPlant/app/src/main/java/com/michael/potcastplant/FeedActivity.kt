package com.michael.potcastplant

import AddPostActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityFeedBinding

class FeedActivity : Fragment() {

    private lateinit var binding : ActivityFeedBinding

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityFeedBinding.inflate(inflater, container, false)

        binding.recyclerViewFeed.layoutManager = LinearLayoutManager(requireContext())

        val posts = arrayOf(
            FeedsPostClass("Michael", R.drawable.baseline_person_24, R.drawable.plants, "This is a very nice flower, I will love to have some soon", "1 days ago"),
            FeedsPostClass("Naga", R.drawable.baseline_person_24, R.drawable.ic_launcher_background, "Nothing to post here.. haha, you wis", "3 days ago"),
            FeedsPostClass("Michael", R.drawable.baseline_person_24, R.drawable.potcast_logo, "Bla bla bla, our logo", "4 min ago")
        )

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val adapter = FeedsAdapter(posts)
        binding.recyclerViewFeed.adapter = adapter

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.floatingButtonAddPost.setOnClickListener {
            val intent = Intent(this.context, AddPostActivity::class.java)
            startActivity(intent)
        }

    }

// Placeholder for FeedsAdapter class
    private class FeedsAdapter(private val posts: Array<FeedsPostClass>) :
        RecyclerView.Adapter<FeedsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // Create and return the ViewHolder
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feeds, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Bind data to the ViewHolder
            val post = posts[position]
            // Set the values to the corresponding views in the item_feed layout
            // holder.textViewUsername.text = post.username
            // holder.imageViewProfilePic.setImageResource(post.profilePic)
            // holder.imageViewPostImg.setImageResource(post.postImg)
            // holder.textViewDescription.text = post.description
            // holder.textViewTimestamp.text = post.timestamp
        }

        override fun getItemCount(): Int {
            // Return the number of items in the data set
            return posts.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // Define the views in the item_feed layout here
            // val textViewUsername: TextView = itemView.findViewById(R.id.text_view_username)
            // val imageViewProfilePic: ImageView = itemView.findViewById(R.id.image_view_profile_pic)
            // val imageViewPostImg: ImageView = itemView.findViewById(R.id.image_view_post_img)
            // val textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
            // val textViewTimestamp: TextView = itemView.findViewById(R.id.text_view_timestamp)
        }
    }
}
