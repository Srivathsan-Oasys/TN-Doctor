package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui

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
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.InfoDialogFragmentLayoutBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PresDrugInfoResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrescriptionInfoResponsModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.view_model.InfoViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.view_model.InfoViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class InfoDialogFragment : DialogFragment() {
    private var drugID: String? = ""

    private var itemMasterUUID: String? = ""
    private var storeMasterUUID: String? = ""
    private var content: String? = null
    private var routeSpinnerList = mutableMapOf<Int, String>()
    private var frequencySpinnerList = mutableMapOf<Int, String>()
    private var instructionSpinnerList = mutableMapOf<Int, String>()
    private var viewModel: InfoViewModel? = null
    private var prescriptionInfoData: PrescriptionInfoResponsModel? = null

    var binding: InfoDialogFragmentLayoutBinding? = null
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
                R.layout.info_dialog_fragment_layout,
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
            drugID = args.getString("DrugID")
            itemMasterUUID = args.getString("itemMasterUUID")
            storeMasterUUID = args.getString("storeMasterUUID")
        }

        binding!!.cancelCardView.setOnClickListener {
            dialog!!.dismiss()

        }


        viewModel?.getPrescriptionInfoDetails(drugID, facility_id, prescriptionInfoRetrofitCallback)
        viewModel?.getPrescriptionDrugInfoDetails(
            itemMasterUUID,
            storeMasterUUID,
            facility_id,
            prescriptionDrugInfoListRetrofitCallback
        )
        return binding!!.root
    }

    private val prescriptionDrugInfoListRetrofitCallback =
        object : RetrofitCallback<PresDrugInfoResponse> {
            override fun onSuccessfulResponse(response: Response<PresDrugInfoResponse>?) {
                Log.i("res", "" + response?.body()?.responseContents)
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
    val prescriptionInfoRetrofitCallback =
        object : RetrofitCallback<PrescriptionInfoResponsModel> {
            override fun onSuccessfulResponse(response: Response<PrescriptionInfoResponsModel>) {
                Log.i("res", "" + response.body()?.responseContents)

                if (response.body()?.responseContents != null) {
                    binding?.drugNameAutoTextView?.text = response.body()?.responseContents?.name
                    binding?.pharmacyAutoTextView?.text = response.body()?.responseContents?.stock_item?.store_master?.store_name
                    binding?.drugCodeAutoTextView?.text = response.body()?.responseContents?.code


                    if (response.body()?.responseContents?.stock_item != null) {
                        val stockItemIndex =
                            response.body()?.responseContents?.stock_item?.stock_serial_items?.size!! - 1

                        binding?.batchNoTextView?.text =
                            response.body()?.responseContents?.stock_item?.stock_serial_items?.get(
                                stockItemIndex
                            )?.batch_id!!
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        val sdf1 = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        val date = sdf.parse(
                            response.body()?.responseContents?.stock_item?.stock_serial_items?.get(
                                stockItemIndex
                            )?.expiry_date!!
                        )
                        val createdDate = sdf1.format(date!!)
                        binding?.expiryDateTextView?.text = createdDate
                    }
                    binding?.availableQuantityAutoTextVIew?.text = Integer.toString(response.body()?.responseContents?.stock_item?.quantity!!)

                }
            }

            override fun onBadRequest(response: Response<PrescriptionInfoResponsModel>) {
                val gson = GsonBuilder().create()
                val responseModel: PrescriptionInfoResponsModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PrescriptionInfoResponsModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.message
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