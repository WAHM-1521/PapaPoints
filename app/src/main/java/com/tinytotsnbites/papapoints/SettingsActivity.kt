package com.tinytotsnbites.papapoints

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.*
import com.google.android.material.snackbar.Snackbar
import com.tinytotsnbites.papapoints.data.AppDatabase
import com.tinytotsnbites.papapoints.data.Child
import com.tinytotsnbites.papapoints.utilities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .getString("gender","") == "Male") {
            onActivitySetTheme(this, R.style.Theme_PapaPoints_Boy)
        } else {
            onActivitySetTheme(this, R.style.Theme_PapaPoints_Girl)
        }

        setContentView(R.layout.settings_activity)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            val mainScope = CoroutineScope(Dispatchers.Main)
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())

            //Editing Child Name
            val editChildName = findPreference<EditTextPreference>("edit_child_name_preference")
            lateinit var child: Child
            mainScope.launch(Dispatchers.IO) {
                if (editChildName != null) {
                    child = AppDatabase.getInstance(requireActivity()).childDao().getById(1)
                }
                withContext(Dispatchers.Main) {
                    editChildName?.text = child.name
                }
            }

            editChildName?.setOnPreferenceChangeListener { _, newValue ->
                val newName = newValue as String
                mainScope.launch(Dispatchers.IO) {
                    if (newName.isNotBlank())
                    {
                        if(newName != child.name)
                        {
                            val updatedChildDetails = Child(
                                childId = child.childId,
                                name = newName.trimStart().substring(0, 1)
                                    .uppercase(Locale.ROOT) + newName.trimStart()
                                    .substring(1),
                                dob = child.dob,
                                gender = child.gender
                            )
                            // Update the child name in the database
                            context?.let {
                                AppDatabase.getInstance(it)
                                    .childDao().update(updatedChildDetails)
                            }
                            //Update the child_detail_updated_boolean on sharedPref
                            context?.getSharedPreferences("prefs",Context.MODE_PRIVATE)
                                ?.edit()
                                ?.putBoolean("child_name_updated",true)
                                ?.apply()

                            view?.let {
                                Snackbar.make(
                                    it,
                                    R.string.child_pref_update_snack_message,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        view?.let {
                            Snackbar.make(
                                it,
                                R.string.child_pref_empty_snack_message,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                true
            }

            //Turn App Sound On/Off
            val soundPreference = findPreference<SwitchPreference>("sound_preference")
            soundPreference?.setOnPreferenceChangeListener { _, newValue ->
                val soundOn = newValue as Boolean
                sharedPreferences.edit().putBoolean("sound_preference", soundOn).apply()
                LogHelper(this).i("sound is ON $soundOn")
                true
            }

            //Turn App Notification Reminder
            val notificationPreference =
                findPreference<SwitchPreference>("notification_preference")
            notificationPreference?.setOnPreferenceChangeListener { _, newValue ->
                val notificationOn = newValue as Boolean
                sharedPreferences.edit().putBoolean("notification_preference", notificationOn)
                    .apply()
                LogHelper(this).i("Notification set is $newValue")
                if(!notificationOn) {
                    context?.let { cancelNotification(it) }
                } else {
                    context?.let { scheduleNotification(it) }
                }
                true
            }

            //Feedback
            val feedbackPreference = findPreference<Preference>("feedback_preference")
            feedbackPreference?.setOnPreferenceClickListener {
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(TO_EMAIL_ID))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                    resources.getString(R.string.email_subject))
                LogHelper(this).i("feedback is ON $emailIntent")

                val pm = context?.packageManager
                if (pm?.let { it1 -> emailIntent.resolveActivity(it1) } != null) {
                    startActivity(emailIntent)
                    LogHelper(this).d("feedback activity set $emailIntent")
                } else {
                    view?.let {
                        LogHelper(this).w("No email client found in the device")
                        Snackbar.make(it, R.string.email_snack_message, Snackbar.LENGTH_LONG).show()
                    }
                }
                true
            }

            //Rate Us
            val rateUsPreference = findPreference<Preference>("rate_us_preference")
            rateUsPreference?.setOnPreferenceClickListener {
                val appPackageName = APP_NAME
                val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("$PLAY_STORE_URL=$appPackageName")
                    setPackage(PLAY_STORE)
                }
                startActivity(playStoreIntent)
                true
            }

            //About Us
            val aboutUsPreference = findPreference<Preference>("about_us_preference")
            aboutUsPreference?.setOnPreferenceClickListener {
                val builder = view?.let { it1 -> AlertDialog.Builder(it1.context) }
                builder?.create()
                builder?.setTitle(R.string.about_us_title)
                val versionNumber = BuildConfig.VERSION_NAME
                builder?.setMessage("Version: $versionNumber")
                builder?.setPositiveButton(R.string.about_us_dialog_button) { _, _ ->
                }
                builder?.show()
                true
            }

            //Logout
            val logoutPreference = findPreference<Preference>("logout_preference")
            logoutPreference?.setOnPreferenceClickListener {
                val builder = view?.let { it1 -> AlertDialog.Builder(it1.context) }
                builder?.setTitle(R.string.logout_title)
                builder?.setMessage(R.string.logout_preference_message)
                builder?.setPositiveButton(R.string.yes) { _, _ ->
                    //clearing Notifications
                    val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(NOTIFICATION_ID)
                    //clearing data for Preference Manager
                    val preferenceManagerEditor = PreferenceManager.
                        getDefaultSharedPreferences(requireContext()).edit()
                    preferenceManagerEditor.clear()
                    preferenceManagerEditor.apply()
                    //clearing SharedPreferences for gender
                    val sharedPref = requireContext()
                        .getSharedPreferences("prefs",Context.MODE_PRIVATE)
                    val sharedPrefEditor = sharedPref.edit()
                    sharedPrefEditor.clear()
                    sharedPrefEditor.apply()
                    LogHelper(this)
                        .i("shared pref gender ${sharedPref.getString("Gender","")}")

                    //clearing Databases
                    mainScope.launch(Dispatchers.IO) {
                        val database = AppDatabase.getInstance(requireActivity())
                        database.childDao().deleteAll()
                        database.ratingDao().deleteAll()
                        database.taskDao().deleteUserDefinedTasks()
                        database.taskDao().enableAll()
                        withContext(Dispatchers.Main)
                        {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            LogHelper(this).d("finishing Points & Task ")
                        }
                    }
                }
                builder?.setNegativeButton("No") { _, _ -> }
                builder?.show()
                true
            }
        }
    }
}