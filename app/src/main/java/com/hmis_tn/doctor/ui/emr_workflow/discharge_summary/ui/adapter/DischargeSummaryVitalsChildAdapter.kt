package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.PV
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.discharge_child_recycler_list.view.*
import kotlinx.android.synthetic.main.discharge_vitals_child_recycler_list.view.*


class DischargeSummaryVitalsChildAdapter :
    RecyclerView.Adapter<DischargeSummaryVitalsChildAdapter.LabHolder>() {

    private var vitalList: ArrayList<PV> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        i: Int
    ): LabHolder {
        val itemLayout = LayoutInflater.from(parent.context).inflate(
            R.layout.discharge_vitals_child_recycler_list,
            parent,
            false
        ) as ConstraintLayout
        return LabHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: LabHolder, position: Int) {
        holder.bind(vitalList[position])
    }

    inner class LabHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(vitalDetail: PV) {
            if (adapterPosition % 2 == 0)
                itemView.llchildLabMainLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            vitalDetail.also { vital ->
                itemView.apply {
                    if (vital !== null) {
                        tvVitalChildSNo.text = (adapterPosition + 1).toString()
                        val utils = Utils(itemView.context)
                        val date = utils.displayDate(
                            vital.vital_performed_date,
                            "yyyy-MM-dd HH:mm:ss"
                        )
                        tvVitalChildDate.text = date
                        tvVitalChildName.text =
                            vital.vital_name
                        tvVitalChildValue.text =
                            vital.vital_value
                        tvVitalChildUOM.text =
                            vital.uom_name
                    }


                }
            }
        }
    }

    override fun getItemCount(): Int = vitalList.size

    fun setData(labList: List<PV>) {
        this.vitalList = labList as ArrayList<PV>
        notifyDataSetChanged()
    }
}
