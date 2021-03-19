package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui


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
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogManageRadiologyFavouritesBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.RadiologyManageFavAdapter
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model.ManageRadiologyFavourteViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model.ManageRadiologyFavourteViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.io.IOException

class ManageRadiolgyFavouriteFragment : DialogFragment() {
    private var Str_auto_id: Int? = 0
    private var content: String? = null
    private var deletefavouriteID: Int? = 0
    lateinit var drugNmae: TextView
    private var viewModel: ManageRadiologyFavourteViewModel? = null
    var binding: DialogManageRadiologyFavouritesBinding? = null
    var appPreferences: AppPreferences? = null
    private var ResponseContantSave: ManageRadiologyFavAddResponseContents? =
        ManageRadiologyFavAddResponseContents()
    private var utils: Utils? = null

    private var favouriteData: RadiologyFavData? = null
    private var facility_UUID: Int? = 0
    private var customdialog: Dialog? = null
    private var deparment_UUID: Int? = 0
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var favAddResponseMap = mutableMapOf<Int, String>()
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    private var mAdapter: RadiologyManageFavAdapter? = null
    private val favList: MutableList<RecyclerDto> = java.util.ArrayList()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var status: Boolean? = false
    val header: com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.Headers? =
        com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.Headers()
    val detailsList: ArrayList<com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.Detail> =
        ArrayList()
    private var is_active: Boolean = true
    var callbackRadiology: OnFavRadiologyRefreshListener? = null
    val emrFavRequestModel: RequestRadiologyFavModel? = RequestRadiologyFavModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
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
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_manage_radiology_favourites,
                container,
                false
            )
        viewModel = ManageRadiologyFavourteViewModelFactory(
            requireActivity().application
        )
            .create(ManageRadiologyFavourteViewModel::class.java)

        hideSoftKeyboard(view)
        utils = Utils(this.requireContext())


        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        binding?.userName?.setText(userDataStoreBean?.user_name)


        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        binding?.username?.setText(userDataStoreBean?.title?.name + ". " + userDataStoreBean?.first_name)
        binding?.viewModel?.getDepartmentList(facility_UUID, FavLabdepartmentRetrofitCallBack)

        mAdapter =
            RadiologyManageFavAdapter(
                requireActivity(),
                ArrayList()
            )

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.favManageRecyclerview?.layoutManager = linearLayoutManager
        binding?.favManageRecyclerview?.adapter = mAdapter

        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..
            favouriteData = args.getParcelable(AppConstants.RESPONSECONTENT)
            Log.i("", "" + favouriteData)
            ResponseContantSave?.favourite_id = favouriteData?.favourite_id!!
            ResponseContantSave?.favourite_display_order = favouriteData!!.favourite_display_order!!
            ResponseContantSave?.test_master_name = favouriteData!!.test_master_name.toString()
            binding?.addbutton?.text = "Update"

            binding?.editdisplayorder?.setText(favouriteData!!.favourite_display_order?.toString())
//            binding?.displayOrderTextView?.text = favouriteData!!.favourite_display_order.toString()
            binding?.autoCompleteTextViewTestName!!.setText(favouriteData?.test_master_name)
            binding?.autoCompleteTextViewTestName!!.isFocusable = false
            mAdapter?.setFavAddItem(ResponseContantSave)
        }
        binding?.switchCheck!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be
            // true if the switch is in the On position
            is_active = isChecked
        })
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.cancel?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.clear?.setOnClickListener({
            binding?.editdisplayorder?.setText("")
            binding?.autoCompleteTextViewTestName?.setText("")
            binding?.editdisplayorder?.clearFocus()
            binding?.autoCompleteTextViewTestName?.clearFocus()
            binding?.userName?.clearFocus()
        })
        binding?.spinnerFavRadiodepartment?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            binding?.viewModel?.getAllDepartment(facility_UUID, favLabAddAllDepartmentCallBack)
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
        binding?.cancelButton?.setOnClickListener({
            binding?.autoCompleteTextViewTestName!!.setText("")
            Str_auto_id = 0
        })


        mAdapter?.setOnDeleteClickListener(object :
            RadiologyManageFavAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesID: Int?, testMasterName: String?) {
                deletefavouriteID = favouritesID
                viewModel!!.deleteFavourite(
                    facility_UUID,
                    favouritesID, deleteRetrofitResponseCallback

                )
                /* if (customdialog == null) {
                     customdialog = Dialog(requireContext())
                     customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                     customdialog!!.setCancelable(false)
                     customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                     val closeImageView =
                         customdialog!!.findViewById(R.id.closeImageView) as ImageView

                     closeImageView.setOnClickListener {
                         customdialog?.dismiss()
                         customdialog = null

                     }
                     drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                     drugNmae.text = "${drugNmae.text.toString()} '" + testMasterName + "' Record ?"
                     val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                     val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                     yesBtn.setOnClickListener {
                         viewModel!!.deleteFavourite(
                             facility_UUID,
                             favouritesID, deleteRetrofitResponseCallback

                         )

                         customdialog!!.dismiss()


                     }
                     noBtn.setOnClickListener {
                         customdialog!!.dismiss()
                         customdialog = null

                     }
                     if (customdialog != null) {

                         customdialog!!.show()

                     }
                 }*/
            }
        })
        binding?.autoCompleteTextViewTestName!!.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteTextViewTestName?.setText(autocompleteTestResponse?.get(position)?.name)
            Str_auto_id = autocompleteTestResponse?.get(position)?.uuid
        }
        binding?.addbutton?.setOnClickListener {
            val Str_DisplayOrder = binding?.editdisplayorder?.text.toString()
            val nameText = binding?.autoCompleteTextViewTestName?.text.toString()
            if (Str_DisplayOrder.isEmpty()) {
                Toast.makeText(requireContext(), "Please Enter Display Order", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            Log.e("status", "___" + status)
            if (status as Boolean) {
                if (Str_auto_id == 0) {
                    Toast.makeText(requireContext(), "Please Enter Test name", Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                } else {
                    detailsList.clear()
                    header?.is_public = false
                    header?.facility_uuid = facility_UUID?.toString()!!
                    header?.favourite_type_uuid = AppConstants.FAV_TYPE_ID_RADIOLOGY
                    header?.department_uuid = deparment_UUID?.toString()!!
                    header?.user_uuid = userDataStoreBean?.uuid?.toString()!!
                    header?.display_order = Str_DisplayOrder
                    header?.revision = true
                    header?.is_active = is_active
                    header?.name = nameText

                    val details: com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.Detail =
                        com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.Detail()
                    details.test_master_uuid = Str_auto_id!!.toInt()
                    details.test_master_type_uuid = AppConstants.LAB_MASTER_TYPE_ID_RADIOLOGY
                    details.item_master_uuid = 0
                    details.chief_complaint_uuid = 0
                    details.vital_master_uuid = 0
                    details.drug_route_uuid = 0
                    details.drug_frequency_uuid = 0
                    details.duration = 0
                    details.duration_period_uuid = 0
                    details.drug_instruction_uuid = 0
                    details.display_order = Str_DisplayOrder
                    details.revision = true
                    details.is_active = is_active
                    details.diagnosis_uuid = 0
                    detailsList.add(details)

                    emrFavRequestModel?.headers = this.header!!
                    emrFavRequestModel?.details = this.detailsList

                    val jsonrequest = Gson().toJson(emrFavRequestModel)

                    Log.i("", "" + jsonrequest)
                    viewModel?.getADDFavourite(
                        facility_UUID,
                        emrFavRequestModel!!,
                        emrposFavtRetrofitCallback
                    )
                }
            } else {
                //False
                viewModel?.getEditFavourite(
                    facility_UUID,
                    favouriteData!!.test_master_name,
                    favouriteData?.favourite_id,
                    deparment_UUID,
                    Str_DisplayOrder,
                    is_active,
                    emrposListDataFavtEditRetrofitCallback
                )

            }
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


    val emrposFavtRetrofitCallback = object : RetrofitCallback<LabFavManageResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabFavManageResponseModel>?) {
            Log.i(
                "",
                "" + responseBody?.body()?.responseContents?.details?.get(0)?.favourite_master_uuid
            )
            viewModel?.getAddListFav(
                facility_UUID,
                responseBody?.body()?.responseContents?.details?.get(0)?.favourite_master_uuid,
                emrposListDataFavtRetrofitCallback
            )
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                getString(R.string.fav_add_success)
            )
        }

        override fun onBadRequest(response: Response<LabFavManageResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: LabFavManageResponseModel
            if (response!!.code() == 400) {
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
            try {

                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    response.body()?.message!!
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
    val emrposListDataFavtRetrofitCallback =
        object : RetrofitCallback<ManageRadiologyFavAddResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<ManageRadiologyFavAddResponseModel>?) {
                Log.i("", "" + responseBody?.body()?.responseContents)
                if (responseBody?.body()?.responseContents != null) {
                    utils?.showToast(
                        R.color.positiveToast,
                        binding?.mainLayout!!,
                        responseBody.body()?.message ?: ""

                    )

/*
                    alertDialog(responseBody?.body()?.message ?: "")
*/
                    mAdapter?.setFavAddItem(responseBody.body()?.responseContents)
                    binding!!.autoCompleteTextViewTestName.setText("")
                    binding!!.editdisplayorder!!.setText("")

                    utils?.showToast(
                        R.color.positiveToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    callbackRadiology?.onFavRadiologyID(responseBody.body()?.responseContents?.favourite_id!!)
                }
            }

            override fun onBadRequest(response: Response<ManageRadiologyFavAddResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: ManageRadiologyFavAddResponseModel
                try {

                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        response?.body()?.message!!
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
                Log.i("", "" + response.body()?.responseContent)
                Log.i("", "" + response.body()?.responseContent)
                listDepartmentItems.add(response.body()?.responseContent)
                favAddResponseMap =
                    listDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()

                val adapter =
                    ArrayAdapter<String>(
                        requireActivity(),
                        R.layout.spinner_item,
                        favAddResponseMap.values.toMutableList()
                    )
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.spinnerFavRadiodepartment!!.adapter = adapter

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
            Log.i("", "" + responseBody?.body()?.responseContents)
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
            binding?.spinnerFavRadiodepartment!!.adapter = adapter

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
    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            if (!responseBody?.body()?.message.isNullOrEmpty())
                Toast.makeText(
                    requireContext(),
                    responseBody?.body()?.message ?: "",
                    Toast.LENGTH_SHORT
                ).show()

/*
            alertDialog(responseBody?.body()?.message ?: "")
*/
            mAdapter?.adapterrefresh(deletefavouriteID)
            callbackRadiology?.onRefreshList()

        }

        override fun onBadRequest(errorBody: Response<DeleteResponseModel>?) {

        }

        override fun onServerError(response: Response<*>?) {
        }

        override fun onUnAuthorized() {
        }

        override fun onForbidden() {
        }

        override fun onFailure(s: String?) {
        }

        override fun onEverytime() {
        }

    }

    val emrposListDataFavtEditRetrofitCallback = object : RetrofitCallback<FavEditResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavEditResponse>?) {
            Log.i("", "" + responseBody?.body()?.requestContent)
            if (responseBody?.body()?.requestContent != null) {
                Toast.makeText(
                    requireContext(),
                    responseBody.body()?.message ?: "",
                    Toast.LENGTH_SHORT
                ).show()
                ResponseContantSave?.favourite_id =
                    responseBody.body()?.requestContent?.favourite_id!!
                ResponseContantSave?.favourite_display_order =
                    responseBody.body()?.requestContent?.favourite_display_order!!
                ResponseContantSave?.test_master_name =
                    responseBody.body()?.requestContent?.testname.toString()
                mAdapter?.clearadapter()

                mAdapter?.setFavAddItem(ResponseContantSave)
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseBody.body()?.message!!
                )

                callbackRadiology?.onFavRadiologyID(responseBody.body()?.requestContent?.favourite_id!!)
            }
        }

        override fun onBadRequest(response: Response<FavEditResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavEditResponse
            try {

                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    response?.body()?.message!!
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

//    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
//            labManageFavAdapter?.adapterrefresh(deletefavouriteID)
//            callbackfavourite?.onRefreshList()
//
//        }
//
//        override fun onBadRequest(errorBody: Response<DeleteResponseModel>?) {
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//
//        }
//
//        override fun onUnAuthorized() {
//
//        }
//
//        override fun onForbidden() {
//
//        }
//
//        override fun onFailure(s: String?) {
//
//        }
//
//        override fun onEverytime() {
//
//        }
//
//    }

    fun setOnFavRefreshListener(callback: OnFavRadiologyRefreshListener) {
        this.callbackRadiology = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnFavRadiologyRefreshListener {
        fun onFavRadiologyID(position: Int)
        fun onRefreshList()
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