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
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.AllergyDetail


class MrdAllergyAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<MrdAllergyAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String
    private var PatientList: List<AllergyDetail?>? = ArrayList()


    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)


    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNum: TextView
        var dateText: TextView
        var typeText: TextView
        var allergyText: TextView
        var sourceText: TextView
        var durationText: TextView
        var severtyText: TextView


        val mainLinearLayout: LinearLayout


        init {
            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            dateText = view.findViewById<View>(R.id.dateText) as TextView
            typeText = view.findViewById<View>(R.id.typeText) as TextView
            allergyText = view.findViewById<View>(R.id.allergyText) as TextView
            sourceText = view.findViewById<View>(R.id.sourceText) as TextView
            durationText = view.findViewById<View>(R.id.durationText) as TextView
            severtyText = view.findViewById<View>(R.id.severtyText) as TextView



            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.mrd_parent_list,
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
                holder.dateText.text = list.date
                holder.typeText.text = list.encounter_type_name
                holder.allergyText.text = list.allergy_name
                holder.sourceText.text = list.allergy_source_name
                holder.durationText.text = list.allergy_duration.toString()
                holder.severtyText.text = list.allergy_severity_name
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

    fun refreshList(preLabArrayList: List<AllergyDetail>?) {
        this.PatientList = preLabArrayList!!
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return PatientList?.size!!
    }


}





