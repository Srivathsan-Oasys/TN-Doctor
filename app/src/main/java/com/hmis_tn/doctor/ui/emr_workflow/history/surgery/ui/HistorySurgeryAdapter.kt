package com.hmis_tn.doctor.ui.emr_workflow.history.surgery.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response.SurgeryresponseContent
import com.hmis_tn.doctor.utils.Utils

class HistorySurgeryAdapter(
    private val context: Context,
    private var historySurgeryArrayList: List<SurgeryresponseContent?>
) :
    RecyclerView.Adapter<HistorySurgeryAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var utils: Utils = Utils(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_history_surgery, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historySurgeryArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val responseData = historySurgeryArrayList.get(position)
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.dateTextView.text = responseData?.ps_performed_date
        holder.nameTextView.text = responseData?.procedure_name
        holder.institutionTextView.text = responseData?.institution_name
        holder.remarksTextView.text = responseData?.ps_comments


        if (utils.currentDate().equals(utils.compareDate(responseData?.ps_performed_date!!))) {
            holder.editSurgeryData.visibility = View.VISIBLE
        } else {
            holder.editSurgeryData.visibility = View.GONE
        }

        holder.editSurgeryData.setOnClickListener {

            onClickListener!!.OnClick(responseData, position)
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

    interface OnClickListener {
        fun OnClick(
            responseContent: SurgeryresponseContent?,
            position: Int
        )
    }

    fun setOnClickListener(onclick: OnClickListener) {
        this.onClickListener = onclick
    }


    fun setData(responseContents: List<SurgeryresponseContent?>?) {
        historySurgeryArrayList = responseContents!!
        notifyDataSetChanged()
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.serialNumberTextView)
        internal val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        internal val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        internal val institutionTextView: TextView = itemView.findViewById(R.id.institutionTextView)
        internal val remarksTextView: TextView = itemView.findViewById(R.id.remarksTextView)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)
        internal val editSurgeryData: ImageView = itemView.findViewById(R.id.editSurgeryData)
    }

}