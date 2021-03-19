package com.oasys.digihealth.doctor.ui.detailedRegistration.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.detailedRegistration.model.GetPatientAllReferral
import com.oasys.digihealth.doctor.ui.quick_reg.model.QuickSearchresponseContent
import com.oasys.digihealth.doctor.utils.Utils


class PatientAllReferralsAdapter(
    context: Context,
    private var searchPatienList: ArrayList<GetPatientAllReferral?>?
) : RecyclerView.Adapter<PatientAllReferralsAdapter.MyViewHolder>() {

    private var itemLayout: LinearLayout? = null
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    private var isLoadingAdded = false
    var orderNumString: String? = null
    private var onItemClickListener: OnItemClickListener? = null
    var utils: Utils? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNUmber: TextView
        var ReferralType: TextView
        var ReferralDate: TextView
        var ReasonforReferral: TextView


        init {
            serialNUmber = view.findViewById<View>(R.id.sno) as TextView
            ReferralType = view.findViewById<View>(R.id.ReferralType) as TextView
            ReasonforReferral = view.findViewById<View>(R.id.ReasonforReferral) as TextView

            ReferralDate = view.findViewById<View>(R.id.ReferralDate) as TextView


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_referal_history, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        utils = Utils(this.mContext)
        orderNumString = searchPatienList!![position]?.toString()
        val searchList = searchPatienList!![position]!!
        holder.serialNUmber.text = (position + 1).toString()

        holder.ReferralDate.text = utils?.convertDateFormat(
            searchList.refferal_date!!,
            "yyyy-MM-dd HH:mm:ss",
            "dd-MMM-yyyy"
        )

        holder.ReasonforReferral.text = searchList.referral_reason_details ?: ""
        holder.ReferralType.text = searchList.referral_type_details ?: ""
/*
        holder.pinTextView.text = searchList.uhid
        holder.nameTextView.text = searchList.first_name

        holder.fathernameTextView.text=searchList.patient_detail?.father_name?:""




*/


    }

    override fun getItemCount(): Int {
        return searchPatienList!!.size
    }

    /*
sent data
*/
    interface OnItemClickListener {
        fun onItemClick(responseContent: QuickSearchresponseContent)
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


    fun addAll(responseContent: List<GetPatientAllReferral?>?) {
        searchPatienList!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.searchPatienList?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        // add(LabTestresponseContent())
    }

    fun add(r: GetPatientAllReferral) {
        searchPatienList!!.add(r)
        notifyItemInserted(searchPatienList!!.size - 1)
    }

    fun getItem(position: Int): GetPatientAllReferral? {
        return searchPatienList!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

    }


    fun setDataListAdd(
        arrayListPatientList: ArrayList<GetPatientAllReferral?>?
    ) {

        searchPatienList = arrayListPatientList
        notifyDataSetChanged()
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}