package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogManageLabTemplateBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.gettemplate.ResponseContentLabGetDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.request.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.request.Headers
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.request.RequestTemplateAddDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.response.ReponseTemplateadd
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.updaterequest.NewDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.updaterequest.RemovedDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.updaterequest.UpdateRequestModule
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.updateresponse.UpdateResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.view_model.ManageLabTemplateViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.view_model.ManageLabTemplateViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrescriptionDurationResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.any
import kotlin.collections.filterValues
import kotlin.collections.get
import kotlin.collections.indices
import kotlin.collections.map
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.toList
import kotlin.collections.toMap
import kotlin.collections.toMutableList
import kotlin.collections.toMutableMap


class ManageLabTemplateFragment : DialogFragment() {

    private var Itemname: String? = ""
    private var Itemdescription: String? = ""
    private var arrayItemData: ArrayList<ResponseContentsfav?>? = null
    private var Str_auto_id: Int? = 0
    private var Str_auto_name: String? = ""
    private var Str_auto_code: String? = ""
    private var deparment_UUID: Int? = 0
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    private var content: String? = null
    private var viewModel: ManageLabTemplateViewModel? = null
    var binding: DialogManageLabTemplateBinding? = null
    private var facility_UUID: Int? = 0
    private var customdialog: Dialog? = null
    private var favAddResponseMap = mutableMapOf<Int, String>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var dropdownReferenceView: AutoCompleteTextView? = null
    val detailsList: ArrayList<Detail> = ArrayList()
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var mAdapter: LabManageTemplateAdapter? = null
    private val favList: MutableList<RecyclerDto> = java.util.ArrayList()
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    val header: Headers? = Headers()
    var RequestTemplateAddDetails: RequestTemplateAddDetails = RequestTemplateAddDetails()
    var callbacktemplate: OnTemplateRefreshListener? = null
    var status: Boolean? = false
    var rasponsecontentLabGetTemplateDetails: ResponseContentLabGetDetails =
        ResponseContentLabGetDetails()
    var arraylistresponse: ArrayList<ResponseContentsfav?> = ArrayList()
    var UpdateRequestModule: UpdateRequestModule = UpdateRequestModule()
    var ispublic = "false"
    val removeList: ArrayList<RemovedDetail> = ArrayList()

    var newDetail: NewDetail = NewDetail()
    var newDetailList: ArrayList<NewDetail> = ArrayList()
    private val departmentPositionId: HashMap<Int, Int> = HashMap()
    private var selectedDepartmentId: Int? = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (dialog.window != null)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window?.attributes?.windowAnimations = R.style.CardDialogAnimation
            isCancelable = false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_manage_lab_template, container, false)
        viewModel = ManageLabTemplateViewModelFactory(
            requireActivity().application
        )
            .create(ManageLabTemplateViewModel::class.java)

        hideSoftKeyboard(view)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        utils = Utils(this.requireContext())

        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding?.labManageTemplateRecyclerView!!.layoutManager = layoutmanager
        mAdapter = LabManageTemplateAdapter(requireContext(), ArrayList())
        binding?.labManageTemplateRecyclerView!!.adapter = mAdapter

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        binding?.UserName?.setText(userDataStoreBean?.title?.name + "." + userDataStoreBean?.first_name)

//        departmentDropDownAdapter = FavouriteDropDownAdapter(context!!,ArrayList())
        binding?.viewModel?.getDepartmentList(facility_UUID, FavLabdepartmentRetrofitCallBack)
        binding?.cancelButton?.setOnClickListener {
            binding?.autoCompleteTextViewTestName!!.setText("")
            Str_auto_id = 0
        }

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.cancel?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.spinnerFavLabdepartment?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    selectedDepartmentId =
                        favAddResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                }

            }

        binding?.spinnerFavLabdepartment?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            binding?.viewModel?.getAllDepartment(facility_UUID, favLabAddAllDepartmentCallBack)
            false
        })
        val args = arguments
        if (args == null) {
            status = true
        } else {
            rasponsecontentLabGetTemplateDetails =
                args.getParcelable(AppConstants.RESPONSECONTENT)!!
            Itemname = rasponsecontentLabGetTemplateDetails.temp_details?.template_name
            Itemdescription =
                rasponsecontentLabGetTemplateDetails.temp_details?.template_description
            binding?.addDetails?.text = "Add"
            binding?.savebutton?.text = "Update"
            binding?.UserName?.setText(userDataStoreBean?.user_name)
            binding?.editName?.setText(Itemname)
            binding?.editDescription?.setText(Itemdescription)
            binding?.switchId?.isChecked =
                rasponsecontentLabGetTemplateDetails.temp_details?.template_is_active!!
            if (rasponsecontentLabGetTemplateDetails.temp_details?.is_public!!) {
                binding?.myDepartment?.isChecked = true
            } else {
                binding?.mySelf?.isChecked = true
            }
            binding?.displayorder?.setText(rasponsecontentLabGetTemplateDetails.temp_details?.template_displayorder?.toString())
            arraylistresponse.clear()

            for (i in rasponsecontentLabGetTemplateDetails.lab_details?.indices!!) {
                val ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
                ResponseContantSave?.template_details_uuid =
                    rasponsecontentLabGetTemplateDetails.lab_details?.get(i)?.template_details_uuid
                ResponseContantSave?.template_id =
                    rasponsecontentLabGetTemplateDetails.temp_details?.template_id!!
                ResponseContantSave?.test_master_name =
                    rasponsecontentLabGetTemplateDetails.lab_details?.get(i)?.lab_name
                ResponseContantSave?.test_master_id =
                    rasponsecontentLabGetTemplateDetails.lab_details?.get(i)?.lab_test_uuid
                ResponseContantSave?.test_master_code =
                    rasponsecontentLabGetTemplateDetails.lab_details?.get(i)?.lab_code
                arraylistresponse.add(ResponseContantSave)

            }
            mAdapter?.setFavAddItem(arraylistresponse)

        }

        val first = "Name"
        val disOrder = "Display Order"
        val next = "<font color='#EE0000'>*</font>"

        /* binding?.labelName?.setText(Html.fromHtml(first+next))
         binding?.labelTestName?.setText(Html.fromHtml("Test Name$next"))
         binding?.labelDepartment?.setText(Html.fromHtml("Department$next"))
         binding?.labelOrder?.setText(Html.fromHtml(disOrder+next))*/

        mAdapter?.setOnDeleteClickListener(object : LabManageTemplateAdapter.OnDeleteClickListener {
            override fun onDeleteClick(
                responseData: ResponseContentsfav?,
                position: Int
            ) {
                val removedDetail: RemovedDetail = RemovedDetail()
                removedDetail.template_uuid = responseData?.template_id
                removedDetail.template_details_uuid = responseData?.template_details_uuid
                removeList.add(removedDetail)
                mAdapter?.removeItem(position)
                Toast.makeText(
                    requireContext(),
                    "Test name Deleted successfully",
                    Toast.LENGTH_LONG
                ).show()

            }

        })


        binding?.clear?.setOnClickListener {
            binding?.displayorder?.setText("")
            binding?.autoCompleteTextViewTestName?.setText("")
            binding?.editName?.setText("")

            binding?.editDescription?.setText("")

        }
        binding?.save?.setOnClickListener {

            val arraydata = mAdapter?.getItems()
            if (arraydata?.size!! > 0) {
                val displayorder = binding?.displayorder?.text?.toString()
                val templeteName = binding?.editName?.text?.toString()
                if (templeteName?.isEmpty()!!) {
                    Toast.makeText(
                        requireContext(),
                        "Please Enter Templete name",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                if (displayorder?.isEmpty()!!) {
                    Toast.makeText(
                        requireContext(),
                        "Please Enter Display Order",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                if (binding!!.myDepartment!!.isChecked) {

                    ispublic = "true"
                } else {
                    ispublic = "false"
                }
                if (status as Boolean) {   /*
                Add Details
                 */

                    val displayordervalue = binding?.displayorder?.text.toString()
                    arrayItemData = mAdapter?.getItems()
                    detailsList.clear()
                    if (arrayItemData?.size!! > 0) {
                        for (i in arrayItemData?.indices!!) {
                            val details: Detail = Detail()
                            details.chief_complaint_uuid = 0
                            details.vital_master_uuid = 0
                            details.test_master_uuid = arrayItemData?.get(i)?.test_master_id
                            details.item_master_uuid = 0
                            details.drug_route_uuid = 0
                            details.drug_frequency_uuid = 0
                            details.duration = 0
                            details.duration_period_uuid = 0
                            details.drug_instruction_uuid = 0
                            details.revision = true
                            details.is_active = true
                            detailsList.add(details)
                        }

                        header?.name = binding?.editName?.text.toString()
                        header?.description = Itemdescription
                        header?.template_type_uuid = AppConstants.FAV_TYPE_ID_LAB
                        header?.diagnosis_uuid = 0
                        header?.is_public = ispublic
                        header?.facility_uuid = facility_UUID?.toString()
                        header?.department_uuid = selectedDepartmentId.toString()
                        header?.display_order = binding?.displayorder?.text?.toString()
                        header?.revision = true
                        header?.is_active = binding?.switchId?.isChecked

                        RequestTemplateAddDetails.headers = header!!
                        RequestTemplateAddDetails.details = this.detailsList

                        val request = Gson().toJson(RequestTemplateAddDetails)


                        viewModel?.labTemplateDetails(
                            facility_UUID,
                            RequestTemplateAddDetails,
                            emrlabtemplatepostRetrofitCallback
                        )

                    }
                } else {
                    Itemname = binding?.editName?.text.toString()

                    /*
                    Edit Details
                     */
                    val displayordervalue = binding?.displayorder?.text.toString()
                    arrayItemData = mAdapter?.getItems()
                    if (arrayItemData?.size!! > 0) {
                        header?.template_id =
                            rasponsecontentLabGetTemplateDetails.temp_details?.template_id
                        header?.name = binding?.editName?.text.toString()
                        header?.description = Itemdescription
                        header?.template_type_uuid = AppConstants.FAV_TYPE_ID_LAB
                        header?.diagnosis_uuid = 0
                        header?.is_public = ispublic
                        header?.facility_uuid = facility_UUID?.toString()
                        header?.department_uuid = selectedDepartmentId.toString()
                        header?.display_order = displayordervalue
                        header?.revision = true
                        header?.is_active = binding?.switchId?.isChecked
                        UpdateRequestModule.headers = header!!
                        UpdateRequestModule.new_details = this.newDetailList
                        UpdateRequestModule.removed_details = this.removeList
                        val requestupdate = Gson().toJson(UpdateRequestModule)

                        viewModel?.labUpdateTemplateDetails(
                            facility_UUID,
                            UpdateRequestModule,
                            UpdateemrlabtemplatepostRetrofitCallback
                        )


                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please select any one item", Toast.LENGTH_LONG)
                    .show()
            }


        }

        binding?.addDetails?.setOnClickListener {
            val displayorder = binding?.displayorder?.text?.toString()
            val templeteName = binding?.editName?.text?.toString()
            if (templeteName?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Please Enter Templete name", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (displayorder?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Please Enter Display Order", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (Str_auto_id == 0) {
                Toast.makeText(requireContext(), "Please Enter Test name", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (Str_auto_name?.isNotEmpty()!! && displayorder.isNotEmpty()) {
                arrayItemData = mAdapter?.getItems()
                val testmasterId = Str_auto_id
                val check = arrayItemData!!.any { it!!.test_master_id == testmasterId }

                if (!check) {
                    if (status as Boolean) {
                        Itemname = binding?.editName?.text.toString()
                        Itemdescription = binding?.editDescription?.text.toString()
                        val responseContentsfav = ResponseContentsfav()
                        responseContentsfav.test_master_name = Str_auto_name
                        responseContentsfav.test_master_id = testmasterId
                        responseContentsfav.test_master_code = Str_auto_code
                        binding?.autoCompleteTextViewTestName?.setText("")
                        Str_auto_name = ""
                        arraylistresponse.add(responseContentsfav)

                        mAdapter?.setFavAddItem(arraylistresponse)
                    } else {
                        ///Up
                        val newDetail: NewDetail = NewDetail()
                        newDetail.template_master_uuid =
                            rasponsecontentLabGetTemplateDetails.temp_details?.template_id
                        newDetail.test_master_uuid = testmasterId
                        newDetail.chief_complaint_uuid = 0
                        newDetail.vital_master_uuid = 0
                        newDetail.drug_id = 0
                        newDetail.drug_route_uuid = 0
                        newDetail.drug_frequency_uuid = 0
                        newDetail.drug_duration = 0
                        newDetail.drug_period_uuid = 0
                        newDetail.drug_instruction_uuid = 0
                        newDetail.display_order = 0
                        newDetail.quantity = 0
                        newDetail.revision = true
                        newDetail.is_active = true
                        newDetailList.add(newDetail)

                        Itemname = binding?.editName?.text.toString()
                        Itemdescription = binding?.editDescription?.text.toString()
                        val responseContentsfav = ResponseContentsfav()
                        responseContentsfav.test_master_name = Str_auto_name
                        responseContentsfav.test_master_id = testmasterId
                        responseContentsfav.test_master_code = Str_auto_code
                        binding?.autoCompleteTextViewTestName?.setText("")
                        arraylistresponse.add(responseContentsfav)

                        mAdapter?.setFavAddItem(arraylistresponse)

                    }
                } else {
                    Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                        ?.show()
                }

            } else {
                Toast.makeText(context, "Please select all field", Toast.LENGTH_LONG).show()

            }

        }


        binding?.autoCompleteTextViewTestName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }


            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {

                    viewModel?.getTestName(s.toString(), favAddTestNameCallBack)

                }
            }
        })

        binding?.autoCompleteTextViewTestName!!.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteTextViewTestName?.setText(autocompleteTestResponse?.get(position)?.name)


            Str_auto_code = autocompleteTestResponse?.get(position)?.code
            Str_auto_name = autocompleteTestResponse?.get(position)?.name

            Str_auto_id = autocompleteTestResponse?.get(position)?.uuid

        }


        return binding?.root
    }

    private fun hideSoftKeyboard(view: View?) {
        if (view != null) {
            val inputManager = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    val emrlabtemplatepostRetrofitCallback = object : RetrofitCallback<ReponseTemplateadd> {
        override fun onSuccessfulResponse(responseBody: Response<ReponseTemplateadd>?) {

            mAdapter?.cleardata()
            //        Toast.makeText(context,responseBody?.body()?.message,Toast.LENGTH_SHORT).show()

            Toast.makeText(requireContext(), "Template Added successfully", Toast.LENGTH_LONG)
                .show()
            viewModel!!.getTemplete(getTempleteRetrofitCallBack)

        }

        override fun onBadRequest(response: Response<ReponseTemplateadd>) {
            val gson = GsonBuilder().create()
            val responseModel: PrescriptionDurationResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    PrescriptionDurationResponseModel::class.java
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
Up Response
 */


    val UpdateemrlabtemplatepostRetrofitCallback = object : RetrofitCallback<UpdateResponse> {
        override fun onSuccessfulResponse(responseBody: Response<UpdateResponse>?) {
            responseBody?.message()?.let {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    it
                )
            }
            Log.i("", "" + responseBody?.body()?.responseContent)
            mAdapter?.cleardata()
            viewModel!!.getTemplete(getTempleteRetrofitCallBack)


        }

        override fun onBadRequest(response: Response<UpdateResponse>) {
            val gson = GsonBuilder().create()
            val responseModel: PrescriptionDurationResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    PrescriptionDurationResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
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


    val FavLabdepartmentRetrofitCallBack =
        object : RetrofitCallback<FavAddResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavAddResponseModel>) {

                if (response.body()?.responseContent?.uuid != null) {
                    listDepartmentItems.add(response.body()?.responseContent)

                    favAddResponseMap =
                        listDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()

                    try {
                        val adapter =
                            ArrayAdapter<String>(
                                requireContext(),
                                R.layout.spinner_item,
                                favAddResponseMap.values.toMutableList()
                            )
                        adapter.setDropDownViewResource(R.layout.spinner_item)
                        binding?.spinnerFavLabdepartment!!.adapter = adapter
                    } catch (e: Exception) {

                    }


                }

            }

            override fun onBadRequest(response: Response<FavAddResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FavAddResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavAddResponseModel::class.java
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

    val favLabAddAllDepartmentCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {

            listAllAddDepartmentItems = responseBody?.body()?.responseContents!!
            favAddResponseMap =
                listAllAddDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()
            for (i in listAllAddDepartmentItems.indices) {
                departmentPositionId[listAllAddDepartmentItems.get(i)!!.uuid] = i
            }

            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    R.layout.spinner_item,
                    favAddResponseMap.values.toMutableList()
                )

            adapter.setDropDownViewResource(R.layout.spinner_item)
            binding?.spinnerFavLabdepartment!!.adapter = adapter
            binding?.spinnerFavLabdepartment?.setSelection(
                departmentPositionId.get(
                    selectedDepartmentId
                )!!
            )

        }

        override fun onBadRequest(errorBody: Response<FavAddAllDepatResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddAllDepatResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    FavAddAllDepatResponseModel::class.java
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


    val favAddTestNameCallBack = object : RetrofitCallback<FavAddTestNameResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavAddTestNameResponse>?) {


            autocompleteTestResponse = responseBody?.body()?.responseContents
            val responseContentAdapter = FavTestNameSearchResultAdapter(
                requireContext(),
                R.layout.row_chief_complaint_search_result,
                responseBody?.body()?.responseContents!!
            )
            binding?.autoCompleteTextViewTestName?.threshold = 1
            binding?.autoCompleteTextViewTestName?.setAdapter(responseContentAdapter)

        }

        override fun onBadRequest(errorBody: Response<FavAddTestNameResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddTestNameResponse
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    FavAddTestNameResponse::class.java
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
    val getTempleteRetrofitCallBack =
        object : RetrofitCallback<TempleResponseModel> {
            override fun onSuccessfulResponse(response: Response<TempleResponseModel>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)
                callbacktemplate?.onRefreshList()

                dialog?.dismiss()

                /*     if (response.body()?.responseContents?.templates_lab_list?.isNotEmpty()!!) {
                         templeteAdapter.refreshList(response.body()?.responseContents?.templates_lab_list)
                     }*/
            }

            override fun onBadRequest(response: Response<TempleResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: TempleResponseModel


                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        TempleResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.bad)
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

    fun setOnTemplateRefreshListener(callback: OnTemplateRefreshListener) {
        this.callbacktemplate = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnTemplateRefreshListener {
        fun onTemplateID(position: Int)
        fun onRefreshList()
    }


}


