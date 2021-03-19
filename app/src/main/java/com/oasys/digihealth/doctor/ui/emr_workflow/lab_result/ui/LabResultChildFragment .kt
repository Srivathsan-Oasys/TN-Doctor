package com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.LabResultChildFragmentBinding
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.LabResultViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.LabResultResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.LabResultResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.compare.LabCompareResp
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.compare.ResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.view_model.LabResultViewModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LabResultChildFragment : Fragment() {

    var startDate: String = ""
    var endDate: String = ""
    private var binding: LabResultChildFragmentBinding? = null
    private var viewModel: LabResultViewModel? = null
    var cal: Calendar = Calendar.getInstance()
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var patientID: Int? = 0
    private var facilityid: Int? = 0

    private var mAdapter: LabResultParentAdapter? = null

    private val labResultLIst: ArrayList<LabResultResponseContent> = ArrayList()
    private var fromCalenderDateSetListener: OnDateSetListener? = null
    private var toCalenderDateSetListener: OnDateSetListener? = null
    private val fromCalender = Calendar.getInstance()
    private val toCalender = Calendar.getInstance()
    private var fragmentBackClick: FragmentBackClick? = null
    private var compareLabList = ArrayList<String>()
    private var compareDateList = ArrayList<String>()
    private var isCompare = false
    private var masterId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.lab_result_child_fragment,
                container,
                false
            )

        if (activity !is FragmentBackClick) {
//            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }

        viewModel = LabResultViewModelFactory(
            requireActivity().application
        ).create(LabResultViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        tracklabResultVisit(utils!!.getEncounterType())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        binding?.calendarEditText?.setOnClickListener {
            showFromDatePickerDialog()
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding?.LabResultRecyclerView!!.layoutManager = layoutManager

        mAdapter = LabResultParentAdapter(onCompareButtonValidate = { selectedCompareList ->
            if (!selectedCompareList.isNullOrEmpty()) {
                compareLabList.clear()
                compareDateList.clear()
                selectedCompareList.forEach { labResultResponseContent: LabResultResponseContent ->
                    compareLabList.add(labResultResponseContent.test_master_uuid?.toString() ?: "")
                    val date: String = utils?.convertIstToUtc(
                        labResultResponseContent.order_request_date,
                        "yyyy-MM-dd HH:mm:ss"
                    ) ?: ""
                    compareDateList.add(date)
                }
                if (selectedCompareList.size > 1) {
                    masterId = compareLabList[0]
                    isCompare = verifyAllEqual(compareLabList)
                    binding?.compareCardView?.setCardBackgroundColor(backGroundColor(R.color.orange))
                    binding?.compareCardView?.isEnabled = true
                } else {
                    binding?.compareCardView?.setCardBackgroundColor(backGroundColor(R.color.clear_button))
                }
            } else {
                binding?.compareCardView?.setCardBackgroundColor(backGroundColor(R.color.clear_button))
                binding?.compareCardView?.isEnabled = false
            }
        })
        binding?.LabResultRecyclerView!!.adapter = mAdapter
        mAdapter?.setData(labResultLIst)

        binding?.compareCardView?.setCardBackgroundColor(backGroundColor(R.color.clear_button))


        fromCalenderDateSetListener =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                fromCalender.set(Calendar.YEAR, year)
                fromCalender.set(Calendar.MONTH, monthOfYear)
                fromCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setFromDate()
            }

        toCalenderDateSetListener =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                toCalender.set(Calendar.YEAR, year)
                toCalender.set(Calendar.MONTH, monthOfYear)
                toCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setToDate()
            }


        binding?.compareCardView?.setOnClickListener {
            if (compareLabList.size > 1)
                if (isCompare) {
                    viewModel?.getLabCompare(
                        patientID.toString(),
                        facilityid,
                        compareDateList,
                        masterId,
                        labResultCompareRetrofitCallback
                    )
                } else {
                    alertDialog("Not matched !!!")
                }
        }

        viewModel?.getLab_Result(
            patientID.toString(),
            facilityid,
            startDate,
            endDate,
            labResultRetrofitCallback
        )

        return binding!!.root
    }

    /**
     * Show From Date picker dialog
     */
    private fun showFromDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.TimePickerTheme,
            fromCalenderDateSetListener, calendar[Calendar.YEAR], calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    /**
     * Show To Date picker dialog
     */
    private fun showToDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.TimePickerTheme,
            toCalenderDateSetListener,
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    /**
     * Setting from date
     */
    private fun setFromDate() {

        val dateMonthAndYear =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        startDate = dateMonthAndYear.format(fromCalender.time)

        showToDatePickerDialog()
    }

    /**
     * Setting to date
     */
    private fun setToDate() {
        val dateMonthAndYear =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        endDate = dateMonthAndYear.format(toCalender.time)

        val viewFormat = SimpleDateFormat("dd-MMM-yyyy")

        val stD = dateMonthAndYear.parse(startDate)

        val etD = dateMonthAndYear.parse(endDate)

        val st = stD?.let { viewFormat.format(it) }

        val et = etD?.let { viewFormat.format(it) }

        binding?.calendarEditText?.setText("$st to $et")

        viewModel?.getLab_ResultByDate(
            patientID.toString(),
            facilityid,
            startDate,
            endDate,
            labDateByResultRetrofitCallback
        )
    }


    private val labResultCompareRetrofitCallback =
        object : RetrofitCallback<LabCompareResp> {
            override fun onSuccessfulResponse(response: Response<LabCompareResp>) {
                if (isAdded)
                    if (!response.body()?.responseContents.isNullOrEmpty()) {
                        binding?.LabResultRecyclerView?.visibility = View.VISIBLE
                        binding?.noDataText?.visibility = View.GONE

//                        mAdapter!!.setData(response.body()?.responseContents as ArrayList<LabResultResponseContent>)

                        val labResultTabFragment = LabResultTabFragment(
                            compareDateList,
                            response.body()?.responseContents as ArrayList<ResponseContent>
                        )
                        replaceFragment(labResultTabFragment)
                    } else {
                        binding?.LabResultRecyclerView?.visibility = View.GONE
                        binding?.noDataText?.visibility = View.VISIBLE
                    }
            }

            override fun onBadRequest(response: Response<LabCompareResp>) {
                if (isAdded)
                    if (response.code() == 400) {
                        binding?.LabResultRecyclerView?.visibility = View.GONE
                        binding?.noDataText?.visibility = View.VISIBLE

                    } else {
                        binding?.LabResultRecyclerView?.visibility = View.VISIBLE
                        binding?.noDataText?.visibility = View.GONE
                    }
            }


            override fun onServerError(response: Response<*>) {
                if (isAdded)
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
            }

            override fun onUnAuthorized() {
                if (isAdded)
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.unauthorized)
                    )
            }

            override fun onForbidden() {
                if (isAdded)
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
            }

            override fun onFailure(failure: String) {
                if (isAdded)
                    utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val labResultRetrofitCallback = object : RetrofitCallback<LabResultResponseModel> {
        override fun onSuccessfulResponse(response: Response<LabResultResponseModel>) {
            if (isAdded)
                if (!response.body()?.responseContents.isNullOrEmpty()) {
                    binding?.LabResultRecyclerView?.visibility = View.VISIBLE
                    binding?.noDataText?.visibility = View.GONE

                    mAdapter!!.setData(response.body()?.responseContents as ArrayList<LabResultResponseContent>)

                } else {
                    binding?.LabResultRecyclerView?.visibility = View.GONE
                    binding?.noDataText?.visibility = View.VISIBLE
                }

        }

        override fun onBadRequest(response: Response<LabResultResponseModel>) {
            if (isAdded)
                if (response.code() == 400) {
                    binding?.LabResultRecyclerView?.visibility = View.GONE
                    binding?.noDataText?.visibility = View.VISIBLE

                } else {
                    binding?.LabResultRecyclerView?.visibility = View.VISIBLE
                    binding?.noDataText?.visibility = View.GONE
                }
        }


        override fun onServerError(response: Response<*>) {
            if (isAdded)
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
        }

        override fun onUnAuthorized() {
            if (isAdded)
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
        }

        override fun onForbidden() {
            if (isAdded)
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
        }

        override fun onFailure(failure: String) {
            if (isAdded)
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private val labDateByResultRetrofitCallback =
        object : RetrofitCallback<LabResultResponseModel> {
            override fun onSuccessfulResponse(response: Response<LabResultResponseModel>) {

                if (isAdded)
                    if (!response.body()?.responseContents.isNullOrEmpty()) {
                        binding?.LabResultRecyclerView?.visibility = View.VISIBLE
                        binding?.noDataText?.visibility = View.GONE

                        mAdapter!!.setData(response.body()?.responseContents as ArrayList<LabResultResponseContent>)

                    } else {
                        binding?.LabResultRecyclerView?.visibility = View.GONE
                        binding?.noDataText?.visibility = View.VISIBLE
                    }

            }

            override fun onBadRequest(response: Response<LabResultResponseModel>) {
                if (isAdded)
                    if (response.code() == 400) {
                        binding?.LabResultRecyclerView?.visibility = View.GONE
                        binding?.noDataText?.visibility = View.VISIBLE

                    } else {
                        binding?.LabResultRecyclerView?.visibility = View.VISIBLE
                        binding?.noDataText?.visibility = View.GONE
                    }
            }

            override fun onServerError(response: Response<*>) {
                if (isAdded)
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
            }

            override fun onUnAuthorized() {
                if (isAdded)
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.unauthorized)
                    )
            }

            override fun onForbidden() {
                if (isAdded)
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
            }

            override fun onFailure(failure: String) {
                if (isAdded)
                    utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun replaceFragment(
        fragment: Fragment
    ) {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.labRestultfragmentContainer, fragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }

    fun tracklabResultVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackLabResultVisit(type)

    }

    private fun alertDialog(msg: String) {
        val builder = AlertDialog.Builder(
            requireContext()
        )
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage(msg)
        builder.setPositiveButton("OK") { d, _ ->
            d?.dismiss()
        }
        builder.create()
        builder.show()
    }

    private fun backGroundColor(color: Int): Int {
        return ContextCompat.getColor(requireContext(), color)
    }

    private fun verifyAllEqual(list: List<String>?): Boolean {
        return HashSet(list).size <= 1
    }

    override fun onStart() {
        super.onStart()
        fragmentBackClick?.setSelectedFragment(this)
    }
}
