package com.michael.potcastplant

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.common.io.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityPlantInformationBinding
import io.grpc.internal.SharedResourceHolder.Resource

class PlantInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantInformationBinding

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var plantInfo: PlantDashboardClass
    private lateinit var potId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlantInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        getPlantInformation()

        potId = plantInfo.potId.toString()
        val imageUrl = plantInfo.image_url
        val title = plantInfo.title

        binding.nameText.setText(title)
        Glide.with(this)
            .load(imageUrl)
            .into(binding.plantImage)

        setToggleOnClickListener()
    }

    private fun setToggleOnClickListener() {
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            val document = firestore.collection("pots").document(potId)
            document.update("automaticWatering", isChecked.toString())
                .addOnSuccessListener {
                    if (isChecked.toString() == "true") {
                        Toast.makeText(
                            this,
                            "Your plant is now set to Automatic Watering",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (isChecked.toString() == "false") {
                        displayDialog()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Failed to update watering setting: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun displayDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Automatic watering is turned off, You'll have to water the plants yourself")
            .setNegativeButton("Close") {Dialog,_ ->
                Dialog.dismiss()
            }
    }


    override fun onResume() {
        super.onResume()

        val document = firestore.collection("pots").document(potId)
        document.get().addOnSuccessListener {documentSnapshot ->
            if(documentSnapshot.exists()) {
                val temperature = documentSnapshot.getLong("temperature")
                val moisture = documentSnapshot.getLong("moisture")
                val sunlight = documentSnapshot.getLong("sunlight")
                val waterlevel = documentSnapshot.getLong("waterlevel")
                val humidity = documentSnapshot.getLong("humidity")
                val automaticWatering = documentSnapshot.getString("automaticWatering")

                binding.textViewHumidity.setText(humidity.toString())
                binding.textViewWaterLevel.setText(waterlevel.toString().plus("%"))
                binding.textViewTemperature.setText(temperature.toString().plus("\u2103"))
                binding.textViewSoilMoisture.setText(moisture.toString())

                if (automaticWatering != null) {
                    binding.switch1.isChecked = automaticWatering.toBoolean()
                }
            }
        }

        setLineChart()
    }

    private fun setLineChart() {
        val lineChart = binding.lineChart

        val entries = listOf(
            Entry(0f, 20f),
            Entry(2f, 200f),
            Entry(3f, 400f),
            Entry(4f, 600f),
            Entry(5f, 800f),
            Entry(6f, 800f),
            Entry(7f, 1000f),
            Entry(8f, 800f),
        )

        val dataSet = LineDataSet(entries, "Sunlight Values")
        dataSet.color = Color.WHITE
        dataSet.setDrawValues(false) // Disable value labels on data points
        dataSet.setDrawCircles(false)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.lineWidth = 3F


        val lineData = LineData(dataSet)

        lineChart.apply {
            description.isEnabled = false // Disable chart description
            setDrawGridBackground(false) // Disable grid background
            axisRight.isEnabled = false // Disable left axis
            xAxis.position = XAxis.XAxisPosition.BOTTOM // X-axis position
            xAxis.granularity = 1f // X-axis label spacing
            axisLeft.setDrawGridLines(false) // Disable left axis grid lines
            xAxis.setDrawGridLines(false) // Disable x-axis grid lines
        }

        lineChart.xAxis.apply {
            axisLineColor = Color.WHITE // Set color for X axis line
            textColor = Color.WHITE // Set color for X axis text
            textSize = 14F
        }

        lineChart.axisLeft.apply {
            axisLineColor = Color.WHITE // Set color for X axis line
            textColor = Color.WHITE // Set color for X axis text
            textSize = 14F
        }

        lineChart.data = lineData
        lineChart.invalidate() // Refresh the chart
    }

    private fun getPlantInformation() {
        plantInfo = intent.getSerializableExtra("plant") as PlantDashboardClass
    }
}