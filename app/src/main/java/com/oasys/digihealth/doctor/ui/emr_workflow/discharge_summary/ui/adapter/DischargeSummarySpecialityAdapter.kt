package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.SpecialitySketchesDetail
import com.oasys.digihealth.doctor.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_progress_notes.view.*
import kotlinx.android.synthetic.main.row_discharge_summary_speciality.view.*


typealias OnDeleteSpecialityListener = (specialitySketchesDetail: SpecialitySketchesDetail) -> Unit
typealias SpecialityURL = (specialitySketchesDetail: SpecialitySketchesDetail) -> Unit

class DischargeSummarySpecialityChildAdapter(
    private val mContext: Context,
    var OnDeleteSpecialityListener: OnDeleteSpecialityListener,
    var specialityURL: SpecialityURL
) :
    RecyclerView.Adapter<DischargeSummarySpecialityChildAdapter.SpecialityHolder>() {

    private var specialityList = ArrayList<SpecialitySketchesDetail>()
    var specialityImage: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialityHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_discharge_summary_speciality, parent, false)
        return SpecialityHolder(view)
    }

    override fun getItemCount(): Int = specialityList.size

    override fun onBindViewHolder(holder: SpecialityHolder, position: Int) {
        holder.bind(specialityList[position], position)
    }

    fun setData(specialityList: List<SpecialitySketchesDetail>) {
        this.specialityList = specialityList as ArrayList<SpecialitySketchesDetail>
        notifyDataSetChanged()
    }

    fun setSpecialityURL(specialityURL: String) {
        this.specialityImage = specialityURL
        notifyDataSetChanged()
    }

    inner class SpecialityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            investigationDetails: SpecialitySketchesDetail,
            position: Int
        ) {
            if (position % 2 == 0)
                itemView.llSpecialityMainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.alternateRow
                    )
                )
            investigationDetails.also { speciality ->

                try {
                    itemView.apply {
                        tvSpecialitySno.text = (position + 1).toString()
                        val date = Utils(itemView.context).displayDate(
                            speciality.date,
                            "yyyy-MM-dd HH:mm:ss"
                        )
                        tvSpecialityDate.text = date
                        tvSpecialityCode.text = speciality.speciality_sketch_code
                        tvSpecialityName.text = speciality.speciality_sketch_name

                        if (!speciality.sketch_path.isNullOrEmpty())
                            specialityURL.invoke(speciality)
                        else
                            ivSpecialitySketchImage.visibility = View.GONE

                        if (!specialityImage.isNullOrEmpty()) {
                            ivSpecialitySketchImage.visibility = View.VISIBLE

                            Picasso.get()
                                .load(specialityImage)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .into(this.ivSpecialitySketchImage)

                        }

                        previewSpecialityLinearLayout.setOnClickListener {

                            if (resultSpecialityLinearLayout.visibility == View.VISIBLE) {
                                ivSpecialityInfo.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
                                resultSpecialityLinearLayout.visibility = View.GONE

                            } else {
                                ivSpecialityInfo.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
                                resultSpecialityLinearLayout.visibility = View.VISIBLE
                            }
                        }


                        ivSpecialityDelete.setOnClickListener {
                            OnDeleteSpecialityListener.invoke(speciality)
                            specialityList.remove(speciality)
                            notifyDataSetChanged()
                        }
                        ivSpecialityInfo.setOnClickListener {
                            previewSpecialityLinearLayout.performClick()
                        }
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}