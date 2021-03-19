package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogManageLmisLabTemplateBinding
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
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.LabtechData
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.view_model.ManageLmisLabTemplateViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.view_model.manageLmisLabTemplateViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrescriptionDurationResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.oasys.digihealth.doctor.utils.Utils
import kotlinx.android.synthetic.main.custom_layout.*
import retrofit2.Response
import java.io.IOException


class SaveTemplateManageLmisLabTemplateFragment : DialogFragment() {
    private var favouriteData: ArrayList<LabtechData>? = ArrayList()
    private var templeteID: Int? = 0
    private var viewModel: ManageLmisLabTemplateViewModel? = null
    var binding: DialogManageLmisLabTemplateBinding? = null
    var appPreferences: AppPreferences? = null
    val detailsList: ArrayList<Detail> = ArrayList()
    val header: Headers? = Headers()
    private var utils: Utils? = null
    var callbacktemplate: OnsaveTemplateRefreshListener? = null
    private var mAdapter: lmisLabManageTemplateAdapter? = null
    private var arrayItemData: ArrayList<ResponseContentsfav?>? = null
    private var facility_UUID: Int? = 0
    private var Str_auto_id: Int? = 0
    private var Str_auto_name: String? = ""
    private var customdialog: Dialog? = null
    var RequestTemplateAddDetails: RequestTemplateAddDetails = RequestTemplateAddDetails()
    private var Str_auto_code: String? = ""
    private var deparment_UUID: Int? = 0
    var LabUUID: Int? = 0
    val removeList: ArrayList<RemovedDetail> = ArrayList()
    var ispublic = "false"
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var favAddResponseMap = mutableMapOf<Int, String>()
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    var rasponsecontentLabGetTemplateDetails: ResponseContentLabGetDetails =
        ResponseContentLabGetDetails()
    var arraylistresponse: ArrayList<ResponseContentsfav?> = ArrayList()
    var UpdateRequestModule: UpdateRequestModule = UpdateRequestModule()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var status: Boolean? = false
    private var Itemname: String? = ""
    private var Itemdescription: String? = ""
    var newDetailList: ArrayList<NewDetail> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_manage_lmis_lab_template,
                container,
                false
            )
        viewModel = manageLmisLabTemplateViewModelFactory(
            requireActivity().application
        )
            .create(ManageLmisLabTemplateViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding?.labManageTemplateRecyclerView!!.layoutManager = layoutmanager
        mAdapter = lmisLabManageTemplateAdapter(requireContext(), ArrayList())
        binding?.labManageTemplateRecyclerView!!.adapter = mAdapter

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        LabUUID = appPreferences?.getInt(AppConstants.LAB_UUID)

        binding?.userNameTextView?.setText(userDataStoreBean?.user_name)

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
//        binding?.viewModel?.getDepartmentList(facility_UUID, FavLabdepartmentRetrofitCallBack)
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

        mAdapter?.setOnDeleteClickListener(object :
            lmisLabManageTemplateAdapter.OnDeleteClickListener {
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
                    Log.i("", "" + responseData)

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


        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {

            arraylistresponse.clear()
            favouriteData = args.getParcelableArrayList<LabtechData>(AppConstants.RESPONSECONTENT)
            for (i in favouriteData!!.indices) {
                val ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
                ResponseContantSave?.test_master_name = favouriteData!![i].labName
                ResponseContantSave?.test_master_id = favouriteData!![i].labId
                ResponseContantSave?.test_master_code = favouriteData!![i].code.toString()
                if (i != favouriteData?.size!! - 1) {
                    arraylistresponse.add(ResponseContantSave)

                } else {

                }

            }
            mAdapter?.setFavAddItem(arraylistresponse)
        }

        /*    val args = arguments
            if (args == null) {
                status = true
                //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
            } else {
                // get value from bundle..
                rasponsecontentLabGetTemplateDetails = args.getParcelable(AppConstants.RESPONSECONTENT)!!
                Itemname = rasponsecontentLabGetTemplateDetails.temp_details?.template_name
                Itemdescription = rasponsecontentLabGetTemplateDetails.temp_details?.template_description
                binding?.save?.text = "Update"
                binding?.templateName?.setText(Itemname)
                binding?.templateName?.isClickable = false
                binding?.templateName?.isFocusable = false
                binding?.nameText?.setText(rasponsecontentLabGetTemplateDetails?.temp_details?.template_name!!)
                binding?.description?.setText(rasponsecontentLabGetTemplateDetails?.temp_details?.template_description)
                binding?.userNameTextView?.setText(userDataStoreBean?.user_name)
                binding?.editTextDisplayOrder?.setText(rasponsecontentLabGetTemplateDetails.temp_details?.template_displayorder?.toString())
                arraylistresponse.clear()
                for(i in rasponsecontentLabGetTemplateDetails.lab_details?.indices!!)
                {
                    val ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
                    ResponseContantSave?.template_details_uuid = rasponsecontentLabGetTemplateDetails?.lab_details?.get(i)?.template_details_uuid
                    ResponseContantSave?.template_id = rasponsecontentLabGetTemplateDetails?.temp_details?.template_id!!
                    templeteID = rasponsecontentLabGetTemplateDetails?.temp_details?.template_id
                    ResponseContantSave?.test_master_name = rasponsecontentLabGetTemplateDetails.lab_details?.get(i)?.lab_name
                    ResponseContantSave?.test_master_id = rasponsecontentLabGetTemplateDetails.lab_details?.get(i)?.lab_test_uuid
                    ResponseContantSave?.test_master_code = rasponsecontentLabGetTemplateDetails.lab_details?.get(i)?.lab_code
                    arraylistresponse.add(ResponseContantSave)

                }
                mAdapter?.setFavAddItem(arraylistresponse)

            }*/
        binding?.addFav?.setOnClickListener {
            val displayorder = binding?.editTextDisplayOrder?.text?.toString()
            if (Str_auto_name?.isNotEmpty()!! && displayorder?.isNotEmpty()!!) {
                arrayItemData = mAdapter?.getItems()
                val testmasterId = Str_auto_id
                val check = arrayItemData!!.any { it!!.test_master_id == testmasterId }

                if (!check) {
                    if (status as Boolean) {
                        Itemname = binding?.templateName?.text.toString()
                        val responseContentsfav = ResponseContentsfav()
                        responseContentsfav.test_master_name = Str_auto_name
                        responseContentsfav.test_master_id = testmasterId
                        responseContentsfav.test_master_code = Str_auto_code
                        binding?.autoCompleteTextViewTestName?.setText("")
                        Str_auto_name = ""
                        arraylistresponse.add(responseContentsfav)

                        mAdapter?.setFavAddItem(arraylistresponse)
                    } else {
                        ///Update
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

                        Itemname = binding?.templateName?.text.toString()
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
        binding?.save?.setOnClickListener {
            val displayordervalue = binding?.editTextDisplayOrder?.text.toString()
            val description = binding?.description?.text?.toString()
            val name = binding?.nameText?.text?.toString()
            //Add details
            if (binding?.editTextDisplayOrder?.text?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Please Enter Display Order", Toast.LENGTH_LONG)
                    ?.show()

                return@setOnClickListener

            }
            if (binding?.description?.text?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Please Enter Description", Toast.LENGTH_LONG)
                    ?.show()

                return@setOnClickListener
            }
            if (binding?.nameText?.text?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Please Enter Template name", Toast.LENGTH_LONG)
                    ?.show()

                return@setOnClickListener
            }

            //Add details

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
                    details.department_uuid = deparment_UUID
                    details.duration_period_uuid = 0
                    details.drug_instruction_uuid = 0
                    details.revision = true
                    details.is_active = true
                    details.lab_uuid = LabUUID
                    detailsList.add(details)
                }
                header?.name = name
                header?.description = description
                header?.template_type_uuid = AppConstants.FAV_TYPE_ID_LAB
                header?.diagnosis_uuid = 0
                header?.facility_uuid = facility_UUID?.toString()
                header?.department_uuid = deparment_UUID?.toString()
                header?.display_order = displayordervalue
                header?.revision = true
                header?.is_active = true
                header?.lab_uuid = LabUUID
                RequestTemplateAddDetails.headers = header!!
                RequestTemplateAddDetails.details = this.detailsList

                val request = Gson().toJson(RequestTemplateAddDetails)

                Log.i("", "" + request)
                viewModel?.labTemplateDetails(
                    facility_UUID,
                    RequestTemplateAddDetails,
                    emrlabtemplatepostRetrofitCallback
                )

            }


        }


        return binding!!.root
    }

    /*   val FavLabdepartmentRetrofitCallBack =
           object : RetrofitCallback<FavAddResponseModel> {
               override fun onSuccessfulResponse(response: Response<FavAddResponseModel>) {
                   Log.i("", "" + response.body()?.responseContent);
                   Log.i("", "" + response.body()?.responseContent);
                   listDepartmentItems.add(response.body()?.responseContent)
                   favAddResponseMap =
                       listDepartmentItems.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

                   val adapter =
                       ArrayAdapter<String>(
                           requireContext(),
                           R.layout.spinner_item,
                           favAddResponseMap.values.toMutableList()
                       )
                   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                   binding?.spinnerdepartment!!.adapter = adapter

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
           }*/

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

    val emrlabtemplatepostRetrofitCallback = object : RetrofitCallback<ReponseTemplateadd> {
        override fun onSuccessfulResponse(responseBody: Response<ReponseTemplateadd>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )
            mAdapter?.cleardata()
            viewModel!!.getTemplete(LabUUID!!, getTempleteRetrofitCallBack)


        }

        override fun onBadRequest(response: Response<ReponseTemplateadd>) {
            val gson = GsonBuilder().create()
            if (response.code() == 400) {
                val gson = GsonBuilder().create()
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

            val responseModel: ManageLmisLabTemplateViewModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    ManageLmisLabTemplateViewModel::class.java
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
            viewModel!!.getTemplete(LabUUID!!, getTempleteRetrofitCallBack)


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


    fun setOnSaveTemplateRefreshListener(callback: OnsaveTemplateRefreshListener) {
        this.callbacktemplate = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnsaveTemplateRefreshListener {
        fun onRefreshList()
    }

}