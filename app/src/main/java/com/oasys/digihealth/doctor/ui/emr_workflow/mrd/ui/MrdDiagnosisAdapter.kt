package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.ui

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.DiagnosisDetail


class MrdDiagnosisAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<MrdDiagnosisAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String
    private var PatientList: List<DiagnosisDetail?>? = ArrayList()


    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)


    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNum: TextView
        var typeText: TextView
        var Diagnosis: TextView
        var diagnosisType: TextView
        var dateText: TextView


        val mainLinearLayout: LinearLayout

        init {
            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            typeText = view.findViewById<View>(R.id.typeText) as TextView
            Diagnosis = view.findViewById<View>(R.id.Diagnosis) as TextView
            diagnosisType = view.findViewById<View>(R.id.diagnosisType) as TextView
            dateText = view.findViewById<View>(R.id.dateText) as TextView




            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.mrd_diagnosis_recycler_list,
            parent,
            false
        ) as LinearLayout
        val recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = PatientList?.get(position).toString()
        val prevList = PatientList?.get(position)
        prevList?.let { list ->
            if (position < PatientList?.size!!) {
                holder.serialNum.text = (position + 1).toString()
                holder.typeText.text = list.encounter_type_name
                holder.Diagnosis.text = list.diagnosis_name
                holder.diagnosisType.text = list.diagnosis_type
                holder.dateText.text = list.performed_date

            }
        }





        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        }


    }

    fun refreshList(preLabArrayList: List<DiagnosisDetail?>) {
        this.PatientList = preLabArrayList
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return PatientList?.size!!
    }


}
