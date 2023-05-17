package com.michael.potcastplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.michael.potcastplant.databinding.ActivityPlantInformationBinding

class PlantInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantInformationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlantInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}