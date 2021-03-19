package com.oasys.digihealth.doctor.ui.emr_workflow.history.admission.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.history.admission.model.AdmissionReferalresponseContent
import com.oasys.digihealth.doctor.utils.Utils

class HistoryAdmissionAdapter(
    private val context: Context,
    private var admissionArrayList: List<AdmissionReferalresponseContent?>
) :
    RecyclerView.Adapter<HistoryAdmissionAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_history_admission, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return admissionArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        try {
            val responseData = admissionArrayList[position]
            holder.serialno.text = (position + 1).toString()
            val utils = Utils(holder.itemView.context)
            val date =
                utils.displayDate(responseData?.pr_referral_date ?: "", "yyyy-MM-dd HH:mm:ss")
            holder.date.text = date
            holder.doctorName.text = responseData?.u_first_name
            holder.departmentAdmisson.text = responseData?.d_name
            holder.institutionAdmission.text = responseData?.f_name

        } catch (e: Exception) {

        }


        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.alternateRow
                )
            )
        }

    }


    fun setData(responseContents: List<AdmissionReferalresponseContent>?) {
        admissionArrayList = responseContents!!
        notifyDataSetChanged()
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialno: TextView = itemView.findViewById(R.id.historyLabSerialno)
        internal val date: TextView = itemView.findViewById(R.id.historyLabDate)
        internal val doctorName: TextView = itemView.findViewById(R.id.doctorName)
        internal val departmentAdmisson: TextView = itemView.findViewById(R.id.departmentAdmisson)
        internal val institutionAdmission: TextView =
            itemView.findViewById(R.id.institutionAdmission)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)
    }

}