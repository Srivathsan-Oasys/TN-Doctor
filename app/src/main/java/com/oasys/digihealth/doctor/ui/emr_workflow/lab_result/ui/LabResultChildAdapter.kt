package com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.Repsonse


class LabResultChildAdapter(
    private val mContext: Context,
    private var pod_Result: List<Repsonse>

) : RecyclerView.Adapter<LabResultChildAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private var onCommandClickListener: OnCommandClickListener? = null


    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)

    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sNoTextView: TextView
        var resultTextView: TextView
        var observationTextView: TextView
        var uomTextView: TextView
        var referenceRange: TextView
        var commentsImageView: ImageView
        var mainLinearLayout: LinearLayout


        init {

            sNoTextView = view.findViewById<View>(R.id.sNoTextView) as TextView
            resultTextView = view.findViewById<View>(R.id.resultTextView) as TextView
            observationTextView = view.findViewById<View>(R.id.observationTextView) as TextView
            uomTextView = view.findViewById<View>(R.id.uomTextView) as TextView
            referenceRange = view.findViewById<View>(R.id.referenceRange) as TextView
            commentsImageView = view.findViewById<View>(R.id.deleteImageView) as ImageView
            mainLinearLayout = view.findViewById<View>(R.id.mainLinearLayout) as LinearLayout


        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.lab_result_child_layout,
            viewGroup,
            false
        ) as LinearLayoutCompat
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prevList = pod_Result.get(position)
        var pos = position + 1
        holder.sNoTextView.text = pos.toString()
        holder.resultTextView.text = prevList.result_value as CharSequence?
        holder.observationTextView.text = prevList.test_or_analyte
        holder.referenceRange.text =
            prevList.test_or_analyte_ref_min.toString() + "-" + prevList.test_or_analyte_ref_max.toString()
        if (prevList.analyte_uom != null) {
            holder.uomTextView.text = prevList.analyte_uom.toString()

        }
        holder.commentsImageView.setOnClickListener {


            /* onCommandClickListener!!.onCommandClick(position)*/

            val ft = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
            val dialog = LabResultCommentDialogFragment()
            val bundle = Bundle()
            bundle.putString("RESULT", (prevList.result_value as CharSequence?).toString())
            dialog.arguments = bundle
            dialog.show(ft, "Tag")

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
        return pod_Result.size
    }

    interface OnCommandClickListener {
        fun onCommandClick(
            position: Int
        )
    }


    fun setOnCommandClickListener(onCommandClickListener: OnCommandClickListener) {
        this.onCommandClickListener = onCommandClickListener
    }


}
