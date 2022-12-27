package com.tinytotsnbites.papapoints

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.tinytotsnbites.papapoints.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.tinytotsnbites.papapoints.data.Task

class PointsAndTaskActivity : AppCompatActivity() {

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskList: List<Task>
        mainScope.launch(Dispatchers.IO) {
            val lists = AppDatabase.getInstance(applicationContext).taskDao().getAll()
            withContext(Dispatchers.Main) {
                updateUI(lists)
            }
        }

        setContentView(R.layout.points_and_task)
        Log.d("tushar","showing tasks")
        // Get the list view and set the adapterx
//        val listView = findViewById<ListView>(R.id.listView)
//        val tasks = resources.getStringArray(R.array.tasks).mapIndexed { index, task ->
//            Task((index + 1).toLong(),task, 0f )
//        }
//        val data = tasks.map { Item(it.title, it.points) }
//        val adapter = ListAdapter(this, data)
//        listView.adapter = adapter
    }

    private fun updateUI(lists: List<Task>) {
        val listView = findViewById<ListView>(R.id.listView)
        val data = lists.map { Item(it.taskName, it.taskId) }
        val adapter = ListAdapter(this, data)
        listView.adapter = adapter
    }
}

// Custom adapter to bind data to the list view
class ListAdapter(context: Context, data: List<Item>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val data: List<Item> = data

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
        holder.ratingBar.rating = 0f

        return view
    }

    // View holder to optimize the list view
    private class ViewHolder {
        lateinit var textView: TextView
        lateinit var ratingBar: RatingBar
    }
}

// Data class to represent an item in the list
data class Item(val text: String, val rating: Long)
