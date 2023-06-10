package com.michael.potcastplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.michael.potcastplant.R.*
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import java.io.File
import java.io.FileOutputStream
import android.net.Uri

class AddPostActivity : AppCompatActivity() {

    private lateinit var etDescription: EditText
    private lateinit var imageView: ImageView
    private val PICK_IMAGE_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_add_post)

        //Find views by their IDs
        val btnUploadImage: Button = findViewById(id.btnUploadImage)
        val btnUpload: Button = findViewById(id.btnUpload)
        etDescription = findViewById(id.etDescription)
        val imageView: ImageView = findViewById(id.imageView)


        //Set click listener for upload img button
        btnUploadImage.setOnClickListener {
            // Launch img picker

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)

        }

        //Set click listener for upload button
        btnUpload.setOnClickListener {
            val description = etDescription.text.toString()
            val imageUri = getImageUri()
            // TODO: Implement logic
            if(imageUri != null){
                //Upload img and description
                uploadPost(imageUri, description)
            }

        }
    }

    private fun getImageUri(): Uri? {
        val drawable = imageView.drawable
        return if (drawable != null){
            val imageBitmap = drawable.toBitmap()
            val imageFile = createImageFile (imageBitmap)
            FileProvider.getUriForFile(this, "com.michael.potcastplant.fileprovider", imageFile)
        } else {
            null
        }
    }

    private fun createImageFile(bitmap: Bitmap): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(
            "image_${System.currentTimeMillis()}",
            ".jpg",
            storageDir /*directory*/
        )
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.close()
        return imageFile
    }

    private fun uploadPost (ImageUri: Uri, description: String) {
        // TODO: Implement Upload logic
        // Upload post (img and description) to database
    }
    override fun onActivityResult (requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            val imageUri = data.data
            // Set selected img to ImageView
            imageView.setImageURI(imageUri)
        }
    }
}