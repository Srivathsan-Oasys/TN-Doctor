package com.hmis_tn.doctor.ui.sample_dispatch.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogSampleListBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.home.HomeActivity
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.SendIdList
import com.hmis_tn.doctor.ui.sample_dispatch.model.DispatchReq
import com.hmis_tn.doctor.ui.sample_dispatch.model.SampleDispatchResponseModel
import com.hmis_tn.doctor.ui.sample_dispatch.view_model.SampleDispatchViewModel
import com.hmis_tn.doctor.ui.sample_dispatch.view_model.SampleDispatchViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import retrofit2.Response
import java.util.*

class DispatchDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var content: String? = null
    private var viewModel: SampleDispatchViewModel? = null
    var binding: DialogSampleListBinding? = null

    private var customProgressDialog: CustomProgressDialog? = null
    var selectData: Int = 0
    private var favouriteData: ArrayList<SendIdList> = ArrayList()

    private var ref = mutableMapOf<Int, String>()

    private var utils: Utils? = null

    private var tofacility: Int? = 0

    private var intData: ArrayList<Int> = ArrayList()
    private var dispatchdata: ArrayList<Int> = ArrayList()

    var appPreferences: AppPreferences? = null

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
            DataBindingUtil.inflate(inflater, R.layout.dialog_sample__list_, container, false)
        viewModel = SampleDispatchViewModelFactory(
            requireActivity().application
        )
            .create(SampleDispatchViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        val args = arguments
        if (args == null) {

        } else {
            // get value from bundle..
            intData = args.getIntegerArrayList(AppConstants.RESPONSECONTENT)!!
            //    dispatchdata = args.getIntegerArrayList(AppConstants.RESPONSEDISPATCH)!!
            tofacility = args.getInt(AppConstants.RESPONSENEXT)
        }

        binding!!.number.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val datasize = s.trim().length
                val dt = s.trim()
                if (datasize != 0) {
                    val firstlt = dt[0].toInt()
                    Log.i("first", "" + firstlt)

                    if (firstlt > 5) {
                        if (datasize == 10) {
                            if (s.trim().toString().toLong() < 6000000000) {
                                binding!!.number.error = "InValid Mobile Number"
                            } else {
                                binding!!.number.error = null
                            }
                        } else {
                            binding!!.number.error = "Mobile Number Must be 10 digit"
                        }
                    } else {
                        binding!!.number.error = "InValid Mobile Number"
                    }
                }
            }
        })

        binding?.saveCardView?.setOnClickListener {

            if (!isValidMobileStart(binding!!.number.text.trim().toString())) {
                binding?.number!!.error = "Mobile Number Not Valid "
                Toast.makeText(requireContext(), "Enter Valid Attender Mobile", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (!binding!!.name.text.toString().isNullOrEmpty()) {
                if (!binding!!.number.text.toString().isNullOrEmpty()) {
                    if (binding!!.number.text.length == 10) {
                        Log.i("save", "" + intData)
                        val req: DispatchReq = DispatchReq()
                        req.uuids = intData
                        req.comments = binding!!.commentEdittext.text.toString()
                        req.dispatch_name = binding!!.name.text.toString()
                        req.mobile = binding!!.number.text.toString()
                        req.to_facility_id = tofacility!!
                        viewModel!!.dispatch(req, dispatchRapidRetrofitCallback)
                    } else {
                        binding!!.number.error = "Mobile no must be a 10 digit"
                    }
                } else {
                    binding!!.number.error = "Please enter contact Number"
                }
            } else {
                binding!!.name.error = "Please enter Name"
            }
            //Call back
            //dialog?.dismiss()
        }

        binding?.closeImageView?.setOnClickListener {
            //Call back
            dialog?.dismiss()
        }

        binding?.cancelCardView?.setOnClickListener {
            //Call back
            dialog?.dismiss()
        }
        return binding?.root
    }

    val dispatchRapidRetrofitCallback = object : RetrofitCallback<SampleDispatchResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SampleDispatchResponseModel>?) {
            Toast.makeText(context, "Save Successfully", Toast.LENGTH_LONG).show()
            Log.i("", "" + responseBody!!.body()!!.responseContents[0].sample_transport_batch_uuid)
            //call back then pdf  navigation send dispatchdata Array for print
            val bundle = Bundle()
            bundle.putInt(
                "pdfid",
                responseBody.body()!!.responseContents[0].sample_transport_batch_uuid
            )
            val labtemplatedialog = DispatchPDF()
            labtemplatedialog.arguments = bundle
            (activity as HomeActivity).replaceFragment(labtemplatedialog)

            dialog!!.dismiss()

        }

        override fun onBadRequest(errorBody: Response<SampleDispatchResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: SampleDispatchResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    SampleDispatchResponseModel::class.java
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

    private fun isValidMobileStart(mobileNo: String): Boolean {
        return if (mobileNo != null && mobileNo.isNotEmpty())
            mobileNo.substring(0, 1).toInt() > 5
        else
            false
    }
}