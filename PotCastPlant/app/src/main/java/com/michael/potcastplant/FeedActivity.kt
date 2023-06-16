package com.michael.potcastplant

import android.content.ClipDescription
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
    val posts = mutableListOf<FeedsPostClass>()

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

                for (document in result) {

                    val imageUrl = document.getString("imageUrl") ?: ""
                    val description = document.getString("description") ?: ""
                    val timestamp = document.getString("timestamp") ?: ""
                    val uid = document.getString("uid") ?: ""

<<<<<<< Updated upstream
                    fetchFirstNameAndLastName(imageUrl, description, timestamp, uid)
=======

                    firestore



                     val post = FeedsPostClass(postImage, description, timestamp, uid)
                     posts.add(post)
>>>>>>> Stashed changes
                }

                val adapter = FeedsAdapter(posts)
                binding.recyclerViewFeed.adapter = adapter
            }
            .addOnFailureListener { e: Exception ->
                println("Error retrieving posts: ${e.message}")
            }

        binding.floatingButtonAddPost.setOnClickListener {
            val intent = Intent(requireContext(), AddPostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchFirstNameAndLastName(imageUrl : String, description: String, timestamp : String, uid : String) {

        firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { result ->
                val firstName = result.getString("firstName") ?: ""
                val profileUrl = result.getString("profileUrl") ?: ""

                val post = FeedsPostClass(firstName, profileUrl, imageUrl, description, timestamp)
                posts.add(post)
            }
    }


}
