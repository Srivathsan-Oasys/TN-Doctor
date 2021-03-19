package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.ui.criticalcarechartconfig

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.config.CriticalCareChartConfigListData

class CccConfigMainRecyclerAdapter(
    private val context: Context,
    private var configResponseContent: ArrayList<CriticalCareChartConfigListData>
) :
    RecyclerView.Adapter<CccConfigMainRecyclerAdapter.MyViewHolder>() {

    private var configlistdata: ArrayList<CriticalCareChartConfigListData> = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.config_list, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return configResponseContent.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var config_Name_TextView: TextView = itemView.findViewById(R.id.config_name)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val configlistdata = configResponseContent[position]
        holder.config_Name_TextView.text = configlistdata.name
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(configlistdata, position)
            this.configResponseContent.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount - position)
        }
    }

    fun setConfigList(configResponse: CriticalCareChartConfigListData) {
        val check = configResponseContent.any { it.uuid == configResponse.uuid }
        if (!check) {
            this.configResponseContent.add(configResponse)
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(configResponseContent: CriticalCareChartConfigListData, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun removeitem(position: Int) {
        for (i in configlistdata.indices) {
            if (configlistdata[i].uuid == position) {
                this.configlistdata.removeAt(i)
                notifyItemRemoved(i)
                notifyItemRangeChanged(i, itemCount - i)
                break
            }
        }
    }

    fun setConfigItem(responseContents: ArrayList<CriticalCareChartConfigListData>) {
        this.configResponseContent = responseContents
        this.configlistdata = responseContents
        notifyDataSetChanged()
    }

    fun getConfigData(): ArrayList<CriticalCareChartConfigListData> {
        return this.configResponseContent
    }

    fun removeall(conficFinalData: ArrayList<CriticalCareChartConfigListData>) {
        for (i in conficFinalData.indices) {
            this.configResponseContent = conficFinalData
            this.configResponseContent.removeAt(0)
            notifyItemRemoved(0)
            notifyItemRangeChanged(0, itemCount - 0)
        }
    }

    fun getItemSize(): Int {
        return this.configResponseContent.size
    }

    fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    configResponseContent = configlistdata
                } else {
                    val filteredList = java.util.ArrayList<CriticalCareChartConfigListData>()
                    for (messageList in configlistdata) {
                        if (messageList.name != null) {
                            if (messageList.name.toLowerCase().contains(
                                    charString
                                )
                            ) {
                                filteredList.add(messageList)
                            }
                        }
                    }
                    configResponseContent = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = configResponseContent
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                configResponseContent =
                    filterResults.values as java.util.ArrayList<CriticalCareChartConfigListData>
                notifyDataSetChanged()
            }
        }
    }

    fun setConfigfavList(configResponse: CriticalCareChartConfigListData) {
        val check = configResponseContent.any { it.uuid == configResponse.uuid }
        if (!check) {
            this.configResponseContent.add(configResponse)
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
        }
    }

    fun clearALL() {
        this.configResponseContent.clear()
        notifyDataSetChanged()
    }

    fun addData(criticalCareChartConfigListData: CriticalCareChartConfigListData) {
        this.configResponseContent.add(criticalCareChartConfigListData)
        notifyDataSetChanged()
    }
}