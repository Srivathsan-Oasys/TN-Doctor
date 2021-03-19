package com.oasys.digihealth.doctor.ui.telemedicine.booking.adapter


import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.loadCircleImage
import com.oasys.digihealth.doctor.component.listener.OnClickPatient
import com.oasys.digihealth.doctor.data.networking.api.res.ResPatientListSearch
import kotlinx.android.synthetic.main.layout_appointment_doctor_detail.view.*
import kotlin.math.roundToInt

class AdapterPatientSearchListAppointment(val onClickPatient: OnClickPatient) :
    RecyclerView.Adapter<AdapterPatientSearchListAppointment.ADListViewHolder>() {

    var patientList = mutableListOf<ResPatientListSearch>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ADListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_appointment_doctor_detail, parent, false)
        return ADListViewHolder(view)
    }

    override fun getItemCount(): Int = patientList.size

    override fun onBindViewHolder(holder: ADListViewHolder, position: Int) {
        holder.bind(patientList[position])
    }

    inner class ADListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            patient: ResPatientListSearch
        ) {
            itemView.apply {
                patient.also { patientInfo ->
                    ivDoctorAvatar.loadCircleImage("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSbPhAkKyZtvZMx02vMMTzhxhX_skAjFOcBL098j4io5S6zqefT&usqp=CAU")
                    tvDoctorName.text = patientInfo.first_name ?: ""
                    setOnClickListener {
                        onClickPatient.invoke(patientInfo)
                    }

                }
            }
        }
    }

    class PatientSearchListDecoration : RecyclerView.ItemDecoration() {
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
