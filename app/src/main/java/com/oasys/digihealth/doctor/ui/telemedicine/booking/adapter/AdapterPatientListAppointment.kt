package com.oasys.digihealth.doctor.ui.telemedicine.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.listener.OnClickCheckInBookAppointment
import com.oasys.digihealth.doctor.component.listener.OnClickDeleteBookAppointment
import com.oasys.digihealth.doctor.component.listener.OnClickEditBookAppointment
import com.oasys.digihealth.doctor.data.networking.api.res.ResAppointmentBookedList
import kotlinx.android.synthetic.main.holder_appointment_patient_list.view.*

class AdapterPatientListAppointment(
    val onClickDeleteBookAppointment: OnClickDeleteBookAppointment,
    val onClickEditBookAppointment: OnClickEditBookAppointment,
    val onClickCheckInBookAppointment: OnClickCheckInBookAppointment
) : RecyclerView.Adapter<AdapterPatientListAppointment.MyPatientListHolder>() {

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
            .inflate(R.layout.holder_appointment_patient_list, parent, false)
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
                if (position % 2 == 0)
                    llAppointntmentPatientMain.setBackgroundColor(
                        ContextCompat.getColor(
                            this.context,
                            R.color.alternateRow
                        )
                    )
                tvAppoinSessSno.text = (adapterPosition + 1).toString()
                tvAppoinSessPatientName.text = appointment.patient_name
                    ?: "" + "/" + appointment.patient_age + "/" + appointment.patient_gender
                tvAppoinSessNo.text = appointment.appointment_no
                tvAppoinSessDepartment.text = appointment.department_name ?: ""
                tvAppoinSessDoctorName.text = appointment.doc_firstname ?: ""
                tvAppoinSessLabName.text = appointment.lab_firstname ?: ""
                tvAppoinSessRadName.text = appointment.rad_firstname
                tvAppoinSessDate.text = appointment.appointment_date
                tvAppoinSessStatus.text = appointment.appointment_status_name

                if (appointment.appointment_status_code == "3" && appointment.appointment_status_name == "Cancelled") {
                    ivCancelPatientAppointment.isEnabled = false
                    ivDownloadPatientAppointment.isEnabled = false
                    ivUpdatePatientAppointment.isEnabled = true

                }
                when (appointment.appointment_status_code) {
                    "3" -> {
                        //Cancelled
                        tvAppoinSessStatus.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red
                            )
                        )
                        tvAppoinSessStatus.background = ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.cancelled_status_border
                        )
                    }
                    "1" -> {
                        // Booked
                        tvAppoinSessStatus.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.orderColorBlue
                            )
                        )
                        tvAppoinSessStatus.background = ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.booked_status_border
                        )
                    }
                    "7" -> {
                        // Check IN
                        tvAppoinSessStatus.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.purple
                            )
                        )
                        tvAppoinSessStatus.background = ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.check_in_status_border
                        )
                    }
                }

                ivCancelPatientAppointment.setOnClickListener {
                    onClickDeleteBookAppointment.invoke(appointment)
                }

                ivUpdatePatientAppointment.setOnClickListener {
                    onClickEditBookAppointment.invoke(appointment)
                }

                ivDownloadPatientAppointment.setOnClickListener {
                    onClickCheckInBookAppointment.invoke(appointment)
                }

            }
        }
    }
}