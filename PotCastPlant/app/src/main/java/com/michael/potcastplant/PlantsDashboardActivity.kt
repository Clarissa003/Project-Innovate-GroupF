package com.michael.potcastplant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityPlantsDashboardBinding

class PlantsDashboardActivity : Fragment() {

    private lateinit var binding : ActivityPlantsDashboardBinding

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var adapter: PlantsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityPlantsDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recyclerViewPlants.layoutManager = LinearLayoutManager(requireContext())
        val plants = mutableListOf<PlantDashboardClass>()


        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val sharedPreferences = requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", null) ?: ""


        if (uid.isNotEmpty()) {
            val userDocument = firestore.collection("users").document(uid)
            userDocument.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val potIds = documentSnapshot.get("potId") as? ArrayList<Long>
                    // Call a function to retrieve pot details using the potIds
                    Log.d("retrieving POT", "retrieving the pot")

                    if(potIds != null && potIds.isNotEmpty()) {
                        retrievePotDetails(potIds)
                    } else {
                        binding.recyclerViewPlants.visibility = View.GONE
                        binding.textViewNoPlantsYet.visibility = View.VISIBLE
                    }

                } else {
                    // Handle document not found or other errors
                }
            }.addOnFailureListener { exception ->
                // Handle exceptions
            }
        }
//        Log.d("Setting Adapter", "retrieving the pot")
        adapter = PlantsAdapter(plants)
        binding.recyclerViewPlants.adapter = adapter

        binding.buttonAddPlant.setOnClickListener {
            val intent = Intent(context, AddPlantsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun retrievePotDetails(potIds: ArrayList<Long>) {
        for (potId in potIds) {
           val id = potId.toString()
            val potDocument = firestore.collection("pots").document(id)
            potDocument.get().addOnSuccessListener { potDocument ->
                if (potDocument.exists()) {
                    val plantId = potDocument.getLong("plantId")
                    if (plantId != null) {
                        // Call a function to retrieve plant details using the plantId
                        Log.d("retrieving Plant", "retrieving the plant")
                        retrievePlantDetails(plantId)
                    }
                } else {
                    // Handle pot document not found or other errors
                }
            }.addOnFailureListener { exception ->
                // Handle exceptions
            }
        }
    }


    private fun retrievePlantDetails(plantId: Long) {
        val id = plantId.toString()
        val plantDocumentRef = FirebaseFirestore.getInstance().collection("plants").document(id)
        plantDocumentRef.get().addOnSuccessListener { plantDocument ->
            if (plantDocument.exists()) {

                Log.d("Getting details", "Getting the details")
                var title = plantDocument.getString("name")
                val plantImageUrl = plantDocument.getString("image_url")
                // Add the retrieved plant details to your RecyclerView adapter or data structure

                if (title != null && plantImageUrl != null) {
                    val plantDetails = PlantDashboardClass(
                       title, plantImageUrl
                    )
                    // Add the plant details to a list in your RecyclerView adapter
                    adapter.addPlant(plantDetails)
                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged()
                }
            } else {
                // Handle plant document not found or other errors
                Log.d("error", "document not found")
            }
        }.addOnFailureListener { exception ->
            // Handle exceptions
        }
    }


}