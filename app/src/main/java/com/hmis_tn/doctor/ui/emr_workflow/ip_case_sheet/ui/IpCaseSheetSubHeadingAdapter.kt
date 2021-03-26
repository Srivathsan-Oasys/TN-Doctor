package com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.model.ProfileSection
import com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.model.ProfileSectionCategory
import com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.model.prev_records.observed_values.ResponseContent
import kotlinx.android.synthetic.main.item_ip_case_sheet_sub_heading.view.*

class IpCaseSheetSubHeadingAdapter(
    private val profileSection: ProfileSection,
    private val list: ArrayList<ProfileSectionCategory>?,
    private val ipCaseSheetChildFragment: IpCaseSheetChildFragment,
    private val observedValueList: ArrayList<ResponseContent>? = null
) :
    RecyclerView.Adapter<IpCaseSheetSubHeadingAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ip_case_sheet_sub_heading, parent, false)
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
            IpCaseSheetQuestionsAdapter(
                profileSection,
                profileSectionCategory,
                profileSectionCategory.profile_section_category_concepts!!,
                ipCaseSheetChildFragment,
                observedValueList
            )
        }
    }
}