package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.PreDiagnosisResponseContent
import com.oasys.digihealth.doctor.utils.Utils
import kotlinx.android.synthetic.main.item_lab_details.view.*


class PrevDiagnosisMobileAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<PrevDiagnosisMobileAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var prevDiagnosis_Result: List<PreDiagnosisResponseContent>? = ArrayList()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.prev_diagnosis_recycler_list, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prevList = prevDiagnosis_Result?.get(position)

        holder.itemView.tv_no.text = (position + 1).toString()
        val correctformat =
            Utils.parseDate(prevList?.created_date!!, "yyyy-MM-dd HH:mm", "dd-MMM-yyyy hh:mm a")

        if (prevList.is_snomed) {
            holder.itemView.tv_title.text = ("Snomed  - " + prevList.other_diagnosis)
            holder.itemView.tv_sub_menu.text = correctformat
        } else {
            holder.itemView.tv_title.text = ("ICD 10" + " - " + prevList.diagnosis.code)
            holder.itemView.tv_sub_menu.text = prevList.diagnosis.name + " - " + correctformat
        }
    }

    override fun getItemCount(): Int {
        return prevDiagnosis_Result?.size!!
    }

    fun refreshList(preLabArrayList: List<PreDiagnosisResponseContent>?) {
        this.prevDiagnosis_Result = preLabArrayList!!
        this.notifyDataSetChanged()
    }
}
