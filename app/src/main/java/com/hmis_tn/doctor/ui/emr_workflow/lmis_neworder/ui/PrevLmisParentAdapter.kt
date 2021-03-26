package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.ui

import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.ui.emr_workflow.lab.ui.ManageLabPervLabDialogFragment
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.PodArrResult
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.PrevLabLmisResponseContent
import com.hmis_tn.doctor.utils.Utils


class PrevLmisParentAdapter(
    context: Context,
    private var labParentList: List<PrevLabLmisResponseContent>
) : RecyclerView.Adapter<PrevLmisParentAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null
    private var onItemClickListener: OnItemClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var firstTextView: TextView
        var statusTextView: TextView
        val byTextView: TextView
        val dateTextView: TextView
        val statusTV: TextView
        var recyclerView: RecyclerView
        var previewLinearLayout: LinearLayout
        var resultLinearLayout: LinearLayout
        var repeatLab: Button
        var modify: Button
        val viewLab: Button

        init {
            firstTextView = view.findViewById<View>(R.id.drName) as TextView
            statusTextView = view.findViewById<View>(R.id.statusTextView) as TextView
            byTextView = view.findViewById<View>(R.id.byTextView) as TextView
            statusTV = view.findViewById<View>(R.id.statusTV) as TextView
            dateTextView = view.findViewById<View>(R.id.dateTextView) as TextView
            recyclerView = view.findViewById<View>(R.id.child_recycler) as RecyclerView
            previewLinearLayout = view.findViewById<View>(R.id.previewLinearLayout) as LinearLayout
            resultLinearLayout = view.findViewById<View>(R.id.resultLinearLayout) as LinearLayout
            repeatLab = view.findViewById(R.id.repeatLab)
            viewLab = view.findViewById<View>(R.id.viewButton) as Button
            modify = view.findViewById(R.id.modifyLab)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.lmis_prev_parent_recycler_list, parent, false) as CardView
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = labParentList[position].toString()
        val prevList = labParentList[position]
        holder.firstTextView.text = prevList.doctor_name
        holder.statusTextView.text = prevList.order_status
        val correctformat =
            Utils.parseDate(prevList.created_date, "yyyy-MM-dd HH:mm", "dd-MMM-yyyy hh:mm a")
        holder.dateTextView.text = correctformat
        val myOrderChildAdapter = PrevLmisChildAdapterAdapter(mContext, prevList.pod_arr_result)
        val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        holder.recyclerView.addItemDecoration(itemDecor)
        holder.recyclerView.adapter = myOrderChildAdapter
        labParentList[position].checkboxdeclardtatus = !labParentList[position].order_status.equals("APPROVED")

        holder.previewLinearLayout.setOnClickListener {

            if (holder.resultLinearLayout.visibility == View.VISIBLE) {
                holder.resultLinearLayout.visibility = View.GONE

            } else {
                holder.resultLinearLayout.visibility = View.VISIBLE
            }
        }


        if (labParentList[position].checkboxdeclardtatus == false) {
            holder.viewLab.isEnabled = true
            holder.viewLab.isFocusable = true
            holder.viewLab.isFocusableInTouchMode = true

            holder.modify.alpha = 0.2f
            holder.modify.isEnabled = false
            holder.modify.isFocusable = false
            holder.modify.isFocusableInTouchMode = false
        } else {

            holder.viewLab.alpha = 0.2f
            holder.viewLab.isEnabled = false
            holder.viewLab.isFocusable = false
            holder.viewLab.isFocusableInTouchMode = false


            holder.modify.isEnabled = true
            holder.modify.isClickable = true
            // holder.modify.setFocusable(true);
            //holder.modify.setFocusableInTouchMode(true);
        }

        holder.repeatLab.setOnClickListener {
            onItemClickListener!!.onItemClick(labParentList[position].pod_arr_result, false)

        }

        holder.modify.setOnClickListener({
            onItemClickListener!!.onItemClick(labParentList[position].pod_arr_result, true)
        })

        holder.viewLab.setOnClickListener {
            val patientdetails = labParentList[position].pod_arr_result
            val patientOrderUUID = patientdetails.get(0).patient_order_uuid


            val ft = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
            val dialog = ManageLabPervLabDialogFragment()
            val bundle = Bundle()
            bundle.putInt(AppConstants.PATIENT_ORDER_UUID, patientOrderUUID)
            dialog.arguments = bundle
            dialog.show(ft, "Tag")
        }

    }

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: List<PodArrResult>, isModify: Boolean
        )
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return labParentList.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }

    fun refreshList(preLabArrayList: List<PrevLabLmisResponseContent>?) {
        this.labParentList = preLabArrayList!!
        this.notifyDataSetChanged()
    }

}