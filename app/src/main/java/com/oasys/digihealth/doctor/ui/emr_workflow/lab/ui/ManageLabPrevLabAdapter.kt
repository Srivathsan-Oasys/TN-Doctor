package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.labviewresponse.ResponseContentsLabView

class ManageLabPrevLabAdapter(private val context: Context) :
    RecyclerView.Adapter<ManageLabPrevLabAdapter.MyViewHolder>() {


    private var manageLabPrevLabArrayList: List<ResponseContentsLabView?>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_labprev_dialog, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return manageLabPrevLabArrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val responseData = manageLabPrevLabArrayList?.get(position)
        holder.sNoTextView.text = (position + 1).toString()
        holder.observationTxtView.text = responseData?.tm_name
        holder.resultTextView.text = responseData?.pwd_result_value
        holder.uomTextView.text = ""
        holder.referenceRangeTextView.text = ""

        holder.commentsImageView.setOnClickListener {
            /* onCommandClickListener!!.onCommandClick(position)*/
            val pwdresult = manageLabPrevLabArrayList?.get(position)?.pwd_result_value

            val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val dialog = LabCommentDialogFragment()
            val bundle = Bundle()
            bundle.putString(AppConstants.Comments, pwdresult)
            dialog.arguments = bundle
            dialog.show(ft, "Tag")


        }


        /*if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.alternateRow
                )
            )
        }*/

    }


    fun setData(responseContents: List<ResponseContentsLabView?>?) {
        manageLabPrevLabArrayList = responseContents!!
        notifyDataSetChanged()
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val sNoTextView: TextView = itemView.findViewById(R.id.sNoTextView)
        internal val observationTxtView: TextView = itemView.findViewById(R.id.observationTxtView)
        internal val resultTextView: TextView = itemView.findViewById(R.id.resultTextView)
        internal val uomTextView: TextView = itemView.findViewById(R.id.uomTextView)
        internal val referenceRangeTextView: TextView =
            itemView.findViewById(R.id.referenceRangeTextView)

        internal val commentsImageView: ImageView = itemView.findViewById(R.id.commentsImageView)

        //internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)
    }

}