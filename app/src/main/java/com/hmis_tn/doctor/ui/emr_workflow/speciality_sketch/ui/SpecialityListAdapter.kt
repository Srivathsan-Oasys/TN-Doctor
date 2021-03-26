package com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.model.SpecialitySketchDetail
import kotlinx.android.synthetic.main.row_speciality_img_list.view.*

class SpecialityListAdapter(
    context: Context,
    private var sessionList: ArrayList<SpecialitySketchDetail>
) :
    RecyclerView.Adapter<SpecialityListAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater
    private var isLoadingAdded = false
    private val mContext: Context

    private var onImageClickListener: OnImageClickListener? = null


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /*   var btn: Button

           init {
               btn = view.findViewById<View>(R.id.buttonstatus) as Button

           }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_speciality_img_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //orderNumString = labTestList[position].toString()


        val sessiondata = this.sessionList[position]


/*

        if (sessiondata?.status!!){

            holder.btn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.session))

            holder.btn.setTextColor(Color.WHITE)

//            holder.btn.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.session))
        }
        else{

            holder.btn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.nonsession))

            holder.btn.setTextColor(Color.BLACK)

        }


        holder.btn.setOnClickListener {

      //      onPrintClickListener?.onPrintClick(this.sessionList!![position]!!)

            setActive(position)

        }
*/

        holder.itemView.layout.setOnClickListener {

            onImageClickListener!!.onClick(sessionList[position].bytestream!!)
        }


        if (sessiondata.bytestream != null) {
            holder.itemView.imageView.setImageBitmap(sessiondata.bytestream)
        }

    }

    private fun setActive(position: Int) {

        for (i in sessionList.indices) {

            sessionList[i].status = i == position

        }

        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return sessionList.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }

    fun addAll(responseContent: List<SpecialitySketchDetail>) {

        this.sessionList.addAll(responseContent)
        notifyDataSetChanged()
    }

    interface OnImageClickListener {
        fun onClick(
            bitmap: Bitmap
        )

    }

    fun setOnImageClickListener(onClickListener: OnImageClickListener) {
        this.onImageClickListener = onClickListener
    }


    fun setData(labResultLIst: ArrayList<SpecialitySketchDetail>) {

        this.sessionList = labResultLIst

        notifyDataSetChanged()

    }


    fun setImage(byteStream: Bitmap, imgposition: Int) {

        sessionList[imgposition].bytestream = byteStream

        notifyItemChanged(imgposition)

    }

    fun clear() {

        this.sessionList.clear()

        notifyDataSetChanged()

    }


}