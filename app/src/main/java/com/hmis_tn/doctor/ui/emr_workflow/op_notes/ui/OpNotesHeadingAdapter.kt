package com.hmis_tn.doctor.ui.emr_workflow.op_notes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.op_notes.model.ProfileSection
import kotlinx.android.synthetic.main.item_op_notes_heading.view.*

class OpNotesHeadingAdapter(
    private val list: ArrayList<ProfileSection>,
    private val emrWorkflowList: ArrayList<com.hmis_tn.doctor.ui.emr_workflow.model.ResponseContent>,
    private val select: (ProfileSection) -> Unit
) :
    RecyclerView.Adapter<OpNotesHeadingAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_op_notes_heading, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val profileSection = list[position]
        holder.itemView.tvHeading.text = profileSection.sections?.name
            ?: getNameFromActivityId(profileSection.activity_uuid ?: 0)

//                1294 -> "Chief Complaints"
//                1295 -> "History"
//                1296 -> "Vitals"
//                1297 -> "Investigation"            ?: getNameFromActivityId(profileSection.activity_uuid ?: 0)
//
////        holder.itemView.tvHeading.text =
////            profileSection.sections?.name ?: when (profileSection.activity_uuid) {
////                1290 -> "Lab"
////                1291 -> "Radiology"
////                1292 -> "Prescription"
////                1293 -> "Treatment Kit"
//                1298 -> "Diagnosis"
//                1299 -> "Admission"
//                1300 -> "Lab Result"
//                1301 -> "Radiology Result"
//                1302 -> "Investigation Result"
//                1303 -> "Document"
//                1289 -> "OP Notes"
//                1325 -> "Certificate"
//                1315 -> "Critical Care Chart"
//                1309 -> "Blood Request"
//                1310 -> "Speciality Sketch"
//                1326 -> "MRD"
//                else -> ""
//            }

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