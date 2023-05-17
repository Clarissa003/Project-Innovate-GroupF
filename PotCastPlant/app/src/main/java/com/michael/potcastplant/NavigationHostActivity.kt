package com.michael.potcastplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.michael.potcastplant.databinding.ActivityNavigationHostBinding

class NavigationHostActivity : AppCompatActivity(), OnItemSelectedListener {

    private lateinit var binding: ActivityNavigationHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.navigation_myplant -> {
            onMyPlantClicked()
        }
        R.id.navigation_notification -> {
            onNotificationClicked()
        }
        R.id.navigation_profile -> {
            onProfileClicked()
        }
        R.id.navigation_feed -> {
            onFeedClicked()
        }
        else -> false
    }

    private fun onFeedClicked(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_content, FeedActivity())
        }
        return true
    }

    private fun onProfileClicked(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_content, ProfileActivity())
        }
        return true
    }

    private fun onNotificationClicked(): Boolean{
        supportFragmentManager.commit {
            replace(R.id.fragment_content, NotificationActivity())
        }
        return true
    }

    private fun onMyPlantClicked(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_content, AllPlantsActivity())
        }
        return true
    }
}




