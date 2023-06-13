package com.michael.potcastplant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.michael.potcastplant.databinding.ActivityEditProfileBinding
import java.io.ByteArrayOutputStream


class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var imageBytes: ByteArray
    private val sharedPreferences: SharedPreferences by lazy { getSharedPreferences("myPref", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        displayInfo()

        binding.buttonUpload.setOnClickListener {
            openGallery()
        }

        binding.buttonConfirm.setOnClickListener {
            perfomUpload()
        }

        binding.buttonUpdateNames.setOnClickListener {
            updateNameInDb()
        }
    }

    private fun displayInfo() {
        val uid = sharedPreferences.getString("uid", null) ?: ""
        val document = firestore.collection("users").document(uid)
        document.get().addOnSuccessListener { document ->
            if(document.exists()) {
                val data = document.data
                binding.editTextFirstName.setText(data?.get("firstname").toString())
                binding.editTextLastName.setText(data?.get("lastname").toString())
            }
        }
    }

    private fun updateNameInDb() {
        val uid = sharedPreferences.getString("uid", "") ?: ""
        val firstName = binding.editTextFirstName.text.toString()
        val lastName = binding.editTextLastName.text.toString()

        val document = firestore.collection("users").document(uid)

        val updates = hashMapOf<String, Any>(
            "firstname" to firstName,
            "lastname" to lastName
        )

        document.update(updates).addOnSuccessListener {
            Toast
                .makeText(this, "Details Updated Successfully", Toast.LENGTH_SHORT)
                .show()
        }.addOnFailureListener {
            Toast
                .makeText(this, "Update Failed", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun perfomUpload() {
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

                    // Set the image URL under the user's document in Firestore
                    val userRef = firestore.collection("users").document(uid)
                    userRef.update("profileUrl", imageUrl)
                        .addOnSuccessListener {
                            Toast
                                .makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT)
                                .show()
                            binding.buttonConfirm.isEnabled = false
                            binding.imageViewPreview.visibility = android.view.View.GONE
                        }
                        .addOnFailureListener { e ->
                            Toast
                                .makeText(this, "FAILED to Update", Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    Toast
                        .makeText(this, "FAILED to Update", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

            binding.imageViewPreview.visibility = android.view.View.VISIBLE
            binding.imageViewPreview.setImageBitmap(bitmap)

            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            imageBytes = outputStream.toByteArray()

            binding.buttonUpload.isEnabled = true
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

}