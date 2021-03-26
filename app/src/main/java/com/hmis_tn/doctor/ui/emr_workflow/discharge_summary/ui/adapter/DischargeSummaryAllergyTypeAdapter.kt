package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R

import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.AllergyDetail
import com.hmis_tn.doctor.utils.Utils

import kotlinx.android.synthetic.main.row_summary_discharge_allergy_type.view.*

typealias OnClickDeleteListener = (allergyDetailsList: AllergyDetail) -> Unit

class DischargeSummaryAllergyTypeAdapter(var onClickDeleteListener: OnClickDeleteListener) :
    RecyclerView.Adapter<DischargeSummaryAllergyTypeAdapter.DSAllergyTypeHolder>() {

    private var allergyList: ArrayList<AllergyDetail> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DSAllergyTypeHolder {
        val binding = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_summary_discharge_allergy_type, parent, false)
        return DSAllergyTypeHolder(binding.rootView)
    }

    override fun getItemCount(): Int = allergyList.size

    override fun onBindViewHolder(
        holder: DSAllergyTypeHolder,
        position: Int
    ) {
        holder.bind(allergyList[position], position)
    }

    inner class DSAllergyTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            allergyDetails: AllergyDetail,
            position: Int
        ) {
            if (adapterPosition % 2 == 0)
                itemView.llAllergyMainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            allergyDetails.also { allergy ->
                itemView.apply {
                    tvAllergySno.text = (position + 1).toString()
                    val date = Utils(itemView.context).displayDate(
                        allergy.date,
                        "yyyy-MM-dd HH:mm:ss"
                    )
                    tvAllergyDate.text = date
                    tvAllergyType.text = allergy.allergy_type_name
                    tvAllergy.text = allergy.allergy_name
                    tvAllergySource.text = allergy.allergy_source_name
                    tvAllergyDestination.text =
                        allergy.allergy_duration.toString() + allergy.periods_name
                    tvAllergySeverity.text = allergy.allergy_severity_name
                    ivAllergyDelete.setOnClickListener {
                        // Interface method call from adapter to view
                        onClickDeleteListener.invoke(allergy)
                        // Locally remove from adapter and refresh it...
                        allergyList.remove(allergy)
                        notifyDataSetChanged()
                    }
                    //Noy available info for Allergy
                    ivAllergyInfo.visibility = View.GONE
                    ivAllergyInfo.setOnClickListener {
                        //TODO
                    }
                }
            }

        }
    }

    fun setData(allergyList: List<AllergyDetail>) {
        this.allergyList = allergyList as ArrayList
        notifyDataSetChanged()
    }
}