package com.hmis_tn.doctor.ui.institution.rmis.view.fragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogSelectInstituteRadiologyBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.institution.lmis.model.LocationMaster
import com.hmis_tn.doctor.ui.institution.lmis.model.LocationMasterResponseModel
import com.hmis_tn.doctor.ui.institution.lmis.view.adapter.SelectInstituteDropDownAdapter
import com.hmis_tn.doctor.ui.institution.rmis.view.adapter.RadiologyDropDownAdapter
import com.hmis_tn.doctor.ui.institution.viewModel.InstituteViewModel
import com.hmis_tn.doctor.ui.login.model.institution_response.InstitutionResponseModel
import com.hmis_tn.doctor.ui.login.model.institution_response.InstitutionresponseContent
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response


class RadiologyInstituteDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var labuuid: Int? = null
    var otherdepaertment: ArrayList<Int> = ArrayList()
    private var institution_NAME: String? = null
    private var content: String? = null
    private var viewModel: InstituteViewModel? = null
    var binding: DialogSelectInstituteRadiologyBinding? = null
    private var utils: Utils? = null

    private var institutionDropDownAdapter: SelectInstituteDropDownAdapter? = null
    private var departmentDropDownAdapter: RadiologyDropDownAdapter? = null

    private var arraylist_institution: ArrayList<InstitutionresponseContent?> = ArrayList()
    private var arraylist_department: ArrayList<LocationMaster?> = ArrayList()

    var appPreferences: AppPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_select_institute, container, false)

        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelBinding()
        initView()
        defaultData()
        ClearData()
        spinnerView()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun spinnerView() {

        binding?.spinnerInstitution!!.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->

                    viewModel!!.getfacilityCallback(facilitycallbackRetrofitCallback)

            }
            v?.onTouchEvent(event) ?: true
        }

        binding?.spinnerInstitution?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != institutionDropDownAdapter?.count!!) {
                    val institutionListGetDetails = institutionDropDownAdapter?.getlistDetails()

                    facilitylevelID = institutionListGetDetails?.get(position)?.facility_uuid
                    institution_NAME = institutionListGetDetails?.get(position)?.facility?.name

                    if (facilitylevelID != 0) {

                        viewModel?.getLocationMaster(facilitylevelID!!, departmentRetrofitCallBack)
                    }
                    return
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        binding?.spinnerDeparment?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val departmentListGetDetails = departmentDropDownAdapter?.getlistDetails()

                department_uuid = departmentListGetDetails?.get(position)?.department_uuid



                labuuid = departmentListGetDetails?.get(position)!!.uuid


                var tolocationMap =
                    departmentListGetDetails.get(position)!!.to_location_department_maps



                if (tolocationMap.isNotEmpty()) {

                    otherdepaertment.clear()

                    for (i in tolocationMap.indices) {

                        otherdepaertment.add(tolocationMap[i].department_uuid)

                    }

                    val res = otherdepaertment.toString()

                    Log.i("department", "" + res)


                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    private fun defaultData() {

        utils = Utils(requireContext())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)


    }

    private fun initView() {

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.listName?.setText(R.string.Lab)
        binding?.clear?.setOnClickListener {
            ClearData()
        }
        binding?.save?.setOnClickListener {
            if (facilitylevelID != 0 && labuuid != 0) {

                appPreferences?.saveInt(AppConstants.FACILITY_UUID, facilitylevelID!!)
                appPreferences?.saveString(AppConstants.INSTITUTION_NAME, institution_NAME!!)

                appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, department_uuid!!)
                appPreferences?.saveInt(
                    AppConstants.LAB_UUID,
                    labuuid!!
                )
                appPreferences?.saveString(
                    AppConstants.OTHER_DEPARTMENT_UUID,
                    otherdepaertment.toString()
                )

                // startActivity(Intent(context, MainLandScreenActivity::class.java))
                //requireActivity().finish()
            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.empty_item)
                )
            }
        }
        viewModel!!.getfacilityCallback(facilitycallbackRetrofitCallback)

    }

    private fun viewModelBinding() {

        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(InstituteViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

    }

    private fun ClearData() {

        arraylist_institution.clear()
        institutionDropDownAdapter = SelectInstituteDropDownAdapter(requireContext(), ArrayList())
        arraylist_institution.add(InstitutionresponseContent())
        institutionDropDownAdapter?.setInstitutionListDetails(arraylist_institution)
        binding?.spinnerInstitution?.adapter = institutionDropDownAdapter

        arraylist_department.clear()
        departmentDropDownAdapter = RadiologyDropDownAdapter(requireContext(), ArrayList())
        arraylist_department.add(LocationMaster())
        departmentDropDownAdapter?.setDepatmentListDetails(arraylist_department)
        binding?.spinnerDeparment?.adapter = departmentDropDownAdapter

        facilitylevelID = 0
        institution_NAME = ""

        labuuid = 0


    }

    val facilitycallbackRetrofitCallback = object :
        RetrofitCallback<InstitutionResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<InstitutionResponseModel?>) {

            Log.i("", "" + responseBody.body()?.responseContents)
            Log.i("", "" + responseBody.body()?.responseContents)
            Log.i("", "" + responseBody.body()?.responseContents)

            institutionDropDownAdapter?.setInstitutionListDetails(responseBody.body()?.responseContents as ArrayList<InstitutionresponseContent?>?)
            binding?.spinnerInstitution?.adapter = institutionDropDownAdapter


        }

        override fun onBadRequest(errorBody: Response<InstitutionResponseModel?>) {

            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
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

        override fun onFailure(s: String?) {
            if (s != null) {

                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    val departmentRetrofitCallBack =
        object : RetrofitCallback<LocationMasterResponseModel> {
            override fun onSuccessfulResponse(response: Response<LocationMasterResponseModel?>) {
                Log.i("", "" + response.body())
                val data = response.body()!!.responseContents
                if (data.isNotEmpty()) {

                    setward(response.body()?.responseContents)

                } else {
                    Toast.makeText(
                        context,
                        "No Radiolgy Mapped to this department",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onBadRequest(errorBody: Response<LocationMasterResponseModel?>) {
                val gson = GsonBuilder().create()
                val responseModel: LocationMasterResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody.errorBody()!!.string(),
                        LocationMasterResponseModel::class.java
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

            override fun onFailure(s: String?) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, s!!)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun setward(responseContents: List<LocationMaster>?) {

        departmentDropDownAdapter?.setDepatmentListDetails(responseContents as ArrayList<LocationMaster?>?)
        binding?.spinnerDeparment?.adapter = departmentDropDownAdapter


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
            }
        }
    }


}


