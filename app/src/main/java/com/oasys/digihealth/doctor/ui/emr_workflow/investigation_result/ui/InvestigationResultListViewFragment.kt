package com.oasys.digihealth.doctor.ui.emr_workflow.investigation_result.ui

import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.InvestigationResultChildFragmentBinding
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.InvestigationResultViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.documents.model.AddDocumentDetailsResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation_result.model.InvestigationResultResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation_result.model.InvestigationResultResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation_result.view_model.InvestigationResultViewModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.ResponseBody
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class InvestigationResultListViewFragment : Fragment() {
    private var dateDialog: DatePickerDialog? = null
    private var Counter = 1
    var startDate: String? = null
    var endDate: String? = null

    private var binding: InvestigationResultChildFragmentBinding? = null
    private var viewModel: InvestigationResultViewModel? = null
    var cal = Calendar.getInstance()
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var mAdapter: InvestigationResultParentAdapter? = null

    private var mChildAdapter: InvestigationResultChildAdapter? = null

    private var fromCalenderDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var toCalenderDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val fromCalender = Calendar.getInstance()
    private val toCalender = Calendar.getInstance()
    private var patientID: Int? = 0
    private var facilityid: Int? = 0


    var imgposition: Int? = null
    private var fileformat: String? = ""

    var selectPosition: Int = 0
    var dataSize: Int = 0
    var listData: ArrayList<InvestigationResultResponseContent> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.investigation_result_child_fragment,
                container,
                false
            )

        viewModel = InvestigationResultViewModelFactory(
            requireActivity().application
        ).create(InvestigationResultViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        trackInvestigationResultVisit(utils!!.getEncounterType())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        viewModel?.getLab_Result(
            patientID, facilityid,
            startDate.toString(), endDate.toString(), investigationResultRetrofitCallback
        )

/*
        viewModel?.getRadiology_Result(patientID,facilityid,radiologyResultRetrofitCallback)
*/

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding?.investigationResultRecyclerView!!.layoutManager = layoutManager
        mAdapter = InvestigationResultParentAdapter(requireContext(), ArrayList())
        binding?.investigationResultRecyclerView!!.adapter = mAdapter

        binding?.calendarEditText?.setOnClickListener {
            showFromDatePickerDialog()
        }



        fromCalenderDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                fromCalender.set(Calendar.YEAR, year)
                fromCalender.set(Calendar.MONTH, monthOfYear)
                fromCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setFromDate()
            }

        toCalenderDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                toCalender.set(Calendar.YEAR, year)
                toCalender.set(Calendar.MONTH, monthOfYear)
                toCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setToDate()
            }
        //adapter setting


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
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        startDate = dateMonthAndYear.format(fromCalender.time)

        showToDatePickerDialog()
    }

    /**
     * Setting to date
     */
    private fun setToDate() {
        val dateMonthAndYear =
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        endDate = dateMonthAndYear.format(toCalender.time)
        binding?.calendarEditText?.setText(startDate + "~" + endDate)
        viewModel?.getInvestigation_ResultByDate(
            patientID.toString(), facilityid,
            startDate.toString(), endDate.toString(), investigationDateByResultRetrofitCallback
        )

    }

    val investigationResultRetrofitCallback =
        object : RetrofitCallback<InvestigationResultResponseModel> {
            override fun onSuccessfulResponse(response: Response<InvestigationResultResponseModel>) {

                if (isAdded)
                    if (!response.body()?.responseContents.isNullOrEmpty()) {
                        binding?.investigationResultRecyclerView?.visibility = View.VISIBLE
                        binding?.noDataText?.visibility = View.GONE

                        mAdapter!!.setData(response.body()?.responseContents as ArrayList<InvestigationResultResponseContent>)


                        dataSize =
                            (response.body()?.responseContents as ArrayList<InvestigationResultResponseContent>).size

                        listData =
                            response.body()?.responseContents as ArrayList<InvestigationResultResponseContent>

                        selectPosition = 0

                        if (listData[selectPosition].repsonse[0].work_order_attachment_file_path != null) {

                            dowloadimage(
                                listData[selectPosition].repsonse[0].work_order_attachment_file_path,
                                selectPosition
                            )
                        } else {

                            selectPosition++
                        }

                    } else {
                        binding?.investigationResultRecyclerView?.visibility = View.GONE
                        binding?.noDataText?.visibility = View.VISIBLE
                    }


            }

            override fun onBadRequest(response: Response<InvestigationResultResponseModel>) {
                if (isAdded)
                    if (response.code() == 400) {
                        binding?.investigationResultRecyclerView?.visibility = View.GONE
                        binding?.noDataText?.visibility = View.VISIBLE

                    } else {
                        binding?.investigationResultRecyclerView?.visibility = View.VISIBLE
                        binding?.noDataText?.visibility = View.GONE
                    }
                /* utils?.showToast(
                     R.color.negativeToast,
                     binding?.mainLayout!!,
                     response.message()
                 )*/
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


    val investigationDateByResultRetrofitCallback =
        object : RetrofitCallback<InvestigationResultResponseModel> {
            override fun onSuccessfulResponse(response: Response<InvestigationResultResponseModel>) {

                if (isAdded)
                    if (!response.body()?.responseContents.isNullOrEmpty()) {
                        binding?.investigationResultRecyclerView?.visibility = View.VISIBLE
                        binding?.noDataText?.visibility = View.GONE

                        mAdapter!!.setData(response.body()?.responseContents as ArrayList<InvestigationResultResponseContent>)

                    } else {
                        binding?.investigationResultRecyclerView?.visibility = View.GONE
                        binding?.noDataText?.visibility = View.VISIBLE
                    }

            }

            override fun onBadRequest(response: Response<InvestigationResultResponseModel>) {
                if (isAdded)
                    if (response.code() == 400) {
                        binding?.investigationResultRecyclerView?.visibility = View.GONE
                        binding?.noDataText?.visibility = View.VISIBLE

                    } else {
                        binding?.investigationResultRecyclerView?.visibility = View.VISIBLE
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


/*
    val radiologyResultRetrofitCallback = object : RetrofitCallback<InvestigationResultResponseModel> {
        override fun onSuccessfulResponse(response: Response<InvestigationResultResponseModel>) {

            if (!response.body()?.responseContents?.isNullOrEmpty()!!) {
                viewModel?.errorTextVisibility?.value = 8
            //    mChildAdapter!!.setData(response.body()?.responseContents as ArrayList<InvestigationResultResponseContent>)

            } else {
                viewModel?.errorTextVisibility?.value = 0
            }

        }

        override fun onBadRequest(response: Response<InvestigationResultResponseModel>) {
            if (response?.code() == 400) {
                //TODO NO RESULT FOUND
            }else{

            }
            */
/* utils?.showToast(
                 R.color.negativeToast,
                 binding?.mainLayout!!,
                 response.message()
             )*//*

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
*/


    fun trackInvestigationResultVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackInvestigationResultVisit(type)
    }


    private fun dowloadimage(filepath: String?, position: Int) {
        imgposition = position
        fileformat =
            filepath!!.substring(filepath.lastIndexOf(".") + 1) // Without dot jpg, png
        viewModel?.getImage(
            filepath,
            downloadimagecallback,
            facilityid
        )
    }

    val downloadimagecallback =
        object : RetrofitCallback<ResponseBody> {
            override fun onSuccessfulResponse(response: Response<ResponseBody>) {
                val bmp = BitmapFactory.decodeStream(response.body()!!.byteStream())


                mAdapter?.setImage(bmp, selectPosition)

                selectPosition++

                if (selectPosition < dataSize) {


                    if (listData[selectPosition].repsonse[0].work_order_attachment_file_path != null) {
                        dowloadimage(
                            listData[selectPosition].repsonse[0].work_order_attachment_file_path,
                            selectPosition
                        )

                    } else {

                        selectPosition++

                    }
                }


            }

            override fun onBadRequest(response: Response<ResponseBody>) {
                val responseModel: AddDocumentDetailsResponseModel
                try {
                    responseModel = GsonBuilder().create().fromJson(
                        response.errorBody()!!.string(),
                        AddDocumentDetailsResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        response.message()
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


}






