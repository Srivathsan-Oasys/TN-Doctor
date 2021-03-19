package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.isTablet
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.PodArrResult
import kotlinx.android.synthetic.main.prev_radiology_child_recycler_list.view.*


class PrevRadiologyChildAdapter(
    private val mContext: Context,
    private var pod_Result: List<PodArrResult>?

) : RecyclerView.Adapter<PrevRadiologyChildAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater


    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)

    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mainLinearLayout: ConstraintLayout


        init {


            mainLinearLayout = view.findViewById<View>(R.id.mainLinearLayout) as ConstraintLayout

        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.prev_radiology_child_recycler_list,
            viewGroup,
            false
        ) as ConstraintLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prevList = pod_Result?.get(position)
        var pos = position + 1
        holder.itemView.sNoTextView.text = pos.toString()
        if (isTablet(mContext)) {
            holder.itemView.nameTextView.text = prevList?.name
            holder.itemView.codeTextView.text = prevList?.code
            holder.itemView.orederToLocation.text = prevList?.order_to_location
            holder.itemView.typeTextView.text = prevList?.type.toString()
        } else {
            holder.itemView.textNames.text = prevList?.code + "-" + prevList?.name
            holder.itemView.codesTextView.text = prevList?.type + "-" + prevList?.order_to_location

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
        return pod_Result?.size!!
    }
}
