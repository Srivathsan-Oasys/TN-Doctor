package com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav


class SpecialitySketchManageFavAdapter(
    private val context: Activity,
    val arrayListLabFavList: ArrayList<ResponseContentsfav?>
) :
    RecyclerView.Adapter<SpecialitySketchManageFavAdapter.MyViewHolder>() {

    private var onDeleteClickListener: OnDeleteClickListener? = null

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val sNoTxt: TextView = itemView.findViewById(R.id.sNoTxt)
        internal val nameTxt: TextView = itemView.findViewById(R.id.nameTxt)
        internal val displayOrderTxt: TextView = itemView.findViewById(R.id.displayOrderTxt)
        internal val actionDelete: ImageView = itemView.findViewById(R.id.actionDelete)
    }

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpecialitySketchManageFavAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.row_speciality_sketch_fav_add, parent, false) as LinearLayout
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }


    override fun onBindViewHolder(
        holder: SpecialitySketchManageFavAdapter.MyViewHolder,
        position: Int
    ) {
        val response = arrayListLabFavList[position]
        holder.sNoTxt.text = (position + 1).toString()
        holder.nameTxt.text = response?.favourite_name
        holder.displayOrderTxt.text = response?.favourite_display_order.toString()
        holder.actionDelete.setOnClickListener({
            onDeleteClickListener?.onDeleteClick(response?.favourite_id!!, response.favourite_name)
        })
    }

    fun setFavAddItem(responseContents: ResponseContentsfav?) {
        arrayListLabFavList.add(responseContents)
        notifyDataSetChanged()
    }

    /*
   Delete
    */
    interface OnDeleteClickListener {
        fun onDeleteClick(
            favouritesID: Int?,
            testMasterName: String?
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun clearadapter() {
        arrayListLabFavList.clear()
        notifyDataSetChanged()
    }

    fun adapterrefresh(deletefavouriteID: Int?) {

        for (i in arrayListLabFavList.indices) {
            if (arrayListLabFavList.get(i)?.favourite_id?.equals(deletefavouriteID!!)!!) {
                this.arrayListLabFavList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        notifyDataSetChanged()

    }

}