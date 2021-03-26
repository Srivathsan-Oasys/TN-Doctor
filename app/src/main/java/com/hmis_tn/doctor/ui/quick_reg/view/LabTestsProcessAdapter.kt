package com.hmis_tn.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.testprocess.TestPrecessresponseContent

class LabTestsProcessAdapter(
    context: Context,
    private var labTestList: ArrayList<TestPrecessresponseContent?>?
) :
    RecyclerView.Adapter<LabTestsProcessAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null
    var status: String? = null
    var selectAllCheckbox: Boolean? = false
    private var isLoadingAdded = false
    private var SelectedLabData: ArrayList<TestPrecessresponseContent?>? = ArrayList()

    private var onPrintClickListener: OnPrintClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var patient_info: TextView = view.findViewById<View>(R.id.patient_info) as TextView
        var status: TextView = view.findViewById<View>(R.id.status) as TextView
        var dateText: TextView = view.findViewById<View>(R.id.dateText) as TextView
        var sampleid: TextView = view.findViewById(R.id.sample_id)
        var testMethod: TextView = view.findViewById(R.id.testMethod)
        var testname: TextView = view.findViewById(R.id.test_name)
        var mainLinearLayout: LinearLayout = view.findViewById(R.id.mainLinearLayout)
        var selectCheckBox: CheckBox = view.findViewById(R.id.checkbox)
        var print: ImageView = view.findViewById(R.id.print)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_lab_test_process_, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //orderNumString = labTestList[position].toString()
        val labAllData = labTestList!![position]
        holder.patient_info.text =
            labAllData!!.first_name + "/" + labAllData.ageperiod + "/" + labAllData.gender_name
        holder.dateText.text = labAllData.order_request_date
        holder.status.text = labAllData.order_status_name
        holder.sampleid.text = labAllData.order_number.toString()
        holder.testMethod.text = labAllData.test_method_name
        holder.testname.text = labAllData.test_name
        holder.selectCheckBox.isChecked = labAllData.is_selected!!

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

        if (this.labTestList!![position]!!.auth_status_uuid == 4) {
            holder.print.visibility = View.VISIBLE
        }

        holder.print.setOnClickListener {
            onPrintClickListener?.onPrintClick(this.labTestList!![position]!!.uuid!!)
            //     this.labTestList!![position]!!.uuid
        }

        if (labTestList!![position]!!.order_status_uuid == 7 &&
            labTestList!![position]!!.auth_status_uuid == 4 ||
            labTestList!![position]!!.order_status_uuid == 2
        ) {
            if (labTestList!![position]!!.order_status_uuid == 2) {
                holder.status.text = labTestList!![position]!!.order_status_name
            } else {
                holder.status.text = labTestList!![position]!!.auth_status_name
            }
            labTestList!![position]!!.checkboxdeclardtatus = false
        } else {
            labTestList!![position]!!.checkboxdeclardtatus = true
        }

        if (labTestList!![position]!!.checkboxdeclardtatus == false) {
            holder.selectCheckBox.isEnabled = false
            holder.status.setTextColor(Color.parseColor("#FF0000"))
        } else {
            holder.selectCheckBox.isEnabled = true
            holder.status.setTextColor(Color.parseColor("#000000"))
        }

        holder.selectCheckBox.setOnClickListener {
            val myCheckBox = it as CheckBox
            val responseLabTestContent = labTestList!![position]
            if (myCheckBox.isChecked) {
                labTestList!![position]!!.is_selected = true
                SelectedLabData!!.add(responseLabTestContent)
            } else {
                labTestList!![position]!!.is_selected = false
                SelectedLabData!!.remove(responseLabTestContent)
            }
        }

        if (selectAllCheckbox == true) {
            if (labTestList!![position]!!.checkboxdeclardtatus == false) {
                holder.selectCheckBox.isEnabled = false
                holder.selectCheckBox.isChecked = false
            } else {
                holder.selectCheckBox.isChecked = true
                holder.selectCheckBox.isEnabled = true
            }
        }
    }

    fun getSelectedCheckData(): ArrayList<TestPrecessresponseContent?>? {
        return SelectedLabData
    }

    fun clearAll() {
        this.labTestList?.clear()
        this.SelectedLabData?.clear()
        notifyDataSetChanged()
    }

    fun setData(setContents: List<TestPrecessresponseContent?>?) {
        this.labTestList = setContents as ArrayList<TestPrecessresponseContent?>?
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return labTestList!!.size
    }

    fun addAll(responseContent: List<TestPrecessresponseContent?>?) {
        labTestList?.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(TestPrecessresponseContent())
    }

    fun add(r: TestPrecessresponseContent) {
        labTestList!!.add(r)
        notifyItemInserted(labTestList!!.size - 1)
    }

    fun getItem(position: Int): TestPrecessresponseContent? {
        return labTestList!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        /* val position = labTestList!!.size - 1
         val result = getItem(position)
         if (result != null) {
             labTestList!!.removeAt(position)
             notifyItemRemoved(position)
         }*/
    }

    fun selectAllCheckboxes(ischeckedBox: Boolean) {

        for (i in labTestList!!.indices) {

            if (ischeckedBox) {

                this.labTestList!![i]!!.is_selected = true
                selectAllCheckbox = true

                SelectedLabData!!.add(this.labTestList!![i])

                notifyDataSetChanged()

            } else {
                labTestList!![i]!!.is_selected = false
                selectAllCheckbox = false
                SelectedLabData!!.remove(this.labTestList!![i])
                notifyDataSetChanged()
            }

        }

    }

    interface OnPrintClickListener {
        fun onPrintClick(
            uuid: Int
        )
    }


    fun setOnPrintClickListener(onprintClickListener: OnPrintClickListener) {
        this.onPrintClickListener = onprintClickListener
    }
}
