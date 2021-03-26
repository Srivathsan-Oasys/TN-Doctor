package com.hmis_tn.doctor.ui.emr_workflow.radiology.ui


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogManageRadiologyTemplateBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.*
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.gettemplate.ResponseContentLabGetDetails
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.request.Detail
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.request.Headers
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.request.RequestTemplateAddDetails
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.response.ReponseTemplateadd
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.updaterequest.NewDetail
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.updaterequest.RemovedDetail
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.updaterequest.UpdateRequestModule
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.updateresponse.UpdateResponse
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.PrescriptionDurationResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.ManageTemplateModel
import com.hmis_tn.doctor.ui.emr_workflow.radiology.view_model.ManageRadiologyTemplateViewModel
import com.hmis_tn.doctor.ui.emr_workflow.radiology.view_model.ManageRadiologyTemplateViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class ManageRadiologyTemplateFragment : DialogFragment() {

    private var Itemdescription: String? = null
    private var Itemname: String? = ""
    private var deparment_UUID: Int? = 0
    private var content: String? = null
    var displayOrder: ArrayList<Int> = ArrayList()

    private var customdialog: Dialog? = null
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    private var viewModel: ManageRadiologyTemplateViewModel? = null
    var binding: DialogManageRadiologyTemplateBinding? = null
    val removeList: ArrayList<RemovedDetail> = ArrayList()
    var UpdateRequestModule: UpdateRequestModule = UpdateRequestModule()
    private var facility_UUID: Int? = 0
    private var favAddResponseMap = mutableMapOf<Int, String>()
    var dropdownReferenceView: AutoCompleteTextView? = null
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var mAdapter: RadiologyManageTemplateAdapter? = null
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
    var callbacktemplate: OnTemplateRefreshListener? = null
    var RequestTemplateAddDetails: RequestTemplateAddDetails = RequestTemplateAddDetails()
    var arraylistresponse: ArrayList<ResponseContentsfav?> = ArrayList()
    var rasponsecontentLabGetTemplateDetails: ResponseContentLabGetDetails =
        ResponseContentLabGetDetails()
    var status: Boolean? = false
    var newDetailList: ArrayList<NewDetail> = ArrayList()
    var ispublic = "false"


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
        hideSoftKeyboard(view)
        utils = Utils(this.requireContext())
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding?.templateRadiologyRecyclerView!!.layoutManager = layoutmanager
        mAdapter = RadiologyManageTemplateAdapter(requireContext(), ArrayList())
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

        mAdapter?.setOnDeleteClickListener(object :
            RadiologyManageTemplateAdapter.OnDeleteClickListener {
            override fun onDeleteClick(
                responseData: ResponseContentsfav?,
                position: Int
            ) {

                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog!!.dismiss()
                }
                val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
//                drugNmae.text = responseData?.test_master_name
                drugNmae.text =
                    "${drugNmae.text} '" + responseData?.test_master_name + "' Record ?"
                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {


                    val removedDetail: RemovedDetail = RemovedDetail()
                    removedDetail.template_uuid = responseData?.template_id
                    removedDetail.template_details_uuid = responseData?.template_details_uuid
                    removeList.add(removedDetail)
                    mAdapter?.removeItem(position)

                    customdialog!!.dismiss()
                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                }
                customdialog!!.show()

            }

        })


        binding?.save?.setOnClickListener {
            val arraydata = mAdapter?.getItems()


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

            if (status!!) {
                /*
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
                    header?.department_uuid = deparment_UUID?.toString()
                    header?.display_order = displayordervalue
                    header?.revision = true
                    header?.is_active = binding?.switchId?.isChecked

                    RequestTemplateAddDetails.headers = this.header!!
                    RequestTemplateAddDetails.details = this.detailsList

                    val request = Gson().toJson(RequestTemplateAddDetails)

                    viewModel?.radiologyTemplateDetails(
                        facility_UUID,
                        RequestTemplateAddDetails,
                        emrradilogytemplatepostRetrofitCallback
                    )

                }
            } else {
                /*
                Edit Details
                 */
                val displayordervalue = binding?.displayorder?.text.toString()
                arrayItemData = mAdapter?.getItems()
                if (arrayItemData?.size!! > 0) {
                    header?.template_id = arrayItemData?.get(0)?.template_id
                    header?.name = binding?.editName?.text.toString()
                    header?.description = Itemdescription
                    header?.template_type_uuid = AppConstants.FAV_TYPE_ID_RADIOLOGY
                    header?.diagnosis_uuid = 0
                    if (binding!!.myDepartment!!.isChecked) {
                        ispublic = "true"
                    } else {
                        ispublic = "false"
                    }
                    header?.is_public = ispublic
                    header?.facility_uuid = facility_UUID?.toString()
                    header?.department_uuid = deparment_UUID?.toString()
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
        }



        binding?.addDetail?.setOnClickListener {

            val displayorder = binding?.displayorder?.text?.toString()
            val templeteName = binding?.editName?.text?.toString()
            if (templeteName?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Please Enter Template name", Toast.LENGTH_LONG)
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
            if (Str_auto_name?.isNotEmpty()!!) {
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
                        newDetail.template_master_uuid = arrayItemData?.get(0)?.template_id
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
        val args = arguments
        if (args == null) {

            status = true

            // Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()

        } else {
            // get value from bundle..


            status = args.getBoolean("status")

            displayOrder = args.getIntegerArrayList("DisplayOrder")!!


            if (!status!!) {
                rasponsecontentLabGetTemplateDetails =
                    args.getParcelable(AppConstants.RESPONSECONTENT)!!
                Itemname = rasponsecontentLabGetTemplateDetails.temp_details?.template_name
                Itemdescription =
                    rasponsecontentLabGetTemplateDetails.temp_details?.template_description
                binding?.UserName?.setText(userDataStoreBean?.user_name)
                binding?.addDetail?.text = "Add"
                binding?.save?.text = "Update"
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
                for (i in rasponsecontentLabGetTemplateDetails.radiology_details?.indices!!) {
                    val ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
                    ResponseContantSave?.template_details_uuid =
                        rasponsecontentLabGetTemplateDetails.radiology_details?.get(i)?.template_details_uuid
                    ResponseContantSave?.template_id =
                        rasponsecontentLabGetTemplateDetails.temp_details?.template_id!!
                    ResponseContantSave?.test_master_name =
                        rasponsecontentLabGetTemplateDetails.radiology_details?.get(i)?.lab_name
                    ResponseContantSave?.test_master_id =
                        rasponsecontentLabGetTemplateDetails.radiology_details?.get(i)?.lab_test_uuid
                    ResponseContantSave?.test_master_code =
                        rasponsecontentLabGetTemplateDetails.radiology_details?.get(i)?.lab_code
                    arraylistresponse.add(ResponseContantSave)

                }
                mAdapter?.setFavAddItem(arraylistresponse)


            }


        }




        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.cancel?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.clear?.setOnClickListener({
            binding?.displayorder?.setText("")
            binding?.autoCompleteTextViewTestName?.setText("")
            binding?.editName?.setText("")
            binding?.editDescription?.setText("")
            binding?.editName?.clearFocus()
            binding?.editDescription?.clearFocus()
            binding?.displayorder?.clearFocus()
            binding?.autoCompleteTextViewTestName?.clearFocus()

        })


        binding?.spinnerRadiologydepartment?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
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


            Str_auto_code = autocompleteTestResponse?.get(position)?.code
            Str_auto_name = autocompleteTestResponse?.get(position)?.name

            Str_auto_id = autocompleteTestResponse?.get(position)?.uuid


        }



        return binding!!.root
    }

    private fun hideSoftKeyboard(view: View?) {
        if (view != null) {
            val inputManager = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


/*
Response
 */

    val emrradilogytemplatepostRetrofitCallback = object : RetrofitCallback<ReponseTemplateadd> {
        override fun onSuccessfulResponse(responseBody: Response<ReponseTemplateadd>?) {
            Toast.makeText(
                requireContext(),
                responseBody?.body()?.message ?: "",
                Toast.LENGTH_SHORT
            ).show()
/*
            alertDialog(responseBody?.body()?.message ?: "")
*/
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )
            mAdapter?.cleardata()
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


    val templateRadiodepartmentRetrofitCallBack =
        object : RetrofitCallback<FavAddResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavAddResponseModel>) {

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

    val tempRadiologyAddAllDepartmentCallBack =
        object : RetrofitCallback<FavAddAllDepatResponseModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {

                listAllAddDepartmentItems = responseBody?.body()?.responseContents!!
                favAddResponseMap =
                    listAllAddDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()
                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        R.layout.spinner_item,
                        favAddResponseMap.values.toMutableList()
                    )

                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.spinnerRadiologydepartment!!.adapter = adapter
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
    val favAddTestNameCallBack = object : RetrofitCallback<FavAddTestNameResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavAddTestNameResponse>?) {


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

    fun setOnTemplateRefreshListener(callback: OnTemplateRefreshListener) {
        this.callbacktemplate = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnTemplateRefreshListener {
        fun onRefreshList()
    }

    val UpdateemrlabtemplatepostRetrofitCallback = object : RetrofitCallback<UpdateResponse> {
        override fun onSuccessfulResponse(responseBody: Response<UpdateResponse>?) {
            Toast.makeText(
                requireContext(),
                responseBody?.body()?.message ?: "",
                Toast.LENGTH_SHORT
            ).show()


            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )


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

    private fun alertDialog(msg: String) {
        val builderA = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        builderA.setTitle(getString(R.string.app_name))
        builderA.setMessage(msg)
        builderA.setPositiveButton("OK") { d, _ -> d?.dismiss() }
        builderA.create().show()
    }


}