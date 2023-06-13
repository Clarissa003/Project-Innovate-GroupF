package com.michael.potcastplant

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityAddPlantsBinding

class AddPlantsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddPlantsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

      /* var plants = arrayOf(
            Plant(1, "Bay Leaves", "A very nice Bay Leave",10, 10, 10, 10, 10, 10, ""),
            Plant(2, "Rose Flower", "A very nice Rose Flower",10, 10, 10, 10, 10, 10, ""),
            Plant(3, "Mahogany", "A very nice Mahogany",10, 10, 10, 10, 10, 10, ""),
            Plant(4, "Normal Flower", "A very nice Normal Flower",10, 10, 10, 10, 10, 10, ""),
        ) */


        var plants = mutableListOf<Plant>()

        //Retrieve all plants from the database
        firestore.collection("plants")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val allPlants = Plant(
                        document.data["id"] as Long,
                        document.data["name"] as String,
                        document.data["description"] as String,
                        document.data["humidityMax"] as Long,
                        document.data["humidityMin"] as Long,
                        document.data["sunlightMax"] as Long,
                        document.data["sunlightMin"] as Long,
                        document.data["moistureMax"] as Long,
                        document.data["moistureMin"] as Long,
                        document.data["image_url"] as String
                    )
                    plants.add(allPlants)

                 /*   //process data
                    plantData.get("id").toString()
                    val plantName = plantData ["name"] as Plant
                    val plantDescription = plantData ["description"] as String
                    val plantHumidityMax = plantData ["humidityMax"] as Int
                    val plantHumidityMin = plantData ["humidityMin"] as Int
                    val plantSunlightMax = plantData ["sunlightMax"] as Int
                    val plantSunlightMin = plantData ["sunlightMin"] as Int
                    val plantMoistureMax = plantData ["moistureMax"] as Int
                    val plantMoistureMin = plantData ["moistureMin"] as Int
                    val imageUrl = plantData ["imageUrl"] as String */


                }
            }
            .addOnFailureListener { e: Exception ->
                println ("Error retrieving plants: ${e.message}")
            }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, plants.map { it.plant_name })
        binding.spinnerPlants.adapter = adapter


        binding.buttonAddPlant.setOnClickListener {
            val potId = binding.editTextEmail.text.toString()
            val selectedPlant : Plant= binding.spinnerPlants.selectedItem as Plant

            // TODO: Handle adding the plant with potId and selectedPlant

            //Add plant to database

            addPlantToDb(potId, selectedPlant)
            println("Pot ID: $potId, Selected Plant: $selectedPlant")
        }


        binding.buttonSubmitRequest.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto: support@potcastplant.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Adding Plants")
            startActivity(intent)
        }
    }

    private fun addPlantToDb(potId: String, selectedPlant: Plant) {
        //operation to add plants to database
        val plantData = hashMapOf(
            "potId" to potId,
            "name" to selectedPlant.plant_name,
            "description" to selectedPlant.description,
            "humidityMax" to selectedPlant.humidity_max,
            "humidityMin" to selectedPlant.humidity_min,
            "sunlightMax" to selectedPlant.sunlight_max,
            "sunlightMin" to selectedPlant.sunlight_min,
            "moistureMax" to selectedPlant.moisture_max,
            "moistureMin" to selectedPlant.moisture_min,
            "image_url" to selectedPlant.image_url
        )

        firestore.collection("plants")
            .add(plantData)
            .addOnSuccessListener { documentReference ->
                println("Plant added successfully - Document ID: ${documentReference.id}")
            }
            .addOnFailureListener { e: Exception ->
                println("Error adding plant to the database: ${e.message}")
            }
        println("Adding Plant to Database - Pot ID: $potId, Selected Plant: $selectedPlant")

    }
}