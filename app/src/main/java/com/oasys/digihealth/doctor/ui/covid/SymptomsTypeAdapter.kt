package com.oasys.digihealth.doctor.ui.covid

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.dashboard.model.ResponseSpicemanTypeContent


class SymptomsTypeAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<SymptomsTypeAdapter.MyViewHolder>() {
    private var specimalTypeModel: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.symptoms_recycler_list, parent, false)
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

        holder.duration.isEnabled=response!!.isSelect

        holder.duration.setText(specimalTypeModel!![position]!!.othercommands)


        holder.checkBox.setOnClickListener {
            val myCheckBox = it as CheckBox

            if (myCheckBox.isChecked) {
                specimalTypeModel!![position]!!.isSelect = true
                holder.duration.isEnabled=true
                notifyDataSetChanged()

            } else if (!myCheckBox.isChecked) {
                specimalTypeModel!![position]!!.isSelect = false
                holder.duration.isEnabled=false
                notifyDataSetChanged()
            }
        }

        holder.duration.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable) {

                specimalTypeModel!![position]!!.othercommands=s.toString()
            }
        })

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
        internal val duration : EditText = itemView.findViewById(R.id.durationEditText)
    }}
