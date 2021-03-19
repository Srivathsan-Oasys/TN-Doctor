package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model.ResponseContentXXX

class BloodRequestAdapter(private val list: ArrayList<ResponseContentXXX>) :
    RecyclerView.Adapter<BloodRequestAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var chkBloodComponent: CheckBox = view.findViewById(R.id.chkBloodComponent)
        var etBloodComponent: EditText = view.findViewById(R.id.etBloodComponent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_blood_request, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bloodComponentModel = list[position]
        holder.chkBloodComponent.isChecked = bloodComponentModel.isChecked
        holder.etBloodComponent.isEnabled = bloodComponentModel.isChecked
        holder.etBloodComponent.hint = bloodComponentModel.name

        if (!bloodComponentModel.text.isNullOrEmpty()) {
            holder.etBloodComponent.setText(bloodComponentModel.text)
        } else {
            holder.etBloodComponent.setText("")
        }

        holder.chkBloodComponent.setOnCheckedChangeListener { buttonView, isChecked ->
            bloodComponentModel.isChecked = isChecked
            holder.etBloodComponent.isEnabled = isChecked
            if (!isChecked) {
                holder.etBloodComponent.text?.clear()
            }
        }

        holder.etBloodComponent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                bloodComponentModel.text = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }
}