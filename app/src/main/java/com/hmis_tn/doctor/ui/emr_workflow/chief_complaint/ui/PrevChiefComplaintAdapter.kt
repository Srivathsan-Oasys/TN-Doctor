package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.isTablet
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.PrevChiefComplaintResponseContent


class PrevChiefComplaintAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<PrevChiefComplaintAdapter.MyViewHolder>() {

    private var prevChief_Result: List<PrevChiefComplaintResponseContent>? = ArrayList()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var sNoTextView: TextView = view.findViewById<View>(R.id.sNoTextView) as TextView
        var nameTextView: TextView = view.findViewById<View>(R.id.nameTextView) as TextView
        var durationTextview: TextView = view.findViewById<View>(R.id.durationTextview) as TextView
        var mainLinearLayout: LinearLayout? = null

        init {
            if (isTablet(mContext)) {
                mainLinearLayout = view.findViewById<View>(R.id.mainLayout) as LinearLayout
            }
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): MyViewHolder {
        val view = if (isTablet(mContext)) {
            LayoutInflater.from(mContext).inflate(
                R.layout.prev_chief_recycler_list,
                viewGroup,
                false
            ) as ConstraintLayout
        } else {
            LayoutInflater.from(mContext).inflate(
                R.layout.prev_chief_mobile_list,
                viewGroup,
                false
            )
        }
        return MyViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prevList = prevChief_Result?.get(position)
        val pos = position + 1
        holder.sNoTextView.text = pos.toString()
        holder.nameTextView.text = prevList?.chiefComplaintName
        holder.durationTextview.text =
            prevList?.chiefComplaintDuration.toString() + "-" + prevList?.chiefComplaintDurationName

        if (isTablet(mContext)) {
            if (position % 2 == 0) {
                holder.mainLinearLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.alternateRow
                    )
                )
            } else {
                holder.mainLinearLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.white
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return prevChief_Result?.size!!
    }

    fun refreshList(preLabArrayList: List<PrevChiefComplaintResponseContent>?) {
        this.prevChief_Result = preLabArrayList!!
        this.notifyDataSetChanged()
    }
}
