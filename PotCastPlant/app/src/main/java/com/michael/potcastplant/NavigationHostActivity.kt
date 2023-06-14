package com.michael.potcastplant

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.edit
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.michael.potcastplant.databinding.ActivityNavigationHostBinding

class NavigationHostActivity : AppCompatActivity(), OnItemSelectedListener {

    private lateinit var binding: ActivityNavigationHostBinding
    private val myPreferences: SharedPreferences by lazy {
        getSharedPreferences("myPref", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_logout) {
            myPreferences.edit {
                clear()
                apply()
            }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        if(item.itemId == R.id.menu_feedback) {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto: support@potcastplant.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Adding Plants")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
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
            replace(R.id.fragment_content, PlantsDashboardActivity())
        }
        return true
    }
}




