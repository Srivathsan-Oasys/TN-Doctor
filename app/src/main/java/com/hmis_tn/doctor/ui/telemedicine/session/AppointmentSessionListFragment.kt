package com.hmis_tn.doctor.ui.telemedicine.session

import android.app.Dialog
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.component.extention.toast
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.data.event.NetworkEvent
import com.hmis_tn.doctor.data.networking.api.req.ReqAppointmentSession
import com.hmis_tn.doctor.data.networking.api.req.ReqDeleteSession
import com.hmis_tn.doctor.data.networking.api.req.ReqDepartmentBody
import com.hmis_tn.doctor.data.networking.api.req.ReqDoctorBody
import com.hmis_tn.doctor.data.networking.api.res.*
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.ui.telemedicine.AppointmentActivity
import com.hmis_tn.doctor.ui.telemedicine.AppointmentViewModel
import com.hmis_tn.doctor.ui.telemedicine.session.AppointmentSessionCreateFragment.Companion.APPOINTMENT_SESSION
import com.hmis_tn.doctor.ui.telemedicine.session.AppointmentSessionViewFragment.Companion.APPOINTMENT_VIEW
import com.hmis_tn.doctor.ui.telemedicine.session.adapter.AdapterSessionListAppointment
import com.hmis_tn.doctor.utils.FileHelperAppointment
import kotlinx.android.synthetic.main.fragment_appointment_session_list.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AppointmentSessionListFragment : Fragment(), View.OnClickListener {

    private var adapterSessionListAppointment: AdapterSessionListAppointment? = null

    private val viewModelAppointment by viewModel<AppointmentViewModel>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var tokenBearer = ""
    private var userUUID: Int = -1
    private var facilityUUID: Int = -1

    private var pageSize = 10
    private var page: Int = 0
    private var loading: Boolean = false
    private var nextUrl: Boolean = true
    private var toDownload: Boolean = false
    private var bookedAppointmentSessionList: MutableList<ResAppointmentSessionList>? =
        mutableListOf()


    private var instituteList: ArrayList<ResInstituteList>? = ArrayList()
    private var instituteListMap = mutableMapOf<Int, String>()
    var instituteUUID: String = ""

    private var doctorList: ArrayList<Doctors>? = ArrayList()
    private var doctorListMap = mutableMapOf<Int, String>()
    var doctorUUID: String = ""

    private var departmentList: ArrayList<DepartmentList>? = ArrayList()
    private var departmentListMap = mutableMapOf<Int, String>()
    var departmentUUID: String = ""

    private var instituteID: Int? = null
    private var departmentID: Int? = null
    private var doctorID: Int? = null
    private var labSearch: String? = null
    private var radiologySearch: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_session_list, container, false)
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
        initUI()
        if (HmisApplication.isConnected()) {
            subscribeToNetworkEvents()
            subscribeToSessionListAPI()

            subscribeToInstituteListAPI()
            subscribeToDoctorListAPI()
            subscribeToDepartmentListAPI()
        } else {
            showLoading(false)
            requireContext().toast(getString(R.string.no_connection))
        }
    }

    private fun initToolBar() {
        rlAppointmentToolbar.run {
            this.tbAppointment.apply {
                title = getString(R.string.appointment_session)
                setNavigationOnClickListener {
                    (activity as AppointmentActivity).onBackPressed()
                }
            }
            //            this.ivAppointmentProfile.loadCircleImage("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSbPhAkKyZtvZMx02vMMTzhxhX_skAjFOcBL098j4io5S6zqefT&usqp=CAU")
        }
    }

    private fun clickListener() {
        btnAddBookAppointment.setOnClickListener(this)
        btnDownloadAppointment.setOnClickListener(this)
        cvFilterAppointmentsSessionList.setOnClickListener(this)
        btnSearchFilter.setOnClickListener(this)
        btnClearFilter.setOnClickListener(this)
        flSessionList.setOnClickListener(this)
        tvAdvancedSearch.setOnClickListener(this)
    }

    private fun initUI() {
        rvAppointmentPatientList.apply {
            adapterSessionListAppointment = AdapterSessionListAppointment(
                deleteSession = {
                    alertDialogConfirmDeleteSession(it)
                },
                viewSession = {
                    findNavController().navigate(
                        R.id.action_nav_appointment_session_list_to_nav_appointment_session_view,
                        bundleOf(APPOINTMENT_VIEW to it)
                    )
                },
                editSession = {
                    findNavController().navigate(
                        R.id.action_nav_appointment_session_list_to_appointment_create_session,
                        bundleOf(APPOINTMENT_SESSION to it)
                    )
                }
            )
            adapter = adapterSessionListAppointment

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
                        if (etAppointmentSessionListSearch.text.toString().isNotEmpty())
                            subscribeToSessionListFilterAPI()
                        else
                            subscribeToSessionListAPI()
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddBookAppointment -> navigateToBookAppointmentNew()
            R.id.btnDownloadAppointment -> downloadPDF()
            R.id.cvFilterAppointmentsSessionList -> navigationDrawer()
            R.id.btnSearchFilter -> {
                navigationDrawer()
                if (etAppointmentSessionListSearch.text.toString()
                        .isNotEmpty() || !labSearch.isNullOrEmpty() ||
                    !radiologySearch.isNullOrEmpty() || instituteID != null || departmentID != null || doctorID != null
                ) {
                    bookedAppointmentSessionList?.clear()
                    labSearch =
                        if (etLabSearch.text.toString().isNotEmpty()) etLabSearch.text.toString()
                            .trim() else null
                    radiologySearch = if (etRadiologySearch.text.toString()
                            .isNotEmpty()
                    ) etRadiologySearch.text.toString().trim() else null
                    subscribeToSessionListFilterAPI()
                } else
                    subscribeToSessionListAPI()
            }
            R.id.btnClearFilter -> {
                etAppointmentSessionListSearch.text.clear()
                departmentID = null
                instituteID = null
                doctorID = null
                labSearch = ""
                radiologySearch = ""

            }
            R.id.flSessionList -> {
//                etAppointmentSessionListSearch.hideKeyboard()
                if (drawerLayout.isDrawerOpen(GravityCompat.END))
                    drawerLayout.closeDrawer(
                        GravityCompat.END
                    )

            }
            R.id.tvAdvancedSearch -> {
                if (llAdvanceSearch.visibility == View.GONE) {
                    tvAdvancedSearch.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    llAdvanceSearch.visibility = View.VISIBLE
                } else {
                    tvAdvancedSearch.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey
                        )
                    )
                    llAdvanceSearch.visibility = View.GONE
                }
            }
        }
    }

    private fun downloadPDF() {
        val folderName = "${FileHelperAppointment.PARENT_FOLDER}/Appointments"
        val simpleDateFormat = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss", Locale.getDefault())
        val date = simpleDateFormat.format(Calendar.getInstance().time)

        val fileHelper = FileHelperAppointment(context)
        val bitmap = fileHelper.getScreenshotFromRecyclerView(rvAppointmentPatientList)
        bitmap?.let {
            fileHelper.saveImageToPDF(
                title = mainLayout,
                bitmap = bitmap,
                folder = File(
                    Environment.getExternalStorageDirectory().toString() + "/" + folderName
                ),
                filename = "Session$date",
                onDownloadPDFSuccess = {

                }
            )
        }
        toDownload = false
    }

    private fun navigationDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.openDrawer(GravityCompat.END)
        else
            drawerLayout.closeDrawer(GravityCompat.END)
    }

    private fun navigateToBookAppointmentNew() {
        findNavController().navigate(R.id.action_nav_appointment_session_list_to_appointment_create_session)
    }

    private fun subscribeToSessionListAPI() {
        viewModelAppointment.getAppointmentSession(
            tokenBearer,
            userUUID,
            facilityUUID,
            ReqAppointmentSession(page, 25, "", "")
        ).observe(viewLifecycleOwner) {
            if (it.statusCode == 200) {
                tvSessionListCount.text = it.totalRecords.toString()
                loading = false
                if (it.responseContents?.size == pageSize) {
                    page += 1
                    nextUrl = true
                } else
                    nextUrl = false
                it.responseContents?.forEach { patientObj ->
                    if (!bookedAppointmentSessionList?.contains(patientObj)!!) {
                        bookedAppointmentSessionList?.addAll(it.responseContents ?: emptyList())
                    }
                }
            }
            if (!bookedAppointmentSessionList.isNullOrEmpty()) {
                adapterSessionListAppointment?.bookedAppointmentList =
                    bookedAppointmentSessionList as MutableList<ResAppointmentSessionList>
                visibleRecordViews(isRecordAvailable = true)
            } else {
                visibleRecordViews(isRecordAvailable = false)
            }
        }
    }

    private fun initInstituteSpinner(instituteList: ArrayList<ResInstituteList>) {
        instituteListMap =
            instituteList.map { it.uuid!! to it.name!! }.toMap().toMutableMap()

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            instituteListMap.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spInstituteList.adapter = adapter

        spInstituteList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!instituteList.isNullOrEmpty()) {
                    if (position == 0) {
                        instituteID = null
                    } else
                        instituteID = instituteList[position].uuid!!
                    instituteUUID = instituteList[position].uuid!!.toString()
                }
            }
        }
    }

    private fun initDoctorSpinner(doctorList: ArrayList<Doctors>) {
        doctorListMap =
            doctorList.map { it.uuid!! to it.firstName!! }.toMap().toMutableMap()

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            doctorListMap.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDoctorList.adapter = adapter

        spDoctorList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!doctorList.isNullOrEmpty()) {
                    if (position == 0) {
                        doctorID = null
                    } else
                        doctorID = doctorList[position].uuid!!
                    doctorUUID = doctorList[position].uuid!!.toString()
                }
            }
        }
    }

    private fun initDepartmentSpinner(departmentList: ArrayList<DepartmentList>) {
        departmentListMap =
            departmentList.map { it.department?.uuid!! to it.department.name!! }.toMap()
                .toMutableMap()

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            departmentListMap.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDepartmentList.adapter = adapter

        spDepartmentList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!departmentList.isNullOrEmpty()) {

                    if (position == 0) {
                        departmentID = null
                    } else
                        departmentID = departmentList[position].uuid!!
                    departmentUUID = departmentList[position].uuid!!.toString()
                }
            }
        }
    }


    private fun subscribeToInstituteListAPI() {
        viewModelAppointment.getInstituteList(tokenBearer, userUUID, facilityUUID) {
            if (it != null) {
                val resInstituteList = ResInstituteList(-1, "Select Institute Name")
                this.instituteList?.add(0, resInstituteList)
                it.responseContents.also { instituteList ->
                    instituteList?.forEach { institute ->
                        this.instituteList?.add(institute)
                    }
                }
                initInstituteSpinner(this.instituteList!!)

            }
        }
    }

    /*
    * DOCTOR RELATED SEARCH
    * */
    private fun subscribeToDoctorListAPI() {
        viewModelAppointment.searchAPI(
            tokenBearer,
            userUUID,
            ReqDoctorBody("Physician", true, facilityUUID)
        ).observe(viewLifecycleOwner) {
            val doctor = Doctors(-1, "Select Doctor", "Select Doctor")
            this.doctorList?.add(0, doctor)
            if (!it.doctorList.isNullOrEmpty()) {
                it.doctorList?.forEach { doc ->
                    doctorList?.add(doc)
                }
            }
            initDoctorSpinner(doctorList!!)
        }
    }

    /*
    * DEPARTMENT RELATED LIST
    * */
    private fun subscribeToDepartmentListAPI() {
        viewModelAppointment.departmentListAPI(
            tokenBearer, userUUID,
            ReqDepartmentBody("5", facilityUUID.toString())
        )
            .observe(viewLifecycleOwner) {
                if (!it.responseContents.isNullOrEmpty()) {
                    val department =
                        DepartmentList(department = DepartmentMain("", "Select Department", -1))
                    this.departmentList?.add(0, department)
                    it.responseContents?.forEach { dep ->
                        departmentList?.add(dep)
                    }
                }
                initDepartmentSpinner(departmentList!!)
            }
    }

    private fun subscribeToSessionListFilterAPI() {
        viewModelAppointment.getAppointmentSession(
            tokenBearer,
            userUUID,
            facilityUUID,
            ReqAppointmentSession(
                page, 25, "", etAppointmentSessionListSearch.text.toString(),
                department_uuid = departmentID,
                doc_uuid = doctorID,
                facility_uuid = instituteID,
                lab_firstname = labSearch,
                rad_firstname = radiologySearch
            )
        ).observe(viewLifecycleOwner) {
            if (it.statusCode == 200) {
                tvSessionListCount.text = it.totalRecords.toString()
                loading = false
                if (it.responseContents?.size == pageSize) {
                    page += 1
                    nextUrl = true
                } else
                    nextUrl = false
                it.responseContents?.forEach { patientObj ->
                    if (!bookedAppointmentSessionList?.contains(patientObj)!!) {
                        bookedAppointmentSessionList?.addAll(it.responseContents ?: emptyList())
                    }
                }
            }
            if (!bookedAppointmentSessionList.isNullOrEmpty()) {
                adapterSessionListAppointment?.bookedAppointmentList =
                    bookedAppointmentSessionList as MutableList<ResAppointmentSessionList>
                visibleRecordViews(isRecordAvailable = true)
            } else {
                visibleRecordViews(isRecordAvailable = false)
            }
        }
    }

    private fun deleteAppointmentListAPI(appointment: ResAppointmentSessionList) {
        viewModelAppointment.deleteAppointmentList(
            tokenBearer,
            userUUID, ReqDeleteSession(Id = appointment.as_uuid)
        ) {
            if (it?.statusCode == 200 || it?.statusCode == 201) {
                requireContext().toast(it.message ?: "Success")
                subscribeToSessionListAPI()
            } else
                requireContext().toast(it?.message ?: "Try Again !")
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

    private fun visibleRecordViews(isRecordAvailable: Boolean) {
        if (isRecordAvailable) {
            nsvDownloadAppointment.visibility = View.VISIBLE
            tvNoRecordSessionList.visibility = View.GONE
        } else {
            nsvDownloadAppointment.visibility = View.GONE
            tvNoRecordSessionList.visibility = View.VISIBLE
        }
    }

    private fun alertDialogConfirmDeleteSession(appointment: ResAppointmentSessionList) {
        val builder = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage("Are you sure you want to delete ${appointment.facility_name} ?")
        builder.setPositiveButton("YES") { d, _ ->
            d?.dismiss()
            deleteAppointmentListAPI(appointment)
        }
        builder.setNegativeButton("NO") { d, _ -> d?.dismiss() }
        val alert: AlertDialog = builder.create()
        alert.setOnShowListener {
            val btnPositive: Button = alert.getButton(Dialog.BUTTON_POSITIVE)
            val btnNegative: Button = alert.getButton(Dialog.BUTTON_NEGATIVE)
            btnPositive.textSize = 16F
            btnPositive.typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins)
            btnNegative.textSize = 16F
            btnNegative.typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        }
        alert.show()

    }

    private fun showLoading(isLoading: Boolean) {
        pbAppointmentList.isVisible = isLoading
    }
}
