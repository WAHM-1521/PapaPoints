package com.tinytotsnbites.papapoints

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.tinytotsnbites.papapoints.data.AppDatabase
import com.tinytotsnbites.papapoints.data.Child
import com.tinytotsnbites.papapoints.utilities.LogHelper
import com.tinytotsnbites.papapoints.workers.PapaPointsDatabaseWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class MainActivity : AppCompatActivity() {

    private val mainScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainScope.launch(Dispatchers.IO) {
            val lists = AppDatabase.getInstance(applicationContext).taskDao().getAll()
        }
        // Load the name from SharedPreferences
        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        var name = sharedPreferences.getString("name", "")

        if (name != null) {
            if (name.isNotEmpty()) {
                val intent = Intent(this, PointsAndTaskActivity::class.java)
                intent.putExtra("name", name)
                startActivity(intent)
                finish()
            } else {
                setContentView(R.layout.activity_main)

                val nameEditText = findViewById<EditText>(R.id.edit_text_name)
                val ageEditText = findViewById<EditText>(R.id.edit_text_age)
                val radioGroupGender = findViewById<RadioGroup>(R.id.radio_group_gender)
                radioGroupGender.setOnCheckedChangeListener { group, checkedId ->
                    val selectedGender: String = when(checkedId) {
                        R.id.radio_button_male -> "Male"
                        R.id.radio_button_female -> "Female"
                        else ->""
                    }

                val completeTaskButton = findViewById<Button>(R.id.button_complete_task)
                completeTaskButton.setOnClickListener {
                    name = nameEditText.text.toString()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    val age = ageEditText.text.toString()
                    val ageInt: Int = age.toInt()


                    if(name.isNullOrBlank()) {
                        Snackbar.make(
                            findViewById(R.id.rootView),
                            getString(R.string.snack_enter_name),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else if(age.isNullOrBlank()) {
                        Snackbar.make(findViewById(R.id.rootView), "Please enter your Child Age", Snackbar.LENGTH_SHORT).show()
                    } else {
                        LogHelper(this).d("Gender is $selectedGender")
                        val child = Child(1, name, ageInt, selectedGender)
                        mainScope.launch(Dispatchers.IO) {
                            AppDatabase.getInstance(applicationContext).childDao().insert(child)
                            withContext(Dispatchers.Main) {
                                startPointsTaskActivity()
                            }
                        }
                        // Save the name and age to SharedPreferences
                        val editor = sharedPreferences.edit()
                        editor.putString("name", name)
                        editor.putString("age", age)
                        editor.apply()
                        finish()
                    }
                }
                }
            }
        }
    }

    private fun startPointsTaskActivity() {
        val intent = Intent(this, PointsAndTaskActivity::class.java)
        startActivity(intent)
        finish()
    }
}