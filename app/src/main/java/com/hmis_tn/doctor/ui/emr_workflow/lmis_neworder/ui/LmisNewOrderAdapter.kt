package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.ResponseContentslmisorder


class LmisNewOrderAdapter(context: Context) :
    RecyclerView.Adapter<LmisNewOrderAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null
    private var isLoadingAdded = false
    private var onItemClickListener: OnItemClickListener? = null
    private var responseContent: ArrayList<ResponseContentslmisorder?>? = ArrayList()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNumberTextView: TextView
        var pin: TextView
        var patientName: TextView
        var ageText: TextView
        var genderText: TextView
        var mobile: TextView
        var mainLinearLayout: ConstraintLayout

        init {
            serialNumberTextView = view.findViewById<View>(R.id.serialNumberTextView) as TextView
            patientName = view.findViewById<View>(R.id.patientName) as TextView
            pin = view.findViewById<View>(R.id.pin) as TextView
            ageText = view.findViewById<View>(R.id.ageText) as TextView
            genderText = view.findViewById<View>(R.id.genderText) as TextView
            mobile = view.findViewById<View>(R.id.mobile) as TextView
            mainLinearLayout = view.findViewById<View>(R.id.mainLinearLayout) as ConstraintLayout


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.lmis_recycler_list, parent, false) as ConstraintLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseContent = responseContent!![position]

        holder.serialNumberTextView.text = (position + 1).toString()
        holder.patientName.text = responseContent!!.first_name
        holder.pin.text = responseContent.uhid
        holder.mobile.text = responseContent.patient_detail?.mobile
        if (responseContent.gender_uuid == 1) {
            holder.genderText.text = "Male"
        } else if (responseContent.gender_uuid == 2) {
            holder.genderText.text = "Female"
        } else if (responseContent.gender_uuid == 3) {
            holder.genderText.text = "Transgender"
        } else {
            holder.genderText.text = "Male"
        }

        holder.ageText.text = responseContent.age.toString() + " Y"
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(responseContent, position)
        }
        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return responseContent!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }

    fun addAll(responseContent: List<ResponseContentslmisorder?>?) {
        this.responseContent?.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(responseContent: ResponseContentslmisorder?, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun clearAll() {
        this.responseContent?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(ResponseContentslmisorder())
    }

    fun add(r: ResponseContentslmisorder) {
        responseContent!!.add(r)
        notifyItemInserted(responseContent!!.size - 1)
    }

    fun getItem(position: Int): ResponseContentslmisorder? {
        return responseContent!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        /*val position = responseContent!!.size - 1
        val result = getItem(position)
        if (result != null) {
            responseContent!!.removeAt(position)
            notifyItemRemoved(position)
        }*/
    }

}