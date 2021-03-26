package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.RadiologyDetail
import com.hmis_tn.doctor.utils.Utils

import kotlinx.android.synthetic.main.row_discharge_summary_radiology.view.*

typealias OnDeleteRadioLogyListener = (radiologyDetails: RadiologyDetail) -> Unit

class DischargeSummaryRadiologyTypeAdapter(var onDeleteRadioLogyListener: OnDeleteRadioLogyListener) :
    RecyclerView.Adapter<DischargeSummaryRadiologyTypeAdapter.RadioLogyHolder>() {
    private var radioLogyList = ArrayList<RadiologyDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioLogyHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_discharge_summary_radiology, parent, false)
        return RadioLogyHolder(view)
    }

    override fun getItemCount(): Int = radioLogyList.size

    override fun onBindViewHolder(holder: RadioLogyHolder, position: Int) {
        holder.bind(radioLogyList[position], position)
    }

    fun setData(radioLogyList: ArrayList<RadiologyDetail>) {
        this.radioLogyList = radioLogyList
        notifyDataSetChanged()
    }

    inner class RadioLogyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            radiologyDetails: RadiologyDetail,
            position: Int
        ) {
            if (position % 2 == 0)
                itemView.llRadiologyMainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )

            radiologyDetails.also { radiology ->
                itemView.apply {
                    tvRadiologySno.text = (position + 1).toString()
                    val date = Utils(itemView.context).displayDate(
                        radiology.modified_date,
                        "yyyy-MM-dd HH:mm:ss"
                    )
                    tvRadiologyDate.text = date
                    tvRadiologyType.text = radiology.encounter_type_name
                    tvRadiologyDepartment.text = radiology.department_name
                    tvRadiologyGivenBy.text = radiology.vw_user_info.first_name
                    tvRadiologyInstitution.text = radiology.facility_name
                    val myOrderChildAdapter = DischargeSummaryRadiologyChildAdapter()
                    val itemDecor =
                        DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL)
                    rvRadiologyList.addItemDecoration(itemDecor)
                    rvRadiologyList.adapter = myOrderChildAdapter
                    myOrderChildAdapter.setData(radioLogyList[position].patient_order_test_details)
                    previewRadiologyLinearLayout.setOnClickListener {

                        if (resultRadiologyLinearLayout.visibility == View.VISIBLE) {
                            ivRadiologyInfo.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
                            resultRadiologyLinearLayout.visibility = View.GONE

                        } else {
                            ivRadiologyInfo.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
                            resultRadiologyLinearLayout.visibility = View.VISIBLE
                        }
                    }

                    ivRadiologyDelete.setOnClickListener {
                        onDeleteRadioLogyListener.invoke(radiologyDetails)
                        radioLogyList.remove(radiology)
                        notifyDataSetChanged()
                    }
                    ivRadiologyInfo.setOnClickListener {
                        previewRadiologyLinearLayout.performClick()
                    }
                }
            }
        }
    }
}