package com.hmis_tn.doctor.ui.telemedicine.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.toast
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.data.event.NetworkEvent
import com.hmis_tn.doctor.data.networking.api.req.ReqPatientSearch
import com.hmis_tn.doctor.data.networking.api.res.ResPatientListSearch
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.ui.telemedicine.AppointmentActivity
import com.hmis_tn.doctor.ui.telemedicine.booking.adapter.AdapterPatientListSearch
import kotlinx.android.synthetic.main.fragment_patient_list_dialog.*

class PatientListDialogFragment : DialogFragment() {
    private val viewModelAppointment by lazy {
        (activity as? AppointmentActivity)?.viewModelAppointment
            ?: throw IllegalArgumentException("Activity is not attached")
    }
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var tokenBearer = ""
    private var userUUID: Int = -1
    private var facilityUUID: Int = -1
    private var pageSize = 10
    private var page: Int = 0
    private var loading: Boolean = false
    private var nextUrl: Boolean = true

    private var name: String? = ""
    private var mobileNumber: String? = ""
    private var appointmentNumber: String? = ""

    private var adapterPatientListSearch: AdapterPatientListSearch? = null
    private var patientList: MutableList<ResPatientListSearch> = mutableListOf()
    private lateinit var onSelectPatient: OnSelectPatient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            patientList = it.getParcelableArrayList(PATIENT_LIST)!!
            name = it.getString(NAME)
            mobileNumber = it.getString(MOBILE_NUMBER)
            appointmentNumber = it.getString(APPOINTMENT_NUMBER)
        }
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.also {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (it.window != null)
                it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        tokenBearer = AppConstants.BEARER_AUTH + userDataStoreBean?.access_token
        userUUID = userDataStoreBean?.uuid!!
        facilityUUID = userDataStoreBean.facility_uuid!!
        subscribeToNetworkEvents()
        initUI()
        subscribeToSearchPatientAPI()
    }

    private fun initUI() {
        rvPatientListSearch.apply {
            adapterPatientListSearch = AdapterPatientListSearch(onPatientSelected = {
                onSelectPatient.onSelectPatientSave(it)
                viewModelAppointment.selectedPatient(it)
            })
            adapter = adapterPatientListSearch
        }

        paginationListener(rvPatientListSearch)

        btnSaveAppointment.setOnClickListener {
            dismiss()
        }

        btnCancelAppointment.setOnClickListener {
            patientList.clear()
            dismiss()
        }

        ivClosePatientList.setOnClickListener {
            btnCancelAppointment.performClick()
        }
    }

    private fun subscribeToSearchPatientAPI() {
        viewModelAppointment.patientSearchAPI(
            tokenBearer,
            userUUID,
            1,
            ReqPatientSearch(
                searchKeyWord = name,
                mobile = mobileNumber,
                pin = appointmentNumber,
                pds = "",
                pageNo = page,
                paginationSize = 10
            )
        ).observe(viewLifecycleOwner) {
            if (!it.responseContents.isNullOrEmpty()) {
                loading = false
                if (it.responseContents?.size == pageSize) {
                    page += 1
                    nextUrl = true
                } else
                    nextUrl = false
                it.responseContents?.forEach { patientObj ->
                    if (!patientList.contains(patientObj)) {
                        patientList.addAll(it.responseContents ?: emptyList())
                    }
                }
                if (!patientList.isNullOrEmpty()) {
                    adapterPatientListSearch?.patientListSearch = patientList
                }

                if (!patientList.isNullOrEmpty()) {
                    cvPatientList.visibility = View.VISIBLE
                    tvNoRecordPatientList.visibility = View.GONE
                } else {
                    tvNoRecordPatientList.visibility = View.VISIBLE
                    cvPatientList.visibility = View.GONE
                }
            }
        }
    }

    private fun paginationListener(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!loading && nextUrl) {
                        loading = true
                        subscribeToSearchPatientAPI()
                    }
                }
            }
        })
    }

    fun setSelectedPatient(onSelectPatient: OnSelectPatient) {
        this.onSelectPatient = onSelectPatient
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
        pbPatientSearchList.isVisible = isLoading

    }

    interface OnSelectPatient {
        fun onSelectPatientSave(patient: ResPatientListSearch)
    }

    companion object {
        const val PATIENT_LIST = "PATIENT_LIST"
        const val NAME = "NAME"
        const val MOBILE_NUMBER = "MOBILE_NUMBER"
        const val APPOINTMENT_NUMBER = "APPOINTMENT_NUMBER"

        @JvmStatic
        fun newInstance(
            patientList: MutableList<ResPatientListSearch>,
            name: String,
            mobileNumber: String,
            appointmentNumber: String
        ) = PatientListDialogFragment().apply {
            arguments = bundleOf(
                PATIENT_LIST to patientList,
                NAME to name,
                MOBILE_NUMBER to mobileNumber,
                APPOINTMENT_NUMBER to appointmentNumber
            )
        }
    }
}