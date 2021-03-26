package com.hmis_tn.doctor.ui.emr_workflow.history.prescription.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.history.prescription.model.PrescriptionDetail

class HistoryPrescriptionChildAdapter(
    private val context: Context,
    private val historyPrescriptionArrayList: List<PrescriptionDetail?>?
) :
    RecyclerView.Adapter<HistoryPrescriptionChildAdapter.myViewHolder>() {
    private val mLayoutInflater: LayoutInflater


    init {
        this.mLayoutInflater = LayoutInflater.from(context)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemLayout = LayoutInflater.from(context).inflate(
            R.layout.row_child_history_prescription,
            parent,
            false
        ) as LinearLayout
        return myViewHolder(itemLayout)
    }

    class myViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sNoTextView: TextView
        var drugNameTextView: TextView
        var routeTextView: TextView
        var frequencyText: TextView
        var durationText: TextView
        var instructionTextView: TextView


        init {

            sNoTextView = itemView.findViewById<View>(R.id.sNoTextView) as TextView
            drugNameTextView = itemView.findViewById<View>(R.id.drugNameTextView) as TextView
            routeTextView = itemView.findViewById(R.id.routeTextView)
            frequencyText = itemView.findViewById(R.id.frequencyTextView)
            durationText = itemView.findViewById(R.id.durationTextView)
            instructionTextView = itemView.findViewById(R.id.instructionTextView)


        }


    }


    override fun getItemCount(): Int {
        return historyPrescriptionArrayList?.size!!

    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val prevList = historyPrescriptionArrayList?.get(position)
        val pos = position + 1
        holder.sNoTextView.text = pos.toString()
        holder.drugNameTextView.text = prevList?.item_master?.name
        holder.routeTextView.text = prevList?.drug_route?.name
        holder.frequencyText.text = prevList?.drug_frequency?.name
        holder.durationText.text =
            prevList?.duration.toString().toInt().toString() + prevList?.duration_period?.code
        holder.instructionTextView.text = prevList?.drug_instruction?.name


    }

}