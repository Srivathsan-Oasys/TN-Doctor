package com.hmis_tn.doctor.ui.emr_workflow.diet.ui

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
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogManageDietTemplateBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.TempDetails
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.TemplateDetailsResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.TemplateDietDetail
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.request.RequestDietFavModel
import com.hmis_tn.doctor.ui.emr_workflow.diet.view_model.ManageDietTemplateViewModel
import com.hmis_tn.doctor.ui.emr_workflow.diet.view_model.ManageDietTemplateViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.ui.FavInvestigationTestNameSearchResultAdapter
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.request.Detail
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.request.Headers
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.request.RequestTemplateAddDetails
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.response.ReponseTemplateadd
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.updaterequest.NewDetail
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.updaterequest.RemovedDetail
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.updaterequest.UpdateRequestModule
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.updateresponse.UpdateResponse
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.PrescriptionDurationResponseModel
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class ManageDietTemplateFragment : DialogFragment() {

    private var content: String = ""
    private var Itemname: String = ""
    private var Itemdescription: String = ""
    private var arrayItemData: ArrayList<ResponseContentsfav?>? = null
    private var Str_auto_id: Int = 0
    private var Str_auto_name: String = ""
    private var Str_auto_code: String = ""
    private var testName: String = ""
    private var viewModel: ManageDietTemplateViewModel? = null
    var binding: DialogManageDietTemplateBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int = 0
    private var deparment_UUID: Int = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var listDepartmentItems: ArrayList<FavAddAllDepatResponseContent?> = ArrayList()
    private var listCategoryItems: ArrayList<FavAddAllDepatResponseContent?> = ArrayList()
    private var listFavFrequencyItems: ArrayList<FavAddAllDepatResponseContent?> = ArrayList()
    private var favAddResponseMapDepart = mutableMapOf<Int, String>()
    private var favAddResponseMapCategoty = mutableMapOf<Int, String>()
    private var favAddResponseMapFrequency = mutableMapOf<Int, String>()
    private var autocompleteTestResponse: List<InvestigationSearchResponseContent>? = null
    var status: Boolean = false
    val header: Headers = Headers()
    val detailsList: ArrayList<Detail> = ArrayList()
    private var is_active: Boolean = true
    val emrFavRequestModel: RequestDietFavModel = RequestDietFavModel()
    var dietSearchuuid: Int = 0
    private var deletefavouriteID: Int = 0
    private var customdialog: Dialog? = null
    private var mAdapter: DietManageTemplateAdapter? = null
    var callbacktemplate: OnTemplateRefreshListener? = null
    private lateinit var listDepartmentID: List<Int?>
    private lateinit var listCategoryID: List<Int?>
    private lateinit var listFrequencyID: List<Int?>
    private var favouriteData: FavouritesModel? = null
    private var ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
    val removeList: ArrayList<RemovedDetail> = ArrayList()
    var newDetailList: ArrayList<NewDetail> = ArrayList()
    var arraylistresponse: ArrayList<ResponseContentsfav?> = ArrayList()
    var UpdateRequestModule: UpdateRequestModule = UpdateRequestModule()

    var RequestTemplateAddDetails: RequestTemplateAddDetails = RequestTemplateAddDetails()
    var rasponsecontentLabGetTemplateDetails: TemplateDetailsResponseContent? =
        TemplateDetailsResponseContent()

    private var departmentList = mutableMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG) ?: ""
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
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
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_manage_diet_template,
                container,
                false
            )
        viewModel = ManageDietTemplateViewModelFactory(
            requireActivity().application
        )
            .create(ManageDietTemplateViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        //Recycleview adapter init

        mAdapter = DietManageTemplateAdapter(
            requireActivity(),
            ArrayList()
        )

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.listData?.layoutManager = linearLayoutManager
        binding?.listData?.adapter = mAdapter

        initViews()
        listeners()

        binding?.viewModel?.getAllDepartment(facility_UUID, favAllDepartmentCallBack)
        binding?.viewModel?.getDietCategory(facility_UUID, favDietCategoryCallBack)
        binding?.viewModel?.getDietFrequency(facility_UUID, favDietFrequencyCallBack)
        viewModel?.getDietName(facility_UUID.toString(), favAddDietNameCallBack)

        return binding!!.root
    }

    private fun initViews() {
        utils = Utils(requireContext())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
//        binding?.userNameTextVIew?.setText(userDataStoreBean?.user_name)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID) ?: 0
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID) ?: 0

        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..
            status = false

            rasponsecontentLabGetTemplateDetails =
                args.getParcelable(AppConstants.RESPONSECONTENT)!!

            val a: Int = args.getInt("TAG")

            if (a != 1) {

                val teamDetils: TempDetails =
                    rasponsecontentLabGetTemplateDetails!!.templates_list.get(0).temp_details!!
                val dietdetils: List<TemplateDietDetail> =
                    rasponsecontentLabGetTemplateDetails!!.templates_list.get(0).diet_details

                Itemname = teamDetils.template_name
                Itemdescription = teamDetils.template_desc

                binding?.tvSave?.text = "Update"

//            binding?.userNameTextVIew?.setText(userDataStoreBean?.user_name)
                /*  binding?.autoCompleteTextViewTestName?.setText(favouriteData?.diet_master_name)
              binding?.autoCompleteTextViewTestName!!.isFocusable = false*/
                //  binding?.qtyTextview?.setText(favouriteData?.diet_quantity.toString())
                binding?.edDiscription?.setText(Itemdescription)
                binding?.displayorder?.setText(teamDetils.display_order.toString())
                arraylistresponse.clear()

                for (i in dietdetils.indices) {
                    val responseContantSave = ResponseContentsfav()
                    responseContantSave.template_details_uuid =
                        dietdetils.get(i).template_details_uuid
                    responseContantSave.template_id = teamDetils.template_id
                    responseContantSave.diet_master_name = dietdetils.get(i).diet_name
                    responseContantSave.diet_master_id = dietdetils.get(i).diet_id
                    responseContantSave.diet_master_code = dietdetils.get(i).diet_code
                    responseContantSave.diet_quantity = dietdetils.get(i).quantity
                    responseContantSave.diet_category_id = dietdetils.get(i).diet_category_id
                    responseContantSave.diet_category_name = dietdetils.get(i).diet_category_name
                    responseContantSave.diet_category_code = dietdetils.get(i).diet_category_code
                    responseContantSave.diet_frequency_id = dietdetils.get(i).diet_frequency_id
                    responseContantSave.diet_frequency_name = dietdetils.get(i).diet_frequency_name
                    responseContantSave.diet_frequency_code = dietdetils.get(i).diet_frequency_code
                    arraylistresponse.add(responseContantSave)
                }
                mAdapter?.setFavAddItem(arraylistresponse)
            } else {
                status = true
                val dietdetils: List<TemplateDietDetail> =
                    rasponsecontentLabGetTemplateDetails!!.templates_list.get(0).diet_details

//                Itemname = teamDetils.template_name
//                Itemdescription = teamDetils.template_desc

//                binding?.tvSave?.text = "Update"

//            binding?.userNameTextVIew?.setText(userDataStoreBean?.user_name)
                /*  binding?.autoCompleteTextViewTestName?.setText(favouriteData?.diet_master_name)
              binding?.autoCompleteTextViewTestName!!.isFocusable = false*/
                //  binding?.qtyTextview?.setText(favouriteData?.diet_quantity.toString())
                binding?.edDiscription?.setText(Itemdescription)
//                binding?.displayorder?.setText(teamDetils.display_order.toString())
                arraylistresponse.clear()

                for (i in (dietdetils.indices - 1)) {
                    val responseContantSave = ResponseContentsfav()
                    responseContantSave.template_details_uuid =
                        dietdetils.get(i).template_details_uuid
//                    responseContantSave.template_id = teamDetils.template_id
                    responseContantSave.diet_master_name = dietdetils.get(i).diet_name
                    responseContantSave.diet_master_id = dietdetils.get(i).diet_id
                    responseContantSave.diet_master_code = dietdetils.get(i).diet_code
                    responseContantSave.diet_quantity = dietdetils.get(i).quantity
                    responseContantSave.diet_category_id = dietdetils.get(i).diet_category_id
                    responseContantSave.diet_category_name = dietdetils.get(i).diet_category_name
                    responseContantSave.diet_category_code = dietdetils.get(i).diet_category_code
                    responseContantSave.diet_frequency_id = dietdetils.get(i).diet_frequency_id
                    responseContantSave.diet_frequency_name = dietdetils.get(i).diet_frequency_name
                    responseContantSave.diet_frequency_code = dietdetils.get(i).diet_frequency_code
                    arraylistresponse.add(responseContantSave)
                }
                mAdapter?.setFavAddItem(arraylistresponse)
            }
        }
    }

    private fun listeners() {
        mAdapter?.setOnDeleteClickListener(object :
            DietManageTemplateAdapter.OnDeleteClickListener {

            override fun onDeleteClick(responseData: ResponseContentsfav?, position: Int) {
                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog!!.dismiss()
                }

                testName = responseData?.diet_master_name ?: ""
                val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
//                drugNmae.text = responseData?.test_master_name
                drugNmae.text =
                    "${drugNmae.text} '" + responseData?.diet_master_name + "' Record ?"
                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    val removedDetail = RemovedDetail()
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

        binding?.switchCheck!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be
            // true if the switch is in the On position
            is_active = isChecked
        })

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        /*   binding?.spinnerFavdietdepartment?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
               false
           })*/

        binding?.autoCompleteTextViewTestName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    viewModel?.getDietName(s.toString(), favAddDietNameCallBack)
                }
            }
        })

        binding?.autoCompleteTextViewTestName!!.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteTextViewTestName?.setText(autocompleteTestResponse?.get(position)?.name)
            dietSearchuuid = autocompleteTestResponse!!.get(position).uuid
            Str_auto_code = autocompleteTestResponse?.get(position)?.code ?: ""
            Str_auto_name = autocompleteTestResponse?.get(position)?.name ?: ""
            Str_auto_id = autocompleteTestResponse?.get(position)?.uuid ?: 0
        }

        binding?.add?.setOnClickListener {
            validationData()
        }

        binding?.save?.setOnClickListener {
            if (status) {   /*
                Add Details
                 */

                val displayordervalue = binding?.displayorder?.text.toString()
                arrayItemData = mAdapter?.getItems()
                detailsList.clear()
                if (arrayItemData?.size!! > 0) {
                    for (i in arrayItemData?.indices!!) {
                        val details = Detail()
                        details.chief_complaint_uuid = 0
                        details.vital_master_uuid = 0
                        details.diet_master_uuid = arrayItemData?.get(i)?.diet_master_id!!
                        details.item_master_uuid = 0
                        details.drug_route_uuid = 0
                        details.drug_frequency_uuid = 0
                        details.duration = 0
                        details.duration_period_uuid = 0
                        details.drug_instruction_uuid = 0
                        details.revision = true
                        details.is_active = true
                        details.diet_category_uuid = arrayItemData?.get(i)!!.diet_category_id
                        details.diet_frequency_uuid = arrayItemData?.get(i)!!.diet_frequency_id
                        detailsList.add(details)
                    }

                    header.name = binding?.userNameTextVIew?.text?.toString()
                    header.description = binding?.descriptionTextVIew?.text?.toString()
                    header.template_type_uuid = AppConstants.FAV_TYPE_ID_LAB
                    header.diagnosis_uuid = 0
                    header.is_public = is_active.toString()
                    header.facility_uuid = facility_UUID.toString()
                    header.department_uuid = deparment_UUID.toString()
                    header.display_order = binding?.displayorder?.text?.toString()
                    header.revision = true
                    header.is_active = true

                    RequestTemplateAddDetails.headers = header
                    RequestTemplateAddDetails.details = this.detailsList

                    if (header.name.isNullOrBlank()) {
                        Toast.makeText(
                            requireContext(),
                            "Please enter a valid template name",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    val request = Gson().toJson(RequestTemplateAddDetails)

                    viewModel?.dietTemplateDetails(
                        facility_UUID,
                        RequestTemplateAddDetails,
                        emrlabtemplatepostRetrofitCallback
                    )
                }
            } else {
                /*
                Edit Details
                 */
                val displayordervalue = binding?.displayorder?.text.toString()
                arrayItemData = mAdapter?.getItems()
                if (arrayItemData?.size!! > 0) {
                    header.template_id = arrayItemData?.get(0)?.template_id
                    header.name = Itemname
                    header.description = Itemdescription
                    header.template_type_uuid = AppConstants.FAV_TYPE_ID_LAB
                    header.diagnosis_uuid = 0
                    header.is_public = is_active.toString()
                    header.facility_uuid = facility_UUID.toString()
                    header.department_uuid = deparment_UUID.toString()
                    header.display_order = displayordervalue
                    header.revision = true
                    header.is_active = true
                    UpdateRequestModule.headers = header
                    UpdateRequestModule.new_details = this.newDetailList
                    UpdateRequestModule.removed_details = this.removeList
                    val requestupdate = Gson().toJson(UpdateRequestModule)

                    viewModel?.dietUpdateTemplateDetails(
                        facility_UUID,
                        UpdateRequestModule,
                        UpdateemrlabtemplatepostRetrofitCallback
                    )
                }
            }
        }

        binding?.clear?.setOnClickListener {
            binding?.userNameTextVIew?.setText("")
            binding?.descriptionTextVIew?.setText("")
            binding?.displayorder?.setText("")
            binding?.spinnerFavdietdepartment?.setSelection(0)
            binding?.spinnerFavdietcategory?.setSelection(0)
            binding?.spinnerFavdietfreaquency?.setSelection(0)
            binding?.autoCompleteTextViewTestName?.setText("")
            binding?.qtyTextview?.setText("")
            binding?.autoCompleteTextViewTestName?.clearFocus()
            binding?.edDiscription?.clearFocus()
            binding?.displayorder?.clearFocus()
            binding?.userNameTextVIew?.clearFocus()
            binding?.switchCheck!!.isChecked = true
            binding?.mySelf?.isChecked = true
        }

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.cancel?.setOnClickListener {
            dialog?.dismiss()
        }

    }


    val favAllDepartmentCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            var data = FavAddAllDepatResponseContent()
            data.uuid = 0
            data.name = ""
            listDepartmentItems.add(data)
            listDepartmentItems.addAll(responseBody?.body()?.responseContents!!)
            favAddResponseMapDepart =
                listDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()

            listDepartmentID = ArrayList<Int?>(favAddResponseMapDepart.keys) // <== Set to List


            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    favAddResponseMapDepart.values.toMutableList()
                )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.spinnerFavdietdepartment!!.adapter = adapter

            setDepartment()
        }


        override fun onBadRequest(errorBody: Response<FavAddAllDepatResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddAllDepatResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()?.string() ?: "",
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

    private fun setDepartment() {
        if (status) {
//            binding?.spinnerFavdietdepartment!!.setSelection(0)
            for (i in listDepartmentID.indices)
                if (listDepartmentID.get(i)?.equals(deparment_UUID) == true) {
                    binding?.spinnerFavdietdepartment!!.setSelection(i)
                }
        } else {
            for (i in listDepartmentID.indices)
                if (listDepartmentID.get(i)?.equals(favouriteData?.department_id) == true) {
                    binding?.spinnerFavdietdepartment!!.setSelection(i)
                }
        }
    }

    private fun validationData() {
        if (isNullOrEmpty(binding?.userNameTextVIew?.text.toString())) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "Please enter username"
            )
        } else if (isNullOrEmpty(binding?.edDiscription?.text.toString())) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "Please enter description"
            )
        } else if (isNullOrEmpty(binding?.displayorder?.text.toString())) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "Please Enter Display order"
            )
        } else if (isNullOrEmpty(binding?.autoCompleteTextViewTestName?.text.toString())) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "Please Enter  Name/Code"
            )
        } else if (isNullOrEmpty(binding?.qtyTextview?.text.toString())) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "Please Enter Qty"
            )
        } else if (isNullOrEmpty(binding?.spinnerFavdietfreaquency?.selectedItem.toString())) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "Please select  Frequency"
            )
        } else if (isNullOrEmpty(binding?.spinnerFavdietcategory?.selectedItem.toString())) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "Please select  Category"
            )
        } else {
            AddDietList()
        }
    }

    private fun AddDietList() {
        val displayorder = binding?.displayorder?.text?.toString()
        val qty = binding?.qtyTextview!!.text.toString()
        if (Str_auto_name.isNotEmpty() && displayorder?.isNotEmpty()!!) {
            arrayItemData = mAdapter?.getItems()
            val dietmasterId = Str_auto_id
            val check = arrayItemData!!.any { it!!.diet_master_id == dietmasterId }

            if (!check) {
                if (status) {
                    Itemname = binding?.userNameTextVIew?.text.toString()
                    Itemdescription = binding?.edDiscription?.text.toString()
                    val responseContentsfav = ResponseContentsfav()
                    responseContentsfav.diet_master_name = Str_auto_name
                    responseContentsfav.diet_master_id = dietmasterId
                    responseContentsfav.diet_master_code = Str_auto_code.toString()
                    responseContentsfav.diet_quantity = qty.toInt()
                    responseContentsfav.diet_category_id =
                        listCategoryID.get(binding?.spinnerFavdietcategory!!.selectedItemPosition)!!
                    responseContentsfav.diet_category_name =
                        binding?.spinnerFavdietcategory!!.selectedItem.toString()
                    responseContentsfav.diet_frequency_id =
                        listFrequencyID.get(binding?.spinnerFavdietfreaquency!!.selectedItemPosition)!!
                    responseContentsfav.diet_frequency_name =
                        binding?.spinnerFavdietfreaquency!!.selectedItem.toString()

                    binding?.autoCompleteTextViewTestName?.setText("")
                    Str_auto_name = ""
                    arraylistresponse.add(responseContentsfav)

                    mAdapter?.setFavAddItem(arraylistresponse)
                } else {
                    ///Update
                    val newDetail: NewDetail = NewDetail()
                    newDetail.template_master_uuid = arrayItemData?.get(0)?.template_id
                    newDetail.diet_master_id = dietmasterId
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
                    newDetail.is_active = is_active
                    newDetailList.add(newDetail)

                    Itemname = binding?.userNameTextVIew?.text.toString()
                    Itemdescription = binding?.edDiscription?.text.toString()
                    val responseContentsfav = ResponseContentsfav()
                    responseContentsfav.diet_master_name = Str_auto_name
                    responseContentsfav.diet_master_id = dietmasterId
                    responseContentsfav.diet_master_code = Str_auto_code.toString()
                    responseContentsfav.diet_quantity = qty.toInt()
                    responseContentsfav.diet_category_id =
                        listCategoryID.get(binding?.spinnerFavdietcategory!!.selectedItemPosition)!!
                    responseContentsfav.diet_category_name =
                        binding?.spinnerFavdietcategory!!.selectedItem.toString()
                    responseContentsfav.diet_frequency_id =
                        listFrequencyID.get(binding?.spinnerFavdietfreaquency!!.selectedItemPosition)!!
                    responseContentsfav.diet_frequency_name =
                        binding?.spinnerFavdietfreaquency!!.selectedItem.toString()

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

    fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.trim().isEmpty())
            return false
        return true
    }

    val favDietCategoryCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            var data = FavAddAllDepatResponseContent()
            data.uuid = 0
            data.name = ""
            listCategoryItems.add(data)
            listCategoryItems.addAll(responseBody?.body()?.responseContents!!)
            favAddResponseMapCategoty =
                listCategoryItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()

            listCategoryID = ArrayList<Int?>(favAddResponseMapCategoty.keys) // <== Set to List
            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    favAddResponseMapCategoty.values.toMutableList()
                )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.spinnerFavdietcategory!!.adapter = adapter
            binding?.spinnerFavdietcategory!!.setSelection(0)

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

    val favDietFrequencyCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            var data = FavAddAllDepatResponseContent()
            data.uuid = 0
            data.name = ""
            listFavFrequencyItems.add(data)
            listFavFrequencyItems.addAll(responseBody?.body()?.responseContents!!)
            favAddResponseMapFrequency =
                listFavFrequencyItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()

            listFrequencyID = ArrayList<Int?>(favAddResponseMapFrequency.keys) // <== Set to List
            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    favAddResponseMapFrequency.values.toMutableList()
                )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.spinnerFavdietfreaquency!!.adapter = adapter
            binding?.spinnerFavdietfreaquency!!.setSelection(0)

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

    val favAddDietNameCallBack = object : RetrofitCallback<InvestigationSearchResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<InvestigationSearchResponseModel>?) {
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

    val emrlabtemplatepostRetrofitCallback =
        object : RetrofitCallback<ReponseTemplateadd> {
            override fun onSuccessfulResponse(responseBody: Response<ReponseTemplateadd>?) {
                Log.i("", "onSuccessfulResponse: $responseBody")

                Toast.makeText(requireContext(), "Template Added successfully", Toast.LENGTH_SHORT)
                    .show()


                /*        utils?.showToast(
                            R.color.negativeToast,
                            binding?.mainLayout!!,
                            responseBody?.body()?.message!!
                        )*/
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