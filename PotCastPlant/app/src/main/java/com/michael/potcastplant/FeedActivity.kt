package com.michael.potcastplant

import android.content.ClipDescription
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.michael.potcastplant.databinding.ActivityFeedBinding
import com.michael.potcastplant.databinding.ItemFeedsBinding

class FeedActivity : Fragment() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var posts : MutableList<FeedsPostClass>


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

        fetchPosts { posts ->
            bind(posts)
        }

        binding.floatingButtonAddPost.setOnClickListener {
            val intent = Intent(requireContext(), AddPostActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchPosts(callback: (MutableList<FeedsPostClass>) -> Unit) {
        firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val posts = mutableListOf<FeedsPostClass>()
                val documentCount = result.size()

                for (document in result) {
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val description = document.getString("description") ?: ""
                    val timestamp = document.getTimestamp("timestamp")
                    val uid = document.getString("uid") ?: ""

                    firestore.collection("users").document(uid)
                        .get()
                        .addOnSuccessListener { userResult ->
                            val firstName = userResult.getString("firstName") ?: ""
                            val profileUrl = userResult.getString("profileUrl") ?: ""

                            val post = FeedsPostClass(firstName, profileUrl, imageUrl, description, timestamp!!)
                            posts.add(post)

                            // Check if all documents have been processed
                            if (posts.size == documentCount) {
                                callback(posts) // Invoke the callback with the retrieved posts
                            }
                        }
                }
            }
            .addOnFailureListener { e: Exception ->
                println("Error retrieving posts: ${e.message}")
            }
    }


    private fun bind(posts : MutableList<FeedsPostClass>) {
        val adapter = FeedsAdapter(posts)
        binding.recyclerViewFeed.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}
