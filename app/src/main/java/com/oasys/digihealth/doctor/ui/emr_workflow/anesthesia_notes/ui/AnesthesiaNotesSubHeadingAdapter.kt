package com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.ProfileSection
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.ProfileSectionCategory
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.prev_records.observed_values.ResponseContent
import kotlinx.android.synthetic.main.item_anesthesia_notes_sub_heading.view.*

class AnesthesiaNotesSubHeadingAdapter(
    private val profileSection: ProfileSection,
    private val list: ArrayList<ProfileSectionCategory>?,
    private val anesthesiaNotesChildFragment: AnesthesiaNotesChildFragment,
    private val observedValueList: ArrayList<ResponseContent>? = null
) :
    RecyclerView.Adapter<AnesthesiaNotesSubHeadingAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_anesthesia_notes_sub_heading, parent, false)
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
            AnesthesiaNotesQuestionsAdapter(
                profileSection,
                profileSectionCategory,
                profileSectionCategory.profile_section_category_concepts!!,
                anesthesiaNotesChildFragment,
                observedValueList
            )
        }
    }
}