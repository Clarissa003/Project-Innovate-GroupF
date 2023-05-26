package com.michael.potcastplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.michael.potcastplant.databinding.ActivityAddPostBinding
import com.michael.potcastplant.databinding.ActivityRegistrationBinding

//creating your class for page
class RegistrationActivity : AppCompatActivity() {

    //override will create your page
    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etFirstName.text.toString()
        binding.etLastName.text.toString()
        binding.etEmail.text.toString()
        binding.etPassword.text.toString()
        binding.etPasswordRepeat.text.toString()

        var btnRegister = findViewById(R.id.btnRegister) as Button
        btnRegister.setOnClickListener {
            //code
            Log.i("RegistrationActivity", "Information was sent!")
        }
    }
}