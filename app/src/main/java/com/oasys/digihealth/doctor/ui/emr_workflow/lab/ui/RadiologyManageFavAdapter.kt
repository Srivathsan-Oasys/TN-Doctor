package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.isTablet
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.ManageRadiologyFavAddResponseContents
import kotlinx.android.synthetic.main.row_lab_fav_add.view.*

class RadiologyManageFavAdapter(
    private val context: Activity,
    val arrayListLabFavList: ArrayList<ManageRadiologyFavAddResponseContents?>
) :
    RecyclerView.Adapter<RadiologyManageFavAdapter.MyViewHolder>() {

    private var onDeleteClickListener: OnDeleteClickListener? = null

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.sNoTextView)

        //internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        internal val testNameTextView: TextView =
            itemView.findViewById(R.id.textName)
        internal val displayORder: TextView = itemView.findViewById(R.id.displayOrderTextView)
        internal val deleteImage: ImageView = itemView.findViewById(R.id.actiondelete)
    }

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RadiologyManageFavAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_radiology_fav_add, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }


    override fun onBindViewHolder(holder: RadiologyManageFavAdapter.MyViewHolder, position: Int) {
        val response = arrayListLabFavList[position]
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.testNameTextView.text = response?.test_master_name
        holder.displayORder.text = response?.favourite_display_order?.toString()
        holder.deleteImage.setOnClickListener({
            onDeleteClickListener?.onDeleteClick(
                response?.favourite_id!!,
                response.test_master_name
            )
        })
        if (isTablet(context)) {
            if (position % 2 == 0) {
                holder.itemView.mainLinearLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.alternateRow
                    )
                )
            } else {
                holder.itemView.mainLinearLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            }
        }


    }

    fun setFavAddItem(responseContents: ManageRadiologyFavAddResponseContents?) {
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