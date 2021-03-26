package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.ui.adapter

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.response.OtScheduleToCalendarresponseContent
import kotlinx.android.synthetic.main.item_agenda_calendar.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

typealias OnDisplayEventList = (ArrayList<OtScheduleToCalendarresponseContent>) -> Unit

class CalendarAdapter(
    val context: Context,
    var currentDate: Int,
    var month: Int,
    var year: Int,
    var tvCalendarPrevious: AppCompatTextView,
    var tvCalendarMonth: AppCompatTextView,
    var tvCalendarNext: AppCompatTextView,
    var onDisplayEventList: OnDisplayEventList
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    private var eventList = ArrayList<OtScheduleToCalendarresponseContent>()
    private var eventDateList = ArrayList<String>()
    private var isFirst = false

    var height: Int = 0

    private val daysOfMonth =
        intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    private val list = ArrayList<String>()
    private val monthsArray = context.resources.getStringArray(R.array.month_array).toMutableList()
    private var currentDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var selectedPosition: Int = -1

    init {
        printMonth(month, year)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_agenda_calendar,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = list[if (selectedPosition == position) selectedPosition else position].split("-")
            .toTypedArray()

//        val day = list[position].split("-".toRegex()).toTypedArray()
//        val month = list[if (selectedPosition == position) selectedPosition else position].split("-")
//            .toTypedArray()
//        println("GET____MESSAGGE______"+list)
        println("GET____MESSAGGE______" + month)
        holder.bind(day, eventList, eventDateList)
        holder.itemView.rlAgendaItem.setOnClickListener {
            try {
                var x: Int = (position / 7 + 1) * ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.event_selected_background
                )!!.intrinsicHeight
                x += 2 * (holder.itemView.context as AppCompatActivity).supportActionBar?.height!! - 20
                height = x

                currentDate = day[0].toInt()
                if (selectedPosition != position) {
                    Handler().postDelayed({
                        enableEventOnDay(day)
                        setSelectedPosition(position)
                    }, 10)
                } else {
//                        disableEventOnDay()
                    setSelectedPosition(-1)
                }
                /* if (holder.itemView.tvAgendaNotification.visibility == View.VISIBLE ||
                     holder.itemView.tvAgendaNotificationMore.visibility == View.VISIBLE) {
                     if (selectedPosition != position) {
                         Handler().postDelayed({
                             enableEventOnDay(day)
                             setSelectedPosition(position)
                         }, 10)
                     } else {
 //                        disableEventOnDay()
                         setSelectedPosition(-1)
                     }
                 }*/
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun setSelectedPosition(selectedPosition: Int) {
        this.selectedPosition = selectedPosition
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            day: Array<String>,
            eventList: ArrayList<OtScheduleToCalendarresponseContent>,
            eventDateList: ArrayList<String>
        ) {
            itemView.tvAgendaDate.text = day[0]
            try {
                if (day[0] == currentDate.toString()) {
                    itemView.ivEventDecoration.visibility = View.VISIBLE
                } else
                    itemView.ivEventDecoration.visibility = View.GONE
                if (eventDateList.contains(day[0])) {
                    itemView.tvAgendaNotification.visibility = View.VISIBLE
                    val selectedEvents: ArrayList<OtScheduleToCalendarresponseContent> = ArrayList()
                    for (i in eventList.indices) {
                        val format = SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss",

                            Locale.ENGLISH
                        )
                        val date = format.parse(eventList[i].os_start_date!!)
                        val calendar = Calendar.getInstance()
                        calendar.time = date!!
                        val eventDate = calendar[Calendar.DATE]
                        if (eventDate == day[0].toInt() || day[0].toInt() in (eventDate + 1)..eventDate) {
                            selectedEvents.add(eventList[i])
                        }
                    }

                    if (selectedEvents.size > 1) {
                        itemView.tvAgendaNotificationMore.visibility = View.VISIBLE
                    } else {
                        itemView.tvAgendaNotificationMore.visibility = View.GONE
                    }


                } else {
                    itemView.tvAgendaNotification.visibility = View.INVISIBLE
                    itemView.pbOperationRDV.visibility = View.GONE
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun enableEventOnDay(
        day: Array<String>
    ) {
        try {
            var selectedEvents = arrayListOf<OtScheduleToCalendarresponseContent>()
            if (day.isEmpty()) {
                selectedEvents = ArrayList()
            } else {
                val date = day[0].toInt()
                for (l in eventList.indices) {
                    var rdvDate = Date()
                    val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
                    if (eventList[l].os_start_date != null) {
                        rdvDate = format.parse(eventList[l].os_start_date!!)!!
                    }
                    val calendar = Calendar.getInstance()
                    calendar.time = rdvDate
                    val eventDate = calendar[Calendar.DATE]
                    val eventMonth = calendar[Calendar.MONTH]
                    val eventYear = calendar[Calendar.YEAR]

                    if (year == eventYear && month == eventMonth) {
                        if (eventDate == date || date in (eventDate + 1)..eventDate) {
                            if (!selectedEvents.contains(eventList[l]))
                                selectedEvents.add(eventList[l])
                            Log.e("selected", "tested" + selectedEvents.size)
                        }
                    }
                }
                onDisplayEventList.invoke(selectedEvents)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun printMonth(currentMonth: Int, currentYear: Int) {
        try {
            var trailingSpaces: Int
            val prevMonth: Int
            val nextMonth: Int
            val cal =
                GregorianCalendar(currentYear, currentMonth, currentDayOfMonth)
            // Days in Current Month
            var daysInMonth = daysOfMonth[currentMonth]
            val prevMonthYear: Int
            val nextMonthYear: Int
            when (currentMonth) {
                11 -> {
                    prevMonth = 10
                    nextMonth = 0
                    prevMonthYear = currentYear
                    nextMonthYear = currentYear + 1
                }
                0 -> {
                    prevMonth = 11
                    nextMonth = 1
                    prevMonthYear = currentYear - 1
                    nextMonthYear = currentYear
                }
                else -> {
                    prevMonth = currentMonth - 1
                    nextMonth = currentMonth + 1
                    prevMonthYear = currentYear
                    nextMonthYear = currentYear
                }
            }
            val calc: Calendar = GregorianCalendar(year, currentMonth, 1)
            trailingSpaces = calc[Calendar.DAY_OF_WEEK]
            if (cal.isLeapYear(cal[Calendar.YEAR]) && currentMonth == 1) {
                ++daysInMonth
            }
            if (trailingSpaces - 2 < 0) {
                trailingSpaces += -2 + 7
            } else {
                trailingSpaces -= 2
            }
            // Trailing Month days
            for (i in 0 until trailingSpaces) {
                list.add("")
            }
            // Current Month Days
            for (i in 1..daysInMonth) {
                list.add(i.toString() + "-WHITE" + "-" + monthsArray[currentMonth] + "-" + currentYear)
            }
            // Leading Month days
            for (i in 0 until list.size % 7) {
                list.add("")
            }
            tvCalendarPrevious.text = monthsArray[prevMonth].substring(0, 3) + " " + prevMonthYear
            tvCalendarNext.text = monthsArray[nextMonth].substring(
                0,
                3
            ) + " " + nextMonthYear
            tvCalendarMonth.text = monthsArray[currentMonth] + " " + currentYear
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setData(
        eventList: ArrayList<OtScheduleToCalendarresponseContent>,
        eventDateList: ArrayList<String>,
        isFirst: Boolean
    ) {
        this.isFirst = isFirst
        this.eventList = eventList
        this.eventDateList = eventDateList
        notifyDataSetChanged()
    }

}