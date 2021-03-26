package com.hmis_tn.doctor.ui.emr_workflow.diet.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.PatientDiet
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.PatientDietOrder
import kotlinx.android.synthetic.main.item_prev_diet.view.*
import java.text.SimpleDateFormat

class PrevDietAdapter(
    private val list: ArrayList<PatientDiet>,
    private val callback: DietCallback,
    private val compressAllOthers: (previousDiet: PatientDiet) -> Unit
) :
    RecyclerView.Adapter<PrevDietAdapter.MyViewHolder>() {

    private val prevDietOrderList = ArrayList<PatientDietOrder>()
    private var prevDietChildAdapter: PreviousDietChildAdapter? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prev_diet, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val previousDiet = list[position]
        var encounter: String? = null
        if (previousDiet.encounter_type_uuid == 2) {
            encounter = "IP"
        } else if (previousDiet.encounter_type_uuid == 1) {
            encounter = "OP"
        } else {
            encounter = "IP"
        }


        try {
            val inputPattern = "yyyy-MM-dd"
            val outputPattern = "dd-MMM-yyyy"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)
            val date = inputFormat.parse(previousDiet.created_date)
            val tarDate = outputFormat.format(date)
            holder.itemView.tvBloodRequestTime.text =
                tarDate + " 12:00 AM - " + previousDiet.department_name + " - " + encounter
        } catch (e: Exception) {
            e.printStackTrace()
        }


        holder.itemView.tvRequestedBy.text = previousDiet.doctor_name ?: ""
        holder.itemView.tvStatus.text = previousDiet.diet_order_status ?: ""

        if (previousDiet.isExpanded)
            holder.itemView.llResult.visibility = View.VISIBLE
        else
            holder.itemView.llResult.visibility = View.GONE

        prevDietOrderList.clear()
        previousDiet.patient_dietOrder_list?.forEach { patientDietOrder ->
            prevDietOrderList.add(patientDietOrder)
        }

        holder.itemView.recyclerViewPrevDietChild.layoutManager =
            LinearLayoutManager(holder.itemView.recyclerViewPrevDietChild.context)
        prevDietChildAdapter = PreviousDietChildAdapter(prevDietOrderList)
        holder.itemView.recyclerViewPrevDietChild.adapter = prevDietChildAdapter

        holder.itemView.imgDown.setOnClickListener {
            compressAllOthers(previousDiet)
            previousDiet.isExpanded = true
            notifyDataSetChanged()
        }

        holder.itemView.viewButton.setOnClickListener {
            callback.OnviewClick(previousDiet.patient_dietOrder_list?.get(0))
        }

        holder.itemView.repeatLab.setOnClickListener {
            callback.OnviewClick(previousDiet.patient_dietOrder_list?.get(0))
        }

        holder.itemView.modifyLab.setOnClickListener {
            callback.OnviewClick(previousDiet.patient_dietOrder_list?.get(0))
        }

    }
}