package com.hmis_tn.doctor.ui.telemedicine.session.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.listener.OnCalendarDaysSelected
import kotlinx.android.synthetic.main.holder_appointment_calendar_days.view.*
import java.util.*

class AdapterCalendarDays(
    var calendarDaysList: ArrayList<String>,
    val onDaysSelected: OnCalendarDaysSelected
) :
    RecyclerView.Adapter<AdapterCalendarDays.CalendarViewHolder>() {

    private var calendarMapAdapter = HashMap<String, String>()
    private var isView: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val viewCalendar = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_appointment_calendar_days, parent, false)
        return CalendarViewHolder(viewCalendar)
    }

    override fun getItemCount(): Int = calendarDaysList.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(calendarDaysList[position])

        holder.itemView.tvCalendarDays.setOnClickListener {
            holder.itemView.tvCalendarDays.isSelected = !holder.itemView.tvCalendarDays.isSelected
            onDaysSelected.invoke(calendarDaysList[position])
        }
        holder.itemView.tvCalendarDays.isSelected =
            calendarMapAdapter.containsKey(calendarDaysList[position])

        if (isView) {
            holder.itemView.tvCalendarDays.isEnabled = false
        }

    }

    fun notifyDaysSelected(calendarMapAdapter: HashMap<String, String>, isView: Boolean) {
        this.isView = isView
        this.calendarMapAdapter = calendarMapAdapter
        notifyDataSetChanged()
    }

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(calendarDay: String) {
            itemView.apply {
                tvCalendarDays.text = calendarDay
            }
        }
    }

}