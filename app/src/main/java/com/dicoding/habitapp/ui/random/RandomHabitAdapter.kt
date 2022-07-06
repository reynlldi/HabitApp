package com.dicoding.habitapp.ui.random

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.ui.countdown.CountDownActivity
import com.dicoding.habitapp.utils.HABIT

class RandomHabitAdapter(
    private val onClick: (Habit) -> Unit
) : RecyclerView.Adapter<RandomHabitAdapter.PagerViewHolder>() {

    private val habitMap = LinkedHashMap<PageType, Habit>()

    fun submitData(key: PageType, habit: Habit) {
        habitMap[key] = habit
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PagerViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.pager_item, parent, false))

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val key = getIndexKey(position) ?: return
        val pageData = habitMap[key] ?: return
        holder.bind(key, pageData)
    }

    override fun getItemCount() = habitMap.size

    private fun getIndexKey(position: Int) = habitMap.keys.toTypedArray().getOrNull(position)

    enum class PageType {
        HIGH, MEDIUM, LOW
    }

    inner class PagerViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //TODO 14 : Create view and bind data to item view
        val title:TextView = itemView.findViewById(R.id.pager_tv_title)
        val startTime:TextView = itemView.findViewById(R.id.pager_tv_start_time)
        private val tvMinutes:TextView = itemView.findViewById(R.id.pager_tv_minutes)
        private val ivPriority:ImageView = itemView.findViewById(R.id.item_priority_level)
        private val btnCountdown:Button = itemView.findViewById(R.id.btn_open_count_down)

        fun bind(pageType: PageType, pageData: Habit) {
            title.text = pageData.title
            startTime.text = pageData.startTime
            tvMinutes.text = pageData.minutesFocus.toString()
            ivPriority.setImageResource(
                when(pageType){
                    PageType.LOW -> R.drawable.ic_priority_low
                    PageType.MEDIUM -> R.drawable.ic_priority_medium
                    PageType.HIGH -> R.drawable.ic_priority_high
                }
            )
            btnCountdown.setOnClickListener {
                val intent = Intent(itemView.context, CountDownActivity::class.java)
                intent.putExtra(HABIT, pageData)
                itemView.context.startActivity(intent)
            }
        }
    }
}
