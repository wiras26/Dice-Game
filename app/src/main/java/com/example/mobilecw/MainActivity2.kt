package com.example.mobilecw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity2 : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var btn: Button
    private var maxScore: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.target_score)

        editText = findViewById(R.id.inputScore)
        btn = findViewById(R.id.final_popup_btn2)

        btn.setOnClickListener {
            maxScore = editText.text.toString().toInt() // Convert to Int value

            // Create the intent to start the next activity
            val intent = Intent(this, NewGameActivity::class.java)

            // Add the maxScore value as an extra to the intent
            intent.putExtra("maxScore", maxScore)

            // Start the next activity
            startActivity(intent)
        }

        if (savedInstanceState != null) {
            // Restore the previous value of the EditText
            editText.setText(savedInstanceState.getString("editTextValue"))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current value of the EditText
        outState.putString("editTextValue", editText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore the previous value of the EditText
        editText.setText(savedInstanceState.getString("editTextValue"))
    }
}
