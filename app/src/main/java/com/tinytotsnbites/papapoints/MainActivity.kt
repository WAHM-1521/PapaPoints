package com.tinytotsnbites.papapoints

import android.content.Context
import android.content.Intent
import android.os.Build
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
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

    private lateinit var analytics: FirebaseAnalytics
    private var dateOfBirth = Date()
    private lateinit var dobEditText: EditText

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var keepSplashScreen = true
        val splashScreen = installSplashScreen()
        val layoutInflater = layoutInflater
        val view = layoutInflater.inflate(R.layout.splash_screen, null)
        val imageView = view.findViewById<ImageView>(R.id.splash_screen_image_view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Load the GIF file
            Glide.with(this)
                .load(R.drawable.ic_splash_screen)
                .into(imageView)

            LogHelper(this).d("build version is ${Build.VERSION.SDK_INT}")
        } else {
            // Load the app icon
            //imageView.setImageResource(R.mipmap.ic_launcher_foreground)
            LogHelper(this).d("build version is ${Build.VERSION_CODES.S}")
        }
        setContentView(view)
        splashScreen.setKeepOnScreenCondition {
            keepSplashScreen
        }

        analytics = Firebase.analytics
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN,null)
        // Uncomment the below line to see the splash screen for 5 seconds.
         Thread.sleep(3000)

        var childDetails : List<Child>
        mainScope.launch(Dispatchers.IO) {
            childDetails = AppDatabase.getInstance(applicationContext).childDao().getAll()
            withContext(Dispatchers.Main) {
                keepSplashScreen = false
                if(childDetails.isNotEmpty()) {
                    startPointsTaskActivity()
                } else {
                    showChildEntryScreen()
                }
            }
        }
    }

    private fun showChildEntryScreen() {
        setContentView(R.layout.activity_main)

        val nameEditText = findViewById<EditText>(R.id.edit_text_name)
        dobEditText = findViewById(R.id.edit_text_age)
        dobEditText.setOnClickListener {
            showDatePicker()
        }

        val radioGroupGender = findViewById<RadioGroup>(R.id.radio_group_gender)
        var selectedGender = ""
        radioGroupGender.setOnCheckedChangeListener { _, checkedId ->
             selectedGender = when (checkedId) {
                R.id.radio_button_male -> "Male"
                R.id.radio_button_female -> "Female"
                else -> ""
            }
        }

        val nextButton = findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            val childName = nameEditText.text.toString()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            when {
                childName.isBlank() -> {
                    showSnackbar(getString(R.string.snack_enter_name))
                }
                TextUtils.isEmpty(dobEditText.text)-> {
                    showSnackbar(getString(R.string.snack_enter_dob))
                    LogHelper(this).d("DOB is $dateOfBirth")
                }
                selectedGender == "" -> {
                    showSnackbar(getString(R.string.snack_enter_gender))
                    LogHelper(this).d("Gender is $selectedGender")

                }
                else -> {
                    LogHelper(this).d("Gender is $selectedGender and DOB is $dateOfBirth")
                    val child = Child(1, childName, dateOfBirth, selectedGender, Uri.EMPTY)
                    mainScope.launch(Dispatchers.IO) {
                        AppDatabase.getInstance(applicationContext).childDao().insert(child)
                        getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putString("gender",selectedGender).apply()
                        withContext(Dispatchers.Main) {
                            startPointsTaskActivity()
                        }
                    }
                    finish()
                }
            }
        }
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(
            findViewById(R.id.rootView), text,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showDatePicker() {
        val dateValidator : CalendarConstraints.DateValidator = DateValidatorPointBackward.now()
        val endCalendarConstraints = CalendarConstraints.Builder()
            .setValidator(dateValidator)
            .build()

        val datePickerDialog: MaterialDatePicker<Long> = MaterialDatePicker
            .Builder
            .datePicker()
            .setSelection(getCalendarInDateFormat().time)
            .setCalendarConstraints(endCalendarConstraints)
            .setTitleText(getString(R.string.select_date_of_birth))
            .setTheme(R.style.MaterialCalendarTheme)
            .build()

        datePickerDialog.show(supportFragmentManager, "DATE_PICKER")

        datePickerDialog.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val selectedDate  = it
            dateOfBirth.time = selectedDate
            dobEditText.setText(dateFormatter.format(selectedDate))
        }
    }

    private fun startPointsTaskActivity() {
        val intent = Intent(this, PointsAndTaskActivity::class.java)
        startActivity(intent)
        finish()
    }
}