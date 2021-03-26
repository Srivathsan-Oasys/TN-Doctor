package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.ChiefComplaintDetails
import kotlinx.android.synthetic.main.discharge_chief_child_recycler_list.view.*
import kotlinx.android.synthetic.main.discharge_child_recycler_list.view.llchildLabMainLayout


class DischargeSummaryChiefComplaintChildAdapter :
    RecyclerView.Adapter<DischargeSummaryChiefComplaintChildAdapter.LabHolder>() {

    private var chiefComplaintDetailsList: ArrayList<ChiefComplaintDetails> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): LabHolder {
        val binding = LayoutInflater.from(parent.context)
            .inflate(R.layout.discharge_chief_child_recycler_list, parent, false)
        return LabHolder(binding.rootView)
    }

    override fun getItemCount(): Int = chiefComplaintDetailsList.size

    override fun onBindViewHolder(holder: LabHolder, position: Int) {
        holder.bind(chiefComplaintDetailsList[position])
    }

    fun setData(chiefComplaintDetailsList: List<ChiefComplaintDetails>) {
        this.chiefComplaintDetailsList =
            chiefComplaintDetailsList as ArrayList<ChiefComplaintDetails>
        notifyDataSetChanged()
    }

    inner class LabHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            chiefComplaintDetail: ChiefComplaintDetails
        ) {
            if (adapterPosition % 2 == 0)
                itemView.llchildLabMainLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            chiefComplaintDetail.also { chiefComplaint ->
                itemView.sNoTextViews.text = (adapterPosition + 1).toString()
                itemView.othersTextViews.text =
                    chiefComplaint.cheif_complaint_name
                itemView.durationViews.text =
                    chiefComplaint.chief_complaint_duration.toString() + " "
                chiefComplaint.chief_complaint_duration_period_name

            }
        }
    }

}
