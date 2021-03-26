package com.hmis_tn.doctor.ui.emr_workflow.investigation.ui

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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogManageInvestigationTemplateBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.view_model.ManageInvestTemplateViewModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.view_model.ManageInvestTemplateViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddResponseModel
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
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class ManageISavenvestTemplateFragment : DialogFragment() {

    private var deparment_UUID: Int? = 0
    private var content: String? = null
    private var customdialog: Dialog? = null

    private var autocompleteTestResponse: List<InvestigationSearchResponseContent>? = null
    private var viewModel: ManageInvestTemplateViewModel? = null
    var binding: DialogManageInvestigationTemplateBinding? = null
    private var facility_UUID: Int? = 0
    private var favAddResponseMap = mutableMapOf<Int, String>()
    var dropdownReferenceView: AutoCompleteTextView? = null
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var mAdapter: ManageTemplatenstituteAdapter? = null
    private var Str_auto_id: Int? = 0
    private var Str_auto_name: String? = ""
    private var Str_auto_code: String? = ""
    var status: Boolean? = false
    var rasponsecontentLabGetTemplateDetails: ResponseContentLabGetDetails =
        ResponseContentLabGetDetails()
    private var Itemname: String? = ""
    private var Itemdescription: String? = ""
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var arraylistresponse: ArrayList<ResponseContentsfav?> = ArrayList()
    val removeList: ArrayList<RemovedDetail> = ArrayList()
    var ispublic = "false"
    val detailsList: ArrayList<Detail> = ArrayList()
    private var arrayItemData: ArrayList<ResponseContentsfav?>? = null
    val header: Headers? = Headers()
    var RequestTemplateAddDetails: RequestTemplateAddDetails = RequestTemplateAddDetails()
    var UpdateRequestModule: UpdateRequestModule = UpdateRequestModule()
    var newDetail: NewDetail = NewDetail()
    var newDetailList: ArrayList<NewDetail> = ArrayList()
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    var callbacktemplate: OnTemplateRefreshListener? = null


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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_manage_investigation_template,
            container,
            false
        )
        viewModel = ManageInvestTemplateViewModelFactory(
            requireActivity().application
        ).create(ManageInvestTemplateViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        binding?.viewModel?.getRadiologyDepartmentList(
            facility_UUID,
            templateRadiodepartmentRetrofitCallBack
        )

/*
        binding?.clear?.setOnClickListener({
            binding?.autoCompleteTextViewTestName!!.setText("");
            Str_auto_id =0;
        })
*/
        binding?.clear?.setOnClickListener {
            binding?.dispalyOrder?.setText("")
            binding?.autoCompleteTextViewTestName?.setText("")
            binding?.editName?.setText("")

            binding?.editDescription?.setText("")

        }




        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.spinnerInvestdepartment?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            binding?.viewModel?.getRadiologyAllDepartment(
                facility_UUID,
                tempRadiologyAddAllDepartmentCallBack
            )
            false
        })


        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..
            rasponsecontentLabGetTemplateDetails =
                args.getParcelable(AppConstants.RESPONSECONTENT)!!
            Itemname = rasponsecontentLabGetTemplateDetails.temp_details?.template_name
            Itemdescription =
                rasponsecontentLabGetTemplateDetails.temp_details?.template_description
            binding?.cheifAdd?.text = "Add"
            binding?.savebutton?.text = "Update"
            binding?.UserName?.setText(userDataStoreBean?.user_name)
            binding?.editName?.setText(Itemname)
            binding?.editDescription?.setText(Itemdescription)
            binding?.dispalyOrder?.setText(rasponsecontentLabGetTemplateDetails.temp_details?.template_displayorder?.toString())
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

        mAdapter?.setOnDeleteClickListener(object :
            ManageTemplatenstituteAdapter.OnDeleteClickListener {
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

        binding?.save?.setOnClickListener {

            val arraydata = mAdapter?.getItems()
            if (arraydata?.size!! > 0) {
                if (binding!!.myDepartment!!.isChecked) {

                    ispublic = "true"
                }




                if (status as Boolean) {   /*
                Add Details
                 */

                    val displayordervalue = binding?.dispalyOrder?.text.toString()
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

                        header?.name = Itemname
                        header?.description = Itemdescription
                        header?.template_type_uuid = AppConstants.FAV_TYPE_ID_LAB
                        header?.diagnosis_uuid = 0
                        header?.is_public = ispublic
                        header?.facility_uuid = facility_UUID?.toString()
                        header?.department_uuid = deparment_UUID?.toString()
                        header?.display_order = binding?.dispalyOrder?.text?.toString()
                        header?.revision = true
                        header?.is_active = true

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
                    /*
                    Edit Details
                     */
                    val displayordervalue = binding?.dispalyOrder?.text.toString()
                    arrayItemData = mAdapter?.getItems()
                    if (arrayItemData?.size!! > 0) {
                        header?.template_id = arrayItemData?.get(0)?.template_id
                        header?.name = Itemname
                        header?.description = Itemdescription
                        header?.template_type_uuid = AppConstants.FAV_TYPE_ID_LAB
                        header?.diagnosis_uuid = 0
                        header?.is_public = ispublic
                        header?.facility_uuid = facility_UUID?.toString()
                        header?.department_uuid = deparment_UUID?.toString()
                        header?.display_order = displayordervalue
                        header?.revision = true
                        header?.is_active = true
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



        return binding!!.root
    }

    val templateRadiodepartmentRetrofitCallBack =
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
                        android.R.layout.simple_spinner_item,
                        favAddResponseMap.values.toMutableList()
                    )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.spinnerInvestdepartment!!.adapter = adapter
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
                Log.i("", "" + responseBody?.body()?.responseContents)
                listAllAddDepartmentItems = responseBody?.body()?.responseContents!!
                favAddResponseMap =
                    listAllAddDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()
                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        favAddResponseMap.values.toMutableList()
                    )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.spinnerInvestdepartment!!.adapter = adapter
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
    val favAddTestNameCallBack = object : RetrofitCallback<InvestigationSearchResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<InvestigationSearchResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            autocompleteTestResponse = responseBody?.body()?.responseContents
            val responseContentAdapter = FavInvestigationTestNameSearchResultAdapter(
                context!!,
                R.layout.row_chief_complaint_search_result,
                responseBody?.body()?.responseContents!!
            )
            binding?.autoCompleteTextViewTestName?.threshold = 1
            binding?.autoCompleteTextViewTestName?.setAdapter(responseContentAdapter)

        }

        override fun onBadRequest(errorBody: Response<InvestigationSearchResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: InvestigationSearchResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    InvestigationSearchResponseModel::class.java
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

    val emrlabtemplatepostRetrofitCallback = object : RetrofitCallback<ReponseTemplateadd> {
        override fun onSuccessfulResponse(responseBody: Response<ReponseTemplateadd>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )
            mAdapter?.cleardata()
            Toast.makeText(context, responseBody?.body()?.message, Toast.LENGTH_SHORT).show()
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


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
            }
        }
    }

    /*
Update Response
 */


    val UpdateemrlabtemplatepostRetrofitCallback = object : RetrofitCallback<UpdateResponse> {
        override fun onSuccessfulResponse(responseBody: Response<UpdateResponse>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )


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