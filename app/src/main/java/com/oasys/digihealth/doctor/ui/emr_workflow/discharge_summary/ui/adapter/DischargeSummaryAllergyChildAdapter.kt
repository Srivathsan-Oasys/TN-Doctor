package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.AllergyDetail
import kotlinx.android.synthetic.main.discharge_child_recycler_list.view.*


class DischargeSummaryAllergyChildAdapter :
    RecyclerView.Adapter<DischargeSummaryAllergyChildAdapter.AllergyChildHolder>() {

    private var labList: ArrayList<AllergyDetail> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        i: Int
    ): AllergyChildHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.discharge_child_recycler_list, parent, false)
        return AllergyChildHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: AllergyChildHolder, position: Int) {
        holder.bind(labList[position], position)
    }

    fun setData(labList: ArrayList<AllergyDetail>) {
        this.labList = labList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = labList.size

    inner class AllergyChildHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            labDetails: AllergyDetail,
            position: Int
        ) {
            if (adapterPosition % 2 == 0)
                itemView.llchildLabMainLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            labDetails.also { lab ->
                itemView.apply {

                    // sNoTextView.text = (position + 1).toString()
                    /*  nameTextView.text =
                          lab.patient_order_test_details?.get(position)?.test_master?.name
                      codeTextView.text =
                          lab.patient_order_test_details?.get(position)?.test_master?.code
                      typeTextView.text =
                          lab.patient_order_test_details?.get(position)?.order_priority?.name
*/

                }
            }
        }
    }

}
