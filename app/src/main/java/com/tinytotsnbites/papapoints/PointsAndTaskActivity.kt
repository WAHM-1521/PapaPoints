package com.tinytotsnbites.papapoints

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.scaleMatrix
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tinytotsnbites.papapoints.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.tinytotsnbites.papapoints.utilities.LogHelper
import com.tinytotsnbites.papapoints.utilities.getCalendarInDateFormat
import java.util.*

class PointsAndTaskActivity : AppCompatActivity() {

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.points_and_task)

        populateChildDetails()
        refreshTasks()
        manageAddingNewTask()

//        viewPager = findViewById(R.id.view_pager)
//        tabLayout = findViewById(R.id.tab_layout)
//
//        /*val adapter = MyFragmentStateAdapter(this)
//        viewPager.adapter = adapter
//        tabLayout.setupWithViewPager(viewPager)*/
//
//        tabLayout.setTabTextColors(
//            ContextCompat.getColor(this, R.color.colorPrimary),
//            ContextCompat.getColor(this, R.color.colorAccent)
//        )
//        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent))

       /* TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Yesterday"
                1 -> "Today"
                else -> "Tomorrow"
            }
        }.attach()*/
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
        val nameTextView = findViewById<TextView>(R.id.text_view_name)
        nameTextView.text = child.name

        val imageView = findViewById<ImageView>(R.id.imageView)
        val gender = child.gender

        LogHelper(this).d("Gender is $gender")

        if(gender.equals("Male")) {
            imageView.setImageResource(R.drawable.male_image)
        } else if(gender.equals("Female")) {
            imageView.setImageResource(R.drawable.female_image)
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
                getCalendarInDateFormat())
            withContext(Dispatchers.Main) {
                LogHelper(this).d("tasks with Rating ${tasksWithRating.size}")
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
        val adapter = ListAdapter(this, data)
        listView.adapter = adapter
    }
}

/*class MyFragmentStateAdapter(pointsAndTaskActivity: PointsAndTaskActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //TODO("Not yet implemented")
    }

        private val fragments = listOf(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment()
        )
         override fun getItemCount(): Int {
             return fragments.size
         }
         fun createFragment(position: Int): Fragment {
             returnents[position]
         }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //TODO("Not yet implemented")
    }

//}

         private fun TodayFragment(): Fragment {

         }


}*/

// Custom adapter to bind data to the list view
class ListAdapter(private val activity: PointsAndTaskActivity, data: List<Item>) : BaseAdapter() {

    private val listScope = CoroutineScope(Dispatchers.Main)
    private val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var data: MutableList<Item> = data.toMutableList()
    private val currentDate = getCalendarInDateFormat()

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
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
            view.tag = holder
        } else {
            // Reuse the view holder
            view = convertView
            holder = view.tag as ViewHolder
        }

        // Bind the data to the view
        val item = getItem(position) as Item
        holder.textView.text = item.text
        holder.ratingBar.rating = item.rating

        holder.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            // Save the rating to the database here
            if(fromUser) {
                listScope.launch(Dispatchers.IO) {
                    val existingRating = AppDatabase.getInstance(view.context).ratingDao().getByTaskId(item.taskID)
                    LogHelper(this).d("existing rating for ${item.taskID} is $existingRating")
                    if(existingRating.isNotEmpty()) {
                        val ratingToSave = Rating(
                            ratingId = existingRating.get(0).ratingId,
                            childId = 1, // Replace with the actual child ID
                            taskId = item.taskID,
                            rating = rating,
                            date = currentDate
                        )
                        AppDatabase.getInstance(view.context).ratingDao().update(ratingToSave)
                    } else {
                        val ratingToSave = Rating(
                            ratingId = 0,
                            childId = 1, // Replace with the actual child ID
                            taskId = item.taskID,
                            rating = rating,
                            date = currentDate
                        )
                        AppDatabase.getInstance(view.context).ratingDao().insert(ratingToSave)
                    }
                    withContext(Dispatchers.Main) {
                        data[position] = data[position].copy(rating = rating)
                        notifyDataSetChanged()
                        activity.showTotalPoints()
                    }
                }
            }
        }
        return view
    }

    // View holder to optimize the list view
    private class ViewHolder {
        lateinit var textView: TextView
        lateinit var ratingBar: RatingBar
    }
}

// Data class to represent an item in the list
data class Item(val text: String, val taskID: Long, val rating: Float)
