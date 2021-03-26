package com.hmis_tn.doctor.ui.quick_reg.view

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogRejectListBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.RejectRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.SendIdList
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.RejectReference
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.RejectReferenceResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.SimpleResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.UserProfileResponseModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestViewModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestViewModelFactory
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.util.*

class RejectDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var content: String? = null
    private var viewModel: LabTestViewModel? = null
    var binding: DialogRejectListBinding? = null

    private var customProgressDialog: CustomProgressDialog? = null
    var selectData: Int = 0
    var callbackLabTest: OnLabTestRefreshListener? = null
    private var favouriteData: ArrayList<SendIdList> = ArrayList()

    private var ref = mutableMapOf<Int, String>()

    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
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
            DataBindingUtil.inflate(inflater, R.layout.dialog_reject_list, container, false)
        viewModel = LabTestViewModelFactory(
            requireActivity().application

        )
            .create(LabTestViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        viewModel!!.progress.observe(requireActivity(), Observer { progress ->
            if (progress == View.VISIBLE) {
                customProgressDialog!!.show()
            } else if (progress == View.GONE) {
                customProgressDialog!!.dismiss()
            }

        })

        val args = arguments

        if (args == null) {

        } else {
            // get value from bundle..
            favouriteData = args.getParcelableArrayList<SendIdList>(AppConstants.RESPONSECONTENT)!!


        }


        viewModel!!.getRejectReference(getRejectRefernceRetrofitCallback)


        binding?.closeImageView?.setOnClickListener {
            //Call back
            dialog?.dismiss()
        }

        binding?.cancelCardView?.setOnClickListener {
            //Call back
            dialog?.dismiss()
        }
        binding?.saveCardView?.setOnClickListener {

            val request: RejectRequestModel = RejectRequestModel()

            val Idlist: ArrayList<Int> = ArrayList()

            Log.i("reject", "" + favouriteData)

            for (i in favouriteData.indices) {
                Idlist.add(favouriteData[i].Id)
            }
            request.Id = Idlist
            request.reject_category_uuid = selectData.toString()
            request.reject_reason = binding!!.commentEdittext.text.toString()
            viewModel!!.rejectLabTest(request, rejectRetrofitCallback)

        }

        return binding?.root
    }


    val rejectRetrofitCallback = object : RetrofitCallback<SimpleResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel>?) {

            callbackLabTest?.onRefreshList()

            dialog!!.dismiss()

        }

        override fun onBadRequest(errorBody: Response<SimpleResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: SimpleResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    SimpleResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "something wrong"
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


    val getRejectRefernceRetrofitCallback =
        object : RetrofitCallback<RejectReferenceResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<RejectReferenceResponseModel>?) {

                setspinner(responseBody!!.body()!!.responseContents)
            }

            override fun onBadRequest(errorBody: Response<RejectReferenceResponseModel>?) {
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

    private fun setspinner(responseContents: List<RejectReference>) {

        ref = responseContents.map { it.uuid to it.name }.toMap().toMutableMap()

        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ref.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding?.orderProcess!!.adapter = adapter

        binding?.orderProcess?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectData = ref.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    selectData = ref.filterValues { it == itemValue }.keys.toList()[0]
                }
            }
    }

    fun setOnLabTestRefreshListener(callback: OnLabTestRefreshListener) {
        callbackLabTest = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnLabTestRefreshListener {
        fun onRefreshList()
    }
}