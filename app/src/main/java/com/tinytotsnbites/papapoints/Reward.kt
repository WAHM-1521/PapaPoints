package com.tinytotsnbites.papapoints

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Reward : AppCompatActivity() {

    private var rewardPoints = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rewards)

        var rewardTextView = findViewById<TextView>(R.id.text_view_total_points)
        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("age", 0)

        val nameTextView = findViewById<TextView>(R.id.text_view_name)
        nameTextView.text = name

        val ageTextView = findViewById<TextView>(R.id.text_view_age)
        ageTextView.text = age.toString()

        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        rewardPoints = sharedPreferences.getInt("rewardPoints", 0)
        rewardTextView.text = rewardPoints.toString()

        val checkBoxTask1Mondayput = findViewById<CheckBox>(R.id.checkBox_task_1_monday)
        val isChecked = sharedPreferences.getBoolean(checkBoxTask1Mondayput.toString(),false)
        checkBoxTask1Mondayput.isChecked = isChecked

//        for (i in 1..70)
//        {
//            val checkBoxId = resources.getIdentifier("check_box_task_$i", "id", packageName)
//            val checkBox = findViewById<CheckBox>(checkBoxId)
//
//            // Load the checked state from SharedPreferences
//            val isChecked = sharedPreferences.getBoolean("task_$i", false)
//            checkBox.isChecked = isChecked
//
//            checkBox.setOnCheckedChangeListener { _, isChecked ->
//                // Save the checked state to SharedPreferences
//                if (isChecked) {
//                    rewardPoints++
//                } else {
//                    rewardPoints--
//                }
//                val editor = sharedPreferences.edit()
//                editor.putBoolean("task_$i", isChecked)
//                editor.apply()
//            }
//        }

        val updateRewardButton = findViewById<Button>(R.id.button_reward_update)
        updateRewardButton.setOnClickListener {
            rewardTextView.text = rewardPoints.toString()
        }

        val checkBoxTask1Monday = findViewById<CheckBox>(R.id.checkBox_task_1_monday)
        checkBoxTask1Monday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
                val editor = sharedPreferences.edit()
                editor.putBoolean(checkBoxTask1Monday.toString(), isChecked)
                editor.apply()
        }
        val checkBoxTask1Tuesday = findViewById<CheckBox>(R.id.checkBox_task_1_tuesday)
        checkBoxTask1Tuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask1Wednesday = findViewById<CheckBox>(R.id.checkBox_task_1_wednesday)
        checkBoxTask1Wednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask1Thursday = findViewById<CheckBox>(R.id.checkBox_task_1_thursday)
        checkBoxTask1Thursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask1Friday = findViewById<CheckBox>(R.id.checkBox_task_1_friday)
        checkBoxTask1Friday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask1Saturday = findViewById<CheckBox>(R.id.checkBox_task_1_saturday)
        checkBoxTask1Saturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask1Sunday = findViewById<CheckBox>(R.id.checkBox_task_1_sunday)
        checkBoxTask1Sunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }

        val checkBoxTask2Monday = findViewById<CheckBox>(R.id.checkBox_task_2_monday)
        checkBoxTask2Monday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask2Tuesday = findViewById<CheckBox>(R.id.checkBox_task_2_tuesday)
        checkBoxTask2Tuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask2Wednesday = findViewById<CheckBox>(R.id.checkBox_task_2_wednesday)
        checkBoxTask2Wednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask2Thursday = findViewById<CheckBox>(R.id.checkBox_task_2_thursday)
        checkBoxTask2Thursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask2Friday = findViewById<CheckBox>(R.id.checkBox_task_2_friday)
        checkBoxTask2Friday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask2Saturday = findViewById<CheckBox>(R.id.checkBox_task_2_saturday)
        checkBoxTask2Saturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask2Sunday = findViewById<CheckBox>(R.id.checkBox_task_2_sunday)
        checkBoxTask2Sunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask3Monday = findViewById<CheckBox>(R.id.checkBox_task_3_monday)
        checkBoxTask3Monday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask3Tuesday = findViewById<CheckBox>(R.id.checkBox_task_3_tuesday)
        checkBoxTask3Tuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask3Wednesday = findViewById<CheckBox>(R.id.checkBox_task_3_wednesday)
        checkBoxTask3Wednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask3Thursday = findViewById<CheckBox>(R.id.checkBox_task_3_thursday)
        checkBoxTask3Thursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask3Friday = findViewById<CheckBox>(R.id.checkBox_task_3_friday)
        checkBoxTask3Friday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask3Saturday = findViewById<CheckBox>(R.id.checkBox_task_3_saturday)
        checkBoxTask3Saturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask3Sunday = findViewById<CheckBox>(R.id.checkBox_task_3_sunday)
        checkBoxTask3Sunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask4Monday = findViewById<CheckBox>(R.id.checkBox_task_4_monday)
        checkBoxTask4Monday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask4Tuesday = findViewById<CheckBox>(R.id.checkBox_task_4_tuesday)
        checkBoxTask4Tuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask4Wednesday = findViewById<CheckBox>(R.id.checkBox_task_4_wednesday)
        checkBoxTask4Wednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask4Thursday = findViewById<CheckBox>(R.id.checkBox_task_4_thursday)
        checkBoxTask4Thursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask4Friday = findViewById<CheckBox>(R.id.checkBox_task_4_friday)
        checkBoxTask4Friday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask4Saturday = findViewById<CheckBox>(R.id.checkBox_task_4_saturday)
        checkBoxTask4Saturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask4Sunday = findViewById<CheckBox>(R.id.checkBox_task_4_sunday)
        checkBoxTask4Sunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask5Monday = findViewById<CheckBox>(R.id.checkBox_task_5_monday)
        checkBoxTask5Monday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask5Tuesday = findViewById<CheckBox>(R.id.checkBox_task_5_tuesday)
        checkBoxTask5Tuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask5Wednesday = findViewById<CheckBox>(R.id.checkBox_task_5_wednesday)
        checkBoxTask5Wednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask5Thursday = findViewById<CheckBox>(R.id.checkBox_task_5_thursday)
        checkBoxTask5Thursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask5Friday = findViewById<CheckBox>(R.id.checkBox_task_5_friday)
        checkBoxTask5Friday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask5Saturday = findViewById<CheckBox>(R.id.checkBox_task_5_saturday)
        checkBoxTask5Saturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask5Sunday = findViewById<CheckBox>(R.id.checkBox_task_5_sunday)
        checkBoxTask5Sunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask6Monday = findViewById<CheckBox>(R.id.checkBox_task_6_monday)
        checkBoxTask6Monday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask6Tuesday = findViewById<CheckBox>(R.id.checkBox_task_6_tuesday)
        checkBoxTask6Tuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask6Wednesday = findViewById<CheckBox>(R.id.checkBox_task_6_wednesday)
        checkBoxTask6Wednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask6Thursday = findViewById<CheckBox>(R.id.checkBox_task_6_thursday)
        checkBoxTask6Thursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask6Friday = findViewById<CheckBox>(R.id.checkBox_task_6_friday)
        checkBoxTask6Friday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask6Saturday = findViewById<CheckBox>(R.id.checkBox_task_6_saturday)
        checkBoxTask6Saturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask6Sunday = findViewById<CheckBox>(R.id.checkBox_task_6_sunday)
        checkBoxTask6Sunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask7Monday = findViewById<CheckBox>(R.id.checkBox_task_7_monday)
        checkBoxTask7Monday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask7Tuesday = findViewById<CheckBox>(R.id.checkBox_task_7_tuesday)
        checkBoxTask7Tuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask7Wednesday = findViewById<CheckBox>(R.id.checkBox_task_7_wednesday)
        checkBoxTask7Wednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask7Thursday = findViewById<CheckBox>(R.id.checkBox_task_7_thursday)
        checkBoxTask7Thursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask7Friday = findViewById<CheckBox>(R.id.checkBox_task_7_friday)
        checkBoxTask7Friday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask7Saturday = findViewById<CheckBox>(R.id.checkBox_task_7_saturday)
        checkBoxTask7Saturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask7Sunday = findViewById<CheckBox>(R.id.checkBox_task_7_sunday)
        checkBoxTask7Sunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask8Monday = findViewById<CheckBox>(R.id.checkBox_task_8_monday)
        checkBoxTask8Monday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask8Tuesday = findViewById<CheckBox>(R.id.checkBox_task_8_tuesday)
        checkBoxTask8Tuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask8Wednesday = findViewById<CheckBox>(R.id.checkBox_task_8_wednesday)
        checkBoxTask8Wednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask8Thursday = findViewById<CheckBox>(R.id.checkBox_task_8_thursday)
        checkBoxTask8Thursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask8Friday = findViewById<CheckBox>(R.id.checkBox_task_8_friday)
        checkBoxTask8Friday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask8Saturday = findViewById<CheckBox>(R.id.checkBox_task_8_saturday)
        checkBoxTask8Saturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask8Sunday = findViewById<CheckBox>(R.id.checkBox_task_8_sunday)
        checkBoxTask8Sunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask9Monday = findViewById<CheckBox>(R.id.checkBox_task_9_monday)
        checkBoxTask9Monday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask9Tuesday = findViewById<CheckBox>(R.id.checkBox_task_9_tuesday)
        checkBoxTask9Tuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask9Wednesday = findViewById<CheckBox>(R.id.checkBox_task_9_wednesday)
        checkBoxTask9Wednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask9Thursday = findViewById<CheckBox>(R.id.checkBox_task_9_thursday)
        checkBoxTask9Thursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask9Friday = findViewById<CheckBox>(R.id.checkBox_task_9_friday)
        checkBoxTask9Friday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask9Saturday = findViewById<CheckBox>(R.id.checkBox_task_9_saturday)
        checkBoxTask9Saturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask9Sunday = findViewById<CheckBox>(R.id.checkBox_task_9_sunday)
        checkBoxTask9Sunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask10Monday = findViewById<CheckBox>(R.id.checkBox_task_10_monday)
        checkBoxTask10Monday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask10Tuesday = findViewById<CheckBox>(R.id.checkBox_task_10_tuesday)
        checkBoxTask10Tuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask10Wednesday = findViewById<CheckBox>(R.id.checkBox_task_10_wednesday)
        checkBoxTask10Wednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask10Thursday = findViewById<CheckBox>(R.id.checkBox_task_10_thursday)
        checkBoxTask10Thursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask10Friday = findViewById<CheckBox>(R.id.checkBox_task_10_friday)
        checkBoxTask10Friday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask10Saturday = findViewById<CheckBox>(R.id.checkBox_task_10_saturday)
        checkBoxTask10Saturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
        val checkBoxTask10Sunday = findViewById<CheckBox>(R.id.checkBox_task_10_sunday)
        checkBoxTask10Sunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rewardPoints++
            } else {
                rewardPoints--
            }
        }
    }


        override fun onStop() {
            super.onStop()

            // Save the reward points to SharedPreferences
            val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("rewardPoints", rewardPoints)
            editor.apply()
        }
}