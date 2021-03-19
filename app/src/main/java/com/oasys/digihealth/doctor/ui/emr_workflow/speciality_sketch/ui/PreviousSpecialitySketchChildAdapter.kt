package com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.PatientDietOrder
import kotlinx.android.synthetic.main.item_prev_diet_child.view.*

class PreviousSpecialitySketchChildAdapter(private val list: ArrayList<PatientDietOrder>) :
    RecyclerView.Adapter<PreviousSpecialitySketchChildAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prev_diet_child, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prevDietOrder = list[position]
        holder.itemView.tvSNo.text = "${position + 1}"
        holder.itemView.tvDietType.text = prevDietOrder.diet_name
    }
}
