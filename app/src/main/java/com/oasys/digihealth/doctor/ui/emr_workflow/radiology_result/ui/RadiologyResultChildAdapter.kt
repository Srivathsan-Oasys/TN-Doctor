package com.oasys.digihealth.doctor.ui.emr_workflow.radiology_result.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology_result.model.RadilogyResultResponseContent
import com.squareup.picasso.Picasso


class RadiologyResultChildAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<RadiologyResultChildAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private var radiologyResultList: ArrayList<RadilogyResultResponseContent> = ArrayList()


    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)

    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var RadiologyImageview: AppCompatImageView


        init {

            RadiologyImageview = view.findViewById<View>(R.id.imageView) as AppCompatImageView

        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.radiology_result_child_layout,
            viewGroup,
            false
        ) as CardView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prevList = radiologyResultList.get(position)
/* Picasso.get()
.load("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464")
.into(holder.RadiologyImageview)*/
        if (prevList.work_order_attachment_uuid != null) {
            Picasso.get()
                .load(prevList.work_order_attachment_uuid)
                .placeholder(R.drawable.ic_radiology_result)
                .error(R.drawable.ic_radiology_result)
                .into(holder.RadiologyImageview)
        } else
            holder.RadiologyImageview.setImageResource(R.drawable.ic_radiology_result)

    }

    override fun getItemCount(): Int {
        return radiologyResultList.size
    }

    fun setData(labResultLIst: ArrayList<RadilogyResultResponseContent>) {

        this.radiologyResultList = labResultLIst

        notifyDataSetChanged()

    }

}