package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.PatientOrderTestDetail
import kotlinx.android.synthetic.main.discharge_child_recycler_list.view.llchildLabMainLayout
import kotlinx.android.synthetic.main.discharge_radiology_child_recycler_list.view.*


class DischargeSummaryRadiologyChildAdapter :
    RecyclerView.Adapter<DischargeSummaryRadiologyChildAdapter.RadiologyChildHolder>() {

    private var radiologyList: ArrayList<PatientOrderTestDetail> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, i: Int): RadiologyChildHolder {
        val binding = LayoutInflater.from(parent.context).inflate(
            R.layout.discharge_radiology_child_recycler_list, parent,
            false
        )
        return RadiologyChildHolder(binding.rootView)
    }

    override fun getItemCount(): Int = radiologyList.size

    override fun onBindViewHolder(holder: RadiologyChildHolder, position: Int) {
        holder.bind(radiologyList[position])
    }

    fun setData(radioLogyList: List<PatientOrderTestDetail>) {
        this.radiologyList = radioLogyList as ArrayList<PatientOrderTestDetail>
        notifyDataSetChanged()
    }

    inner class RadiologyChildHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            patientOrderTestDetail: PatientOrderTestDetail
        ) {

            if (adapterPosition % 2 == 0)
                itemView.llchildLabMainLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            patientOrderTestDetail.also { patientDetail ->
                itemView.sNoTextView.text = (adapterPosition + 1).toString()
                patientDetail.also {
                    itemView.nameTextView.text =
                        it.test_master.name
                    itemView.codeTextView.text =
                        it.test_master.code
                    itemView.typeTextView.text =
                        it.order_priority.name
                }
            }
        }
    }
}
