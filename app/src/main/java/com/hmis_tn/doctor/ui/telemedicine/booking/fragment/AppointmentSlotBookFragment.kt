package com.hmis_tn.doctor.ui.telemedicine.booking.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.makkalnalam.data.networking.api.req.ReqUpdateBooking
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication.Companion.paymentSuccess
import com.hmis_tn.doctor.application.HmisApplication.Companion.selectedDoctorByPatient
import com.hmis_tn.doctor.application.HmisApplication.Companion.selectedSlotByPatient
import com.hmis_tn.doctor.component.extention.toast
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.data.event.NetworkEvent
import com.hmis_tn.doctor.data.networking.api.req.ReqAvailability
import com.hmis_tn.doctor.data.networking.api.req.ReqNewBooking
import com.hmis_tn.doctor.data.networking.api.res.DoctorList
import com.hmis_tn.doctor.data.networking.api.res.DoctorSlotList
import com.hmis_tn.doctor.data.networking.api.res.ResMyBookingData
import com.hmis_tn.doctor.data.networking.api.res.ResPatientListSearch
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.ui.telemedicine.AppointmentActivity
import com.hmis_tn.doctor.ui.telemedicine.booking.adapter.AdapterSlots
import com.hmis_tn.doctor.ui.telemedicine.booking.fragment.AppointmentListMainFragment.Companion.IS_FROM_BOOKED_APPOINTMENT
import com.hmis_tn.doctor.ui.viewmodel.AppointmentViewModel
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.fragment_appointment_reschedule.*
import kotlinx.android.synthetic.main.holder_appointment_doctor_list.view.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AppointmentSlotBookFragment : Fragment() {
    private val viewModelAppointment by viewModel<AppointmentViewModel>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var doctor: DoctorList? = null
    private var selectedPatient: ResPatientListSearch? = null

    private var myBookingData: ResMyBookingData? = null
    private var isReschedule: Boolean? = false
    private var previousAppointmentId: Int? = null

    //    private var adapterDoctorAvailability: DoctorAvailabilityAdapter? = null
    private var adapterDoctorAvailability: AdapterSlots? = null
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var selectedSlot: DoctorSlotList? = null
    private var listSlotAvailable = ArrayList<DoctorSlotList>()
    private var reqUpdateBooking: ReqUpdateBooking? = null
    private var reqUpdateSlotNew: ReqUpdateBooking? = null
    private var reqNewBooking: ReqNewBooking? = null
    private var selectedDate = ""
    private var tokenBearer = ""
    private var userUUID: Int = -1
    private var doctorId: Int? = 0
    private var patientId: Int? = 0
    private var eConsultCharges: Int? = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils(requireContext()).setCalendarLocale("en", requireContext())
        arguments?.apply {
            if (this.getParcelable<DoctorList>(DOCTOR) != null) {
                doctor = getParcelable(DOCTOR)!!
                doctorId = doctor?.doc_uuid
                selectedDoctorByPatient = doctor
            }
            if (this.getParcelable<ResPatientListSearch>(PATIENT) != null) {
                selectedPatient = getParcelable(PATIENT)!!
                patientId = selectedPatient?.uuid
            }
            if (this.getParcelable<ResMyBookingData>(BOOKING_DATA) != null) {
                myBookingData = getParcelable(BOOKING_DATA)
                isReschedule = getBoolean(BOOKING_RESCHEDULE)
                doctorId = myBookingData?.doctor_uuid
//                previousAppointmentId = getInt(RESCHEDULE_PREVIOUS_ID)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_reschedule, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        tokenBearer = AppConstants.BEARER_AUTH + userDataStoreBean?.access_token
        userUUID = userDataStoreBean?.uuid!!

        selectedDate = dateFormatter.format(Date().time)

        initToolBar()
        layout_doctor_info.apply {
            this.ivAppointmentDoctor.setImageResource(R.drawable.ic_man_placeholder)
            this.btnAppointmentBook.visibility = View.GONE
            if (myBookingData != null) {
                myBookingData?.also { doctor ->
                    this.tvAppointmentDoctorName.text = doctor.doc_firstname ?: ""
                    this.tvAppointmentDoctorDate.text = doctor.as_created_date ?: ""
                    btnSaveAppointment.text = "Update"
                }
            } else {
                if (doctor != null) {
                    doctor?.also {
                        selectedDoctorByPatient = it
                        val startTime = it.start_time ?: ""
                        val endTime = it.end_time ?: ""
                        val appointmentStartDate = it.start_date ?: ""
                        val appointmentEndDate = it.end_date ?: ""
                        this.tvAppointmentDoctorName.text = it.doc_firstname ?: ""
                        this.tvAppointmentDoctorDate.text =
                            "$appointmentStartDate to $appointmentEndDate\n$startTime to $endTime"
                        btnSaveAppointment.text = "Book"
                    }
                }
            }
        }

        initDoctorAvailability()
        clickListener()
        subscribeToNetworkEvents()
        subscribeToGetAvailability(selectedDate)
    }

    private fun initToolBar() {
        rlAppointmentResult.run {
            this.tbAppointment.apply {
                setNavigationOnClickListener {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun initDoctorAvailability() {
        rvDoctorAvailability.apply {
            adapterDoctorAvailability = AdapterSlots(onBookAppointment = {
                selectedSlot = it
                selectedSlotByPatient = selectedSlot
            })
            adapter = adapterDoctorAvailability
            if (itemDecorationCount == 0) this.addItemDecoration(AdapterSlots.SlotsGridDecoration())
        }
    }

    private fun clickListener() {
        btnSaveAppointment.setOnClickListener {
//            findNavController().navigate(R.id.action_payment_start)
            if (tvNoSlot.visibility == View.VISIBLE) {
                requireContext().toast(getString(R.string.select_any_slot_alert_message))
            } else {
                subscribeToBookAppointment()
            }
        }
        btnCancelAppointment.setOnClickListener {
            findNavController().navigateUp()
        }
        dayCalendarLayout.addOnDayOfWeekClickListener { date ->
            selectedDate = dateFormatter.format(date.time)
            adapterDoctorAvailability?.unSelectSlot()
            subscribeToGetAvailability(selectedDate)
        }
    }

    private fun subscribeToGetAvailability(selectedDate: String) {
        viewModelAppointment.getAvailableSlotAPI(
            tokenBearer,
            userUUID,
            ReqAvailability(
                doctor_id = doctorId.toString(),
                appointment_session_uuid = "",
                appointment_date = selectedDate
            )
        ).observe(viewLifecycleOwner) {
            listSlotAvailable.clear()
            if (!it.responseContents.isNullOrEmpty()) {
                it.responseContents.forEach { slotData ->
                    if (!listSlotAvailable.contains(slotData))
                        listSlotAvailable.add(slotData)
                }
            }
            /* if (!listSlotAvailable.isNullOrEmpty())
                 eConsultCharges =
                     if (!listSlotAvailable.isNullOrEmpty() && listSlotAvailable[0].appointment_session != null)
                         listSlotAvailable[0].appointment_session?.econsult_charges
                     else
                         250

             tvEConsultCharges.text =
                 requireContext().getString(R.string.consultation_charges) + eConsultCharges + " ₹"*/
            adapterDoctorAvailability?.setData(listSlotAvailable)

            if (!listSlotAvailable.isNullOrEmpty()) {
                rvDoctorAvailability.visibility = View.VISIBLE
                tvNoSlot.visibility = View.GONE
                llCalendarAvailability.visibility = View.VISIBLE
            } else {
                rvDoctorAvailability.visibility = View.GONE
                tvNoSlot.visibility = View.VISIBLE
                llCalendarAvailability.visibility = View.GONE
            }
        }
    }

    private fun subscribeToBookAppointment() {
        if (tvNoSlot.visibility == View.VISIBLE) {
            requireContext().toast(getString(R.string.select_any_slot_alert_message))
        } else {
            if (selectedSlotByPatient != null) {
                if (selectedDoctorByPatient != null) {
                    selectedSlot = selectedSlotByPatient
                    doctor = selectedDoctorByPatient
                    doctorId = doctor?.doc_uuid
                    reqNewBooking = ReqNewBooking(
                        doctor_uuid = doctorId,
                        appointment_slots_uuid = selectedSlot?.uuid,
                        appointment_date = selectedDate,
                        start_time = selectedSlot?.start_time ?: "",
                        end_time = selectedSlot?.end_time ?: "",
                        patient_uuid = patientId ?: -1,
                        facility_uuid = doctor?.facility_uuid,
                        department_uuid = doctor?.department_uuid,
                        appointment_session_uuid = 1,
                        encounter_date = "",
                        encounter_uuid = 0,
                        comments = "",
                        rating = 0,
                        is_reschedule = false
                    )
                    reqUpdateBooking = ReqUpdateBooking(
                        id = selectedSlot?.uuid,
                        appointment_date = selectedDate,
                        start_time = selectedSlot?.start_time ?: "",
                        doctor_uuid = doctorId
                    )

                    // Book Appointment API
                    viewModelAppointment.bookAppointment(
                        tokenBearer,
                        userUUID,
                        reqNewBooking
                    ).observe(viewLifecycleOwner) {
                        if (it.statusCode == 200 || it.statusCode == 201) {
                            // Update Appointment Slot API
                            viewModelAppointment.updateSlot(
                                tokenBearer,
                                userUUID,
                                reqUpdateBooking
                            ) {
                                //SUCCESS CASE
                                selectedDoctorByPatient = null
                                selectedSlotByPatient = null
                                paymentSuccess = false
                                requireContext().toast("Successfully booked appointment!")
                                findNavController().navigate(
                                    R.id.action_nav_booking_to_appointment_patient_list,
                                    bundleOf(IS_FROM_BOOKED_APPOINTMENT to true)
                                )
                            }
                        }
                    }

                } else {
                    reqNewBooking = ReqNewBooking(
                        doctor_uuid = myBookingData?.doctor_uuid,
                        appointment_slots_uuid = selectedSlot?.uuid,
                        appointment_date = selectedDate,
                        start_time = selectedSlot?.start_time ?: "",
                        end_time = selectedSlot?.end_time ?: "",
                        patient_uuid = patientId ?: -1,
                        facility_uuid = myBookingData?.facility_uuid,
                        department_uuid = myBookingData?.department_uuid,
                        appointment_session_uuid = 1,
                        encounter_date = "",
                        encounter_uuid = 0,
                        comments = "",
                        rating = 0,
                        is_reschedule = true,
                        previous_appointment_uuid = previousAppointmentId
                    )

                    reqUpdateSlotNew = ReqUpdateBooking(
                        id = selectedSlot?.uuid,
                        appointment_date = selectedDate,
                        start_time = selectedSlot?.start_time ?: "",
                        doctor_uuid = doctorId
                    )

                    // Book Appointment API
                    viewModelAppointment.bookAppointment(
                        tokenBearer,
                        userUUID,
                        reqNewBooking
                    ).observe(viewLifecycleOwner) {
                        if (it.statusCode == 200 || it.statusCode == 201) {
                            // Update Appointment Slot API
                            viewModelAppointment.updateSlot(
                                tokenBearer,
                                userUUID,
                                reqUpdateSlotNew
                            ) {
                                //SUCCESS CASE
                                paymentSuccess = false
                                (activity as AppointmentActivity).onBackPressed()
                            }
                        }
                    }

                }
            } else {
                requireContext().toast(getString(R.string.select_any_slot_alert_message))
            }
        }
    }

    private fun subscribeToNetworkEvents() {
        viewModelAppointment.networkEvent.observe(viewLifecycleOwner) {
            when (it) {
                NetworkEvent.Loading -> showLoading(true)
                NetworkEvent.Success -> showLoading(false)
                is NetworkEvent.ApiMessage -> {
                    showLoading(false)
                    it.getContentIfNotHandled().run {
                        requireContext().toast(it.msg)
                    }
                }
                is NetworkEvent.Failure -> {
                    showLoading(false)
                    it.getContentIfNotHandled().run {
                        requireContext().toast(it.res)
                    }

                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        pbDoctorDetail.isVisible = isLoading
    }


    companion object {
        const val DOCTOR = "DOCTOR"
        const val PATIENT = "PATIENT"
        const val BOOKING_DATA = "BOOKING_DATA"
        const val BOOKING_RESCHEDULE = "BOOKING_RESCHEDULE"

        fun newInstance(
            doctor: DoctorList,
            selectedPatient: ResPatientListSearch,
            resMyBookingData: ResMyBookingData,
            isReschedule: Boolean
        ) =
            AppointmentSlotBookFragment().apply {
                this.arguments = bundleOf(
                    DOCTOR to doctor,
                    PATIENT to selectedPatient,
                    BOOKING_DATA to resMyBookingData,
                    BOOKING_RESCHEDULE to isReschedule
                )
            }
    }
}
