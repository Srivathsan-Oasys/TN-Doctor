package com.hmis_tn.doctor.ui.telemedicine.session

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.component.extention.toast
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.data.event.NetworkEvent
import com.hmis_tn.doctor.data.networking.api.req.ReqDepartmentBody
import com.hmis_tn.doctor.data.networking.api.req.ReqDoctorBody
import com.hmis_tn.doctor.data.networking.api.res.*
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.ui.telemedicine.AppointmentActivity
import com.hmis_tn.doctor.ui.viewmodel.AppointmentViewModel
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.fragment_appointment_session_create.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppointmentSessionCreateFragment : Fragment(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private val viewModelAppointment by viewModel<AppointmentViewModel>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var tokenBearer = ""
    private var userUUID: Int = -1
    private var facilityUUID: Int = -1

    private var appointmentSession: ResAppointmentSessionList? = null

    private var instituteList: ArrayList<ResInstituteList>? = ArrayList()
    private var instituteListMap = mutableMapOf<Int, String>()
    var instituteUUID: String = ""

    private var doctorList: ArrayList<Doctors>? = ArrayList()
    private var doctorListMap = mutableMapOf<Int, String>()
    var doctorUUID: String = ""

    private var radiologyList: ArrayList<Doctors>? = ArrayList()
    private var radiologyListMap = mutableMapOf<Int, String>()
    var radiologyUUID: String = ""

    private var departmentList: ArrayList<DepartmentList>? = ArrayList()
    private var departmentListMap = mutableMapOf<Int, String>()
    var departmentUUID: String = ""

    private var labList: ArrayList<Doctors>? = ArrayList()
    private var labListMap = mutableMapOf<Int, String>()
    var labUUID: String = ""
    private var TYPE = ""
    private var myCalendarStart = Calendar.getInstance(TimeZone.getDefault())
    private var myCalendarEnd = Calendar.getInstance(TimeZone.getDefault())
    private var sdfFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var displayFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    private var isHoliday = false
    private var startDate = ""
    private var endDate = ""
    private var startAndEndDate = ""
    private var startAndEndDateDisplay = ""
    private val instituteSelectedSpinner: HashMap<Int, Int> = HashMap()
    private val departmentSelectedSpinner: HashMap<Int, Int> = HashMap()
    private val doctorSelectedSpinner: HashMap<Int, Int> = HashMap()
    private val labSelectedSpinner: HashMap<Int, Int> = HashMap()
    private val radiologySelectedSpinner: HashMap<Int, Int> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            appointmentSession = it.getParcelable(APPOINTMENT_SESSION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_session_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Utils(requireContext()).setCalendarLocale("en", requireContext())
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        tokenBearer = AppConstants.BEARER_AUTH + userDataStoreBean?.access_token
        userUUID = userDataStoreBean?.uuid!!
        facilityUUID = userDataStoreBean.facility_uuid!!

        initToolBar()

        if (HmisApplication.isConnected()) {
            subscribeToNetworkEvents()
            initUI()
        } else {
            showLoading(false)
            requireContext().toast(getString(R.string.no_connection))
        }
        initClickListener()
    }

    private fun initToolBar() {
        rlAppointmentToolbar.apply {
            this.tbAppointment?.apply {
                if (activity is AppointmentActivity) {
                    (activity as AppointmentActivity).apply {
                        tbAppointment?.title = getString(R.string.appointment_session)
                    }
                }
                setNavigationOnClickListener {
                    (activity as AppointmentActivity).onBackPressed()
                }
            }
//            this.ivAppointmentProfile.loadCircleImage("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSbPhAkKyZtvZMx02vMMTzhxhX_skAjFOcBL098j4io5S6zqefT&usqp=CAU")
        }
    }

    private fun initUI() {
        subscribeToInstituteListAPI()
        subscribeToDoctorListAPI()
        subscribeToLabListAPI()
        subscribeToRadiologyListAPI()
        subscribeToDepartmentListAPI()

        showLoading(true)
        Handler().postDelayed({
            initBindData()
        }, 1000)
    }

    private fun initBindData() {
        appointmentSession?.also { appointment ->
            startDate = appointment.start_date ?: startDate
            endDate = appointment.start_date ?: endDate
            startAndEndDate = appointment.holiday_from!! + "" + appointment.holiday_to

            etSessionStartDate.setText(startDate)
            etSessionEndDate.setText(endDate)
            etSessionStartAndEndDate.setText(startAndEndDate)
            swSessionStatus.isChecked = appointment.is_active ?: true

            spInstituteList?.setSelection(instituteSelectedSpinner[appointment.facility_uuid] ?: 0)
            spDepartmentList?.setSelection(
                departmentSelectedSpinner[appointment.department_uuid] ?: 0
            )
            spDoctorList?.setSelection(doctorSelectedSpinner[appointment.doc_uuid] ?: 0)
            spLabList?.setSelection(labSelectedSpinner[appointment.lab_uuid] ?: 0)
            spRadiologyList?.setSelection(radiologySelectedSpinner[appointment.rad_uuid] ?: 0)

        }
    }

    private fun initClickListener() {
        etSessionDepartment.setOnClickListener(this)
        etSessionStartDate.setOnClickListener(this)
        etSessionEndDate.setOnClickListener(this)
        etSessionStartAndEndDate.setOnClickListener(this)
        btnBookedAppointment.setOnClickListener(this)
        btnAddBookAppointment.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.etSessionDepartment -> Unit
            R.id.etSessionStartDate -> openDatePickerDialog("START")
            R.id.etSessionEndDate -> openDatePickerDialog("END")
            R.id.etSessionStartAndEndDate -> {
                isHoliday = true
                openDatePickerDialog("START")
            }
            R.id.btnBookedAppointment -> clear()
            R.id.btnAddBookAppointment -> {
                if (isValid()) {
                    findNavController().navigate(R.id.action_nav_appointment_create_session_to_session_dialog)
                }
            }
        }
    }

    private fun initInstituteSpinner(instituteList: ArrayList<ResInstituteList>) {
        instituteListMap =
            instituteList.map { it.uuid!! to it.name!! }.toMap().toMutableMap()
        instituteSelectedSpinner.clear()

        instituteList.forEach {
            instituteSelectedSpinner[it.uuid!!] = it.uuid!!
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            instituteListMap.values.toMutableList()
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spInstituteList?.adapter = adapter

        spInstituteList?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!instituteList.isNullOrEmpty())
                    instituteUUID = instituteList[position].uuid!!.toString()
            }
        }
    }

    private fun initDoctorSpinner(doctorList: ArrayList<Doctors>) {
        doctorListMap =
            doctorList.map { it.uuid!! to it.firstName!! }.toMap().toMutableMap()
        doctorSelectedSpinner.clear()
        doctorList.forEach {
            doctorSelectedSpinner[it.uuid!!] = it.uuid!!
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            doctorListMap.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDoctorList?.adapter = adapter

        spDoctorList?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!doctorList.isNullOrEmpty())
                    doctorUUID = doctorList[position].uuid!!.toString()
            }
        }
    }

    private fun initLabSpinner(labList: ArrayList<Doctors>) {
        labListMap =
            labList.map { it.uuid!! to it.firstName!! }.toMap().toMutableMap()

        labSelectedSpinner.clear()
        labList.forEach {
            labSelectedSpinner[it.uuid!!] = it.uuid!!
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            labListMap.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spLabList?.adapter = adapter

        spLabList?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!labList.isNullOrEmpty())
                    labUUID = labList[position].uuid!!.toString()
            }
        }
    }

    private fun initRadiologySpinner(radiologyList: ArrayList<Doctors>) {
        radiologyListMap =
            radiologyList.map { it.uuid!! to it.firstName!! }.toMap().toMutableMap()
        radiologySelectedSpinner.clear()
        radiologyList.forEach {
            radiologySelectedSpinner[it.uuid!!] = it.uuid!!
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            radiologyListMap.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spRadiologyList?.adapter = adapter

        spRadiologyList?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!radiologyList.isNullOrEmpty())
                    radiologyUUID = radiologyList[position].uuid!!.toString()
            }
        }
    }

    private fun initDepartmentSpinner(departmentList: ArrayList<DepartmentList>) {
        departmentListMap =
            departmentList.map { it.department?.uuid!! to it.department.name!! }.toMap()
                .toMutableMap()

        departmentSelectedSpinner.clear()
        departmentList.forEach {
            departmentSelectedSpinner[it.uuid!!] = it.uuid
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            departmentListMap.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDepartmentList?.adapter = adapter

        spDepartmentList?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!departmentList.isNullOrEmpty())
                    departmentUUID = departmentList[position].uuid!!.toString()
            }
        }
    }


    private fun subscribeToInstituteListAPI() {
        viewModelAppointment.getInstituteList(tokenBearer, userUUID, facilityUUID) {
            if (it != null) {
                val resInstituteList = ResInstituteList(-1, "Select Institute Name")
                this.instituteList?.add(0, resInstituteList)
                it.responseContents?.also { instituteList ->
                    instituteList.forEach { institute ->
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
    * LAB RELATED SEARCH
    * */
    private fun subscribeToLabListAPI() {
        viewModelAppointment.searchAPI(
            tokenBearer, userUUID,
            ReqDoctorBody("Lab Incharge", true, facilityUUID)
        )
            .observe(viewLifecycleOwner) {
                val doctor = Doctors(-1, "Select Lab", "Select Lab")
                this.labList?.add(0, doctor)
                if (!it.doctorList.isNullOrEmpty()) {
                    it.doctorList?.forEach { doc ->
                        labList?.add(doc)
                    }
                }
                initLabSpinner(labList!!)
            }
    }

    /*
    * RADIOLOGY RELATED SEARCH
    * */
    private fun subscribeToRadiologyListAPI() {
        viewModelAppointment.searchAPI(
            tokenBearer, userUUID,
            ReqDoctorBody("Radiology Incharege", true, facilityUUID)
        )
            .observe(viewLifecycleOwner) {
                if (!it.doctorList.isNullOrEmpty()) {
                    val doctor = Doctors(-1, "Select Radiology", "Select Radiology")
                    this.radiologyList?.add(0, doctor)
                    it.doctorList?.forEach { doc ->
                        radiologyList?.add(doc)
                    }
                }
                initRadiologySpinner(radiologyList!!)
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


    private fun subscribeToNetworkEvents() {
        viewModelAppointment.networkEvent.observe(viewLifecycleOwner) {
            when (it) {
                NetworkEvent.Loading -> showLoading(true)
                NetworkEvent.Success -> showLoading(false)
                is NetworkEvent.ApiMessage -> {
                    showLoading(false)
                    it.getContentIfNotHandled()?.run {
                        requireContext().toast(it.msg)
                    }
                }
                is NetworkEvent.Failure -> {
                    showLoading(false)
                    it.getContentIfNotHandled()?.run {
                        requireContext().toast(it.res)
                    }
                }
            }
        }
    }

    private fun openDatePickerDialog(type: String) {
        TYPE = type
        val dialog: DatePickerDialog? = if (TYPE == "START") {
            val mYear: Int = myCalendarStart.get(Calendar.YEAR)
            val mMonth: Int = myCalendarStart.get(Calendar.MONTH)
            val mDay: Int = myCalendarStart.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(
                requireContext(), this,
                mYear, mMonth, mDay
            )
        } else {
            val mYear: Int = myCalendarEnd.get(Calendar.YEAR)
            val mMonth: Int = myCalendarEnd.get(Calendar.MONTH)
            val mDay: Int = myCalendarEnd.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(
                requireContext(), this,
                mYear, mMonth, mDay
            )
        }
        if (isHoliday) {
            dialog?.datePicker?.minDate = myCalendarStart.timeInMillis
            dialog?.datePicker?.maxDate = myCalendarEnd.timeInMillis
        } else {
            if (TYPE == "START") {
                dialog?.datePicker?.minDate = System.currentTimeMillis() - 1000
            } else {
                dialog?.datePicker?.minDate = myCalendarStart.timeInMillis
            }
        }
        dialog!!.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        if (TYPE == "START") {
            myCalendarStart.set(Calendar.YEAR, year)
            myCalendarStart.set(Calendar.MONTH, month)
            myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            if (isHoliday) {
                startAndEndDateDisplay = displayFormat.format(myCalendarStart.time)
                openDatePickerDialog(type = "END")
            } else {
                startDate = sdfFormat.format(myCalendarStart.time)
                etSessionStartDate.setText(displayFormat.format(myCalendarStart.time))
            }
        } else if (TYPE == "END") {
            myCalendarEnd.set(Calendar.YEAR, year)
            myCalendarEnd.set(Calendar.MONTH, month)
            myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            if (isHoliday) {
                startAndEndDateDisplay += " to " + displayFormat.format(myCalendarEnd.time)
                etSessionStartAndEndDate.setText(startAndEndDateDisplay)
            } else {
                endDate = sdfFormat.format(myCalendarEnd.time)
                etSessionEndDate.setText(displayFormat.format(myCalendarEnd.time))
            }
            isHoliday = false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        pbSessionCreate.isVisible = isLoading
    }

    private fun clear() {
        spInstituteList?.setSelection(0)
        spDepartmentList?.setSelection(0)
        spRadiologyList?.setSelection(0)
        spDoctorList?.setSelection(0)
        etSessionStartDate.setText("")
        etSessionEndDate.setText("")
        etSessionStartAndEndDate.setText("")
    }

    private fun isValid(): Boolean {
        return when {
            spInstituteList.selectedItemPosition == 0 -> {
                requireContext().toast("Choose Institute to process !")
                false
            }
            spDepartmentList.selectedItemPosition == 0 -> {
                requireContext().toast("Choose Department to process !")
                false
            }
            etSessionStartDate.text.toString().isEmpty() -> {
                requireContext().toast("Choose Start date to process !")
                false
            }
            etSessionEndDate.text.toString().isEmpty() -> {
                requireContext().toast("Choose End date to process !")
                false
            }
            else -> true
        }
    }

    companion object {
        const val APPOINTMENT_SESSION = "APPOINTMENT_SESSION"

        @JvmStatic
        fun newInstance(resAppointmentSessionList: ResAppointmentSessionList) =
            AppointmentSessionCreateFragment().apply {
                arguments = bundleOf(
                    APPOINTMENT_SESSION to resAppointmentSessionList
                )
            }
    }
}