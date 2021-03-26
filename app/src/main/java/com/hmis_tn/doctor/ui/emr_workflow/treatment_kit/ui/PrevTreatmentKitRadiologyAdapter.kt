package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.RadilogyDetail
import kotlinx.android.synthetic.main.item_prev_treatment_kit_child.view.*

class PrevTreatmentKitRadiologyAdapter(
    private val list: ArrayList<RadilogyDetail>
) : RecyclerView.Adapter<PrevTreatmentKitRadiologyAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prev_treatment_kit_child, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val radilogyDetail = list[position]
        var name = ""
        name = radilogyDetail.lab_name ?: radilogyDetail.profile_master_name ?: ""
        holder.itemView.tvName.text = "$name - ${radilogyDetail.prority_name}"
        holder.itemView.tvDetail.text = radilogyDetail.location_name
    }

    override fun getItemCount(): Int {
        return list.size
    }
}