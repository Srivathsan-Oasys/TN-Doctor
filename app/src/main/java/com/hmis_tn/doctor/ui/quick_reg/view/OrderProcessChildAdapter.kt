package com.hmis_tn.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto

class OrderProcessChildAdapter(context: Context, private val labTestList: List<RecyclerDto>) :
    RecyclerView.Adapter<OrderProcessChildAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mContext: Context = context
    var orderNumString: String? = null
    var status: String? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNumberTextView: TextView = view.findViewById(R.id.serialNumberTextView)
        var textProcess: TextView = view.findViewById(R.id.textProcess)
        var result_text: Spinner = view.findViewById(R.id.result_spinner)
        var uom_text: TextView = view.findViewById(R.id.uom_text)
        var normal_val_text: TextView = view.findViewById(R.id.normal_val_text)
        var mainLinearLayout: LinearLayout = view.findViewById(R.id.mainLinearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_approval_result_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = labTestList[position].toString()
        holder.serialNumberTextView.text = (position + 1).toString()
        val movie = labTestList[position]
        holder.textProcess.text = movie.title
/*
        status = holder.status.text as String?
*/
        holder.normal_val_text.text = movie.year
        holder.uom_text.text = movie.genre

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
        return labTestList.size
    }
}