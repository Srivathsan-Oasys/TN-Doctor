package com.hmis_tn.doctor.ui.emr_workflow.radiology.ui


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
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.PodArrResult
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.PrevLabResponseContent
import com.hmis_tn.doctor.utils.Utils


class PrevRadiologyParentAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<PrevRadiologyParentAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String
    private var PatientList: List<PrevLabResponseContent>? = ArrayList()

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
        val modify: Button
        val viewLab: Button
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
            R.layout.preview_radiology_parent_list,
            parent,
            false
        ) as LinearLayout
        val recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = PatientList?.get(position).toString()
        val prevList = PatientList?.get(position)
        holder.firstTextView.text = prevList?.doctor_name
        holder.statusTextView.text = prevList?.order_status
        val correctformat =
            Utils.parseDate(prevList?.created_date!!, "yyyy-MM-dd HH:mm", "dd-MMM-yyyy hh:mm a")
        holder.dateTextView.text =
            correctformat + " - " + prevList.department_name + " - " + prevList.encounter_type_name
        holder.recyclerView.layoutManager = LinearLayoutManager(mContext)
        val myOrderChildAdapter = PrevRadiologyChildAdapter(mContext, prevList.pod_arr_result)
        val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        holder.recyclerView.addItemDecoration(itemDecor)
        holder.recyclerView.adapter = myOrderChildAdapter
        if (position == 0) {
            holder.resultLinearLayout.visibility = View.VISIBLE
        }
//        holder.modify.isEnabled = false
//        holder.modify.setBackgroundColor(ContextCompat.getColor(mContext, R.color.alternateRow))
//        holder.viewLab.isEnabled = false
//        holder.viewLab.setBackgroundColor(ContextCompat.getColor(mContext, R.color.alternateRow))

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
                    R.color.Skyblue
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
        holder.previewLinearLayout.setOnClickListener {

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

    fun refreshList(preLabArrayList: List<PrevLabResponseContent>?) {
        this.PatientList = preLabArrayList!!
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return PatientList?.size!!
    }

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: List<PodArrResult>?, isModify: Boolean
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


}




