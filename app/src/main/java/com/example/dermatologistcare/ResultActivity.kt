package com.example.dermatologistcare

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import java.io.File

class ResultActivity : AppCompatActivity() {
    private lateinit var imageResult: ImageView
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Initialize views
        imageResult = findViewById(R.id.imageResult)
        btnSave = findViewById(R.id.btnSave)

        // Get the image URI from intent
        val imageUriString = intent.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)

            // Display the image
            imageResult.setImageURI(imageUri)

            // Save button functionality
            btnSave.setOnClickListener {
                try {
                    // Example of saving the image (you might want to customize this)
                    val sourceFile = imageUri.toFile()
                    val destinationFile = File(
                        getExternalFilesDir(null),
                        "dermcare_${System.currentTimeMillis()}.jpg"
                    )

                    // Copy the file
                    sourceFile.copyTo(destinationFile, overwrite = true)

                    Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show()
        }
    }
}