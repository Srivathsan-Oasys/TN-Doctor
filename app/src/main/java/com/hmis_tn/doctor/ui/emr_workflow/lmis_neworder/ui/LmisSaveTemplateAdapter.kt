package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto

class LmisSaveTemplateAdapter(context: Context, private val moviesList: List<RecyclerDto>) :
    RecyclerView.Adapter<LmisSaveTemplateAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNumber: TextView
        var TestName: TextView
        var codeText: TextView
        var mainLinearLayout: LinearLayout


        init {
            serialNumber = view.findViewById<View>(R.id.serialNumber) as TextView
            codeText = view.findViewById<View>(R.id.codeText) as TextView
            TestName = view.findViewById<View>(R.id.TestName) as TextView
            mainLinearLayout = view.findViewById<View>(R.id.mainLinearLayout) as LinearLayout

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.lmis_save_template_recycler_list, parent, false) as ConstraintLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = moviesList[position].toString()
        val movie = moviesList[position]
        holder.serialNumber.text = movie.title
        holder.codeText.text = movie.title
        holder.TestName.text = movie.genre
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
        return moviesList.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}