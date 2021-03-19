package com.oasys.digihealth.doctor.ui.helpdesk.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.helpdesk.model.TicketListResponseContent
import java.text.SimpleDateFormat

class TicketsListAdapter(
    context: Context,
    private var userManualTutotrial: ArrayList<TicketListResponseContent?>?,
    private var clickListener: HelpDeskCallback
) :
    RecyclerView.Adapter<TicketsListAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    private var isLoadingAdded = false
    private var customdialog: Dialog? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sNoTxt: TextView
        var dateTxt: TextView
        var ticketIdTxt: TextView
        var categoryTxt: TextView
        var institutionTxt: TextView
        var priorityTxt: TextView
        var statusTxt: TextView
        var assignedByTxt: TextView
        var imgAction: ImageView

        init {
            sNoTxt = view.findViewById<View>(R.id.sNo_txt) as TextView
            dateTxt = view.findViewById<View>(R.id.date_txt) as TextView
            ticketIdTxt = view.findViewById<View>(R.id.ticketId_txt) as TextView
            categoryTxt = view.findViewById<View>(R.id.category_txt) as TextView
            institutionTxt = view.findViewById<View>(R.id.institution_txt) as TextView
            priorityTxt = view.findViewById<View>(R.id.priority_txt) as TextView
            statusTxt = view.findViewById<View>(R.id.status_txt) as TextView
            assignedByTxt = view.findViewById<View>(R.id.assigned_txt) as TextView
            imgAction = view.findViewById<View>(R.id.imgAction) as ImageView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_ticket_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(
            itemLayout
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listDate = userManualTutotrial!![position]
        holder.sNoTxt.text = (position + 1).toString()
        holder.ticketIdTxt.text = listDate?.ticket_id!!
        holder.categoryTxt.text = listDate.help_desk_category_name
        holder.institutionTxt.text = listDate.institution_name
        holder.priorityTxt.text = listDate.ticket_details_priority_name
        holder.statusTxt.text = listDate.ticket_details_ticket_status_name
        holder.assignedByTxt.text = listDate.ticket_created_by

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val sdf1 = SimpleDateFormat("dd-MM-yyyy")
        var date = sdf.parse(listDate.created_date)
        var createddate = sdf1.format(date)
        holder.dateTxt.text = createddate

        holder.imgAction.setOnClickListener {
            clickListener.OnViewClick(listDate)
        }

    }

    override fun getItemCount(): Int {
        return userManualTutotrial!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }


    fun addAll(responseContent: List<TicketListResponseContent?>?) {

        this.userManualTutotrial!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.userManualTutotrial?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(TicketListResponseContent())
    }

    fun add(r: TicketListResponseContent) {
        userManualTutotrial!!.add(r)
        notifyItemInserted(userManualTutotrial!!.size - 1)
    }

    fun getItem(position: Int): TicketListResponseContent? {
        return userManualTutotrial!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = userManualTutotrial!!.size - 1
        val result = getItem(position)
        if (result != null) {
            userManualTutotrial!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}