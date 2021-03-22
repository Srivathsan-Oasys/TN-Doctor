package com.oasys.digihealth.doctor.ui.out_patient.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.InPatientResponseData

class InpatientAdapter(val context: Context) :
    RecyclerView.Adapter<InpatientAdapter.MyViewHolder>() {
    private var responseContent: ArrayList<InPatientResponseData?>? = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null
    private var isLoadingAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_patient, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseContent = responseContent!![position]
        if (responseContent?.patient_info != null) {
            holder.patientNameTextView.text =
                responseContent.patient_info.toString()
        }
        holder.pinTextView.text = responseContent?.patient_uhid
        holder.phoneNumberTextView.text = responseContent?.patient_mobile


        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(responseContent, position)
        }

    }

    override fun getItemCount(): Int {
        return responseContent!!.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val patientNameTextView: TextView = itemView.findViewById(R.id.patientNameTextView)
        internal val pinTextView: TextView = itemView.findViewById(R.id.pinTextView)
        internal val phoneNumberTextView: TextView = itemView.findViewById(R.id.phoneNumberTextView)


    }

    fun addAll(responseContent: List<InPatientResponseData?>?) {
        this.responseContent?.clear()
        this.responseContent?.addAll(responseContent!!)
        notifyDataSetChanged()
    }


    interface OnItemClickListener {
        fun onItemClick(responseContent: InPatientResponseData?, position: Int)
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun clearAll() {
        this.responseContent?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
//        isLoadingAdded = true
//        add(InPatientResponseData())
    }

    fun add(r: InPatientResponseData) {
        responseContent!!.add(r)
        notifyItemInserted(responseContent!!.size - 1)
    }

    fun getItem(position: Int): InPatientResponseData? {
        return responseContent!![position]
    }

    fun removeLoadingFooter() {
//        isLoadingAdded = false
//        val position = responseContent!!.size - 1
//        val result = getItem(position)
//        if (result != null) {
//            responseContent!!.removeAt(position)
//            notifyItemRemoved(position)
//        }
    }
}


