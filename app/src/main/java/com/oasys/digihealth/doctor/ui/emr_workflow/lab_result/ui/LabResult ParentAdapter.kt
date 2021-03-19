package com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.LabResultResponseContent
import com.oasys.digihealth.doctor.utils.Utils
import kotlinx.android.synthetic.main.lab_result_parent_layout.view.*

typealias OnCompareButtonValidate = (labResultArrayListCompare: ArrayList<LabResultResponseContent>?) -> Unit

class LabResultParentAdapter(var onCompareButtonValidate: OnCompareButtonValidate) :
    RecyclerView.Adapter<LabResultParentAdapter.MyViewHolder>() {
    internal lateinit var orderNumString: String
    private var labResultList = ArrayList<LabResultResponseContent>()
    private val labResultListCompare = ArrayList<LabResultResponseContent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.lab_result_parent_layout, parent, false)
        return MyViewHolder(itemLayout.rootView)
    }

    override fun getItemCount(): Int {
        return labResultList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = labResultList.get(position).toString()
        holder.bindData(labResultList, position, labResultListCompare)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(
            labResultList: ArrayList<LabResultResponseContent>?,
            position: Int,
            labResultListCompare: ArrayList<LabResultResponseContent>?
        ) {
            labResultList?.get(position)?.also {
                itemView.dateCheckbox.isChecked = labResultList[position].isSelected
                itemView.dateCheckbox.tag = position

                val date = Utils(itemView.context).displayDate(
                    it.order_request_date,
                    "yyyy-MM-dd HH:mm:ss"
                )
                itemView.dateTextView.text =
                    date + " - " + it.test_master + " - " + "-" + it.department + " - " + it.encounter_type
                itemView.child_recycler.layoutManager = LinearLayoutManager(itemView.context)
                val myOrderChildAdapter =
                    LabResultChildAdapter(
                        itemView.context,
                        labResultList[position].repsonse
                    )
                val itemDecor =
                    DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL)
                itemView.child_recycler.addItemDecoration(itemDecor)
                itemView.child_recycler.adapter = myOrderChildAdapter
            }

            itemView.previewLinearLayout.setOnClickListener {
                if (itemView.resultLinearLayout.visibility == View.VISIBLE) {
                    itemView.resultLinearLayout.visibility = View.GONE
                } else {
                    itemView.resultLinearLayout.visibility = View.VISIBLE
                }
            }
            itemView.dateCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.id == itemView.dateCheckbox.id)
                    if (isChecked) {
                        if (!labResultList.isNullOrEmpty()) {
                            if (labResultListCompare == null)
                                labResultListCompare?.add(labResultList[position])
                            else
                                labResultListCompare.add(labResultList[position])
                            onCompareButtonValidate.invoke(labResultListCompare)
                        }
                    } else {
                        if (!labResultList.isNullOrEmpty() && labResultListCompare?.contains(
                                labResultList[position]
                            )!!
                        ) {
                            labResultListCompare.remove(labResultList[position])
                            onCompareButtonValidate.invoke(labResultListCompare)
                        }
                    }
            }
            itemView.dateCheckbox.setOnClickListener {
                val pos = itemView.dateCheckbox.tag as Int
                labResultList?.get(pos)?.also { labResult ->
                    labResult.isSelected = !labResult.isSelected
                }
            }


        }
    }

    fun refreshList(preLabArrayList: ArrayList<LabResultResponseContent>?) {
        this.notifyDataSetChanged()
    }

    fun setData(labResultLIst: ArrayList<LabResultResponseContent>) {
        this.labResultList = labResultLIst
        notifyDataSetChanged()

    }
}