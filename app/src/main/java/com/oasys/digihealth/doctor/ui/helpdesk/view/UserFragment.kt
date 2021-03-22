package com.oasys.digihealth.doctor.ui.helpdesk.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.ActivityUserTicketBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.helpdesk.model.*
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.UserTicketViewModel
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.UserTicketViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.util.*

class UserFragment : Fragment() {

    var binding: ActivityUserTicketBinding? = null
    private var viewModel: UserTicketViewModel? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var facility_id: Int? = 0

    private var categorylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var filterCategoryResponseMap = mutableMapOf<Int, String>()
    private var selectedCategoryId: Int = 0

    private var prioritylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var filterPriorityResponseMap = mutableMapOf<Int, String>()
    private var selectedPriorityId: Int = 0

    private var subCategoryListFilterItem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var filterSubCategoryResponseMap = mutableMapOf<Int, String>()
    private var selectedSubCategoryId: Int = 0

    private var autocompleteVendorResponse: List<VendorResponseContent?>? = null

    private var autocompleteTestResponse: List<AssetResponseContent?>? = null
    private var selectedAssetUUID: Int? = 0
    private var selectedFacilityUUID: Int? = 0
    private var selectedDepartmentUUID: Int? = 0
    private var selectedAssetCode: String? = null

    private var filterInstitutionResponseMap = mutableMapOf<Int, String>()
    private var listfilteritem: ArrayList<TicketInstitutionResponseContent?>? = ArrayList()
    private var selectedInstitutionId: Int = 0

    private var departmenrListFilterItem: ArrayList<DepartmentResponseContent?>? = ArrayList()
    private var selectedDepartmentId: Int = 0
    private var filterDepartmentResponseMap = mutableMapOf<Int, String>()

    private var selectedVendorUUID: Int = 0
    private var selectedAssignTo: Int = 0
    private var selectedVendorEmail: String = ""

    private var assetSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_user_ticket,
                container,
                false
            )

        viewModel = UserTicketViewModelFactory(
            requireActivity().application
        )
            .create(UserTicketViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel

        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )

        initViews()
        listeners()

        viewModel?.getInstitution(facility_id!!, labInstitutionSpinnerRetrofitCallback)

        return binding!!.root
    }

    private fun initViews() {
        assetSelected = false
        utils = Utils(requireContext())

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
    }

    private fun listeners() {
        binding?.categorySpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedCategoryId = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if (pos == 0) {
                        selectedCategoryId = 0
                    } else {
                        selectedCategoryId = 0
                        selectedCategoryId =
                            filterCategoryResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected Category Id", selectedCategoryId.toString())

                        viewModel?.getSubCategoryList(
                            selectedCategoryId,
                            subCategorySpinnerRetrofitCallback
                        )
                    }
                }
            }

        binding?.prioritySpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedPriorityId = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if (pos == 0) {
                        selectedPriorityId = 0
                    } else {
                        selectedPriorityId = 0
                        selectedPriorityId =
                            filterPriorityResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected Priority Id", selectedPriorityId.toString())
                    }
                }
            }

        binding?.subcategorySpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedSubCategoryId = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if (pos == 0) {
                        selectedSubCategoryId = 0
                    } else {
                        selectedSubCategoryId = 0
                        selectedSubCategoryId =
                            filterSubCategoryResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected SubCategory Id", selectedSubCategoryId.toString())
                    }
                }
            }

        binding?.autoCompleteTextViewAssignTo?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    viewModel?.getVendorList(s.toString(), vendorResponseRetrofitCallback)
                }
            }
        })

        binding?.autoCompleteTextViewAssignTo!!.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteTextViewAssignTo?.setText(autocompleteVendorResponse?.get(position)?.vendor_name)
            selectedAssignTo = autocompleteVendorResponse?.get(position)?.uuid!!
            binding?.autoCompleteTextViewAssignTo?.dismissDropDown()
        }

        binding?.clear?.setOnClickListener { clearSearch() }

        binding?.save?.setOnClickListener {
            if (binding?.edtMobileNumber!!.text.trim().toString().isEmpty()) {
                binding?.edtMobileNumber!!.error = "Enter User Number"
                return@setOnClickListener
            }

            if (!assetSelected) {
                binding?.autoCompleteTextViewAssetId!!.error = "Enter Asset Id"
                return@setOnClickListener
            }

            if (binding?.edtSubject!!.text.trim().toString().isEmpty()) {
                binding?.edtSubject!!.error = "Enter Subject"
                return@setOnClickListener
            }

            if (binding?.edtDescription!!.text.trim().toString().isEmpty()) {
                binding?.edtDescription!!.error = "Enter Problem description"
                return@setOnClickListener
            }

            if (selectedAssignTo == 0) {
                binding?.autoCompleteTextViewAssignTo!!.error = "Enter Assign To"
                return@setOnClickListener
            }

            if (selectedCategoryId == 0) {
                (binding?.categorySpinner?.selectedView as TextView).error = "Select Category"
                return@setOnClickListener
            }
            if (selectedSubCategoryId == 0) {
                (binding?.subcategorySpinner?.selectedView as TextView).error =
                    "Select Sub Category"
                return@setOnClickListener
            }
            if (selectedPriorityId == 0) {
                (binding?.prioritySpinner?.selectedView as TextView).error = "Select Priority"
                return@setOnClickListener
            }

            val model = AddTicketRequestModel()
            model.subject = binding?.edtSubject?.text.toString()
            model.problem_description = binding?.edtDescription?.text.toString()
            model.user_type_uuid = 2
            model.mobile = binding?.edtMobileNumber?.text.toString()
            model.assignto = selectedAssignTo
            model.category_uuid = selectedCategoryId
            model.subcategory_uuid = selectedSubCategoryId
            model.priority = selectedPriorityId
            model.vendor_uuid = selectedVendorUUID
            model.vendorMail = selectedVendorEmail
            model.ticketstatus_uuid = 1
            model.assest_uuid = selectedAssetUUID
            model.make = binding?.edtMake?.text.toString()
            model.model = binding?.edtModel?.text.toString()
            model.serial = binding?.edtSerialNumber?.text.toString()
            model.application_user_uuid = userDetailsRoomRepository?.getUserDetails()?.uuid!!
            model.facility_uuid = selectedFacilityUUID
            model.department_uuid = selectedDepartmentUUID
            model.created_by = userDetailsRoomRepository?.getUserDetails()?.uuid!!


            val requestModel = Gson().toJson(model)
            Log.e("requestModel", requestModel)

            viewModel?.saveVendor(model, saveVendorResponseRetrofitCallback)
        }


        binding?.edtMobileNumber?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length == 10) {
                    viewModel?.getUserProfile(
                        s.toString(),
                        vendorByMobileResponseRetrofitCallback
                    )
                } else {
                    binding?.vendorDetails?.text = "Vendor Name / Mobile Number"
                }
            }
        })

        binding?.autoCompleteTextViewAssetId?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    Log.e("asset :", s.toString())
                    val jsonBody = JSONObject()
                    try {
                        jsonBody.put("codename", s.toString())
                        jsonBody.put("facility_uuid", facility_id.toString())
                        jsonBody.put("department_uuid", selectedDepartmentId)
                        jsonBody.put("pageNo", 0)
                        jsonBody.put("paginationSize", 10)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    viewModel?.getAssetCode(jsonBody, assetResponseRetrofitCallback)
                }
            }
        })

        binding?.autoCompleteTextViewAssetId!!.setOnItemClickListener { parent, _, position, id ->
            assetSelected = true
            autocompleteTestResponse?.let { list ->
                if (list.size > position) {
                    list[position]?.let { assetResponseContent ->
                        binding?.autoCompleteTextViewAssetId?.setText(assetResponseContent.asset_name)
                        assetResponseContent.AssetDetails?.let { list ->
                            if (list.isNotEmpty()) {
                                list[0]?.let { assetDetailResponseContent ->
                                    binding?.edtModel?.setText(assetDetailResponseContent.model_no)
                                    binding?.edtMake?.setText(assetDetailResponseContent.model_name)
                                    binding?.edtSerialNumber?.setText(assetDetailResponseContent.serial_no)

                                    selectedAssetUUID = assetDetailResponseContent.uuid
                                }
                            }
                        }
                        selectedAssetCode = assetResponseContent.asset_code
                        selectedFacilityUUID = assetResponseContent.facility_uuid
                        selectedDepartmentUUID = assetResponseContent.department_uuid
                        binding?.autoCompleteTextViewAssetId?.dismissDropDown()
                    }
                }
            }
        }

        binding?.institutionSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedInstitutionId = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if (pos == 0) {
                        selectedInstitutionId = 0
                    } else {
                        selectedInstitutionId = 0
                        selectedInstitutionId =
                            filterInstitutionResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected insitution Id", selectedInstitutionId.toString())
                    }
                }
            }

        binding?.departmentSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedDepartmentId = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if (pos == 0) {
                        selectedDepartmentId = 0
                    } else {
                        selectedDepartmentId =
                            filterDepartmentResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected department Id", selectedDepartmentId.toString())
                    }
                }
            }
    }

    private fun clearSearch() {
        binding?.categorySpinner?.setSelection(0)
        binding?.subcategorySpinner?.setSelection(0)
        binding?.prioritySpinner?.setSelection(0)
        binding?.institutionSpinner?.setSelection(0)
        binding?.departmentSpinner?.setSelection(0)

        binding?.edtDescription?.setText("")
        binding?.edtSubject?.setText("")
        binding?.edtMobileNumber?.setText("")
        binding?.edtMake?.setText("")
        binding?.edtSerialNumber?.setText("")
        binding?.edtModel?.setText("")
        binding?.autoCompleteTextViewAssignTo?.setText("")
        binding?.autoCompleteTextViewAssetId?.setText("")
    }

    val categorySpinnerRetrofitCallback =
        object : RetrofitCallback<CategoryListResponseModel> {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)

                categorylistfilteritem?.add(CategoryResponseContent())
                categorylistfilteritem?.addAll(response.body()?.responseContents!!)

                filterCategoryResponseMap =
                    categorylistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                filterCategoryResponseMap.put(0, "Category")
                try {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            filterCategoryResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.categorySpinner!!.adapter = adapter
                } catch (e: Exception) {

                }

                binding?.categorySpinner?.prompt = categorylistfilteritem?.get(0)?.name
                binding?.categorySpinner!!.setSelection(0)

                viewModel?.getSubCategoryList(
                    selectedCategoryId,
                    subCategorySpinnerRetrofitCallback
                )

            }

            override fun onBadRequest(response: Response<CategoryListResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: CategoryListResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        CategoryListResponseModel::class.java
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


    val prioritySpinnerRetrofitCallback =
        object : RetrofitCallback<CategoryListResponseModel> {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)

                prioritylistfilteritem?.add(CategoryResponseContent())
                prioritylistfilteritem?.addAll(response.body()?.responseContents!!)

                filterPriorityResponseMap =
                    prioritylistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                filterPriorityResponseMap.put(0, "Priority")
                try {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            filterPriorityResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.prioritySpinner!!.adapter = adapter
                } catch (e: Exception) {

                }

                binding?.prioritySpinner?.prompt = prioritylistfilteritem?.get(0)?.name
                binding?.prioritySpinner!!.setSelection(0)

            }

            override fun onBadRequest(response: Response<CategoryListResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: CategoryListResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        CategoryListResponseModel::class.java
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

    val subCategorySpinnerRetrofitCallback =
        object : RetrofitCallback<CategoryListResponseModel> {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)

                subCategoryListFilterItem?.clear()
                subCategoryListFilterItem?.add(CategoryResponseContent())
                subCategoryListFilterItem?.addAll(response.body()?.responseContents!!)

                filterSubCategoryResponseMap.clear()
                filterSubCategoryResponseMap =
                    subCategoryListFilterItem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                filterSubCategoryResponseMap.put(0, "Sub Category")
                try {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            filterSubCategoryResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.subcategorySpinner!!.adapter = adapter
                } catch (e: Exception) {

                }

                binding?.subcategorySpinner?.prompt = subCategoryListFilterItem?.get(0)?.name
                binding?.subcategorySpinner!!.setSelection(0)

                viewModel?.getPriorityList(prioritySpinnerRetrofitCallback)

            }

            override fun onBadRequest(response: Response<CategoryListResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: CategoryListResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        CategoryListResponseModel::class.java
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


    val vendorResponseRetrofitCallback =
        object : RetrofitCallback<VendorListResponseModel> {

            override fun onSuccessfulResponse(response: Response<VendorListResponseModel>) {
                Log.e("Vendor Response", "" + response.body()?.responseContents)
                autocompleteVendorResponse = null
                autocompleteVendorResponse = response.body()?.responseContents
                Log.e("Vendor size", "" + response.body()?.responseContents?.size)
                val adapter = VendorSearchResultAdapter(
                    activity!!,
                    R.layout.row_chief_complaint_search_result,
                    response.body()?.responseContents!!
                )
                binding?.autoCompleteTextViewAssignTo?.threshold = 1
                binding?.autoCompleteTextViewAssignTo?.setAdapter(adapter)
                binding?.autoCompleteTextViewAssignTo?.showDropDown()
            }

            override fun onBadRequest(response: Response<VendorListResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: VendorListResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        VendorListResponseModel::class.java
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

    val vendorByMobileResponseRetrofitCallback =
        object : RetrofitCallback<VendorListResponseModel> {

            override fun onSuccessfulResponse(response: Response<VendorListResponseModel>) {
                Log.e("Vendor Mobile Response", "" + response.body()?.responseContents)
                var firstName = ""
                var age = ""
                var gender = ""
                var institution = ""
                var department = ""
                response.body()?.responseContents?.let { list ->
                    if (list.isNotEmpty()) {
                        list[0]?.let { vendorResponseContent ->
                            firstName = vendorResponseContent.first_name ?: ""
                            age = vendorResponseContent.age.toString()
                            gender = vendorResponseContent.gender?.name ?: ""
                            institution = vendorResponseContent.facility?.name ?: ""
                            department = vendorResponseContent.department?.name ?: ""

                            selectedVendorUUID = vendorResponseContent.uuid ?: 0
                            selectedVendorEmail = vendorResponseContent.email_id ?: ""
                        }
                    }
                }

                binding?.vendorDetails?.text =
                    "$firstName / $age (Years) - $gender / $institution / $department"
            }

            override fun onBadRequest(response: Response<VendorListResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: VendorListResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        VendorListResponseModel::class.java
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

    val assetResponseRetrofitCallback =
        object : RetrofitCallback<AssetResponseModel> {

            override fun onSuccessfulResponse(response: Response<AssetResponseModel>) {
                Log.e("assetResponseRetrofit", "" + response.body()?.responseContents)
                autocompleteTestResponse = null
                autocompleteTestResponse = response.body()?.responseContents!!
                val responseContentAdapter = AssetSearchResultAdapter(
                    context!!,
                    R.layout.row_chief_complaint_search_result,
                    response.body()?.responseContents!!
                )
                binding?.autoCompleteTextViewAssetId?.threshold = 1
                binding?.autoCompleteTextViewAssetId?.setAdapter(responseContentAdapter)
            }

            override fun onBadRequest(response: Response<AssetResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: AssetResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        AssetResponseModel::class.java
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

    val labInstitutionSpinnerRetrofitCallback =
        object : RetrofitCallback<TicketInstitutionResponseModel> {

            override fun onSuccessfulResponse(response: Response<TicketInstitutionResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)

                listfilteritem?.add(TicketInstitutionResponseContent())
                listfilteritem?.add(response.body()?.responseContents!!)

                filterInstitutionResponseMap =
                    listfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                filterInstitutionResponseMap.put(0, "Institution")
                try {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            filterInstitutionResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.institutionSpinner!!.adapter = adapter
                } catch (e: Exception) {

                }

                binding?.institutionSpinner?.prompt = listfilteritem?.get(0)?.name
                binding?.institutionSpinner!!.setSelection(1)

                viewModel?.getDepartment(facility_id!!, labDepartmentSpinnerRetrofitCallback)

            }

            override fun onBadRequest(response: Response<TicketInstitutionResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: TicketInstitutionResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        TicketInstitutionResponseModel::class.java
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

    val labDepartmentSpinnerRetrofitCallback =
        object : RetrofitCallback<DepartmentResponseModel> {

            override fun onSuccessfulResponse(response: Response<DepartmentResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)

                departmenrListFilterItem?.add(DepartmentResponseContent())
                departmenrListFilterItem?.addAll(response.body()?.responseContents!!)

                filterDepartmentResponseMap =
                    departmenrListFilterItem!!.filter { it?.uuid != null && it.department != null }
                        .map { it?.uuid!! to it.department?.name!! }.toMap().toMutableMap()
                filterDepartmentResponseMap.put(0, "Department")
                try {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            filterDepartmentResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.departmentSpinner!!.adapter = adapter
                    binding?.departmentSpinner?.setSelection(1)

                } catch (e: Exception) {
                }

                val jsonBody = JSONObject()
                try {
                    jsonBody.put("table_name", "help_desk_category")
                    jsonBody.put("searchColumn", "name")
                    jsonBody.put("searchDetail", "")
                    jsonBody.put("is_active", 1)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                viewModel?.getCategoryList(categorySpinnerRetrofitCallback)
            }

            override fun onBadRequest(response: Response<DepartmentResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: DepartmentResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        DepartmentResponseModel::class.java
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

    val saveVendorResponseRetrofitCallback =
        object : RetrofitCallback<VendorByMobileResponseModel> {

            override fun onSuccessfulResponse(response: Response<VendorByMobileResponseModel>) {
                Toast.makeText(context!!, response.body()?.msg, Toast.LENGTH_LONG).show()
                clearSearch()

                val op = AgentTicketListFragment()
                val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.landfragment, op)
                fragmentTransaction?.commit()

            }

            override fun onBadRequest(response: Response<VendorByMobileResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: VendorByMobileResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        VendorByMobileResponseModel::class.java
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