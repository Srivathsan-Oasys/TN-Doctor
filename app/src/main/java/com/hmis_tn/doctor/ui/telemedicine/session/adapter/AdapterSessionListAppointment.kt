package com.hmis_tn.doctor.ui.telemedicine.session.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.listener.OnClickDeleteSession
import com.hmis_tn.doctor.data.networking.api.res.ResAppointmentSessionList
import kotlinx.android.synthetic.main.holder_appointment_session_list.view.*

class AdapterSessionListAppointment(
    val deleteSession: OnClickDeleteSession,
    val viewSession: OnClickDeleteSession,
    val editSession: OnClickDeleteSession
) : RecyclerView.Adapter<AdapterSessionListAppointment.MyPatientListHolder>() {

    var bookedAppointmentList = mutableListOf<ResAppointmentSessionList>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPatientListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_appointment_session_list, parent, false)
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
            appointment: ResAppointmentSessionList,
            position: Int
        ) {
            itemView.apply {
                if (adapterPosition % 2 == 0)
                    llAppointntmentPatientMain.setBackgroundColor(
                        ContextCompat.getColor(
                            this.context,
                            R.color.alternateRow
                        )
                    )
                tvAppoinSessSno.text = (adapterPosition + 1).toString()
                tvAppoinSessPatientName.text = appointment.facility_name ?: ""
                tvAppoinSessDepartment.text = appointment.department_name ?: ""
                tvAppoinSessDoctorName.text = appointment.doc_firstname ?: ""
                tvAppoinSessLabName.text = appointment.lab_firstname ?: ""
                tvAppoinSessRadName.text = appointment.rad_firstname
//                tvAppoinSessStatus.text = appointment.appointment_status_name

                ivEditAppointmentSession.setOnClickListener {
                    editSession.invoke(appointment)
                }

                ivViewAppointmentSession.setOnClickListener {
                    viewSession.invoke(appointment)
                }

                ivDeleteAppointmentSession.setOnClickListener {
                    deleteSession.invoke(appointment)
                }

            }
        }
    }
}