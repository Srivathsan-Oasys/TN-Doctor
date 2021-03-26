package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.InvestigationDetails
import com.hmis_tn.doctor.utils.Utils


import kotlinx.android.synthetic.main.row_discharge_summary_investigation.view.*

typealias OnDeleteInvestigationListener = (investigationDetails: InvestigationDetails) -> Unit

class DischargeSummaryInvestigationTypeAdapter(
    private val mContext: Context,
    var onDeleteInvestigationListener: OnDeleteInvestigationListener
) :
    RecyclerView.Adapter<DischargeSummaryInvestigationTypeAdapter.InvestigationHolder>() {

    private var investigationList = ArrayList<InvestigationDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvestigationHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_discharge_summary_investigation, parent, false)
        return InvestigationHolder(view)
    }

    override fun getItemCount(): Int = investigationList.size

    override fun onBindViewHolder(holder: InvestigationHolder, position: Int) {
        holder.bind(investigationList[position], position)
    }

    fun setData(investigationList: List<InvestigationDetails>) {
        this.investigationList = investigationList as ArrayList<InvestigationDetails>
        notifyDataSetChanged()
    }

    inner class InvestigationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            investigationDetails: InvestigationDetails,
            position: Int
        ) {
            if (position % 2 == 0)
                itemView.llInvestigationMainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            investigationDetails.also { investigation ->
                itemView.apply {
                    tvInvestigationSno.text = (position + 1).toString()
                    val date = Utils(itemView.context).displayDate(
                        investigation.order_request_date ?: "",
                        "yyyy-MM-dd HH:mm:ss"
                    )
                    tvInvestigationDate.text = date
                    tvInvestigationDepartment.text = investigation.department_name
                    tvInvestigationType.text = investigation.encounter_type_name
                    tvInvestigationGivenBy.text = investigation.encounter_type_name
                    tvInvestigationInstitution.text = investigation.facility_name

                    val myOrderChildAdapter = DischargeSummaryInvestigationChildAdapter()
                    val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
                    rvInvestigationList.addItemDecoration(itemDecor)
                    rvInvestigationList.adapter = myOrderChildAdapter
                    myOrderChildAdapter.setData(investigationList[position].patient_order_test_details)
                    previewInvestigationLinearLayout.setOnClickListener {

                        if (resultInvestigationLinearLayout.visibility == View.VISIBLE) {
                            ivInvestigationInfo.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
                            resultInvestigationLinearLayout.visibility = View.GONE

                        } else {
                            ivInvestigationInfo.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
                            resultInvestigationLinearLayout.visibility = View.VISIBLE
                        }
                    }
                    ivInvestigationDelete.setOnClickListener {
                        onDeleteInvestigationListener.invoke(investigation)
                        investigationList.remove(investigation)
                        notifyDataSetChanged()
                    }
                    ivInvestigationInfo.setOnClickListener {
                        previewInvestigationLinearLayout.performClick()
                    }
                }
            }
        }
    }
}