package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.PatientOrderTestDetails
import kotlinx.android.synthetic.main.discharge_child_recycler_list.view.llchildLabMainLayout
import kotlinx.android.synthetic.main.discharge_investigation_child_recycler_list.view.*

class DischargeSummaryInvestigationChildAdapter :
    RecyclerView.Adapter<DischargeSummaryInvestigationChildAdapter.LabHolder>() {

    private var investigationList: ArrayList<PatientOrderTestDetails> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): LabHolder {
        val binding = LayoutInflater.from(parent.context)
            .inflate(R.layout.discharge_investigation_child_recycler_list, parent, false)
        return LabHolder(binding.rootView)
    }

    override fun getItemCount(): Int = investigationList.size

    override fun onBindViewHolder(holder: LabHolder, position: Int) {
        holder.bind(investigationList[position])
    }

    fun setData(investigationList: List<PatientOrderTestDetails>) {
        this.investigationList = investigationList as ArrayList<PatientOrderTestDetails>
        notifyDataSetChanged()
    }

    inner class LabHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            patientOrderTestDetails: PatientOrderTestDetails
        ) {
            if (adapterPosition % 2 == 0)
                itemView.llchildLabMainLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            patientOrderTestDetails.also { patientDetail ->
                itemView.sNoTextViews.text = (adapterPosition + 1).toString()
                patientDetail.also {
                    itemView.nameTextViews.text =
                        it.test_master?.name
                    itemView.codeTextViews.text =
                        it.test_master?.code
                    itemView.typeTextViews.text =
                        it.order_priority?.name
                }
            }
        }
    }
}
