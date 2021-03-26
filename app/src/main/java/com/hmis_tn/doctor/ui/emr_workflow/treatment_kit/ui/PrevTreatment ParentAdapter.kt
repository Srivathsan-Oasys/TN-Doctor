package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.TreatmentKitResponsResponseContent
import kotlinx.android.synthetic.main.preview_treatment_kit_parent_list.view.*
import java.text.SimpleDateFormat

class PrevTreatmentParentParentAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<PrevTreatmentParentParentAdapter.MyViewHolder>() {

    internal lateinit var orderNumString: String
    private var patientList: List<TreatmentKitResponsResponseContent>? = ArrayList()

    private var onRepeatItemClickListener: OnRepeatItemClickListener? = null
    private var onModifyItemClickListener: OnModifyItemClickListener? = null
    private var addNewItem: AddClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var firstTextView: TextView = view.findViewById(R.id.drName)
        val byTextView: TextView = view.findViewById(R.id.byTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val resultLinearLayout: LinearLayout = view.findViewById(R.id.resultLinearLayout)
        val previewLinearLayout: LinearLayout = view.findViewById(R.id.previewLinearLayout)
        val repeat: Button = view.findViewById(R.id.repeatLab)
        val modify: Button = view.findViewById(R.id.modifyLab)
        val addNew: Button = view.findViewById(R.id.addNew)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.preview_treatment_kit_parent_list, parent, false)
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = patientList?.get(position).toString()
        val prevList = patientList?.get(position)!!
        holder.firstTextView.text = prevList.doctor_name
        //holder.dateTextView.text = prevList?.ordered_date

        val encounter: String = when {
            prevList.encounter_type_uuid == 2 -> "IP"
            prevList.encounter_type_uuid == 1 -> "OP"
            else -> "IP"
        }

        try {
            val inputPattern = "yyyy-MM-dd HH:mm:ss"
            val outputPattern = "dd-MMM-yyyy h:mm a"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)
            val date = inputFormat.parse(prevList.ordered_date)
            val tarDate = outputFormat.format(date)
            holder.dateTextView.text =
                tarDate + " - " + prevList.department_name + " - " + encounter
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.resultLinearLayout.visibility = View.GONE

        holder.previewLinearLayout.setOnClickListener {
            if (holder.resultLinearLayout.visibility == View.VISIBLE) {
                holder.resultLinearLayout.visibility = View.GONE
            } else {
                holder.resultLinearLayout.visibility = View.VISIBLE
            }
        }

        if (prevList.diagnosis.isNotEmpty()) {
            holder.itemView.rvDiagnosis.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.itemView.rvDiagnosis.adapter =
                PrevTreatmentKitDiagnosisAdapter(prevList.diagnosis)
            holder.itemView.llDiagnosis.visibility = View.VISIBLE
        } else {
            holder.itemView.llDiagnosis.visibility = View.GONE
        }

        if (prevList.drugDetails.isNotEmpty()) {
            holder.itemView.rvPrescription.layoutManager =
                LinearLayoutManager(holder.itemView.context)
            holder.itemView.rvPrescription.adapter =
                PrevTreatmentKitPrescriptionAdapter(prevList.drugDetails)
            holder.itemView.llPrescription.visibility = View.VISIBLE
        } else {
            holder.itemView.llPrescription.visibility = View.GONE
        }

        if (prevList.labDetails.isNotEmpty()) {
            holder.itemView.rvLab.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.itemView.rvLab.adapter = PrevTreatmentKitLabAdapter(prevList.labDetails)
            holder.itemView.llLab.visibility = View.VISIBLE
        } else {
            holder.itemView.llLab.visibility = View.GONE
        }

        if (prevList.radilogyDetails.isNotEmpty()) {
            holder.itemView.rvRadiology.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.itemView.rvRadiology.adapter =
                PrevTreatmentKitRadiologyAdapter(prevList.radilogyDetails)
            holder.itemView.llRadiology.visibility = View.VISIBLE
        } else {
            holder.itemView.llRadiology.visibility = View.GONE
        }

        if (prevList.InvestigationDetails.isNotEmpty()) {
            holder.itemView.rvInvestigation.layoutManager =
                LinearLayoutManager(holder.itemView.context)
            holder.itemView.rvInvestigation.adapter =
                PrevTreatmentKitInvestigationAdapter(prevList.InvestigationDetails)
            holder.itemView.llInvestigation.visibility = View.VISIBLE
        } else {
            holder.itemView.llInvestigation.visibility = View.GONE
        }

        holder.repeat.setOnClickListener {
            onRepeatItemClickListener!!.onRepeatItemClick(this.patientList!![position])
        }

        holder.modify.setOnClickListener {
            onModifyItemClickListener!!.onModifyItemClick(this.patientList!![position])
        }

        holder.addNew.setOnClickListener {
            addNewItem!!.onAddNew()
        }
    }

    fun refreshList(preLabArrayList: List<TreatmentKitResponsResponseContent>?) {
        this.patientList = preLabArrayList!!
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return patientList?.size!!
    }

    interface OnRepeatItemClickListener {
        fun onRepeatItemClick(
            responseContent: TreatmentKitResponsResponseContent
        )
    }

    interface OnModifyItemClickListener {
        fun onModifyItemClick(
            responseContent: TreatmentKitResponsResponseContent
        )
    }

    fun setOnRepeatItemClickListener(onRepeatItemClickListener: OnRepeatItemClickListener) {
        this.onRepeatItemClickListener = onRepeatItemClickListener
    }

    fun setOnModifyItemClickListener(onModifyItemClickListener: OnModifyItemClickListener) {
        this.onModifyItemClickListener = onModifyItemClickListener
    }

    interface AddClickListener {
        fun onAddNew()
    }

    fun addNewItem(addNewItem: AddClickListener) {
        this.addNewItem = addNewItem
    }
}




