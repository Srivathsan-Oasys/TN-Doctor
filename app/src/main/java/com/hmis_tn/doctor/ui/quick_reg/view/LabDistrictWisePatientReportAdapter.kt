package com.hmis_tn.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.quick_reg.model.reports.response.LabWiseReportResponseContent

class LabDistrictWisePatientReportAdapter(context: Context, private var labDistrictWisePatientReport: ArrayList<LabWiseReportResponseContent?>?) :
    RecyclerView.Adapter<LabDistrictWisePatientReportAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    private var isLoadingAdded = false

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sNoTxt: TextView
        var districtTxt: TextView
        var institutionTxt: TextView
        var adultM: TextView
        var adultF: TextView
        var adultTG: TextView
        var adultTotal: TextView
        var childM: TextView
        var childF: TextView
        var childTotal: TextView
        var grandTotal: TextView


        init {
            sNoTxt = view.findViewById<View>(R.id.sl_no_txt) as TextView
            districtTxt = view.findViewById<View>(R.id.district_txt) as TextView
            institutionTxt = view.findViewById<View>(R.id.instituion_txt) as TextView
            adultM = view.findViewById<View>(R.id.adult_m_txt) as TextView
            adultF = view.findViewById<View>(R.id.adult_f_txt) as TextView
            adultTG = view.findViewById<View>(R.id.adult_tg_txt) as TextView
            adultTotal = view.findViewById<View>(R.id.totalAdult_txt) as TextView
            childM = view.findViewById<View>(R.id.child_m_txt) as TextView
            childF = view.findViewById<View>(R.id.child_f_txt) as TextView
            childTotal = view.findViewById<View>(R.id.child_total_txt) as TextView
            grandTotal = view.findViewById<View>(R.id.grand_total_txt) as TextView


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_districtwise_patient_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reportData = labDistrictWisePatientReport!![position]
        holder.districtTxt.text =reportData?.District
        holder.sNoTxt.text =(position+1).toString()
        holder.institutionTxt.text =reportData?.Institution
        holder.adultM.text = reportData?.maleadult.toString()
        holder.adultF.text =reportData?.femaleadult.toString()
        holder.adultTG.text =reportData?.transgenderadult.toString()
        holder.adultTotal.text =reportData?.totaladult.toString()
        holder.childM.text =reportData?.malechild.toString()
        holder.childF.text =reportData?.femalechild.toString()
        holder.childTotal.text =reportData?.totalchild.toString()
        holder.grandTotal.text =reportData?.total.toString()

    }

    override fun getItemCount(): Int {
        return labDistrictWisePatientReport!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }


    fun addAll(responseContent: List<LabWiseReportResponseContent?>?) {

        this.labDistrictWisePatientReport!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.labDistrictWisePatientReport?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(LabWiseReportResponseContent())
    }
    fun add(r: LabWiseReportResponseContent) {
        labDistrictWisePatientReport!!.add(r)
        notifyItemInserted(labDistrictWisePatientReport!!.size - 1)
    }
    fun getItem(position: Int): LabWiseReportResponseContent? {
        return labDistrictWisePatientReport!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = labDistrictWisePatientReport!!.size - 1
        val result = getItem(position)
        if (result != null) {
            labDistrictWisePatientReport!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }


}