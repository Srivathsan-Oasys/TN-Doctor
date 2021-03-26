package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.PrescriptionDetail
import com.hmis_tn.doctor.utils.Utils

import kotlinx.android.synthetic.main.row_discharge_summary_prescription.view.*
import java.util.*

typealias OnDeletePrescriptionListener = (prescriptionDetails: PrescriptionDetail) -> Unit

class DischargeSummaryPrescriptionTypeAdapter(val onDeletePrescriptionListener: OnDeletePrescriptionListener) :
    RecyclerView.Adapter<DischargeSummaryPrescriptionTypeAdapter.DSPrescriptionTypeHolder>() {
    private var prescriptionList: ArrayList<PrescriptionDetail> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DSPrescriptionTypeHolder {
        val binding = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_discharge_summary_prescription, parent, false)
        return DSPrescriptionTypeHolder(binding.rootView)
    }

    override fun getItemCount(): Int = prescriptionList.size

    override fun onBindViewHolder(
        holder: DSPrescriptionTypeHolder,
        position: Int
    ) {
        holder.bind(prescriptionList[position], position)
    }

    fun setData(prescriptionList: List<PrescriptionDetail>) {
        this.prescriptionList = prescriptionList as ArrayList<PrescriptionDetail>
        notifyDataSetChanged()
    }

    inner class DSPrescriptionTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            prescriptionDetails: PrescriptionDetail,
            position: Int
        ) {
            if (position % 2 == 0)
                itemView.llPrescriptionMainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            prescriptionDetails.also { prescription ->
                itemView.apply {
                    tvPrescriptionSno.text = (position + 1).toString()
                    val date = Utils(itemView.context).displayDate(
                        prescription.prescription_date,
                        "yyyy-MM-dd HH:mm:ss"
                    )
                    tvPrescriptionDate.text = date
                    tvPrescriptionDepartment.text = prescription.department_name
                    tvPrescriptionType.text = prescription.encounter_doctor_uuid.toString()
                    tvPrescriptionInstitution.text = prescription.facility_name
                    tvPrescriptionPresBy.text = prescription.priscribedDoctor_name

                    val myOrderChildAdapter = DischargeSummaryPresriptionChildAdapter()
                    val itemDecor =
                        DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL)
                    rvPrescriptionList.addItemDecoration(itemDecor)
                    rvPrescriptionList.adapter = myOrderChildAdapter
                    myOrderChildAdapter.setData(prescriptionList[position].prescription_details)
                    previewPrescriptionLinearLayout.setOnClickListener {

                        if (resultPrescriptionLinearLayout.visibility == View.VISIBLE) {
                            ivPrescriptionInfo?.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
                            resultPrescriptionLinearLayout.visibility = View.GONE

                        } else {
                            ivPrescriptionInfo?.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
                            resultPrescriptionLinearLayout.visibility = View.VISIBLE
                        }
                    }
                    ivPrescriptionDelete.setOnClickListener {
                        prescriptionList.remove(prescription)
                        onDeletePrescriptionListener.invoke(prescription)
                        notifyDataSetChanged()
                    }
                    ivPrescriptionInfo.setOnClickListener {
                        previewPrescriptionLinearLayout.performClick()
                    }

                }
            }
        }
    }
}