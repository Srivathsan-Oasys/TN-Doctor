package com.oasys.digihealth.doctor.ui.emr_workflow.diet.ui

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


class DietManageFavAdapter(
    private val context: Activity,
    val arrayListLabFavList: ArrayList<ResponseContentsfav?>
) :
    RecyclerView.Adapter<DietManageFavAdapter.MyViewHolder>() {

    private var onDeleteClickListener: OnDeleteClickListener? = null

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView = itemView.findViewById(R.id.tv_sno)
        internal val namecode: TextView = itemView.findViewById(R.id.tv_namecode)
        internal val qty: TextView = itemView.findViewById(R.id.tv_qty)
        internal val catagory: TextView = itemView.findViewById(R.id.tv_catagory)
        internal val frequency: TextView = itemView.findViewById(R.id.tv_frequency)
        internal val displayorder: TextView = itemView.findViewById(R.id.tv_displayorder)

        //internal val mainLinearLayout:LinearLayout  = itemView.findViewById(R.id.mainLayout)
        internal val deleteImage: ImageView = itemView.findViewById(R.id.actionTextView)

    }

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DietManageFavAdapter.MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_diet_fav_add, parent, false) as LinearLayout
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }


    override fun onBindViewHolder(holder: DietManageFavAdapter.MyViewHolder, position: Int) {
        val response = arrayListLabFavList[position]
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.namecode.text = response?.diet_master_name
        holder.qty.text = response?.diet_quantity.toString()
        holder.catagory.text = response?.diet_category_name
        holder.frequency.text = response?.diet_frequency_name
        holder.displayorder.text = response?.favourite_display_order?.toString()
        holder.deleteImage.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(
                response?.favourite_id ?: 0,
                response?.diet_master_name
            )
        }
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