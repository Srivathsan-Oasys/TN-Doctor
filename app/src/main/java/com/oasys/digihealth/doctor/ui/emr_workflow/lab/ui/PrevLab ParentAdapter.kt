package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makkalnalam.ui.Expandable.PrevLabChildAdapter
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.PodArrResult
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.PrevLabResponseContent
import com.oasys.digihealth.doctor.utils.Utils.Companion.parseDate

class PrevLabParentAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<PrevLabParentAdapter.MyViewHolder>() {

    internal lateinit var orderNumString: String
    private var patientList: List<PrevLabResponseContent>? = ArrayList()

    private var onItemClickListener: OnItemClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var firstTextView: TextView = view.findViewById<View>(R.id.drName) as TextView
        var statusTextView: TextView = view.findViewById<View>(R.id.statusTextView) as TextView
        val byTextView: TextView = view.findViewById<View>(R.id.byTextView) as TextView
        val dateTextView: TextView = view.findViewById<View>(R.id.dateTextView) as TextView
        val statusTV: TextView = view.findViewById<View>(R.id.statusTV) as TextView
        val resultLinearLayout: LinearLayout = view.findViewById(R.id.resultLinearLayout)
        val previewLinearLayout: LinearLayout =
            view.findViewById<View>(R.id.previewLinearLayout) as LinearLayout
        val repeat: Button = view.findViewById<View>(R.id.repeatLab) as Button
        val modify: Button = view.findViewById<View>(R.id.modifyLab) as Button
        val viewLab: Button = view.findViewById<View>(R.id.viewButton) as Button

        internal var recyclerView: RecyclerView = view.findViewById(R.id.child_recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.preview_parent_list, parent, false) as LinearLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = patientList?.get(position).toString()
        val prevList = patientList?.get(position)

        holder.firstTextView.text = prevList?.doctor_name
        holder.statusTextView.text = prevList?.order_status
        val correctFormat =
            parseDate(prevList?.modified_date!!, "yyyy-MM-dd HH:mm", "dd-MMM-yyyy hh:mm a")
        val display =
            correctFormat + " - " + prevList.department_name + " - " + prevList.encounter_type_name
        holder.dateTextView.text = display
        holder.recyclerView.layoutManager = LinearLayoutManager(mContext)
        val myOrderChildAdapter = PrevLabChildAdapter(mContext, prevList.pod_arr_result)
        val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        holder.recyclerView.addItemDecoration(itemDecor)
        holder.recyclerView.adapter = myOrderChildAdapter
        if (position == 0) {
            holder.resultLinearLayout.visibility = View.VISIBLE
        }

        patientList!![position].checkboxdeclardtatus =
            patientList!![position].order_status != "APPROVED"
        holder.previewLinearLayout.setOnClickListener {
            if (holder.resultLinearLayout.visibility == View.VISIBLE) {
                holder.resultLinearLayout.visibility = View.GONE

            } else {
                holder.resultLinearLayout.visibility = View.VISIBLE
            }
        }

        if (patientList!![position].order_status == "APPROVED") {
            holder.viewLab.isEnabled = true
            holder.viewLab.isFocusable = true
            holder.viewLab.isFocusableInTouchMode = true
            holder.viewLab.isClickable = true
        } else {
            holder.viewLab.alpha = 0.2f
            holder.viewLab.isEnabled = false
            holder.viewLab.isFocusable = false
            holder.viewLab.isFocusableInTouchMode = false
        }

        holder.repeat.setOnClickListener {
            onItemClickListener!!.onItemClick(this.patientList!![position].pod_arr_result, false)
        }

        holder.viewLab.setOnClickListener {
            val patientDetails = patientList!![position].pod_arr_result
            val patientOrderUUID = patientDetails[0].patient_order_uuid

            val ft = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
            val dialog = ManageLabPervLabDialogFragment()
            val bundle = Bundle()
            bundle.putInt(AppConstants.PATIENT_ORDER_UUID, patientOrderUUID)
            dialog.arguments = bundle
            dialog.show(ft, "Tag")
        }

        holder.modify.setOnClickListener {
            onItemClickListener!!.onItemClick(this.patientList!![position].pod_arr_result, true)
        }
    }

    fun refreshList(preLabArrayList: List<PrevLabResponseContent>?) {
        this.patientList = preLabArrayList!!
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return patientList?.size!!
    }

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: List<PodArrResult>?,
            isModifiy: Boolean
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}