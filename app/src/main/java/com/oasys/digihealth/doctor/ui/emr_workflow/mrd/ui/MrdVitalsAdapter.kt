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
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.VitalDetail


class MrdVitalsAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<MrdVitalsAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String
    private var PatientList: List<VitalDetail?>? = ArrayList()


    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)


    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNum: TextView
        var dateText: TextView
        var typeText: TextView
        var depatmentText: TextView
        var givenByText: TextView
        var institutionText: TextView


        val mainLinearLayout: LinearLayout


        init {
            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            dateText = view.findViewById<View>(R.id.dateText) as TextView
            typeText = view.findViewById<View>(R.id.typeText) as TextView
            depatmentText = view.findViewById<View>(R.id.depatmentText) as TextView
            givenByText = view.findViewById<View>(R.id.givenByText) as TextView
            institutionText = view.findViewById<View>(R.id.institutionText) as TextView



            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.mrd_vitals_recycler_list,
            parent,
            false
        ) as LinearLayout
        val recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = PatientList?.get(position).toString()
        val prevList = PatientList?.get(position)
        holder.serialNum.text = (position + 1).toString()
        holder.dateText.text = prevList?.created_date
        holder.typeText.text = prevList?.encounter_type_name
        holder.depatmentText.text = prevList?.department_name
        holder.givenByText.text = prevList?.doctor_firstname
/*
        holder.institutionText.text=prevList?.fa
*/


        // holder.dateText.text =prevList?.discharge_result?.investigation?.investigation_details?.get(0)?.created_date
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

    fun refreshList(preLabArrayList: List<VitalDetail?>) {
        this.PatientList = preLabArrayList
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return PatientList?.size!!
    }


}
