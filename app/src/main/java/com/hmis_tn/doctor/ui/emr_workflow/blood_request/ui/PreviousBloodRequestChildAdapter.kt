package com.hmis_tn.doctor.ui.emr_workflow.blood_request.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.isTablet
import com.hmis_tn.doctor.ui.emr_workflow.blood_request.model.BloodRequestDetail

class PreviousBloodRequestChildAdapter(
    private val list: ArrayList<BloodRequestDetail>
) :
    RecyclerView.Adapter<PreviousBloodRequestChildAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvSNo: TextView = view.findViewById(R.id.tvSNo)
        var tvBloodComponent: TextView = view.findViewById(R.id.tvBloodComponent)
        var tvBloodUnits: TextView = view.findViewById(R.id.tvBloodUnits)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prev_blood_request_child, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bloodRequestDetail = list[position]
        holder.tvSNo.text = "${position + 1}"
        holder.tvBloodComponent.text = bloodRequestDetail.bloodComponent?.name
        if (isTablet(holder.itemView.context)) {
            holder.tvBloodUnits.text = bloodRequestDetail.quantityRequired?.toString()

            if (position % 2 == 1) {
                setContentBg(holder.tvSNo)
                setContentBg(holder.tvBloodComponent)
                setContentBg(holder.tvBloodUnits)
            } else {
                setHeaderBg(holder.tvSNo)
                setHeaderBg(holder.tvBloodComponent)
                setHeaderBg(holder.tvBloodUnits)
            }
        } else {
            holder.tvBloodUnits.text = "${bloodRequestDetail.quantityRequired?.toString()} Units"
        }
    }

    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.color.white)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.color.alternateRow)
    }
}