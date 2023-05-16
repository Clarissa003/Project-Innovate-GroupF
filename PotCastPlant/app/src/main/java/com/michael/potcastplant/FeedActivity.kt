package com.michael.potcastplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michael.potcastplant.databinding.ActivityFeedBinding

class FeedActivity : Fragment() {

    private lateinit var binding : ActivityFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

}