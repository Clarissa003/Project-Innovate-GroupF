package com.michael.potcastplant

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityLoginBinding
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val myPreferences: SharedPreferences by lazy {
        getSharedPreferences("myPref", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        val uid = myPreferences.getString("uid", null)
        if (uid != null) {
            val intent = Intent(this, NavigationHostActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.buttonLogin.setOnClickListener {
            performLogin()
        }

        binding.textViewForgotPassword.setOnClickListener {
            val intent = Intent(this, PasswordResetActivity::class.java)
            startActivity(intent)
        }

        binding.textViewRegisterRedirect.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {


        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields cannot be Empty", Toast.LENGTH_SHORT).show()
            return
        }

        binding.buttonLogin.isEnabled = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val uid = auth.currentUser?.uid

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    myPreferences.edit {
                        putString("uid", uid)
                    }
                    val intent = Intent(this, NavigationHostActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    binding.buttonLogin.isEnabled = true
                }
            }

        binding.buttonLogin.isEnabled = true
    }

}