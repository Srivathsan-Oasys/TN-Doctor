package com.hmis_tn.doctor.ui.emr_workflow.ot_notes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.ot_notes.model.ProfileSection
import com.hmis_tn.doctor.ui.emr_workflow.ot_notes.model.ProfileSectionCategory
import com.hmis_tn.doctor.ui.emr_workflow.ot_notes.model.prev_records.observed_values.ResponseContent
import kotlinx.android.synthetic.main.item_ot_notes_sub_heading.view.*

class OtNotesSubHeadingAdapter(
    private val profileSection: ProfileSection,
    private val list: ArrayList<ProfileSectionCategory>?,
    private val otNotesChildFragment: OtNotesChildFragment,
    private val observedValueList: ArrayList<ResponseContent>? = null
) :
    RecyclerView.Adapter<OtNotesSubHeadingAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ot_notes_sub_heading, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val profileSectionCategory = list?.get(position)

        holder.itemView.tvSubHeading.text = profileSectionCategory?.categories?.name

        holder.itemView.rvQuestions.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.itemView.rvQuestions.adapter = profileSectionCategory?.let {
            OtNotesQuestionsAdapter(
                profileSection,
                profileSectionCategory,
                profileSectionCategory.profile_section_category_concepts!!,
                otNotesChildFragment,
                observedValueList
            )
        }
    }
}