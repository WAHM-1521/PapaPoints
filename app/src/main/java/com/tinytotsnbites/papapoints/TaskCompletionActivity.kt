package com.tinytotsnbites.papapoints

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class TaskCompletionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.rewards)

        val nextButton = findViewById<Button>(R.id.button_complete_task)
       nextButton.setOnClickListener{
            val intent = Intent(this, Reward::class.java)
            startActivity(intent)

       }
            // Handle add points action here

    }
}