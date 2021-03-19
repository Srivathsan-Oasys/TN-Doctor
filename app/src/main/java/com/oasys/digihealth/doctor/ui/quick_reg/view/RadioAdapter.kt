package com.oasys.digihealth.doctor.ui.quick_reg.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.dashboard.model.ClinicalSymptomsDto

class RadioAdapter(context: Context) :
    RecyclerView.Adapter<RadioAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null
    private var moviesList: ArrayList<ClinicalSymptomsDto?>? = ArrayList()


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var checkboxTextview: TextView

        init {
            checkboxTextview = view.findViewById<View>(R.id.radio_button_text) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.row_radio_button_layout,
            parent,
            false
        ) as ConstraintLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = this.moviesList?.get(position)?.toString()
        val movie = moviesList?.get(position)
        holder.checkboxTextview.text = movie?.title

    }

    override fun getItemCount(): Int {
        return moviesList?.size!!
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}