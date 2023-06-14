import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.michael.potcastplant.R
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.os.Environment
import com.michael.potcastplant.FeedsPostClass
import java.io.File
import java.io.FileOutputStream


class AddPostActivity : AppCompatActivity() {

    private lateinit var etDescription: EditText
    private lateinit var imageView: ImageView
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        // Find views by their IDs
        val btnUploadImage: Button = findViewById(R.id.btnUploadImage)
        val btnUpload: Button = findViewById(R.id.btnUpload)
        etDescription = findViewById(R.id.etDescription)

        // Set click listener for upload img button
        btnUploadImage.setOnClickListener {
            // Launch the image picker
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Set click listener for upload button
        btnUpload.setOnClickListener {
            val description = etDescription.text.toString()
            val imageUri = getImageUri()

            if (imageUri != null) {
                // Upload the post to the database
                uploadPostToDatabase(imageUri, description)
            }
        }
    }

    private fun getImageUri(): Uri? {
        val drawable = imageView.drawable
        return if (drawable != null) {
            // Convert the ImageView drawable to a Bitmap
            val imageBitmap = (drawable as BitmapDrawable).bitmap
            // Save the Bitmap to a file and return its Uri
            saveBitmapToFile(imageBitmap)
        } else {
            null
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): Uri {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(
            "image_${System.currentTimeMillis()}",
            ".jpg",
            storageDir                               /* directory */
        )
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.close()
        return Uri.fromFile(imageFile)
    }

    private fun uploadPostToDatabase(imageUri: Uri, description: String) {
        // TODO: Implement your database upload logic here
        // Use imageUri and description to upload the post to a database

        // Example code for uploading to Firebase Realtime Database
        /*  val database = FirebaseDatabase.getInstance()
        val postsRef = database.getReference("posts")

        val post = FeedsPostClass(imageUri.toString(), description)
        val postKey = postsRef.push().key
        if (postKey != null) {
            postsRef.child(postKey).setValue(post)
                .addOnSuccessListener {
                    // Post uploaded successfully
                    showToast("Post uploaded successfully")
                    finish()
                }
                .addOnFailureListener { e: Exception ->
                    // Handle upload failure
                    showToast("Post upload failed: ${e.message}")
                }
        } else {
            // Handle error when generating post key
            showToast("Failed to generate post key")
        }
    }

    private fun FeedsPostClass(username: String, profilePic: String): FeedsPostClass {

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }
}
