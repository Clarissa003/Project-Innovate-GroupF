package com.michael.potcastplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityLoginBinding
import com.michael.potcastplant.databinding.ActivityPasswordResetBinding
import com.michael.potcastplant.databinding.ActivityProfileBinding

class PasswordResetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordResetBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var firestore: FirebaseFirestore}
/*
   override fun onCreate(savedInstanceState: Bundle?) {
=======
    private lateinit var firestore: FirebaseFirestore

    fun onCreateView(savedInstanceState: Bundle?) {
>>>>>>> 9de9e977513bab7e6e1945303874a9bb286b11b5
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val email = binding.editTextEmail.text.toString()
        Log.d("email", email)

        fun setOnClickListeners() {
            binding.buttonLogin.setOnClickListener {
                auth!!.sendPasswordResetEmail(email).addOnCompleteListener {
                    Toast.makeText(this, "Done sent", Toast.LENGTH_LONG).show();
                }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this@PasswordResetActivity,
                            "Error Occurred",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }
    }
}
*/