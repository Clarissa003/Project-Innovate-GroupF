package com.michael.potcastplant

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michael.potcastplant.databinding.ActivityProfileBinding


class ProfileActivity : Fragment() {

    private lateinit var binding: ActivityProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

//    var nameData = sharedPreferences.getString("key", "Your name")
//    val name = binding.nameText.text.toString()
//
//    var emailData = sharedPreferences.getString("key", "Your email")
//    val email = binding.emailText.text.toString()
//
//    val picture = binding.profileImage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE)

        binding.editProfileButton.setOnClickListener {
            // Edit profile button click
            val intent = Intent(this.context, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            val intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}