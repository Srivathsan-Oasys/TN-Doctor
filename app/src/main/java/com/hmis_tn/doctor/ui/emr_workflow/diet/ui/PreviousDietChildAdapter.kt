package com.hmis_tn.doctor.ui.emr_workflow.diet.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.PatientDietOrder
import kotlinx.android.synthetic.main.item_prev_diet_child.view.*

class PreviousDietChildAdapter(private val list: ArrayList<PatientDietOrder>) :
    RecyclerView.Adapter<PreviousDietChildAdapter.MyViewHolder>() {

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
        holder.itemView.tvQty.text = prevDietOrder.quantity.toString()
        holder.itemView.tvCategory.text = prevDietOrder.diet_category_name
        holder.itemView.tvfrequency.text = prevDietOrder.diet_frequency_name
    }
}
