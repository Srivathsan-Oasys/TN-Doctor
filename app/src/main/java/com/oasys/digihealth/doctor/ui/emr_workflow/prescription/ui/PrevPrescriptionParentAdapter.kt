package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrescriptionDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrevRow
import com.oasys.digihealth.doctor.utils.Utils


class PrevPrescriptionParentAdapter(
    private val mContext: Context
) :
    RecyclerView.Adapter<PrevPrescriptionParentAdapter.myViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String
    private var prevPrescriptionList: List<PrevRow?>? = ArrayList()

    private var onItemClickListener: OnItemClickListener? = null

    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)
    }

    class myViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var firstTextView: TextView
        var statusTextView: TextView
        val byTextView: TextView
        val dateTextView: TextView
        val statusTV: TextView

        // val tableLayout: TableLayout
        val resultLinearLayout: LinearLayout
        val previewLinearLayout: LinearLayout

        internal var recyclerView: RecyclerView
        val prescription_repeat: Button
        val prescription_modify: Button

        init {
            firstTextView = view.findViewById<View>(R.id.drName) as TextView
            statusTextView = view.findViewById<View>(R.id.statusTextView) as TextView
            byTextView = view.findViewById<View>(R.id.byTextView) as TextView
            statusTV = view.findViewById<View>(R.id.statusTV) as TextView
            dateTextView = view.findViewById<View>(R.id.dateTextView) as TextView

            resultLinearLayout = view.findViewById(R.id.resultLinearLayout)
            previewLinearLayout = view.findViewById<View>(R.id.previewLinearLayout) as LinearLayout
            recyclerView = view.findViewById(R.id.child_recycler)
            prescription_repeat = view.findViewById(R.id.Prescription_repeat)
            prescription_modify = view.findViewById(R.id.Prescription_modify)
            //  tableLayout = view.findViewById<View>(R.id.tableLayout) as TableLayout

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.prev_previous_parent_list,
            parent,
            false
        ) as CardView
        return myViewHolder(itemLayout)

    }

    override fun getItemCount(): Int {
        return prevPrescriptionList?.size!!
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        orderNumString = prevPrescriptionList?.get(position).toString()
        val prevList = prevPrescriptionList?.get(position)
        holder.firstTextView.text = prevList?.priscribedDoctor_name
        holder.statusTextView.text = prevList?.prescription_status!!.name.toString()
        val correctformat =
            "${
                Utils.parseDate(
                    prevList.created_date!!,
                    "yyyy-MM-dd HH:mm",
                    "dd-MMM-yyyy hh:mm a"
                )
            } - ${prevList.department_name} - ${prevList.store_master?.store_name}"
        holder.dateTextView.text = correctformat
        holder.recyclerView.layoutManager = LinearLayoutManager(mContext)

        if (Utils(mContext).isTablet(mContext)) {
            val myOrderChildAdapter =
                PrevPrescriptionChildAadpter(mContext, prevList.prescription_details)
            val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
            holder.recyclerView.addItemDecoration(itemDecor)

            holder.recyclerView.adapter = myOrderChildAdapter
        } else {
            val myOrderChildAdapter =
                PrevPrescriptionChildMobAadpter(mContext, prevList.prescription_details)

            val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
            holder.recyclerView.addItemDecoration(itemDecor)
            holder.recyclerView.adapter = myOrderChildAdapter
            if (prevList.injection_room != null)
                myOrderChildAdapter.setInjectionData(prevList.injection_room)
        }
        holder.previewLinearLayout.setOnClickListener {

            if (holder.resultLinearLayout.visibility == View.VISIBLE) {

                holder.resultLinearLayout.visibility = View.GONE

            } else {

                holder.resultLinearLayout.visibility = View.VISIBLE

            }
        }

        holder.prescription_repeat.setOnClickListener {

            onItemClickListener?.onItemClick(prevList.prescription_details, false, prevList)

        }

        holder.prescription_modify.setOnClickListener {

            onItemClickListener?.onItemClick(prevList.prescription_details, true, prevList)

        }

    }

    fun refreshList(preLabArrayList: List<PrevRow?>?) {
        this.prevPrescriptionList = preLabArrayList!!
        this.notifyDataSetChanged()
    }


    interface OnItemClickListener {
        fun onItemClick(
            responseContent: List<PrescriptionDetail?>?,
            isModify: Boolean,
            injection_room_uuid: PrevRow?
        )
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

}