package com.michael.potcastplant

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityProfileBinding


class ProfileActivity : Fragment() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("MyPref",Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", null) ?: ""

        val name = binding.nameText.text.toString()
        val profilePic = binding.profileImage
        val email = binding.emailText.text.toString()


        binding.editProfileButton.setOnClickListener {
            // Edit profile button after click

        }

        binding.logoutButton.setOnClickListener {
            val intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}