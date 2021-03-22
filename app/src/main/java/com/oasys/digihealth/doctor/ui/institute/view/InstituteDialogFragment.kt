package com.oasys.digihealth.doctor.ui.institute.view


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogInstituteListBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.oasys.digihealth.doctor.ui.home.HomeActivity
import com.oasys.digihealth.doctor.ui.institute.model.*
import com.oasys.digihealth.doctor.ui.institute.view_model.InstituteViewModel
import com.oasys.digihealth.doctor.ui.institute.view_model.InstituteViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class InstituteDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var office_UUID: Int? = null
    private var institution_NAME: String? = null
    private var content: String? = null
    private var viewModel: InstituteViewModel? = null
    var binding: DialogInstituteListBinding? = null
    private var utils: Utils? = null
    private var officeDropDownAdapter: OfficeDropDownAdapter? = null
    private var institutionDropDownAdapter: InstitutionDropDownAdapter? = null
    private var departmentDropDownAdapter: DepartmentDropDownAdapter? = null
    private var arraylist_institution: ArrayList<InstitutionResponseContent?> = ArrayList()
    private var arraylist_department: ArrayList<DepartmentResponseContent?> = ArrayList()
    private var arraylist_office: ArrayList<OfficeResponseContent?> = ArrayList()
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
            DataBindingUtil.inflate(inflater, R.layout.dialog_institute_list, container, false)
        viewModel = InstituteViewModelFactory(
            requireActivity().application,
            officeRetrofitCallBack
        )
            .create(InstituteViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        office_UUID = appPreferences?.getInt(AppConstants.OFFICE_UUID)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        ClearData()
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.clear?.setOnClickListener {
            ClearData()
        }
        binding?.save?.setOnClickListener {
            if (office_UUID != 0 && department_uuid != 0 && facilitylevelID != 0) {
                /*         utils?.showToast(
                             R.color.positiveToast,
                             binding?.mainLayout!!,
                             getString(R.string.data_save)
                         )*/
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
        binding?.spinnerOfficelist?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != officeDropDownAdapter?.count!!) {

                    val officeListGetDetails = officeDropDownAdapter?.getlistDetails()
                    office_UUID = officeListGetDetails?.get(position)?.health_office?.uuid

                    appPreferences?.saveInt(AppConstants.OFFICE_UUID, office_UUID!!)

                    if (office_UUID != 0) {
                        viewModel?.getInstitutionList(office_UUID, instituteRetrofitCallBack)
                    }
                    return
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

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

                    facilitylevelID = institutionListGetDetails?.get(position)?.uuid
                    institution_NAME = institutionListGetDetails?.get(position)?.name
                    appPreferences?.saveInt(AppConstants.FACILITY_UUID, facilitylevelID!!)
                    appPreferences?.saveString(AppConstants.INSTITUTION_NAME, institution_NAME!!)
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
                appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, department_uuid!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        return binding?.root
    }

    private fun ClearData() {
        arraylist_office.clear()
        officeDropDownAdapter = OfficeDropDownAdapter(requireContext(), ArrayList())
        arraylist_office.add(OfficeResponseContent())
        officeDropDownAdapter?.setOfficeListDetails(arraylist_office)
        binding?.spinnerOfficelist?.adapter = officeDropDownAdapter

        arraylist_institution.clear()
        institutionDropDownAdapter = InstitutionDropDownAdapter(requireContext(), ArrayList())
        arraylist_institution.add(InstitutionResponseContent())
        institutionDropDownAdapter?.setInstitutionListDetails(arraylist_institution)
        binding?.spinnerInstitution?.adapter = institutionDropDownAdapter

        arraylist_department.clear()
        departmentDropDownAdapter = DepartmentDropDownAdapter(requireContext(), ArrayList())
        arraylist_department.add(DepartmentResponseContent())
        departmentDropDownAdapter?.setDepatmentListDetails(arraylist_department)
        binding?.spinnerDeparment?.adapter = departmentDropDownAdapter

        appPreferences?.saveInt(AppConstants.OFFICE_UUID, 0)
        appPreferences?.saveString(AppConstants.OFFICE_NAME, "")
        appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, 0)
        appPreferences?.saveInt(AppConstants.FACILITY_UUID, 0)

        viewModel?.getOfficeList()
    }

    val officeRetrofitCallBack =
        object : RetrofitCallback<OfficeResponseModel> {
            override fun onSuccessfulResponse(response: Response<OfficeResponseModel>) {
                Log.i("", "" + response.body())
                if (response.body()?.responseContents!!.isNotEmpty()) {
                    officeDropDownAdapter?.setOfficeListDetails(response.body()?.responseContents as ArrayList<OfficeResponseContent?>?)
                    binding?.spinnerOfficelist?.adapter = officeDropDownAdapter
                    binding?.spinnerOfficelist?.setSelection(officeDropDownAdapter?.count!!)
                }

            }

            override fun onBadRequest(response: Response<OfficeResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FavouritesResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavouritesResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.message!!
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


    val instituteRetrofitCallBack =
        object : RetrofitCallback<InstitutionResponseModel> {
            override fun onSuccessfulResponse(response: Response<InstitutionResponseModel>) {

                Log.i("Instection", "" + response.body()?.responseContents)
                Log.i("Instection", "" + response.body()?.responseContents)
                Log.i("Instection", "" + response.body()?.responseContents)


                institutionDropDownAdapter?.setInstitutionListDetails(response.body()?.responseContents)
                binding?.spinnerInstitution?.adapter = institutionDropDownAdapter
            }

            override fun onBadRequest(response: Response<InstitutionResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FavouritesResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavouritesResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.message!!
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

    val departmentRetrofitCallBack =
        object : RetrofitCallback<DepartmentResponseModel> {
            override fun onSuccessfulResponse(response: Response<DepartmentResponseModel>) {
                Log.i("", "" + response.body())
                departmentDropDownAdapter?.setDepatmentListDetails(response.body()?.responseContents as ArrayList<DepartmentResponseContent?>?)
                binding?.spinnerDeparment?.adapter = departmentDropDownAdapter
            }

            override fun onBadRequest(response: Response<DepartmentResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FavouritesResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavouritesResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.message!!
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
            }
        }
    }
}