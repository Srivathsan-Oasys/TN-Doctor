package com.oasys.digihealth.doctor.ui.emr_workflow.history.radiology.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.history.radiology.model.RadiologyResModel
import com.oasys.digihealth.doctor.utils.Utils

class HistoryRadiologyAdapter(
    private val context: Context,
    private var historyRadiologyArrayList: List<RadiologyResModel?>
) :
    RecyclerView.Adapter<HistoryRadiologyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_history_radiology, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyRadiologyArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseData = historyRadiologyArrayList.get(position)
        holder.historyRadioSerialno.text = (position + 1).toString()
        val utils = Utils(holder.itemView.context)
        val date = utils.displayDate(responseData?.created_date ?: "", "yyyy-MM-dd HH:mm:ss")
        holder.historyRadioDate.text = date
        holder.historyRadioCode.text = responseData?.code
        holder.historyRadioName.text = responseData?.name
        holder.historyRadioType.text = responseData?.type
        holder.historyRadioOrderLocation.text = responseData?.order_to_location


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


    fun setData(responseContents: List<RadiologyResModel>?) {
        historyRadiologyArrayList = responseContents!!
        notifyDataSetChanged()
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val historyRadioSerialno: TextView =
            itemView.findViewById(R.id.historyRadioSerialno)
        internal val historyRadioDate: TextView = itemView.findViewById(R.id.historyRadioDate)
        internal val historyRadioCode: TextView = itemView.findViewById(R.id.historyRadioCode)
        internal val historyRadioName: TextView = itemView.findViewById(R.id.historyRadioName)
        internal val historyRadioType: TextView = itemView.findViewById(R.id.historyRadioType)
        internal val historyRadioOrderLocation: TextView =
            itemView.findViewById(R.id.historyRadioOrderLocation)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)
    }

}