package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.previous_model.Ds
import kotlinx.android.synthetic.main.row_discharge_summary_previous.view.*

typealias OnDSPreviousEditListener = (dSList: Ds) -> Unit
typealias OnDSPreviousPrintListener = (dSList: Ds) -> Unit

class DischargeSummaryPreviousAdapter(
    var onDSPreviousListener: OnDSPreviousEditListener,
    var onDSPreviousPrintListener: OnDSPreviousPrintListener
) :
    RecyclerView.Adapter<DischargeSummaryPreviousAdapter.DSPreviousHolder>() {
    private var previousDSList = ArrayList<Ds>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DischargeSummaryPreviousAdapter.DSPreviousHolder {
        val binding = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_discharge_summary_previous, parent, false)
        return DSPreviousHolder(binding.rootView)
    }

    override fun getItemCount(): Int = previousDSList.size

    override fun onBindViewHolder(
        holder: DischargeSummaryPreviousAdapter.DSPreviousHolder,
        position: Int
    ) {
        holder.bind(previousDSList[position], position)
    }

    fun setData(previousDSList: List<Ds>) {
        this.previousDSList = previousDSList as ArrayList<Ds>
        notifyDataSetChanged()
    }

    inner class DSPreviousHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dSList: Ds, position: Int) {
            if (position % 2 == 0)
                itemView.mainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )

            dSList.also { previousDischargeSummary ->
                itemView.apply {
                    tvDSPreviousSno.text = (position + 1).toString()
                    tvDSPreviousDate.text = previousDischargeSummary.ds_details.generated_date
                    tvDSPreviousDoctorName.text = previousDischargeSummary.ds_details.dctor_name
                    tvDSPreviousNurseName.text = previousDischargeSummary.ds_details.nurse_name

                    ivDSPreviousEdit.setOnClickListener {
                        onDSPreviousListener.invoke(previousDischargeSummary)
                    }
                    ivDSPreviousPrint.setOnClickListener {
                        onDSPreviousPrintListener.invoke(previousDischargeSummary)
                    }
                }

            }


        }
    }
}