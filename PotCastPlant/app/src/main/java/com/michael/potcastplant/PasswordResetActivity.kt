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

class PasswordResetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordResetBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var email = binding.editTextEmail.text.toString()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            Toast.makeText(LoginActivity.this, "Done sent", Toast.LENGTH_LONG).show();
        }
            .addOnFailureListener(new OnFailureListener () {
                Toast.makeText(LoginActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
            })
    }
}