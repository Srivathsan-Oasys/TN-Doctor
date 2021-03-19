package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav


class ManageTemplatenstituteAdapter(
    context: Context,
    private var arrayListVitualFavList: ArrayList<ResponseContentsfav?>
) : RecyclerView.Adapter<ManageTemplatenstituteAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private var mContext: Context
    var orderNumString: String? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textName: TextView
        var codeText: TextView
        var serialNumber: TextView
        var deleteImage: ImageView
        var mainLinearLayout: ConstraintLayout


        init {
            serialNumber = view.findViewById<View>(R.id.serialNumberTextView) as TextView
            textName = view.findViewById<View>(R.id.textName) as TextView
            codeText = view.findViewById<View>(R.id.codeText) as TextView
            deleteImage = view.findViewById<View>(R.id.deleteImageView) as ImageView
            mainLinearLayout = view.findViewById<View>(R.id.mainLinearLayout) as ConstraintLayout


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_manage_template_institute, parent, false) as ConstraintLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = arrayListVitualFavList[position].toString()
        val list = arrayListVitualFavList[position]
        holder.serialNumber.text = (position + 1).toString()
        if (list!!.test_master_name !== null) {
            holder.textName.text = list!!.test_master_name.toString()
        }

        holder.codeText.text = list!!.test_master_code.toString()
        holder.deleteImage.setOnClickListener({

            onDeleteClickListener?.onDeleteClick(list, position)


        })


    }

    fun setFavAddItem(responseContantSave: ArrayList<ResponseContentsfav?>) {
        arrayListVitualFavList = responseContantSave
        notifyDataSetChanged()
    }


    fun setTemplateItem(responseData: ResponseContentsfav) {

        val check =
            arrayListVitualFavList.any { it!!.test_master_id == responseData.test_master_id }

        if (!check) {
            arrayListVitualFavList.add(responseData)
            notifyDataSetChanged()

        } else {

            Toast.makeText(this.mContext, "Already Item available in the list", Toast.LENGTH_LONG)
                ?.show()

        }

    }


/*
    fun setFavAddItem(responseContents: ArrayList<ResponseContentsfav?>) {
        arrayListVitualFavList.add(responseContents)
        notifyDataSetChanged()
    }
*/

    override fun getItemCount(): Int {
        return arrayListVitualFavList.size
    }


    fun getItems(): ArrayList<ResponseContentsfav?> {

        return arrayListVitualFavList

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
        arrayListVitualFavList.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        arrayListVitualFavList.removeAt(position)
        notifyItemRemoved(position)

    }

/*
    fun adapterrefresh(deletefavouriteID: Int?) {

        for (i in arrayListVitualFavList.indices)
        {
            if(arrayListVitualFavList.get(i)?.favourite_id?.equals(deletefavouriteID!!)!!)
            {
                this.arrayListVitualFavList.removeAt(i)
                notifyItemRemoved(i);
                break
            }

        }
        notifyDataSetChanged()

    }
*/

    fun adapterrefresh(deletefavouriteID: Int?) {

        for (i in arrayListVitualFavList.indices) {
            if (arrayListVitualFavList.get(i)?.favourite_id?.equals(deletefavouriteID!!)!!) {
                this.arrayListVitualFavList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        notifyDataSetChanged()

    }

    fun addRow(data: ResponseContentsfav) {

        this.arrayListVitualFavList.add(data)
        notifyDataSetChanged()

    }

    fun clearadapter() {
        this.arrayListVitualFavList.clear()
        notifyDataSetChanged()
    }


    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}


