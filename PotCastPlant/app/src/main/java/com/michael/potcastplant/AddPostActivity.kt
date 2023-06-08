package com.michael.potcastplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.michael.potcastplant.R.*

class AddPostActivity : AppCompatActivity() {

    private lateinit var etDescription: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        //Find views by their IDs
        val btnUploadImage: Button = findViewById(id.btnUploadImage)
        val btnUpload: Button = findViewById(R.id.btnUpload)
        etDescription = findViewById(R.id.etDescription)
        /*val imageView: ImageView = findViewById(R.id.image_view_plant)*/

        //Set click listener for upload img button
        btnUploadImage.setOnClickListener {

        }

        //Set click listener for upload button
        btnUpload.setOnClickListener {
            val description = etDescription.text.toString()
        }
    }
}