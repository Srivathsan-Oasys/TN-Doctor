package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
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
import com.oasys.digihealth.doctor.databinding.DialogManageLabFavouriteBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavAddListResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.request.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.request.Headers
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.request.RequestLabFavModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.view_model.ManageLabFavourteViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.view_model.ManageLabFavourteViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.filterValues
import kotlin.collections.get
import kotlin.collections.indices
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.toList
import kotlin.collections.toMap
import kotlin.collections.toMutableList
import kotlin.collections.toMutableMap


class ManageLabFavouriteFragment : DialogFragment() {
    private var deletefavouriteID: Int? = 0
    private var customdialog: Dialog? = null
    lateinit var drugNmae: TextView
    private var ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
    private var favouriteData: LabFavModel? = null
    private var is_active: Boolean = true

    private var Str_auto_id: Int? = 0
    private var content: String? = null
    private var viewModel: ManageLabFavourteViewModel? = null
    var binding: DialogManageLabFavouriteBinding? = null
    var callbackfavourite: OnFavRefreshListener? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var selectedDepartmentId: Int? = 0
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var favAddResponseMap = mutableMapOf<Int, String>()
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var status: Boolean? = false

    val header: Headers? = Headers()
    val detailsList: ArrayList<Detail> = ArrayList()
    var labManageFavAdapter: LabManageFavAdapter? = null
    val emrFavRequestModel: RequestLabFavModel? = RequestLabFavModel()
    private val departmentPositionId: HashMap<Int, Int> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        content = arguments?.getString(AppConstants.ALERTDIALOG)
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
                R.layout.dialog_manage_lab_favourite,
                container,
                false
            )
        viewModel = ManageLabFavourteViewModelFactory(
            requireActivity().application
        )
            .create(ManageLabFavourteViewModel::class.java)

        hideSoftKeyboard(view)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        utils = Utils(this.requireContext())

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)


        binding?.viewModel?.getDepartmentList(facility_UUID, FavLabdepartmentRetrofitCallBack)
        viewModel?.getTestName(facility_UUID.toString(), favAddTestNameCallBack)
        binding?.userNameTextView?.text =
            (userDataStoreBean?.title?.name + ". " + userDataStoreBean?.first_name)

        labManageFavAdapter =
            LabManageFavAdapter(
                requireActivity(),
                ArrayList()
            )

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.listdatarecycleview?.layoutManager = linearLayoutManager
        binding?.listdatarecycleview?.adapter = labManageFavAdapter
        //   binding?.listDataRecycleview?.adapter = labManageFavAdapter

        binding?.cancel?.setOnClickListener {

            dialog!!.dismiss()
        }

        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..
            favouriteData = args.getParcelable(AppConstants.RESPONSECONTENT)

            ResponseContantSave?.favourite_id = favouriteData?.favourite_id
            ResponseContantSave?.favourite_display_order = favouriteData!!.favourite_display_order
            ResponseContantSave?.test_master_name = favouriteData!!.test_master_name

            Str_auto_id = favouriteData!!.test_master_id
            binding?.buttonstatus?.text = "Update"

            binding?.editTextDisplayOrder?.setText(favouriteData!!.favourite_display_order?.toString())
//            binding?.displayOrderTextView?.text = favouriteData!!.favourite_display_order.toString()
            binding?.autoCompleteTextViewTestName!!.setText(favouriteData?.test_master_name)
            binding?.autoCompleteTextViewTestName!!.isFocusable = false
            labManageFavAdapter?.setFavAddItem(ResponseContantSave)
        }


        val disOrder = "Display Order"
        val next = "<font color='#EE0000'>*</font>"

        binding?.tvTestName?.text = Html.fromHtml("Test Name$next")
        binding?.tvDepartment?.text = Html.fromHtml("Department$next")
        binding?.tvOrder?.text = Html.fromHtml(disOrder + next)

        labManageFavAdapter?.setOnDeleteClickListener(object :
            LabManageFavAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesID: Int?, testMasterName: String?) {
                viewModel!!.deleteFavourite(
                    facility_UUID,
                    favouritesID, deleteRetrofitResponseCallback
                )
                Toast.makeText(
                    requireContext(),
                    "Test Name Deleted successfully",
                    Toast.LENGTH_LONG
                ).show()
                /*  deletefavouriteID = favouritesID
                  customdialog = Dialog(requireContext())
                  customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                  customdialog!!.setCancelable(false)
                  customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                  val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView

                  closeImageView.setOnClickListener {
                      customdialog?.dismiss()
                  }
                  drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView

                  drugNmae.text = "${drugNmae.text} '" + testMasterName + "' Record ?"
                  val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                  val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                  yesBtn.setOnClickListener {
                      viewModel!!.deleteFavourite(
                          facility_UUID,
                          favouritesID, deleteRetrofitResponseCallback
                      )
                      customdialog!!.dismiss()

                      Toast.makeText(requireContext(), "Test Name Deleted successfully", Toast.LENGTH_LONG).show()

                  }
                  noBtn.setOnClickListener {
                      customdialog!!.dismiss()
                  }
                  customdialog!!.show()*/

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
        binding?.spinnerFavLabdepartment?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
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
        binding?.autoCompleteTextViewTestName!!.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteTextViewTestName?.setText(autocompleteTestResponse?.get(position)?.name)
            Str_auto_id = autocompleteTestResponse?.get(position)?.uuid


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

        binding?.addFav?.setOnClickListener {
            val Str_DisplayOrder = binding?.editTextDisplayOrder?.text.toString()

            if (Str_DisplayOrder.isEmpty()) {
                Toast.makeText(requireContext(), "Please Enter Display Order", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (Str_auto_id == 0) {
                Toast.makeText(requireContext(), "Please Enter Test name", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (status as Boolean) {
                detailsList.clear()

                header?.is_public = false
                header?.facility_uuid = facility_UUID?.toString()!!
                header?.favourite_type_uuid = AppConstants.FAV_TYPE_ID_LAB
                header?.department_uuid = selectedDepartmentId ?: 0
                header?.user_uuid = userDataStoreBean?.uuid?.toString()!!
                header?.display_order = Str_DisplayOrder
                header?.revision = true
                header?.is_active = is_active

                val details: Detail = Detail()
                details.test_master_uuid = Str_auto_id!!.toInt()
                details.test_master_type_uuid = AppConstants.LAB_TESTMASTER_UUID
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



                viewModel?.getADDFavourite(
                    facility_UUID,
                    emrFavRequestModel!!,
                    emrposFavtRetrofitCallback
                )
            } else {
                //False
                viewModel?.getEditFavourite(
                    facility_UUID,
                    favouriteData?.test_master_name,
                    favouriteData?.favourite_id,
                    deparment_UUID,
                    Str_DisplayOrder,
                    is_active,
                    emrposListDataFavtEditRetrofitCallback
                )


            }


        }
        binding?.clearCardview?.setOnClickListener {
            binding?.editTextDisplayOrder?.setText("")
            binding?.autoCompleteTextViewTestName?.setText("")

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

    val emrposFavtRetrofitCallback = object : RetrofitCallback<LabFavManageResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabFavManageResponseModel>?) {


            viewModel?.getAddListFav(
                facility_UUID,
                responseBody?.body()?.responseContents?.details?.get(0)?.favourite_master_uuid,
                emrposListDataFavtRetrofitCallback
            )

            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Favorite Added successfully"
            )

            Toast.makeText(requireContext(), "Favorite Added successfully", Toast.LENGTH_LONG)
                .show()
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
    val emrposListDataFavtRetrofitCallback = object : RetrofitCallback<FavAddListResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavAddListResponse>?) {


            if (is_active) {
                labManageFavAdapter?.setFavAddItem(responseBody?.body()?.responseContents)
            }

            binding!!.autoCompleteTextViewTestName.setText("")
            binding!!.editTextDisplayOrder.setText("")

            Toast.makeText(context, responseBody?.body()?.message, Toast.LENGTH_SHORT).show()

            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )
            callbackfavourite?.onRefreshList()
        }

        override fun onBadRequest(response: Response<FavAddListResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddListResponse
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

    val emrposListDataFavtEditRetrofitCallback = object : RetrofitCallback<FavEditResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavEditResponse>?) {

            ResponseContantSave?.favourite_id = responseBody?.body()?.requestContent?.favourite_id
            ResponseContantSave?.favourite_display_order =
                responseBody?.body()?.requestContent?.favourite_display_order
            ResponseContantSave?.test_master_name = responseBody?.body()?.requestContent?.testname
            labManageFavAdapter?.clearadapter()

            labManageFavAdapter?.setFavAddItem(ResponseContantSave)
            Toast.makeText(requireContext(), "Favorite Updated successfully", Toast.LENGTH_LONG)
                .show()
            callbackfavourite?.onFavID(responseBody?.body()?.requestContent?.favourite_id!!)
            dismiss()


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

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            labManageFavAdapter?.adapterrefresh(deletefavouriteID)
            callbackfavourite?.onRefreshList()
            dialog?.dismiss()

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

    val getFavouritesRetrofitCallBack =
        object : RetrofitCallback<FavouritesResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavouritesResponseModel>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {

//                    favouriteData.refreshList(response.body()?.responseContents!!)
                }
            }

            override fun onBadRequest(response: Response<FavouritesResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FavouritesResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavouritesResponseModel::class.java
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

    fun setOnFavRefreshListener(callback: OnFavRefreshListener) {
        this.callbackfavourite = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnFavRefreshListener {
        fun onFavID(position: Int)
        fun onRefreshList()
    }


}