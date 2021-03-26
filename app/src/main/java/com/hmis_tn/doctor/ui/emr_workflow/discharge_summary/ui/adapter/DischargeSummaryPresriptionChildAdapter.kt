package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.PrescriptionDetailX
import kotlinx.android.synthetic.main.discharge_child_recycler_list.view.llchildLabMainLayout
import kotlinx.android.synthetic.main.discharge_prescription_child_recycler_list.view.*


class DischargeSummaryPresriptionChildAdapter :
    RecyclerView.Adapter<DischargeSummaryPresriptionChildAdapter.PresriptionChildHolder>() {

    private var prescriptionDetailList: ArrayList<PrescriptionDetailX> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        i: Int
    ): PresriptionChildHolder {
        val itemLayout = LayoutInflater.from(parent.context).inflate(
            R.layout.discharge_prescription_child_recycler_list,
            parent,
            false
        ) as ConstraintLayout
        return PresriptionChildHolder(itemLayout.rootView)
    }

    override fun onBindViewHolder(holder: PresriptionChildHolder, position: Int) {
        holder.bind(prescriptionDetailList[position], position)
    }

    override fun getItemCount(): Int = prescriptionDetailList.size

    fun setData(prescriptionDetailList: List<PrescriptionDetailX>) {
        this.prescriptionDetailList = prescriptionDetailList as ArrayList<PrescriptionDetailX>
        notifyDataSetChanged()
    }

    inner class PresriptionChildHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            prescriptionDetail: PrescriptionDetailX,
            position: Int
        ) {
            if (adapterPosition % 2 == 0)
                itemView.llchildLabMainLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            prescriptionDetail.also { presDetail ->
                itemView.apply {
                    sNoTextView.text = (position + 1).toString()
                    presDetail.also {
                        drugNameTest.text = it.item_master.name
                        routeText.text = it.drug_route.code
                        frequencyText.text = it.drug_frequency.name
                        durationText.text = it.duration_period.name
                        instructionText.text = it.drug_instruction.name
                    }
                }
            }
        }
    }

}
