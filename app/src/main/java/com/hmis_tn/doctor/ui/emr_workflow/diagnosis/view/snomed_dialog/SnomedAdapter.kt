package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedData


class SnomedAdapter(
    private val context: Activity,
    private var arrayListLabFavList: ArrayList<SnomedData>
) : RecyclerView.Adapter<SnomedAdapter.MyViewHolder>() {
    private var onViewClickListener: OnViewClickListener? = null

    private var selectposition: Int? = null

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView = itemView.findViewById(R.id.snomedSno)
        internal val conceptView: ImageView = itemView.findViewById(R.id.snomedView)
        internal val conceptName: TextView = itemView.findViewById(R.id.snomedName)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)
        internal val conceptId: TextView = itemView.findViewById(R.id.snomedId)
    }

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_snomed_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (arrayListLabFavList.size != 0) {
            val response = arrayListLabFavList[position]

            holder.serialNumberTextView.text = (position + 1).toString()

            holder.conceptId.text = arrayListLabFavList[position].ConceptId

            holder.conceptName.text = arrayListLabFavList[position].ConceptName

        } else {


        }



        holder.conceptView.setOnClickListener {

            onViewClickListener!!.onViewClick(arrayListLabFavList[position])

        }
        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        }

        if (selectposition != null && selectposition == position) {

            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.hmis_background
                )
            )
            holder.serialNumberTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.conceptId.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.conceptName.setTextColor(ContextCompat.getColor(context, R.color.white))


        }

        holder.mainLinearLayout.setOnClickListener {

            selectposition = position

            notifyDataSetChanged()
        }


    }

    fun setData(snomedData: ArrayList<SnomedData>) {

        this.arrayListLabFavList = snomedData

        selectposition = null

        notifyDataSetChanged()

    }

    interface OnViewClickListener {
        fun onViewClick(
            favouritesModel: SnomedData?
        )
    }

    fun setOnViewClickListener(onViewClickListener1: OnViewClickListener) {
        this.onViewClickListener = onViewClickListener1
    }

    fun getFirstData(): SnomedData {

        return this.arrayListLabFavList[0]

    }

    fun getitemsize(): Int {

        return arrayListLabFavList.size

    }
}


