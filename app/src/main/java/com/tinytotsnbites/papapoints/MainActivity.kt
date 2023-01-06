package com.tinytotsnbites.papapoints

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.tinytotsnbites.papapoints.data.AppDatabase
import com.tinytotsnbites.papapoints.data.Child
import com.tinytotsnbites.papapoints.utilities.LogHelper
import com.tinytotsnbites.papapoints.utilities.getCalendarInDateFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var dateOfBirth = Date()
    private lateinit var dobEditText: EditText

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
                dobEditText = findViewById(R.id.edit_text_age)
                dobEditText.setOnClickListener {
                    showDatePicker()
                }

                val radioGroupGender = findViewById<RadioGroup>(R.id.radio_group_gender)
                radioGroupGender.setOnCheckedChangeListener { group, checkedId ->
                    val selectedGender: String = when (checkedId) {
                        R.id.radio_button_male -> "Male"
                        R.id.radio_button_female -> "Female"
                        else -> ""
                    }

                    val completeTaskButton = findViewById<Button>(R.id.button_complete_task)
                    completeTaskButton.setOnClickListener {
                        name = nameEditText.text.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                        if (name.isNullOrBlank()) {
                            Snackbar.make(
                                findViewById(R.id.rootView),
                                getString(R.string.snack_enter_name),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            LogHelper(this).d("Gender is $selectedGender")
                            LogHelper(this).d("DOB selected is $dateOfBirth")
                            val child = Child(1, name, dateOfBirth, selectedGender)
                            mainScope.launch(Dispatchers.IO) {
                                AppDatabase.getInstance(applicationContext).childDao()
                                    .insert(child)
                                withContext(Dispatchers.Main) {
                                    startPointsTaskActivity()
                                }
                            }
                            // Save the name and age to SharedPreferences
                            val editor = sharedPreferences.edit()
                            editor.putString("name", name)
                            editor.apply()
                            finish()
                        }
                    }
                }
            }
        }
    }


    private fun showDatePicker() {
        val calendarConstraints = CalendarConstraints.Builder()
            .setEnd(getCalendarInDateFormat().time)
            .build()

        val datePicker: MaterialDatePicker<Long> = MaterialDatePicker
            .Builder
            .datePicker()
            .setSelection(getCalendarInDateFormat().time)
            .setCalendarConstraints(calendarConstraints)
            .setTitleText("Select date of birth")
            .build()

            datePicker.show(supportFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val selectedDate  = it
            if ( selectedDate > getCalendarInDateFormat().time) {
                // Show an error message to the user
                Snackbar.make(
                    findViewById(R.id.rootView),
                    "Please select a valid date",
                    Snackbar.LENGTH_SHORT
                ).show()
                dobEditText.setText("")
            } else {
                dateOfBirth.time = selectedDate
                dobEditText.setText(sdf.format(selectedDate))
            }

        }
    }

    private fun startPointsTaskActivity() {
        val intent = Intent(this, PointsAndTaskActivity::class.java)
        startActivity(intent)
        finish()
    }
}