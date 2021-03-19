package com.oasys.digihealth.doctor.ui.sample_dispatch.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.LabTestApprovalresponseContent

class SampleDispatchAdapter(
    context: Context,
    private var labTestApproval: ArrayList<LabTestApprovalresponseContent?>?
) :
    RecyclerView.Adapter<SampleDispatchAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater
    private var isLoadingAdded = false
    private val mContext: Context
    var orderNumString: String? = null
    var status: String? = null
    private var RTLabData: ArrayList<LabTestApprovalresponseContent?>? = ArrayList()
    var selectAllCheckbox: Boolean? = false

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var patient_info: TextView
        var status: TextView
        var sample_id: TextView
        var testName: TextView
        var dateetime: TextView
//        var assignedToOthers: TextView

        var mainLinearLayout: LinearLayout
        var selectCheckBox: CheckBox

        init {
            patient_info = view.findViewById<View>(R.id.patient_info) as TextView
            status = view.findViewById<View>(R.id.statueText) as TextView
            sample_id = view.findViewById<View>(R.id.sample_id) as TextView
            testName = view.findViewById<View>(R.id.testName) as TextView
            dateetime = view.findViewById<View>(R.id.dateText) as TextView
            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)
            selectCheckBox = view.findViewById(R.id.selectAllCheckBox)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_sample_dispatch_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.selectCheckBox.tag = position

        val labAllData = this.labTestApproval!![position]

        holder.status.setText(labTestApproval!![position]!!.order_status_name)

        holder.selectCheckBox.setOnClickListener {

            val myCheckBox = it as CheckBox
            val responseLabTestContent = labTestApproval!![position]
            if (myCheckBox.isChecked) {
                responseLabTestContent!!.is_selected = true
                RTLabData!!.add(responseLabTestContent)
            } else {
                responseLabTestContent!!.is_selected = false
                RTLabData!!.remove(responseLabTestContent)
            }
        }
        holder.testName.text = labAllData!!.test_name

        holder.sample_id.text = labAllData.sample_identifier

        holder.dateetime.text = labAllData.order_request_date

        holder.patient_info.text =
            labAllData!!.first_name + "/" + labAllData!!.ageperiod + "/" + labAllData.gender_name

        holder.selectCheckBox.isChecked = labAllData.is_selected!!

        labTestApproval!![position]!!.checkboxdeclardtatus =
            labTestApproval!![position]!!.order_status_uuid != 2

        if (labTestApproval!![position]!!.is_selected!! == true) {
            holder.selectCheckBox.isChecked = true
            holder.selectCheckBox.isEnabled = true
        } else {
            holder.selectCheckBox.isChecked = false
            holder.selectCheckBox.isEnabled = false
        }

        if (selectAllCheckbox == true) {
            for (i in labTestApproval!!.indices) {
                labTestApproval!![position]!!.is_selected =
                    labTestApproval!![position]!!.checkboxdeclardtatus != false
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

        holder.selectCheckBox.isEnabled =
            labTestApproval!![position]!!.checkboxdeclardtatus != false
    }


    override fun getItemCount(): Int {
        return labTestApproval!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }


    fun clearAll() {

        this.labTestApproval?.clear()
        this.RTLabData?.clear()
        notifyDataSetChanged()
    }

    fun selectAllCheckbox(ischeckedBox: Boolean) {
        for (i in labTestApproval!!.indices) {
            if (ischeckedBox) {
                if (this.labTestApproval!![i]!!.order_status_uuid != 2) {
                    this.labTestApproval!![i]!!.is_selected = true
                    selectAllCheckbox = true
                    RTLabData!!.add(this.labTestApproval!![i])
                } else {
                    labTestApproval!![i]!!.is_selected = false
                    selectAllCheckbox = false
                    RTLabData!!.remove(this.labTestApproval!![i])
                }
            } else {
                labTestApproval!![i]!!.is_selected = false
                selectAllCheckbox = false
                RTLabData!!.remove(this.labTestApproval!![i])
            }
        }
        notifyDataSetChanged()
    }

    fun addAll(responseContent: List<LabTestApprovalresponseContent?>?) {
        this.labTestApproval!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(LabTestApprovalresponseContent())
    }

    fun add(r: LabTestApprovalresponseContent) {
        labTestApproval!!.add(r)
        notifyItemInserted(labTestApproval!!.size - 1)
    }

    fun getItem(position: Int): LabTestApprovalresponseContent? {
        return labTestApproval!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        /*  val position = labTestApproval!!.size - 1
          val result = getItem(position)
          if (result != null) {
              labTestApproval!!.removeAt(position)
              notifyItemRemoved(position)
          }*/
    }

    fun getSelectedCheckData(): ArrayList<LabTestApprovalresponseContent?>? {
        return RTLabData
    }


}