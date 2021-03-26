package com.hmis_tn.doctor.ui.emr_workflow.vitals.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.vitals.model.previous_vitals.PPV
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.item_previous_vitals.view.*

class PreviousVitalsAdapter(
    private val list: ArrayList<PPV>
) : RecyclerView.Adapter<PreviousVitalsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_previous_vitals, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ppv = list[position]

        val utils = Utils(holder.itemView.context)
        val title = "${utils.displayDate(ppv.created_date!!, "yyyy-MM-dd HH:mm:ss")}-" +
                "${ppv.salutaion_name}${ppv.created_by_firstname}-${ppv.encounter_type_name}"
        holder.itemView.tvTitle.text = title

        holder.itemView.rvPreviousVitalsChild.layoutManager =
            LinearLayoutManager(holder.itemView.context)
        holder.itemView.rvPreviousVitalsChild.adapter =
            ppv.PV_list?.let { PreviousVitalsChildAdapter(it) }

        holder.itemView.tvTitle.setOnClickListener {
            if (holder.itemView.llTableHeading.visibility == View.VISIBLE) {
                holder.itemView.llTableHeading.visibility = View.GONE
                holder.itemView.rvPreviousVitalsChild.visibility = View.GONE
            } else {
                holder.itemView.llTableHeading.visibility = View.VISIBLE
                holder.itemView.rvPreviousVitalsChild.visibility = View.VISIBLE
            }
        }

        if (position == list.size - 1) {
            holder.itemView.llTableHeading.visibility = View.VISIBLE
            holder.itemView.rvPreviousVitalsChild.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
