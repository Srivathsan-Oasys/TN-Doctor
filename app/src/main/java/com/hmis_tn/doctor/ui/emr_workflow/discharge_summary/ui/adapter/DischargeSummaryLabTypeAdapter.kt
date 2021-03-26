package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.LabDetail
import com.hmis_tn.doctor.utils.Utils

import kotlinx.android.synthetic.main.row_discharge_summary_lab.view.*

typealias OnDeleteLabListener = (labDetails: LabDetail) -> Unit

class DischargeSummaryLabTypeAdapter(val onDeleteLabListener: OnDeleteLabListener) :
    RecyclerView.Adapter<DischargeSummaryLabTypeAdapter.LabHolder>() {

    private var labList = ArrayList<LabDetail>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        i: Int
    ): DischargeSummaryLabTypeAdapter.LabHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_discharge_summary_lab, parent, false)
        return LabHolder(itemLayout)
    }

    override fun getItemCount(): Int {
        return labList.size
    }


    override fun onBindViewHolder(holder: LabHolder, position: Int) {
        holder.bind(labList[position], position)


    }

    fun setData(labList: List<LabDetail>) {
        this.labList = labList as ArrayList<LabDetail>
        notifyDataSetChanged()
    }

    inner class LabHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            labDetails: LabDetail,
            position: Int
        ) {
            if (adapterPosition % 2 == 0)
                itemView.llLabMainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            labDetails.also { lab ->
                itemView.apply {
                    tvLabSno.text = (position + 1).toString()
                    val date = Utils(itemView.context).displayDate(
                        lab.modified_date,
                        "yyyy-MM-dd HH:mm:ss"
                    )
                    tvLabDate.text = date
                    tvLabInstitution.text = lab.facility_name
                    tvLabDepartment.text =
                        lab.department_name
                    tvLabType.text = lab.encounter_type_name
                    tvLabGivenBy.text = lab.vw_user_info.first_name
                    val myOrderChildAdapter = DischargeSummaryLabChildAdapter()
                    val itemDecor =
                        DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL)
                    rvLabList.addItemDecoration(itemDecor)
                    rvLabList.adapter = myOrderChildAdapter
                    myOrderChildAdapter.setData(labList[position].patient_order_test_details)
                    previewLinearLayout.setOnClickListener {

                        if (resultLinearLayout.visibility == View.VISIBLE) {
                            ivLabInfo.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
                            resultLinearLayout.visibility = View.GONE

                        } else {
                            ivLabInfo.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
                            resultLinearLayout.visibility = View.VISIBLE
                        }
                    }


                    ivLabDelete.setOnClickListener {
                        onDeleteLabListener.invoke(lab)
                        labList.remove(lab)

                        notifyDataSetChanged()
                    }
                    ivLabInfo.setOnClickListener {
                        previewLinearLayout.performClick()
                    }
                }
            }
        }
    }
}