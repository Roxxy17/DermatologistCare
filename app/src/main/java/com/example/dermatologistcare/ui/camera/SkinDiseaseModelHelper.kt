import android.content.Context
import com.example.dermatologistcare.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer

class SkinDiseaseDetectionHelper(private val context: Context) {

    private var model: Model? = null

    // Initialize the model
    fun initModel() {
        model = Model.newInstance(context)
    }

    // Run inference
    fun predict(byteBuffer: ByteBuffer): FloatArray? {
        // Ensure model is initialized
        if (model == null) {
            throw IllegalStateException("Model is not initialized. Call initModel() first.")
        }

        // Create input tensor
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)


        // Run inference
        val outputs = model!!.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // Return the result as a FloatArray
        return outputFeature0.floatArray
    }

    // Close the model to release resources
    fun closeModel() {
        model?.close()
        model = null
    }
}
