package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.ui


//import com.applandeo.materialcalendarview.EventDay
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentOtScheduleChildBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.response.*
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.ui.adapter.CalendarAdapter
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.ui.adapter.EventCalenderAdapter
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.viewmodel.OtScheduleViewModel
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.fragment_ot_schedule_child.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OtScheduleChildFragment : Fragment(), View.OnClickListener,
    AddSurgeryDialogFragment.OnRefreshListener {

    var binding: FragmentOtScheduleChildBinding? = null
    private var viewModel: OtScheduleViewModel? = null
    private var utils: Utils? = null
    private var tarDate: String? = null

    private var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null
    private var patientId: Int? = null
    private var departmentUuid: Int? = null
    private var encounterType: Int? = null
    private var encounterDoctorUuid: Int? = null
    private var encounterUuid: Int? = null

    private var surgeryNamesList = mutableMapOf<Int, String>()
    private var otNamesList = mutableMapOf<Int, String>()
    private var otTypeList = mutableMapOf<Int, String>()

    private var selectedSurgNameId: String? = ""
    private var selectedOtNameId: String? = ""
    private var selectedOtTypeId: String? = ""

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    //Spinner
    private var OtSurguryName: ArrayList<OtSurgeryNameresponseContent?>? = ArrayList()
    private var OtNames: ArrayList<OtNameSpinnerresponseContent?>? = ArrayList()
    private var OtType: ArrayList<OtTyperesponseContent?>? = ArrayList()


    //Calendar
    lateinit var c: Calendar
    var date: String = " "
    var month_file: Int = 0


    /*
    * Calendar Related
    * */

    private var otScheduleChildFragment: OtScheduleChildFragment? = null
    private var eventList = ArrayList<OtScheduleToCalendarresponseContent>()
    private val eventDateList = ArrayList<String>()
    private var year: Int = 0
    private var month: Int = 0
    private var currentDate: Int = 0

    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventCalenderAdapter: EventCalenderAdapter

    /*
    * Calendar end
    * */

    fun getInstance(): OtScheduleChildFragment {
        return otScheduleChildFragment!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initial set Calendar
        otScheduleChildFragment = this
        month = Calendar.getInstance(Locale.ENGLISH)[Calendar.MONTH]
        year = Calendar.getInstance(Locale.ENGLISH)[Calendar.YEAR]
        val calendar =
            Calendar.getInstance(Locale.ENGLISH)
        currentDate = calendar.get(Calendar.DATE)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_ot_schedule_child,
                container,
                false
            )
        utils = Utils(requireContext())

        viewModel = ViewModelProvider(this)[OtScheduleViewModel::class.java]

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patientId = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        departmentUuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounterUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID) //sri 0
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        encounterDoctorUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID) //sri 0
        userDetailsRoomRepository = activity?.application?.let { UserDetailsRoomRepository(it) }

        trackOtScheduleVisit(utils?.getEncounterType())

        var c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)

        val minDate = Calendar.getInstance()
        minDate.set(Calendar.DAY_OF_MONTH, minDate.getActualMinimum(Calendar.DAY_OF_MONTH))
        minDate.set(Calendar.MONTH, month)
        minDate.set(Calendar.YEAR, year)
        minDate.set(Calendar.HOUR_OF_DAY, 0)
        minDate.set(Calendar.MINUTE, 0)
        minDate.set(Calendar.SECOND, 0)
        val maxDate = Calendar.getInstance()

        maxDate.set(Calendar.DAY_OF_MONTH, maxDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        maxDate.set(Calendar.MONTH, month + 4)
        maxDate.set(Calendar.YEAR, year)


        var dateStr = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        tarDate = simpleDateFormat.format(dateStr)

        //  Log.e("curDate", tarDate)

        viewModel?.getOtScheduleList(
            facilityId,
            tarDate!!,
            selectedSurgNameId!!,
            selectedOtNameId!!,
            selectedOtTypeId!!,
            getOtScheduleListCallback
        )


        loadSpinners()
        listeners()

        return binding?.root
    }

    private fun loadSpinners() {
        viewModel?.getSurgeryName(facilityId, departmentUuid, getOtSurgeryCallback)
        viewModel?.getOtName(facilityId, getOtNameRetrofitCallback)
        viewModel?.getOtType(facilityId, getOtTypeSurgeryCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListener()
        setCalendarOfOTSchedule()
        initCalendarEventList()
    }


    fun listeners() {

        binding?.searchCardView!!.setOnClickListener {

            viewModel?.getOtScheduleList(
                facilityId,
                tarDate!!,
                selectedSurgNameId!!,
                selectedOtNameId!!,
                selectedOtTypeId!!,
                getOtScheduleListCallback
            )
        }

        binding?.repeatCardView!!.setOnClickListener {
            selectedSurgNameId = ""
            selectedOtNameId = ""
            selectedOtTypeId = ""

            binding?.spinnerSurgeryName!!.setSelection(0)
            binding?.spinnerOTtype!!.setSelection(0)
            binding?.spinnerOTName!!.setSelection(0)

            viewModel?.getOtScheduleList(
                facilityId,
                tarDate!!,
                selectedSurgNameId!!,
                selectedOtNameId!!,
                selectedOtTypeId!!,
                getOtScheduleListCallback
            )
        }

        binding?.addCardView!!.setOnClickListener {
            trackOtScheduleAdd(utils?.getEncounterType())
            val ft = childFragmentManager.beginTransaction()
            val surgeryDialog = AddSurgeryDialogFragment()
            surgeryDialog.show(ft, "Tag")

        }

//        binding?.spinnerSurgeryName?.setOnTouchListener { v, event ->
//            viewModel?.getSurgeryName(facilityId, departmentUuid, getOtSurgeryCallback)
//            false
//        }
//
//        binding?.spinnerOTName?.setOnTouchListener { v, event ->
//            viewModel?.getOtName(facilityId, getOtNameRetrofitCallback)
//            false
//        }
//
//        binding?.spinnerOTtype?.setOnTouchListener { v, event ->
//            viewModel?.getOtType(facilityId, getOtTypeSurgeryCallback)
//            false
//        }


        binding?.spinnerSurgeryName!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedSurgNameId = null
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (pos == 0) {
                        selectedSurgNameId = ""
                    } else {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()
                        selectedSurgNameId =
                            surgeryNamesList.filterValues { it == itemValue }.keys.toList()[0].toString()
                        Log.e("SurguryNameId", "" + selectedSurgNameId)
                    }
                }

            }


        binding?.spinnerOTName!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedOtNameId = null
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (pos == 0) {
                        selectedOtNameId = ""
                    } else {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()
                        selectedOtNameId =
                            otNamesList.filterValues { it == itemValue }.keys.toList()[0].toString()
                        Log.e("OtName", "" + selectedOtNameId)
                    }
                }

            }


        binding?.spinnerOTtype!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedOtTypeId = null
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (pos == 0) {
                        selectedOtTypeId = ""
                    } else {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()
                        selectedOtTypeId =
                            otTypeList.filterValues { it == itemValue }.keys.toList()[0].toString()
                        Log.e("OtType", "" + selectedOtTypeId)
                    }
                }

            }


    }


    val getOtNameRetrofitCallback = object : RetrofitCallback<OtNameSpinnerResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<OtNameSpinnerResponseModel>?) {
            if (responseBody?.isSuccessful == true) {

                OtNames!!.add(OtNameSpinnerresponseContent())
                OtNames!!.addAll(responseBody.body()?.responseContents!!)
                setOtNameValue(OtNames)

            }
        }

        override fun onBadRequest(errorBody: Response<OtNameSpinnerResponseModel>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }


    fun setOtNameValue(responseContents: List<OtNameSpinnerresponseContent?>?) {

        otNamesList =
            responseContents?.map { it?.om_uuid!! to it.om_name!! }!!.toMap().toMutableMap()


        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_item,
            otNamesList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerOTName!!.adapter = adapter

        binding?.spinnerOTName?.prompt = responseContents.get(0)?.om_name
        binding?.spinnerOTName?.setSelection(0)

    }

    val getOtSurgeryCallback = object : RetrofitCallback<OtSurgeryNameResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<OtSurgeryNameResponseModel>?) {
            if (responseBody?.isSuccessful == true) {

                OtSurguryName!!.add(OtSurgeryNameresponseContent())
                OtSurguryName!!.addAll(responseBody.body()?.responseContents!!)
                setSurgeryName(OtSurguryName)
            }
        }

        override fun onBadRequest(errorBody: Response<OtSurgeryNameResponseModel>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }


    fun setSurgeryName(responseContents: List<OtSurgeryNameresponseContent?>?) {

        surgeryNamesList =
            responseContents?.map { it?.p_uuid!! to it.p_name!! }!!.toMap().toMutableMap()

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_item,
            surgeryNamesList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerSurgeryName!!.adapter = adapter

        binding?.spinnerSurgeryName?.prompt = responseContents.get(0)?.p_name
        binding?.spinnerSurgeryName?.setSelection(0)

    }


    val getOtTypeSurgeryCallback = object : RetrofitCallback<OtTypeResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<OtTypeResponseModel>?) {
            if (responseBody?.isSuccessful == true) {

                OtType!!.add(OtTyperesponseContent())
                OtType!!.addAll(responseBody.body()?.responseContents!!)
                setOtTypeValue(OtType)

            }
        }

        override fun onBadRequest(errorBody: Response<OtTypeResponseModel>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }


    fun setOtTypeValue(responseContents: List<OtTyperesponseContent?>?) {
        otTypeList = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_item,
            otTypeList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerOTtype!!.adapter = adapter

        binding?.spinnerOTtype?.prompt = responseContents.get(0)?.name
        binding?.spinnerOTtype?.setSelection(0)
    }


    val getOtScheduleListCallback = object : RetrofitCallback<OtSchedulToCalendarResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<OtSchedulToCalendarResponseModel>?) {
            if (responseBody?.isSuccessful == true) {

                eventList =
                    responseBody.body()?.responseContents!! as ArrayList<OtScheduleToCalendarresponseContent>
                if (!eventList.isNullOrEmpty())
                    setCalendarEventData(eventList)
                else
                    Toast.makeText(activity, "Sorry.Data not found", Toast.LENGTH_LONG).show()
//                loadCalender(responseBody.body()?.responseContents!!)

            }
        }

        override fun onBadRequest(errorBody: Response<OtSchedulToCalendarResponseModel>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }

    private fun clickListener() {
        tvCalendarPrevious.setOnClickListener(this)
        tvCalendarNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvCalendarPrevious -> {
                if (month < 1) {
                    month = 11
                    year--
                } else {
                    month--
                }
                refreshCalendar()
                setCalendarOfOTSchedule()
                if (!eventList.isNullOrEmpty())
                    setCalendarEventData(eventList)
            }
            R.id.tvCalendarNext -> {
                if (month >= 11) {
                    month = 0
                    year++
                } else {
                    month++
                }
                refreshCalendar()
                setCalendarOfOTSchedule()
                if (!eventList.isNullOrEmpty())
                    setCalendarEventData(eventList)
            }
        }
    }

    private fun initCalendarEventList() {
        eventCalenderAdapter = EventCalenderAdapter(
            onClickItem = {
                val ft = childFragmentManager.beginTransaction()
                val surgeryDialog = OtScheduleViewDialogFragment(it)
                surgeryDialog.show(ft, "Tag")
            },
            onClickEdit = {

            },
            onClickDelete = {

            })
        rvEventCalender.adapter = eventCalenderAdapter
    }

    private fun setCalendarOfOTSchedule() {
        rvCalendar?.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 7, LinearLayoutManager.VERTICAL, false)
            calendarAdapter = CalendarAdapter(
                requireContext(),
                currentDate,
                month,
                year,
                tvCalendarPrevious,
                tvCalendarMonth,
                tvCalendarNext,
                onDisplayEventList = { event_list ->
                    if (event_list.size > 0) {
                        Log.e("event_list", "selected__" + event_list.size)

                        eventCalenderAdapter.addEventDetails(event_list)

                        rvEventCalender.visibility = View.VISIBLE
                        binding?.llEvent?.removeAllViews()
                        event_list.forEach { otScheduleToCalendarresponseContent ->

                            val eventView = LayoutInflater.from(requireContext())
                                .inflate(R.layout.item_calendar_event_list, null)
                            val tvEventName = eventView.findViewById<TextView>(R.id.tvEventName)
                            val rlEventItem =
                                eventView.findViewById<RelativeLayout>(R.id.rlEventItem)
                            tvEventName.text = otScheduleToCalendarresponseContent.facility_name

                            rlEventItem.setOnClickListener {
                                calendarAdapter.setSelectedPosition(-1)
                            }

                            binding?.llEvent!!.addView(eventView)
                        }
                    } else {
                        rvEventCalender.visibility = View.GONE
                        disableEventOnDay()
                    }
                }
            )
            adapter = calendarAdapter
            disableEventOnDay()

            llCalendar.visibility = View.VISIBLE
        }
    }

    private fun disableEventOnDay() {
        try {
            binding?.let {
//                llEvent.removeAllViews()
                cvEvent.layoutParams.height = 0
                cvEvent.requestLayout()
                cvEvent.isEnabled = false
                cvEvent.visibility = View.GONE
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun refreshCalendar() {
//        calendarAdapter.disableEventOnDay()
        calendarAdapter.setSelectedPosition(-1)
    }

    private fun setCalendarEventData(eventList: ArrayList<OtScheduleToCalendarresponseContent>) {
        try {
            eventDateList.clear()
            Log.e("currentDate", "___$currentDate")
            eventList.forEach {
                if (it.os_start_date != null) {
                    val format = SimpleDateFormat(
                        "yyyy-MM-dd hh:mm:ss",
                        Locale.ENGLISH
                    )
                    val date = format.parse(it.os_start_date)
                    val calendar = Calendar.getInstance()
                    calendar.time = date!!

                    val currentMonth = month + 1

                    val eventStartYear = calendar[Calendar.YEAR]
                    val eventEndYear = calendar[Calendar.YEAR]
                    val eventStartMonth = calendar[Calendar.MONTH] + 1
                    val eventEndMonth = calendar[Calendar.MONTH] + 1
                    val eventStartDate = calendar[Calendar.DATE]
                    val eventEndDate = calendar[Calendar.DATE]

                    if (year == eventStartYear) {
                        if (currentMonth == eventStartMonth && currentMonth == eventEndMonth) {
                            for (startDate in eventStartDate..eventEndDate) {
                                if (!eventDateList.contains(startDate.toString() + ""))
                                    eventDateList.add(
                                        startDate.toString() + ""
                                    )
                            }
                        } else if (eventStartMonth == currentMonth && eventEndMonth >= currentMonth) {
                            for (startDate in eventStartDate..31) {
                                if (!eventDateList.contains(startDate.toString() + ""))
                                    eventDateList.add(
                                        startDate.toString() + ""
                                    )
                            }
                        } else if (currentMonth in (eventStartMonth + 1)..eventEndMonth) {
                            for (startDate in 0..31) {
                                if (!eventDateList.contains(startDate.toString() + ""))
                                    eventDateList.add(
                                        startDate.toString() + ""
                                    )
                            }
                        }
                    } else if (year in eventStartYear..eventEndYear) {
                        if (currentMonth == eventEndMonth) {
                            for (startDate in eventEndDate downTo 1) {
                                if (!eventDateList.contains(startDate.toString() + ""))
                                    eventDateList.add(
                                        startDate.toString() + ""
                                    )
                            }
                        } else if (currentMonth < eventEndMonth) {
                            for (startDate in eventStartDate..31) {
                                if (!eventDateList.contains(startDate.toString() + ""))
                                    eventDateList.add(
                                        startDate.toString() + ""
                                    )
                            }
                        }
                    }
                }
            }
            calendarAdapter.setData(
                eventList,
                eventDateList,
                true
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRefreshList() {
        viewModel?.getOtScheduleList(
            facilityId,
            tarDate!!,
            selectedSurgNameId!!,
            selectedOtNameId!!,
            selectedOtTypeId!!,
            getOtScheduleListCallback
        )
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is AddSurgeryDialogFragment) {
            childFragment.setOnRefreshListener(this)
        }
    }

    private fun trackOtScheduleVisit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackOtScheduleVisit(type)
    }

    private fun trackOtScheduleAdd(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackOtScheduleAdd(type)
    }
}
