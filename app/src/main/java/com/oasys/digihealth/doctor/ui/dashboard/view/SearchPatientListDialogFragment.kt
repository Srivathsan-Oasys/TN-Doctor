package com.oasys.digihealth.doctor.ui.dashboard.view


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogCovidSearchListBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.dashboard.model.registration.CovidRegistrationSearchResponseContent
import com.oasys.digihealth.doctor.ui.dashboard.model.registration.CovidRegistrationSearchResponseModel
import com.oasys.digihealth.doctor.ui.dashboard.view_model.CovidRegistrationViewModel
import com.oasys.digihealth.doctor.ui.dashboard.view_model.CovidRegistrationViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response


class SearchPatientListDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var office_UUID: Int? = null
    private var content: String? = null
    private var name: String? = null
    private var viewModel: CovidRegistrationViewModel? = null
    private var autocompleteNameTestResponse: List<CovidRegistrationSearchResponseContent>? = null
    private var searchListAdapter: SearchPatientListAdapter? = null

    private var Str_auto_id: Int? = 0
    var binding: DialogCovidSearchListBinding? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    var dialogListener: DialogListener? = null
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
            DataBindingUtil.inflate(inflater, R.layout.dialog_covid_search_list, container, false)
        viewModel = CovidRegistrationViewModelFactory(
            requireActivity().application
        )
            .create(CovidRegistrationViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        office_UUID = appPreferences?.getInt(AppConstants.OFFICE_UUID)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        searchListAdapter =
            SearchPatientListAdapter(
                requireActivity(),
                ArrayList()
            )

        binding?.searchListRecyclerView!!.adapter = searchListAdapter


        val args = arguments
        if (args == null) {

            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            val Str_MobileNumber = args.getString(AppConstants.SEARCHKEYMOBILE)
            val Str_PinNumber: String? = args.getString(AppConstants.SEARCHKEYPIN)
            val Str_name: String? = args.getString(AppConstants.SEARCHNAME)
            viewModel?.getAllSearchCovidPatientList(
                Str_MobileNumber,
                Str_PinNumber,
                CovidSearchNameCallBack,
                Str_name
            )
        }

        searchListAdapter!!.setOnItemClickListener(object :
            SearchPatientListAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: CovidRegistrationSearchResponseContent?,
                position: Int
            ) {

                val dialogListener = activity as DialogListener?
                dialogListener!!.onFinishEditDialog(responseContent)
                dismiss()
            }
        })

        return binding?.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
            }
        }
    }

    val CovidSearchNameCallBack = object : RetrofitCallback<CovidRegistrationSearchResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CovidRegistrationSearchResponseModel>?) {
            autocompleteNameTestResponse = responseBody?.body()?.responseContents
            searchListAdapter?.setData(responseBody?.body()?.responseContents)
        }

        override fun onBadRequest(errorBody: Response<CovidRegistrationSearchResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: CovidRegistrationSearchResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    CovidRegistrationSearchResponseModel::class.java
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


    interface DialogListener {
        fun onFinishEditDialog(inputText: CovidRegistrationSearchResponseContent?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try { /* this line is main difference for fragment to fragment communication & fragment to activity communication
            fragment to fragment: onInputListener = (OnInputListener) getTargetFragment();
            fragment to activity: onInputListener = (OnInputListener) getActivity();
             */
            dialogListener = targetFragment as DialogListener?

        } catch (e: ClassCastException) {

        }
    }

}


