package com.hmis_tn.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.LabTestApprovalresponseContent

class LabTestApprovalAdapter(
    context: Context,
    private var labTestApproval: ArrayList<LabTestApprovalresponseContent?>?
) :
    RecyclerView.Adapter<LabTestApprovalAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null
    var status: String? = null
    var selectAllCheckbox: Boolean? = false
    private var isLoadingAdded = false
    private var RTLabData: ArrayList<LabTestApprovalresponseContent?>? = ArrayList()
    private var positive_value: Int = 0
    private var negative_value: Int = 0
    private var equavel_value: Int = 0
    private var rejected_value: Int = 0

    private var onPrintClickListener: OnPrintClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var patient_info: TextView
        var status: TextView
        var dateText: TextView
        var testname: TextView
        var testMethod: TextView
        var orderNo: TextView
        var mainLinearLayout: LinearLayout
        var selectCheckBox: CheckBox
        var radioGroup: RadioGroup
        var radioButton1: RadioButton
        var radioButton2: RadioButton
        var radioButton3: RadioButton
        var print: ImageView

        init {
            patient_info = view.findViewById<View>(R.id.patient_info) as TextView
            dateText = view.findViewById<View>(R.id.dateText) as TextView
            status = view.findViewById<View>(R.id.status) as TextView
            orderNo = view.findViewById<View>(R.id.orderNo) as TextView
            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)
            selectCheckBox = view.findViewById(R.id.checkbox)
            radioGroup = view.findViewById(R.id.radioGroup)
            radioButton1 = view.findViewById(R.id.radiobutton1)
            radioButton2 = view.findViewById(R.id.radiobutton2)
            radioButton3 = view.findViewById(R.id.radiobutton3)
            testMethod = view.findViewById(R.id.testMethod)
            testname = view.findViewById(R.id.test_name)

            print = view.findViewById(R.id.print)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_lab_test_process_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //orderNumString = labTestList[position].toString()
        val labAllData = labTestApproval!![position]
        holder.patient_info.text =
            labAllData!!.first_name + "/" + labAllData!!.ageperiod + "/" + labAllData.gender_name
        holder.dateText.text = labAllData.order_request_date
        // holder.status.text = labAllData.order_status_name
        holder.testMethod.text = labAllData.test_method_name
        holder.selectCheckBox.isChecked = labAllData.is_selected!!
        holder.orderNo.text = labAllData.order_number.toString()
        holder.testname.text = labAllData.test_name


        if (labTestApproval!![position]!!.order_status_uuid == 7 && labTestApproval!![position]!!.order_status_uuid != null) {

            holder?.status.setText(labTestApproval!![position]!!.auth_status_name)
        } else {

            holder?.status.setText(labTestApproval!![position]!!.order_status_name)
        }


        /*
         if(labTestApproval!![position]!!.order_status_uuid ==2)
          {

              holder?.status.setText(labTestApproval!![position]!!.order_status_name)
          }

          */

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


        if (this.labTestApproval!![position]!!.auth_status_uuid == 4) {

            holder.print.visibility = View.VISIBLE
        }

        holder.print.setOnClickListener {

            onPrintClickListener?.onPrintClick(this.labTestApproval!![position]!!.uuid!!)

            //     this.labTestList!![position]!!.uuid

        }


        if (labTestApproval!![position]!!.test_method_uuid != 3 && labTestApproval!![position]!!.test_code == "COVID") {

            holder.radioGroup.visibility = View.VISIBLE

        } else {

            holder.radioGroup.visibility = View.GONE
        }



        if (labTestApproval!![position]!!.order_status_uuid == 7 && labTestApproval!![position]!!.auth_status_uuid == 4 || labTestApproval!![position]!!.order_status_uuid == 2) {

            if (labTestApproval!![position]!!.test_method_uuid != 3 && labTestApproval!![position]!!.test_code == "COVID") {

                holder.radioGroup.visibility = View.VISIBLE

            }
            if (labTestApproval!![position]!!.order_status_uuid == 2) {

                holder?.status?.setText(labTestApproval!![position]!!.order_status_name)
            } else {

                holder?.status.setText(labTestApproval!![position]!!.auth_status_name)
            }

            labTestApproval!![position]!!.checkboxdeclardtatus = false

            when {

                labTestApproval!![position]!!.qualifier_uuid == 2 -> {

                    holder.radioButton1.isChecked = true

                }
                labTestApproval!![position]!!.qualifier_uuid == 1 -> {

                    holder.radioButton2.isChecked = true

                }
                labTestApproval!![position]!!.qualifier_uuid == 3 -> {

                    holder.radioButton3.isChecked = true
                }
            }
        } else {
            labTestApproval!![position]!!.checkboxdeclardtatus = true

            holder.radioGroup.visibility = View.GONE

        }

        if (labTestApproval!![position]!!.checkboxdeclardtatus == false) {
            holder.radioButton1.isEnabled = false

            holder.radioButton2.isEnabled = false

            holder.radioButton3.isEnabled = false
            holder.selectCheckBox.isEnabled = false

            holder?.status?.setTextColor(Color.parseColor("#FF0000"))

            when {

                labTestApproval!![position]!!.qualifier_uuid == 2 -> {

                    holder.radioButton1.isChecked = true

                }
                labTestApproval!![position]!!.qualifier_uuid == 1 -> {

                    holder.radioButton2.isChecked = true

                }
                labTestApproval!![position]!!.qualifier_uuid == 3 -> {

                    holder.radioButton3.isChecked = true
                }
            }

        } else {
            holder.radioButton1.isEnabled = false
            holder.radioButton2.isEnabled = false
            holder.radioButton3.isEnabled = false
            holder.selectCheckBox.isEnabled = true
            holder?.status?.setTextColor(Color.parseColor("#000000"))


            when {

                labTestApproval!![position]!!.qualifier_uuid == 2 -> {

                    holder.radioButton1.isChecked = true

                }
                labTestApproval!![position]!!.qualifier_uuid == 1 -> {

                    holder.radioButton2.isChecked = true

                }
                labTestApproval!![position]!!.qualifier_uuid == 3 -> {

                    holder.radioButton3.isChecked = true
                }
            }
        }


        holder.selectCheckBox.setOnClickListener {

            val myCheckBox = it as CheckBox
            val responseLabTestContent = labTestApproval!![position]
            if (myCheckBox.isChecked) {

                responseLabTestContent!!.is_selected = true

                RTLabData!!.add(responseLabTestContent)

                if (responseLabTestContent.test_method_uuid == 2 && responseLabTestContent.radioselectName == 0) {

                    Toast.makeText(this.mContext, "Please select one Result", Toast.LENGTH_SHORT)
                        .show()
                }


            } else {
                holder.radioGroup.clearCheck()

                labTestApproval!![position]!!.radioselectName = 0

                responseLabTestContent!!.is_selected = false

                RTLabData!!.remove(responseLabTestContent)

            }

        }
        if (selectAllCheckbox == true) {

            if (labTestApproval!![position]!!.checkboxdeclardtatus == false) {
                holder.selectCheckBox.isEnabled = false
                holder.selectCheckBox.isChecked = false
            } else {
                holder.selectCheckBox.isChecked = true
                holder.selectCheckBox.isEnabled = true
            }
        }


        if (this.labTestApproval!![position]!!.qualifier_uuid == 2) {

            holder.radioButton1.isChecked = true

        } else if (this.labTestApproval!![position]!!.qualifier_uuid == 1) {

            holder.radioButton2.isChecked = true
        } else if (this.labTestApproval!![position]!!.qualifier_uuid == 3) {

            holder.radioButton3.isChecked = true
        }


    }

    override fun getItemCount(): Int {
        return labTestApproval!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }

    fun getSelectedCheckData(): ArrayList<LabTestApprovalresponseContent?>? {

        return RTLabData
    }


    fun selectAllCheckbox(ischeckedBox: Boolean) {

        for (i in labTestApproval!!.indices) {

            if (ischeckedBox) {

                labTestApproval!![i]!!.is_selected = true
                selectAllCheckbox = true

                RTLabData!!.add(labTestApproval!![i])

                notifyDataSetChanged()

            } else {
                labTestApproval!![i]!!.is_selected = false
                selectAllCheckbox = false
                RTLabData!!.remove(labTestApproval!![i])
                notifyDataSetChanged()
            }

        }

    }

    fun addAll(responseContent: List<LabTestApprovalresponseContent?>?) {


        /*     for(i in responseContent!!.indices){

                 val check= this.labTestApproval!!.any{ it!!.order_number == responseContent[i]!!.order_number}

                 if(!check){

                     this.labTestApproval!!.add(responseContent[i]!!)

                 }

             }
     */

        this.labTestApproval!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.RTLabData?.clear()
        this.labTestApproval?.clear()
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

/*
    fun positiveValue():Int{
        val value=arrayOf(labTestApproval.qualifier_uuid.get("0")).count()

    }
*/


    interface OnPrintClickListener {
        fun onPrintClick(
            uuid: Int
        )
    }


    fun setOnPrintClickListener(onprintClickListener: OnPrintClickListener) {
        this.onPrintClickListener = onprintClickListener
    }

}