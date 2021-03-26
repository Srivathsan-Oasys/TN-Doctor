package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.PatientOrderTestDetailX

import kotlinx.android.synthetic.main.discharge_child_recycler_list.view.*


class DischargeSummaryLabChildAdapter :
    RecyclerView.Adapter<DischargeSummaryLabChildAdapter.LabChildHolder>() {
    private var labDetailList: ArrayList<PatientOrderTestDetailX> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        i: Int
    ): LabChildHolder {
        val binding = LayoutInflater.from(parent.context).inflate(
            R.layout.discharge_child_recycler_list,
            parent,
            false
        )
        return LabChildHolder(binding.rootView)
    }


    override fun getItemCount(): Int {
        return labDetailList.size
    }

    override fun onBindViewHolder(holder: LabChildHolder, position: Int) {
        holder.bind(labDetailList[position], position)
    }

    fun setData(labDetailList: List<PatientOrderTestDetailX>) {
        this.labDetailList = labDetailList as ArrayList<PatientOrderTestDetailX>
        notifyDataSetChanged()
    }

    inner class LabChildHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            labDetails: PatientOrderTestDetailX,
            position: Int
        ) {
            try {
                if (position % 2 == 0)
                    itemView.llchildLabMainLayout?.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.alternateRow
                        )
                    )
                labDetails.also { lab ->
                    itemView.sNoLabTextVie.text = (position + 1).toString()
                    lab.also {
                        itemView.nameLabTextView.text =
                            it.test_master.name
                        itemView.codeLabTextView.text =
                            it.test_master.code
                        itemView.typeLabTextView.text =
                            it.order_priority.name
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
