import android.content.Context
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.potcastplant.FeedsPostClass
import java.io.File
import java.io.FileOutputStream
import com.michael.potcastplant.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {

    private lateinit var etDescription: EditText
    private lateinit var imageView: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var binding: ActivityAddPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find views by their IDs
        etDescription = binding.etDescription
        imageView = binding.imageView

        // Set click listener for upload img button
        binding.btnUploadImage.setOnClickListener {
            // Launch the image picker
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Set click listener for upload button
        binding.btnUpload.setOnClickListener {
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
        // TODO: Implement database upload logic here
        // Use imageUri and description to upload the post to a database

        // Example code for uploading to Firebase Firestore Database
        val firestore = FirebaseFirestore.getInstance()
        val postsCollection = firestore.collection("posts")

        val post = hashMapOf(
            "imageUri" to imageUri.toString(),
            "description" to description
        )

        postsCollection.add(post)
            .addOnSuccessListener {
                // Post uploaded successfully
                showToast(applicationContext, "Post uploaded successfully")
                finish()
            }
            .addOnFailureListener {
                // Handle upload failure
                showToast(applicationContext, "Post failed to upload")
            }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
