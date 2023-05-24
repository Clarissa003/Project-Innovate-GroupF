package com.michael.potcastplant

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.potcastplant.databinding.ActivityPlantsDashboardBinding

class PlantsDashboardActivity : Fragment() {

    private lateinit var binding : ActivityPlantsDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityPlantsDashboardBinding.inflate(inflater, container, false)

        binding.recyclerViewPlants.layoutManager = LinearLayoutManager(requireContext())

        val plants = arrayOf(
            PlantDashboardClass("Bay Leaves", R.drawable.plants),
            PlantDashboardClass("Rose Flowers", R.drawable.plants),
            PlantDashboardClass("Mahogany", R.drawable.plants),
            PlantDashboardClass("Normal Flowers", R.drawable.plants)
        )

        val adapterPlants = PlantsAdapter(plants)
        binding.recyclerViewPlants.adapter = adapterPlants

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddPlant.setOnClickListener {
            val intent = Intent(context, AddPlantsActivity::class.java)
            startActivity(intent)
        }
    }


}