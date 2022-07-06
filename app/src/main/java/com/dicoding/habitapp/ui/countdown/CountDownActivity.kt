package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIFICATION_CHANNEL_ID
import java.util.concurrent.TimeUnit

class CountDownActivity : AppCompatActivity() {
    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        workManager = WorkManager.getInstance(this)

        val habit = intent.getParcelableExtra<Habit>(HABIT) as Habit

        val tvCountDownTitle = findViewById<TextView>(R.id.tv_count_down_title)
        val tvCountDown = findViewById<TextView>(R.id.tv_count_down)
        tvCountDownTitle.text= habit.title

        val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)

        //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished

        viewModel.setInitialTime(habit.minutesFocus)
        viewModel.currentTimeString.observe(this){
            tvCountDown.text = it
        }

        //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up.

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            viewModel.startTimer()
            updateButtonState(true)

            val data = Data.Builder()
                .putInt(HABIT_ID, habit.id)
                .putString(HABIT_TITLE, habit.title)
                .build()

            val oneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                .setInputData(data)
                .setInitialDelay(habit.minutesFocus, TimeUnit.MINUTES)
                .addTag(NOTIFICATION_CHANNEL_ID)
                .build()

            workManager.enqueue(oneTimeWorkRequest)
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            viewModel.resetTimer()
            workManager.cancelAllWorkByTag(NOTIFICATION_CHANNEL_ID)
        }

        viewModel.eventCountDownFinish.observe(this){
            updateButtonState(!it)
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }
}