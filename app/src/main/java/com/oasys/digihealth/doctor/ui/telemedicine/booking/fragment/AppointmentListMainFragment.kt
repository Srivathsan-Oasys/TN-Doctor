package com.oasys.digihealth.doctor.ui.telemedicine.booking.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.toast
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.data.event.NetworkEvent
import com.oasys.digihealth.doctor.data.networking.api.req.ReqAppointmentCancel
import com.oasys.digihealth.doctor.data.networking.api.req.ReqAppointmentCheckIn
import com.oasys.digihealth.doctor.data.networking.api.req.ReqAppointmentSession
import com.oasys.digihealth.doctor.data.networking.api.res.ResAppointmentBookedList
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.telemedicine.AppointmentActivity
import com.oasys.digihealth.doctor.ui.telemedicine.booking.adapter.AdapterPatientListAppointment
import com.oasys.digihealth.doctor.ui.telemedicine.booking.fragment.AppointmentMainSearchFragment.Companion.APPOINTMENT_BOOKED
import com.oasys.digihealth.doctor.ui.viewmodel.AppointmentViewModel
import kotlinx.android.synthetic.main.fragment_appointment_list_main.*
import kotlinx.android.synthetic.main.fragment_appointment_list_main.btnAddBookAppointment
import kotlinx.android.synthetic.main.fragment_appointment_list_main.btnDownloadAppointment
import kotlinx.android.synthetic.main.fragment_appointment_list_main.drawerLayout
import kotlinx.android.synthetic.main.fragment_appointment_list_main.pbAppointmentList
import kotlinx.android.synthetic.main.fragment_appointment_list_main.rlAppointmentToolbar
import kotlinx.android.synthetic.main.fragment_appointment_list_main.rvAppointmentPatientList
import kotlinx.android.synthetic.main.fragment_appointment_session_list.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AppointmentListMainFragment : Fragment(), View.OnClickListener {

    private var adapterPatientListAppointment: AdapterPatientListAppointment? = null

    private val viewModelAppointment by viewModel<AppointmentViewModel>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var tokenBearer = ""
    private var userUUID: Int = -1

    private var pageSize = 10
    private var page: Int = 0
    private var loading: Boolean = false
    private var nextUrl: Boolean = true
    private var bookedAppointmentList: MutableList<ResAppointmentBookedList>? = mutableListOf()

    private var sdf = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())

    private var isFromBookedAppointment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            isFromBookedAppointment = getBoolean(IS_FROM_BOOKED_APPOINTMENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_list_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        tokenBearer = AppConstants.BEARER_AUTH + userDataStoreBean.access_token
        userUUID = userDataStoreBean.uuid!!
        initToolBar()
        clickListener()
        initUI()
        subscribeToNetworkEvents()
        subscribeToSessionListAPI()
    }

    private fun initToolBar() {
        rlAppointmentToolbar.run {
            this.tbAppointment.apply {
                title = getString(R.string.book_appointment_new)
                setNavigationOnClickListener {
                    (activity as AppointmentActivity).onBackPressed()
                }
            }
    //            this.ivAppointmentProfile.loadCircleImage("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSbPhAkKyZtvZMx02vMMTzhxhX_skAjFOcBL098j4io5S6zqefT&usqp=CAU")
        }
    }

    private fun clickListener() {
        btnBookedAppointment.setOnClickListener(this)
        btnAddBookAppointment.setOnClickListener(this)
        btnDownloadAppointment.setOnClickListener(this)
        cvFilterAppointments.setOnClickListener(this)
        flAppointmentList.setOnClickListener(this)
    }

    private fun initUI() {
        rvAppointmentPatientList.apply {
            adapterPatientListAppointment =
                AdapterPatientListAppointment(
                    onClickDeleteBookAppointment = {
                        alertDialogDeleteBookAppointment(it)
                    },
                    onClickEditBookAppointment = {
                        findNavController().navigate(
                            R.id.action_patient_list_to_nav_appointment_main_search,
                            bundleOf(APPOINTMENT_BOOKED to it)
                        )
                    },
                    onClickCheckInBookAppointment = {
                        subscribeToCheckInAppointment(it)
                    }
                )
            adapter = adapterPatientListAppointment

            paginationListener(this)
        }
    }

    private fun paginationListener(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!loading && nextUrl) {
                        loading = true
                        subscribeToSessionListAPI()
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBookedAppointment -> Unit
            R.id.btnAddBookAppointment -> navigateToBookAppointmentNew()
            R.id.btnDownloadAppointment -> Unit
            R.id.cvFilterAppointments -> navigationDrawer()
            R.id.flAppointmentList -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.END))
                    drawerLayout.closeDrawer(
                        GravityCompat.END
                    )

            }
        }
    }

    private fun navigationDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.openDrawer(GravityCompat.END)
        else
            drawerLayout.closeDrawer(GravityCompat.END)
    }

    private fun navigateToBookAppointmentNew() {
        findNavController().navigate(R.id.action_patient_list_to_nav_appointment_main_search)
    }

    override fun onResume() {
        super.onResume()
        if (isFromBookedAppointment) {
            bookedAppointmentList?.clear()
            page = 0
            subscribeToSessionListAPI()
        }
    }

    private fun subscribeToSessionListAPI() {
        viewModelAppointment.getAppointmentBooked(
            tokenBearer, userUUID, ReqAppointmentSession(page, 10, "", "")
        ).observe(viewLifecycleOwner) {
            if (it.statusCode == 200) {
                tvAppointmentListCount.text = it.totalRecords.toString() ?: "0"
                if (!it.responseContents.isNullOrEmpty()) {
                    loading = false
                    if (it.responseContents.size == pageSize) {
                        page += 1
                        nextUrl = true
                    } else
                        nextUrl = false
                    it.responseContents.forEach { patientObj ->
                        if (!bookedAppointmentList?.contains(patientObj)!!) {
                            bookedAppointmentList?.addAll(it.responseContents ?: emptyList())
                        }
                    }
                }
            }

            if (!bookedAppointmentList.isNullOrEmpty()) {
                adapterPatientListAppointment?.bookedAppointmentList =
                    bookedAppointmentList as MutableList<ResAppointmentBookedList>
                visibleRecordViews(isRecordAvailable = true)
            } else {
                visibleRecordViews(isRecordAvailable = false)
            }
        }
    }

    private fun subscribeToCheckInAppointment(appointment: ResAppointmentBookedList) {
        viewModelAppointment.checkInAppointment(
            tokenBearer,
            userUUID,
            ReqAppointmentCheckIn(appointment.as_uuid)
        ) {
            if (it != null) {
                if (it.statusCode == 200 || it.statusCode == 201) {
                    requireContext().toast(it.msg ?: "Success")
                    bookedAppointmentList?.clear()
                    page = 0
                    subscribeToSessionListAPI()
                } else {
                    requireContext().toast(it.msg ?: "Something went wrong try again !")
                }
            } else {
                requireContext().toast(it.msg ?: "Something went wrong try again !")
            }
        }
    }

    private fun deleteAppointmentBookedListAPI(
        appointment: ResAppointmentBookedList,
        comment: String
    ) {
        viewModelAppointment.cancelAppointmentList(
            tokenBearer, userUUID,
            ReqAppointmentCancel(
                previous_appointment_uuid = appointment.previous_appointment_uuid,
                appointment_uuid = appointment.as_uuid,
                is_reschedule = if (appointment.is_reschedule!!) 1 else 0,
                appointment_slots_uuid = appointment.appointment_slots_uuid,
                appointment_status_uuid = appointment.appointment_status_uuid,
                comments = comment,
                cancelled_date = sdf.format(Date().time)
            )
        ) {
            // Refresh the view
            if (it != null) {
                if (it.statusCode == 200 || it.statusCode == 201)
                    subscribeToSessionListAPI()
                else
                    requireContext().toast("Something Went wrong try again !")
            }
        }
    }

    private fun visibleRecordViews(isRecordAvailable: Boolean) {
        if (isRecordAvailable) {
            nsvAppointmentList.visibility = View.VISIBLE
            rvAppointmentPatientList.visibility = View.VISIBLE
            tvNoRecordAppointmentList.visibility = View.GONE
        } else {
            nsvAppointmentList.visibility = View.GONE
            rvAppointmentPatientList.visibility = View.GONE
            tvNoRecordAppointmentList.visibility = View.VISIBLE
        }
    }

    private fun alertDialogDeleteBookAppointment(appointment: ResAppointmentBookedList) {
        var reason = ""
        val builder = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage("Are you sure you want to delete ${appointment.patient_name} ?")


        builder.setPositiveButton("OK") { d, _ ->
            d?.dismiss()
            deleteAppointmentBookedListAPI(appointment = appointment, comment = reason)
        }
        builder.setNegativeButton("Cancel") { d, _ -> d?.dismiss() }

        val alert: AlertDialog = builder.create()
        val reasonEditText = EditText(requireContext())
        val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lp.setMargins(56, 0, 56, 0)
        reasonEditText.layoutParams = lp
        reasonEditText.hint = "Comment..."
        alert.setView(reasonEditText)
        reason = reasonEditText.text.toString().trim()

        alert.setOnShowListener {
            val btnPositive: Button = alert.getButton(Dialog.BUTTON_POSITIVE)
            btnPositive.textSize = 16F
            btnPositive.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_semibold)
            val btnNegative: Button = alert.getButton(Dialog.BUTTON_NEGATIVE)
            btnNegative.textSize = 16F
            btnNegative.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_semibold)
        }
        alert.show()
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
        pbAppointmentList.isVisible = isLoading
    }

    companion object {
        const val IS_FROM_BOOKED_APPOINTMENT = "IS_FROM_BOOKED_APPOINTMENT"
        fun newInstance(isFromBookedAppointment: Boolean) = AppointmentListMainFragment().apply {
            this.arguments = bundleOf(
                IS_FROM_BOOKED_APPOINTMENT to isFromBookedAppointment
            )
        }
    }
}
