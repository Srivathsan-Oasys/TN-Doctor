package com.makkalnalam.ui.Expandable

import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.isTablet
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.InvestigationPrevResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui.PrevInvestigationMobileChildAdapter
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.ManageRadiologyResultDialogFragment
import com.oasys.digihealth.doctor.utils.Utils


class PrevInvestigationParentAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<PrevInvestigationParentAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String
    private var PatientList: List<InvestigationPrevResponseContent>? = ArrayList()

    private var onItemClickListener: OnItemClickListener? = null

    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)


    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var firstTextView: TextView
        var statusTextView: TextView
        val byTextView: TextView
        val dateTextView: TextView
        val statusTV: TextView
        val resultLinearLayout: LinearLayout
        val previewLinearLayout: LinearLayout
        val repeat: Button
        val viewLab: Button
        val modify: Button
        internal var recyclerView: RecyclerView

        init {
            firstTextView = view.findViewById<View>(R.id.drName) as TextView
            statusTextView = view.findViewById<View>(R.id.statusTextView) as TextView
            byTextView = view.findViewById<View>(R.id.byTextView) as TextView
            statusTV = view.findViewById<View>(R.id.statusTV) as TextView
            dateTextView = view.findViewById<View>(R.id.dateTextView) as TextView
            resultLinearLayout = view.findViewById(R.id.resultLinearLayout)
            previewLinearLayout = view.findViewById<View>(R.id.previewLinearLayout) as LinearLayout
            recyclerView = view.findViewById(R.id.child_recycler)
            repeat = view.findViewById<View>(R.id.repeatLab) as Button
            modify = view.findViewById<View>(R.id.modifyLab) as Button
            viewLab = view.findViewById<View>(R.id.viewButton) as Button

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.preview_parent_list,
            parent,
            false
        )
        val recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = PatientList?.get(position).toString()
        val prevList = PatientList?.get(position)
        holder.firstTextView.text = prevList?.doctor_name
        holder.statusTextView.text = prevList?.order_status


        val correctformat =
            Utils.parseDate(prevList?.modified_date!!, "yyyy-MM-dd HH:mm:ss", "dd-MMM-yyyy hh:mm a")
        holder.dateTextView.text =
            correctformat + " - " + prevList.department_name + " - " + prevList.encounter_type_name
        holder.recyclerView.layoutManager = LinearLayoutManager(mContext)
        holder.recyclerView.layoutManager = LinearLayoutManager(mContext)
        if (isTablet(mContext)) {
            val myOrderChildAdapter =
                PrevInvestigationChildAdapter(mContext, prevList.pod_arr_result)
            val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
            holder.recyclerView.addItemDecoration(itemDecor)
            holder.recyclerView.adapter = myOrderChildAdapter
        } else {

            val myOrderChildAdapter =
                PrevInvestigationMobileChildAdapter(mContext, prevList.pod_arr_result)
            val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
            holder.recyclerView.addItemDecoration(itemDecor)
            holder.recyclerView.adapter = myOrderChildAdapter
        }
        /* holder.modify.isEnabled = false
         holder.modify.setBackgroundColor(ContextCompat.getColor(mContext, R.color.alternateRow))*/
        holder.viewLab.isEnabled = false
        holder.viewLab.setBackgroundColor(ContextCompat.getColor(mContext, R.color.alternateRow))



        holder.previewLinearLayout.setOnClickListener {

            if (prevList.order_status == "APPROVED") {
                holder.viewLab.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.colorPrimary
                    )
                )
                holder.viewLab.isEnabled = true
            } else {
                holder.viewLab.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.alternateRow
                    )
                )
                holder.viewLab.isEnabled = false
            }
            if (prevList.order_status == "CREATED") {
                holder.modify.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.outPtientGreen
                    )
                )
                holder.modify.isEnabled = true
            } else {
                holder.modify.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.alternateRow
                    )
                )
                holder.modify.isEnabled = false
            }



            if (holder.resultLinearLayout.visibility == View.VISIBLE) {
                holder.resultLinearLayout.visibility = View.GONE

            } else {
                holder.resultLinearLayout.visibility = View.VISIBLE


            }
        }


        holder.repeat.setOnClickListener {

            onItemClickListener!!.onItemClick(this.PatientList!![position].pod_arr_result, false)

        }

        holder.viewLab.setOnClickListener {
            val patientdetails = PatientList!![position].pod_arr_result
            val patientOrderUUID = patientdetails.get(0).patient_order_uuid

            val ft = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
            val dialog = ManageRadiologyResultDialogFragment()
            val bundle = Bundle()
            bundle.putInt(AppConstants.PATIENT_ORDER_UUID, patientOrderUUID)
            dialog.arguments = bundle
            dialog.show(ft, "Tag")


        }


        holder.modify.setOnClickListener {
            if (prevList.order_status == "CREATED")
                onItemClickListener!!.onItemClick(this.PatientList!![position].pod_arr_result, true)

        }
    }

    fun refreshList(preLabArrayList: List<InvestigationPrevResponseContent>?) {
        this.PatientList = preLabArrayList!!
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return PatientList?.size!!
    }

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: List<com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.PodArrResult>,
            isModify: Boolean
        )
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


}




