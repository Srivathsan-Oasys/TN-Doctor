package com.hmis_tn.doctor.ui.emr_workflow.investigation.ui

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import kotlinx.android.synthetic.main.row_lab.view.*

class PrevInvestigationMobileChildAdapter(
    private val mContext: Context,
    private var pod_Result: List<com.hmis_tn.doctor.ui.emr_workflow.investigation.model.PodArrResult>?

) : RecyclerView.Adapter<PrevInvestigationMobileChildAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater

    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)

    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
/*
        var sNoTextView: TextView
        var nameTextView: TextView
        var codeTextView: TextView
        var typeTextView: TextView
        var orderToLocation: TextView
        var mainLinearLayout: ConstraintLayout

*/

        init {

/*
            sNoTextView = view.findViewById<View>(R.id.sNoTextView) as TextView
            nameTextView = view.findViewById<View>(R.id.nameTextView) as TextView
            codeTextView = view.findViewById<View>(R.id.codeTextView) as TextView
            typeTextView = view.findViewById<View>(R.id.typeTextView) as TextView
            orderToLocation = view.findViewById<View>(R.id.orederToLocation) as TextView
            mainLinearLayout=view.findViewById<View>(R.id.mainLinearLayout)as ConstraintLayout
*/

        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.child_recycler_list,
            viewGroup,
            false
        ) as RelativeLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
/*
        val prevList = pod_Result?.get(position)
        var pos = position + 1
        holder.sNoTextView.text = pos.toString()
        holder.nameTextView.text = prevList?.name
        holder.codeTextView.text = prevList?.code
        holder.orderToLocation.text = prevList?.order_to_location as CharSequence?
        holder.typeTextView.text = prevList?.type.toString()

        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.alternateRow
                )
            )
        }
        else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        }

*/

        val prevList = pod_Result?.get(position)
        var pos = position + 1
        holder.itemView.tv_no.text = pos.toString()
        holder.itemView.tv_title.text = prevList?.code + "-" + prevList?.name
        holder.itemView.tv_sub_menu.text =
            prevList?.type.toString() + "-" + prevList?.order_to_location


    }

    override fun getItemCount(): Int {
        return pod_Result?.size!!
    }

}
