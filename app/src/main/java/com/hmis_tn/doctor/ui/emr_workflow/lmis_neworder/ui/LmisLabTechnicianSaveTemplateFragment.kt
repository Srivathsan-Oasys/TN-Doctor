package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogManageLimsLabSaveTemplateBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.*
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.request.Detail
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.view_model.ManageLmisLabTemplateViewModel
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.view_model.manageLmisLabTemplateViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response


class LmisLabTechnicianSaveTemplateFragment : DialogFragment() {


    private var deparment_UUID: Int? = 0
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    private var content: String? = null
    private var viewModel: ManageLmisLabTemplateViewModel? = null
    var binding: DialogManageLimsLabSaveTemplateBinding? = null
    private var facility_UUID: Int? = 0
    private var favAddResponseMap = mutableMapOf<Int, String>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var dropdownReferenceView: AutoCompleteTextView? = null
    val detailsList: ArrayList<Detail> = ArrayList()
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var mAdapter: LmisSaveTemplateAdapter? = null
    private val favList: MutableList<RecyclerDto> = java.util.ArrayList()

    var linearLayoutManager: LinearLayoutManager? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_manage_lims_lab_save_template,
                container,
                false
            )
        viewModel = manageLmisLabTemplateViewModelFactory(
            requireActivity().application
        )
            .create(ManageLmisLabTemplateViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this


        mAdapter = LmisSaveTemplateAdapter(requireContext(), favList)
        binding?.labManageTSaveemplateRecyclerView!!.adapter = mAdapter


        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        binding?.UserName?.setText(userDataStoreBean?.user_name)


        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.spinnerdepartment?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            binding?.viewModel?.getAllDepartment(facility_UUID, favAddAllDepartmentCallBack)
            false
        })

        binding?.UserName?.setText(userDataStoreBean?.user_name)



        return binding?.root
    }


    val favAddAllDepartmentCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {

            listAllAddDepartmentItems = responseBody?.body()?.responseContents!!
            favAddResponseMap =
                listAllAddDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()

            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    favAddResponseMap.values.toMutableList()
                )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.spinnerdepartment!!.adapter = adapter

        }

        override fun onBadRequest(errorBody: Response<FavAddAllDepatResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddAllDepatResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    FavAddAllDepatResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    ""
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }

    val favAddTestNameCallBack = object : RetrofitCallback<FavAddTestNameResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavAddTestNameResponse>?) {


            autocompleteTestResponse = responseBody?.body()?.responseContents
            val responseContentAdapter = FavTestNameSearchResultAdapter(
                context!!,
                R.layout.row_chief_complaint_search_result,
                responseBody?.body()?.responseContents!!
            )
            binding?.autoCompleteTextViewTestName?.threshold = 1
            binding?.autoCompleteTextViewTestName?.setAdapter(responseContentAdapter)

        }

        override fun onBadRequest(errorBody: Response<FavAddTestNameResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddTestNameResponse
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    FavAddTestNameResponse::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    ""
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }


}


