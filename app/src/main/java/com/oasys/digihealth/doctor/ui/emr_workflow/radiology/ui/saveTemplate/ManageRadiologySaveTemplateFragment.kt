package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.saveTemplate


import android.annotation.SuppressLint
import android.app.Dialog
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
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.isTablet
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogManageRadiologyTemplateBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.gettemplate.ResponseContentLabGetDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.request.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.request.Headers
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.request.RequestTemplateAddDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.response.ReponseTemplateadd
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrescriptionDurationResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.ManageTemplateModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.RadiologyManageSaveTemplateAdapter
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model.ManageRadiologyTemplateViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model.ManageRadiologyTemplateViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableList
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

class ManageRadiologySaveTemplateFragment : DialogFragment() {

    private var Itemdescription: String? = null
    private var Itemname: String? = ""
    private var deparment_UUID: Int? = 0
    private var content: String? = null
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    private var viewModel: ManageRadiologyTemplateViewModel? = null
    var binding: DialogManageRadiologyTemplateBinding? = null
    private var facility_UUID: Int? = 0
    private var favAddResponseMap = mutableMapOf<Int, String>()
    var dropdownReferenceView: AutoCompleteTextView? = null
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var mAdapter: RadiologyManageSaveTemplateAdapter? = null
    private val favList: MutableList<ManageTemplateModel> = java.util.ArrayList()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var arrayItemData: ArrayList<ResponseContentsfav?>? = null
    private var Str_auto_id: Int? = 0
    private var Str_auto_name: String? = ""
    private var Str_auto_code: String? = ""
    val detailsList: ArrayList<Detail> = ArrayList()
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    val header: Headers? = Headers()
    var callbacktemplate: OnSaveTemplateRefreshListener? = null
    var RequestTemplateAddDetails: RequestTemplateAddDetails = RequestTemplateAddDetails()
    var arraylistresponse: ArrayList<ResponseContentsfav?> = ArrayList()
    var rasponsecontentLabGetTemplateDetails: ResponseContentLabGetDetails =
        ResponseContentLabGetDetails()
    var mCallback: LabChiefComplaintListener? = null
    var tCallback: RadiologyTempReferesh? = null
    private var customdialog: Dialog? = null
    private val departmentPositionId: HashMap<Int, Int> = HashMap()
    private var selectedDepartmentId: Int? = 0
    var ispublic = "false"


    var responsecontentLabGetTemplateDetails: ArrayList<FavouritesModel> = ArrayList()

    var status: Boolean? = false
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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_manage_radiology_template,
            container,
            false
        )
        viewModel = ManageRadiologyTemplateViewModelFactory(
            requireActivity().application
        ).create(ManageRadiologyTemplateViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(this.requireContext())

        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding?.templateRadiologyRecyclerView!!.layoutManager = layoutmanager
        mAdapter = RadiologyManageSaveTemplateAdapter(requireContext(), ArrayList())
        binding?.templateRadiologyRecyclerView!!.adapter = mAdapter

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        binding?.UserName?.setText(userDataStoreBean?.user_name)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        binding?.viewModel?.getRadiologyDepartmentList(
            facility_UUID,
            templateRadiodepartmentRetrofitCallBack
        )

        binding?.cancel?.setOnClickListener {
            dialog?.dismiss()

        }

        binding?.clear?.setOnClickListener({
            binding?.editDescription?.setText("")
            binding?.editName?.setText("")
            binding?.displayorder?.setText("")
            binding?.autoCompleteTextViewTestName?.setText("")
        })

        binding?.spinnerRadiologydepartment?.onItemSelectedListener =
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


        mAdapter!!.setOnItemClickListener(object :
            RadiologyManageSaveTemplateAdapter.OnItemClickListener {
            override fun onItemClick(responseContent: ResponseContentsfav?, position: Int) {

                binding!!.displayorder!!.setText(responseContent!!.favourite_display_order!!.toString())

                binding!!.autoCompleteTextViewTestName.setText(responseContent.test_master_name!!.toString())

            }
        })


        val args = arguments

        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {

            val favouriteData =
                args.getParcelableArrayList<FavouritesModel>(AppConstants.RESPONSECONTENT)

            val dataSize = favouriteData!!.size
            if (isTablet(requireContext())) {
                for (i in 0..dataSize - 2) {
                    val ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
                    ResponseContantSave?.test_master_name = favouriteData[i].itemAppendString
                    ResponseContantSave?.test_master_id = favouriteData[i].test_master_id
                    ResponseContantSave?.test_master_code =
                        favouriteData[i]?.test_master_code.toString()
                    arraylistresponse.add(ResponseContantSave)

                }
            } else {
                for (i in 0..dataSize - 1) {
                    val ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
                    ResponseContantSave?.test_master_name = favouriteData[i].itemAppendString
                    ResponseContantSave?.test_master_id = favouriteData[i].test_master_id
                    ResponseContantSave?.test_master_code =
                        favouriteData[i]?.test_master_code.toString()
                    arraylistresponse.add(ResponseContantSave)

                }
            }
            mAdapter?.setFavAddItem(arraylistresponse)


        }
        mAdapter?.setOnDeleteClickListener(object :
            RadiologyManageSaveTemplateAdapter.OnDeleteClickListener {
            override fun onDeleteClick(responseData: ResponseContentsfav?, position: Int) {

                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                }
                val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                drugNmae.text =
                    "${drugNmae.text} '" + responseData?.test_master_name + "' Record ?"
                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {

                    mAdapter?.delete(position)

                    customdialog!!.dismiss()


                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                }
                customdialog!!.show()


            }

        })


        binding?.save?.setOnClickListener {
            val displayordervalue = binding?.displayorder?.text.toString()
            arrayItemData = mAdapter?.getItems()
            detailsList.clear()
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
                if (binding!!.myDepartment!!.isChecked) {

                    ispublic = "true"
                } else {
                    ispublic = "false"
                }
                header?.name = binding?.editName?.text.toString()
                header?.description = Itemdescription
                header?.template_type_uuid = AppConstants.FAV_TYPE_ID_RADIOLOGY
                header?.diagnosis_uuid = 0
                header?.is_public = ispublic
                header?.facility_uuid = facility_UUID?.toString()
                header?.department_uuid = selectedDepartmentId.toString()
                header?.display_order = displayordervalue
                header?.revision = true
                header?.is_active = binding?.switchId?.isChecked

                RequestTemplateAddDetails.headers = this.header!!
                RequestTemplateAddDetails.details = this.detailsList

                val request = Gson().toJson(RequestTemplateAddDetails)
                Log.i("", "" + request)
                viewModel?.radiologyTemplateDetails(
                    facility_UUID,
                    RequestTemplateAddDetails,
                    emrradilogytemplatepostRetrofitCallback
                )
            }
        }



        binding?.addDetail?.setOnClickListener {

            Itemname = binding?.editName?.text.toString()
            Itemdescription = binding?.editDescription?.text.toString()
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

            if (!binding?.editName?.text.toString().isNullOrEmpty()) {


                if (!binding?.displayorder?.text.toString().isNullOrEmpty()) {


                    if (Str_auto_id != 0) {

                        val testmasterId = Str_auto_id

                        val responseContentsfav = ResponseContentsfav()

                        responseContentsfav.test_master_name = Str_auto_name
                        responseContentsfav.test_master_id = testmasterId
                        responseContentsfav.test_master_code = Str_auto_code

                        binding?.autoCompleteTextViewTestName?.setText("")

                        mAdapter?.setAddItem(responseContentsfav)

                        Str_auto_id = 0
                    } else {
                        binding?.autoCompleteTextViewTestName?.error = "Test Name must be valid"

                    }
                } else {
                    binding?.displayorder?.error = "Display order can't be empty"

                }
            } else {

                binding?.editName?.error = "Template name must be valid"


            }


        }




        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.spinnerRadiologydepartment?.setOnTouchListener(View.OnTouchListener { _, motionEvent ->
            binding?.viewModel?.getRadiologyAllDepartment(
                facility_UUID,
                tempRadiologyAddAllDepartmentCallBack
            )
            false
        })

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

            Log.i("", "" + autocompleteTestResponse!!.get(position).name)
            Str_auto_code = autocompleteTestResponse?.get(position)?.code
            Str_auto_name = autocompleteTestResponse?.get(position)?.name

            Str_auto_id = autocompleteTestResponse?.get(position)?.uuid


        }



        return binding!!.root
    }

/*
Response
 */

    val emrradilogytemplatepostRetrofitCallback = object : RetrofitCallback<ReponseTemplateadd> {
        override fun onSuccessfulResponse(responseBody: Response<ReponseTemplateadd>?) {
            Toast.makeText(context, responseBody?.body()?.message, Toast.LENGTH_SHORT).show()


            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )
            mAdapter?.cleardata()
            callbacktemplate?.onRefreshList()
            tCallback?.onRefresh()


            dialog!!.dismiss()
/*
            mCallback?.sendDataChiefComplaint()
*/
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

    private val templateRadiodepartmentRetrofitCallBack =
        object : RetrofitCallback<FavAddResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavAddResponseModel>) {
                Log.i("", "" + response.body()?.responseContent)
                Log.i("", "" + response.body()?.responseContent)
                listDepartmentItems.add(response.body()?.responseContent)
                favAddResponseMap =
                    listDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()
                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        R.layout.spinner_item,
                        favAddResponseMap.values.toMutableList()
                    )
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.spinnerRadiologydepartment!!.adapter = adapter
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

    private val tempRadiologyAddAllDepartmentCallBack =
        object : RetrofitCallback<FavAddAllDepatResponseModel> {
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
                binding?.spinnerRadiologydepartment!!.adapter = adapter
                binding?.spinnerRadiologydepartment?.setSelection(
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
    val getTempleteRetrofitCallBack =
        object : RetrofitCallback<TempleResponseModel> {
            override fun onSuccessfulResponse(response: Response<TempleResponseModel>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)
                callbacktemplate?.onRefreshList()

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
    val favAddTestNameCallBack = object : RetrofitCallback<FavAddTestNameResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavAddTestNameResponse>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            autocompleteTestResponse = responseBody?.body()?.responseContents
            val responseContentAdapter = FavTestNameSearchResultAdapter(
                context!!,
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
            }
        }
    }


    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    fun setOnTemplateRefreshListener(callback: OnSaveTemplateRefreshListener) {
        this.callbacktemplate = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnSaveTemplateRefreshListener {
        fun onRefreshList()
    }

    //defining Interface
    fun setOnClickedListener(callback: LabChiefComplaintListener) {
        this.mCallback = callback
    }

    interface LabChiefComplaintListener {
        fun sendDataChiefComplaint()
    }


    fun setTempRefresh(tCallback: RadiologyTempReferesh) {
        this.tCallback = tCallback
    }

    interface RadiologyTempReferesh {
        fun onRefresh()
    }


}