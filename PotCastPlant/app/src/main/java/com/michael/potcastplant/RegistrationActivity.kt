package com.michael.potcastplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.michael.potcastplant.databinding.ActivityRegistrationBinding

//creating your class for page
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    //override will create your page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        var etFirstName = findViewById(R.id.etFirstName) as EditText
        etFirstName.text.toString()

        var etLastName = findViewById(R.id.etLastName) as EditText
        etLastName.text.toString()

        var etEmail = findViewById(R.id.etEmail) as EditText
        etEmail.text.toString()

        var etPassword = findViewById(R.id.etPassword) as EditText
        etPassword.text.toString()

        var etPasswordRepeat = findViewById(R.id.etPasswordRepeat) as EditText
        etPasswordRepeat.text.toString()

        var btnRegister = findViewById(R.id.btnRegister) as Button
        btnRegister.setOnClickListener {
            //code
            Log.i("RegistrationActivity", "Information was sent!")
        }
    }
}