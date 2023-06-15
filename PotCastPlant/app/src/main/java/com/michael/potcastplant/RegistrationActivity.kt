package com.michael.potcastplant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    //override will create your page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.btnRegister.setOnClickListener {
            performRegistration()
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegistration() {
        binding.btnRegister.isEnabled = false

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val rePassword = binding.etPasswordRepeat.text.toString()
        val profileUrl = R.drawable.baseline_person_24.toString()
        val potIdArray: ArrayList<Int> = ArrayList()

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            Toast.makeText(this, "Your fields cannot be Empty, Ensure to Complete all fields", Toast.LENGTH_SHORT).show()
            binding.btnRegister.isEnabled = true
            return //TO_DO Make this a Snack Bar later on.
        }

        if(password == rePassword) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() {task ->
                    if(task.isSuccessful) {
                        val user = auth.currentUser
                        val uid = user?.uid

                        //Creating a document with UID
                        val userDocument = firestore.collection("users").document(uid!!)
                        val userData = hashMapOf(
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "email" to email,
                            "profileUrl" to profileUrl,
                            "potId" to potIdArray
                        )

                        userDocument.set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener{
                                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                }

        }else {
            Toast.makeText(this, "Passwords do not match ", Toast.LENGTH_SHORT).show()
            binding.btnRegister.isEnabled = true
            return //TO_DO Make this a Snack Bar later on.
        }
        binding.btnRegister.isEnabled = true
    }
}