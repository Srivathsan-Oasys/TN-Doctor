package com.hmis_tn.doctor.ui.emr_workflow.mrd.ui

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R

import com.hmis_tn.doctor.ui.emr_workflow.mrd.models.PrescriptionDetail


class MrdPrescriptionAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<MrdPrescriptionAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String
    private var PatientList: List<PrescriptionDetail?>? = ArrayList()


    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)


    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNum: TextView
        var dateText: TextView
        var typeText: TextView
        var departmentText: TextView
        var prescribedByText: TextView
        var institutionText: TextView


        val mainLinearLayout: LinearLayout


        init {
            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            dateText = view.findViewById<View>(R.id.dateText) as TextView
            typeText = view.findViewById<View>(R.id.typeText) as TextView
            departmentText = view.findViewById<View>(R.id.departmentText) as TextView
            prescribedByText = view.findViewById<View>(R.id.prescribedByText) as TextView
            institutionText = view.findViewById<View>(R.id.institutionText) as TextView



            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.mrd_prescription_recycler_list,
            parent,
            false
        ) as LinearLayout
        val recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = PatientList?.get(position).toString()
        val prevList = PatientList?.get(position)

        prevList?.let { list ->
            if (position < PatientList?.size!!) {
                holder.serialNum.text = (position + 1).toString()
                holder.dateText.text = list.prescription_date
                holder.typeText.text = list.encounter_type_uuid.toString()
                holder.departmentText.text = list.department_name
                holder.prescribedByText.text = list.priscribedDoctor_name
                holder.institutionText.text = list.facility_name.toString()
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

    fun refreshList(preLabArrayList: List<PrescriptionDetail>?) {
        this.PatientList = preLabArrayList!!
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return PatientList?.size!!
    }


}


/*
package com.hmis_tn.doctor.ui.emr_workflow.mrd.ui

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.mrd.models.AllergyDetail

import com.hmis_tn.doctor.ui.emr_workflow.mrd.models.MrdResponseMOdel
import com.hmis_tn.doctor.ui.emr_workflow.mrd.models.PrescriptionDetails


class MrdPrescriptionAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<MrdPrescriptionAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String
    private var PatientList: List<PrescriptionDetails?>? = ArrayList()



    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)


    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNum: TextView
        var dateText: TextView
        var typeText: TextView
        var allergyText: TextView
        var sourceText: TextView
        var durationText: TextView




        val mainLinearLayout: LinearLayout




        init {
            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            dateText = view.findViewById<View>(R.id.dateText) as TextView
            typeText = view.findViewById<View>(R.id.typeText) as TextView
            allergyText = view.findViewById<View>(R.id.allergyText) as TextView
            sourceText = view.findViewById<View>(R.id.sourceText) as TextView
            durationText = view.findViewById<View>(R.id.durationText) as TextView



            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.mrd_prescription_recycler_list,
            parent,
            false
        ) as LinearLayout
        val recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = PatientList?.get(position).toString()
        val prevList = PatientList?.get(position)
        holder.serialNum.text =(position+1).toString()

        prevList?.let { list ->
            if (position < PatientList?.size!!) {

            }
        }

*/
/*
        holder.dateText.text =prevList?.discharge_result?.investigation?.investigation_details?.get(0)?.
*//*

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

    fun refreshList(preLabArrayList: List<PrescriptionDetails>?) {
        this.PatientList = preLabArrayList!!
        this.notifyDataSetChanged()
    }


*/
/*
    fun refreshList(preLabArrayList: List<PrescriptionDetails?>) {
        this.PatientList = preLabArrayList!!
        this.notifyDataSetChanged()
    }
*//*



    override fun getItemCount(): Int {
        return PatientList?.size!!
    }





}
*/
