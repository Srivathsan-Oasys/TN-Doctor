package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R

import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.PrescriptionDetailXX

import kotlinx.android.synthetic.main.discharge_meditation_child_recycler_list.view.*


class DischargeSummaryMeditationChildAdapter :
    RecyclerView.Adapter<DischargeSummaryMeditationChildAdapter.MeditationChildHolder>() {

    private var labList: ArrayList<PrescriptionDetailXX> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        i: Int
    ): MeditationChildHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.discharge_meditation_child_recycler_list, parent, false)
        return MeditationChildHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MeditationChildHolder, position: Int) {
        holder.bind(labList[position], position)
    }

    fun setData(labList: ArrayList<PrescriptionDetailXX>) {
        this.labList = labList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = labList.size

    inner class MeditationChildHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            labDetails: PrescriptionDetailXX,
            position: Int
        ) {
            if (adapterPosition % 2 == 0)
                itemView.llchildMeditationMainLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            labDetails.also { lab ->
                itemView.apply {

                    sNoMeditationTextVie.text = (position + 1).toString()
                    drugMeditationTextView.text =
                        lab.item_master.name

                    routeMeditationTextView.text =
                        lab.drug_route.name
                    FrequencyMeditationTextView.text =
                        lab.drug_frequency.name
                    durationMeditationTextView.text =
                        lab.duration_period.name
                    instructionMeditationTextView.text =
                        lab.drug_instruction.name


                }
            }
        }
    }

}
