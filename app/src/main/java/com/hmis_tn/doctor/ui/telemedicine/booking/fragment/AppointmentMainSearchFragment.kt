package com.hmis_tn.doctor.ui.telemedicine.booking.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.toast
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.data.networking.api.res.ResAppointmentBookedList
import com.hmis_tn.doctor.data.networking.api.res.ResPatientListSearch
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.ui.telemedicine.AppointmentActivity
import com.hmis_tn.doctor.ui.telemedicine.booking.adapter.AdapterPatientSearchListAppointment
import com.hmis_tn.doctor.ui.telemedicine.fragment.PatientListDialogFragment
import kotlinx.android.synthetic.main.fragment_appointment_main_search.*
import kotlinx.android.synthetic.main.layout_appointment_doctor_detail.view.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class AppointmentMainSearchFragment : Fragment(), View.OnClickListener,
    PatientListDialogFragment.OnSelectPatient {


    private val viewModelAppointment by lazy {
        (activity as? AppointmentActivity)?.viewModelAppointment
            ?: throw IllegalArgumentException("Activity is not attached")
    }
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var tokenBearer = ""
    private var userUUID: Int = -1
    private var facilityUUID: Int = -1
    private var patientList: MutableList<ResPatientListSearch>? = mutableListOf()
    private var selectedPatient: ResPatientListSearch? = null

    private var adapterPatientSearchListAppointment: AdapterPatientSearchListAppointment? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_main_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        tokenBearer = AppConstants.BEARER_AUTH + userDataStoreBean?.access_token
        userUUID = userDataStoreBean?.uuid!!
        facilityUUID = userDataStoreBean.facility_uuid!!
        initToolBar()
        clickListener()
        resetListener()
        initUI()
    }

    private fun initToolBar() {
        rlAppointmentToolbar.run {
            this.tbAppointment.apply {
                setNavigationOnClickListener {
                    (activity as AppointmentActivity).onBackPressed()
                }
            }
            //            this.ivAppointmentProfile.loadCircleImage("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSbPhAkKyZtvZMx02vMMTzhxhX_skAjFOcBL098j4io5S6zqefT&usqp=CAU")
        }
    }

    private fun clickListener() {
        btnSearchAppointment.setOnClickListener(this)
        btnClearAppointment.setOnClickListener(this)
        etAppointmentName.setOnClickListener(this)
        etMobileNumber.setOnClickListener(this)
        etAppointmentNumber.setOnClickListener(this)
    }

    private fun initUI() {
        rvSearchAppointments.apply {
            adapterPatientSearchListAppointment = AdapterPatientSearchListAppointment {

            }
            adapter = adapterPatientSearchListAppointment
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSearchAppointment -> {
                if (isValid()) {
                    val patientListDialogFragment = PatientListDialogFragment.newInstance(
                        patientList!!,
                        etAppointmentName.text.toString().trim(),
                        etMobileNumber.text.toString().trim(),
                        etAppointmentNumber.text.toString().trim()
                    )
                    patientListDialogFragment.setSelectedPatient(this)
                    patientListDialogFragment.show(childFragmentManager, "")
                } else {
                    requireContext().toast("Choose any one field to process")
                }
            }
            R.id.btnClearAppointment -> resetListener()
            R.id.etAppointmentName -> Unit
            R.id.etMobileNumber -> Unit
            R.id.etAppointmentNumber -> Unit
        }
    }

    private fun resetListener() {
        etAppointmentName.setText("")
        etMobileNumber.setText("")
        etAppointmentNumber.setText("")
        etAppointmentName.clearFocus()
        etMobileNumber.clearFocus()
        etAppointmentNumber.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        if (viewModelAppointment.getSelectedPatient() != null) {
            resetListener()
            onSelectPatientSave(viewModelAppointment.getSelectedPatient()!!)
        }
    }

    private fun isValid(): Boolean {
        var isValidField = false
        if (!etAppointmentName.text.isNullOrEmpty())
            isValidField = true
        else if (!etMobileNumber.text.isNullOrEmpty())
            isValidField = true
        else if (!etAppointmentNumber.text.isNullOrEmpty())
            isValidField = true
        return isValidField
    }

    override fun onSelectPatientSave(patient: ResPatientListSearch) {
        resetListener()
        selectedPatient = patient

        layoutDoctorInfo.apply {
            visibility = View.VISIBLE
            var genderPatient = ""
            if (selectedPatient?.gender_uuid == 1) {
                genderPatient = "Male"
                ivDoctorAvatar.setImageResource(R.drawable.ic_man_placeholder)
            } else {
                genderPatient = "Female"
                ivDoctorAvatar.setImageResource(R.drawable.ic_female_palceholder)
            }

            val patientInfo =
                selectedPatient?.first_name + " / " + selectedPatient?.age + " " + "Year(s)" + " / " +
                        genderPatient + "\n" + selectedPatient?.uhid + " / " + selectedPatient?.patient_detail?.mobile

            this.tvDoctorName.text = patientInfo
            this.tvDoctorMobilePin.visibility = View.GONE

            setOnClickListener {
                findNavController().navigate(
                    R.id.action_appointment_main_search_to_nav_appointment_search,
                    bundleOf(
                        SELECTED_PATIENT to selectedPatient
                    )
                )
            }
        }
    }

    companion object {
        const val SELECTED_PATIENT = "SELECTED_PATIENT"
        const val APPOINTMENT_BOOKED = "APPOINTMENT_BOOKED"

        fun newInstance(resAppointmentBookedList: ResAppointmentBookedList) =
            AppointmentMainSearchFragment().apply {
                this.arguments = bundleOf(
                    APPOINTMENT_BOOKED to resAppointmentBookedList
                )
            }
    }
}
