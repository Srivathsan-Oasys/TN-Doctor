package com.oasys.digihealth.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.quick_reg.model.reports.response.LabWiseReportResponseContent

class LabWiseReportAdapter(
    context: Context,
    private var labWiseReport: ArrayList<LabWiseReportResponseContent?>?
) :
    RecyclerView.Adapter<LabWiseReportAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mContext: Context = context
    private var isLoadingAdded = false

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sNoTxt: TextView = view.findViewById<View>(R.id.sNo_txt) as TextView
        var labnameTxt: TextView = view.findViewById<View>(R.id.labname_txt) as TextView
        var institutionTxt: TextView = view.findViewById<View>(R.id.institution_txt) as TextView
        var maleAdultTxt: TextView = view.findViewById<View>(R.id.maleAdult_txt) as TextView
        var femaleAdultTxt: TextView = view.findViewById<View>(R.id.femaleAdult_txt) as TextView
        var tansgenderAdultTxt: TextView =
            view.findViewById<View>(R.id.tansgenderAdult_txt) as TextView
        var adultTotalTxt: TextView = view.findViewById<View>(R.id.adultTotal_txt) as TextView
        var maleChildTxt: TextView = view.findViewById<View>(R.id.maleChild_txt) as TextView
        var femaleChildTxt: TextView = view.findViewById<View>(R.id.femaleChild_txt) as TextView
        var totalChildTxt: TextView = view.findViewById<View>(R.id.totalChild_txt) as TextView
        var grandTotalTxt: TextView = view.findViewById<View>(R.id.grandTotal_txt) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_lab_wise_report_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reportData = labWiseReport!![position]

        holder.sNoTxt.text = (position + 1).toString()
        holder.institutionTxt.text = reportData?.Institution
        holder.labnameTxt.text = reportData?.labname
        holder.maleAdultTxt.text = reportData?.maleadult.toString()
        holder.femaleAdultTxt.text = reportData?.femaleadult.toString()
        holder.tansgenderAdultTxt.text = reportData?.transgenderadult.toString()
        holder.adultTotalTxt.text = reportData?.totaladult.toString()
        holder.maleChildTxt.text = reportData?.malechild.toString()
        holder.femaleChildTxt.text = reportData?.femalechild.toString()
        holder.totalChildTxt.text = reportData?.totalchild.toString()
        holder.grandTotalTxt.text = reportData?.total.toString()

    }

    override fun getItemCount(): Int {
        return labWiseReport!!.size
    }


    fun addAll(responseContent: List<LabWiseReportResponseContent?>?) {

        this.labWiseReport!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.labWiseReport?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(LabWiseReportResponseContent())
    }

    fun add(r: LabWiseReportResponseContent) {
        labWiseReport!!.add(r)
        notifyItemInserted(labWiseReport!!.size - 1)
    }

    fun getItem(position: Int): LabWiseReportResponseContent? {
        return labWiseReport!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = labWiseReport!!.size - 1
        val result = getItem(position)
        if (result != null) {
            labWiseReport!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}