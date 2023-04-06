package com.example.mobilecw

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val abtBtn= findViewById<Button>(R.id.aboutBtn)
        val newGameBtn = findViewById<Button>(R.id.newBtn)

        // Starts a new game
        newGameBtn.setOnClickListener {
            val intent= Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        // Pop up
        abtBtn.setOnClickListener {
            val popupBinding = layoutInflater.inflate(R.layout.about_pop_up,null)

            val myPopup = Dialog(this)
            myPopup.setContentView(popupBinding)

            myPopup.setCancelable(true)
            myPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myPopup.show()

            val okayBtn = popupBinding.findViewById<Button>(R.id.popup_btn)

            okayBtn.setOnClickListener {
                myPopup.dismiss()
            }
        }

    }
}