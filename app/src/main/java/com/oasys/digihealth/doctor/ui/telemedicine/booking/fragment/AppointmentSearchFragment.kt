package com.oasys.digihealth.doctor.ui.telemedicine.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.component.extention.toast
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.data.event.NetworkEvent
import com.oasys.digihealth.doctor.data.networking.api.req.ReqDoctorListBody
import com.oasys.digihealth.doctor.data.networking.api.res.DoctorList
import com.oasys.digihealth.doctor.data.networking.api.res.ResPatientListSearch
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.telemedicine.AppointmentActivity
import com.oasys.digihealth.doctor.ui.telemedicine.booking.adapter.AppointmentDoctorListAdapter
import com.oasys.digihealth.doctor.ui.telemedicine.booking.fragment.AppointmentMainSearchFragment.Companion.SELECTED_PATIENT
import com.oasys.digihealth.doctor.ui.telemedicine.booking.fragment.AppointmentSlotBookFragment.Companion.DOCTOR
import com.oasys.digihealth.doctor.ui.telemedicine.booking.fragment.AppointmentSlotBookFragment.Companion.PATIENT
import com.oasys.digihealth.doctor.ui.viewmodel.AppointmentViewModel
import kotlinx.android.synthetic.main.fragment_appointment_search.*
import kotlinx.android.synthetic.main.layout_appointment_doctor_detail.view.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppointmentSearchFragment : Fragment(), View.OnClickListener {
    private val viewModelAppointment by viewModel<AppointmentViewModel>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var tokenBearer = ""
    private var userUUID: Int = -1

    private var pageSize = 10
    private var page: Int = 0
    private var loading: Boolean = false
    private var nextUrl: Boolean = true

    private var selectedPatient: ResPatientListSearch? = null

    private var appointmentDoctorListAdapter: AppointmentDoctorListAdapter? = null

    private var doctorsList = ArrayList<DoctorList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            selectedPatient = getParcelable(SELECTED_PATIENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        tokenBearer = AppConstants.BEARER_AUTH + userDataStoreBean.access_token
        userUUID = userDataStoreBean.uuid!!
        initToolBar()
        clickListener()
        resetAllField()
        initUI()

        if (HmisApplication.isConnected()) {
            subscribeToNetworkEvents()
        } else {
            showLoading(false)
            requireContext().toast(getString(R.string.no_connection))
        }
    }

    private fun initToolBar() {
        rlAppointmentToolbar.run {
            this.tbAppointment.apply {
                setNavigationOnClickListener {
                    (activity as AppointmentActivity).onBackPressed()
                }
            }
        }
    }

    private fun clickListener() {
        btnSearchAppointment.setOnClickListener(this)
        btnClearAppointment.setOnClickListener(this)
        etDoctorName.setOnClickListener(this)
        etDepartmentName.setOnClickListener(this)
        etRadiologyName.setOnClickListener(this)
        etLabName.setOnClickListener(this)
    }

    private fun initUI() {
        layoutDoctorInfo.apply {
            var genderPatient = ""
            if (selectedPatient.gender_uuid == 1) {
                genderPatient = "Male"
                ivDoctorAvatar.setImageResource(R.drawable.ic_man_placeholder)
            } else {
                genderPatient = "Female"
                ivDoctorAvatar.setImageResource(R.drawable.ic_female_palceholder)
            }
            val patientInfo =
                selectedPatient.first_name + " / " + selectedPatient.age + " " + "Year(s)" + " / " +
                        genderPatient + "\n" + selectedPatient.uhid + " / " + selectedPatient.patient_detail.mobile

            this.tvDoctorName.text = patientInfo
            this.tvDoctorMobilePin.visibility = View.GONE

        }
        rvBookedAppointments.apply {
            appointmentDoctorListAdapter = AppointmentDoctorListAdapter {
                findNavController().navigate(
                    R.id.action_nav_appointment_search_to_appointment_booking,
                    bundleOf(
                        DOCTOR to it,
                        PATIENT to selectedPatient
                    )
                )
            }
            adapter = appointmentDoctorListAdapter
            if (this.itemDecorationCount == 0) this.addItemDecoration(
                AppointmentDoctorListAdapter.DoctorListDecoration()
            )
            paginationListener(this)
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSearchAppointment -> {
                if (isValid()) {
                    hideSoftKeyboard(etDoctorName)
                    doctorsList.clear()
                    subscribeToSearchAvailability()
                    /*findNavController().navigate(R.id.action_nav_appointment_search_to_appointment_booking)*/
                } else {
                    requireContext().toast("Choose any one field to process next !")
                }
            }
            R.id.btnClearAppointment -> resetAllField()
            R.id.etDoctorName -> Unit
            R.id.etDepartmentName -> Unit
            R.id.etRadiologyName -> Unit
            R.id.etLabName -> Unit
        }
    }

    private fun resetAllField() {
        etDoctorName.setText("")
        etDepartmentName.setText("")
        etRadiologyName.setText("")
        etLabName.setText("")
        etDoctorName.clearFocus()
        etDepartmentName.clearFocus()
        etRadiologyName.clearFocus()
        etLabName.clearFocus()
    }

    private fun hideSoftKeyboard(view: View?) {
        if (view != null) {
            val inputManager = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun isValid(): Boolean {
        var isValidField = false
        if (!etDoctorName.text.isNullOrEmpty())
            isValidField = true
        else if (!etDepartmentName.text.isNullOrEmpty())
            isValidField = true
        else if (!etRadiologyName.text.isNullOrEmpty())
            isValidField = true
        else if (!etLabName.text.isNullOrEmpty())
            isValidField = true
        return isValidField
    }

    private fun subscribeToSearchAvailability() {
        subscribeToDoctorListAPI()
    }

    private fun paginationListener(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!loading && nextUrl) {
                        loading = true
                        subscribeToDoctorListAPI()
                    }
                }
            }
        })
    }

    private fun subscribeToDoctorListAPI() {
        viewModelAppointment.getDoctorList(
            tokenBearer,
            userUUID,
            ReqDoctorListBody(
                search = etDoctorName.text.toString().trim(),
                paginationSize = 20,
                doc_uuid = "",
                department_uuid = "",
                lab_uuid = "",
                rad_uuid = ""
            )
        ).observe(viewLifecycleOwner) {
            if (!it.doctorList.isNullOrEmpty()) {
                loading = false
                if (it.doctorList.size == pageSize) {
                    page += 1
                    nextUrl = true
                } else
                    nextUrl = false
                it.doctorList.forEach { doctorObj ->
                    if (!doctorsList.contains(doctorObj)) {
                        doctorsList.add(doctorObj)
                    }
                }
                appointmentDoctorListAdapter?.doctorsList = doctorsList
            }

            llDoctorAvailability.visibility = View.VISIBLE
            if (!doctorsList.isNullOrEmpty()) {
                tvNoRecordDoctorList.visibility = View.GONE
                rvBookedAppointments.visibility = View.VISIBLE
            } else {
                tvNoRecordDoctorList.visibility = View.VISIBLE
                rvBookedAppointments.visibility = View.GONE
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
        pbAppointmentSearch.isVisible = isLoading
    }

    override fun onResume() {
        super.onResume()
        if (isValid())
            subscribeToSearchAvailability()
    }

    companion object {
        @JvmStatic
        fun newInstance(patient: ResPatientListSearch) =
            AppointmentSearchFragment().apply {
                arguments = bundleOf(
                    SELECTED_PATIENT to patient
                )
            }
    }
}
