package com.hmis_tn.doctor.ui.telemedicine.booking.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.invisible
import com.hmis_tn.doctor.component.extention.show
import com.hmis_tn.doctor.component.listener.OnSlotAppointment
import com.hmis_tn.doctor.data.networking.api.res.DoctorSlotList
import com.hmis_tn.doctor.view.dp
import kotlinx.android.synthetic.main.holder_slots.view.*

class AdapterSlots(val onBookAppointment: OnSlotAppointment) :
    RecyclerView.Adapter<AdapterSlots.DoctorAvailabilityViewHolder>() {
    private var slotList = ArrayList<DoctorSlotList>()
    private var selectedSlot: Int = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorAvailabilityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_slots, parent, false)
        return DoctorAvailabilityViewHolder(view)
    }

    override fun getItemCount(): Int = slotList.size
    override fun onBindViewHolder(holder: DoctorAvailabilityViewHolder, position: Int) {
        holder.bind(slotList[position])
    }

    fun setData(slotList: List<DoctorSlotList>?) {
//        this.slotList.clear()
        this.slotList = slotList as ArrayList<DoctorSlotList>
        notifyDataSetChanged()
    }

    fun unSelectSlot() {
        selectedSlot = -1
        notifyDataSetChanged()
    }

    inner class DoctorAvailabilityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(slot: DoctorSlotList) {
            itemView.apply {
                val slotTime = slot.start_time!!.substring(0, slot.start_time!!.length - 3)
                tvCalendarTime.text = slotTime
                // 1 BOOKED SLOT
                //2 AVAILABLE SLOT
                when (slot.appointment_status_uuid) {
                    1 -> {
                        ivCalendarVideo.invisible()
                        setBackColorView(R.color.booked_color, this.llCalendarMain)
                    }
                    2 -> {
                        ivCalendarVideo.invisible()
                        setBackColorView(R.color.availability_color, this.llCalendarMain)
                    }
                }
                if (slot.is_e_consult != null) {
                    if (slot.is_e_consult!!) {
                        ivCalendarVideo.show()
                    }
                }
                setOnClickListener {
                    selectedSlot = adapterPosition
                    onBookAppointment.invoke(slot)
                    notifyDataSetChanged()
                }
                if (slot.appointment_status_uuid == 2) {
                    if (selectedSlot == adapterPosition)
                        setBackColorView(R.color.selected_color, this.llCalendarMain)
                    else
                        setBackColorView(R.color.availability_color, this.llCalendarMain)
                }
            }
        }

        private fun setBackColorView(
            selectedColor: Int,
            llCalendarMain: LinearLayoutCompat
        ) {
            llCalendarMain.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    selectedColor
                )
            )
        }
    }

    class SlotsGridDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.set(
                view.dp(4),
                view.dp(4),
                view.dp(4),
                view.dp(4)
            )
        }
    }
}