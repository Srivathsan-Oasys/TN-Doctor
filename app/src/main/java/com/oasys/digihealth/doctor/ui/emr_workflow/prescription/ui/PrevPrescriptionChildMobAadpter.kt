package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.InjectionData
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrescriptionDetail


class PrevPrescriptionChildMobAadpter(
    private val mContext: Context,
    private var prev_Result: List<PrescriptionDetail?>?
) : RecyclerView.Adapter<PrevPrescriptionChildMobAadpter.myViewHolder>() {
    private val mLayoutInflater: LayoutInflater

    private var injectionData: InjectionData = InjectionData()

    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
/*
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.row_manage_pre_fav_temp,
            parent,
            false
        ) as CardView
        return myViewHolder(itemLayout)

*/

        val view =
            LayoutInflater.from(mContext).inflate(R.layout.row_prev_mob_prescription, parent, false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return prev_Result?.size!!

    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val response = prev_Result?.get(position)

        holder.nameTextView.text = response!!.item_master?.name + "- " + response.drug_route?.name + "- " + response.item_master?.strength


        if (response.is_emar!!) {


            holder.detailsTextView.text =
                response.drug_frequency?.name + "- " + "${response.duration} ${response.duration_period?.code}- ${response.prescribed_quantity?.toString() ?: ""} Pills - ${injectionData.store_name}"

        } else {

            holder.detailsTextView.text =
                response.drug_frequency?.name + "- " + "${response.duration} ${response.duration_period?.code}- ${response.prescribed_quantity?.toString() ?: ""} Pills - ${response.drug_instruction?.name}"

        }
    }

    class myViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        internal val mainLinearLayout: CardView = itemView.findViewById(R.id.cardView)
        internal val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        internal val detailsTextView: TextView = itemView.findViewById(R.id.DetailsTextView)
/*
        var sNoTextView: TextView
        var nameTextView: TextView
        var strengthTextView: TextView
        var quantityTextView: TextView
        var routeTextView: TextView
        var frequencyText: TextView
        var durationText: TextView
        var instrationText: TextView

        init {

            sNoTextView = view.findViewById<View>(R.id.sNoTextView) as TextView
            nameTextView = view.findViewById<View>(R.id.nameTextView) as TextView
            strengthTextView = view.findViewById<View>(R.id.strengthTextView) as TextView
            quantityTextView = view.findViewById<View>(R.id.quantityTextView) as TextView
            routeTextView = view.findViewById(R.id.routeTextView)
            frequencyText = view.findViewById(R.id.frequencyTextView)
            durationText = view.findViewById(R.id.durationTextView)

            instrationText = view.findViewById(R.id.instructionTextView)
        }*/


    }


    fun setInjectionData(injectiondata: InjectionData) {


        injectionData = injectiondata

        notifyDataSetChanged()


    }
}