package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrescriptionDetail


class PrevPrescriptionChildAadpter(
    private val mContext: Context,
    private var prev_Result: List<PrescriptionDetail?>?
) : RecyclerView.Adapter<PrevPrescriptionChildAadpter.myViewHolder>() {
    private val mLayoutInflater: LayoutInflater

    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.prev_previous_child_list,
            parent,
            false
        ) as LinearLayout
        return myViewHolder(itemLayout)
    }

    override fun getItemCount(): Int {
        return prev_Result?.size!!

    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val prevList = prev_Result?.get(position)
        val pos = position + 1
        holder.sNoTextView.text = pos.toString()
        holder.nameTextView.text = prevList?.item_master?.name
        holder.routeTextView.text = prevList?.drug_route?.name
        holder.strengthTextView.text = prevList?.item_master?.strength
        holder.quantityTextView.text = prevList?.prescribed_quantity?.toString() ?: ""
        holder.frequencyText.text = prevList?.drug_frequency?.name
        holder.durationText.text = prevList?.duration_period?.code
        holder.instrationText.text = prevList?.drug_instruction?.name

    }

    class myViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sNoTextView: TextView
        var nameTextView: TextView
        var strengthTextView: TextView
        var quantityTextView: TextView
        var routeTextView: TextView
        var frequencyText: TextView
        var durationText: TextView
        var instrationText: TextView

        init {

            sNoTextView = view.findViewById<View>(R.id.sNoTextView) as TextView
            nameTextView = view.findViewById<View>(R.id.nameTextView) as TextView
            strengthTextView = view.findViewById<View>(R.id.strengthTextView) as TextView
            quantityTextView = view.findViewById<View>(R.id.quantityTextView) as TextView
            routeTextView = view.findViewById(R.id.routeTextView)
            frequencyText = view.findViewById(R.id.frequencyTextView)
            durationText = view.findViewById(R.id.durationTextView)

            instrationText = view.findViewById(R.id.instructionTextView)
        }


    }

}