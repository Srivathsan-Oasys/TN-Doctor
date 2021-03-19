package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav

class RadiologyManageTemplateAdapter(
    context: Context,
    private var arrayListLabFavList: ArrayList<ResponseContentsfav?>
) : RecyclerView.Adapter<RadiologyManageTemplateAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private var mContext: Context
    var orderNumString: String? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textName: TextView
        var serialNumber: TextView
        var deleteImage: ImageView
        var code: TextView
        var layout: ConstraintLayout

        init {
            serialNumber = view.findViewById<View>(R.id.serialNumberTextView) as TextView
            textName = view.findViewById<View>(R.id.textNames) as TextView
            code = view.findViewById<View>(R.id.codesTextView) as TextView
            deleteImage = view.findViewById<View>(R.id.deleteImageView) as ImageView
            layout = view.findViewById<View>(R.id.mainLinearLayout) as ConstraintLayout


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_manage_template_lab, parent, false) as ConstraintLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = arrayListLabFavList[position].toString()
        val list = arrayListLabFavList[position]
        holder.serialNumber.text = (position + 1).toString()
        holder.textName.text = list!!.test_master_name
        holder.code.text = list.test_master_code
        holder.deleteImage.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(list, position)
        }
        if (position % 2 == 0) {
            holder.layout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.alternateRow
                )
            )
        } else {
            holder.layout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
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


    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}


/*
package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.LabManageTemplateAdapter

class RadiologyManageTemplateAdapter(context: Context, private var arrayListLabFavList: ArrayList<ResponseContentsfav?>) : RecyclerView.Adapter<RadiologyManageTemplateAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private var mContext: Context
    var orderNumString: String? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null


    private  var onItemClickListener:OnItemClickListener?=null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textName: TextView
        var serialNumber: TextView
        var deleteImage : ImageView
        var code: TextView
        var layout:ConstraintLayout

        init {
            serialNumber = view.findViewById<View>(R.id.serialNumberTextView) as TextView
            textName = view.findViewById<View>(R.id.textNames) as TextView
            code = view.findViewById<View>(R.id.codesTextView) as TextView
            deleteImage = view.findViewById<View>(R.id.deleteImageView) as ImageView
            layout = view.findViewById<View>(R.id.mainLinearLayout) as ConstraintLayout


        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(R.layout.row_manage_templates_radiology, parent, false) as ConstraintLayout
        return MyViewHolder(itemLayout)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = arrayListLabFavList[position].toString()
        val list = arrayListLabFavList[position]
        holder.serialNumber.text = (position + 1).toString()
        holder.textName.text = list!!.test_master_name
        holder.code.text = list.test_master_code
        holder.deleteImage.setOnClickListener({

            onDeleteClickListener?.onDeleteClick(list,position)


        })

        if (position % 2 == 0) {
            holder.layout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.alternateRow
                )
            )
        } else {
            holder.layout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        }


*/
/*
        holder.layout.setOnClickListener {

            onItemClickListener!!.onItemClick(arrayListLabFavList[position],position)


        }
*//*

    }


    interface OnItemClickListener {
        fun onItemClick(
                responseContent: ResponseContentsfav?,
                position: Int
        )
    }

    fun setOnItemClickListener(onItemClickListener1: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener1
    }




    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }

    fun setAddItem(responseContantSave: ResponseContentsfav) {


        val check= arrayListLabFavList.any{ it!!.test_master_id == responseContantSave?.test_master_id}

        if (!check) {
            arrayListLabFavList.add(responseContantSave)
            notifyDataSetChanged()

        }
        else{

            Toast.makeText(this!!.mContext,"Already Item available in the list", Toast.LENGTH_LONG)?.show()

        }

    }

    fun setFavAddItem(responseContantSave: ArrayList<ResponseContentsfav?>) {
        arrayListLabFavList = responseContantSave
        notifyDataSetChanged()
    }


    fun getItems(): ArrayList<ResponseContentsfav?> {

        return arrayListLabFavList

    }

    fun cleardata() {
        arrayListLabFavList.clear()
        notifyDataSetChanged()
    }


    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
    interface OnDeleteClickListener {
        fun onDeleteClick(
            responseData: ResponseContentsfav?,
            position: Int
        )
    }
    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }
    fun removeItem(position: Int) {
        arrayListLabFavList.removeAt(position)
        notifyDataSetChanged()
    }
    fun delete(position: Int) {

        arrayListLabFavList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, getItemCount() - position);

    }




}


*/
