package com.tinytotsnbites.papapoints

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.tinytotsnbites.papapoints.data.*
import com.tinytotsnbites.papapoints.utilities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class PointsAndTaskActivity : AppCompatActivity(), ListAdapter.UpdatePointsList {

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private lateinit var adapter: ListAdapter
    private var calendar = Calendar.getInstance()

    enum class SwipeDirection {
        LEFT, RIGHT, NONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("gender","") == "Male") {
            onActivitySetTheme(this, R.style.Theme_PapaPoints_Boy)
        } else {
            onActivitySetTheme(this, R.style.Theme_PapaPoints_Girl)
        }
        setContentView(R.layout.points_and_task)

        setSupportActionBar(findViewById(R.id.toolbar))
        populateChildDetails()
        refreshTasks(SwipeDirection.NONE)
        manageCalendarButtons()
        manageAddingNewTask()
        if(PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("notification_preference", true)) {
            scheduleNotification(applicationContext)
        } else {
            cancelNotification(applicationContext)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_file, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun manageCalendarButtons()
    {
        val textView = findViewById<TextView>(R.id.DayText)  // Get a reference to the TextView in the ViewPager
        val dateFormat = SimpleDateFormat("dd MMM yy", Locale.ENGLISH)
        val prevButton = findViewById<ImageButton>(R.id.prev_day_button)
        val nextButton = findViewById<ImageButton>(R.id.next_day_button)
        val mediaPlayer = MediaPlayer.create(this, R.raw.button_click_calendar)

        prevButton.setOnClickListener {
            calendar.add(Calendar.DATE, -1)  // Reduce the date by one day
            val animationLeft = AnimationUtils.loadAnimation(this,R.anim.slide_in_from_left)
            textView.startAnimation(animationLeft)
            textView.text = dateFormat.format(getCalendarDateForMidnightTime(calendar))
            refreshTasks(SwipeDirection.LEFT)
            if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("sound_preference", true))
                mediaPlayer.start()
        }

        nextButton.setOnClickListener {
            calendar.add(Calendar.DATE, +1)  // Reduce the date by one day
            val animationRight = AnimationUtils.loadAnimation(this,R.anim.slide_in_from_right)
            textView.startAnimation(animationRight)
            textView.text = dateFormat.format(getCalendarDateForMidnightTime(calendar))
            refreshTasks(SwipeDirection.RIGHT)
            if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("sound_preference", true))
                mediaPlayer.start()
        }
    }

    override fun onResume() {
        super.onResume()
        if(getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .getBoolean("child_name_updated",true)) {
            populateChildDetails()
            //Update the child_detail_updated_boolean on sharedPref false
            getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("child_name_updated", false)
                .apply()
            LogHelper(this).d("on Resume child name updates: Points & Task ")
        }

        // Cancel the notification if the app is already in the foreground.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun populateChildDetails() {
        mainScope.launch(Dispatchers.IO) {
            val child = getChildDetails()
            withContext(Dispatchers.Main) {
                showTotalPoints()
                showChildDetails(child)
            }
        }
    }

    private fun showChildDetails(child:Child) {
        val dateFormat = SimpleDateFormat("dd MMM yy", Locale.ENGLISH)
        val dateText = findViewById<TextView>(R.id.DayText)
        dateText.text = dateFormat.format(Calendar.getInstance().time)

        val nameTextView = findViewById<TextView>(R.id.text_view_name)
        nameTextView.text = child.name

        val imageView = findViewById<ImageView>(R.id.imageView)
        val gender = child.gender
        LogHelper(this).d("Gender is $gender")
        if(gender == "Male") {
            imageView.setImageResource(R.drawable.ic_male)
        }
    }

    private fun showTotalPoints() {
        val totalPoints = findViewById<TextView>(R.id.text_view_total_points)
        val totalPointsToday = findViewById<TextView>(R.id.total_points_today)
        mainScope.launch(Dispatchers.IO) {
            val totalPointsValue =
                AppDatabase.getInstance(applicationContext).ratingDao().getTotalRating()
            val totalPointsTodayValue =
                AppDatabase.getInstance(applicationContext).ratingDao().getTotalRatingToday(
                    getCalendarInDateFormat())
            withContext(Dispatchers.Main) {
                totalPoints.text =
                    String.format(getString(R.string.total_points), totalPointsValue.toString())
                totalPointsToday.text =
                    String.format(getString(R.string.total_points_today),totalPointsTodayValue.toString())
            }
        }
    }

    private fun getChildDetails() : Child {
            return(AppDatabase.getInstance(applicationContext).childDao().getById(1))
    }

    private fun refreshTasks(swipe: SwipeDirection) {
        mainScope.launch(Dispatchers.IO) {
            //val lists = AppDatabase.getInstance(applicationContext).taskDao().getAll()
            val tasksWithRating = AppDatabase.getInstance(applicationContext).taskDao().getTasksWithRatingForDate(
                getCalendarDateForMidnightTime(calendar)
            )
            withContext(Dispatchers.Main) {
                LogHelper(this).d("Total tasks ${tasksWithRating.size} : " +
                        "Task id ${tasksWithRating[0].task.taskId}  " +
                        "is ${tasksWithRating[0].task.taskName} " +
                        "and has rating ${tasksWithRating[0].rating}")
                updateUI(tasksWithRating, swipe)
            }
        }
    }

    private fun manageAddingNewTask() {
        val addTaskButton = findViewById<Button>(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
            val taskEditText = dialogView.findViewById<EditText>(R.id.taskNameEditText)
            val addButton = dialogView.findViewById<Button>(R.id.addButton)

            // Add border to dialogView
            val drawable = GradientDrawable()
            //TODO - Set the border color as per the theme.
            drawable.setStroke(2, Color.BLACK)
            dialogView.background = drawable

            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            addButton.setOnClickListener {
                // Add the task to the database here
                if(taskEditText.text.isNotEmpty()) {
                    val taskName = taskEditText.text.trimStart().substring(0, 1)
                        .uppercase(Locale.ROOT) + taskEditText.text.trimStart()
                        .substring(1)
                    val task = Task(0, taskName, enabled = true, user_defined = true)
                    mainScope.launch (Dispatchers.IO) {
                        AppDatabase.getInstance(applicationContext).taskDao().insert(task)
                        refreshTasks(SwipeDirection.NONE)
                        withContext(Dispatchers.Main) {
                            Snackbar.make(findViewById(R.id.topLayout), "Task Added", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }

    private fun updateUI(lists: List<TaskWithRating>, swipe: SwipeDirection) {
        val animation : Animation? = when(swipe) {
            SwipeDirection.LEFT -> AnimationUtils.loadAnimation(this,R.anim.slide_in_from_left)
            SwipeDirection.RIGHT -> AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
            else -> null
        }
        val listView = findViewById<ListView>(R.id.listView)
        val data = lists.map { Item(it.task.taskName, it.task.taskId, it.rating) }
        adapter = ListAdapter(this, data)
        listView.adapter = adapter
        animation?.let { listView.startAnimation(animation) }
        adapter.setOnUpdateListListener(this)

        showTotalPoints()
    }

    override fun onDeleteTask(taskID: Long) {
        mainScope.launch(Dispatchers.IO) {
            AppDatabase.getInstance(applicationContext).taskDao().disableTask(taskID)
            withContext(Dispatchers.Main) {
                refreshTasks(SwipeDirection.NONE)
                Snackbar.make(findViewById(R.id.topLayout), "Task Deleted", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPointsGiven(taskID: Long, newRating: Int) {
        mainScope.launch(Dispatchers.IO) {
            val selectedDate = getCalendarDateForMidnightTime(calendar)
            val existingRating =
                AppDatabase.getInstance(applicationContext).ratingDao().getByTaskId(taskID, selectedDate)
            LogHelper(this).d("existing rating for task $taskID is $existingRating")
            if (existingRating.isNotEmpty()) {
                val ratingToSave = Rating(
                    ratingId = existingRating[0].ratingId,
                    childId = 1, // Replace with the actual child ID
                    taskId = taskID,
                    rating = newRating,
                    date = selectedDate
                )
                AppDatabase.getInstance(applicationContext).ratingDao().update(ratingToSave)
            } else {
                val ratingToSave = Rating(
                    ratingId = 0,
                    childId = 1, // Replace with the actual child ID
                    taskId = taskID,
                    rating = newRating,
                    date = selectedDate
                )
                AppDatabase.getInstance(applicationContext).ratingDao().insert(ratingToSave)
            }
            val tasksWithRating = AppDatabase.getInstance(applicationContext).taskDao().getTasksWithRatingForDate(
                selectedDate
            )
            withContext(Dispatchers.Main) {
                val data = tasksWithRating.map { Item(it.task.taskName, it.task.taskId, it.rating) }
                adapter.updateData(data)
                showTotalPoints()
            }
        }
    }
}
