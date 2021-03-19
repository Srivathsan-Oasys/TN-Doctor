package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.PreDiagnosisResponseContent
import com.oasys.digihealth.doctor.utils.Utils


class PrevDiagnosisAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<PrevDiagnosisAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var prevDiagnosis_Result: List<PreDiagnosisResponseContent>? = ArrayList()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sNoTextView: TextView = view.findViewById<View>(R.id.sNoTextView) as TextView
        var nameTextView: TextView = view.findViewById<View>(R.id.nameTextview) as TextView
        var codeTextView: TextView = view.findViewById<View>(R.id.codeTextView) as TextView
        var typeTextView: TextView = view.findViewById<View>(R.id.typeTextView) as TextView
        var dateTextView: TextView = view.findViewById<View>(R.id.dateTextView) as TextView
        var mainLinearLayout: LinearLayout =
            view.findViewById<View>(R.id.mainLinearLayout) as LinearLayout
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.prev_diagnosis_recycler_list,
            viewGroup,
            false
        ) as ConstraintLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prevList = prevDiagnosis_Result?.get(position)
        val pos = position + 1
        holder.sNoTextView.text = pos.toString()
        holder.nameTextView.text = prevList?.diagnosis?.name
        holder.codeTextView.text = prevList?.diagnosis?.code
        val correctformat =
            Utils.parseDate(prevList?.created_date!!, "yyyy-MM-dd HH:mm", "dd-MMM-yyyy hh:mm a")
        holder.dateTextView.text = correctformat
        holder.typeTextView.text = "ICD10"

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
        return prevDiagnosis_Result?.size!!
    }

    fun refreshList(preLabArrayList: List<PreDiagnosisResponseContent>?) {
        this.prevDiagnosis_Result = preLabArrayList!!
        this.notifyDataSetChanged()
    }
}
