package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response.PrevInvestigationDetail
import kotlinx.android.synthetic.main.item_prev_treatment_kit_child.view.*

class PrevTreatmentKitInvestigationAdapter(
    private val list: ArrayList<PrevInvestigationDetail>
) : RecyclerView.Adapter<PrevTreatmentKitInvestigationAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prev_treatment_kit_child, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prevInvestigationDetail = list[position]
        var name = ""
        name = prevInvestigationDetail.lab_name ?: prevInvestigationDetail.profile_master_name ?: ""
        holder.itemView.tvName.text = "$name - ${prevInvestigationDetail.prority_name}"
        holder.itemView.tvDetail.text = prevInvestigationDetail.location_name
    }

    override fun getItemCount(): Int {
        return list.size
    }
}