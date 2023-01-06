package com.tinytotsnbites.papapoints

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.tinytotsnbites.papapoints.data.*
import com.tinytotsnbites.papapoints.utilities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.text.SimpleDateFormat

class PointsAndTaskActivity : AppCompatActivity(), ListAdapter.UpdatePointsList {

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private lateinit var adapter: ListAdapter
    var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.points_and_task)

        populateChildDetails()
        refreshTasks()
        manageAddingNewTask()

        val dateFormat = SimpleDateFormat("dd MMM yy")
        val prevButton = findViewById<ImageButton>(R.id.prev_day_button)
        val nextButton = findViewById<ImageButton>(R.id.next_day_button)
        val textView = findViewById<TextView>(R.id.DayText)  // Get a reference to the TextView in the ViewPager
        val mediaPlayer = MediaPlayer.create(this, R.raw.button_click_calendar)

        prevButton.setOnClickListener {
            calendar.add(Calendar.DATE, -1)  // Reduce the date by one day
            textView.text = dateFormat.format(getCalendarDateForMidnightTime(calendar))
            refreshTasks()
            mediaPlayer.start()
        }

        nextButton.setOnClickListener {
            calendar.add(Calendar.DATE, +1)  // Reduce the date by one day
            textView.text = dateFormat.format(getCalendarDateForMidnightTime(calendar))
            refreshTasks()
            mediaPlayer.start()
        }
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
        val dateFormat = SimpleDateFormat("dd MMM yy")
        val dateText = findViewById<TextView>(R.id.DayText)
        dateText.text = dateFormat.format(Calendar.getInstance().time)

        val nameTextView = findViewById<TextView>(R.id.text_view_name)
        nameTextView.text = child.name

        val imageView = findViewById<ImageView>(R.id.imageView)
        val gender = child.gender
        LogHelper(this).d("Gender is $gender")
        if(gender.equals("Male")) {
            imageView.setImageResource(R.drawable.male_image)
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

    private fun refreshTasks() {
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
                updateUI(tasksWithRating)
            }
        }
    }

    private fun manageAddingNewTask() {
        val addTaskButton = findViewById<Button>(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
            val taskEditText = dialogView.findViewById<EditText>(R.id.taskNameEditText)
            val addButton = dialogView.findViewById<Button>(R.id.addButton)

            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            addButton.setOnClickListener {
                // Add the task to the database here
                if(taskEditText.text.isNotEmpty()) {
                    val task = Task(taskName = taskEditText.text.toString())
                    mainScope.launch (Dispatchers.IO) {
                        AppDatabase.getInstance(applicationContext).taskDao().insert(task)
                        refreshTasks()
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

    private fun updateUI(lists: List<TaskWithRating>) {
        val listView = findViewById<ListView>(R.id.listView)
        val data = lists.map { Item(it.task.taskName, it.task.taskId, it.rating) }
        adapter = ListAdapter(this, data)
        listView.adapter = adapter
        adapter.setOnUpdateListListener(this)

        showTotalPoints()
    }

    override fun onPointsGiven(taskID: Long, newRating: Int) {
        mainScope.launch(Dispatchers.IO) {
            val selectedDate = getCalendarDateForMidnightTime(calendar)
            val existingRating =
                AppDatabase.getInstance(applicationContext).ratingDao().getByTaskId(taskID, selectedDate)
            LogHelper(this).d("existing rating for task ${taskID} is $existingRating")
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
