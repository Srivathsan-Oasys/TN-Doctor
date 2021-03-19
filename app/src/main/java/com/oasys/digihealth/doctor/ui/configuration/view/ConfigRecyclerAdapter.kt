package com.oasys.digihealth.doctor.ui.configuration.view


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.configuration.model.ConfigResponseContent


class ConfigRecyclerAdapter(private val mContext: Context?, private var configResponseContent: ArrayList<ConfigResponseContent?>) :
    RecyclerView.Adapter<ConfigRecyclerAdapter.MyViewHolder>() {
    private var configlistdata: ArrayList<ConfigResponseContent?> = ArrayList()
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
        internal var config_Name_TextView: TextView
        init {
            config_Name_TextView  = itemView.findViewById(R.id.config_name)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val configlistdata = configResponseContent[position]
        holder.config_Name_TextView.text = configlistdata?.activity?.name
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(configlistdata!!, position)
        }
    }
    fun setConfigList(configResponse: ConfigResponseContent?) {

        val check= configResponseContent.any{ it!!.activity_uuid == configResponse?.activity_uuid}
        if (!check) {
            this.configResponseContent.add(configResponse)
            notifyDataSetChanged()
        }
        else{
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(configResponseContent: ConfigResponseContent, position: Int)
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
    fun removeitem(position: Int) {
        this.configResponseContent.removeAt(position)
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }



    fun setConfigItem(responseContents: ArrayList<ConfigResponseContent?>) {
        this.configResponseContent = responseContents
        this.configlistdata = responseContents
        notifyDataSetChanged()

    }


    fun getConfigData(): ArrayList<ConfigResponseContent?> {
        return this.configResponseContent
    }

    fun removeall(conficFinalData: ArrayList<ConfigResponseContent?>?) {
        for(i in conficFinalData!!.indices)
        {
            this.configResponseContent = conficFinalData
            this.configResponseContent.removeAt(0)
            notifyItemRemoved(0);
            notifyItemRangeChanged(0, getItemCount() - 0);

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

                    val filteredList = java.util.ArrayList<ConfigResponseContent?>()
                    for (messageList in configlistdata!!) {
                        if (messageList?.activity?.name != null) {
                            if (messageList?.activity?.name?.toLowerCase()?.contains(
                                    charString
                                )!!
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
                configResponseContent = filterResults.values as java.util.ArrayList<ConfigResponseContent?>
                notifyDataSetChanged()
            }
        }
    }

    fun setConfigfavList(configResponse: ConfigResponseContent?) {

        val check= configResponseContent.any{ it!!.activity_uuid == configResponse?.activity_uuid}
        if (!check) {
            this.configResponseContent.add(configResponse)
            notifyDataSetChanged()
        }
        else{
            notifyDataSetChanged()
        }

    }

/*    fun setConfigItemRemove(responseContents: ResponseContent?) {

        Log.i("",""+responseContents)

        val check= configResponseContent.any{ it!!.activity_uuid == responseContents?.activity_id}
        if (check) {
            this.configResponseContent.remo
            notifyDataSetChanged()
        }
        else{
            notifyDataSetChanged()
        }

    }*/

    fun clearALL() {
        this?.configResponseContent?.clear()
        notifyDataSetChanged()
    }

}



