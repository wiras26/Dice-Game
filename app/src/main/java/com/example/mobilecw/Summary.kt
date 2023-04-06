package com.example.mobilecw

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class Summary : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val playerScore = intent.getIntExtra("playerScore", 0)
        val computerScore = intent.getIntExtra("computerScore", 0)

        val textView=findViewById<TextView>(R.id.final_score)
        val button=findViewById<Button>(R.id.final_popup_btn)
        val result=findViewById<TextView>(R.id.final_message)

        textView.text="H:$playerScore/C:$computerScore"

        //Checks who won and the correct message is made visible
        if (playerScore > computerScore){
            result.visibility = View.VISIBLE
        }else{
            result.visibility = View.VISIBLE
            result.text= "You Lose!"
            result.setTextColor(Color.RED)
        }
        // Retry button functionality
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    //When android back button is pressed moves to the newGame activity
    override fun onBackPressed() {
        // Create an intent to navigate to the next activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        // Call super.onBackPressed() to let the default behavior execute as well
        super.onBackPressed()
    }
}