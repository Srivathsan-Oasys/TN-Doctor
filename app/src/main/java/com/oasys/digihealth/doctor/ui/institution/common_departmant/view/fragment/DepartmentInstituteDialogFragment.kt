package com.oasys.digihealth.doctor.ui.institution.common_departmant.view.fragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogSelectInstituteBinding
import com.oasys.digihealth.doctor.ui.home.HomeActivity
import com.oasys.digihealth.doctor.ui.institution.common_departmant.model.DepartmentResponseContent
import com.oasys.digihealth.doctor.ui.institution.common_departmant.model.DepartmentResponseModel
import com.oasys.digihealth.doctor.ui.institution.common_departmant.view.adapter.DepartmentDropDownAdapter
import com.oasys.digihealth.doctor.ui.institution.lmis.view.adapter.SelectInstituteDropDownAdapter
import com.oasys.digihealth.doctor.ui.institution.viewModel.InstituteViewModel
import com.oasys.digihealth.doctor.ui.login.model.institution_response.InstitutionResponseModel
import com.oasys.digihealth.doctor.ui.login.model.institution_response.InstitutionresponseContent
import com.oasys.digihealth.doctor.utils.Utils

import retrofit2.Response


class DepartmentInstituteDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var office_UUID: Int? = null
    private var institution_NAME: String? = null
    private var content: String? = null
    private var departmentname: String? = ""
    private var viewModel: InstituteViewModel? = null
    var binding: DialogSelectInstituteBinding? = null
    private var utils: Utils? = null

    private var institutionDropDownAdapter: SelectInstituteDropDownAdapter? = null
    private var departmentDropDownAdapter: DepartmentDropDownAdapter? = null
    private var arraylist_institution: ArrayList<InstitutionresponseContent?> = ArrayList()
    private var arraylist_department: ArrayList<DepartmentResponseContent?> = ArrayList()

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
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_select_institute,
                container,
                false
            )

        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModelBinding()
        defaultData()
        ClearData()
        initView()
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
                        viewModel?.getDepartmentList(facilitylevelID, departmentRetrofitCallBack)
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
                department_uuid =
                    departmentListGetDetails?.get(position)?.department_uuid

                departmentname = departmentListGetDetails?.get(position)?.department?.name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun initView() {
        binding?.listName?.text = getString(R.string.department_title)
        viewModel!!.getfacilityCallback(facilitycallbackRetrofitCallback)
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.clear?.setOnClickListener {
            ClearData()
        }

        binding?.save?.setOnClickListener {
            if (department_uuid != 0 && facilitylevelID != 0) {
                /*  utils?.showToast(
                      R.color.positiveToast,
                      binding?.mainLayout!!,
                      getString(R.string.data_save)
                  )*/

                appPreferences?.saveInt(AppConstants.FACILITY_UUID, facilitylevelID!!)
                appPreferences?.saveString(AppConstants.INSTITUTION_NAME, institution_NAME!!)
                appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, department_uuid!!)
                appPreferences?.saveString(AppConstants.DEPARTMENT_NAME, departmentname!!)

                startActivity(Intent(context, HomeActivity::class.java))
                requireActivity().finish()
            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.empty_item)
                )
            }
        }
    }

    private fun defaultData() {
        utils = Utils(requireContext())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        office_UUID = appPreferences?.getInt(AppConstants.OFFICE_UUID)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
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
        departmentDropDownAdapter = DepartmentDropDownAdapter(requireContext(), ArrayList())
        arraylist_department.add(DepartmentResponseContent())
        departmentDropDownAdapter?.setDepatmentListDetails(arraylist_department)
        binding?.spinnerDeparment?.adapter = departmentDropDownAdapter

        facilitylevelID = 0
        institution_NAME = ""
        department_uuid = 0
        departmentname = ""
    }

    val facilitycallbackRetrofitCallback =
        object : RetrofitCallback<InstitutionResponseModel> {

            override fun onSuccessfulResponse(responseBody: Response<InstitutionResponseModel?>) {
                Log.i("", "" + responseBody.body()?.responseContents)
                Log.i("", "" + responseBody.body()?.responseContents)
                Log.i("", "" + responseBody.body()?.responseContents)
                setInstution(responseBody.body()?.responseContents as ArrayList<InstitutionresponseContent?>?)
            }

            override fun onBadRequest(errorBody: Response<InstitutionResponseModel?>) {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!, "Bad Request")
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onServerError(response: Response<*>?) {
//                AnalyticsManager.getAnalyticsManager()
//                    .trackLoginFailed(context!!, getString(R.string.something_went_wrong))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!, getString(R.string.unauthorized))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!, getString(R.string.something_went_wrong))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(s: String?) {
                if (s != null) {
                    try {
//                        AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!, s)
                        utils?.showToast(
                            R.color.negativeToast,
                            binding?.mainLayout!!,
                            s
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun setInstution(arrayList: ArrayList<InstitutionresponseContent?>?) {
        institutionDropDownAdapter?.setInstitutionListDetails(arrayList)
        binding?.spinnerInstitution?.adapter = institutionDropDownAdapter
        if (facilitylevelID != 0) {
            for (i in arrayList!!.indices) {
                if (arrayList[i]?.facility_uuid == facilitylevelID) {
                    binding?.spinnerInstitution?.setSelection(i)
                    break
                }
            }
        }
    }

    val departmentRetrofitCallBack =
        object : RetrofitCallback<DepartmentResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<DepartmentResponseModel?>) {
                setDepartment(responseBody.body()?.responseContents as ArrayList<DepartmentResponseContent?>?)
            }

            override fun onBadRequest(errorBody: Response<DepartmentResponseModel?>) {
                val gson = GsonBuilder().create()
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
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, s!!)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun setDepartment(arrayList: ArrayList<DepartmentResponseContent?>?) {
        departmentDropDownAdapter?.setDepatmentListDetails(arrayList)
        binding?.spinnerDeparment?.adapter = departmentDropDownAdapter

        if (department_uuid != 0) {
            for (i in arrayList!!.indices) {
                if (arrayList[i]?.department_uuid == department_uuid) {
                    binding?.spinnerDeparment?.setSelection(i)
                    break
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
            }
        }
    }
}
