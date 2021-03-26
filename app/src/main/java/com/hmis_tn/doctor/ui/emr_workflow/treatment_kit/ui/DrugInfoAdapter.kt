package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.drug_info.ResponseContent
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.item_drug_info.view.*

class DrugInfoAdapter(
    private val list: ArrayList<ResponseContent>
) : RecyclerView.Adapter<DrugInfoAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_drug_info, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val drugInfo = list[position]
        holder.itemView.tvStoreName.text = drugInfo.store_master?.store_name ?: ""
        holder.itemView.tvBatchNo.text = drugInfo.batch_id ?: ""
        holder.itemView.tvExpiryDate.text = Utils(holder.itemView.context).convertDateFormat(
            drugInfo.expiry_date ?: "",
            "yyyy-MM-dd HH:mm:ss",
            "MMM yyyy"
        )
        holder.itemView.tvQty.text = drugInfo.quantity?.toString() ?: "0"
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
