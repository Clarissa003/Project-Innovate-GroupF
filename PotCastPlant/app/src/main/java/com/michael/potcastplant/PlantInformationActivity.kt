package com.michael.potcastplant

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.common.io.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityNotificationBinding
import com.michael.potcastplant.databinding.ActivityPlantInformationBinding
import io.grpc.internal.SharedResourceHolder.Resource

class PlantInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantInformationBinding
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private var threshold = 20
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("myPref", Context.MODE_PRIVATE) }
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var plantInfo: PlantDashboardClass
    private lateinit var potId : String
    private lateinit var plantName : String

    var entries = ArrayList<Entry>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlantInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        getPlantInformation()

        potId = plantInfo.potId.toString()
        val imageUrl = plantInfo.image_url
        plantName = plantInfo.title

        binding.nameText.setText(plantName)
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
                val waterlevel = documentSnapshot.getLong("waterlevel")
                val humidity = documentSnapshot.getLong("humidity")
                val automaticWatering = documentSnapshot.getString("automaticWatering")

                binding.textViewHumidity.setText(humidity.toString().plus("%"))
                binding.textViewWaterLevel.setText(waterlevel.toString().plus("%"))
                binding.textViewTemperature.setText(temperature.toString().plus("\u2103"))
                binding.textViewSoilMoisture.setText(moisture.toString().plus("%"))


                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = NotificationChannel(
                        channelId,
                        description,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.GREEN
                    notificationChannel.enableVibration(false)
                    notificationManager.createNotificationChannel(notificationChannel)

                    // Create the notification builder
                    builder = Notification.Builder(this, channelId)

                    // Set notification properties
                    builder.setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(
                            BitmapFactory.decodeResource(
                                this.resources,
                                R.drawable.ic_launcher_background
                            )
                        )

                    if (waterlevel != null && waterlevel.compareTo(threshold) < 0) {
                        var title = "Critical water level"
                        var text = "Your water tank is almost empty, please fill it up"
                        builder.setContentTitle(title)
                            .setContentText(text)
                        uploadNotificationToDatabase(title, text)
                    }

                    if (moisture != null && moisture.compareTo(threshold) < 0) {
                        var title = "Critical moisture Level"
                        var text = "Your plant needs water"
                        builder.setContentTitle(title)
                            .setContentText(text)
                        uploadNotificationToDatabase(title, text)
                    }

                    val notification = builder.build()

                }
                notificationManager.notify(1234, builder.build())


                if (automaticWatering != null) {
                    binding.switch1.isChecked = automaticWatering.toBoolean()
                }
            }
        }

        readSunlightDataFromFirestore()
    }

    private fun uploadNotificationToDatabase(title: String, text: String) {
        val notificationRef = firestore.collection("notifications")
        val uid = sharedPreferences.getString("uid", null) ?: ""
        val timestamp = com.google.firebase.Timestamp.now()

        val notification = hashMapOf<String, Any>(
            "title" to title,
            "message" to text,
            "timestamp" to timestamp,
            "potId" to potId,
            "userId" to uid,
            "plantName" to plantName
        )
        notificationRef.add(notification)
            .addOnSuccessListener {
                Log.d("success", "notification was uploaded successfully")
            }
            .addOnFailureListener{
                Log.d("fail", "notification was not uploaded")
            }

    }

    private fun setLineChart(sunlightArray: ArrayList<Float>) {
        val lineChart = binding.lineChart

        val xAxisLabels = arrayOf("24hrs", "20hrs", "16hrs", "12hrs", "8hrs", "4hrs", "0hr")

        for (i in 0 until 7) {
            val xValue = i
            val yValue = sunlightArray[i]
            val entry = Entry(xValue.toFloat(), yValue)
            entries.add(entry)
        }

        val dataSet = LineDataSet(entries, "Sunlight Values")
        dataSet.color = Color.WHITE
        dataSet.valueTextColor = Color.WHITE
        dataSet.setDrawValues(false) // Disable value labels on data points
        dataSet.setDrawCircles(false)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.lineWidth = 3F
        dataSet.valueTextSize = 12f // Set the text size for the dataset label
        dataSet.valueFormatter = DefaultValueFormatter(2) // Set the formatter for the dataset label
        lineChart.setExtraOffsets(0f, 0f, 0f, 10f) // Add bottom margin between chart and label


        val lineData = LineData(dataSet)

        lineChart.apply {
            description.isEnabled = false // Disable chart description
            setDrawGridBackground(false) // Disable grid background
            axisRight.isEnabled = false // Disable left` axis
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

        // Set custom labels for the x-axis
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
    }

    private fun getPlantInformation() {
        plantInfo = intent.getSerializableExtra("plant") as PlantDashboardClass
    }

    fun readSunlightDataFromFirestore() {
        firestore.collection("pots")
            .document(potId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                        val sunlightArray= document.get("sunlight") as ArrayList<Float>
                    setLineChart(sunlightArray)
                }
            }
            .addOnFailureListener {exception ->
                // Handle any errors that occur during data retrieval
            }
    }

}