package com.oasys.digihealth.doctor.ui.telemedicine

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.toast
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.data.event.NetworkEvent
import com.oasys.digihealth.doctor.data.networking.api.req.ReqAppointmentSession
import com.oasys.digihealth.doctor.data.networking.api.res.ResAppointmentBookedList
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.telemedicine.booking.adapter.AdapterBookedPatients
import com.oasys.digihealth.doctor.ui.viewmodel.AppointmentViewModel
import kotlinx.android.synthetic.main.activity_telemedicine.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TelemedicineActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModelAppointment by viewModel<AppointmentViewModel>()
    private var adapterBookedPatients: AdapterBookedPatients? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var tokenBearer = ""
    private var userUUID: Int = -1

    private var pageSize = 10
    private var page: Int = 0
    private var loading: Boolean = false
    private var nextUrl: Boolean = true
    private var bookedAppointmentList: MutableList<ResAppointmentBookedList>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telemedicine)
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        tokenBearer = AppConstants.BEARER_AUTH + userDataStoreBean?.access_token
        userUUID = userDataStoreBean?.uuid!!
        initToolBar()
        initUI()
        clickListener()
        subscribeToNetworkEvents()
        subscribeToBookedPatientListAPI()
    }

    private fun initToolBar() {
        rlAppointmentToolbar.run {
            this.tbAppointment.apply {
                setNavigationOnClickListener {
                    finish()
                }
            }
        }
    }

    private fun initUI() {
        rvBookedPatient.apply {
            adapterBookedPatients = AdapterBookedPatients()
            adapter = adapterBookedPatients
            paginationListener(this)
        }
    }

    private fun clickListener() {
        cvFilterBookedPatientList.setOnClickListener(this)
        btnSearchFilter.setOnClickListener(this)
        btnClearFilter.setOnClickListener(this)
    }

    private fun paginationListener(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!loading && nextUrl) {
                        loading = true
                        subscribeToBookedPatientListAPI()
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cvFilterBookedPatientList -> navigationDrawer()
            R.id.btnSearchFilter -> {
                navigationDrawer()
                if (etFilterDate.text.toString().isNotEmpty()) {
                    bookedAppointmentList?.clear()
                    subscribeToBookedPatientListFilterAPI()
                } else
                    subscribeToBookedPatientListAPI()
            }
            R.id.btnClearFilter -> {
                etFilterDate.text.clear()
            }
        }
    }

    private fun navigationDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.openDrawer(GravityCompat.END)
        } else {
            drawerLayout.closeDrawer(GravityCompat.END)
        }
    }

    private fun subscribeToBookedPatientListAPI() {
        viewModelAppointment.getAppointmentBooked(
            tokenBearer, userUUID, ReqAppointmentSession(page, 10, "", "")
        ).observe(this) {
            if (it.statusCode == 200) {
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
                adapterBookedPatients?.bookedAppointmentList =
                    bookedAppointmentList as MutableList<ResAppointmentBookedList>
                visibleRecordViews(isRecordAvailable = true)
            } else {
                visibleRecordViews(isRecordAvailable = false)
            }
        }
    }

    private fun subscribeToBookedPatientListFilterAPI() {
        viewModelAppointment.bookAppointmentFilter(
            tokenBearer,
            userUUID,
            1,
            ReqAppointmentSession(page, 10, "", etFilterDate.text.toString())
        ).observe(this) {
            if (it.statusCode == 200) {
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
                adapterBookedPatients?.bookedAppointmentList =
                    bookedAppointmentList as MutableList<ResAppointmentBookedList>
                visibleRecordViews(isRecordAvailable = true)
            } else {
                visibleRecordViews(isRecordAvailable = false)
            }
        }
    }

    private fun visibleRecordViews(isRecordAvailable: Boolean) {
        if (isRecordAvailable) {
            nsvBookedPatientList.visibility = View.VISIBLE
            tvNoRecordBookedPatientList.visibility = View.GONE
        } else {
            nsvBookedPatientList.visibility = View.GONE
            tvNoRecordBookedPatientList.visibility = View.VISIBLE
        }
    }

    private fun subscribeToNetworkEvents() {
        viewModelAppointment.networkEvent.observe(this) {
            when (it) {
                NetworkEvent.Loading -> showLoading(true)
                NetworkEvent.Success -> showLoading(false)
                is NetworkEvent.ApiMessage -> {
                    showLoading(false)
                    it.getContentIfNotHandled().run {
                        toast(it.msg)
                    }
                }
                is NetworkEvent.Failure -> {
                    showLoading(false)
                    it.getContentIfNotHandled().run {
                        toast(it.res)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        pbBookedPatientList.isVisible = isLoading
    }
}