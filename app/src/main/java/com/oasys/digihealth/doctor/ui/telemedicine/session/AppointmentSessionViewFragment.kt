package com.oasys.digihealth.doctor.ui.telemedicine.session

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.component.extention.toast
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.data.event.NetworkEvent
import com.oasys.digihealth.doctor.data.networking.api.req.ReqViewSession
import com.oasys.digihealth.doctor.data.networking.api.res.ResAppointmentSessionList
import com.oasys.digihealth.doctor.data.networking.api.res.ResAppointmentSessionView
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.telemedicine.session.AppointmentSessionCreateDialogFragment.Companion.APPOINTMENT_SESSION_DIALOG
import com.oasys.digihealth.doctor.ui.viewmodel.AppointmentViewModel
import kotlinx.android.synthetic.main.fragment_appointment_session_view.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppointmentSessionViewFragment : Fragment() {

    private val viewModelAppointment by viewModel<AppointmentViewModel>()
    var appointmentSession: ResAppointmentSessionList? = null
    var appointmentSessionView: ResAppointmentSessionView? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var tokenBearer = ""
    private var userUUID: Int = -1
    private var facilityUUID: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            appointmentSession = it.getParcelable(APPOINTMENT_VIEW)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_session_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        tokenBearer = AppConstants.BEARER_AUTH + userDataStoreBean.access_token
        userUUID = userDataStoreBean.uuid!!
        facilityUUID = userDataStoreBean.facility_uuid!!
        initToolBar()
        initLocalData()
        clickListener()

        if (HmisApplication.isConnected()) {
            subscribeToNetworkEvents()
            subscribeToAppointmentSessionViewAPI()
        } else {
            showLoading(false)
            requireContext().toast(getString(R.string.no_connection))
        }
    }

    private fun initToolBar() {
        rlAppointmentToolbar.apply {
            this.tbAppointment.apply {
                setNavigationOnClickListener {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun clickListener() {
        btnViewAppointmentDialog.setOnClickListener {
            openSessionDialog(appointmentSessionView)
        }

        btnBackAppointmentView.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initLocalData() {
        appointmentSession.also { appointmentObj ->
            tvInstitutionValue.text = appointmentObj.facility_name ?: ""
            tvDepartmentValue.text = appointmentObj.department_name ?: ""
            tvDoctorValue.text = appointmentObj.doc_firstname ?: ""
            tvLabValue.text = appointmentObj.lab_firstname ?: ""
            tvRadiologyValue.text = appointmentObj.rad_firstname ?: ""
            tvStartDateValue.text = appointmentObj.start_date ?: ""
            tvEndDateValue.text = appointmentObj.end_date ?: ""
            tvHolidaysValue.text =
                appointmentObj.holiday_from + " to " + appointmentObj.holiday_to
        }

    }

    private fun initUI(resAppointmentSessionView: ResAppointmentSessionView?) {
        resAppointmentSessionView.also {
            appointmentSessionView = it
            it.responseContents.also { appointmentObj ->
                tvInstitutionValue.text = appointmentObj.facility_name ?: ""
                tvDepartmentValue.text = appointmentObj.department_name ?: ""
                tvDoctorValue.text = appointmentObj.doc_firstname ?: ""
                tvLabValue.text = appointmentObj.lab_firstname ?: ""
                tvRadiologyValue.text = appointmentObj.rad_firstname ?: ""
                tvStartDateValue.text = appointmentObj.start_date ?: ""
                tvEndDateValue.text = appointmentObj.end_date ?: ""
                tvHolidaysValue.text =
                    appointmentObj.holiday_from + " to " + appointmentObj.holiday_to
            }
        }

    }

    private fun openSessionDialog(appointmentSessionView: ResAppointmentSessionView?) {
        findNavController().navigate(
            R.id.action_nav_appointment_view_session_to_session_dialog,
            bundleOf(APPOINTMENT_SESSION_DIALOG to appointmentSessionView)
        )
    }

    private fun subscribeToAppointmentSessionViewAPI() {
        viewModelAppointment.viewAppointmentSession(
            tokenBearer,
            userUUID,
            ReqViewSession(appointmentSession.as_uuid)
        ) {
            initUI(it)
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
        pbAppointmentView.isVisible = isLoading
    }

    companion object {

        const val APPOINTMENT_VIEW = "APPOINTMENT_VIEW"

        @JvmStatic
        fun newInstance(appointmentSession: ResAppointmentSessionList) =
            AppointmentSessionViewFragment().apply {
                arguments = bundleOf(
                    APPOINTMENT_VIEW to appointmentSession
                )
            }

    }
}