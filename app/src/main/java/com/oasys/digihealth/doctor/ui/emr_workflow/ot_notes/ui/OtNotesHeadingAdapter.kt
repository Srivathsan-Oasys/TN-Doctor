package com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.ProfileSection
import kotlinx.android.synthetic.main.item_op_notes_heading.view.*

class OtNotesHeadingAdapter(
    private val list: ArrayList<ProfileSection>,
    private val emrWorkflowList: ArrayList<com.oasys.digihealth.doctor.ui.emr_workflow.model.ResponseContent>,
    private val select: (ProfileSection) -> Unit
) :
    RecyclerView.Adapter<OtNotesHeadingAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ot_notes_heading, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val profileSection = list[position]
        holder.itemView.tvHeading.text = profileSection.sections?.name
            ?: getNameFromActivityId(profileSection.activity_uuid ?: 0)

        if (profileSection.isSelected)
            holder.itemView.viewSelected.visibility = View.VISIBLE
        else
            holder.itemView.viewSelected.visibility = View.GONE

        holder.itemView.tvHeading?.setOnClickListener {
            select(profileSection)
            profileSection.isSelected = true
            notifyDataSetChanged()
        }
    }

    private fun getNameFromActivityId(activityId: Int): String {
        for (i in emrWorkflowList.indices) {
            if (emrWorkflowList[i].activity_id == activityId) {
                return emrWorkflowList[i].activity_name ?: ""
            }
        }
        return ""
    }
}