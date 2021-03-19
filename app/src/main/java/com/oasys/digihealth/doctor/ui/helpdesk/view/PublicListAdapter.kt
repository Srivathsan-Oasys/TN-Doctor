package com.oasys.digihealth.doctor.ui.helpdesk.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.helpdesk.model.VendorResponseContent
import java.text.SimpleDateFormat

class PublicListAdapter(
    context: Context,
    private var userManualTutotrial: ArrayList<VendorResponseContent.PatientVisits?>?,
    private var clickListener: HelpDeskCallback
) :
    RecyclerView.Adapter<PublicListAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    private var isLoadingAdded = false
    private var customdialog: Dialog? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dateTxt: TextView
        var institutionTxt: TextView
        var departmentTxt: TextView
        var typeTxt: TextView

        init {
            dateTxt = view.findViewById<View>(R.id.date_txt) as TextView
            institutionTxt = view.findViewById<View>(R.id.institution_txt) as TextView
            departmentTxt = view.findViewById<View>(R.id.department_txt) as TextView
            typeTxt = view.findViewById<View>(R.id.type_txt) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_public_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(
            itemLayout
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listDate = userManualTutotrial!![position]
        holder.departmentTxt.text = listDate?.department_details?.name
        holder.typeTxt.text = listDate?.patient_type_detail?.name
        holder.institutionTxt.text = listDate?.facility_details?.name

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val sdf1 = SimpleDateFormat("dd-MM-yyyy")
        var date = sdf.parse(listDate?.registered_date)
        var createddate = sdf1.format(date)
        holder.dateTxt.text = createddate

    }

    override fun getItemCount(): Int {
        return userManualTutotrial!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }


    fun addAll(responseContent: List<VendorResponseContent.PatientVisits?>?) {

        this.userManualTutotrial!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.userManualTutotrial?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(VendorResponseContent.PatientVisits())
    }

    fun add(r: VendorResponseContent.PatientVisits) {
        userManualTutotrial!!.add(r)
        notifyItemInserted(userManualTutotrial!!.size - 1)
    }

    fun getItem(position: Int): VendorResponseContent.PatientVisits? {
        return userManualTutotrial!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = userManualTutotrial!!.size - 1
        val result = getItem(position)
        if (result != null) {
            userManualTutotrial!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}