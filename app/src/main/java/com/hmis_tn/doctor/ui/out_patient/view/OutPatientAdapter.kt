package com.hmis_tn.doctor.ui.out_patient.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.out_patient.search_response_model.ResponseContent

class OutPatientAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<OutPatientAdapter.MyViewHolder>() {
    private var responseContent: ArrayList<ResponseContent?>? = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null
    private var isLoadingAdded = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_patient, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseContent = responseContent!![position]

        if (responseContent?.first_name != null && responseContent.last_name != null) {
            holder.patientNameTextView.text =
                responseContent.first_name + " " + responseContent.last_name + "/" + responseContent.age + " Y" + "/ M"
        } else {
            holder.patientNameTextView.text =
                responseContent?.first_name + "/" + responseContent?.age + " Y" + " M"
        }
        holder.pinTextView.text = responseContent?.uhid
        holder.phoneNumberTextView.text = responseContent?.patient_detail?.mobile
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

    fun addAll(responseContent: List<ResponseContent?>?) {
        this.responseContent?.addAll(responseContent!!)
        notifyDataSetChanged()
    }


    interface OnItemClickListener {
        fun onItemClick(responseContent: ResponseContent?, position: Int)
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun clearAll() {
        this.responseContent?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(ResponseContent())
    }

    fun add(r: ResponseContent) {
        responseContent!!.add(r)
        notifyItemInserted(responseContent!!.size - 1)
    }

    fun getItem(position: Int): ResponseContent? {
        return responseContent!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        /* val position = responseContent!!.size - 1
         val result = getItem(position)
         if (result != null) {
             responseContent!!.removeAt(position)
             notifyItemRemoved(position)
         }*/
    }

    fun setData(responseContent: ResponseContent) {
        this.responseContent?.add(responseContent)
        notifyDataSetChanged()

    }
}


