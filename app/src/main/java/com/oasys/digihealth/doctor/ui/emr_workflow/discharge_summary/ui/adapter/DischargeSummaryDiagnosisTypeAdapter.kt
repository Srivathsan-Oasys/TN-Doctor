package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.DiagnosisDetail
import com.oasys.digihealth.doctor.utils.Utils
import kotlinx.android.synthetic.main.row_discharge_summary_diagnosis.view.*

typealias OnDeleteDiagnosisListener = (diagnosisDetails: DiagnosisDetail) -> Unit

class DischargeSummaryDiagnosisTypeAdapter(var onDeleteDiagnosisListener: OnDeleteDiagnosisListener) :
    RecyclerView.Adapter<DischargeSummaryDiagnosisTypeAdapter.DiagnosisHolder>() {
    private var diagnosisList = ArrayList<DiagnosisDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosisHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_discharge_summary_diagnosis, parent, false)
        return DiagnosisHolder(view)
    }

    override fun getItemCount(): Int = diagnosisList.size

    override fun onBindViewHolder(holder: DiagnosisHolder, position: Int) {
        holder.bind(diagnosisList[position], position)
    }

    fun setData(diagnosisList: List<DiagnosisDetail>) {
        this.diagnosisList = diagnosisList as ArrayList<DiagnosisDetail>
        notifyDataSetChanged()
    }

    inner class DiagnosisHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            diagnosisDetails: DiagnosisDetail,
            position: Int
        ) {
            if (adapterPosition % 2 == 0)
                itemView.llDiagnosisMainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )

            diagnosisDetails.also { diagnosis ->
                itemView.apply {
                    tvDiagnosisSno.text = (position + 1).toString()
                    val date = Utils(itemView.context).displayDate(
                        diagnosis.performed_date,
                        "yyyy-MM-dd HH:mm:ss"
                    )
                    tvDiagnosisDate.text = date
                    tvDiagnosisType.text = diagnosis.encounter_type_name
                    tvDiagnosisName.text = diagnosis.diagnosis_name
                    tvDiagnosisTypeName.text = diagnosis.diagnosis_type
                    ivDiagnosisDelete.setOnClickListener {
                        onDeleteDiagnosisListener.invoke(diagnosis)
                        diagnosisList.remove(diagnosis)
                        notifyDataSetChanged()
                    }

                    ivDiagnosisInfo.visibility = View.GONE
                }
            }
        }
    }
}