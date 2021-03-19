package com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.ui

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
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.LabResultTabFragmentBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.LabResultGetByDateViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.LabResultGetByDataResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.LabResultGetByDataResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.compare.ResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.view_model.LabResultGetByDateViewModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class LabResultGetDataFragment(
    private val compareDateList: java.util.ArrayList<String>,
    private val list: ArrayList<ResponseContent>,
    private val backClick: () -> Unit,
    private val cancelClick: () -> Unit
) : Fragment() {

    private var binding: LabResultTabFragmentBinding? = null
    private var viewModel: LabResultGetByDateViewModel? = null
    var cal = Calendar.getInstance()
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var mAdapter: LabResultByDateAdapter? = null
    private val labResultLIst: ArrayList<LabResultGetByDataResponseContent> = ArrayList()
    private var fragmentBackClick: FragmentBackClick? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.lab_result_tab_fragment,
                container,
                false
            )
        if (activity !is FragmentBackClick) {
//            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }

        viewModel = LabResultGetByDateViewModelFactory(
            requireActivity().application
        ).create(LabResultGetByDateViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        initViews()
        listeners()

        return binding!!.root
    }

    private fun initViews() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding?.LabResultByDataRecyclerView!!.layoutManager = layoutManager
        val list1 = prepareList(list)
        mAdapter = LabResultByDateAdapter(requireContext(), compareDateList, list1)
        binding?.LabResultByDataRecyclerView!!.adapter = mAdapter

        binding?.tvDate1?.text = utils?.displayDate(
            utils?.convertUtcToIst(compareDateList[0], "yyyy-MM-dd HH:mm:ss") ?: "",
            "yyyy-MM-dd HH:mm:ss"
        )
        binding?.tvDate2?.text = utils?.displayDate(
            utils?.convertUtcToIst(compareDateList[1], "yyyy-MM-dd HH:mm:ss") ?: "",
            "yyyy-MM-dd HH:mm:ss"
        )
    }

    private fun listeners() {
        binding?.nextCardView?.setOnClickListener {
            backClick()
        }

        binding?.cancelCardView?.setOnClickListener {
            cancelClick()
        }
    }

    private fun prepareList(list: ArrayList<ResponseContent>): ArrayList<ResponseContent> {
        val requiredList = ArrayList<ResponseContent>()
        for (i in 0 until list.size) {
            val singleResponseContent = list[i]
            if (singleResponseContent.order_request_date ==
                utils?.convertUtcToIst(compareDateList[0], "yyyy-MM-dd HH:mm:ss")
            ) {
                singleResponseContent.date1 =
                    singleResponseContent.order_request_batches?.get(0)?.result_value ?: ""
            } else if (singleResponseContent.order_request_date ==
                utils?.convertUtcToIst(compareDateList[1], "yyyy-MM-dd HH:mm:ss")
            ) {
                singleResponseContent.date2 =
                    singleResponseContent.order_request_batches?.get(0)?.result_value ?: ""
            }

            if (i % 2 == 0) {
                requiredList.add(singleResponseContent)
            } else {
                val tempResponseContent = requiredList[requiredList.size - 1]
                requiredList.removeAt(requiredList.size - 1)
                if (tempResponseContent.date1 == "")
                    tempResponseContent.date1 = singleResponseContent.date1
                else
                    tempResponseContent.date2 = singleResponseContent.date2
                requiredList.add(tempResponseContent)
            }
        }
        return requiredList
    }

    val labResultgetByDateRetrofitCallback =
        object : RetrofitCallback<LabResultGetByDataResponseModel> {
            override fun onSuccessfulResponse(response: Response<LabResultGetByDataResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    viewModel?.errorTextVisibility?.value = 8
//                    mAdapter!!.setData(response.body()?.responseContents as ArrayList<LabResultGetByDataResponseContent>)
                } else {
                    viewModel?.errorTextVisibility?.value = 0
                }
            }

            override fun onBadRequest(response: Response<LabResultGetByDataResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: LabResultGetByDataResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        LabResultGetByDataResponseModel::class.java
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

    override fun onStart() {
        super.onStart()
        fragmentBackClick?.setSelectedFragment(this)
    }

    private fun replaceFragment(
        fragment: Fragment
    ) {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.labRestultfragmentContainer, fragment)
        //////fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }
}
