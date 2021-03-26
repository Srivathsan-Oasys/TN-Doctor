package com.hmis_tn.doctor.ui.emr_workflow.history.familyhistory.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.history.allergy.model.response.period.PeriodresponseContent

class DurationFamilyHistoryAdapter(
    private val context: Context,
    private var durationArrayList: List<PeriodresponseContent?>?
) : RecyclerView.Adapter<DurationFamilyHistoryAdapter.MyViewHolder>() {
    //private var onItemClickListener: OnItemClickListener? = null
    var selectedPosition = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_allergy_duration, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseContent = durationArrayList?.get(position)!!
        holder.durationTextView.text = responseContent.name?.get(0).toString()
        holder.durationCardView.setOnClickListener {
            selectedPosition = responseContent.uuid!!
            //onItemClickListener?.onItemClick(selectedPosition)

            Log.e("durationSelect", selectedPosition.toString())

            // Toast.makeText(context,holder.durationTextView.text.toString(),Toast.LENGTH_LONG).show()
            notifyDataSetChanged()
        }

        if (selectedPosition == position + 1) {
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

    fun setDurationData(responseContent: List<PeriodresponseContent?>?) {

        selectedPosition = 0
        durationArrayList = responseContent

        notifyDataSetChanged()
    }


    fun selectDurationPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun selectPositionValidation(): Int {
        return selectedPosition
    }
}


