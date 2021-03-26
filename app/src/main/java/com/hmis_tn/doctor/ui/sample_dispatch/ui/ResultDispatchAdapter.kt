package com.hmis_tn.doctor.ui.sample_dispatch.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.resultdispatch.model.ResponseContentsResultDispatch


class ResultDispatchAdapter(
    context: Context,
    private var resonseresultdispatchList: ArrayList<ResponseContentsResultDispatch?>?
) :
    RecyclerView.Adapter<ResultDispatchAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater
    private var isLoadingAdded = false
    private val mContext: Context
    private var onItemClickListener: OnItemClickListener? = null
    var orderNumString: String? = null

    var status: String? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var patient_info: TextView
        var TestName: TextView
        var orderedNUm: TextView
        var approvedBy: TextView
        var ic_print: ImageView

        var serialNum: TextView
        var mainLinearLayout: LinearLayout

        init {
            patient_info = view.findViewById<View>(R.id.patient_info) as TextView
            TestName = view.findViewById<View>(R.id.TestName) as TextView
            orderedNUm = view.findViewById<View>(R.id.orderedNUm) as TextView
            approvedBy = view.findViewById<View>(R.id.approvedBy) as TextView
            ic_print = view.findViewById<View>(R.id.pdf) as ImageView

            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_result_dispatch_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.serialNum.text = (position + 1).toString()

        val ResultDispathData = this.resonseresultdispatchList!![position]



        if (ResultDispathData!!.patient_order_detail?.vw_patient_info?.pattitle != null) {

            holder.patient_info.text =
                ResultDispathData.patient_order_detail?.vw_patient_info?.pattitle + " " + ResultDispathData.patient_order_detail?.vw_patient_info?.first_name + "/" + ResultDispathData.patient_order_detail?.vw_patient_info?.ageperiod

        } else {

            holder.patient_info.text =
                ResultDispathData.patient_order_detail?.vw_patient_info?.first_name + "/" + ResultDispathData.patient_order_detail?.vw_patient_info?.ageperiod
        }
        holder.TestName.text =
            ResultDispathData.patient_order_detail?.test_master!!.name.toString()
        holder.orderedNUm.text =
            ResultDispathData.patient_order_detail?.patient_order?.order_number.toString()
        holder.approvedBy.text = ResultDispathData.approved_details?.first_name.toString()

        holder.ic_print.setOnClickListener {

            onItemClickListener?.onItemClick(ResultDispathData, position)

        }


        /*

        holder.patient_info.text = labAllData!!.title
        holder.pinNum.text=labAllData!!.genre
        holder.TestName.text = labAllData.year

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
    }

    override fun getItemCount(): Int {
        return resonseresultdispatchList!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }

    fun setData(responseContent: List<ResponseContentsResultDispatch?>?) {
        this.resonseresultdispatchList =
            responseContent as ArrayList<ResponseContentsResultDispatch?>?
        notifyDataSetChanged()
    }

    fun addAll(responseContent: List<ResponseContentsResultDispatch?>?) {
        this.resonseresultdispatchList!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.resonseresultdispatchList?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(ResponseContentsResultDispatch())
    }

    fun add(r: ResponseContentsResultDispatch) {
        resonseresultdispatchList!!.add(r)
        notifyItemInserted(resonseresultdispatchList!!.size - 1)
    }

    fun getItem(position: Int): ResponseContentsResultDispatch? {
        return resonseresultdispatchList!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        /*  val position = resonseresultdispatchList!!.size - 1
          val result = getItem(position)
          if (result != null) {
              resonseresultdispatchList!!.removeAt(position)
              notifyItemRemoved(position)
          }*/
    }

    interface OnItemClickListener {
        fun onItemClick(responseContent: ResponseContentsResultDispatch?, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


}