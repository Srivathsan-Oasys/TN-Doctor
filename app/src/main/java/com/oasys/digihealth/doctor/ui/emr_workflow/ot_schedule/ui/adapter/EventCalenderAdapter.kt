package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response.OtScheduleToCalendarresponseContent
import com.oasys.digihealth.doctor.utils.Utils
import kotlinx.android.synthetic.main.row_otschedule_events_list.view.*

typealias OnClickItem = (content: OtScheduleToCalendarresponseContent) -> Unit
typealias OnClickEdit = (content: OtScheduleToCalendarresponseContent) -> Unit
typealias OnClickDelete = (content: OtScheduleToCalendarresponseContent) -> Unit

class EventCalenderAdapter(
    val onClickItem: OnClickItem,
    val onClickEdit: OnClickEdit,
    val onClickDelete: OnClickDelete
) :
    RecyclerView.Adapter<EventCalenderAdapter.EventHolder>() {
    var eventItem = ArrayList<OtScheduleToCalendarresponseContent>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        return EventHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_otschedule_events_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = eventItem.size

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.bindUI(eventItem[position], position)
    }

    fun addEventDetails(eventList: ArrayList<OtScheduleToCalendarresponseContent>) {
        eventItem.clear()
        eventItem = eventList
        //eventItem.addAll(eventList)
        notifyDataSetChanged()

    }

    inner class EventHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindUI(data: OtScheduleToCalendarresponseContent?, position: Int) {

            view.apply {
                data?.also {
                    val utils = Utils(view.context)
                    eventsTextView.text = utils.displayTime(
                        it.os_start_date!!,
                        "yyyy-MM-dd HH:mm:ss"
                    ) + " " + it.facility_name
                    serialNumberTextView.text = (position + 1).toString()

                    mainLinearLayout.setOnClickListener {
                        onClickItem.invoke(data)
                    }
                }
            }
        }
    }
}