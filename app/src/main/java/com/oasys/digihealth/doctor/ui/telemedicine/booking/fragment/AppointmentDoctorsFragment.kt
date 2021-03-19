package com.oasys.digihealth.doctor.ui.telemedicine.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.oasys.digihealth.doctor.data.networking.api.res.Doctors
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.telemedicine.AppointmentActivity
import com.oasys.digihealth.doctor.ui.telemedicine.booking.adapter.AppointmentDoctorListAdapter
import com.oasys.digihealth.doctor.ui.telemedicine.booking.fragment.AppointmentSlotBookFragment
import com.oasys.digihealth.doctor.ui.viewmodel.AppointmentViewModel
import kotlinx.android.synthetic.main.fragment_appointment_doctors.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AppointmentDoctorsFragment : Fragment() {

    private val viewModelAppointment by viewModel<AppointmentViewModel>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var doctors: Doctors? = null

    private var mView: View? = null

    private var adapterAppointmentDoctorList: AppointmentDoctorListAdapter? = null
    private var pageSize = 10
    private var page: Int = 0
    private var loading: Boolean = false
    private var nextUrl: Boolean = true
    private var isRefresh: Boolean = true
    private var doctorsList: MutableList<DoctorList>? = mutableListOf()

    private var selectedDoctorUUID: String? = null
    private var selectedDepartmentUUID: String? = null
    private var selectedRadiologyUUID: String? = null
    private var selectedLabUUID: String? = null
    private var currentTime = ""
    private var currentDate = ""
    private var tokenBearer = ""
    private var userUUID: Int = -1


    private val timeFormatter = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            doctors = getParcelable(DOCTOR)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (mView == null) {
            val v = inflater.inflate(R.layout.fragment_appointment_doctors, container, false)
            mView = v
            mView
        } else {
            mView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        tokenBearer = AppConstants.BEARER_AUTH + userDataStoreBean.access_token
        userUUID = userDataStoreBean.uuid!!

        currentDate = dateFormatter.format(Date().time)
        currentTime = timeFormatter.format(Date().time)
        showLoading(true)
        initToolBar()
        initAppointmentDoctorList()
        if (HmisApplication.isConnected()) {
            subscribeToNetworkEvents()
            subscribeToDoctorAllListAPI()
        } else {
            showLoading(false)
            requireContext().toast(getString(R.string.no_connection))
        }
        swipeToRefreshView()
    }

    private fun initToolBar() {
        rlAppointmentToolbar.run {
            this.tbAppointment.apply {
                if (activity is AppointmentActivity) {
                    (activity as AppointmentActivity).apply {
                        tbAppointment.title = getString(R.string.book_appointment)
                    }
                }
                setNavigationOnClickListener {
                    (activity as AppointmentActivity).onBackPressed()
                }
            }
    //            this.ivAppointmentProfile.loadCircleImage("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSbPhAkKyZtvZMx02vMMTzhxhX_skAjFOcBL098j4io5S6zqefT&usqp=CAU")
        }
    }

    private fun initAppointmentDoctorList() {
        rvDoctorList.apply {
            adapterAppointmentDoctorList = AppointmentDoctorListAdapter(
                onBookAppointmentListener = { doctor ->
                    findNavController().navigate(
                        R.id.action_nav_appointment_doctors_to_appointment_booking,
                        bundleOf(AppointmentSlotBookFragment.DOCTOR to doctor)
                    )
                }
            )
            adapter = adapterAppointmentDoctorList
            if (this.itemDecorationCount == 0) this.addItemDecoration(
                AppointmentDoctorListAdapter.DoctorListDecoration()
            )

            //Pagination
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
                        subscribeToDoctorAllListAPI()
                    }
                }
            }
        })
    }

    private fun subscribeToDoctorAllListAPI() {
        if (isRefresh)
            page = 0
        viewModelAppointment.getDoctorList(
            tokenBearer, userUUID,
            ReqDoctorListBody(
                "", "", page, 10,
                if (!selectedDoctorUUID.isNullOrEmpty())
                    selectedDoctorUUID
                else "",
                if (!selectedDepartmentUUID.isNullOrEmpty())
                    selectedDepartmentUUID
                else "",
                if (!selectedLabUUID.isNullOrEmpty())
                    selectedLabUUID
                else "",
                if (!selectedRadiologyUUID.isNullOrEmpty())
                    selectedRadiologyUUID
                else ""
            )
        ).observe(viewLifecycleOwner) {
            Log.e("msg", "res____" + it.msg)
            disableRefreshView()

            loading = false
            if (it.doctorList.size == pageSize) {
                page += 1
                nextUrl = true
            } else
                nextUrl = false
            it.doctorList.forEach { doctorObj ->
                if (!doctorsList?.contains(doctorObj)!!) {
                    doctorsList?.addAll(it.doctorList ?: emptyList())
                }
            }

            if (!doctorsList.isNullOrEmpty()) {
                adapterAppointmentDoctorList?.doctorsList = doctorsList!!
                rvDoctorList.visibility = View.VISIBLE
                tvNoRecordFound.visibility = View.GONE
            } else {
                rvDoctorList.visibility = View.GONE
                tvNoRecordFound.visibility = View.VISIBLE
                tvNoRecordFound.text = it.msg.toString()
            }
        }
    }

    private fun swipeToRefreshView() {
        srlDoctorList.apply {
            setOnRefreshListener {
                isRefresh = true
                subscribeToDoctorAllListAPI()
            }
        }
    }

    private fun disableRefreshView() {
        srlDoctorList.apply {
            Handler().postDelayed({
                if (isRefreshing) {
                    isRefreshing = false
                    isRefresh = false
                }
            }, 1000)
        }
    }

    private fun subscribeToNetworkEvents() {
        viewModelAppointment.networkEvent.observe(viewLifecycleOwner) {
            disableRefreshView()
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
        pbDoctors.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val DOCTOR = "DOCTOR"

        @JvmStatic
        fun newInstance(doctor: Doctors) =
            AppointmentDoctorsFragment().apply {
                arguments = bundleOf(
                    DOCTOR to doctor
                )
            }
    }
}
