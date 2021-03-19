package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.PatientOrderTestDetailX


class MrdLabChildAdapter(
    private val mContext: Context,
    private var PatientList: List<PatientOrderTestDetailX?>? = ArrayList()


) : RecyclerView.Adapter<MrdLabChildAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String


    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)


    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNum: TextView
        var codeText: TextView
        var testNameText: TextView
        var tyepText: TextView


        val mainLinearLayout: ConstraintLayout


        init {
            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            codeText = view.findViewById<View>(R.id.codeText) as TextView
            testNameText = view.findViewById<View>(R.id.testNameText) as TextView
            tyepText = view.findViewById<View>(R.id.tyepText) as TextView


            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.mrd_lab_child_recycler_list,
            parent,
            false
        ) as ConstraintLayout
        val recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = PatientList?.get(position).toString()
        val prevList = PatientList?.get(position)
        prevList?.let { list ->
            if (position < PatientList?.size!!) {
                holder.serialNum.text = (position + 1).toString()
                holder.codeText.text = list.test_master.code
                holder.testNameText.text = list.test_master.name
                holder.tyepText.text = list.order_priority.name
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


    override fun getItemCount(): Int {
        return PatientList?.size!!
    }


}
