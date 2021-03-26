package com.hmis_tn.doctor.ui.dashboard.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto


class MedicalConditionAdapter(context: Context, private val moviesList: List<RecyclerDto>) :
    RecyclerView.Adapter<MedicalConditionAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var checkboxTextview: TextView

        init {
            checkboxTextview = view.findViewById<View>(R.id.checkboxTextview) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.medical_condition_recycler_list, parent, false) as ConstraintLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = moviesList[position].toString()
        val movie = moviesList[position]
        holder.checkboxTextview.text = movie.title
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}