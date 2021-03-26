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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.isTablet
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogManageInvestigationTemplateBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.ChiefComplaintFavAddresponseModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationGetTemplateDetailsResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.models.InvestigationListData
import com.hmis_tn.doctor.ui.emr_workflow.investigation.view_model.ManageInvestTemplateViewModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.view_model.ManageInvestTemplateViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.*
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
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
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.row_prev_certificate.*
import retrofit2.Response
import java.io.IOException

class ManageInvestTemplateFragment : DialogFragment() {

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
    private var mAdapterMob: ManageTemplatMobileAdapter? = null
    private var Str_auto_id: Int? = 0
    private var Str_auto_name: String? = ""
    private var Str_auto_code: String? = ""
    var status: Boolean? = false
    var rasponsecontentLabGetTemplateDetails: InvestigationGetTemplateDetailsResponseContent =
        InvestigationGetTemplateDetailsResponseContent()
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
        binding?.UserName?.setText(userDataStoreBean?.user_name)



        binding?.viewModel?.getRadiologyDepartmentList(
            facility_UUID,
            templateRadiodepartmentRetrofitCallBack
        )

        if (isTablet(requireContext())) {
            mAdapter = ManageTemplatenstituteAdapter(requireContext(), ArrayList())
            binding?.manageInstituteData!!.adapter = mAdapter
        } else {

            mAdapterMob = ManageTemplatMobileAdapter(requireContext(), ArrayList())


            val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding?.manageInstituteData?.layoutManager = linearLayoutManager
            binding?.manageInstituteData!!.adapter = mAdapterMob


            mAdapterMob?.setOnDeleteClickListener(object :
                ManageTemplatMobileAdapter.OnDeleteClickListener {
                override fun onDeleteClick(
                    responseData: ResponseContentsfav?,
                    position: Int
                ) {


                    val removedDetail: RemovedDetail = RemovedDetail()
                    removedDetail.template_uuid = responseData?.template_id
                    removedDetail.template_details_uuid = responseData?.template_details_uuid
                    removeList.add(removedDetail)
                    mAdapterMob?.removeItem(position)

                }
            })
        }
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


        binding?.cancel?.setOnClickListener {
            dialog?.dismiss()
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


            var st = args.getString("status")!!

            if (st == "Edit") {
                rasponsecontentLabGetTemplateDetails =
                    args.getParcelable(AppConstants.RESPONSECONTENT)!!
                Itemname = rasponsecontentLabGetTemplateDetails.temp_details.template_name

                if (rasponsecontentLabGetTemplateDetails.temp_details.is_public) {


                    binding?.myDepartment?.isChecked = true

                } else {

                    binding?.mySelf?.isChecked = true


                }
                Itemdescription =
                    rasponsecontentLabGetTemplateDetails.temp_details.template_description
                binding?.cheifAdd?.text = "Add"
                binding?.savebutton?.text = "Update"
                binding?.UserName?.setText(userDataStoreBean?.user_name)
                binding?.editName?.setText(Itemname)
                binding?.editDescription?.setText(Itemdescription)
                binding?.dispalyOrder?.setText(rasponsecontentLabGetTemplateDetails.temp_details.template_displayorder.toString())
                arraylistresponse.clear()

                for (i in rasponsecontentLabGetTemplateDetails.Invest_details.indices) {
                    val ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
                    ResponseContantSave?.template_details_uuid =
                        rasponsecontentLabGetTemplateDetails.Invest_details.get(i).template_details_uuid
                    ResponseContantSave?.template_id =
                        rasponsecontentLabGetTemplateDetails.temp_details.template_id
                    ResponseContantSave?.test_master_name =
                        rasponsecontentLabGetTemplateDetails.Invest_details.get(i).lab_name
                    ResponseContantSave?.test_master_id =
                        rasponsecontentLabGetTemplateDetails.Invest_details.get(i).lab_test_uuid
                    ResponseContantSave?.test_master_code =
                        rasponsecontentLabGetTemplateDetails.Invest_details.get(i).lab_code
                    arraylistresponse.add(ResponseContantSave)

                }

                if (isTablet(requireContext()))
                    mAdapter?.setFavAddItem(arraylistresponse)
                else
                    mAdapterMob?.setFavAddItem(arraylistresponse)

            } else {


                status = true

                var ListData = args.getParcelableArrayList<InvestigationListData>("ListData")!!

                arraylistresponse.clear()

                for (i in ListData.indices) {
                    val ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()

                    ResponseContantSave?.test_master_name =
                        ListData[i].investigation_name
                    ResponseContantSave?.test_master_id =
                        ListData[i].investigation_id
                    ResponseContantSave?.test_master_code =
                        ListData[i].investigation_code
                    arraylistresponse.add(ResponseContantSave)

                }

                if (isTablet(requireContext()))
                    mAdapter?.setFavAddItem(arraylistresponse)
                else
                    mAdapterMob?.setFavAddItem(arraylistresponse)

            }
        }

        mAdapter?.setOnDeleteClickListener(object :
            ManageTemplatenstituteAdapter.OnDeleteClickListener {
            override fun onDeleteClick(
                responseData: ResponseContentsfav?,
                position: Int
            ) {


                val removedDetail: RemovedDetail = RemovedDetail()
                removedDetail.template_uuid = responseData?.template_id
                removedDetail.template_details_uuid = responseData?.template_details_uuid
                removeList.add(removedDetail)
                mAdapter?.removeItem(position)

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

        binding?.addCardView?.setOnClickListener {
            val displayorder = binding?.dispalyOrder?.text?.toString()
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
                //arrayItemData = mAdapter?.getItems()
                //val testmasterId = Str_auto_id
                //val check= arrayItemData!!.any{ it!!.test_master_id == testmasterId}

                //if (!check) {
                //if(status as Boolean){
                Itemname = binding?.editName?.text.toString()
                Itemdescription = binding?.editDescription?.text.toString()
                val responseContentsfav = ResponseContentsfav()
                responseContentsfav.test_master_name = Str_auto_name
                responseContentsfav.test_master_id = Str_auto_id
                responseContentsfav.test_master_code = Str_auto_code
                binding?.autoCompleteTextViewTestName?.setText("")
                Str_auto_name = ""
                //arraylistresponse.add(responseContentsfav)

                if (isTablet(requireContext()))
                    mAdapter?.setTemplateItem(responseContentsfav)
                else
                    mAdapterMob?.setTemplateItem(responseContentsfav)
                //}
                /*else{
                    ///Up
                    val newDetail :NewDetail = NewDetail()
                    newDetail.template_master_uuid = arrayItemData?.get(0)?.template_id
                    newDetail.test_master_uuid = testmasterId
                    newDetail.chief_complaint_uuid=0
                    newDetail.vital_master_uuid=0
                    newDetail.drug_id=0
                    newDetail.drug_route_uuid=0
                    newDetail.drug_frequency_uuid=0
                    newDetail.drug_duration=0
                    newDetail.drug_period_uuid=0
                    newDetail.drug_instruction_uuid=0
                    newDetail.display_order=0
                    newDetail.quantity=0
                    newDetail.revision=true
                    newDetail.is_active=true
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

                }*/
                //}
                //else{
                //Toast.makeText(context,"Already Item available in the list",Toast.LENGTH_LONG)?.show()
                // }

            } else {
                Toast.makeText(context, "Please select all field", Toast.LENGTH_LONG).show()

            }

        }


        binding?.save?.setOnClickListener {

            Itemname = binding?.editName?.text.toString()

            if (isTablet(requireContext())) {

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

                            header?.name = binding?.editName?.text.toString()
                            header?.description = Itemdescription
                            header?.template_type_uuid = AppConstants.FAV_TYPE_ID_INVESTIGATION
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
                            header?.name = binding?.editName?.text.toString()
                            header?.description = Itemdescription
                            header?.template_type_uuid = AppConstants.FAV_TYPE_ID_INVESTIGATION
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
                    Toast.makeText(
                        requireContext(),
                        "Please select any one item",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }


            } else {
                val arraydata = mAdapterMob?.getItems()
                if (arraydata?.size!! > 0) {
                    if (binding!!.myDepartment!!.isChecked) {

                        ispublic = "true"
                    }




                    if (status as Boolean) {   /*
                Add Details
                 */

                        val displayordervalue = binding?.dispalyOrder?.text.toString()
                        arrayItemData = mAdapterMob?.getItems()
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
                            header?.template_type_uuid = AppConstants.FAV_TYPE_ID_INVESTIGATION
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
                        arrayItemData = mAdapterMob?.getItems()
                        if (arrayItemData?.size!! > 0) {
                            header?.template_id = arrayItemData?.get(0)?.template_id
                            header?.name = Itemname
                            header?.description = Itemdescription
                            header?.template_type_uuid = AppConstants.FAV_TYPE_ID_INVESTIGATION
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
                    Toast.makeText(
                        requireContext(),
                        "Please select any one item",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

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
            binding?.autoCompleteTextViewTestName?.showDropDown()

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
            if (isTablet(requireContext()))
                mAdapter?.cleardata()
            else
                mAdapterMob?.cleardata()

            Toast.makeText(context, responseBody?.body()?.message, Toast.LENGTH_SHORT).show()
            viewModel!!.getTemplete(getTempleteRetrofitCallBack)


        }

        override fun onBadRequest(response: Response<ReponseTemplateadd>) {

            val gson = GsonBuilder().create()
            val responseModel: ChiefComplaintFavAddresponseModel
            var mError = ErrorAPIClass()
            try {
                mError =
                    gson.fromJson(response.errorBody()!!.string(), ErrorAPIClass::class.java)
                Toast.makeText(
                    context,
                    mError.message,
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: IOException) { // handle failure to read error
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
            if (isTablet(requireContext()))
                mAdapter?.cleardata()
            else
                mAdapterMob?.cleardata()

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