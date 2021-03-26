package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.PrescriptionDrugInfoDialogFragmentBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.PresDrugInfoData
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.PresDrugInfoResponse
import com.hmis_tn.doctor.ui.emr_workflow.prescription.view_model.InfoViewModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.view_model.InfoViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response


class PrescriptionDrugInfoDialogFragment : DialogFragment() {
    private var drugID: String? = ""

    private var drugCode: String? = ""
    private var drugName: String? = ""
    private var itemMasterUUID: String? = ""
    private var storeMasterUUID: String? = ""
    private var content: String? = null
    private var viewModel: InfoViewModel? = null

    var binding: PrescriptionDrugInfoDialogFragmentBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_id: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var adapterPrescriptionDrugInfoList: AdapterPrescriptionDrugInfoList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (dialog.window != null)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window?.attributes?.windowAnimations = R.style.CardDialogAnimation
            isCancelable = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.prescription_drug_info_dialog_fragment,
                container,
                false
            )
        viewModel = InfoViewModelFactory(
            requireActivity().application
        )
            .create(InfoViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
//
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)


        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        val args = arguments
        if (args != null) {
            drugCode = args.getString("drugCode")
            drugName = args.getString("drugName")
            itemMasterUUID = args.getString("itemMasterUUID")
            storeMasterUUID = args.getString("storeMasterUUID")
        }

        binding!!.cancel.setOnClickListener {
            dialog!!.dismiss()
        }

        binding?.drugName?.text = "$drugName( $drugCode )"


        viewModel?.getPrescriptionDrugInfoDetails(
            itemMasterUUID,
            storeMasterUUID,
            facility_id,
            prescriptionDrugInfoListRetrofitCallback
        )

        binding?.rvPresDrugListInfo?.apply {
            adapterPrescriptionDrugInfoList =
                AdapterPrescriptionDrugInfoList(Utils(requireContext()))
            adapter = adapterPrescriptionDrugInfoList
        }
        return binding!!.root
    }

    private val prescriptionDrugInfoListRetrofitCallback =
        object : RetrofitCallback<PresDrugInfoResponse> {
            override fun onSuccessfulResponse(response: Response<PresDrugInfoResponse>?) {
                Log.i("res", "" + response?.body()?.responseContents)

                if (response?.body()?.statusCode == 200) {
                    if (!response.body()?.responseContents.isNullOrEmpty()) {
                        binding?.apply {
                            llDrugInfoList.visibility = View.VISIBLE
                            rvPresDrugListInfo.visibility = View.VISIBLE
                            totalQuantity.visibility = View.VISIBLE
                            noDataText.visibility = View.GONE
                        }

                        adapterPrescriptionDrugInfoList?.setData(response.body()?.responseContents as ArrayList<PresDrugInfoData>)
                        var totalQuantity = 0
                        response.body()?.responseContents?.forEach {
                            totalQuantity += it.quantity ?: 0
                        }

                        binding?.totalQuantity?.text = "Total Quanitity : $totalQuantity"
                    } else {
                        binding?.apply {
                            llDrugInfoList.visibility = View.GONE
                            rvPresDrugListInfo.visibility = View.GONE
                            totalQuantity.visibility = View.GONE
                            noDataText.visibility = View.VISIBLE
                        }

                    }
                }
            }

            override fun onBadRequest(response: Response<PresDrugInfoResponse>) {
                val gson = GsonBuilder().create()
                val responseModel: PresDrugInfoResponse
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PresDrugInfoResponse::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.message ?: ""
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