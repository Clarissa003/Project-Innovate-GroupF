package com.michael.potcastplant

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.michael.potcastplant.databinding.ActivityAddPlantsBinding

class AddPlantsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddPlantsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var plants = arrayOf(
            Plant(1, "Bay Leaves", "A very nice Bay Leave",10, 10, 10, 10, 10, 10, ""),
            Plant(2, "Rose Flower", "A very nice Rose Flower",10, 10, 10, 10, 10, 10, ""),
            Plant(3, "Mahogany", "A very nice Mahogany",10, 10, 10, 10, 10, 10, ""),
            Plant(4, "Normal Flower", "A very nice Normal Flower",10, 10, 10, 10, 10, 10, ""),
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, plants.map { it.plant_name })
        binding.spinnerPlants.adapter = adapter


        binding.buttonAddPlant.setOnClickListener {
            val potId = binding.editTextEmail.text.toString()
            val selectedPlant = binding.spinnerPlants.selectedItem.toString()

            // TODO: Handle adding the plant with potId and selectedPlant

            //Add plant to database

            addPlantToDb (potId, selectedPlant)
            println("Pot ID: $potId, Selected Plant: $selectedPlant")
        }


        binding.buttonSubmitRequest.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto: support@potcastplant.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Adding Plants")
            startActivity(intent)
        }
    }

    private fun addPlantToDb(potId: String, selectedPlant: String) {
        //operation to add plants to database

        println("Adding Plant to Database - Pot ID: $potId, Selected Plant: $selectedPlant")

    }
}