package com.oasys.digihealth.doctor.ui.telemedicine.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.data.networking.api.res.ResAppointmentBookedList
import kotlinx.android.synthetic.main.holder_booked_patient.view.*

class AdapterBookedPatients :
    RecyclerView.Adapter<AdapterBookedPatients.MyPatientListHolder>() {

    var bookedAppointmentList = mutableListOf<ResAppointmentBookedList>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPatientListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_booked_patient, parent, false)
        return MyPatientListHolder(view)
    }

    override fun getItemCount(): Int = bookedAppointmentList.size

    override fun onBindViewHolder(
        holder: MyPatientListHolder,
        position: Int
    ) {
        holder.bindUI(bookedAppointmentList[position], position)
    }

    inner class MyPatientListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindUI(
            appointment: ResAppointmentBookedList,
            position: Int
        ) {
            itemView.apply {
                if (adapterPosition % 2 == 0)
                    llBookedPatients.setBackgroundColor(
                        ContextCompat.getColor(
                            this.context,
                            R.color.alternateRow
                        )
                    )
                tvBookedPatientSno.text = (adapterPosition + 1).toString()
                tvBookedPatientName.text = appointment.patient_name
                    ?: "" + "/" + appointment.patient_age + "/" + appointment.patient_gender
                tvBookedPatientPin.text = appointment.appointment_no
                tvBookedPatientAge.text = appointment.patient_age.toString() ?: ""
                tvBookedPatientGender.text = appointment.patient_gender.toString() ?: ""
                tvBookedPatientMobile.text = appointment.patient_mobile ?: ""
                tvBookedPatientDateTime.text = appointment.appointment_date ?: ""

            }
        }
    }
}