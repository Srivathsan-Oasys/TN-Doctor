package com.oasys.digihealth.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogSendApprovalListBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.SendApprovalRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.SendIdList
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.SimpleResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.UserProfileResponse
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.UserProfileResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.DoctorListAdapter
import com.oasys.digihealth.doctor.ui.quick_reg.view_model.LabTestProcessViewModel
import com.oasys.digihealth.doctor.ui.quick_reg.view_model.LabTestProcessViewModelFactory
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class SendForApprovalDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var content: String? = null
    private var viewModel: LabTestProcessViewModel? = null
    var binding: DialogSendApprovalListBinding? = null

    var callbacksendForApprovalProcess: OnsendForApprovalListener? = null
    private var favouriteData: ArrayList<SendIdList> = ArrayList()
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var customProgressDialog: CustomProgressDialog? = null
    var selectDoctorId: Int = 0

    var responseAdapter: DoctorListAdapter? = null
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
            DataBindingUtil.inflate(inflater, R.layout.dialog_send_approval_list, container, false)
        viewModel = LabTestProcessViewModelFactory(
            requireActivity().application

        ).create(LabTestProcessViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        responseAdapter =

            DoctorListAdapter(
                this.requireContext(), R.layout.row_chief_complaint_search_result,
                ArrayList()
            )
        viewModel!!.progress.observe(requireActivity(), Observer { progress ->
            if (progress == View.VISIBLE) {
                customProgressDialog!!.show()
            } else if (progress == View.GONE) {
                customProgressDialog!!.dismiss()
            }

        })
        viewModel!!.orderProcess(userDataRetrofitCallback)

        binding?.closeImageView?.setOnClickListener {

            dialog?.dismiss()
        }

        binding?.cancelCardView?.setOnClickListener {

            dialog?.dismiss()
        }


        binding!!.doctorName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.isEmpty()) {

                    selectDoctorId = 0
                }

            }
        })

        binding?.saveCardView?.setOnClickListener {

            if (binding!!.doctorName.text.toString() != "" && selectDoctorId != 0) {

                val request: SendApprovalRequestModel = SendApprovalRequestModel()

                var Idlist: ArrayList<Int> = ArrayList()

                for (i in favouriteData.indices) {

                    Idlist.add(favouriteData[i].Id)

                }

                request.Id = Idlist

                request.doctor_Id = selectDoctorId

                request.comments = binding!!.commentEdittext.text.toString()

                var jsonresponse = Gson().toJson(request)

                Log.i("sendapprovel", "" + jsonresponse)

                viewModel!!.saveApprovel(request, saveApprovelRetrofitCallback)

            } else {

                Toast.makeText(this.context, "Please Select Valid Doctor Name", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        val args = arguments

        if (args == null) {

        } else {
            // get value from bundle..
            favouriteData = args.getParcelableArrayList<SendIdList>(AppConstants.RESPONSECONTENT)!!

        }
        return binding?.root
    }

    val userDataRetrofitCallback = object : RetrofitCallback<UserProfileResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<UserProfileResponseModel>?) {

            responseAdapter!!.setData(responseBody?.body()?.responseContents as ArrayList<UserProfileResponse>)

            val data = responseBody.body()?.responseContents as ArrayList<UserProfileResponse>

            if (data.size == 1) {

                binding!!.doctorName.setText(data[0].first_name)

                selectDoctorId = data[0].uuid

            }

            setAdapter()


        }

        override fun onBadRequest(errorBody: Response<UserProfileResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: UserProfileResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    UserProfileResponseModel::class.java
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

    val saveApprovelRetrofitCallback = object : RetrofitCallback<SimpleResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel>?) {

            Toast.makeText(context, responseBody?.body()?.message, Toast.LENGTH_SHORT).show()

            callbacksendForApprovalProcess?.onRefreshsendApprovalList()
            dialog!!.dismiss()
        }

        override fun onBadRequest(errorBody: Response<SimpleResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: UserProfileResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    UserProfileResponseModel::class.java
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

    private fun setAdapter() {

        binding!!.doctorName.threshold = 1

        binding!!.doctorName.setAdapter(responseAdapter)

        binding!!.doctorName.showDropDown()

        binding!!.doctorName.setOnItemClickListener { parent, _, pos, id ->

            val selectedPoi = parent.adapter.getItem(pos) as UserProfileResponse?

            binding!!.doctorName.setText(selectedPoi!!.first_name)

            selectDoctorId = selectedPoi.uuid

        }
    }


    fun setOnForApprovalRefreshListener(callback: OnsendForApprovalListener) {
        callbacksendForApprovalProcess = callback
    }

    // or a separate test implementation.
    interface OnsendForApprovalListener {
        fun onRefreshsendApprovalList()
    }
}

