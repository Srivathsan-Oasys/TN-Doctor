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
import com.hmis_tn.doctor.ui.quick_reg.model.reports.response.LabTestWiseReportResponseContent

class LabDistrictWiseTestReportAdapter(context: Context, private var labTestWiseReport: ArrayList<LabTestWiseReportResponseContent?>?) :
    RecyclerView.Adapter<LabDistrictWiseTestReportAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    private var isLoadingAdded = false

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sNoTxt: TextView
        var testnameTxt: TextView
        var districtTxt: TextView
        var institutionTxt: TextView
        var maleAdultTxt: TextView
        var femaleAdultTxt: TextView
        var tansgenderAdultTxt: TextView
        var adultTotalTxt: TextView
        var maleChildTxt: TextView
        var femaleChildTxt: TextView
        var totalChildTxt: TextView
        var grandTotalTxt: TextView

        init {
            sNoTxt = view.findViewById<View>(R.id.sl_no_txt) as TextView
            institutionTxt = view.findViewById<View>(R.id.instituion_txt) as TextView
            districtTxt = view.findViewById<View>(R.id.district_txt) as TextView
            testnameTxt = view.findViewById<View>(R.id.test_txt) as TextView
            maleAdultTxt = view.findViewById<View>(R.id.adult_m_txt) as TextView
            femaleAdultTxt = view.findViewById<View>(R.id.adult_f_txt) as TextView
            tansgenderAdultTxt = view.findViewById<View>(R.id.adult_tg_txt) as TextView
            adultTotalTxt = view.findViewById<View>(R.id.totalAdult_txt) as TextView
            maleChildTxt = view.findViewById<View>(R.id.child_m_txt) as TextView
            femaleChildTxt = view.findViewById<View>(R.id.child_f_txt) as TextView
            totalChildTxt = view.findViewById<View>(R.id.child_total_txt) as TextView
            grandTotalTxt = view.findViewById<View>(R.id.grand_total_txt) as TextView


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_district_test_wise_report_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reportData = labTestWiseReport!![position]

        holder.sNoTxt.text =(position+1).toString()
        holder.institutionTxt.text =reportData?.Institution
        holder.districtTxt.text =reportData?.District
        holder.testnameTxt.text =reportData?.testname
        holder.maleAdultTxt.text =reportData?.maleadult.toString()
        holder.femaleAdultTxt.text =reportData?.femaleadult.toString()
        holder.tansgenderAdultTxt.text =reportData?.transgenderadult.toString()
        holder.adultTotalTxt.text =reportData?.totaladult.toString()
        holder.maleChildTxt.text =reportData?.malechild.toString()
        holder.femaleChildTxt.text =reportData?.femalechild.toString()
        holder.totalChildTxt.text =reportData?.totalchild.toString()
        holder.grandTotalTxt.text =reportData?.total.toString()

    }

    override fun getItemCount(): Int {
        return labTestWiseReport!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }


    fun addAll(responseContent: List<LabTestWiseReportResponseContent?>?) {

        this.labTestWiseReport!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.labTestWiseReport?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(LabTestWiseReportResponseContent())
    }
    fun add(r: LabTestWiseReportResponseContent) {
        labTestWiseReport!!.add(r)
        notifyItemInserted(labTestWiseReport!!.size - 1)
    }
    fun getItem(position: Int): LabTestWiseReportResponseContent? {
        return labTestWiseReport!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = labTestWiseReport!!.size - 1
        val result = getItem(position)
        if (result != null) {
            labTestWiseReport!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }


}