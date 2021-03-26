package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.ChiefComplaintsDetails
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.row_discharge_summary_chief_complaint.view.*


typealias OnDeleteChiefListener = (chiefComplaintDetails: ChiefComplaintsDetails) -> Unit

class DischargeChiefComplaintAdapter(
    private val mContext: Context,
    var onDeleteComplaintsListener: OnDeleteChiefListener
) :
    RecyclerView.Adapter<DischargeChiefComplaintAdapter.ChiefComplaintHolder>() {

    private var chiefComplainList = ArrayList<ChiefComplaintsDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChiefComplaintHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_discharge_summary_chief_complaint, parent, false)
        return ChiefComplaintHolder(view)
    }

    override fun getItemCount(): Int = chiefComplainList.size

    override fun onBindViewHolder(holder: ChiefComplaintHolder, position: Int) {
        holder.bind(chiefComplainList[position], position)
    }

    fun setData(chiefComplainList: List<ChiefComplaintsDetails>) {
        this.chiefComplainList = chiefComplainList as ArrayList<ChiefComplaintsDetails>
        notifyDataSetChanged()
    }

    inner class ChiefComplaintHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            investigationDetails: ChiefComplaintsDetails,
            position: Int
        ) {
            if (position % 2 == 0)
                itemView.llChiefMainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            investigationDetails.also { chiefComplaint ->

                try {
                    itemView.apply {
                        tvChiefSno.text = (position + 1).toString()

                        val utils = Utils(itemView.context)
                        val date = utils.displayDate(
                            chiefComplaint.chief_complaint_details.get(position).cheif_complaint_performed_date
                                ?: "", "yyyy-MM-dd HH:mm:ss"
                        )
                        tvChiefDate.text = date
                        tvChiefDepartment.text = chiefComplaint.department
                        tvChiefType.text = chiefComplaint.encounter_type_name
                        tvChiefPrescribedBy.text =
                            chiefComplaint.performed_by_title + "" + chiefComplaint.performed_by_first_name
                        tvChiefInstitution.text = chiefComplaint.institution

                        val myOrderChildAdapter = DischargeSummaryChiefComplaintChildAdapter()
                        val itemDecor =
                            DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
                        rvChiefComplaintList.addItemDecoration(itemDecor)
                        rvChiefComplaintList.adapter = myOrderChildAdapter
                        myOrderChildAdapter.setData(chiefComplainList[position].chief_complaint_details)
                        previewChiefLinearLayout.setOnClickListener {

                            if (resultChiefLinearLayout.visibility == View.VISIBLE) {
                                ivChiefInfo.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
                                resultChiefLinearLayout.visibility = View.GONE

                            } else {
                                ivChiefInfo.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
                                resultChiefLinearLayout.visibility = View.VISIBLE
                            }
                        }


                        ivChiefDelete.setOnClickListener {
                            onDeleteComplaintsListener.invoke(chiefComplaint)
                            chiefComplainList.remove(chiefComplaint)
                            notifyDataSetChanged()
                        }
                        ivChiefInfo.setOnClickListener {
                            previewChiefLinearLayout.performClick()
                        }
                    }


                } catch (e: Exception) {

                }
            }
        }
    }
}