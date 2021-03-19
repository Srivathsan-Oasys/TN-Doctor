package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.ui

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.oasys.digihealth.doctor.R


class PrevLmisChildAdapterAdapter(
    context: Context,
    private val childList: List<com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.PodArrResult>
) : RecyclerView.Adapter<PrevLmisChildAdapterAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNumber: TextView
        var textName: TextView
        var codeTextView: TextView
        var type: TextView
        var orderToLocation: TextView
        var mainLinearLayout: LinearLayout


        init {
            serialNumber = view.findViewById<View>(R.id.serialNumber) as TextView
            type = view.findViewById<View>(R.id.type) as TextView
            textName = view.findViewById<View>(R.id.textName) as TextView
            codeTextView = view.findViewById<View>(R.id.codeTextView) as TextView
            orderToLocation = view.findViewById<View>(R.id.orderToLocation) as TextView
            mainLinearLayout = view.findViewById<View>(R.id.mainLinearLayout) as LinearLayout

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.lmis_prev_child_recycler_list, parent, false) as ConstraintLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = childList[position].toString()
        val podList = childList[position]
        holder.serialNumber.text = (position + 1).toString()
        if (childList.get(position).test_master_uuid == null) {
            holder.textName.text = podList.profile_name
            holder.codeTextView.text = podList.profile_code
        } else {
            holder.textName.text = podList.name
            holder.codeTextView.text = podList.code
        }
        holder.type.text = podList.type

        holder.orderToLocation.text = podList.order_to_location

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
        return childList.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}