package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.PresDrugInfoData
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.row_prescrtiption_drug_info_list.view.*

class AdapterPrescriptionDrugInfoList(var utils: Utils) :
    RecyclerView.Adapter<AdapterPrescriptionDrugInfoList.MyViewHolder>() {

    var listDataDrugInfo = ArrayList<PresDrugInfoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_prescrtiption_drug_info_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = listDataDrugInfo.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindUI(listDataDrugInfo[position], position)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindUI(presDrugInfoData: PresDrugInfoData, position: Int) {
            itemView.apply {
                presDrugInfoData.also { data ->
                    tvSNoDrugInfo.text = (adapterPosition + 1).toString()
                    tvStoreName.text = data.store_master?.store_name ?: ""
                    tvBatchNo.text = data.batch_id
                    if (!data.expiry_date.isNullOrEmpty()) {
                        val expDate = utils.convertDateFormat(
                            data.expiry_date!!,
                            "yyyy-MM-dd HH:mm:ss",
                            "MM-yyyy"
                        )
                        tvExpiryDate.text = expDate
                    }
                    tvQuantity.text = data.quantity.toString()
                }
            }
        }

    }

    fun setData(listDataDrugInfo: ArrayList<PresDrugInfoData>) {
        this.listDataDrugInfo = listDataDrugInfo
        notifyDataSetChanged()
    }

}