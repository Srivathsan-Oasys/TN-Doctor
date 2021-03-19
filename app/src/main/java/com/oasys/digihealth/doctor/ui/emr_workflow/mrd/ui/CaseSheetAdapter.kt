package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.CaseSheetResponseContent
import com.oasys.digihealth.doctor.utils.Utils


class CaseSheetAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<CaseSheetAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String
    private var PatientList: List<CaseSheetResponseContent?>? = ArrayList()
    private var isLoadingAdded = false
    private var onItemClickListener: OnItemClickListener? = null

    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNum: TextView
        var dateText: TextView
        var typeText: TextView
        var allergyText: TextView
        var sourceText: TextView


        val mainLinearLayout: LinearLayout
        val itemViews: LinearLayoutCompat

        init {
            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            dateText = view.findViewById<View>(R.id.dateText) as TextView
            typeText = view.findViewById<View>(R.id.typeText) as TextView
            allergyText = view.findViewById<View>(R.id.allergyText) as TextView
            sourceText = view.findViewById<View>(R.id.sourceText) as TextView
            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)
            itemViews = view.findViewById(R.id.itemView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.case_sheet_parent_list,
            parent,
            false
        ) as LinearLayout
        val recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = PatientList?.get(position).toString()
        val prevList = PatientList?.get(position)
        val utils = Utils(holder.itemView.context)
        prevList?.let { list ->
            if (position < PatientList?.size!!) {
                holder.serialNum.text = (position + 1).toString()
                holder.dateText.text =
                    utils.displayDate(list.created_date, "yyyy-MM-dd HH:mm:ss")
                holder.typeText.text = list.visit_type
                holder.allergyText.text = list.encounter_doctor_first_name
                holder.sourceText.text = list.encounter_department_name
                holder.itemViews.setOnClickListener {
                    onItemClickListener?.onItemClick(PatientList!![position], position)
                }
            }
        }
        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        }
    }

    fun refreshList(preLabArrayList: List<CaseSheetResponseContent>?) {
        this.PatientList = preLabArrayList!!
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return PatientList?.size!!
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(InPatientResponseData())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        /* val position = responseContent!!.size - 1
         val result = getItem(position)
         if (result != null) {
             responseContent!!.removeAt(position)
             notifyItemRemoved(position)
         }*/
    }

    interface OnItemClickListener {
        fun onItemClick(responseContent: CaseSheetResponseContent?, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}





