package com.hmis_tn.doctor.ui.emr_workflow.diet.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav

class DietManageTemplateAdapter(
    context: Context,
    private var arrayListLabFavList: ArrayList<ResponseContentsfav?>
) : RecyclerView.Adapter<DietManageTemplateAdapter.MyViewHolder>() {

    private var mContext: Context = context
    var orderNumString: String? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textName: TextView = view.findViewById<View>(R.id.tv_namecode) as TextView
        var tvqty: TextView = view.findViewById<View>(R.id.tv_qty) as TextView
        var tvcatagory: TextView = view.findViewById<View>(R.id.tv_catagory) as TextView
        var tvfrequency: TextView = view.findViewById<View>(R.id.tv_frequency) as TextView
        var serialNumber: TextView = view.findViewById<View>(R.id.tv_sno) as TextView
        var deleteImage: ImageView = view.findViewById<View>(R.id.actionTextView) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_manage_template_diet, parent, false) as LinearLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = arrayListLabFavList[position].toString()
        val list = arrayListLabFavList[position]
        holder.serialNumber.text = (position + 1).toString()
        holder.textName.text = list!!.diet_master_name
        holder.tvqty.text = list.diet_quantity.toString()
        holder.tvcatagory.text = list.diet_category_name
        holder.tvfrequency.text = list.diet_frequency_name
        holder.deleteImage.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(list, position)
        }
    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }

    fun setFavAddItem(responseContantSave: ArrayList<ResponseContentsfav?>) {
        arrayListLabFavList = responseContantSave
        notifyDataSetChanged()
    }

    fun getItems(): ArrayList<ResponseContentsfav?> {
        return arrayListLabFavList
    }

    /*
Delete
*/
    interface OnDeleteClickListener {
        fun onDeleteClick(
            responseData: ResponseContentsfav?,
            position: Int
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun cleardata() {
        arrayListLabFavList.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        arrayListLabFavList.removeAt(position)
        notifyDataSetChanged()
    }
}
