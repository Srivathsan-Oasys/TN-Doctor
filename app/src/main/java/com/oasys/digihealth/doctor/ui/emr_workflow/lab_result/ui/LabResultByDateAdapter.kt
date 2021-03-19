package com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.compare.ResponseContent
import kotlinx.android.synthetic.main.lab_result_by_date_row.view.*

class LabResultByDateAdapter(
    private val mContext: Context,
    private val compareDateList: java.util.ArrayList<String>,
    private var labCompareRespList: List<ResponseContent>?
) : RecyclerView.Adapter<LabResultByDateAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.lab_result_by_date_row,
            viewGroup,
            false
        ) as LinearLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val labCompareResp = labCompareRespList?.get(position)
//        val pos = if (position % 2 == 0) {
//            position / 2 + 1
//        } else {
//            (position + 1) / 2
//        }
        val pos = position + 1
        holder.itemView.sNoTextView.text = pos.toString()
        holder.itemView.observationTextView.text = labCompareResp?.test_or_analyte ?: ""
        val referenceRange =
            labCompareResp?.order_request_batches?.get(0)?.test_or_analyte_ref_min.toString() + "-" +
                    labCompareResp?.order_request_batches?.get(0)?.test_or_analyte_ref_max.toString()
        holder.itemView.referenceRange.text = referenceRange

//        val utils = Utils(holder.itemView.context)
//        if (labCompareResp?.order_request_date ==
//            utils.convertUtcToIst(compareDateList[0], "yyyy-MM-dd HH:mm:ss")
//        ) {
//            holder.itemView.tvDate1.text =
//                labCompareResp.order_request_batches?.get(0)?.result_value
//        } else if (labCompareResp?.order_request_date ==
//            utils.convertUtcToIst(compareDateList[1], "yyyy-MM-dd HH:mm:ss")
//        ) {
//            holder.itemView.tvDate2.text =
//                labCompareResp.order_request_batches?.get(0)?.result_value
//        }

        holder.itemView.tvDate1.text = labCompareResp?.date1
        holder.itemView.tvDate2.text = labCompareResp?.date2
    }

    override fun getItemCount(): Int {
        return (labCompareRespList?.size ?: 0)
    }

    fun setData(LabResultByDateList: ArrayList<ResponseContent>) {
        this.labCompareRespList = LabResultByDateList
        notifyDataSetChanged()
    }
}
