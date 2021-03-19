package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.previous_vitals.PV
import com.oasys.digihealth.doctor.utils.Utils
import kotlinx.android.synthetic.main.item_previous_vitals_child.view.*

class PreviousVitalsChildAdapter(
    private val list: List<PV>
) : RecyclerView.Adapter<PreviousVitalsChildAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_previous_vitals_child, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pv = list[position]

        holder.itemView.let {
            val utils = Utils(it.context)
            it.tvSno.text = "${position + 1}"
            it.tvVital.text = pv.vital_name
            it.tvVitalValue.text = pv.vital_value
            it.tvUom.text = pv.uom_name
            it.tvDateTime.text = pv.vital_performed_date?.let { it1 ->
                utils.displayDate(
                    it1,
                    "yyyy-MM-dd HH:mm:ss"
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
