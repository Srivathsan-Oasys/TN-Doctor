package com.hmis_tn.doctor.ui.emr_workflow.lab.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogManageLabPrevlabBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.labviewresponse.LabViewResponseModule
import com.hmis_tn.doctor.ui.emr_workflow.lab.view_model.ManageLabPervLabViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.lab.view_model.ManageLabPrevLabViewModel
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response


class ManageLabPervLabDialogFragment : DialogFragment() {
    private var content: String? = null
    private var viewModel: ManageLabPrevLabViewModel? = null
    private var managePrevLabAdapter: ManageLabPrevLabAdapter? = null
    var binding: DialogManageLabPrevlabBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var facility_id: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

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
            DataBindingUtil.inflate(inflater, R.layout.dialog_manage_lab_prevlab, container, false)
        viewModel = ManageLabPervLabViewModelFactory(
            requireActivity().application
        )
            .create(ManageLabPrevLabViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.cancelCardView?.setOnClickListener {
            dialog?.dismiss()
        }

        val args = arguments
        if (args == null) {

            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            val PatientOrderUUID = args.getInt(AppConstants.PATIENT_ORDER_UUID)

            viewModel?.getLabViewDetails(PatientOrderUUID, getLabviewRetrofitCallBack)
        }

        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.labManagePrevLabRecyclerView!!.layoutManager = linearLayoutManager
        managePrevLabAdapter = ManageLabPrevLabAdapter(
            requireActivity()
        )
        binding?.labManagePrevLabRecyclerView!!.adapter = managePrevLabAdapter
        return binding!!.root
    }

    val getLabviewRetrofitCallBack =
        object : RetrofitCallback<LabViewResponseModule> {
            override fun onSuccessfulResponse(response: Response<LabViewResponseModule>) {
                managePrevLabAdapter?.setData(response.body()?.responseContents!!)
            }

            override fun onBadRequest(response: Response<LabViewResponseModule>) {
                val gson = GsonBuilder().create()
                val responseModel: LabViewResponseModule
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        LabViewResponseModule::class.java
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