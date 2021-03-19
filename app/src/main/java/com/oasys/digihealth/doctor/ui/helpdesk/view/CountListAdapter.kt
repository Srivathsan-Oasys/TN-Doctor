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
import com.oasys.digihealth.doctor.ui.helpdesk.model.TicketCountResponseContent

class CountListAdapter(
    context: Context,
    private var userManualTutotrial: ArrayList<TicketCountResponseContent?>?
) :
    RecyclerView.Adapter<CountListAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    private var isLoadingAdded = false
    private var customdialog: Dialog? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ticketnameTxt: TextView
        var countTxt: TextView

        init {
            ticketnameTxt = view.findViewById<View>(R.id.ticketname_txt) as TextView
            countTxt = view.findViewById<View>(R.id.count_txt) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_count_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(
            itemLayout
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listDate = userManualTutotrial!![position]
        holder.ticketnameTxt.text = listDate?.ticket_status_name
        holder.countTxt.text = listDate?.count.toString()
    }

    override fun getItemCount(): Int {
        return userManualTutotrial!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }


    fun addAll(responseContent: List<TicketCountResponseContent?>?) {

        this.userManualTutotrial!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.userManualTutotrial?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(TicketCountResponseContent())
    }

    fun add(r: TicketCountResponseContent) {
        userManualTutotrial!!.add(r)
        notifyItemInserted(userManualTutotrial!!.size - 1)
    }

    fun getItem(position: Int): TicketCountResponseContent? {
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