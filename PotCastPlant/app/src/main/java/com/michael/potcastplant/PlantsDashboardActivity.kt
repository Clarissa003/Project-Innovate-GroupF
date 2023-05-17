package com.michael.potcastplant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michael.potcastplant.databinding.ActivityPlantsDashboardBinding

class AllPlantsActivity : Fragment() {

    private lateinit var binding : ActivityPlantsDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityPlantsDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

}