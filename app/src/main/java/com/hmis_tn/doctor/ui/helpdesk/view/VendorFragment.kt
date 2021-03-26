package com.hmis_tn.doctor.ui.helpdesk.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.ActivityVendorTicketBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.helpdesk.model.*
import com.hmis_tn.doctor.ui.helpdesk.viewmodel.VendorTicketViewModel
import com.hmis_tn.doctor.ui.helpdesk.viewmodel.VendorTicketViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.util.*

class VendorFragment : Fragment() {
    var binding: ActivityVendorTicketBinding? = null
    private var viewModel: VendorTicketViewModel? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var facility_id: Int? = 0
    private var department_id: Int? = 0

    private var categorylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var FilterCategoryResponseMap = mutableMapOf<Int, String>()
    private var selectedCategoryid: Int = 0

    private var prioritylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var FilterPriorityResponseMap = mutableMapOf<Int, String>()
    private var selectedPriorityid: Int = 0

    private var subCategorylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var FilterSubCategoryResponseMap = mutableMapOf<Int, String>()
    private var selectedSubCategoryid: Int = 0

    private var autocompleteVendorResponse: List<VendorResponseContent?>? = null

    private var selectedVendorUUID: Int = 0
    private var selectedAssignTo: Int = 0
    private var selectedVendorEmail: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_vendor_ticket,
                container,
                false
            )


        viewModel = VendorTicketViewModelFactory(
            requireActivity().application
        )
            .create(VendorTicketViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())

        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        department_id = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!

        binding?.categorySpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedCategoryid = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if (pos == 0) {
                        selectedCategoryid = 0
                    } else {
                        selectedCategoryid = 0
                        selectedCategoryid =
                            FilterCategoryResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected Category Id", selectedCategoryid.toString())

                        viewModel?.getSubCategoryList(
                            selectedCategoryid,
                            subCategorySpinnerRetrofitCallback
                        )
                    }

                }
            }

        binding?.prioritySpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedPriorityid = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if (pos == 0) {
                        selectedPriorityid = 0
                    } else {
                        selectedPriorityid = 0
                        selectedPriorityid =
                            FilterPriorityResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected Priority Id", selectedPriorityid.toString())
                    }
                }
            }

        binding?.subcategorySpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedSubCategoryid = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if (pos == 0) {
                        selectedSubCategoryid = 0
                    } else {
                        selectedSubCategoryid = 0
                        selectedSubCategoryid =
                            FilterSubCategoryResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected SubCategory Id", selectedSubCategoryid.toString())
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
            selectedVendorEmail = autocompleteVendorResponse?.get(position)?.email_id!!
            binding?.autoCompleteTextViewAssignTo?.dismissDropDown()

        }

        binding?.clear?.setOnClickListener { clearSearch() }

        binding?.save?.setOnClickListener {

            if (binding?.edtMobileNumber!!.text.trim().toString().isEmpty()) {
                binding?.edtMobileNumber!!.error = "Enter Vendor Number"
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

            if (selectedCategoryid == 0) {
                (binding?.categorySpinner?.selectedView as TextView).error = "Select Category"
                return@setOnClickListener
            }
            if (selectedSubCategoryid == 0) {
                (binding?.subcategorySpinner?.selectedView as TextView).error =
                    "Select Sub Category"
                return@setOnClickListener
            }
            if (selectedPriorityid == 0) {
                (binding?.prioritySpinner?.selectedView as TextView).error = "Select Priority"
                return@setOnClickListener
            }

            var model = AddTicketRequestModel()
            model.subject = binding?.edtSubject?.text.toString()
            model.problem_description = binding?.edtDescription?.text.toString()
            model.user_type_uuid = 3
            model.mobile = binding?.edtMobileNumber?.text.toString()
            model.assignto = selectedAssignTo
            model.category_uuid = selectedCategoryid
            model.subcategory_uuid = selectedSubCategoryid
            model.priority = selectedPriorityid
            model.vendor_uuid = selectedVendorUUID
            model.vendorMail = selectedVendorEmail
            model.ticketstatus_uuid = 1
            model.department_uuid = department_id
            model.application_user_uuid = userDetailsRoomRepository?.getUserDetails()?.uuid!!
            model.facility_uuid = facility_id
            model.created_by = userDetailsRoomRepository?.getUserDetails()?.uuid!!


            var requestModel = Gson().toJson(model)
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
                    viewModel?.getVendorByMobile(
                        s.toString(),
                        vendorByMobileResponseRetrofitCallback
                    )
                } else {
                    binding?.vendorDetails?.text = "Vendor Name / Mobile Number"
                }

            }
        })


        viewModel?.getCategoryList(categorySpinnerRetrofitCallback)

        return binding!!.root
    }

    private fun clearSearch() {

        binding?.categorySpinner?.setSelection(0)
        binding?.subcategorySpinner?.setSelection(0)
        binding?.prioritySpinner?.setSelection(0)

        binding?.edtDescription?.setText("")
        binding?.edtSubject?.setText("")
        binding?.edtMobileNumber?.setText("")
        binding?.autoCompleteTextViewAssignTo?.setText("")

    }

    val categorySpinnerRetrofitCallback =
        object : RetrofitCallback<CategoryListResponseModel> {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)

                categorylistfilteritem?.add(CategoryResponseContent())
                categorylistfilteritem?.addAll(response.body()?.responseContents!!)

                FilterCategoryResponseMap =
                    categorylistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterCategoryResponseMap.put(0, "Category")
                try {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            FilterCategoryResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.categorySpinner!!.adapter = adapter
                } catch (e: Exception) {

                }

                binding?.categorySpinner?.prompt = categorylistfilteritem?.get(0)?.name
                binding?.categorySpinner!!.setSelection(0)

                viewModel?.getSubCategoryList(
                    selectedCategoryid,
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

                FilterPriorityResponseMap =
                    prioritylistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterPriorityResponseMap.put(0, "Priority")
                try {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            FilterPriorityResponseMap.values.toMutableList()
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

                subCategorylistfilteritem?.clear()
                subCategorylistfilteritem?.add(CategoryResponseContent())
                subCategorylistfilteritem?.addAll(response.body()?.responseContents!!)

                FilterSubCategoryResponseMap.clear()
                FilterSubCategoryResponseMap =
                    subCategorylistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterSubCategoryResponseMap.put(0, "Sub Category")
                try {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            FilterSubCategoryResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.subcategorySpinner!!.adapter = adapter
                } catch (e: Exception) {

                }

                binding?.subcategorySpinner?.prompt = subCategorylistfilteritem?.get(0)?.name
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
                var adapter = VendorSearchResultAdapter(
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
        object : RetrofitCallback<VendorByMobileResponseModel> {

            override fun onSuccessfulResponse(response: Response<VendorByMobileResponseModel>) {
                Log.e("Vendor Mobile Response", "" + response.body()?.responseContents)
                var vendor_name = response.body()?.responseContents?.vendor_name
                var mobile_number = response.body()?.responseContents?.mobile_number
                var vendor_type = response.body()?.responseContents?.vendor_type?.name
                selectedVendorUUID = response.body()?.responseContents?.uuid!!

                binding?.vendorDetails?.text = vendor_name + " / " + vendor_type + " / " + mobile_number
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