package com.hmis_tn.doctor.ui.telemedicine.booking.adapter


import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.loadCircleImage
import com.hmis_tn.doctor.component.listener.OnBookAppointmentListener
import com.hmis_tn.doctor.data.networking.api.res.DoctorList
import kotlinx.android.synthetic.main.holder_appointment_doctor_list.view.*
import kotlin.math.roundToInt

class AppointmentDoctorListAdapter(val onBookAppointmentListener: OnBookAppointmentListener) :
    RecyclerView.Adapter<AppointmentDoctorListAdapter.ADViewHolder>() {

    var doctorsList = mutableListOf<DoctorList>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ADViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_appointment_doctor_list, parent, false)
        return ADViewHolder(view)
    }

    override fun getItemCount(): Int = doctorsList.size

    override fun onBindViewHolder(holder: ADViewHolder, position: Int) {
        holder.bind(doctorsList[position], position)
    }

    inner class ADViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(doctor: DoctorList, position: Int) {
            itemView.apply {
                ivAppointmentDoctor.loadCircleImage(doctor.as_image_url, R.drawable.ic_placeholder)
                tvAppointmentDoctorName.text =
                    doctor.doc_firstname + " (" + doctor.department_name + ")"
                val startTime = doctor.start_time ?: ""
                val endTime = doctor.end_time ?: ""
                val appointmentStartDate = doctor.start_date ?: ""
                val appointmentEndDate = doctor.end_date ?: ""
                tvAppointmentDoctorDate.text =
                    "$appointmentStartDate to $appointmentEndDate\n$startTime to $endTime"
                btnAppointmentBook.setOnClickListener {
                    onBookAppointmentListener.invoke(doctor)
                }
            }
        }
    }

    class DoctorListDecoration : RecyclerView.ItemDecoration() {
        private fun View.dp(value: Int) =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value.toFloat(),
                this.resources.displayMetrics
            ).roundToInt()

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            with(parent) {
                outRect.set(
                    dp(16),
                    dp(8),
                    dp(16),
                    dp(8)
                )
            }
        }
    }
}
