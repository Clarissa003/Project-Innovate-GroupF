package com.michael.potcastplant

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.databinding.ActivityAddPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.michael.potcastplant.EditProfileActivity
import java.io.ByteArrayOutputStream


class AddPostActivity : AppCompatActivity() {

    private lateinit var etDescription: EditText
    private lateinit var imageView: ImageView
    private lateinit var binding: ActivityAddPostBinding
    private val sharedPreferences: SharedPreferences by lazy { getSharedPreferences("myPref", MODE_PRIVATE) }
    private lateinit var storageRef: StorageReference
    private lateinit var imageBytes: ByteArray
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = sharedPreferences.getString("uid", null) ?: ""
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        // Find views by their IDs
        etDescription = binding.etDescription
        imageView = binding.imageView

        // Set click listener for upload img button
        binding.btnUploadImage.setOnClickListener {
            // Launch the image picker
            pickImageFromGallery()
        }

        // Set click listener for upload button
        binding.btnUpload.setOnClickListener {
            val description = etDescription.text.toString()
            uploadPostToDatabase(description)
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EditProfileActivity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

            binding.imageView.visibility = View.VISIBLE
            binding.imageView.setImageBitmap(bitmap)

            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            imageBytes = outputStream.toByteArray()
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }


    private fun uploadPostToDatabase(description: String) {

        val uid = sharedPreferences.getString("uid", null) ?: ""
        val imageRef = storageRef.child("images/$uid.jpg")
        val uploadTask = imageRef.putBytes(imageBytes)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val imageUrl = task.result.toString()
                val timestamp = com.google.firebase.Timestamp.now()

                // Set the image URL under the user's document in Firestore
                val userRef = firestore.collection("posts")
                val uid = sharedPreferences.getString("uid", null) ?: ""

                val post = hashMapOf<String, Any>(
                    "description" to description,
                    "timestamp" to timestamp,
                    "imageUrl" to imageUrl,
                    "uid" to uid
                )
                userRef.add(post)
                    .addOnSuccessListener {
                        Toast
                            .makeText(this, "Post Uploaded Successfully", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this, NavigationHostActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast
                            .makeText(this, "FAILED to Upload", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                Toast
                    .makeText(this, "FAILED..... to Upload", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}

