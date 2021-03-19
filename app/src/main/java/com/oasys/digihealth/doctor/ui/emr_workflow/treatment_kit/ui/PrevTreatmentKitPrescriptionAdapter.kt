package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response.DrugDetails
import kotlinx.android.synthetic.main.item_prev_treatment_kit_child.view.*

class PrevTreatmentKitPrescriptionAdapter(
    private val list: ArrayList<DrugDetails>
) : RecyclerView.Adapter<PrevTreatmentKitPrescriptionAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prev_treatment_kit_child, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val drugDetails = list[position]
        holder.itemView.tvName.text =
            drugDetails.drug_name + " - " + drugDetails.drug_route_name
        holder.itemView.tvDetail.text =
            drugDetails.drug_frequency_name + " - " + "${drugDetails.duration} ${drugDetails.drug_period_name} - ${drugDetails.prescribed_quantity} Pills - ${drugDetails.drug_instruction_name}"
    }

    override fun getItemCount(): Int {
        return list.size
    }
}