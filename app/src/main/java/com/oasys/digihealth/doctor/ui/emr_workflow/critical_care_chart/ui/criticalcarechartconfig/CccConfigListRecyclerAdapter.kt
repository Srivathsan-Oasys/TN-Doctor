package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.ui.criticalcarechartconfig

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.configuration.view.StartDragListener
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.config.CriticalCareChartConfigListData
import java.util.*

class CccConfigListRecyclerAdapter(
    private val mStartDragListener: StartDragListener,
    private var configContent: ArrayList<CriticalCareChartConfigListData>
) : RecyclerView.Adapter<CccConfigListRecyclerAdapter.MyViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    private var boolean: Boolean? = false
    private var topos: Int? = 0
    private var from_pos: Int? = 0
    private lateinit var context: Context
    private var arrayListConfigData: ArrayList<CriticalCareChartConfigListData?> = ArrayList()

    class MyViewHolder(rowView: View) : RecyclerView.ViewHolder(rowView) {
        val mTitle: TextView = rowView.findViewById(R.id.config_name)
        internal var imageView: ImageView = rowView.findViewById(R.id.item_remove)
        val headerlayout: LinearLayout = rowView.findViewById(R.id.headerlayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.config_fav_list, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitle.text = configContent[position].name
//        holder.diaplay_order.text = configContent[position]!!.activity?.display_order.toString()
        holder.imageView.setOnClickListener {
            try {
                onItemClickListener?.onItemClick(configContent[position], position)
            } catch (e: Exception) {

            }

        }
        holder.mTitle.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                mStartDragListener.requestDrag(holder)
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return configContent.size
    }

    /*
     override fun onRowSelected(myViewHolder: MyViewHolder) {

         myViewHolder.rowView.setBackgroundColor(Color.GRAY)

     }
     override fun onRowClear(myViewHolder: MyViewHolder) {

         try {
             myViewHolder.rowView.setBackgroundColor(Color.WHITE)
             notifyDataSetChanged();
         }catch (e:Exception)
         {

         }

     }*/
    fun setConfigfavList(configResponseContent: CriticalCareChartConfigListData) {
        boolean = false
        this.configContent.add(configResponseContent)
        notifyDataSetChanged()
    }

    fun getFinalData(): ArrayList<CriticalCareChartConfigListData> {
        return configContent
    }

    interface OnItemClickListener {
        fun onItemClick(configfavResponseContent: CriticalCareChartConfigListData, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun getItemSize(): Int {
        return this.configContent.size
    }

    fun removeitem(position: Int) {
        this.configContent.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount - position)
    }

    fun getConfigFavData(): ArrayList<CriticalCareChartConfigListData> {
        return this.configContent
    }

    fun clearALL() {
        this.configContent.clear()
        notifyDataSetChanged()
    }

    fun setData(configFinalData: ArrayList<CriticalCareChartConfigListData>) {
        this.configContent = configFinalData
        notifyDataSetChanged()
    }

    fun addData(criticalCareChartConfigListData: CriticalCareChartConfigListData) {
        this.configContent.add(criticalCareChartConfigListData)
        notifyDataSetChanged()
    }
}