package com.oasys.digihealth.doctor.ui.emr_workflow.history.lab.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.history.lab.model.LabResModel
import com.oasys.digihealth.doctor.utils.Utils

class HistoryLabAdapter(
    private val context: Context,
    private var historyLabArrayList: List<LabResModel?>
) :
    RecyclerView.Adapter<HistoryLabAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_history_lab, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyLabArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        try {
            val responseData = historyLabArrayList.get(position)
            holder.historyLabSerialno.text = (position + 1).toString()
            val utils = Utils(holder.itemView.context)
            val date = utils.displayDate(responseData?.created_date ?: "", "yyyy-MM-dd HH:mm:ss")
            holder.historyLabDate.text = date
            holder.historyLabCode.text = responseData?.code
            holder.historyLabName.text = responseData?.name
            holder.historyLabType.text = responseData?.type
            holder.historyLabOrderLocation.text = responseData?.order_to_location

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


    fun setData(responseContents: List<LabResModel>?) {
        historyLabArrayList = responseContents!!
        notifyDataSetChanged()
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val historyLabSerialno: TextView = itemView.findViewById(R.id.historyLabSerialno)
        internal val historyLabDate: TextView = itemView.findViewById(R.id.historyLabDate)
        internal val historyLabCode: TextView = itemView.findViewById(R.id.historyLabCode)
        internal val historyLabName: TextView = itemView.findViewById(R.id.historyLabName)
        internal val historyLabType: TextView = itemView.findViewById(R.id.historyLabType)
        internal val historyLabOrderLocation: TextView =
            itemView.findViewById(R.id.historyLabOrderLocation)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)
    }

}