package com.oasys.digihealth.doctor.ui.detailedRegistration.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.detailedRegistration.model.GetPatientAllVisit
import com.oasys.digihealth.doctor.ui.quick_reg.model.QuickSearchresponseContent
import com.oasys.digihealth.doctor.utils.Utils


class PatientAllVisitsAdapter(
    context: Context,
    private var searchPatienList: ArrayList<GetPatientAllVisit?>?
) : RecyclerView.Adapter<PatientAllVisitsAdapter.MyViewHolder>() {

    private var itemLayout: LinearLayout? = null
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    private var isLoadingAdded = false
    var orderNumString: String? = null
    private var onItemClickListener: OnItemClickListener? = null

    var utils: Utils? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNUmber: TextView
        var dateTextView: TextView
        var VisitNumber: TextView
        var DepartmentName: TextView
        var VisitType: TextView
        var SpecialityClinicName: TextView


        init {
            serialNUmber = view.findViewById<View>(R.id.sno) as TextView
            DepartmentName = view.findViewById<View>(R.id.DepartmentName) as TextView
            VisitNumber = view.findViewById<View>(R.id.VisitNumber) as TextView
            VisitType = view.findViewById<View>(R.id.VisitType) as TextView
            dateTextView = view.findViewById<View>(R.id.VisitDate) as TextView

            SpecialityClinicName = view.findViewById<View>(R.id.SpecialityClinicName) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_visit_history, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        utils = Utils(this.mContext)
        orderNumString = searchPatienList!![position]?.toString()
        val searchList = searchPatienList!![position]!!
        holder.serialNUmber.text = (position + 1).toString()

        holder.VisitNumber.text = searchList.visit_number

        holder.DepartmentName.text = searchList.department_details?.name ?: ""

        holder.VisitType.text = searchList.visit_type_detail?.name ?: ""

        holder.SpecialityClinicName.text = searchList.speciality_department_details?.name ?: ""

        holder.dateTextView.text = utils?.convertDateFormat(
            searchList.registered_date!!,
            "yyyy-MM-dd HH:mm:ss",
            "dd-MMM-yyyy HH:mm"
        )


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


    fun addAll(responseContent: List<GetPatientAllVisit?>?) {
        searchPatienList!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.searchPatienList?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
    }

    fun add(r: GetPatientAllVisit) {
        searchPatienList!!.add(r)
        notifyItemInserted(searchPatienList!!.size - 1)
    }

    fun getItem(position: Int): GetPatientAllVisit? {
        return searchPatienList!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

    }


    fun setDataListAdd(
        arrayListPatientList: ArrayList<GetPatientAllVisit?>?
    ) {

        searchPatienList = arrayListPatientList
        notifyDataSetChanged()
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}