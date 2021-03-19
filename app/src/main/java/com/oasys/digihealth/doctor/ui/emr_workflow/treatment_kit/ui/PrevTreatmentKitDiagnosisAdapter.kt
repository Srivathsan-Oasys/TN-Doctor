package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.Diagnosi
import kotlinx.android.synthetic.main.item_prev_treatment_kit_child.view.*

class PrevTreatmentKitDiagnosisAdapter(
    private val list: ArrayList<Diagnosi>
) : RecyclerView.Adapter<PrevTreatmentKitDiagnosisAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prev_treatment_kit_child, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val diagnosi = list[position]
        holder.itemView.tvName.text = diagnosi.diagnosis_code
        holder.itemView.tvDetail.text = diagnosi.diagnosis_name
    }

    override fun getItemCount(): Int {
        return list.size
    }
}