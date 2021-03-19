package com.oasys.digihealth.doctor.ui.telemedicine.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.listener.OnPatientSelected
import com.oasys.digihealth.doctor.data.networking.api.res.ResPatientListSearch
import kotlinx.android.synthetic.main.holder_patient_list.view.*

class AdapterPatientListSearch(val onPatientSelected: OnPatientSelected) :
    RecyclerView.Adapter<AdapterPatientListSearch.PatientHolder>() {
    private var selectedPatient: Int = -1

    var patientListSearch = mutableListOf<ResPatientListSearch>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_patient_list, parent, false)
        return PatientHolder(view)
    }

    override fun getItemCount(): Int = patientListSearch.size

    override fun onBindViewHolder(holder: PatientHolder, position: Int) {
        holder.bindUi(position, patientListSearch[position])
    }

    inner class PatientHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindUi(
            position: Int,
            resPatientListSearch: ResPatientListSearch
        ) {
            itemView.apply {
                resPatientListSearch.let {
                    tvPatientSearchPin.text = it.uhid ?: ""
                    tvPatientName.text = it.first_name ?: ""
                    tvPatientGender.text = if (it.gender_uuid == 1) "Male" else "Female"
                    tvPatientLastVisitDate.text = it.registered_date ?: ""
                    rbPatientSearch.isChecked =
                        resPatientListSearch.isSelected != null && resPatientListSearch.isSelected!!

                    if (selectedPatient == adapterPosition) {
                        resPatientListSearch.isSelected = true
                        rbPatientSearch.isChecked = true
                    } else {
                        resPatientListSearch.isSelected = false
                        rbPatientSearch.isChecked = false
                    }
                    rbPatientSearch.setOnClickListener {
                        selectedPatient = adapterPosition
                        onPatientSelected.invoke(resPatientListSearch)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    fun setData(patientList: MutableList<ResPatientListSearch>) {
        notifyDataSetChanged()
    }
}