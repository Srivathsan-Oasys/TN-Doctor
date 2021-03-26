package com.hmis_tn.doctor.ui.dashboard.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.dashboard.model.registration.CovidRegistrationSearchResponseContent


class SearchPatientListAdapter(
    private val context: Context,
    private var historySurgeryArrayList: List<CovidRegistrationSearchResponseContent?>
) :
    RecyclerView.Adapter<SearchPatientListAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_search_patient_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historySurgeryArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val responseData = historySurgeryArrayList.get(position)
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.pinTextView.text = responseData?.uhid
        holder.nameTextView.text = responseData?.first_name
        if (responseData?.gender_uuid == 1) {
            holder.genderTextView.text = "Male"
        } else {
            holder.genderTextView.text = "Female"
        }

        holder.dateVisitTextView.text = responseData?.registered_date
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(responseData, position)
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
            responseContent: CovidRegistrationSearchResponseContent?,
            position: Int
        )
    }

    fun setOnClickListener(onclick: OnClickListener) {
        this.onClickListener = onclick
    }


    fun setData(responseContents: List<CovidRegistrationSearchResponseContent?>?) {
        historySurgeryArrayList = responseContents!!
        notifyDataSetChanged()
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.serialNumberTextView)
        internal val pinTextView: TextView = itemView.findViewById(R.id.pinTextView)
        internal val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        internal val genderTextView: TextView = itemView.findViewById(R.id.genderTextView)
        internal val dateVisitTextView: TextView = itemView.findViewById(R.id.dateVisitTextView)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)
    }

    interface OnItemClickListener {
        fun onItemClick(responseContent: CovidRegistrationSearchResponseContent?, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


}