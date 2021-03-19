package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentCriticalCareChartChildBinding
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.compare_data.D
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.compare_data.GetCriticalCareChartCompareDataResp
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.config.CriticalCareChartFilterHeading
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.config.CriticalCareChartFilterHeadingsResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.ui.criticalcarechartconfig.ConfigCCCFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.view_model.CriticalCareChartViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CriticalCareChartChildFragment : Fragment(), ConfigCCCFragment.OnsaveHistoryRefreshListener {

    private var viewModel: CriticalCareChartViewModel? = null
    private var binding: FragmentCriticalCareChartChildBinding? = null
    private var utils: Utils? = null
    private var customProgressDialog: CustomProgressDialog? = null

    private var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null

    private var departmentUuid: Int? = null
    private var encounterType: Int? = null

    private var patientUuid: Int = 0
    private var encounter_uuid: Int = 0
    private var doctor_en_uuid: Int = 0

    private val dynamicHeadingList = ArrayList<CriticalCareChartFilterHeading>()

    private val tableHeadingList = ArrayList<CriticalCareChart>()
    private val tableRowsList = ArrayList<ObservedValue>()

    private var fragment: CriticalCareChartFormFragment? = null

    private val saveObservedValueList = ArrayList<ObservedData>()
    private val updateObservedValueList = ArrayList<ObservedDataX>()

    private var criticalCareType: Int = 0

    var mandatoryQuestionsMap = LinkedHashMap<Int, Boolean>()
    var isUpdate = false

    private val compareData =
        LinkedHashMap<String, LinkedHashMap<String, Float>>()     //date, <name, observedValue> - colHead, <rowHead, cellValue>\

    private var getCriticalCareChartEncounterRespValue: GetCriticalCareChartEncounterResp? = null

    private var careName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Utils(requireContext()).setCalendarLocale("en", requireContext())

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_critical_care_chart_child,
            container,
            false
        )
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(activity)

        viewModel = ViewModelProvider(this)[CriticalCareChartViewModel::class.java]

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID) ?: 0
        departmentUuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
//        encounterUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
//        encounterDoctorUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)

        trackCriticalCareChartVisit(utils?.getEncounterType())

        initViews()
        listeners()
        attachObservables()
        binding?.etCriticalCareChartFilterDateStart?.setText(getCurrentDate())
        binding?.etCriticalCareChartFilterDateEnd?.setText(getCurrentDate())
        return binding?.root!!
    }

    private fun initViews() {
        binding?.rvHeading?.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding?.rvHeading?.adapter = CriticalCareChartHeadingAdapter(
            list = dynamicHeadingList,
            clickListener = { heading ->
                select(heading)
            })

//        populateHeadings()
        getCriticalCareChartHeadings()

        binding?.rvTable?.layoutManager = LinearLayoutManager(context)
        binding?.rvTable?.adapter =
            CriticalCareChartTableAdapter(tableHeadingList, tableRowsList) { list ->
                fragment?.clearAll(true, list)
            }


        binding?.config?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val dialog =
                ConfigCCCFragment()
            dialog.show(ft, "Tag")
        }
    }

    private fun populateHeadings() {
//        headingList.add(CriticalCareType(AppConstants.CCC_ABG_TYPE, "Abg Chart"))
//        headingList.add(CriticalCareType(AppConstants.CCC_MONITOR_TYPE, "Monitor Chart"))
//        headingList.add(CriticalCareType(AppConstants.CCC_INTAKE_OUTPUT_TYPE, "Intake/Output"))
//        headingList.add(CriticalCareType(AppConstants.CCC_BP_TYPE, "BP Chart"))
//        headingList.add(CriticalCareType(AppConstants.CCC_DIABETICS_TYPE, "Diabetes Chart"))
//        headingList.add(CriticalCareType(AppConstants.CCC_DIALYSIS_TYPE, "Dialysis Chart"))
//        headingList.add(CriticalCareType(AppConstants.CCC_VITAL_CHART_TYPE, "Vital Chart"))
//        headingList.add(CriticalCareType(AppConstants.CCC_VENTILATOR_TYPE, "Ventilator Chart"))
//        binding?.rvHeading?.adapter?.notifyDataSetChanged()
//
//        select(headingList[0])
    }

    private fun select(heading: CriticalCareChartFilterHeading?) {
        heading?.let {
            if (criticalCareType == heading.cc_type_id) {
                return
            }

            unselectAll()

            mandatoryQuestionsMap.clear()
            saveObservedValueList.clear()

            criticalCareType = heading.cc_type_id ?: 0
            heading.isSelected = true
            loadFragment(null, "Add New ${heading.cc_type_name}")
            careName = heading.cc_type_name ?: ""
            getCriticalCaseChartMaster(heading.cc_type_name)
            getCriticalCareChartByPatientId(heading.cc_type_id)
            binding?.llTableHeading?.removeAllViews()
            binding?.rvHeading?.adapter?.notifyDataSetChanged()
        }
    }

    private fun unselectAll() {
        dynamicHeadingList.forEach { heading ->
            heading.isSelected = false
        }
        tableRowsList.clear()
        binding?.rvTable?.adapter?.notifyDataSetChanged()
    }

    private fun loadFragment(formData: ResponseContents?, heading: String) {
//        val bundle = Bundle()
//        bundle.putString("heading", heading)
//        formData?.let { bundle.putSerializable("form_data", it) }


        fragment = CriticalCareChartFormFragment(this, heading, formData)
//        fragment!!.arguments = bundle
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFormContainer, fragment!!)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun listeners() {
        binding?.etCriticalCareChartFilterDateStart?.setOnClickListener {
            displayCalendar(it as EditText)
        }

        binding?.etCriticalCareChartFilterDateEnd?.setOnClickListener {
            displayCalendar(it as EditText)
        }

        binding?.cvCompare?.setOnClickListener {
//            loadCompareFragment()
//            compare(true)
            val fromDate = binding?.etCriticalCareChartFilterDateStart?.text?.toString()
            val toDate = binding?.etCriticalCareChartFilterDateEnd?.text?.toString()
            if (fromDate?.isNotEmpty() == true && toDate?.isNotEmpty() == true) {


                getCriticalCareChartCompareData(
                    utils?.convertDateFormat(
                        fromDate,
                        "dd-MMM-yyyy HH:mm",
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                    ),
                    utils?.convertDateFormat(
                        toDate,
                        "dd-MMM-yyyy HH:mm",
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                    )
                )
            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.enter_dates)
                )
            }
        }

        binding?.cvNext?.setOnClickListener {
//            compare(false)
            selectNext()
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        return currentDateandTime
    }

    private fun attachObservables() {
        viewModel?.progress?.observe(requireActivity(), androidx.lifecycle.Observer { progress ->
            if (progress == View.VISIBLE) {
                customProgressDialog?.show()
            } else if (progress == View.GONE) {
                customProgressDialog?.dismiss()
            }
        })
    }

    private fun displayCalendar(editText: EditText) {
        val currentDate = Calendar.getInstance()
        val mYear = currentDate[Calendar.YEAR]
        val mMonth = currentDate[Calendar.MONTH]
        val mDay = currentDate[Calendar.DAY_OF_MONTH]
        val mHour = currentDate[Calendar.HOUR_OF_DAY]
        val mMinute = currentDate[Calendar.MINUTE]

        var op: String

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val d = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val m = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
                op = "$d-$m-$year"
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { view1, hourOfDay, minute ->
                        val hr = if (hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
                        val min = if (minute < 10) "0$minute" else "$minute"
                        op = "$op $hr:$min"

                        editText.setText(
                            utils?.displayDate(
                                op,
                                "dd-MM-yyyy HH:mm"
                            )
                        )
                    }, mHour, mMinute, false
                )
                timePickerDialog.show()
            }, mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

//    private fun loadCompareFragment() {
//        val fragment = CriticalCareChartCompareFragment()
//        val fragmentTransaction = childFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.flCompareContainer, fragment)
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()
//    }

    private fun compareDialog(
        compareData: LinkedHashMap<String, LinkedHashMap<String, Float>>?
    ) {
        val ft = childFragmentManager.beginTransaction()
        val surgeryDialog = CompareDialogFragment(compareData)
        surgeryDialog.show(ft, "Tag")
    }

    private fun selectNext() {
        var heading: CriticalCareChartFilterHeading? = null
        dynamicHeadingList.forEach { responseContent ->
            if (responseContent.isSelected) {
                heading = responseContent
            }
        }
        val nextUuid = heading?.cc_type_id ?: 0 + 1
        unselectAll()
        if (nextUuid <= dynamicHeadingList.size) {
            heading = getHeadingObjFromUuid(nextUuid)
        }
        select(heading)
    }

    private fun compare(isCompared: Boolean) {
        if (isCompared) {
            binding?.flCompareContainer?.visibility = View.VISIBLE
            binding?.hsvObservedData?.visibility = View.GONE
            binding?.tvNext?.text = "Back"
            binding?.tvNext?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_arrow_left,
                0,
                0,
                0
            )
        } else {
            binding?.flCompareContainer?.visibility = View.GONE
            binding?.hsvObservedData?.visibility = View.VISIBLE
            binding?.tvNext?.text = "Next"
            binding?.tvNext?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_arrow_right,
                0,
                0,
                0
            )
        }
    }

    fun create() {
        getCriticalCareChartEncounter()
    }

    private fun updateTable(type: Int) {
        binding?.llTableHeading?.removeAllViews()
        context?.let { context ->
            val typefacePoppinsBold = ResourcesCompat.getFont(context, R.font.poppins_bold)
            binding?.llTableHeading?.addView(drawSeparator(context))
            binding?.llTableHeading?.addView(drawCheckBox(context, "Select", typefacePoppinsBold!!))
            binding?.llTableHeading?.addView(drawSeparator(context))

            for (i in tableHeadingList.indices) {
                binding?.llTableHeading?.addView(
                    drawTextView(
                        context,
                        tableHeadingList[i].name ?: "",
                        typefacePoppinsBold!!
                    )
                )

                binding?.llTableHeading?.addView(drawSeparator(context))
            }

            binding?.llTableHeading?.addView(drawSeparator(context))
            binding?.llTableHeading?.addView(drawTextView(context, "Action", typefacePoppinsBold!!))

            binding?.rvTable?.adapter?.notifyDataSetChanged()
        } ?: utils?.showToast(
            R.color.negativeToast,
            binding?.mainLayout!!,
            getString(R.string.something_went_wrong)
        )
    }

    private fun drawCheckBox(context: Context, text: String, typeface: Typeface): CheckBox {
        val chk = CheckBox(context)
        chk.layoutParams =
            ViewGroup.LayoutParams(
                (utils?.convertDpToPixel(100f, context))?.toInt() ?: 100,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        chk.gravity = Gravity.CENTER
        chk.text = text
        chk.typeface = typeface
        return chk
    }

    private fun drawSeparator(context: Context): View {
        val viewDivider = View(context)
        viewDivider.layoutParams =
            ViewGroup.LayoutParams(
                (utils?.convertDpToPixel(2f, context))?.toInt() ?: 2,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        viewDivider.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.view
            )
        )
        return viewDivider
    }

    private fun drawTextView(context: Context, text: String, typeface: Typeface): TextView {
        val tv = TextView(context)
        tv.layoutParams = LinearLayout.LayoutParams(
            (utils?.convertDpToPixel(100f, context))?.toInt() ?: 100,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tv.gravity = Gravity.CENTER
        tv.text = text
        tv.typeface = typeface
        return tv
    }

    private fun formMap(
        data: com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.compare_data.ResponseContents?
    ): LinkedHashMap<String, LinkedHashMap<String, Float>> {
        val res = LinkedHashMap<String, LinkedHashMap<String, Float>>()

        data?.observed_values?.forEach { observedValue ->
            observedValue.dList?.forEach { d: D ->
                var row = res.get(observedValue.observed_date ?: "")
                if (row == null) {
                    row = LinkedHashMap<String, Float>()
                }
                row[d.ccc_name ?: ""] = try {
                    d.observed_value?.toFloat() ?: 0f
                } catch (e: Exception) {
                    0f
                }
                res[observedValue.observed_date ?: ""] = row

//                when {
//                    observedValue.ventilator_date != null -> {
//                        var row = res.get(observedValue.ventilator_date!!)
//                        if (row == null) {
//                            row = LinkedHashMap<String, Float>()
//                        }
//                        row[d.ccc_name ?: ""] = try {
//                            d.observed_value?.toFloat() ?: 0f
//                        } catch (e: Exception) {
//                            0f
//                        }
//                        res[observedValue.ventilator_date!!] = row
//                    }
//                    observedValue.abg_date != null -> {
//                        var row = res.get(observedValue.abg_date!!)
//                        if (row == null) {
//                            row = LinkedHashMap<String, Float>()
//                        }
//                        row[d.ccc_name ?: ""] = try {
//                            d.observed_value?.toFloat() ?: 0f
//                        } catch (e: Exception) {
//                            0f
//                        }
//                        res[observedValue.abg_date!!] = row
//                    }
//                    observedValue.monitor_date != null -> {
//                        var row = res.get(observedValue.monitor_date!!)
//                        if (row == null) {
//                            row = LinkedHashMap<String, Float>()
//                        }
//                        row[d.ccc_name ?: ""] = try {
//                            d.observed_value?.toFloat() ?: 0f
//                        } catch (e: Exception) {
//                            0f
//                        }
//                        res[observedValue.monitor_date!!] = row
//                    }
//                    observedValue.intake_date != null -> {
//                        var row = res.get(observedValue.intake_date!!)
//                        if (row == null) {
//                            row = LinkedHashMap<String, Float>()
//                        }
//                        row[d.ccc_name ?: ""] = try {
//                            d.observed_value?.toFloat() ?: 0f
//                        } catch (e: Exception) {
//                            0f
//                        }
//                        res[observedValue.intake_date!!] = row
//                    }
//                    observedValue.bp_date != null -> {
//                        var row = res.get(observedValue.bp_date!!)
//                        if (row == null) {
//                            row = LinkedHashMap<String, Float>()
//                        }
//                        row[d.ccc_name ?: ""] = try {
//                            d.observed_value?.toFloat() ?: 0f
//                        } catch (e: Exception) {
//                            0f
//                        }
//                        res[observedValue.bp_date!!] = row
//                    }
//                    observedValue.diabetes_date != null -> {
//                        var row = res.get(observedValue.diabetes_date!!)
//                        if (row == null) {
//                            row = LinkedHashMap<String, Float>()
//                        }
//                        row[d.ccc_name ?: ""] = try {
//                            d.observed_value?.toFloat() ?: 0f
//                        } catch (e: Exception) {
//                            0f
//                        }
//                        res[observedValue.diabetes_date!!] = row
//                    }
//                    observedValue.dialysis_date != null -> {
//                        var row = res.get(observedValue.dialysis_date!!)
//                        if (row == null) {
//                            row = LinkedHashMap<String, Float>()
//                        }
//                        row[d.ccc_name ?: ""] = try {
//                            d.observed_value?.toFloat() ?: 0f
//                        } catch (e: Exception) {
//                            0f
//                        }
//                        res[observedValue.dialysis_date!!] = row
//                    }
//                }
            }
        }

        return res
    }

    private fun getHeadingObjFromUuid(uuid: Int): CriticalCareChartFilterHeading {
        dynamicHeadingList.forEach { responseContent: CriticalCareChartFilterHeading ->
            if (responseContent.cc_type_id == uuid) {
                return responseContent
            }
        }
        return dynamicHeadingList[0]
    }

    private fun getCriticalCaseChartMaster(type: String?) {
        val body = GetCriticalCareChartMasterReq(type)
        viewModel?.getCriticalCareChartMaster(
            facilityId!!,
            body,
            getCriticalCareChartMasterRespCallback
        )
    }

/*
    private fun getCriticalCareChartHeadings() {
        val body = GetCriticalCareChartHeadingsReq("critical_care_types")
        viewModel?.getCriticalCareChartHeadings(
            facilityId!!,
            body,
            getCriticalCareChartHeadingsRespCallback
        )
    }
*/


    private fun getCriticalCareChartHeadings() {
        viewModel?.getCriticalCareChartFilterHeadings(
            facilityId!!,
            getCriticalCareChartFilterHeadingsCallback
        )
    }


    private fun getCriticalCareChartByPatientId(criticalCareType: Int?) {
        viewModel?.getCriticalCareChartByPatientId(
            facilityId!!,
            patientUuid,
            criticalCareType ?: 0,
            getCriticalCareChartByPatientIdResp
        )
    }

    private fun getCriticalCareChartEncounter() {
        viewModel?.getCriticalCareChartEncounter(
            facilityId!!,
            patientUuid,
            encounterType!!,
            getCriticalCareChartEncounterRespCallback
        )
    }

    private fun postCriticalCareChartCreate(getCriticalCareChartEncounterResp: GetCriticalCareChartEncounterResp?) {
        val fromDate = utils?.getCurrentDateTime("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        saveObservedValueList.forEach { observedData: ObservedData ->
            observedData.from_date = fromDate
        }
        val body =
            if ((getCriticalCareChartEncounterResp?.responseContents?.size ?: 0) > 0) {
                PostCriticalCareChartCreateReq(
                    headers = Headers(
                        "",
                        criticalCareType,
                        getCriticalCareChartEncounterResp?.responseContents?.get(0)?.encounter_type_uuid,
                        encounter_uuid,
                        facilityId?.toString(),
                        patientUuid.toString()
                    ),
                    observed_data = saveObservedValueList
                )
            } else {
                PostCriticalCareChartCreateReq(
                    headers = Headers(
                        "",
                        criticalCareType,
                        encounterType,
                        encounter_uuid,
                        facilityId?.toString(),
                        patientUuid.toString()
                    ),
                    observed_data = saveObservedValueList
                )
            }

        viewModel?.postCriticalCareChartCreate(
            facilityId!!,
            body,
            postCriticalCareChartCreateRespCallback
        )
    }

    private fun postCriticalCareChartUpdate(getCriticalCareChartEncounterResp: GetCriticalCareChartEncounterResp?) {
        val body = PostCriticalCareChartUpdateReq(
            headers = HeadersX(
                "",
                criticalCareType,
                encounterType,
                encounter_uuid,
                facilityId?.toString(),
                patientUuid.toString()
            ),
            observed_data = updateObservedValueList
        )
        viewModel?.postCriticalCareChartUpdate(
            facilityId!!,
            body,
            postCriticalCareChartUpdateRespCallback
        )
    }

    private fun getCriticalCareChartCompareData(fromDate: String?, toDate: String?) {
        viewModel?.getCriticalCareChartCompareData(
            facilityId!!,
            patientUuid,
            criticalCareType,
            fromDate ?: "",
            toDate ?: "",
            getCriticalCareChartCompareDataRespCallback
        )
    }

    fun observedDataForCreateReqPayload(
        criticalCareChart: CriticalCareChart,
        answer: Any,
        toRemove: Boolean
    ) {
        saveObservedValueList.forEach { data: ObservedData ->
            if (data.cc_chart_uuid == criticalCareChart.uuid) {
                val observedData =
                    saveObservedValueList[saveObservedValueList.indexOf(data)]

                observedData.observed_value = answer

                saveObservedValueList.remove(data)
                if (!toRemove)
                    saveObservedValueList.add(observedData)

                return
            }
        }

        val observedData = ObservedData(
            cc_chart_uuid = criticalCareChart.uuid,
            cc_concept_uuid = criticalCareChart.critical_care_concepts?.cc_chart_uuid,
            cc_concept_value_uuid = criticalCareChart.critical_care_concepts?.value_type_uuid,
            from_date = "",
            observed_value = answer,
            to_date = ""
        )

        saveObservedValueList.add(observedData)
    }

    fun observedDataForUpdateReqPayload(
        criticalCareChart: CriticalCareChart,
        answer: Any,
        toRemove: Boolean
    ) {
        updateObservedValueList.forEach { data ->
            if (data.cc_chart_uuid == criticalCareChart.uuid) {
                val observedData =
                    updateObservedValueList[updateObservedValueList.indexOf(data)]

                observedData.observed_value = answer

                updateObservedValueList.remove(data)
                if (!toRemove)
                    updateObservedValueList.add(observedData)

                return
            }
        }

        val observedData = ObservedDataX(
            cc_chart_uuid = criticalCareChart.uuid,
            cc_concept_uuid = criticalCareChart.critical_care_concepts?.cc_chart_uuid,
            cc_concept_value_uuid = criticalCareChart.critical_care_concepts?.value_type_uuid,
            observed_value = answer,
            to_date = "",
            uuid = ""
        )

        updateObservedValueList.add(observedData)
    }

    fun observedDataForCreateReqCheckBoxPayload(
        criticalCareChart: CriticalCareChart,
        postion: Int,
        answer: Any,
        toRemove: Boolean
    ) {
        saveObservedValueList.forEach { data: ObservedData ->

            if (data.cc_concept_value_uuid == criticalCareChart.critical_care_concepts!!.critical_care_concept_values?.get(
                    postion
                )?.uuid
            ) {
                val observedData =
                    saveObservedValueList[saveObservedValueList.indexOf(data)]

                observedData.observed_value = answer

                saveObservedValueList.remove(data)
                if (!toRemove)
                    saveObservedValueList.add(observedData)

                return
            }

        }

        val observedData = ObservedData(
            cc_chart_uuid = criticalCareChart.uuid,
            cc_concept_uuid = criticalCareChart.critical_care_concepts?.cc_chart_uuid,
            cc_concept_value_uuid = criticalCareChart.critical_care_concepts!!.critical_care_concept_values?.get(
                postion
            )?.uuid,
            from_date = "",
            observed_value = answer,
            to_date = ""
        )

        saveObservedValueList.add(observedData)
    }

    fun observedDataForUpdateReqCheckBoxPayload(
        criticalCareChart: CriticalCareChart,
        postion: Int,
        answer: Any,
        toRemove: Boolean
    ) {
        updateObservedValueList.forEach { data ->
            if (data.cc_chart_uuid == criticalCareChart.critical_care_concepts!!.critical_care_concept_values?.get(
                    postion
                )?.uuid
            ) {
                val observedData =
                    updateObservedValueList[updateObservedValueList.indexOf(data)]

                observedData.observed_value = answer

                updateObservedValueList.remove(data)
                if (!toRemove)
                    updateObservedValueList.add(observedData)

                return
            }
        }

        val observedData = ObservedDataX(
            cc_chart_uuid = criticalCareChart.uuid,
            cc_concept_uuid = criticalCareChart.critical_care_concepts?.cc_chart_uuid,
            cc_concept_value_uuid = criticalCareChart.critical_care_concepts!!.critical_care_concept_values?.get(
                postion
            )?.uuid,
            observed_value = answer,
            to_date = "",
            uuid = ""
        )

        updateObservedValueList.add(observedData)
    }

    private val getCriticalCareChartMasterRespCallback =
        object : RetrofitCallback<GetCriticalCareChartMasterResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetCriticalCareChartMasterResp>?) {
                responseBody?.body()?.let { getCriticalCareChartMasterResp ->
                    getCriticalCareChartMasterResp.responseContents?.let { responseContents ->
                        responseContents.critical_care_charts?.let { list ->
                            tableHeadingList.clear()
                            tableHeadingList.addAll(list)
                            loadFragment(responseContents, "Add New ${responseContents.name}")
                        }
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetCriticalCareChartMasterResp>?) {
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
                viewModel!!.progress.value = 8
            }
        }

    private val getCriticalCareChartByPatientIdResp =
        object : RetrofitCallback<GetCriticalCareChartByPatientIdResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetCriticalCareChartByPatientIdResp>?) {
                responseBody?.body()?.let { getCriticalCareChartByPatientIdResp ->
                    getCriticalCareChartByPatientIdResp.responseContents?.let { responseContentsX ->
                        val type = when {
                            responseContentsX.ventilator_details != null -> AppConstants.CCC_VENTILATOR_TYPE
                            responseContentsX.abg_details != null -> AppConstants.CCC_ABG_TYPE
                            responseContentsX.monitor_details != null -> AppConstants.CCC_MONITOR_TYPE
                            responseContentsX.intake_details != null -> AppConstants.CCC_INTAKE_OUTPUT_TYPE
                            responseContentsX.bp_details != null -> AppConstants.CCC_BP_TYPE
                            responseContentsX.diabetes_details != null -> AppConstants.CCC_DIABETICS_TYPE
                            responseContentsX.dialysis_details != null -> AppConstants.CCC_DIALYSIS_TYPE
                            else -> 0
                        }
                        responseContentsX.observed_values?.let { list ->
                            tableRowsList.clear()
                            tableRowsList.addAll(list)
                            updateTable(type)
                        }
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetCriticalCareChartByPatientIdResp>?) {
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
                viewModel!!.progress.value = 8
            }
        }

    private val getCriticalCareChartEncounterRespCallback =
        object : RetrofitCallback<GetCriticalCareChartEncounterResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetCriticalCareChartEncounterResp>?) {
                responseBody?.body()?.let { getCriticalCareChartEncounterResp ->
                    getCriticalCareChartEncounterRespValue = getCriticalCareChartEncounterResp
                    if (getCriticalCareChartEncounterResp.responseContents?.isNotEmpty() == true) {
                        getCriticalCareChartEncounterResp.responseContents?.get(0)
                            .let { responseContent ->
                                doctor_en_uuid =
                                    responseContent?.encounter_doctors?.get(0)?.uuid ?: 0
                                encounter_uuid = responseContent?.uuid ?: 0
                                patientUuid = responseContent?.patient_uuid ?: 0
                                appPreferences?.saveInt(
                                    AppConstants.ENCOUNTER_DOCTOR_UUID,
                                    doctor_en_uuid
                                )
                                appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid)

                                if (isUpdate)
                                    postCriticalCareChartUpdate(getCriticalCareChartEncounterResp)
                                else
                                    postCriticalCareChartCreate(getCriticalCareChartEncounterResp)
                            }
                    } else {
                        viewModel?.createEncounter(
                            patientUuid,
                            encounterType!!,
                            createEncounterRetrofitCallback
                        )
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetCriticalCareChartEncounterResp>?) {
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
                viewModel!!.progress.value = 8
            }
        }

    private val createEncounterRetrofitCallback =
        object : RetrofitCallback<CreateEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {


                doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
                encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
                patientUuid =
                    response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

                if (isUpdate)
                    postCriticalCareChartUpdate(getCriticalCareChartEncounterRespValue)
                else
                    postCriticalCareChartCreate(getCriticalCareChartEncounterRespValue)
            }

            override fun onBadRequest(response: Response<CreateEncounterResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: CreateEncounterResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        CreateEncounterResponseModel::class.java
                    )
                    /*   utils?.showToast(
                           R.color.negativeToast,
                           binding?.mainLayout!!,
                           responseModel.message!!
                       )*/
                } catch (e: Exception) {
                    /*   utils?.showToast(
                           R.color.negativeToast,
                           binding?.mainLayout!!,
                           getString(R.string.something_went_wrong)
                       )*/
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                /* utils?.showToast(
                     R.color.negativeToast,
                     binding?.mainLayout!!,
                     getString(R.string.something_went_wrong)
                 )*/
            }

            override fun onUnAuthorized() {
                /*    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.unauthorized)
                    )*/
            }

            override fun onForbidden() {
                /*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       getString(R.string.something_went_wrong)
                   )*/
            }

            override fun onFailure(failure: String) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    private val postCriticalCareChartCreateRespCallback =
        object : RetrofitCallback<PostCriticalCareChartCreateResp> {
            override fun onSuccessfulResponse(responseBody: Response<PostCriticalCareChartCreateResp>?) {
                responseBody?.body()?.let { postCriticalCareChartCreateResp ->
//                    val careId = when {
//                        postCriticalCareChartCreateResp.data1 != null ->
//                            postCriticalCareChartCreateResp.data1?.get(0)?.critical_care_type
//                        postCriticalCareChartCreateResp.data2 != null ->
//                            postCriticalCareChartCreateResp.data2?.get(0)?.critical_care_type
//                        postCriticalCareChartCreateResp.data3 != null ->
//                            postCriticalCareChartCreateResp.data3?.get(0)?.critical_care_type
//                        postCriticalCareChartCreateResp.data4 != null ->
//                            postCriticalCareChartCreateResp.data4?.get(0)?.critical_care_type
//                        postCriticalCareChartCreateResp.data5 != null ->
//                            postCriticalCareChartCreateResp.data5?.get(0)?.critical_care_type
//                        postCriticalCareChartCreateResp.data6 != null ->
//                            postCriticalCareChartCreateResp.data6?.get(0)?.critical_care_type
//                        postCriticalCareChartCreateResp.data7 != null ->
//                            postCriticalCareChartCreateResp.data7?.get(0)?.critical_care_type
//                        else -> 0
//                    }
//                    var careName: String? = null
//                    headingList.forEach { criticalCareType: CriticalCareType ->
//                        if (criticalCareType.careId == careId)
//                            careName = criticalCareType.careName
//                    }

                    utils?.showToast(
                        R.color.positiveToast,
                        binding?.mainLayout!!,
                        getString(R.string.data_save)
                    )
                    getCriticalCaseChartMaster(careName)
                    getCriticalCareChartByPatientId(criticalCareType)
                }
            }

            override fun onBadRequest(errorBody: Response<PostCriticalCareChartCreateResp>?) {
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
                viewModel!!.progress.value = 8
            }
        }

    private val postCriticalCareChartUpdateRespCallback =
        object : RetrofitCallback<PostCriticalCareChartUpdateResp> {
            override fun onSuccessfulResponse(responseBody: Response<PostCriticalCareChartUpdateResp>?) {
                responseBody?.body()?.let { postCriticalCareChartUpdateResp ->
                    var careName: String? = null
                    dynamicHeadingList.forEach { ccType ->
                        if (ccType.cc_type_id == criticalCareType) {
                            careName = ccType.cc_type_name
                        }
                    }

                    utils?.showToast(
                        R.color.positiveToast,
                        binding?.mainLayout!!,
                        getString(R.string.data_save)
                    )
                    getCriticalCaseChartMaster(careName)
                    getCriticalCareChartByPatientId(criticalCareType)
                }
            }

            override fun onBadRequest(errorBody: Response<PostCriticalCareChartUpdateResp>?) {
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
                viewModel!!.progress.value = 8
            }
        }

    private val getCriticalCareChartCompareDataRespCallback =
        object : RetrofitCallback<GetCriticalCareChartCompareDataResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetCriticalCareChartCompareDataResp>?) {
                if (responseBody?.isSuccessful == true) {
                    //sri form map and pass to dialog
                    compareDialog(formMap(responseBody.body()?.responseContents))
                }
            }

            override fun onBadRequest(errorBody: Response<GetCriticalCareChartCompareDataResp>?) {
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
                viewModel!!.progress.value = 8
            }
        }

    /*   private val getCriticalCareChartHeadingsRespCallback =
           object : RetrofitCallback<GetCriticalCareChartHeadingsResp> {
               override fun onSuccessfulResponse(responseBody: Response<GetCriticalCareChartHeadingsResp>?) {
                   responseBody?.body()?.let { getCriticalCareChartHeadingsResp ->
                       getCriticalCareChartHeadingsResp.responseContents?.let { list ->

                           dynamicHeadingList.clear()
                           dynamicHeadingList.addAll(list)
                           binding?.rvHeading?.adapter?.notifyDataSetChanged()

                           select(getHeadingObjFromUuid(1))

                       }
                   }
               }

               override fun onBadRequest(errorBody: Response<GetCriticalCareChartHeadingsResp>?) {
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
                   viewModel!!.progress.value = 8
               }
           }
   */
    private fun trackCriticalCareChartVisit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackCriticalCareChartVisit(type)
    }


    private val getCriticalCareChartFilterHeadingsCallback =
        object : RetrofitCallback<CriticalCareChartFilterHeadingsResponse> {
            override fun onSuccessfulResponse(responseBody: Response<CriticalCareChartFilterHeadingsResponse>?) {
                responseBody?.body()?.let { getCriticalCareChartHeadingsResp ->
                    getCriticalCareChartHeadingsResp.responseContents?.let { list ->


                        dynamicHeadingList.clear()


                        dynamicHeadingList.addAll(list)
                        binding?.rvHeading?.adapter?.notifyDataSetChanged()

                        select(getHeadingObjFromUuid(1))

                    }
                }
            }

            override fun onBadRequest(errorBody: Response<CriticalCareChartFilterHeadingsResponse>?) {
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
                viewModel!!.progress.value = 8
            }
        }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is ConfigCCCFragment) {
            childFragment.setOnSaveRefreshListener(this)
        }
    }

    override fun onRefreshList() {
        getCriticalCareChartHeadings()
    }
}
