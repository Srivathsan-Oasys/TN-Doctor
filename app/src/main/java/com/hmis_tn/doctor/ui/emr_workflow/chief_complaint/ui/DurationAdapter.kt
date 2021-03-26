package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.isTablet
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseContent

class DurationAdapter(
    private val context: Context,
    private val durationArrayList: ArrayList<DurationResponseContent?>?
) :
    RecyclerView.Adapter<DurationAdapter.MyViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = if (isTablet(context)) {
            LayoutInflater.from(context)
                .inflate(R.layout.row_duration, parent, false)
        } else {
            LayoutInflater.from(context)
                .inflate(R.layout.row_allergy_duration, parent, false)
        }
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseContent = durationArrayList?.get(position)!!
        holder.durationTextView.text = responseContent.duration_period_name?.get(0).toString()
        holder.durationCardView.setOnClickListener {
            selectedPosition = responseContent.duration_period_id!!
            onItemClickListener?.onItemClick(selectedPosition, position)
            // notifyDataSetChanged()
        }

        if (selectedPosition == responseContent.duration_period_id!!) {
            holder.durationCardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )
            holder.durationTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.durationCardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            holder.durationTextView.setTextColor(ContextCompat.getColor(context, R.color.navColor))
        }
    }

    override fun getItemCount(): Int {
        return durationArrayList?.size!!
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)
        internal val durationCardView: CardView = itemView.findViewById(R.id.durationCardView)
    }

    interface OnItemClickListener {
        fun onItemClick(
            pos: Int, selectedPosition: Int
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun updateSelectStatus(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun setFocus() {

    }
}
