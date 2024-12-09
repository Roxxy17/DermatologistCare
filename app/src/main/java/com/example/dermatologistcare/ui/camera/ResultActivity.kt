package com.example.dermatologistcare.ui.camera

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import com.example.dermatologistcare.R
import java.io.File

class ResultActivity : AppCompatActivity() {
    private lateinit var imageResult: ImageView
    private lateinit var btnSave: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        supportActionBar?.hide()

        // Initialize views
        imageResult = findViewById(R.id.imageResult)
        btnSave = findViewById(R.id.btnSave)
        progressBar = findViewById(R.id.progressBar)

        // Get the image URI from intent
        val imageUriString = intent.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)

            // Display the image
            imageResult.setImageURI(imageUri)

            // Save button functionality
            btnSave.setOnClickListener {
                // Show the ProgressBar (loading state)
                progressBar.visibility = View.VISIBLE

                // Simulate a delay for loading (fake loading)
                Handler().postDelayed({
                    try {
                        // Example of saving the image (you might want to customize this)
                        val sourceFile = imageUri.toFile()
                        val destinationFile = File(
                            getExternalFilesDir(null),
                            "dermcare_${System.currentTimeMillis()}.jpg"
                        )

                        // Copy the file (simulated)
                        sourceFile.copyTo(destinationFile, overwrite = true)

                        // Show the Bottom Sheet dialog
                        val bottomSheetFragment = ResultDialog()
                        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

                        // Show success message
                        Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        // Show error message
                        Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    } finally {
                        // Hide the ProgressBar once the task is complete
                        progressBar.visibility = View.GONE
                    }
                }, 2000) // 2 seconds delay for fake loading
            }
        } else {
            Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show()
        }
    }
}
