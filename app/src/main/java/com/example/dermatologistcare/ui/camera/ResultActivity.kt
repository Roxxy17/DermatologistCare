package com.example.dermatologistcare.ui.camera

import SkinDiseaseDetectionHelper
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dermatologistcare.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.support.image.TensorImage
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ResultActivity : AppCompatActivity() {
    private lateinit var imageResult: ImageView
    private lateinit var btnSave: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var resultText: TextView

    private lateinit var mlHelper: SkinDiseaseDetectionHelper

    companion object {
        private const val TAG = "ResultActivity" // Tag untuk logging
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        supportActionBar?.hide()

        // Initialize views
        imageResult = findViewById(R.id.imageResult)
        btnSave = findViewById(R.id.btnSave)
        progressBar = findViewById(R.id.progressBar)
        resultText = findViewById(R.id.resultText)

        resultText.visibility = View.GONE
        // Initialize ML helper
        mlHelper = SkinDiseaseDetectionHelper(this)
        mlHelper.initModel()

        // Get the image URI from intent
        val imageUriString = intent.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)

            // Display the image
            imageResult.setImageURI(imageUri)

            // Process the image with ML
            processImageWithML(imageUri)

            // Save button functionality
            btnSave.setOnClickListener {
                progressBar.visibility = View.VISIBLE

                lifecycleScope.launch {
                    val result = saveImage(imageUri)
                    progressBar.visibility = View.GONE
                    if (result) {
                        val predictionResult = resultText.text.toString() // Get the prediction result text
                        showResultDialog(predictionResult)
                    } else {
                        Toast.makeText(this@ResultActivity, "Failed to save image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showResultDialog(predictionResult: String) {
        val dialog = ResultDialog.newInstance(predictionResult)
        dialog.show(supportFragmentManager, "ResultDialog")
    }
    private fun processImageWithML(imageUri: Uri) {
        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            val prediction = withContext(Dispatchers.IO) {
                try {
                    Log.d(TAG, "Converting image to ByteBuffer...")
                    // Convert image to ByteBuffer
                    val inputBuffer = convertImageToByteBuffer(imageUri)
                    Log.d(TAG, "Image conversion successful")

                    // Run inference using helper
                    Log.d(TAG, "Running ML model inference...")
                    val result = mlHelper.predict(inputBuffer)
                    Log.d(TAG, "Model inference successful. Result: ${result?.joinToString(", ")}")
                    result
                } catch (e: Exception) {
                    Log.e(TAG, "Error during ML processing", e)
                    null
                }
            }

            // Display result
            if (prediction != null) {
                Log.d(TAG, "Raw Prediction Output: ${prediction.joinToString(", ")}")

                // Find the index of the highest probability
                val maxProbabilityIndex = prediction.indices.maxByOrNull { prediction[it] } ?: -1
                val sortedLabels = listOf(
                    "Actinic keratosis",
                    "Atopic Dermatitis",
                    "Benign keratosis",
                    "Dermatofibroma",
                    "Melanocytic nevus",
                    "Melanoma",
                    "Squamous cell carcinoma",
                    "Tinea Ringworm Candidiasis",
                    "Vascular lesion"
                )

                // Get the disease name based on the index of the highest probability
                val predictedDisease = if (maxProbabilityIndex >= 0) sortedLabels[maxProbabilityIndex] else "Unknown"

                // Update the result text with the prediction
                resultText.text = "Predicted Disease: $predictedDisease with probability: ${prediction[maxProbabilityIndex]}"
                Log.d(TAG, "Predicted disease: $predictedDisease with probability: ${prediction[maxProbabilityIndex]}")
            } else {
                resultText.text = "Prediction failed"
                Log.e(TAG, "Failed to predict the image")
            }
            progressBar.visibility = View.GONE
        }
    }

    private suspend fun saveImage(imageUri: Uri): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val sourceFile = File(imageUri.path ?: return@withContext false)
                if (!sourceFile.exists()) return@withContext false

                val destinationFile = File(
                    getExternalFilesDir(null),
                    "dermcare_${System.currentTimeMillis()}.jpg"
                )
                sourceFile.copyTo(destinationFile, overwrite = true)
                Log.d(TAG, "Image saved at: ${destinationFile.absolutePath}")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save image", e)
                false
            }
        }
    }

    private fun convertImageToByteBuffer(imageUri: Uri): ByteBuffer {
        val sourceFile = File(imageUri.path ?: throw IllegalArgumentException("Invalid URI"))
        val originalBitmap = android.graphics.BitmapFactory.decodeStream(FileInputStream(sourceFile))

        // Resize bitmap to 224x224 (model's expected input size)
        val resizedBitmap = android.graphics.Bitmap.createScaledBitmap(originalBitmap, 224, 224, true)

        // Create ByteBuffer with FLOAT32 type
        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3) // FLOAT32 = 4 bytes per value
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(224 * 224)
        resizedBitmap.getPixels(intValues, 0, 224, 0, 0, 224, 224)

        // Normalize and add to ByteBuffer
        for (pixelValue in intValues) {
            val r = ((pixelValue shr 16) and 0xFF) / 255.0f
            val g = ((pixelValue shr 8) and 0xFF) / 255.0f
            val b = (pixelValue and 0xFF) / 255.0f

            byteBuffer.putFloat(r)
            byteBuffer.putFloat(g)
            byteBuffer.putFloat(b)
        }

        Log.d(TAG, "ByteBuffer size: ${byteBuffer.capacity()}") // Verify size = 602112
        return byteBuffer
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Closing ML model")
        mlHelper.closeModel()
    }
}
