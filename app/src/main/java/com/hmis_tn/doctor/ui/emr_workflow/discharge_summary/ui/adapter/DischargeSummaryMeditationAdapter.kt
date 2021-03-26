package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.DischargeMedicationDetail
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.PrescriptionDetailXX
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.row_discharge_summary_meditation.view.*


typealias OnDeleteMeditationListener = (medicationDetails: DischargeMedicationDetail) -> Unit

class DischargeSummaryMeditationAdapter(
    private val mContext: Context,
    var OnDeleteMeditationListener: OnDeleteMeditationListener
) :
    RecyclerView.Adapter<DischargeSummaryMeditationAdapter.MeditationtHolder>() {

    private var meditationList = ArrayList<DischargeMedicationDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeditationtHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_discharge_summary_meditation, parent, false)
        return MeditationtHolder(view)
    }

    override fun getItemCount(): Int = meditationList.size

    override fun onBindViewHolder(holder: MeditationtHolder, position: Int) {
        holder.bind(meditationList[position], position)
    }

    fun setData(medicationDetailsList: List<DischargeMedicationDetail>) {
        this.meditationList = medicationDetailsList as ArrayList<DischargeMedicationDetail>
        notifyDataSetChanged()
    }

    inner class MeditationtHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            medicationDetails: DischargeMedicationDetail,
            position: Int
        ) {
            if (position % 2 == 0)
                itemView.llMeditationMainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            medicationDetails.also { medicationDetail ->

                try {
                    itemView.apply {
                        tvMeditationSno.text = (position + 1).toString()
                        val date = Utils(itemView.context).displayDate(
                            medicationDetail.prescription_date, "yyyy-MM-dd HH:mm:ss"
                        )
                        tvMeditationDate.text = date
                        tvMeditationDepartment.text = medicationDetail.department_name
                        tvMeditationType.text = medicationDetail.encounter_type_name
                        tvMeditationPrescribedBy.text = medicationDetail.priscribedDoctor_name
                        tvMeditationInstitution.text = medicationDetail.facility_name

                        val myOrderChildAdapter = DischargeSummaryMeditationChildAdapter()
                        val itemDecor =
                            DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
                        rvMeditation.addItemDecoration(itemDecor)
                        rvMeditation.adapter = myOrderChildAdapter
                        myOrderChildAdapter.setData(meditationList.get(position).prescription_details as ArrayList<PrescriptionDetailXX>)
                        previewMeditationLinearLayout.setOnClickListener {

                            if (resultMeditationLinearLayout.visibility == View.VISIBLE) {
                                resultMeditationLinearLayout.visibility = View.GONE

                            } else {
                                resultMeditationLinearLayout.visibility = View.VISIBLE
                            }
                        }


                        ivMeditationDelete.setOnClickListener {
                            OnDeleteMeditationListener.invoke(medicationDetail)
                            meditationList.remove(medicationDetail)
                            notifyDataSetChanged()
                        }
                        ivMeditationInfo.setOnClickListener {
                            previewMeditationLinearLayout.performClick()
                        }
                    }


                } catch (e: Exception) {

                }
            }
        }
    }
}