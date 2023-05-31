package com.michael.potcastplant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.michael.potcastplant.databinding.ActivityRegistrationBinding

//creating your class for page
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    //override will create your page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etFirstName.text.toString()
        binding.etLastName.text.toString()
        binding.etEmail.text.toString()
        binding.etPassword.text.toString()
        binding.etPasswordRepeat.text.toString()


        binding.btnRegister.setOnClickListener {
            //code
            Log.i("RegistrationActivity", "Information was sent!")
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}