package com.hmis_tn.doctor.ui.emr_workflow.history.vitals.model.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.history.vitals.model.response.HistoryVitalResponseContent

class HistoryVitalsAdapter(
    private val context: Context,
    private var historyVitalsArrayList: List<HistoryVitalResponseContent?>
) :
    RecyclerView.Adapter<HistoryVitalsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_history_vitals, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyVitalsArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.historyVitalSerialno.text = (position + 1).toString()
        holder.historyVitalDate.text = (historyVitalsArrayList.get(position)?.vital_performed_date)
        holder.historyVitalName.text = (historyVitalsArrayList[position]?.vital_name)
        holder.historyVitalValue.text = (historyVitalsArrayList[position]?.vital_value)
        holder.historyVitalUOM.text = (historyVitalsArrayList[position]?.uom_name)


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

    fun setData(responseContents: List<HistoryVitalResponseContent?>?) {
        historyVitalsArrayList = responseContents!!
        notifyDataSetChanged()
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val historyVitalSerialno: TextView =
            itemView.findViewById(R.id.historyVitalSerialno)
        internal val historyVitalDate: TextView = itemView.findViewById(R.id.historyVitalDate)
        internal val historyVitalName: TextView = itemView.findViewById(R.id.historyVitalName)
        internal val historyVitalValue: TextView = itemView.findViewById(R.id.historyVitalValue)
        internal val historyVitalUOM: TextView = itemView.findViewById(R.id.historyVitalUOM)
        internal val mainLinearLayout: LinearLayoutCompat =
            itemView.findViewById(R.id.mainLinearLayout)
    }

}