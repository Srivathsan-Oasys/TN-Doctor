package com.hmis_tn.doctor.ui.emr_workflow.investigation_result.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.investigation_result.model.Repsonse
import com.squareup.picasso.Picasso


class InvestigationResultChildAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<InvestigationResultChildAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var investigationGallery: ArrayList<Repsonse> = ArrayList()
//    private var investigationResultList: ArrayList<InvestigationResultResponseContent> = ArrayList()


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var RadiologyImageview: ImageView = view.findViewById<View>(R.id.imageView) as ImageView
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.investigation_result_child_layout,
            viewGroup,
            false
        ) as CardView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prevList = investigationGallery.get(position)

        if (!prevList.work_order_attachment_file_path.isNullOrEmpty())
            Picasso.get()
                .load(prevList.work_order_attachment_file_path)
                .placeholder(R.drawable.ic_baseline_attachment_24)
                .error(R.drawable.ic_baseline_attachment_24)
                .into(holder.RadiologyImageview)

    }

    override fun getItemCount(): Int {
        return investigationGallery.size
    }

    fun setData(investigationGallery: List<Repsonse>) {
        this.investigationGallery = investigationGallery as ArrayList<Repsonse>
        notifyDataSetChanged()

    }

}
