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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.ActivityPublicTicketBinding
import com.oasys.digihealth.doctor.ui.helpdesk.model.*
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.PublicTicketViewModel
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.PublicTicketViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.util.*


class PublicFragment : Fragment(), HelpDeskCallback {
    var binding: ActivityPublicTicketBinding? = null
    private var viewModel: PublicTicketViewModel? = null
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

    var gridLayoutManager: GridLayoutManager? = null
    private var publicListAdpater: PublicListAdapter? = null
    private var callBack: HelpDeskCallback? = null

    private var selectedAssignTo: Int = 0
    private var selectedOfficerMail: String = ""
    private var selectedOfficerName: String = ""
    private var selectedOfficerNo: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_public_ticket,
                container,
                false
            )


        viewModel = PublicTicketViewModelFactory(
            requireActivity().application
        )
            .create(PublicTicketViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        utils = Utils(requireContext())

        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        department_id = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!

        callBack = this

        binding.categorySpinner!!.onItemSelectedListener =
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

        binding.prioritySpinner!!.onItemSelectedListener =
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

        binding.subcategorySpinner!!.onItemSelectedListener =
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

        binding.autoCompleteTextViewAssignTo.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.length > 2) {
                    viewModel?.getUserProfile(s.toString(), vendorResponseRetrofitCallback)
                }

            }
        })

        binding.autoCompleteTextViewAssignTo!!.setOnItemClickListener { parent, _, position, id ->
            binding.autoCompleteTextViewAssignTo.setText(autocompleteVendorResponse?.get(position)?.first_name)
            selectedAssignTo = autocompleteVendorResponse?.get(position)?.uuid!!
            selectedOfficerMail = autocompleteVendorResponse?.get(position)?.email ?: ""
            selectedOfficerName = autocompleteVendorResponse?.get(position)?.first_name!!
            selectedOfficerNo = autocompleteVendorResponse?.get(position)?.mobile1!!
            binding.autoCompleteTextViewAssignTo.dismissDropDown()
        }

        binding.clear.setOnClickListener { clearSearch() }

        binding.save.setOnClickListener {


            if (binding.edtMobileNumber!!.text.trim().toString().isEmpty() &&
                binding.edtPin!!.text.trim().toString().isEmpty()
            ) {
                binding.edtMobileNumber!!.error = "Enter Patient Number"
                return@setOnClickListener
            }

            if (binding.edtSubject!!.text.trim().toString().isEmpty()) {
                binding.edtSubject!!.error = "Enter Subject"
                return@setOnClickListener
            }

            if (binding.edtDescription!!.text.trim().toString().isEmpty()) {
                binding.edtDescription!!.error = "Enter Problem description"
                return@setOnClickListener
            }

            if (selectedAssignTo == 0) {
                binding.autoCompleteTextViewAssignTo!!.error = "Enter Assign To"
                return@setOnClickListener
            }

            if (selectedCategoryid == 0) {
                (binding.categorySpinner.selectedView as TextView).error = "Select Category"
                return@setOnClickListener
            }
            if (selectedSubCategoryid == 0) {
                (binding.subcategorySpinner.selectedView as TextView).error =
                    "Select Sub Category"
                return@setOnClickListener
            }
            if (selectedPriorityid == 0) {
                (binding.prioritySpinner.selectedView as TextView).error = "Select Priority"
                return@setOnClickListener
            }

            var model = AddTicketRequestModel()
            model.subject = binding.edtSubject.text.toString()
            model.problem_description = binding.edtDescription.text.toString()
            model.user_type_uuid = 1
            model.mobile = binding.edtMobileNumber.text.toString()
            model.pin = binding.edtPin.text.toString()
            model.assignto = selectedAssignTo
            model.category_uuid = selectedCategoryid
            model.subcategory_uuid = selectedSubCategoryid
            model.priority = selectedPriorityid
            model.ticketstatus_uuid = 1
            model.application_user_uuid = userDetailsRoomRepository.getUserDetails().uuid!!
            model.facility_uuid = facility_id
            model.department_uuid = department_id
            model.created_by = userDetailsRoomRepository.getUserDetails().uuid!!
            model.grofficer_mail = selectedOfficerMail
            model.grofficer_name = selectedOfficerName
            model.grofficer_no = selectedOfficerNo


            var requestModel = Gson().toJson(model)
            Log.e("requestModel", requestModel)

            viewModel?.saveVendor(model, saveVendorResponseRetrofitCallback)

        }

        binding.edtMobileNumber.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.length == 10) {
                    viewModel?.getVendorByMobile(
                        s.toString(),
                        binding.edtPin.text.toString(),
                        vendorByMobileResponseRetrofitCallback
                    )
                } else {
                    binding.vendorDetails.text = "Vendor Name / Mobile Number"
                }

            }
        })

        binding.edtPin.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.length == 14) {
                    viewModel?.getVendorByMobile(
                        binding.edtMobileNumber.text.toString(),
                        s.toString(),
                        vendorByMobileResponseRetrofitCallback
                    )
                } else {
                    binding.vendorDetails.text = "Vendor Name / Mobile Number"
                }

            }
        })


        viewModel?.getCategoryList(categorySpinnerRetrofitCallback)

        return binding!!.root
    }

    private fun clearSearch() {

        binding.categorySpinner.setSelection(0)
        binding.subcategorySpinner.setSelection(0)
        binding.prioritySpinner.setSelection(0)

        binding.edtDescription.setText("")
        binding.edtSubject.setText("")
        binding.edtMobileNumber.setText("")
        binding.edtPin.setText("")
        binding.autoCompleteTextViewAssignTo.setText("")

    }

    val categorySpinnerRetrofitCallback =
        object {

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
                    binding.categorySpinner!!.adapter = adapter
                } catch (e: Exception) {

                }

                binding.categorySpinner.prompt = categorylistfilteritem?.get(0)?.name
                binding.categorySpinner!!.setSelection(0)

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
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    val prioritySpinnerRetrofitCallback =
        object {

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
                    binding.prioritySpinner!!.adapter = adapter
                } catch (e: Exception) {

                }

                binding.prioritySpinner.prompt = prioritylistfilteritem?.get(0)?.name
                binding.prioritySpinner!!.setSelection(0)

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
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    val subCategorySpinnerRetrofitCallback =
        object {

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
                    binding.subcategorySpinner!!.adapter = adapter
                } catch (e: Exception) {

                }

                binding.subcategorySpinner.prompt = subCategorylistfilteritem?.get(0)?.name
                binding.subcategorySpinner!!.setSelection(0)

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
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    val vendorResponseRetrofitCallback =
        object {

            override fun onSuccessfulResponse(response: Response<VendorListResponseModel>) {
                Log.e("Vendor Response", "" + response.body()?.responseContents)
                autocompleteVendorResponse = null
                autocompleteVendorResponse = response.body()?.responseContents
                Log.e("Vendor size", "" + response.body()?.responseContents?.size)
                var adapter = UserProfileSearchResultAdapter(
                    activity!!,
                    R.layout.row_chief_complaint_search_result,
                    response.body()?.responseContents!!
                )
                binding.autoCompleteTextViewAssignTo.threshold = 1
                binding.autoCompleteTextViewAssignTo.setAdapter(adapter)
                binding.autoCompleteTextViewAssignTo.showDropDown()
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
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    val vendorByMobileResponseRetrofitCallback =
        object {

            override fun onSuccessfulResponse(response: Response<VendorListResponseModel>) {
                Log.e("Vendor Mobile Response", "" + response.body()?.responseContents)
                var firstname = response.body()?.responseContents?.get(0)?.first_name
                var age = response.body()?.responseContents?.get(0)?.age.toString()
                var uhid = response.body()?.responseContents?.get(0)?.uhid
                var gender = response.body()?.responseContents?.get(0)?.gender_details?.name
                var mobileNumber = response.body()?.responseContents?.get(0)?.patient_detail?.mobile

                binding.vendorDetails.text =
                    firstname + " / " + age + " (Years) - " + gender + " / " + uhid + " / " + mobileNumber

                gridLayoutManager =
                    GridLayoutManager(context!!, 1, GridLayoutManager.VERTICAL, false)
                binding.publicRecyclerView!!.layoutManager = gridLayoutManager
                publicListAdpater = PublicListAdapter(context!!, ArrayList(), callBack!!)
                binding.publicRecyclerView!!.adapter = publicListAdpater

                publicListAdpater?.clearAll()
                publicListAdpater!!.addAll(response.body()!!.responseContents?.get(0)?.patient_visits!!)
                binding.listLayout.visibility = View.VISIBLE
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
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    override fun OnDeleteClick(model: TicketListResponseContent?) {
        TODO("Not yet implemented")
    }

    override fun OnViewClick(model: TicketListResponseContent?) {
        TODO("Not yet implemented")
    }

    val saveVendorResponseRetrofitCallback =
        object {

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
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


}