package com.michael.potcastplant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.potcastplant.databinding.ActivityFeedBinding

class FeedActivity : Fragment() {

    private lateinit var binding : ActivityFeedBinding

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

}