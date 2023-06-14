package com.michael.potcastplant

import android.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityAddPlantsBinding

class AddPlantsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlantsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val sharedPreferences: SharedPreferences by lazy { getSharedPreferences("myPref", Context.MODE_PRIVATE) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.buttonAddPlant.setOnClickListener {

        }

        val plants = mutableListOf<Plant>()

        // Retrieve all plants from the database
        firestore.collection("plants")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val allPlants = Plant(
                        document.getLong("id") ?: 0,
                        document.getString("name") ?: "",
                        document.getString("description") ?: "",
                        document.getLong("humidityMax") ?: 0,
                        document.getLong("humidityMin") ?: 0,
                        document.getLong("sunlightMax") ?: 0,
                        document.getLong("sunlightMin") ?: 0,
                        document.getLong("moistureMax") ?: 0,
                        document.getLong("moistureMin") ?: 0,
                        document.getString("image_url") ?: ""
                    )
                    plants.add(allPlants)
                }

                val adapter = ArrayAdapter(
                    this,
                    R.layout.simple_spinner_dropdown_item,
                    plants.map { it.plant_name }
                )
                binding.spinnerPlants.adapter = adapter
            }
            .addOnFailureListener { e: Exception ->
                println("Error retrieving plants: ${e.message}")
            }

        binding.buttonSubmitRequest.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto: support@potcastplant.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Adding Plants")
            startActivity(intent)
        }

        binding.buttonAddPlant.setOnClickListener {
            val potId = binding.editTextPot.text.toString()
            val selectedPlant = binding.spinnerPlants.selectedItem as? Plant

            if (selectedPlant != null) {
                updatePlantInDb(potId, selectedPlant)
                println("Pot ID: $potId, Selected Plant: $selectedPlant")
            } else {
                println("Error: Invalid selected plant.")
            }
        }
    }

   /* private fun addPlantToDb(potId: String, selectedPlant: Plant) {
        // Operation to add plants to the database
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
*/
    private fun updatePlantInDb(potId: String, selectedPlant: Plant) {
       // val uid = auth.currentUser?.uid ?: ""
        val uid = sharedPreferences.getString("uid", null) ?: ""
        val document = firestore.collection("users").document(uid)
        val plantId = selectedPlant.plant_id

       Log.d("plantId", plantId.toString())
       Log.d("potId", potId.toString())
        val updates = hashMapOf<String, Any>(
            "potId" to potId
        )

        document.update(updates)
            .addOnSuccessListener {
                println("Name updated successfully in the database.")
            }
            .addOnFailureListener { e: Exception ->
                println("Error updating name in the database: ${e.message}")
            }

        val documentPot = firestore.collection("pots").document(potId)

        val updatePot = hashMapOf<String, Any>(
            "plantId" to plantId
        )

        documentPot.update(updatePot)
            .addOnSuccessListener {
                Toast.makeText(this, "pot updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e: Exception ->
                Toast.makeText(this, "pot failed to update", Toast.LENGTH_SHORT).show()
            }


    }
}
