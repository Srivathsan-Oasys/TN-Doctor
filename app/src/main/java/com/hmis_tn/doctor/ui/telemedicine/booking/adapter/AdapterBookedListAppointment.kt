package com.hmis_tn.doctor.ui.telemedicine.booking.adapter


import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.loadCircleImage
import com.hmis_tn.doctor.component.listener.OnClickBookAgain
import com.hmis_tn.doctor.data.networking.api.res.ResMyBookingData
import kotlinx.android.synthetic.main.holder_booked_list_appointment.view.*
import java.util.*
import kotlin.math.roundToInt

class AdapterBookedListAppointment(val onClickBookAgain: OnClickBookAgain) :
    RecyclerView.Adapter<AdapterBookedListAppointment.ADListViewHolder>() {
    private var dataCancelledList = ArrayList<ResMyBookingData>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ADListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_booked_list_appointment, parent, false)
        return ADListViewHolder(view)
    }

    override fun getItemCount(): Int = dataCancelledList.size

    override fun onBindViewHolder(holder: ADListViewHolder, position: Int) {
        holder.bind(position, dataCancelledList[position])
    }

    fun setData(myBookingResCancelledList: ArrayList<ResMyBookingData>) {
        dataCancelledList = myBookingResCancelledList
        notifyDataSetChanged()
    }

    inner class ADListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            position: Int,
            resMyBookingData: ResMyBookingData
        ) {
            itemView.apply {
                resMyBookingData.also { res ->
                    ivCancelledProfile.loadCircleImage("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSbPhAkKyZtvZMx02vMMTzhxhX_skAjFOcBL098j4io5S6zqefT&usqp=CAU")
                    tvCancelledName.text = res.doc_firstname ?: ""
                    tvCancelledSpeciality.text = res.facility_name ?: ""
                    tvCancelledHosName.text = res.department_name ?: ""
                    val date = res.start_time ?: "" + " & " + res.end_time ?: ""
                    tvCancelledDateTime.text = date
                    val cancelledDateTime =
                        itemView.context.getString(R.string.last_booked) + " : " + res.appointment_date
                    tvCancelledDateTime.text = cancelledDateTime
                    btnAppointmentBookAgain.setOnClickListener {
                        onClickBookAgain.invoke(res)
                    }

                }
            }
        }
    }

    class BookedListDecoration : RecyclerView.ItemDecoration() {
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
