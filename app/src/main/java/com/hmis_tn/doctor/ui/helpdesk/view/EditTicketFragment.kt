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
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.isTablet
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.ActivityEditTicketBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback

import com.hmis_tn.doctor.ui.helpdesk.model.*
import com.hmis_tn.doctor.ui.helpdesk.viewmodel.EditTicketViewModel
import com.hmis_tn.doctor.ui.helpdesk.viewmodel.EditTicketViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.util.*

class EditTicketFragment : DialogFragment() {
    var binding: ActivityEditTicketBinding? = null
    private var viewModel: EditTicketViewModel? = null
    var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var facility_id : Int = 0
    private var department_id : Int = 0

    private var loginType:String?=null
    private var userUUID:Int?=0
    private var userTypeUUID:Int?=0
    private var userDetailsRoomRepository : UserDetailsRoomRepository?=null

    private var categorylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var FilterCategoryResponseMap = mutableMapOf<Int, String>()
    private var selectedCategoryid :Int=0


    private var prioritylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var FilterPriorityResponseMap = mutableMapOf<Int, String>()
    private var selectedPriorityid  :Int=0

    private var statuslistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var FilterStatusResponseMap = mutableMapOf<Int, String>()
    private var selectedStatusid  :Int=0

    private var selectdAssetUUID : Int?=0
    private var selectdCategoryUUID : Int?=0
    private var selectdAssetCode : String?=null
    private var autocompleteTestResponse: List<AssetResponseContent?>? = null

    var status: Boolean? = false
    var detailList :TicketListResponseContent ?= null

    private var subCategorylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var FilterSubCategoryResponseMap = mutableMapOf<Int, String>()
    private var selectedSubCategoryid :Int=0

    private var selectedMake :String=""
    private var selectedModel :String=""
    private var selectedSerial :String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_edit_ticket,
                container,
                false
            )


        viewModel = EditTicketViewModelFactory(
            requireActivity().application
        )
            .create(EditTicketViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())

        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..
            status = false
            var uuid  = args.getInt("uuid")
            Log.e("selected uuid", uuid.toString())
            getDetails(uuid)
        }


        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        loginType = appPreferences?.getString(AppConstants.LOGINTYPE)!!
        userUUID =  userDataStoreBean?.uuid!!


        if(isTablet(requireContext())) {

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
                            Log.e("selected subcategoy Id", selectedSubCategoryid.toString())

                        }

                    }
                }

            binding?.statusSpinner!!.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        val itemValue = parent!!.getItemAtPosition(0).toString()
                        selectedStatusid = 0
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        pos: Int,
                        id: Long
                    ) {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()
                        if (pos == 0) {
                            selectedStatusid = 0
                        } else {
                            selectedStatusid = 0
                            selectedStatusid =
                                FilterStatusResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                            Log.e("selected status Id", selectedStatusid.toString())

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

            binding?.autoCompleteTextViewAssetId?.addTextChangedListener(object : TextWatcher {

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {

                    if (s.length > 2) {
                        Log.e("asset :", s.toString())
                        val jsonBody = JSONObject()
                        try {
                            jsonBody.put("codename", s.toString())
                            jsonBody.put("facility_uuid", facility_id)
                            jsonBody.put("department_uuid", department_id)
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
                binding?.autoCompleteTextViewAssetId?.setText(autocompleteTestResponse?.get(position)?.asset_name)

                selectdAssetUUID =
                    autocompleteTestResponse?.get(position)?.AssetDetails?.get(0)?.uuid
                selectdAssetCode = autocompleteTestResponse?.get(position)?.asset_code
                selectdCategoryUUID = autocompleteTestResponse?.get(position)?.asset_category_uuid!!
            }

            binding?.cancel!!.setOnClickListener {
                dismiss()
            }

            binding?.searchButton!!.setOnClickListener {

                if (selectdAssetUUID == 0) {
                    binding?.autoCompleteTextViewAssetId!!.error = "Enter Asset Id"
                    return@setOnClickListener
                }

                if (selectedCategoryid == 0) {
                    (binding?.categorySpinner?.selectedView as TextView).error =
                        "Select Category"
                    return@setOnClickListener
                }
                if (selectedSubCategoryid == 0) {
                    (binding?.subcategorySpinner?.selectedView as TextView).error =
                        "Select Sub category"
                    return@setOnClickListener
                }

                if (selectedPriorityid == 0) {
                    (binding?.prioritySpinner?.selectedView as TextView).error =
                        "Select Priority"
                    return@setOnClickListener
                }

                if (selectedStatusid == 0) {
                    (binding?.statusSpinner?.selectedView as TextView).error =
                        "Select Priority"
                    return@setOnClickListener
                }

                var model = AddTicketRequestModel()
                model.facility_uuid = facility_id
                model.department_uuid = department_id
                model.application_user_uuid = userUUID!!
                model.created_by = userUUID!!
                model.user_type_uuid = userTypeUUID!!
                model.ticketstatus_uuid = selectedStatusid
                model.subject = binding?.edtSubject?.text.toString()
                model.problem_description = binding?.edtDescription?.text.toString()
                model.make = selectedMake
                model.model = selectedModel
                model.serial = selectedSerial
                model.assest_uuid = selectdAssetUUID
                model.asset_code = selectdAssetCode
                model.priority_uuid = selectedPriorityid
                model.category_uuid = selectedCategoryid
                model.subcategory_uuid = selectedSubCategoryid
                model.ticketmanagment_uuid = detailList?.uuid!!

                var requestModel = Gson().toJson(model)
                Log.e("requestModel", requestModel)
                viewModel?.updateTicket(model, updateTicketRetrofitCallback)

            }

        }
        else{




        }


        return binding!!.root
    }

    private fun setCategory() {

        if(status as Boolean)
        {
            binding?.categorySpinner!!.setSelection(1)
        }else
        {

            Log.e("category_uuid", ""+detailList?.category_uuid)
            for(i in categorylistfilteritem!!.indices)
                if(categorylistfilteritem?.get(i)?.uuid==detailList?.category_uuid) {
                    binding?.categorySpinner!!.setSelection(i)
                }
        }

    }

    private fun setSubCategory() {

        if(status as Boolean)
        {
            binding?.categorySpinner!!.setSelection(1)
        }else
        {

            Log.e("subcategory_uuid", ""+detailList?.subcategory_uuid)
            for(i in categorylistfilteritem!!.indices)

                if(categorylistfilteritem?.get(i)?.uuid==detailList?.subcategory_uuid) {
                    binding?.categorySpinner!!.setSelection(i)
                }
        }

    }


    private fun setPriority() {

        if(status as Boolean)
        {
            binding?.prioritySpinner!!.setSelection(1)
        }else
        {

            Log.e("priority_uuid", ""+detailList?.priority_uuid)
            for(i in prioritylistfilteritem!!.indices)

                if(prioritylistfilteritem?.get(i)?.uuid==detailList?.priority_uuid) {
                    binding?.prioritySpinner!!.setSelection(i)
                }
        }

    }

    val updateTicketRetrofitCallback =
        object : RetrofitCallback<TicketInstitutionResponseModel> {

            override fun onSuccessfulResponse(response: Response<TicketInstitutionResponseModel>) {
                Log.i("",""+response.body()?.responseContents)
                Toast.makeText(context!!, response.body()?.msg, Toast.LENGTH_LONG).show()
                dismiss()
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

    val categorySpinnerRetrofitCallback =
        object : RetrofitCallback<CategoryListResponseModel> {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("",""+response.body()?.responseContents)

                categorylistfilteritem?.add(CategoryResponseContent())
                categorylistfilteritem?.addAll(response.body()?.responseContents!!)

                FilterCategoryResponseMap =
                    categorylistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterCategoryResponseMap.put(0, "Category")
                try
                {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            FilterCategoryResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.categorySpinner!!.adapter = adapter
                }catch (e:Exception)
                {

                }

                binding?.categorySpinner?.prompt =categorylistfilteritem?.get(0)?.name
                setCategory()

                viewModel?.getSubCategoryList(selectedCategoryid,subCategorySpinnerRetrofitCallback)
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


    val assetResponseRetrofitCallback =
        object : RetrofitCallback<AssetResponseModel> {

            override fun onSuccessfulResponse(response: Response<AssetResponseModel>) {
                Log.e("assetResponseRetrofit",""+response.body()?.responseContents)
                autocompleteTestResponse  = null
                autocompleteTestResponse =response.body()?.responseContents!!
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

    private fun getDetails(uuid :Int){
        val jsonBody = JSONObject()
        try {
            jsonBody.put("Id", uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        viewModel?.getTicketById(jsonBody, ticketByIdResponseRetrofitCallback)

    }


    val ticketByIdResponseRetrofitCallback = object  :
        RetrofitCallback<TicketIdResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<TicketIdResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()?.responseContents)
            if(responseBody?.body()?.responseContents!=null){
                detailList = responseBody.body()?.responseContents

                binding?.edtInstitution?.setText(detailList?.facility_name)
                binding?.edtDepartment?.setText(detailList?.department_name)
                binding?.edtTicketId?.setText(detailList?.ticket_id)
                binding?.edtSubject?.setText(detailList?.subject)
                binding?.edtDescription?.setText(detailList?.problem_description)
                binding?.edtCreatedBy?.setText(detailList?.createdby_detail?.first_name)
                binding?.edtCreatedOn?.setText(detailList?.created_date)
                facility_id =detailList?.facility_uuid!!
                department_id =detailList?.department_uuid!!
                selectedCategoryid =detailList?.category_uuid!!
                selectedSubCategoryid =detailList?.subcategory_uuid!!
                selectedPriorityid =detailList?.ticket_detail_priority_uuid!!
                userUUID =detailList?.application_user_uuid!!
                selectedMake=detailList?.make!!
                selectedModel=detailList?.model!!
                selectedSerial=detailList?.serial!!
                userTypeUUID = detailList?.user_type_uuid!!
                selectdAssetUUID = detailList?.assest_uuid!!
                selectdAssetCode = detailList?.asset_code!!
                binding?.autoCompleteTextViewAssetId?.setText(selectdAssetCode)

            }
            viewModel?.getCategoryList(categorySpinnerRetrofitCallback)

        }

        override fun onBadRequest(errorBody: Response<TicketIdResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: TicketIdResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    TicketIdResponseModel::class.java
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

    val prioritySpinnerRetrofitCallback =
        object : RetrofitCallback<CategoryListResponseModel> {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("",""+response.body()?.responseContents)

                prioritylistfilteritem?.add(CategoryResponseContent())
                prioritylistfilteritem?.addAll(response.body()?.responseContents!!)

                FilterPriorityResponseMap =
                    prioritylistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterPriorityResponseMap.put(0, "Priority")
                try
                {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            FilterPriorityResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.prioritySpinner!!.adapter = adapter
                }catch (e:Exception)
                {

                }

                binding?.prioritySpinner?.prompt =prioritylistfilteritem?.get(0)?.name
                binding?.prioritySpinner?.setSelection(1)
                //setPriority()

                viewModel?.getStatusList(statuspinnerRetrofitCallback)

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

    val statuspinnerRetrofitCallback =
        object : RetrofitCallback<CategoryListResponseModel> {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("",""+response.body()?.responseContents)

                statuslistfilteritem?.add(CategoryResponseContent())
                statuslistfilteritem?.addAll(response.body()?.responseContents!!)

                FilterStatusResponseMap =
                    statuslistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterStatusResponseMap.put(0, "Status")
                try
                {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            FilterStatusResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.statusSpinner!!.adapter = adapter
                }catch (e:Exception)
                {

                }

                binding?.statusSpinner?.prompt =statuslistfilteritem?.get(0)?.name
                binding?.statusSpinner!!.setSelection(1)

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
                Log.i("",""+response.body()?.responseContents)

                subCategorylistfilteritem?.clear()
                subCategorylistfilteritem?.add(CategoryResponseContent())
                subCategorylistfilteritem?.addAll(response.body()?.responseContents!!)

                FilterSubCategoryResponseMap.clear()
                FilterSubCategoryResponseMap =
                    subCategorylistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterSubCategoryResponseMap.put(0, "Sub Category")
                try
                {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            FilterSubCategoryResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.subcategorySpinner!!.adapter = adapter
                }catch (e:Exception)
                {

                }

                binding?.subcategorySpinner?.prompt =subCategorylistfilteritem?.get(0)?.name

                setSubCategory()

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

}