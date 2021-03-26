package com.hmis_tn.doctor.ui.covid

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.dashboard.model.ResponseSpicemanTypeContent


class CustomTypeAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<CustomTypeAdapter.MyViewHolder>() {
    private var specimalTypeModel: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_recycler_list, parent, false)
        var checkboxTextview: TextView
        var checkBox : CheckBox
        return MyViewHolder(view)
    }
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response = specimalTypeModel!![position]
        holder.checkboxTextview.setText(response?.name)
        holder.checkBox.isChecked = response!!.isSelect
        /*
        checkbox on click
         */

        holder.checkBox.isChecked = response!!.isSelect

        holder.checkBox.setOnClickListener {
            val myCheckBox = it as CheckBox

            if (myCheckBox.isChecked) {
                specimalTypeModel!![position]!!.isSelect = true

                notifyDataSetChanged()

            } else if (!myCheckBox.isChecked) {
                specimalTypeModel!![position]!!.isSelect = false

                notifyDataSetChanged()
            }
        }



    }
    override fun getItemCount(): Int {
        return specimalTypeModel!!.size
    }
    fun setdata(responseContents: ArrayList<ResponseSpicemanTypeContent?>?) {
        specimalTypeModel = responseContents
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<ResponseSpicemanTypeContent?>? {
        return specimalTypeModel
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val checkboxTextview: TextView = itemView.findViewById(R.id.checkboxTextview)
        internal val checkBox : CheckBox = itemView.findViewById(R.id.dateCheckbox)

    }}
