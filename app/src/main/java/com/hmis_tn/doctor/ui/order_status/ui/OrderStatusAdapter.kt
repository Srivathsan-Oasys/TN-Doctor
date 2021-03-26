package com.hmis_tn.doctor.ui.order_status.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.order_status.model.OrderStatusResponseContent


class OrderStatusAdapter(context: Context) :
    RecyclerView.Adapter<OrderStatusAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater
    private var isLoadingAdded = false
    private val mContext: Context
    var orderNumString: String? = null
    var status: String? = null
    private var labTestList: ArrayList<OrderStatusResponseContent?>? = ArrayList()


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var patient_info: TextView
        var pinNum: TextView
        var TestName: TextView
        var serialNum: TextView
        var orderStatus: TextView

        var mainLinearLayout: LinearLayout

        init {
            patient_info = view.findViewById<View>(R.id.patient_info) as TextView
            pinNum = view.findViewById<View>(R.id.pinNum) as TextView
            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            TestName = view.findViewById<View>(R.id.TestName) as TextView
            orderStatus = view.findViewById<View>(R.id.order_status) as TextView

            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_order_status_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.serialNum.text = (position + 1).toString()
        val labAllData = this.labTestList!![position]



        if (labAllData!!.pattitle != null) {

            holder.patient_info.text =
                labAllData.pattitle + "" + labAllData.first_name + "/" + labAllData.ageperiod

        } else {

            holder.patient_info.text = labAllData.first_name + " " + labAllData.ageperiod
        }

        holder.pinNum.text = labAllData.uhid
        holder.TestName.text = labAllData.test_name

        holder.orderStatus.text = labAllData.order_status_name


        if (labTestList!![position]!!.order_status_uuid == 7) {

            if (labTestList!![position]!!.order_status_uuid == 2) {

                holder.orderStatus?.text = labTestList!![position]!!.order_status_name
            } else {

                holder.orderStatus?.text = labTestList!![position]!!.auth_status_name
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

    override fun getItemCount(): Int {
        return labTestList!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }

    fun addAll(responseContent: List<OrderStatusResponseContent?>?) {
        this.labTestList?.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun add(r: OrderStatusResponseContent) {
        labTestList!!.add(r)
        notifyItemInserted(labTestList!!.size - 1)
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
    }

    fun getItem(position: Int): OrderStatusResponseContent? {
        return labTestList!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

    }

    fun clearAll() {
        labTestList?.clear()
        notifyDataSetChanged()
    }


}