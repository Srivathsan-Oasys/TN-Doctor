package com.hmis_tn.doctor.ui.quick_reg.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.FileHelperReports
import com.hmis_tn.doctor.MultiSelectionSpinner
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.callbacks.PaginationScrollListener
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppConstants.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.ActivityConsolidatedReportBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.quick_reg.model.reports.requset.LabConsolidatedReportRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.reports.response.*
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabConsolidatedReportViewModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabConsolidatedReportViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class LabConsolidatedReportsActivity : Fragment(),
    ConsolidatedReportResultDialogFragment.OnLabConsolidatedReportActivityRefreshListener,
    RejectDialogFragment.OnLabTestRefreshListener {

    var binding: ActivityConsolidatedReportBinding? = null
    var utils: Utils? = null
    private var selectedDistrictid = mutableListOf<Int>()
    private var selectedHUDid = mutableListOf<Int>()
    private var selectedBlockid = mutableListOf<Int>()
    private var selectedOfficeid = mutableListOf<Int>()
    private var selectedDepartmentid = mutableListOf<Int>()
    private var selectedInstitutionTypeid = mutableListOf<Int>()
    private var selectedInstitutionid = mutableListOf<Int>()
    private var selectedTestNameid = mutableListOf<Int>()
    private var selectedLabNameid = mutableListOf<Int>()
    private var selectedAdultid = mutableListOf<Int>()
    private var selectedGenderid = mutableListOf<Int>()
    private var selectedStatusid = mutableListOf<Int>()

    private var districtList: ArrayList<LabFilterResponseContent?>? = ArrayList()
    private var hudList: ArrayList<LabFilterResponseContent?>? = ArrayList()
    private var blockList: ArrayList<LabFilterResponseContent?>? = ArrayList()
    private var institutionlist: ArrayList<LabFilterResponseContent?>? = ArrayList()
    private var officeList: ArrayList<LabFilterResponseContent?>? = ArrayList()
    private var departmentList: ArrayList<LabFilterResponseContent?>? = ArrayList()
    private var institutionTypeList: ArrayList<LabFilterResponseContent?>? = ArrayList()
    private var testNameList: ArrayList<LabFilterResponseContent?>? = ArrayList()
    private var labNameList: ArrayList<LabFilterResponseContent?>? = ArrayList()
    private var statusList: ArrayList<LabFilterResponseContent?>? = ArrayList()

    private var viewModel: LabConsolidatedReportViewModel? = null
    var appPreferences: AppPreferences? = null
    private var mAdapter: LabConsolidatedReportAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var listfilteritemAssignSpinner: ArrayList<LabFilterResponseContent?>? = ArrayList()

    private var FilterAdultOrChildResponseMap = mutableMapOf<Int, String>()
    private var FilterGenderResponseMap = mutableMapOf<Int, String>()

    /////Pagination
    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0

    private var fromYear: Int? = null
    private var fromMonth: Int? = null
    private var fromDay: Int? = null
    private var toYear: Int? = null
    private var toMonth: Int? = null
    private var toDay: Int? = null
    private var fromDate: String = ""
    private var toDate: String = ""

    var bitmap: Bitmap? = null
    var cal = Calendar.getInstance()
    private var initialLoad: Int = 0

    var listData: List<LabConsolidatedReportResponseContent?>? = listOf()
    var downloadFileFormat: String = ".pdf"

    private var toggle: String = "OP"
    private var toDownload: Boolean = false

    private var facility_id: Int = 0

    //private var customProgressDialog: CustomProgressDialog? = null
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Utils(requireContext()).setCalendarLocale("en", requireContext())
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_consolidated_report,
                container,
                false
            )

        viewModel = LabConsolidatedReportViewModelFactory(
            requireActivity().application
        )
            .create(LabConsolidatedReportViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())
        binding?.searchDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }
        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.consolidateReportRecycleview!!.layoutManager = linearLayoutManager
        mAdapter = LabConsolidatedReportAdapter(requireContext(), ArrayList())
        binding?.consolidateReportRecycleview!!.adapter = mAdapter
        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!

        viewModel?.getDistrict(toggle, labDistrictSpinnerRetrofitCallback)

        /////////////Pagination scrollview


        binding?.consolidateReportRecycleview?.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {

            override fun loadMoreItems() {
                println("bindingdsafgsethry = ${binding}")
                currentPage += 1
                if (currentPage <= TOTAL_PAGES) {

                    labListSecondAPI(pageSize, currentPage)

                }
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun isLastPageReached(): Boolean {
                return isLastPage
            }

            override fun isLoadingHappening(): Boolean {
                println("isLoadingdefwregwtrhey = ${isLoading}")
                return isLoading
            }
        })

        binding?.toggle?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.toggleOP) {
                toggle = "OP"
            } else {
                toggle = "IP"
            }

            Log.e("OnCheckedChange", toggle)
            viewModel?.getDistrict(toggle, labDistrictSpinnerRetrofitCallback)
            labConsolidatedReportListAPI(pageSize, currentPage, 0)
        }

        binding?.adultOrChildSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedAdultid.clear()
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (pos == 0) {
                        selectedAdultid.clear()
                        FilterGenderResponseMap.put(3, "Transgender")
                    } else {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()
                        selectedAdultid.clear()
                        selectedAdultid.add(FilterAdultOrChildResponseMap.filterValues { it == itemValue }.keys.toList()[0])
                        Log.e("selected adult Id", selectedAdultid.toString())
                        if (FilterAdultOrChildResponseMap.filterValues { it == itemValue }.keys.toList()[0] == 0) {
                            FilterGenderResponseMap.remove(3)
                        } else {
                            FilterGenderResponseMap.put(3, "Transgender")
                        }
                    }
                    try {
                        val adapter =
                            ArrayAdapter<String>(
                                context!!,
                                android.R.layout.simple_spinner_item,
                                FilterGenderResponseMap.values.toMutableList()
                            )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding?.genderSpinner!!.adapter = adapter
                    } catch (e: Exception) {
                    }
                }
            }

        binding?.genderSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedGenderid.clear()
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (pos == 0) {
                        selectedGenderid.clear()
                    } else {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()
                        selectedGenderid.clear()
                        selectedGenderid.add(FilterGenderResponseMap.filterValues { it == itemValue }.keys.toList()[0])
                        Log.e("selected gender Id", selectedGenderid.toString())
                    }
                }
            }

        binding?.edtFromDate!!.setOnClickListener {

            val cal = Calendar.getInstance()

            if (toYear != null) {

                cal.set(Calendar.YEAR, toYear!!)

            }
            if (toMonth != null) {

                cal.set(Calendar.MONTH, toMonth!!)
            }

            if (toDay != null) {
                cal.set(Calendar.DAY_OF_MONTH, toDay!!)
            }


            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->


                    var from_Date = String.format(
                        "%02d",
                        dayOfMonth
                    ) + "-" + String.format("%02d", month + 1) + "-" + year

                    fromDate = year.toString() + "-" + String.format(
                        "%02d",
                        month + 1
                    ) + "-" + String.format(
                        "%02d",
                        dayOfMonth
                    )


                    fromYear = year

                    fromMonth = month

                    fromDay = dayOfMonth

                    binding?.edtFromDate!!.setText(from_Date)

                }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]
            )

            if ((toYear != null && toMonth != null) && toDay != null) {

                datePickerDialog.datePicker.maxDate = cal.timeInMillis
            }
//            datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

            datePickerDialog.show()
        }

        binding?.edtToDate!!.setOnClickListener {

            val cal = Calendar.getInstance()

            if (fromYear != null) {

                cal.set(Calendar.YEAR, fromYear!!)

            }
            if (fromMonth != null) {

                cal.set(Calendar.MONTH, fromMonth!!)
            }

            if (fromDay != null) {
                cal.set(Calendar.DAY_OF_MONTH, fromDay!!)
            }

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val date = "$dayOfMonth - ${month + 1} - $year"


                    var to_Date = String.format(
                        "%02d",
                        dayOfMonth
                    ) + "-" + String.format("%02d", month + 1) + "-" + year

                    toDate = year.toString() + "-" + String.format(
                        "%02d",
                        month + 1
                    ) + "-" + String.format(
                        "%02d",
                        dayOfMonth
                    )

                    toYear = year

                    toMonth = month

                    toDay = dayOfMonth

                    binding?.edtToDate!!.setText(to_Date)

                }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]
            )

            if ((fromYear != null && fromMonth != null) && fromDay != null) {

                datePickerDialog.datePicker.minDate = cal.timeInMillis
            }
            datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

            datePickerDialog.show()

        }


        val sdf1 = SimpleDateFormat("yyyy-MM-dd")

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        binding?.edtFromDate!!.setText("${formatter.format(Date())}")
        binding?.edtToDate!!.setText("${formatter.format(Date())}")

        fromDate = sdf1.format(Date())
        toDate = sdf1.format(Date())

        binding?.searchButton!!.setOnClickListener {

            binding?.drawerLayout!!.closeDrawer(GravityCompat.END)
            mAdapter!!.clearAll()
            pageSize = 10
            currentPage = 0
            getSelectedItems(1)


        }

        binding?.clear!!.setOnClickListener {

            clearSearch()

        }
        binding?.pdfDownload!!.setOnClickListener {
            toDownload = true
            if (toDownload) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    runTimePermissionWriteExternalStorage()
                } else {
                    downloadPdf()
                }
            }
        }

        labConsolidatedReportListAPI(pageSize, currentPage, 0)


        return binding!!.root
    }

    private fun runTimePermissionWriteExternalStorage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
            return
        } else {
            downloadPdf()
            return
        }
    }

    private fun downloadPdf() {
        viewModel?.progress?.value = 0
        binding?.startDateLable?.text = "From Date : "
        binding?.endDateLable?.text = "To Date : "
        val folderName = "${FileHelperReports.PARENT_FOLDER}/Reports"
        val simpleDateFormat = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss")
        val date = simpleDateFormat.format(Calendar.getInstance().time)

        val fileHelper = FileHelperReports(context)
        val bitmap = fileHelper.getScreenshotFromRecyclerView(binding?.consolidateReportRecycleview)
        bitmap?.let {
            fileHelper.saveImageToPDF(
                title = binding?.listLayout!!,
                bitmap = bitmap,
                folder = File(
                    Environment.getExternalStorageDirectory().toString() + "/" + folderName
                ),
                filename = "ConsolidatedReport$date"
            )
        }
        binding?.startDateLable?.text = "Start Date : "
        binding?.endDateLable?.text = "End Date : "
        toDownload = false
        viewModel?.progress?.value = 8
    }

    private fun labListSecondAPI(pageSize: Int, currentPage: Int) {

        if (initialLoad == 0) {
            selectedInstitutionid.add(appPreferences?.getInt(AppConstants.FACILITY_UUID)!!)
        }

        val labConsolidatedReportRequestModel = LabConsolidatedReportRequestModel()
        labConsolidatedReportRequestModel.district_Id = selectedDistrictid
        labConsolidatedReportRequestModel.hud_Id = selectedHUDid
        labConsolidatedReportRequestModel.block_Id = selectedBlockid
        labConsolidatedReportRequestModel.office_Id = selectedOfficeid
        labConsolidatedReportRequestModel.institutiontype_Id = selectedInstitutionTypeid
        labConsolidatedReportRequestModel.institution_Id = selectedInstitutionid
        labConsolidatedReportRequestModel.gender_Id = selectedGenderid
        labConsolidatedReportRequestModel.orderstatus_Id = selectedStatusid
        labConsolidatedReportRequestModel.is_adult = selectedAdultid
        labConsolidatedReportRequestModel.testname_Id = selectedLabNameid
        labConsolidatedReportRequestModel.test_Id = selectedTestNameid
        labConsolidatedReportRequestModel.startdate = fromDate + " 00:00:00"
        val sdf = SimpleDateFormat("HH:mm:ss")
        labConsolidatedReportRequestModel.enddate = toDate + " " + sdf.format(Date())
        labConsolidatedReportRequestModel.pageNo = currentPage
        labConsolidatedReportRequestModel.paginationSize = pageSize

        val requestModel = Gson().toJson(labConsolidatedReportRequestModel)

        Log.e("ConsolidatedReport 2::", "Request ::" + requestModel)

        viewModel?.getLabConsolidatedReportListSecond(
            toggle,
            labConsolidatedReportRequestModel,
            consolidatedReportTableResponseSecondRetrofitCallback
        )


    }

    private fun getSelectedItems(search: Int) {
        val array_district = binding?.etDistrictspinner?.selectedITem
        selectedDistrictid.clear()
        for (i in districtList!!.indices) {
            val check = array_district!!.any { it!! == districtList?.get(i)?.districtname }
            if (check) {
                selectedDistrictid.add(districtList?.get(i)?.districtid!!)
            }
        }

        val array_hud = binding?.etHudspinner?.selectedITem
        selectedHUDid.clear()
        for (i in hudList!!.indices) {
            val check = array_hud!!.any { it!! == hudList?.get(i)?.hudname }
            if (check) {
                selectedHUDid.add(hudList?.get(i)?.hudid!!)
            }
        }

        val array_block = binding?.etBlockspinner?.selectedITem
        selectedBlockid.clear()
        for (i in blockList!!.indices) {
            val check = array_block!!.any { it!! == blockList?.get(i)?.blockname }
            if (check) {
                selectedBlockid.add(blockList?.get(i)?.blockid!!)
            }
        }


        val array_office = binding?.etOfficespinner?.selectedITem
        selectedOfficeid.clear()
        for (i in officeList!!.indices) {
            val check = array_office!!.any { it!! == officeList?.get(i)?.officename }
            if (check) {
                selectedOfficeid.add(officeList?.get(i)?.officeid!!)
            }
        }

        val array_department = binding?.etDepartmentpinner?.selectedITem
        selectedDepartmentid.clear()
        for (i in departmentList!!.indices) {
            val check = array_department!!.any { it!! == departmentList?.get(i)?.departmentname }
            if (check) {
                selectedDepartmentid.add(departmentList?.get(i)?.departmentid!!)
            }
        }

        val array_institutionType = binding?.etInstitutionTypespinner?.selectedITem
        selectedInstitutionTypeid.clear()
        for (i in institutionTypeList!!.indices) {
            val check =
                array_institutionType!!.any { it!! == institutionTypeList?.get(i)?.facilitytypename }
            if (check) {
                selectedInstitutionTypeid.add(institutionTypeList?.get(i)?.facilitytypeid!!)
            }
        }


        val array_institution = binding?.etInstitutionspinner?.selectedITem
        if (array_institution?.size == 0) {
            setInstitution()
        } else {
            selectedInstitutionid.clear()
        }

        for (i in institutionlist!!.indices) {
            val check = array_institution!!.any { it!! == institutionlist?.get(i)?.facilityname }
            if (check) {
                selectedInstitutionid.add(institutionlist?.get(i)?.facilityid!!)
            }
        }

        val array_testName = binding?.etTestNamespinner?.selectedITem
        selectedTestNameid.clear()
        for (i in testNameList!!.indices) {
            val check = array_testName!!.any { it!! == testNameList?.get(i)?.testname }
            if (check) {
                selectedTestNameid.add(testNameList?.get(i)?.testid!!)
            }
        }

        val array_labName = binding?.etLabNamespinner?.selectedITem
        selectedLabNameid.clear()
        for (i in labNameList!!.indices) {
            val check = array_labName!!.any { it!! == labNameList?.get(i)?.testtolocationname }
            if (check) {
                selectedLabNameid.add(labNameList?.get(i)?.testtolocationid!!)
            }
        }

        val array_session = binding?.etStatusspinner?.selectedITem
        selectedStatusid.clear()
        for (i in statusList!!.indices) {
            val check = array_session!!.any { it!! == statusList?.get(i)?.orderstatuscode }
            if (check) {
                selectedStatusid.add(statusList?.get(i)?.orderstatusid!!)
            }
        }

        labConsolidatedReportListAPI(pageSize, currentPage, search)

    }


    private fun labConsolidatedReportListAPI(pageSize: Int, currentPage: Int, search: Int) {

        if (initialLoad == 0) {
            selectedInstitutionid.add(appPreferences?.getInt(AppConstants.FACILITY_UUID)!!)
        }

        val labConsolidatedReportRequestModel = LabConsolidatedReportRequestModel()
        labConsolidatedReportRequestModel.district_Id = selectedDistrictid
        labConsolidatedReportRequestModel.hud_Id = selectedHUDid
        labConsolidatedReportRequestModel.block_Id = selectedBlockid
        labConsolidatedReportRequestModel.office_Id = selectedOfficeid
        labConsolidatedReportRequestModel.department_Id = selectedDepartmentid
        labConsolidatedReportRequestModel.institutiontype_Id = selectedInstitutionTypeid
        labConsolidatedReportRequestModel.institution_Id = selectedInstitutionid
        labConsolidatedReportRequestModel.gender_Id = selectedGenderid
        labConsolidatedReportRequestModel.orderstatus_Id = selectedStatusid
        labConsolidatedReportRequestModel.is_adult = selectedAdultid
        labConsolidatedReportRequestModel.testname_Id = selectedLabNameid
        labConsolidatedReportRequestModel.test_Id = selectedTestNameid
        labConsolidatedReportRequestModel.startdate = fromDate + " 00:00:00"
        if (search == 0) {
            val sdf = SimpleDateFormat("HH:mm:ss")
            labConsolidatedReportRequestModel.enddate = toDate + " " + sdf.format(Date())
        } else {
            labConsolidatedReportRequestModel.enddate = toDate + " 23:59:00"
        }
        labConsolidatedReportRequestModel.pageNo = currentPage
        labConsolidatedReportRequestModel.paginationSize = pageSize

        var requestModel = Gson().toJson(labConsolidatedReportRequestModel)

        Log.e("ConsolidatedReport 1::", "Request ::" + requestModel)
        viewModel?.getLabConsolidatedReportList(
            toggle,
            labConsolidatedReportRequestModel,
            consolidatedReportTableResponseRetrofitCallback
        )
        viewModel?.getLabConsolidatedReportLabel(
            toggle,
            labConsolidatedReportRequestModel,
            consolidateReportLabelResponseRetrofitCallback
        )

    }

    val consolidatedReportTableResponseRetrofitCallback = object :
        RetrofitCallback<LabConsolidatedReportResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabConsolidatedReportResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()?.responseContents)

            Log.e("labTestResponse", "" + responsedata)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                Log.e("list size", "" + responseBody.body()?.responseContents?.size)
                mAdapter!!.clearAll()
                mAdapter!!.addAll(responseBody.body()!!.responseContents)
                binding?.progressbar!!.visibility = View.GONE
                var listData: List<LabConsolidatedReportResponseContent?>? = listOf()
                listData = responseBody.body()!!.responseContents

                binding?.pdfDownload!!.visibility = View.VISIBLE
                binding?.listLayout!!.visibility = View.VISIBLE

                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

                val sDate = inputFormat.parse(fromDate + " 00:00:00")
                val start_Date = outputFormat.format(sDate)
                binding?.startDate!!.text = start_Date

                val sdf = SimpleDateFormat("HH:mm:ss")
                val eDate = inputFormat.parse(toDate + " " + sdf.format(Date()))
                val end_Date = outputFormat.format(eDate)
                binding?.endDate!!.text = end_Date

/*
               TOTAL_PAGES = Math.ceil(responseBody!!.body()!!.totalRecords!!.toDouble() / 10).toInt()
                if (responseBody.body()!!.responseContents!!.isNotEmpty()!!) {
                    mAdapter!!.addAll(responseBody!!.body()!!.responseContents)
                    if (currentPage < TOTAL_PAGES!!) {
                        binding?.progressbar!!.setVisibility(View.VISIBLE);
                        mAdapter!!.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                    } else {
                        binding?.progressbar!!.setVisibility(View.GONE);
                        mAdapter!!.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                    }

                } else {
                    binding?.progressbar!!.setVisibility(View.GONE);
                    mAdapter!!.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }
*/


            } else {
                // Toast.makeText(context!!,"No records found",Toast.LENGTH_LONG).show()
                binding?.progressbar!!.visibility = View.GONE
                binding?.pdfDownload!!.visibility = View.GONE
                binding?.listLayout!!.visibility = View.GONE

                binding?.totalPatientsCount!!.text = "0"
                binding?.totalPatientsAdultCount!!.text = "Adult - 0"
                binding?.totalPatientsChildCount!!.text = "Child - 0"

                binding?.adultCount!!.text = "0"
                binding?.adultMaleCount!!.text = "M - 0"
                binding?.adultFemaleCount!!.text = "F - 0"
                binding?.adultTransgenderCount!!.text = "TG - 0"

                binding?.childCount!!.text = "0"
                binding?.childMaleCount!!.text = "M - 0"
                binding?.childFemaleCount!!.text = "F - 0"

                mAdapter!!.clearAll()
            }

        }

        override fun onBadRequest(errorBody: Response<LabConsolidatedReportResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: LabConsolidatedReportResponseModel
            binding?.progressbar!!.visibility = View.GONE
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LabConsolidatedReportResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
            binding?.progressbar!!.visibility = View.GONE
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

        override fun onFailure(failure: String) {
            binding?.progressbar!!.visibility = View.GONE
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }

    val consolidateReportLabelResponseRetrofitCallback = object :
        RetrofitCallback<LabConsolidatedReportLabelResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabConsolidatedReportLabelResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()?.responseContents)
            Log.e("labTestResponse", "" + responsedata)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {

                val responseContents: List<LabConsolidatedReportLabelResponseContent?>? =
                    responseBody.body()?.responseContents

                binding?.totalPatientsCount!!.text = "" + responseContents?.get(0)?.total
                binding?.totalPatientsAdultCount!!.text = "Adult - " + responseContents?.get(0)?.totaladult
                binding?.totalPatientsChildCount!!.text = "Child - " + responseContents?.get(0)?.totalchild

                binding?.adultCount!!.text = "" + responseContents?.get(0)?.totaladult
                binding?.adultMaleCount!!.text = "M - " + responseContents?.get(0)?.maleadult
                binding?.adultFemaleCount!!.text = "F - " + responseContents?.get(0)?.femaleadult
                binding?.adultTransgenderCount!!.text = "TG - " + responseContents?.get(0)?.transgenderadult

                binding?.childCount!!.text = "" + responseContents?.get(0)?.totalchild
                binding?.childMaleCount!!.text = "M - " + responseContents?.get(0)?.malechild
                binding?.childFemaleCount!!.text = "F - " + responseContents?.get(0)?.femalechild
            }


        }

        override fun onBadRequest(errorBody: Response<LabConsolidatedReportLabelResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: LabConsolidatedReportLabelResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LabConsolidatedReportLabelResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }


    val consolidatedReportTableResponseSecondRetrofitCallback =
        object : RetrofitCallback<LabConsolidatedReportResponseModel> {
            override fun onSuccessfulResponse(response: Response<LabConsolidatedReportResponseModel>) {
                if (response.body()?.responseContents!!.isNotEmpty()) {

                    mAdapter!!.removeLoadingFooter()
                    isLoading = false
                    mAdapter?.addAll(response.body()!!.responseContents)
                    Log.i(
                        "page",
                        "" + currentPage + " " + response.body()?.responseContents!!.size
                    )
                    println("testing for two  = $currentPage--$TOTAL_PAGES")
                    binding?.progressbar!!.visibility = View.GONE

                    if (currentPage < TOTAL_PAGES) {
                        binding?.progressbar!!.visibility = View.VISIBLE
                        mAdapter?.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                        println("testing for four  = $currentPage--$TOTAL_PAGES")
                    } else {
                        isLastPage = true
                        binding?.progressbar!!.visibility = View.GONE
                        isLoading = false
                        isLastPage = true
                        println("testing for five  = $currentPage--$TOTAL_PAGES")
                    }


                } else {
                    println("testing for six  = $currentPage--$TOTAL_PAGES")
                    binding?.progressbar!!.visibility = View.GONE
                    mAdapter?.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }


            }

            override fun onBadRequest(response: Response<LabConsolidatedReportResponseModel>?) {
                mAdapter?.removeLoadingFooter()
                isLoading = false
                isLastPage = true

            }

            override fun onServerError(response: Response<*>) {
                viewModel!!.progress.value = View.GONE
                utils!!.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                viewModel!!.progress.value = View.GONE
                utils!!.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onForbidden() {
                viewModel!!.progress.value = View.GONE
                utils!!.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    val labDistrictSpinnerRetrofitCallback =
        object : RetrofitCallback<LabFilterResponseModel> {

            override fun onSuccessfulResponse(response: Response<LabFilterResponseModel>) {
                districtList?.clear()
                districtList?.add(LabFilterResponseContent())
                districtList?.addAll(response.body()?.responseContents!!)

                if ((binding?.etDistrictspinner!! is MultiSelectionSpinner)) {
                    val list = ArrayList<String>()
                    districtList?.forEach { t ->
                        if (t?.districtname != null && !t.districtname.isEmpty()) {
                            list.add((t.districtname).toString())
                        }
                    }
                    binding?.etDistrictspinner?.setItems(list, "District")
                }

                viewModel?.getHUD(toggle, selectedDistrictid, labHUDSpinnerRetrofitCallback)
            }

            override fun onBadRequest(response: Response<LabFilterResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: LabFilterResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        LabFilterResponseModel::class.java
                    )

                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
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

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    val labHUDSpinnerRetrofitCallback = object : RetrofitCallback<LabFilterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabFilterResponseModel>?) {
            hudList?.clear()
            hudList?.add(LabFilterResponseContent())
            hudList?.addAll(responseBody!!.body()?.responseContents!!)
            if ((binding?.etHudspinner!! is MultiSelectionSpinner)) {
                val list = ArrayList<String>()
                hudList?.forEach { t ->
                    if (t?.hudname != null && !t.hudname.isEmpty()) {
                        list.add((t.hudname).toString())
                    }
                }
                binding?.etHudspinner!!.setItems(list, "Hud")
            }

            viewModel?.getBlock(
                toggle,
                selectedDistrictid,
                selectedHUDid,
                labBlockSpinnerRetrofitCallback
            )

        }

        override fun onBadRequest(response: Response<LabFilterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: LabFilterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    LabFilterResponseModel::class.java
                )

            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    val labBlockSpinnerRetrofitCallback = object : RetrofitCallback<LabFilterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabFilterResponseModel>?) {
            blockList?.clear()
            blockList?.add(LabFilterResponseContent())
            blockList?.addAll(responseBody!!.body()?.responseContents!!)
            if ((binding?.etBlockspinner!! is MultiSelectionSpinner)) {
                val list = ArrayList<String>()
                blockList?.forEach { t ->
                    if (t?.blockname != null && !t.blockname.isEmpty()) {
                        list.add((t.blockname).toString())
                    }
                }
                binding?.etBlockspinner!!.setItems(list, "Block")
            }

            viewModel?.getOffice(
                toggle,
                selectedDistrictid,
                selectedHUDid,
                selectedBlockid,
                labOfficeSpinnerRetrofitCallback
            )

        }

        override fun onBadRequest(response: Response<LabFilterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: LabFilterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    LabFilterResponseModel::class.java
                )

            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    val labOfficeSpinnerRetrofitCallback = object : RetrofitCallback<LabFilterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabFilterResponseModel>?) {
            officeList?.clear()
            officeList?.add(LabFilterResponseContent())
            officeList?.addAll(responseBody!!.body()?.responseContents!!)
            if ((binding?.etOfficespinner!! is MultiSelectionSpinner)) {
                val list = ArrayList<String>()
                officeList?.forEach { t ->
                    if (t?.officename != null && !t.officename.isEmpty()) {
                        list.add((t.officename).toString())
                    }
                }
                binding?.etOfficespinner!!.setItems(list, "Office")
            }

            viewModel?.getInstitutionType(
                toggle,
                selectedDistrictid,
                selectedHUDid,
                selectedBlockid,
                selectedOfficeid,
                labInstitutionTypeSpinnerRetrofitCallback
            )

            // viewModel?.getDepartment(toggle,selectedDistrictid,selectedHUDid,selectedBlockid,selectedOfficeid,labDepartmentSpinnerRetrofitCallback)
        }

        override fun onBadRequest(response: Response<LabFilterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: LabFilterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    LabFilterResponseModel::class.java
                )

            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    val labDepartmentSpinnerRetrofitCallback = object : RetrofitCallback<LabFilterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabFilterResponseModel>?) {
            departmentList?.clear()
            departmentList?.add(LabFilterResponseContent())
            departmentList?.addAll(responseBody!!.body()?.responseContents!!)
            if ((binding?.etDepartmentpinner!! is MultiSelectionSpinner)) {
                val list = ArrayList<String>()
                departmentList?.forEach { t ->
                    if (t?.departmentid != null && !t.departmentname!!.isEmpty()) {
                        list.add((t.departmentname).toString())
                    }
                }
                binding?.etDepartmentpinner!!.setItems(list, "Office")
            }

            viewModel?.getInstitutionType(
                toggle,
                selectedDistrictid,
                selectedHUDid,
                selectedBlockid,
                selectedOfficeid,
                labInstitutionTypeSpinnerRetrofitCallback
            )
        }

        override fun onBadRequest(response: Response<LabFilterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: LabFilterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    LabFilterResponseModel::class.java
                )

            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    val labInstitutionTypeSpinnerRetrofitCallback =
        object : RetrofitCallback<LabFilterResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<LabFilterResponseModel>?) {
                institutionTypeList?.clear()
                institutionTypeList?.add(LabFilterResponseContent())
                institutionTypeList?.addAll(responseBody!!.body()?.responseContents!!)
                if ((binding?.etInstitutionTypespinner!! is MultiSelectionSpinner)) {
                    val list = ArrayList<String>()
                    institutionTypeList?.forEach { t ->
                        if (t?.facilitytypename != null && !t.facilitytypename.isEmpty()) {
                            list.add((t.facilitytypename).toString())
                        }
                    }
                    binding?.etInstitutionTypespinner!!.setItems(list, "Institution Type")
                }

                viewModel?.getInstitution(
                    toggle,
                    selectedDistrictid,
                    selectedHUDid,
                    selectedBlockid,
                    selectedOfficeid,
                    selectedInstitutionTypeid,
                    labInstitutionSpinnerRetrofitCallback
                )
            }

            override fun onBadRequest(response: Response<LabFilterResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: LabFilterResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        LabFilterResponseModel::class.java
                    )

                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
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

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }


        }

    val labInstitutionSpinnerRetrofitCallback = object : RetrofitCallback<LabFilterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabFilterResponseModel>?) {
            institutionlist?.clear()
            institutionlist?.add(LabFilterResponseContent())
            institutionlist?.addAll(responseBody!!.body()?.responseContents!!)
            if ((binding?.etInstitutionspinner!! is MultiSelectionSpinner)) {
                val list = ArrayList<String>()
                institutionlist?.forEach { t ->
                    if (t?.facilityname != null && !t.facilityname.isEmpty()) {
                        list.add((t.facilityname).toString())
                    }
                }
                binding?.etInstitutionspinner!!.setItems(list, "Institution")
            }

            if (initialLoad == 0) {
                setInstitution()
                initialLoad++
                viewModel?.getTestName(
                    toggle,
                    selectedInstitutionid,
                    labTestNameSpinnerRetrofitCallback
                )
            }

        }

        override fun onBadRequest(response: Response<LabFilterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: LabFilterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    LabFilterResponseModel::class.java
                )

            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    val labTestNameSpinnerRetrofitCallback = object : RetrofitCallback<LabFilterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabFilterResponseModel>?) {
            testNameList?.clear()
            Log.e("AssignedToSpinner", responseBody?.body()?.responseContents.toString())
            testNameList?.add(LabFilterResponseContent())
            testNameList?.addAll(responseBody!!.body()?.responseContents!!)
            if ((binding?.etTestNamespinner!! is MultiSelectionSpinner)) {
                val list = ArrayList<String>()
                testNameList?.forEach { t ->
                    if (t?.testname != null && !t.testname.isEmpty()) {
                        list.add((t.testname).toString())
                    }
                }
                binding?.etTestNamespinner!!.setItems(list, "Test Name")
            }

            viewModel?.getLabName(toggle, selectedInstitutionid, labNameSpinnerRetrofitCallback)
        }

        override fun onBadRequest(response: Response<LabFilterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: LabFilterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    LabFilterResponseModel::class.java
                )

            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    val labNameSpinnerRetrofitCallback = object : RetrofitCallback<LabFilterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabFilterResponseModel>?) {
            labNameList?.clear()
            Log.e("AssignedToSpinner", responseBody?.body()?.responseContents.toString())
            labNameList?.add(LabFilterResponseContent())
            labNameList?.addAll(responseBody!!.body()?.responseContents!!)
            if ((binding?.etLabNamespinner!! is MultiSelectionSpinner)) {
                val list = ArrayList<String>()
                labNameList?.forEach { t ->
                    if (t?.testtolocationname != null && !t.testtolocationname.isEmpty()) {
                        list.add((t.testtolocationname).toString())
                    }
                }
                binding?.etLabNamespinner!!.setItems(list, "Lab Name")
            }

            viewModel?.getGender(toggle, LabGenderSpinnerRetrofitCallback)
        }

        override fun onBadRequest(response: Response<LabFilterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: LabFilterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    LabFilterResponseModel::class.java
                )

            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    val LabGenderSpinnerRetrofitCallback = object : RetrofitCallback<LabFilterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabFilterResponseModel>?) {

            Log.e("AssignedToSpinner", responseBody?.body()?.responseContents.toString())
            listfilteritemAssignSpinner?.clear()
            listfilteritemAssignSpinner?.add(LabFilterResponseContent())
            listfilteritemAssignSpinner?.addAll(responseBody!!.body()?.responseContents!!)

            FilterGenderResponseMap =
                listfilteritemAssignSpinner!!.filter { it?.uuid != null && it.name != null }
                    .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
            FilterGenderResponseMap.put(0, "Select Gender")
            try {
                val adapter =
                    ArrayAdapter<String>(
                        context!!,
                        android.R.layout.simple_spinner_item,
                        FilterGenderResponseMap.values.toMutableList()
                    )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.genderSpinner!!.adapter = adapter
            } catch (e: Exception) {

            }
            binding?.genderSpinner?.setSelection(0)
            viewModel?.getStatus(toggle, labStatusSpinnerRetrofitCallback)
        }

        override fun onBadRequest(response: Response<LabFilterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: LabFilterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    LabFilterResponseModel::class.java
                )

            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    val labStatusSpinnerRetrofitCallback = object : RetrofitCallback<LabFilterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabFilterResponseModel>?) {
            statusList?.clear()
            Log.e("AssignedToSpinner", responseBody?.body()?.responseContents.toString())
            statusList?.add(LabFilterResponseContent())
            statusList?.addAll(responseBody!!.body()?.responseContents!!)
            if ((binding?.etStatusspinner!! is MultiSelectionSpinner)) {
                val list = ArrayList<String>()
                statusList?.forEach { t ->
                    if (t?.orderstatuscode != null && !t.orderstatuscode.isEmpty()) {
                        list.add((t.orderstatuscode).toString())
                    }
                }
                binding?.etStatusspinner!!.setItems(list, "Status")
            }

            FilterAdultOrChildResponseMap.put(3, "Select Adult / Child")
            FilterAdultOrChildResponseMap.put(0, "Child")
            FilterAdultOrChildResponseMap.put(1, "Adult")

            try {
                val adapter =
                    ArrayAdapter<String>(
                        context!!,
                        android.R.layout.simple_spinner_item,
                        FilterAdultOrChildResponseMap.values.toMutableList()
                    )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.adultOrChildSpinner!!.adapter = adapter
            } catch (e: Exception) {

            }
            binding?.adultOrChildSpinner?.setSelection(0)

        }

        override fun onBadRequest(response: Response<LabFilterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: LabFilterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    LabFilterResponseModel::class.java
                )

            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    private fun clearSearch() {

        val sdf1 = SimpleDateFormat("yyyy-MM-dd")

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        binding?.edtFromDate!!.setText("${formatter.format(Date())}")
        binding?.edtToDate!!.setText("${formatter.format(Date())}")

        fromDate = sdf1.format(Date())
        toDate = sdf1.format(Date())


        binding?.etDistrictspinner?.clear()
        binding?.etHudspinner?.clear()
        binding?.etBlockspinner?.clear()
        binding?.etOfficespinner?.clear()
        binding?.etDepartmentpinner?.clear()
        binding?.etInstitutionspinner?.clear()
        binding?.etStatusspinner?.clear()
        binding?.etLabNamespinner?.clear()
        binding?.etTestNamespinner?.clear()
        binding?.etInstitutionTypespinner?.clear()
        binding?.adultOrChildSpinner?.setSelection(0)
        binding?.genderSpinner?.setSelection(0)

        selectedDistrictid.clear()
        selectedHUDid.clear()
        selectedBlockid.clear()
        selectedOfficeid.clear()
        selectedInstitutionTypeid.clear()
        selectedInstitutionid.clear()
        selectedStatusid.clear()
        selectedLabNameid.clear()
        selectedTestNameid.clear()
        selectedAdultid.clear()
        selectedGenderid.clear()

        setInstitution()

        mAdapter!!.clearAll()
        pageSize = 10
        currentPage = 0
        labConsolidatedReportListAPI(10, 0, 0)

    }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is RejectDialogFragment) {
            childFragment.setOnLabTestRefreshListener(this)
        }
        if (childFragment is ConsolidatedReportResultDialogFragment) {
            childFragment.setOnLabConsolidatedReportRefreshListener(this)
        }

    }

    override fun onRefreshList() {
        Toast.makeText(requireContext(), "Rejected Successfully", Toast.LENGTH_LONG).show()

        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0

        labConsolidatedReportListAPI(10, 0, 0)
    }

    override fun onRefreshLabTestApprovalList() {
        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0

        labConsolidatedReportListAPI(10, 0, 0)

    }

    private fun setInstitution() {
        selectedInstitutionid.clear()
        for (i in institutionlist!!.indices) {
            if (facility_id != null && facility_id != 0) {
                if (institutionlist?.get(i)?.facilityid != null && (institutionlist?.get(i)?.facilityid == facility_id)) {
                    binding?.etInstitutionspinner!!.setChecked(i)
                    selectedInstitutionid.add(institutionlist?.get(i)?.facilityid!!)
                }
            } else {
                if (institutionlist?.get(i)?.facilityid != null) {
                    selectedInstitutionid.add(institutionlist?.get(i)?.facilityid!!)
                }

            }
        }

        initialLoad++
        mAdapter!!.clearAll()
        pageSize = 10
        currentPage = 0
        labConsolidatedReportListAPI(10, 0, 0)
    }


}




