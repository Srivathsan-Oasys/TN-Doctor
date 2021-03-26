package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.config.CriticalCareChartFilterHeading
import kotlinx.android.synthetic.main.item_critical_care_chart_heading.view.*

class CriticalCareChartHeadingAdapter(
    private val list: ArrayList<CriticalCareChartFilterHeading>,
    private val clickListener: (heading: CriticalCareChartFilterHeading) -> Unit
) :
    RecyclerView.Adapter<CriticalCareChartHeadingAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_critical_care_chart_heading, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val heading = list[position]

        holder.itemView.tvHeading.text = heading.cc_type_name

        if (heading.isSelected) {
            holder.itemView.tvHeading.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorPrimary
                )
            )
            holder.itemView.viewSelected.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorPrimary
                )
            )
        } else {
            holder.itemView.tvHeading.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
            holder.itemView.viewSelected.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }

        holder.itemView.cvHeading.setOnClickListener {
            clickListener(heading)
            notifyDataSetChanged()
        }
    }
}
