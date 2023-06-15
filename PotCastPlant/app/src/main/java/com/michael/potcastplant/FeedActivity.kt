package com.michael.potcastplant

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityFeedBinding
import com.michael.potcastplant.databinding.ItemFeedsBinding

class FeedActivity : Fragment() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.recyclerViewFeed.layoutManager = LinearLayoutManager(requireContext())

     /*   val posts = arrayOf(
            FeedsPostClass("Michael", R.drawable.baseline_person_24, R.drawable.plants, "This is a very nice flower, I would love to have some soon", "1 day ago"),
            FeedsPostClass("Naga", R.drawable.baseline_person_24, R.drawable.ic_launcher_background, "Nothing to post here... haha, you wish", "3 days ago"),
            FeedsPostClass("Michael", R.drawable.baseline_person_24, R.drawable.potcast_logo, "Bla bla bla, our logo", "4 min ago")
        )*/

        // Retrieve all posts from the database
        firestore.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                val posts = mutableListOf<FeedsPostClass>()

                for (document in result) {
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val description = document.getString("description") ?: ""
                    val timestamp = document.getString("timestamp") ?: ""
                    val uid = document.getString("uid") ?: ""


                    firestore



                    val post = FeedsPostClass(postImage, description, timestamp, uid)
                    posts.add(post)
                }

                val adapter = FeedsAdapter(posts)
                binding.recyclerViewFeed.adapter = adapter
            }
            .addOnFailureListener { e: Exception ->
                println("Error retrieving posts: ${e.message}")
            }


        


        binding.floatingButtonAddPost.setOnClickListener {
            val intent = Intent(this.requireContext(), AddPostActivity::class.java)
            startActivity(intent)
        }
    }

    private inner class FeedsAdapter(private val posts: MutableList<FeedsPostClass>) :
        RecyclerView.Adapter<FeedsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feeds, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val post = posts[position]
            holder.bind(post)
        }

        override fun getItemCount(): Int {
            return posts.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val binding: ItemFeedsBinding = ItemFeedsBinding.bind(itemView)

            fun bind(post: FeedsPostClass) {
                binding.textViewUsername.text = post.username
                binding.imageViewProfilePic.setImageResource(post.profilePic)
                binding.imageViewPostImg.setImageResource(post.postImage)
                binding.textViewDescription.text = post.description
                binding.textViewTimestamp.text = post.timestamp
            }
        }
    }


}
