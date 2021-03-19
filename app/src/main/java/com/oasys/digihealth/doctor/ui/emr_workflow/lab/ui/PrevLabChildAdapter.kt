package com.makkalnalam.ui.Expandable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.makkalnalam.ui.Expandable.PrevLabChildAdapter.MyViewHolder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.isTablet
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.PodArrResult
import kotlinx.android.synthetic.main.child_recycler_list.view.*
import kotlinx.android.synthetic.main.row_favourites.view.nameTextView
import kotlinx.android.synthetic.main.row_lab.view.codeTextView
import kotlinx.android.synthetic.main.row_lab.view.tv_no
import kotlinx.android.synthetic.main.row_lab.view.tv_sub_menu
import kotlinx.android.synthetic.main.row_lab.view.tv_title
import kotlinx.android.synthetic.main.row_lab_fav_add.view.*
import kotlinx.android.synthetic.main.row_lab_fav_add.view.sNoTextView

class PrevLabChildAdapter(
    private val mContext: Context,
    private var pod_Result: List<PodArrResult>?

) : RecyclerView.Adapter<MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mainLinearLayout: RelativeLayout =
            view.findViewById<View>(R.id.mainLinearLayout) as RelativeLayout
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.child_recycler_list,
            viewGroup,
            false
        )
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (isTablet(mContext)) {
            val prevList = pod_Result?.get(position)
            val pos = position + 1
            holder.itemView.sNoTextView.text = pos.toString()
            holder.itemView.nameTextView.text = prevList?.name
            holder.itemView.codeTextView.text = prevList?.code
            holder.itemView.orederToLocation.text = prevList?.order_to_location
            holder.itemView.typeTextView.text = prevList?.type.toString()
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
        } else {
            val prevList = pod_Result?.get(position)
            val pos = position + 1
            holder.itemView.tv_no.text = pos.toString()
            holder.itemView.tv_title.text = prevList?.code + "-" + prevList?.name
            holder.itemView.tv_sub_menu.text =
                prevList?.type.toString() + "-" + prevList?.order_to_location
        }
    }

    override fun getItemCount(): Int {
        return pod_Result?.size!!
    }
}
