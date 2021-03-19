package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.VitalDetail
import com.oasys.digihealth.doctor.utils.Utils

import kotlinx.android.synthetic.main.row_discharge_summary_vital.view.*

typealias OnDeleteVitalListener = (vitalDetails: VitalDetail) -> Unit

class DischargeSummaryVitalTypeAdapter(var onDeleteVitalListener: OnDeleteVitalListener) :
    RecyclerView.Adapter<DischargeSummaryVitalTypeAdapter.VitalHolder>() {

    private var vitalList = ArrayList<VitalDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VitalHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_discharge_summary_vital, parent, false)
        return VitalHolder(view)
    }

    override fun getItemCount(): Int = vitalList.size

    override fun onBindViewHolder(holder: VitalHolder, position: Int) {
        holder.bind(vitalList[position], position)
    }

    fun setData(vitalList: ArrayList<VitalDetail>) {
        this.vitalList = vitalList
        notifyDataSetChanged()
    }

    inner class VitalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            vitalDetails: VitalDetail,
            position: Int
        ) {
            if (position % 2 == 0)
                itemView.llVitalMainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )

            vitalDetails.also { vital ->
                itemView.apply {
                    tvVitalSno.text = (position + 1).toString()
                    val utils = Utils(itemView.context)
                    val date = utils.displayDate(vital.created_date, "yyyy-MM-dd HH:mm:ss")
                    tvVitalDate.text = date
                    tvVitalDepartment.text = vital.department_name
                    tvVitalType.text = vital.encounter_type_name
                    tvVitalGivenBy.text = "${vital.doctor_title}." + vital.doctor_firstname
                    tvVitalInstitution.text = vital.institution_name

                    val vitalsChildAdapter = DischargeSummaryVitalsChildAdapter()
                    val itemDecor =
                        DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL)
                    rvVitalList.addItemDecoration(itemDecor)
                    rvVitalList.adapter = vitalsChildAdapter
                    vitalsChildAdapter.setData(vitalList[position].PV_list)

                    ivVitalDelete.setOnClickListener {
                        vitalList.remove(vital)
                        onDeleteVitalListener.invoke(vital)
                        notifyDataSetChanged()
                    }
                    ivVitalInfo.setOnClickListener {
                        previewVitalLinearLayout.performClick()
                    }
                    previewVitalLinearLayout.setOnClickListener {
                        if (resultVitalsLinearLayout.visibility == View.VISIBLE) {
                            ivVitalInfo.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
                            resultVitalsLinearLayout.visibility = View.GONE
                        } else {
                            ivVitalInfo.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
                            resultVitalsLinearLayout.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}