package com.tinytotsnbites.papapoints

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.tinytotsnbites.papapoints.utilities.LogHelper

// Custom adapter to bind data to the list view
class ListAdapter(activity: PointsAndTaskActivity, data: List<Item>) : BaseAdapter() {

    private val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var data: MutableList<Item> = data.toMutableList()

    //Interface to communicate back to activity.
    interface UpdatePointsList {
        fun onPointsGiven(taskID: Long, newRating: Int)
        fun onDeleteTask(taskID: Long)
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
            holder.addPointsButton = view.findViewById(R.id.add_points_button)
            holder.removePointsButton = view.findViewById(R.id.remove_points_button)
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

        holder.addPointsButton.setOnClickListener {
            val plusButtonSound = MediaPlayer.create(view.context, R.raw.add_points_sound)
            val newRating = item.rating + 1
            LogHelper(this).d("New rating after adding is $newRating")
            listener?.onPointsGiven(item.taskID, newRating)
            holder.ratingBar.rating = newRating.toFloat()
            if(PreferenceManager.getDefaultSharedPreferences(view.context).getBoolean("sound_preference", true))
                plusButtonSound.start()
        }

        holder.removePointsButton.setOnClickListener {
            val minusButtonSound = MediaPlayer.create(view.context, R.raw.minus_points_sound)
            val newRating = item.rating - 1
            LogHelper(this).d("New rating after subtracting is $newRating")
            listener?.onPointsGiven(item.taskID, newRating)
            holder.ratingBar.rating = newRating.toFloat()
            if(PreferenceManager.getDefaultSharedPreferences(view.context).getBoolean("sound_preference", true))
                minusButtonSound.start()
        }

        holder.ratingBar.setOnRatingBarChangeListener { _, _, fromUser ->
            if(fromUser) {
                listener?.onPointsGiven(item.taskID, item.rating+1)
            }
        }

        view.setOnLongClickListener {
            val builder = AlertDialog.Builder(view.context)
            builder.setTitle(R.string.delete_task)
            builder.setMessage(R.string.delete_task_confirm_msg)
            builder.setPositiveButton(R.string.yes) { _, _ ->
                if(item.rating != 0) {
                    Snackbar.make(it, R.string.delete_task_error, Snackbar.LENGTH_LONG).show()
                } else {
                    listener?.onDeleteTask(item.taskID)
                }
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.show()
            true
        }
        return view
    }

    // View holder to optimize the list view
    private class ViewHolder {
        lateinit var textView: TextView
        lateinit var ratingBar: CustomRating
        lateinit var addPointsButton: Button
        lateinit var removePointsButton: Button
    }
}

// Data class to represent an item in the list
data class Item(val text: String, val taskID: Long, val rating: Int)