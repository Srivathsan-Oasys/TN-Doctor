package com.oasys.digihealth.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.quick_reg.model.reports.response.LabConsolidatedReportResponseContent

class LabConsolidatedReportAdapter(context: Context, private var labConsolidatedReport: ArrayList<LabConsolidatedReportResponseContent?>?) :
    RecyclerView.Adapter<LabConsolidatedReportAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    private var isLoadingAdded = false

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sNoTxt: TextView
        var districtTxt: TextView
        var institutionTypeTxt: TextView
        var institutionTxt: TextView
        var labTxt: TextView
        var patientTxt: TextView
        var testTxt: TextView
        var statusTxt: TextView
        var ageTxt: TextView
        var genderTxt: TextView
        var pinNumberTxt: TextView

        init {
            sNoTxt = view.findViewById<View>(R.id.sNo_txt) as TextView
            districtTxt = view.findViewById<View>(R.id.district_txt) as TextView
            institutionTypeTxt = view.findViewById<View>(R.id.institution_type_txt) as TextView
            institutionTxt = view.findViewById<View>(R.id.institution_txt) as TextView
            labTxt = view.findViewById<View>(R.id.lab_txt) as TextView
            patientTxt = view.findViewById<View>(R.id.patient_txt) as TextView
            testTxt = view.findViewById<View>(R.id.test_txt) as TextView
            statusTxt = view.findViewById<View>(R.id.status_txt) as TextView
            ageTxt = view.findViewById<View>(R.id.age_txt) as TextView
            genderTxt = view.findViewById<View>(R.id.gender_txt) as TextView
            pinNumberTxt = view.findViewById<View>(R.id.pinNumber_txt) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_lab_consolidated_report_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reportData = labConsolidatedReport!![position]
         holder.districtTxt.text =reportData?.district
         holder.sNoTxt.text =(position+1).toString()
         holder.institutionTypeTxt.text =reportData?.institutiontype
         holder.institutionTxt.text =reportData?.institutionname
        if(reportData?.salutation !=null){
            holder.patientTxt.text =reportData?.salutation+reportData?.patientname
        }else{
            holder.patientTxt.text =reportData?.patientname
        }
         holder.labTxt.text =reportData?.labname
         holder.testTxt.text =reportData?.testcodename
         holder.statusTxt.text =reportData?.orderstatusname
         if(reportData?.periodname?.equals("Year")==true){
            holder.ageTxt.text =reportData?.age.toString()+"Y"
         }else{
             holder.ageTxt.text =reportData?.age.toString()
         }
         holder.genderTxt.text =reportData?.gender
         holder.pinNumberTxt.text =reportData?.pinnumber

    }

    override fun getItemCount(): Int {
        return labConsolidatedReport!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }


    fun addAll(responseContent: List<LabConsolidatedReportResponseContent?>?) {

    this.labConsolidatedReport!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.labConsolidatedReport?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(LabConsolidatedReportResponseContent())
    }
    fun add(r: LabConsolidatedReportResponseContent) {
        labConsolidatedReport!!.add(r)
        notifyItemInserted(labConsolidatedReport!!.size - 1)
    }
    fun getItem(position: Int): LabConsolidatedReportResponseContent? {
        return labConsolidatedReport!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = labConsolidatedReport!!.size - 1
        val result = getItem(position)
        if (result != null) {
            labConsolidatedReport!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }


}