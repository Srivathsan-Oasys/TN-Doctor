package com.oasys.digihealth.doctor.ui.helpdesk.view


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.ActivityAddNewTicketBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.documents.ui.DocumentChildFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.documents.ui.FileUtil
import com.oasys.digihealth.doctor.ui.helpdesk.model.*
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.NewTicketViewModel
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.NewTicketViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*

class UserNewTicketFragment : Fragment() {
    var binding: ActivityAddNewTicketBinding? = null
    private var viewModel: NewTicketViewModel? = null
    private var getDirectoryPath: String? = ""
    var utils: Utils? = null
    private var userProfileAdpater: TicketsListAdapter? = null
    var appPreferences: AppPreferences? = null
    private var facility_id : Int = 0
    private var dept_id : Int = 0
    private var loginType:String?=null
    private var userUUID:Int?=null
    private var userDetailsRoomRepository : UserDetailsRoomRepository?=null
    private var listfilteritem: ArrayList<TicketInstitutionResponseContent?>? = ArrayList()
    private var categorylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var prioritylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var departmenrListfilteritem: ArrayList<DepartmentResponseContent?>? = ArrayList()
    private var FilterInstitutionResponseMap = mutableMapOf<Int, String>()
    private var FilterCategoryResponseMap = mutableMapOf<Int, String>()
    private var FilterDepartmentResponseMap = mutableMapOf<Int, String>()
    private var FilterPriorityResponseMap = mutableMapOf<Int, String>()
    private var selectedInstitutionid :Int=0
    private var selectedCategoryid :Int=0
    private var selectedPriorityid  :Int=0
    private var selectedDepartmentid :Int=0
    private var selectedImagePath: Uri? = null
    var FilePathURL : File?=null
    private var selectdAssetUUID : Int?=0
    private var selectdCategoryUUID : Int?=0
    private var selectdAssetCode : String?=null
    private var autocompleteTestResponse: List<AssetResponseContent?>? = null
    private var callBack:HelpDeskCallback?=null
    var status: Boolean? = false
    var detailList :TicketListResponseContent ?= null

    companion object {
        const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 101
        const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 102
        const val LOAD_IMAGE_RESULTS = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_add_new_ticket,
                container,
                false
            )


        viewModel = NewTicketViewModelFactory(
            requireActivity().application
        )
            .create(NewTicketViewModel::class.java)
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
        dept_id = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!
        loginType = appPreferences?.getString(AppConstants.LOGINTYPE)!!
        userUUID =  userDataStoreBean?.uuid!!

        binding?.institutionSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedInstitutionid =0
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if(pos==0){
                        selectedInstitutionid =0
                    }
                    else{
                        selectedInstitutionid =0
                        selectedInstitutionid =FilterInstitutionResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected insitution Id",selectedInstitutionid.toString())
                    }
                }
            }
        binding?.categorySpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedCategoryid =0
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if(pos==0){
                        selectedCategoryid =0
                    }
                    else{
                        selectedCategoryid =0
                        selectedCategoryid=FilterCategoryResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected insitution Id",selectedCategoryid.toString())
                    }

                }
            }


        binding?.prioritySpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedPriorityid =0
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if(pos==0){
                        selectedPriorityid =0
                    }
                    else{
                        selectedPriorityid =0
                        selectedPriorityid=FilterPriorityResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected insitution Id",selectedPriorityid.toString())
                    }

                }
            }

        binding?.uploadFile?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                runTimePermission()

            } else {
                openDocument()
            }
        }
        binding?.autoCompleteTextViewAssetId?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if(s.length>2) {


                    viewModel?.getAssetData(s.toString(),facility_id,selectedDepartmentid,assetResponseRetrofitCallback)

                    /*         Log.e("asset :", s.toString())
                             val jsonBody = JSONObject()
                             try {

                             } catch (e: JSONException) {
                                 e.printStackTrace()
                             }

                             jsonBody.put("codename", s.toString())
                             jsonBody.put("facility_uuid", facility_id)
                             jsonBody.put("department_uuid", selectedDepartmentid)
                             jsonBody.put("pageNo", 0)
                             jsonBody.put("paginationSize", 100)

                             viewModel?.getAssetCode(jsonBody!!,assetResponseRetrofitCallback)*/
                }

            }
        })

        binding?.autoCompleteTextViewAssetId!!.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteTextViewAssetId?.setText(autocompleteTestResponse?.get(position)?.asset_name)
            binding?.edtModel?.setText(autocompleteTestResponse?.get(position)?.AssetDetails?.get(0)?.model_no)
            binding?.edtMake?.setText(autocompleteTestResponse?.get(position)?.AssetDetails?.get(0)?.model_name)
            binding?.edtSerialNumber?.setText(autocompleteTestResponse?.get(position)?.AssetDetails?.get(0)?.serial_no)

            selectdAssetUUID =autocompleteTestResponse?.get(position)?.AssetDetails?.get(0)?.uuid
            selectdAssetCode =autocompleteTestResponse?.get(position)?.asset_code
            selectdCategoryUUID =autocompleteTestResponse?.get(position)?.asset_category_uuid!!
        }



        binding?.departmentSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedDepartmentid =0
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    if(pos==0){
                        selectedDepartmentid =0
                    }
                    else{
                        selectedDepartmentid = FilterDepartmentResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                        Log.e("selected department Id",selectedDepartmentid.toString())
                    }
                }
            }


        binding?.clear!!.setOnClickListener {
            clearSearch()
        }

        binding?.searchButton!!.setOnClickListener {


            if(selectedInstitutionid==0){
                (binding?.institutionSpinner?.selectedView as TextView).error = "Select Institution"
                return@setOnClickListener
            }

            if(selectdAssetUUID==0){
                binding?.autoCompleteTextViewAssetId!!.error = "Enter Asset Id"
                return@setOnClickListener
            }

            if(binding?.edtSubject!!.text.trim().toString().isEmpty()){
                binding?.edtSubject!!.error = "Enter Subject"
                return@setOnClickListener
            }

            if(binding?.edtDescription!!.text.trim().toString().isEmpty()){
                binding?.edtDescription!!.error = "Enter Problem description"
                return@setOnClickListener
            }

            if(binding?.edtFileName!!.text.trim().toString().isEmpty()){
                binding?.edtFileName!!.error = "Select File"
                return@setOnClickListener
            }

            val requestFile = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                FilePathURL
            )
            val body =
                MultipartBody.Part.createFormData("userAppComplaintImage", FilePathURL!!.name, requestFile)

            var model = AddTicketRequestModel()
            model.facility_uuid= facility_id
            model.department_uuid= selectedDepartmentid
            model.application_user_uuid=userUUID!!
            model.created_by=userUUID!!
            model.user_type_uuid= userDataStoreBean.user_type_uuid!!
            model.ticketstatus_uuid=1
            model.subject=binding?.edtSubject?.text.toString()
            model.problem_description=binding?.edtDescription?.text.toString()
            model.make=binding?.edtMake?.text.toString()
            model.model=binding?.edtModel?.text.toString()
            model.serial=binding?.edtSerialNumber?.text.toString()
            model.assest_uuid=selectdAssetUUID
            model.asset_code=selectdAssetCode
            model.priority_uuid = selectedPriorityid
            model.help_desk_category_uuid= selectedCategoryid
            model.ticket_details_priority_uuid= selectedPriorityid

            var requestModel = Gson().toJson(model)
            Log.e("requestModel", requestModel)
            binding?.progressbar?.visibility=View.VISIBLE
            viewModel?.addNewTicket(model, body,addNewTicketRetrofitCallback)

        }

        viewModel?.getInstitution(facility_id,labInstitutionSpinnerRetrofitCallback)


        return binding!!.root
    }

    private fun clearSearch(){

        binding?.institutionSpinner?.setSelection(0)
        binding?.categorySpinner?.setSelection(0)
        binding?.prioritySpinner?.setSelection(0)
        binding?.departmentSpinner?.setSelection(0)
        binding?.edtModel?.setText("")
        binding?.edtDescription?.setText("")
        binding?.edtMake?.setText("")
        binding?.edtSerialNumber?.setText("")
        binding?.edtSubject?.setText("")
        binding?.edtFileName?.setText("")
        binding?.autoCompleteTextViewAssetId?.setText("")

    }

    private fun setDepartment() {

        if(status as Boolean)
        {
            //  binding?.departmentSpinner!!.setSelection(0)

        }else
        {

            for(i in departmenrListfilteritem!!.indices)

                if(departmenrListfilteritem?.get(i)?.equals(detailList?.department_uuid)!!) {
                    binding?.departmentSpinner!!.setSelection(i)
                    selectedDepartmentid =detailList?.department_uuid!!
                }
        }

    }

    private fun setInstitution() {

        if(status as Boolean)
        {
            binding?.institutionSpinner!!.setSelection(1)
        }else
        {

            for(i in listfilteritem!!.indices)

                if(listfilteritem?.get(i)?.equals(detailList?.institution_uuid)!!) {
                    binding?.institutionSpinner!!.setSelection(i)
                }
        }

    }

    private fun setCategory() {

        if(status as Boolean)
        {
            binding?.categorySpinner!!.setSelection(0)
        }else
        {

            for(i in categorylistfilteritem!!.indices)

                if(categorylistfilteritem?.get(i)?.equals(detailList?.help_desk_category_uuid)!!) {
                    binding?.categorySpinner!!.setSelection(i)
                    selectedCategoryid =detailList?.help_desk_category_uuid!!
                }
        }

    }

    private fun setPriority() {

        if(status as Boolean)
        {
            binding?.prioritySpinner!!.setSelection(0)
        }else
        {

            for(i in prioritylistfilteritem!!.indices)

                if(prioritylistfilteritem?.get(i)?.equals(detailList?.ticket_detail_priority_uuid)!!) {
                    binding?.prioritySpinner!!.setSelection(i)
                    selectedPriorityid =detailList?.ticket_detail_priority_uuid!!
                }
        }

    }


    private fun runTimePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
            )
            return
        } else {
            openDocument()
            return
        }
    }

    private fun openDocument() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select File"
            ), DocumentChildFragment.LOAD_IMAGE_RESULTS
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {                    // When an Image is picked
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Log.e("test", "data" + data.data)

                selectedImagePath = data.data
                Log.i("test____", "test" + selectedImagePath)
                if (selectedImagePath != null)
                    try {
                        FilePathURL  = FileUtil.from(requireContext(), this.selectedImagePath!!)
                        val strFileName: String = FilePathURL!!.name
                        val fileuploadname: String = selectedImagePath.toString()
                        val fileuploadName: String =
                            fileuploadname.substring(fileuploadname.lastIndexOf("/") + 1)
                        getDirectoryPath = fileuploadName
                        Log.e("getDirectoryPath", "___" + getDirectoryPath)
                        Log.i("", "" + fileuploadName)
                        Log.e("fileuploadName", "data" + fileuploadName.toString())

                        Log.e("strFileName :", "$strFileName")
                        Log.e("fileuploadName :", "$fileuploadName")
                        binding?.edtFileName?.setText(strFileName)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
            }

        }
    }

    val addNewTicketRetrofitCallback =
        object : RetrofitCallback<TicketInstitutionResponseModel> {

            override fun onSuccessfulResponse(response: Response<TicketInstitutionResponseModel>) {
                Log.i("",""+response.body()?.responseContents)
                binding?.progressbar?.visibility=View.GONE
                Toast.makeText(context!!, response.body()?.msg, Toast.LENGTH_LONG).show()
                clearSearch()

                val op= UserTicketsListFragment()
                val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.landfragment, op)
                fragmentTransaction?.commit()
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



    val labInstitutionSpinnerRetrofitCallback =
        object : RetrofitCallback<TicketInstitutionResponseModel> {

            override fun onSuccessfulResponse(response: Response<TicketInstitutionResponseModel>) {
                Log.i("",""+response.body()?.responseContents)

                listfilteritem?.add(TicketInstitutionResponseContent())
                listfilteritem?.add(response.body()?.responseContents!!)

                FilterInstitutionResponseMap =
                    listfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterInstitutionResponseMap.put(0, "Institution")
                try
                {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            FilterInstitutionResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(R.layout.spinner_item)
                    binding?.institutionSpinner!!.adapter = adapter
                }catch (e:Exception)
                {

                }

                binding?.institutionSpinner?.prompt =listfilteritem?.get(0)?.name
                binding?.institutionSpinner!!.setSelection(1)

                setInstitution()

                viewModel?.getDepartment(facility_id,labDepartmentSpinnerRetrofitCallback)

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
/*

    val categorySpinnerRetrofitCallback =
        object : RetrofitCallback<CategoryListResponseModel> {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("",""+response.body()?.responseContents)

                categorylistfilteritem?.add(CategoryResponseContent())
                categorylistfilteritem?.addAll(response.body()?.responseContents!!)

                FilterCategoryResponseMap =
                    categorylistfilteritem!!. filter { it?.uuid != null && it?.name != null}.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
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
                binding?.categorySpinner!!.setSelection(0)

                setCategory()

                val jsonBody = JSONObject()
                try {
                    jsonBody.put("table_name", "priority")
                    jsonBody.put("searchColumn", "name")
                    jsonBody.put("searchDetail", "")
                    jsonBody.put("is_active", 1)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


                viewModel?.getPriorityList(jsonBody!!,prioritySpinnerRetrofitCallback)
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

*/

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
                binding?.autoCompleteTextViewAssetId?.showDropDown()
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

    val labDepartmentSpinnerRetrofitCallback =
        object : RetrofitCallback<DepartmentResponseModel> {

            override fun onSuccessfulResponse(response: Response<DepartmentResponseModel>) {
                Log.i("",""+response.body()?.responseContents)

                departmenrListfilteritem?.add(DepartmentResponseContent())
                departmenrListfilteritem?.addAll(response.body()?.responseContents!!)

                FilterDepartmentResponseMap =
                    departmenrListfilteritem!!.filter { it?.uuid != null && it.department != null }
                        .map { it?.uuid!! to it.department?.name!! }.toMap().toMutableMap()
                FilterDepartmentResponseMap.put(0, "Department")
                try
                {

                }catch (e:Exception)
                {
                }


                val adapter =
                    ArrayAdapter<String>(
                        context!!,
                        android.R.layout.simple_spinner_item,
                        FilterDepartmentResponseMap.values.toMutableList()
                    )
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.departmentSpinner!!.adapter = adapter


                for(i in departmenrListfilteritem!!.indices) {

                    if (departmenrListfilteritem?.get(i)?.equals(dept_id)!!) {
                        binding?.departmentSpinner!!.setSelection(i+1)
                        selectedDepartmentid = dept_id
                    }

                }
                setDepartment()
/*
                val jsonBody = JSONObject()
                try {
                    jsonBody.put("table_name", "help_desk_category")
                    jsonBody.put("searchColumn", "name")
                    jsonBody.put("searchDetail", "")
                    jsonBody.put("is_active", 1)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


                viewModel?.getCategoryList(jsonBody!!,categorySpinnerRetrofitCallback)*/
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

    /* val prioritySpinnerRetrofitCallback =
         object : RetrofitCallback<CategoryListResponseModel> {

             override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                 Log.i("",""+response.body()?.responseContents)

                 prioritylistfilteritem?.add(CategoryResponseContent())
                 prioritylistfilteritem?.addAll(response.body()?.responseContents!!)

                 FilterPriorityResponseMap =
                     prioritylistfilteritem!!. filter { it?.uuid != null && it?.name != null}.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
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

                 binding?.prioritySpinner?.prompt =listfilteritem?.get(0)?.name
                 binding?.prioritySpinner!!.setSelection(0)

                 setPriority()
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
 */
}