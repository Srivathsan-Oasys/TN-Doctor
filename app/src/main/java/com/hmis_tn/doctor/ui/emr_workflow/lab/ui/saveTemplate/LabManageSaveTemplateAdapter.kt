package com.hmis_tn.doctor.ui.emr_workflow.lab.ui.saveTemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import kotlinx.android.synthetic.main.row_lab.view.serialNumberTextView
import kotlinx.android.synthetic.main.row_manage_template_lab.view.*

class LabManageSaveTemplateAdapter(
    context: Context,
    private var arrayListLabFavList: ArrayList<ResponseContentsfav?>
) : RecyclerView.Adapter<LabManageSaveTemplateAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private var mContext: Context
    var orderNumString: String? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /*   var textName: TextView
   *//*
        var textcode: TextView
*//*
        var serialNumber: TextView
        var displayOrder: TextView
        var code: TextView

        var deleteImage : ImageView
        val mainLinearLayout: ConstraintLayout


        init {
            serialNumber = view.findViewById<View>(R.id.serialNumberTextView) as TextView
            textName = view.findViewById<View>(R.id.textName) as TextView
            code = view.findViewById<View>(R.id.textCode) as TextView
            displayOrder = view.findViewById<View>(R.id.textdisplayOrder) as TextView
            deleteImage = view.findViewById<View>(R.id.deleteImageView) as ImageView
            mainLinearLayout = itemView.findViewById(R.id.mainLinearLayout)


        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_manage_template_lab, parent, false) as ConstraintLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = arrayListLabFavList[position].toString()
        val list = arrayListLabFavList[position]
        holder.itemView.serialNumberTextView.text = (position + 1).toString()
        holder.itemView.textNames.text = list!!.test_master_code
        holder.itemView.codesTextView.text = list.test_master_name
        holder.itemView.im_delete.setOnClickListener({

            onDeleteClickListener?.onDeleteClick(list, position)


        })


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

    fun delete(position: Int) {

        arrayListLabFavList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount - position)

    }

    fun setAddItem(responseContantSave: ResponseContentsfav) {


        val check =
            arrayListLabFavList.any { it!!.test_master_id == responseContantSave.test_master_id }

        if (!check) {
            arrayListLabFavList.add(responseContantSave)
            notifyDataSetChanged()

        } else {

            Toast.makeText(this.mContext, "Already Item available in the list", Toast.LENGTH_LONG)
                ?.show()

        }

    }


    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}


