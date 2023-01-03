package com.tinytotsnbites.papapoints

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.tinytotsnbites.papapoints.data.*
import com.tinytotsnbites.papapoints.utilities.*
import com.tinytotsnbites.papapoints.utilities.getCalendarInDateFormat
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
        val textView = findViewById<TextView>(R.id.DayText)  // Get a reference to the TextView in the ViewPager


        prevButton.setOnClickListener {
            calendar.add(Calendar.DATE, -1)  // Reduce the date by one day
            textView.text = dateFormat.format(getCalendarDateForMidnightTime(calendar))
            refreshTasks()
        }

        val nextButton = findViewById<ImageButton>(R.id.next_day_button)
        nextButton.setOnClickListener {
            calendar.add(Calendar.DATE, +1)  // Reduce the date by one day
            textView.text = dateFormat.format(getCalendarDateForMidnightTime(calendar))
            refreshTasks()
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

    fun showTotalPoints() {
        val totalPoints = findViewById<TextView>(R.id.text_view_total_points)
        mainScope.launch(Dispatchers.IO) {
            val totalPointsValue =
                AppDatabase.getInstance(applicationContext).ratingDao().getTotalRating()
            withContext(Dispatchers.Main) {
                totalPoints.text =
                    String.format(getString(R.string.total_points), totalPointsValue.toString())
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
                        "Task id ${tasksWithRating.get(0).task.taskId}  " +
                        "is ${tasksWithRating.get(0).task.taskName} " +
                        "and has rating ${tasksWithRating.get(0).rating}")
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

    override fun onPointsGiven() {
        mainScope.launch(Dispatchers.IO) {
            val tasksWithRating = AppDatabase.getInstance(applicationContext).taskDao().getTasksWithRatingForDate(
                getCalendarDateForMidnightTime(calendar)
            )
            withContext(Dispatchers.Main) {
                val data = tasksWithRating.map { Item(it.task.taskName, it.task.taskId, it.rating) }
                adapter.updateData(data)
                showTotalPoints()
            }
        }
    }
}


// Custom adapter to bind data to the list view
class ListAdapter(activity: PointsAndTaskActivity, data: List<Item>) : BaseAdapter() {

    private val listScope = CoroutineScope(Dispatchers.Main)
    private val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var data: MutableList<Item> = data.toMutableList()
    private val currentDate = getCalendarInDateFormat()

    interface UpdatePointsList {
        fun onPointsGiven()
    }

    private var listener: UpdatePointsList? = null

    fun setOnUpdateListListener(listener: UpdatePointsList) {
        this.listener = listener
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateData(newData: List<Item>) {
        data = newData as MutableList<Item>
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            // Inflate the layout and create a new view holder
            view = inflater.inflate(R.layout.task_rating_row, parent, false)
            holder = ViewHolder()
            holder.textView = view.findViewById(R.id.textView)
            holder.ratingBar = view.findViewById(R.id.ratingBar)
            holder.plusButton = view.findViewById(R.id.add_points_button)
            holder.minusButton = view.findViewById(R.id.remove_points_button)
            view.tag = holder
        } else {
            // Reuse the view holder
            view = convertView
            holder = view.tag as ViewHolder
        }

        // Bind the data to the view
        val item = getItem(position) as Item
        holder.textView.text = item.text
        holder.ratingBar.rating = item.rating.toFloat()
        holder.ratingBar.setRatingValueNew(item.rating)


        holder.plusButton.setOnClickListener {
            val newRating = item.rating + 1
            LogHelper(this).d("New rating after adding is $newRating")
            updateDbAndShowPoints(view, item, newRating)
            holder.ratingBar.rating = newRating.toFloat()
        }

        holder.minusButton.setOnClickListener {
            val newRating = item.rating - 1
            LogHelper(this).d("New rating after subtracting is $newRating")
            updateDbAndShowPoints(view, item, newRating)
            holder.ratingBar.rating = newRating.toFloat()
        }

        holder.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if(fromUser) {
                updateDbAndShowPoints(view, item, item.rating+1)
            }
        }
        return view
    }

    private fun updateDbAndShowPoints(
        view: View,
        item: Item,
        newRating: Int
    ) {
        listScope.launch(Dispatchers.IO) {
            val existingRating =
                AppDatabase.getInstance(view.context).ratingDao().getByTaskId(item.taskID)
            LogHelper(this).d("existing rating for task ${item.taskID} is $existingRating")
            if (existingRating.isNotEmpty()) {
                val ratingToSave = Rating(
                    ratingId = existingRating.get(0).ratingId,
                    childId = 1, // Replace with the actual child ID
                    taskId = item.taskID,
                    rating = newRating,
                    date = currentDate
                )
                AppDatabase.getInstance(view.context).ratingDao().update(ratingToSave)
            } else {
                val ratingToSave = Rating(
                    ratingId = 0,
                    childId = 1, // Replace with the actual child ID
                    taskId = item.taskID,
                    rating = newRating,
                    date = currentDate
                )
                AppDatabase.getInstance(view.context).ratingDao().insert(ratingToSave)
            }

            withContext(Dispatchers.Main) {
                LogHelper(this).d("new rating is $newRating")
                //data[position] = data[position].copy(rating = newRating)
                listener?.onPointsGiven()
            }
        }
    }

    // View holder to optimize the list view
    private class ViewHolder {
        lateinit var textView: TextView
        lateinit var ratingBar: CustomRating
        lateinit var plusButton: Button
        lateinit var minusButton: Button
    }
}

// Data class to represent an item in the list
data class Item(val text: String, val taskID: Long, val rating: Int)
