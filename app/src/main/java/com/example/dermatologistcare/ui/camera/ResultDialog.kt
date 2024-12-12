package com.example.dermatologistcare.ui.camera

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.dermatologistcare.R

class ResultDialog : DialogFragment() {

    private lateinit var resultText: String

    companion object {
        private const val ARG_RESULT = "prediction_result"

        // Create a new instance of the dialog with the prediction result
        fun newInstance(predictionResult: String): ResultDialog {
            val dialog = ResultDialog()
            val args = Bundle()
            args.putString(ARG_RESULT, predictionResult)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false)

        resultText = arguments?.getString(ARG_RESULT, "No result") ?: "No result"

        val resultTextView: TextView = view.findViewById(R.id.resultTextView)
        resultTextView.text = resultText

        val btnBack: Button = view.findViewById(R.id.btnBack)
        val btnTrack: Button = view.findViewById(R.id.btnTrack)

        btnBack.setOnClickListener {
            dismiss()
        }

        btnTrack.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("DermaCarePrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean("isTracking", true)
                apply()
            }
            Toast.makeText(context, "Tracking disease", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
