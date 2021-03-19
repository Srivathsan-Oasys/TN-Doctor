package com.oasys.digihealth.doctor.ui.helpdesk.view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.*
import com.oasys.digihealth.doctor.ui.helpdesk.model.ActivityModel.TicketDetail
import kotlinx.android.synthetic.main.row_ticket_activity.view.*

class TicketActivityAdapter(
    private val context: Activity,
    private val activityArrayList: ArrayList<TicketDetail?>
) : RecyclerView.Adapter<TicketActivityAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_ticket_activity, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return activityArrayList.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.itemView.firstData.text =
            activityArrayList[position]!!.creator_name + activityArrayList[position]!!.ticketstatus_uuid
        holder.itemView.lastData.text = activityArrayList[position]!!.created_date
        holder.itemView.webViewData.settings.javaScriptEnabled = true
        activityArrayList[position]!!.comments?.let {
            holder.itemView.webViewData.loadData(
                it,
                "text/html; charset=utf-8",
                "UTF-8"
            )
        }

    }

    fun addAll(ticketDetails: List<TicketDetail>?) {

        activityArrayList.addAll(ticketDetails!!)

        notifyDataSetChanged()

    }


    class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

}