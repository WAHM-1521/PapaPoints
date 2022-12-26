package com.tinytotsnbites.papapoints

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the name from SharedPreferences
        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        var name = sharedPreferences.getString("name", "")
        var age = sharedPreferences.getString("age", "")

        if (name != null) {
            if (name.isNotEmpty()) {
                // Name exists, start the RewardActivity
                //setContentView(R.layout.rewards)
                //val intent = Intent(this, Reward::class.java)
                val intent = Intent(this, PointsAndTaskActivity::class.java)

                intent.putExtra("name", name)
                intent.putExtra("age", age)
                startActivity(intent)
                finish()
            } else {
                setContentView(R.layout.activity_main)

                val nameEditText = findViewById<EditText>(R.id.edit_text_name)
                val ageEditText = findViewById<EditText>(R.id.edit_text_age)

                // Load the name and age from SharedPreferences
//                var age = sharedPreferences.getInt("age", 0)
//                nameEditText.setText(name)
                //ageEditText.setText(age.toString())

                val completeTaskButton = findViewById<Button>(R.id.button_complete_task)
                completeTaskButton.setOnClickListener {
                    name = nameEditText.text.toString()
                    age = ageEditText.text.toString()

                    if(name.isNullOrBlank()) {
                        Snackbar.make(findViewById(R.id.rootView), getString(R.string.snack_enter_name), Snackbar.LENGTH_SHORT).show()
                    } else {
                        // Save the name and age to SharedPreferences
                        val editor = sharedPreferences.edit()
                        editor.putString("name", name)
                        editor.putString("age", age)
                        editor.apply()

                        // You can now use the name and age values in your app
                        //val intent = Intent(this@MainActivity, Reward::class.java)
                        val intent = Intent(this, PointsAndTaskActivity::class.java)
                        intent.putExtra("name", name)
                        intent.putExtra("age", age)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}