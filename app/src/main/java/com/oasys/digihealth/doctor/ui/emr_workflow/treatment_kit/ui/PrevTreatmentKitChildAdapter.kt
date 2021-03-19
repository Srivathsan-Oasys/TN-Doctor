package com.oasys.digihealth.doctor.ui.emr_workflow.view.lab.ui

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.PodArrResult


class PrevTreatmentKitChildAdapter(
    private val mContext: Context,
    private var pod_Result: List<PodArrResult>?

) : RecyclerView.Adapter<PrevTreatmentKitChildAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater


    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)

    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sNoTextView: TextView
        var nameTextView: TextView
        var codeTextView: TextView
        var typeTextView: TextView
        var orderToLocation: TextView


        init {

            sNoTextView = view.findViewById<View>(R.id.sNoTextView) as TextView
            nameTextView = view.findViewById<View>(R.id.nameTextView) as TextView
            codeTextView = view.findViewById<View>(R.id.codeTextView) as TextView
            typeTextView = view.findViewById<View>(R.id.typeTextView) as TextView
            orderToLocation = view.findViewById<View>(R.id.orederToLocation) as TextView

        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.treatment_kit_child_recycler_list,
            viewGroup,
            false
        ) as ConstraintLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prevList = pod_Result?.get(position)
        var pos = position + 1
        holder.sNoTextView.text = pos.toString()
        holder.nameTextView.text = prevList?.name
        holder.codeTextView.text = prevList?.code
        holder.orderToLocation.text = prevList?.order_to_location
        holder.typeTextView.text = prevList?.type.toString()


    }

    override fun getItemCount(): Int {
        return pod_Result?.size!!
    }
}
