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
import android.widget.ArrayAdapter
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
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogManageDietFavouritesBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.DietFavMangeResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.request.RequestDietFavModel
import com.hmis_tn.doctor.ui.emr_workflow.diet.view_model.ManageDietFavouriteViewModel
import com.hmis_tn.doctor.ui.emr_workflow.diet.view_model.ManageDietFavouriteViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.ui.FavInvestigationTestNameSearchResultAdapter
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.FavAddListResponse
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.request.Detail
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.request.Headers
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.utils.Utils
import org.json.JSONObject
import retrofit2.Response

class ManageDietFavouriteFragment : DialogFragment() {

    private var content: String? = null
    private var testName: String? = ""
    private var viewModel: ManageDietFavouriteViewModel? = null
    var binding: DialogManageDietFavouritesBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var listDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var listCategoryItems: ArrayList<FavAddAllDepatResponseContent?> = ArrayList()
    private var listFavFrequencyItems: ArrayList<FavAddAllDepatResponseContent?> = ArrayList()
    private var favAddResponseMapDepart = mutableMapOf<Int, String>()
    private var favAddResponseMapCategoty = mutableMapOf<Int, String>()
    private var favAddResponseMapFrequency = mutableMapOf<Int, String>()
    private var autocompleteTestResponse: List<InvestigationSearchResponseContent>? = null


    var status: Boolean? = false
    val header: Headers = Headers()
    val detailsList: ArrayList<Detail> = ArrayList()
    private var is_active: Boolean = true
    val emrFavRequestModel: RequestDietFavModel = RequestDietFavModel()
    var dietManageFavAdapter: DietManageFavAdapter? = null
    var dietSearchuuid: Int = 0
    private var deletefavouriteID: Int? = 0
    private var customdialog: Dialog? = null

    var callbackfavourite: OnFavRefreshListener? = null

    lateinit var dietNmae: TextView

    private lateinit var listDepartmentID: List<Int?>
    private lateinit var listCategoryID: List<Int?>
    private lateinit var listFrequencyID: List<Int?>

    private var favouriteData: FavouritesModel? = null
    private var ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()

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
            dialog.window?.setLayout(
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
                R.layout.dialog_manage_diet_favourites,
                container,
                false
            )
        utils = Utils(requireContext())
        viewModel = ManageDietFavouriteViewModelFactory(
            requireActivity().application
        )
            .create(ManageDietFavouriteViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        binding?.userNameTextVIew?.setText(userDataStoreBean?.title?.name + ". " + userDataStoreBean?.first_name)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        initViews()
        listeners()

        binding?.viewModel?.getAllDepartment(facility_UUID, favAllDepartmentCallBack)
        binding?.viewModel?.getDietCategory(facility_UUID, favDietCategoryCallBack)
        binding?.viewModel?.getDietFrequency(facility_UUID, favDietFrequencyCallBack)

        viewModel?.getDietName(facility_UUID.toString(), favAddDietNameCallBack)

        return binding!!.root
    }

    private fun initViews() {
        val args = arguments
        if (args == null) {
            status = true
        } else {
            // get value from bundle..
            status = false
            favouriteData = args.getParcelable(AppConstants.RESPONSECONTENT)
            Log.i("", "" + favouriteData)
            ResponseContantSave?.favourite_id = favouriteData?.favourite_id
            ResponseContantSave?.favourite_display_order = favouriteData?.favourite_display_order
            ResponseContantSave?.diet_master_name = favouriteData?.diet_master_name ?: ""
            binding?.buttonstatus?.text = "Update"
            binding?.displayorder?.setText(favouriteData?.favourite_display_order?.toString() ?: "")
            binding?.autoCompleteTextViewTestName?.setText(favouriteData?.diet_master_name)
            binding?.autoCompleteTextViewTestName?.isFocusable = false
            binding?.qtyTextview?.setText(favouriteData?.diet_quantity.toString())
            dietManageFavAdapter?.setFavAddItem(ResponseContantSave)
        }


        //Recycleview adapter init

        dietManageFavAdapter = DietManageFavAdapter(
            requireActivity(),
            ArrayList()
        )

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.listData?.layoutManager = linearLayoutManager
        binding?.listData?.adapter = dietManageFavAdapter
    }

    private fun listeners() {
        dietManageFavAdapter?.setOnDeleteClickListener(object :
            DietManageFavAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesID: Int?, testMasterName: String?) {
                deletefavouriteID = favouritesID
                customdialog = Dialog(requireContext())
                customdialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog?.setCancelable(false)
                customdialog?.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog?.findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                }
                dietNmae = customdialog?.findViewById(R.id.addDeleteName) as TextView

                dietNmae.text = "${dietNmae.text} '" + testMasterName + "' Record ?"
                val yesBtn = customdialog?.findViewById(R.id.yes) as CardView
                val noBtn = customdialog?.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    testName = testMasterName
                    viewModel?.deleteFavourite(
                        facility_UUID,
                        favouritesID, deleteRetrofitResponseCallback
                    )
                    customdialog?.dismiss()
                }
                noBtn.setOnClickListener {
                    customdialog?.dismiss()
                }
                customdialog?.show()

            }

        })

        binding?.switchCheck?.setOnCheckedChangeListener { buttonView, isChecked ->
            is_active = isChecked
        }

        binding?.autoCompleteTextViewTestName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }


            override fun afterTextChanged(s: Editable) {
                if (s.length > 0) {
                    Log.e("autoCompleteTextView", s.toString())
                    viewModel?.getDietName(s.toString(), favAddDietNameCallBack)

                }
            }
        })

        binding?.autoCompleteTextViewTestName?.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteTextViewTestName?.setText(autocompleteTestResponse?.get(position)?.name)

            Log.e("onItemClick", "" + autocompleteTestResponse?.get(position)?.name)
            dietSearchuuid = autocompleteTestResponse?.get(position)?.uuid ?: 0

        }

        binding?.add?.setOnClickListener {
            validationData()
        }

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.clear?.setOnClickListener {
            binding?.displayorder?.setText("")
            binding?.autoCompleteTextViewTestName?.setText("")
            binding?.qtyTextview?.setText("")
            binding?.spinnerFavdietfreaquency?.setSelection(0)
            binding?.spinnerFavdietcategory?.setSelection(0)
            binding?.spinnerFavdietdepartment?.setSelection(0)
            binding?.displayorder?.clearFocus()
            binding?.autoCompleteTextViewTestName?.clearFocus()
            binding?.qtyTextview?.clearFocus()

        }

        binding?.cancel?.setOnClickListener {
            dialog?.dismiss()
        }
    }


    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            dietManageFavAdapter?.adapterrefresh(deletefavouriteID)
            callbackfavourite?.onRefreshList()
            Toast.makeText(context!!, "$testName Deleted successfully", Toast.LENGTH_LONG).show()

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


    private fun validationData() {
        when {
            isNullOrEmpty(binding?.userNameTextVIew?.text.toString()) -> {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please enter username"
                )
            }
            isNullOrEmpty(binding?.spinnerFavdietdepartment?.selectedItem.toString()) -> {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please select  Department"
                )
            }
            isNullOrEmpty(binding?.displayorder?.text.toString()) -> {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please Enter Display order"
                )
            }
            isNullOrEmpty(binding?.autoCompleteTextViewTestName?.text.toString()) -> {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please Enter  Name/Code"
                )
            }
            isNullOrEmpty(binding?.qtyTextview?.text.toString()) -> {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please Enter Qty"
                )
            }
            isNullOrEmpty(binding?.spinnerFavdietfreaquency?.selectedItem.toString()) -> {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please select  Frequency"
                )
            }
            isNullOrEmpty(binding?.spinnerFavdietcategory?.selectedItem.toString()) -> {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please select  Category"
                )
            }
            else -> {
                createAndEditDietFav()
            }
        }
    }

    private fun createAndEditDietFav() {

        //status differ add or edit
        if (status as Boolean) {
            detailsList.clear()
            header.is_public = true
            header.ticksheet_type_uuid = 1
            header.facility_uuid = facility_UUID?.toString() ?: ""
            header.favourite_type_uuid = AppConstants.FAV_TYPE_ID_DIET
            header.department_uuid =
                listDepartmentID[binding?.spinnerFavdietdepartment?.selectedItemPosition ?: 0] ?: 0
            header.user_uuid = userDetailsRoomRepository?.getUserDetails()?.uuid?.toString() ?: ""
            header.display_order = binding?.displayorder?.text.toString()
            header.revision = true
            header.is_active = is_active

            val details = Detail()
            details.test_master_uuid = 0
            details.test_master_type_uuid = 1
            details.item_master_uuid = 0
            details.chief_complaint_uuid = 0
            details.vital_master_uuid = 0
            details.drug_route_uuid = 0
            details.drug_frequency_uuid = 0
            details.duration = 0
            details.duration_period_uuid = 0
            details.drug_instruction_uuid = 0
            details.display_order = binding?.displayorder?.text.toString()
            details.quantity = binding?.qtyTextview?.text.toString()
            details.revision = true
            details.is_active = is_active
            details.diagnosis_uuid = 0
            details.diet_category_uuid =
                listCategoryID.get(binding?.spinnerFavdietcategory?.selectedItemPosition ?: 0) ?: 0
            details.diet_frequency_uuid =
                (listFrequencyID.get(binding?.spinnerFavdietfreaquency?.selectedItemPosition ?: 0))
                    ?: 0
            details.diet_master_uuid = dietSearchuuid
            detailsList.add(details)
            emrFavRequestModel.headers = this.header
            emrFavRequestModel.details = this.detailsList

            val jsonrequest = Gson().toJson(emrFavRequestModel)
            viewModel?.getADDFavourite(
                facility_UUID,
                emrFavRequestModel,
                emrposFavtRetrofitCallback
            )
        } else {
            //False
            viewModel?.getEditFavourite(
                facility_UUID,
                favouriteData?.diet_master_name,
                favouriteData?.favourite_id,
                deparment_UUID,
                binding?.displayorder?.text.toString(),
                is_active,
                listFrequencyID.get(binding?.spinnerFavdietfreaquency?.selectedItemPosition ?: 0),
                listCategoryID.get(binding?.spinnerFavdietcategory?.selectedItemPosition ?: 0),
                binding?.qtyTextview?.text.toString(),
                emrposListDataFavtEditRetrofitCallback
            )

        }


    }


    val emrposFavtRetrofitCallback = object : RetrofitCallback<DietFavMangeResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DietFavMangeResponseModel>?) {
            Log.e("FavtRetrofitCallback", "onSuccessfulResponse")


            binding?.displayorder?.setText("")
            binding?.autoCompleteTextViewTestName?.setText("")
            binding?.qtyTextview?.setText("")
            //   binding?.spinnerFavdietfreaquency?.setSelection(0)
            // binding?.spinnerFavdietcategory?.setSelection(0)
            binding?.spinnerFavdietdepartment?.setSelection(0)
            Toast.makeText(context!!, "Favorite Added successfully", Toast.LENGTH_LONG).show()

            viewModel?.getAddListFav(
                facility_UUID ?: 0,
                responseBody?.body()?.responseContents?.details?.get(0)?.favourite_master_uuid ?: 0,
                responseBody?.body()?.responseContents?.headers?.favourite_type_uuid ?: 0,
                emrposListDataFavtRetrofitCallback
            )

        }

        override fun onBadRequest(response: Response<DietFavMangeResponseModel>?) {
            Log.e("FavtRetrofitCallback", "onBadRequest")
            if (response?.code() == 400) {
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        jObjError.getString("message")
                            ?: getString(R.string.something_went_wrong)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Bad Request"
                )
            }
        }

        override fun onServerError(response: Response<*>) {
            Log.e("FavtRetrofitCallback", "onServerError")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            Log.e("FavtRetrofitCallback", "onUnAuthorized")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            Log.e("FavtRetrofitCallback", "onForbidden")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            Log.e("FavtRetrofitCallback", "onFailure")
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            Log.e("FavtRetrofitCallback", "onEverytime")
            viewModel?.progress?.value = 8
        }


    }


    val emrposListDataFavtRetrofitCallback = object : RetrofitCallback<FavAddListResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavAddListResponse>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)
            dietManageFavAdapter?.clearadapter()
            dietManageFavAdapter?.setFavAddItem(responseBody?.body()?.responseContents)
            dietManageFavAdapter?.notifyDataSetChanged()
            binding?.listLayout?.visibility = View.VISIBLE
            callbackfavourite?.onFavID(responseBody?.body()?.responseContents?.favourite_id ?: 0)
        }

        override fun onBadRequest(response: Response<FavAddListResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddListResponse

            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                response?.body()?.message ?: getString(R.string.something_went_wrong)
            )
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
            viewModel?.progress?.value = 8
        }


    }


    val emrposListDataFavtEditRetrofitCallback = object : RetrofitCallback<FavEditResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavEditResponse>?) {
            Log.i("", "" + responseBody?.body()?.requestContent)

            binding?.displayorder?.setText("")
            binding?.autoCompleteTextViewTestName?.setText("")
            binding?.qtyTextview?.setText("")
            binding?.spinnerFavdietfreaquency?.setSelection(0)
            binding?.spinnerFavdietcategory?.setSelection(0)
            binding?.spinnerFavdietdepartment?.setSelection(0)
            Toast.makeText(context!!, responseBody?.body()?.message ?: "", Toast.LENGTH_LONG).show()

            responseBody?.body()?.let { favEditResponse ->
                favEditResponse.requestContent?.let { requestContentfaveditresponse ->
                    ResponseContantSave?.favourite_id = requestContentfaveditresponse.favourite_id
                    ResponseContantSave?.favourite_display_order =
                        requestContentfaveditresponse.favourite_display_order
                    ResponseContantSave?.test_master_name = requestContentfaveditresponse.testname
                    ResponseContantSave?.diet_quantity = requestContentfaveditresponse.quantity
                    ResponseContantSave?.diet_category_name =
                        favAddResponseMapCategoty.get(requestContentfaveditresponse.diet_category_uuid)
                            .toString()
                    ResponseContantSave?.diet_frequency_name =
                        favAddResponseMapFrequency.get(requestContentfaveditresponse.diet_frequency_uuid)
                            .toString()

                    dietManageFavAdapter?.clearadapter()
                    dietManageFavAdapter?.setFavAddItem(ResponseContantSave)
                    callbackfavourite?.onFavID(requestContentfaveditresponse.favourite_id!!)
                    binding?.listLayout?.visibility = View.VISIBLE
                }
            }
        }

        override fun onBadRequest(response: Response<FavEditResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavEditResponse

            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                response?.body()?.message ?: getString(R.string.something_went_wrong)
            )
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

    fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.trim().isEmpty())
            return false
        return true
    }


    val favAllDepartmentCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)
            listDepartmentItems = responseBody?.body()?.responseContents!!
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
        if (status as Boolean) {
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

    private fun setCategory() {

        if (status as Boolean) {
            binding?.spinnerFavdietcategory!!.setSelection(0)
        } else {
            for (i in listCategoryID.indices)
                if (listCategoryID.get(i)?.equals(favouriteData?.diet_category_id) == true) {
                    binding?.spinnerFavdietcategory!!.setSelection(i)
                }
        }

    }

    private fun setFrequency() {

        if (status as Boolean) {
            binding?.spinnerFavdietfreaquency!!.setSelection(0)
        } else {

            for (i in listFrequencyID.indices)

                if (listFrequencyID.get(i)?.equals(favouriteData?.diet_frequency_id) == true) {
                    binding?.spinnerFavdietfreaquency!!.setSelection(i)
                }
        }

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
            setCategory()

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
            setFrequency()

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
            viewModel?.progress?.value = 8
        }

    }


    val favAddDietNameCallBack = object : RetrofitCallback<InvestigationSearchResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<InvestigationSearchResponseModel>?) {
            Log.e("diet name esponse", "" + responseBody?.body()?.responseContents)

            autocompleteTestResponse = responseBody?.body()?.responseContents
            val responseContentAdapter =
                responseBody?.body()?.responseContents?.let {
                    FavInvestigationTestNameSearchResultAdapter(
                        context!!,
                        R.layout.row_chief_complaint_search_result,
                        it
                    )
                }
            binding?.autoCompleteTextViewTestName?.threshold = 1
            binding?.autoCompleteTextViewTestName?.setAdapter(responseContentAdapter)

        }

        override fun onBadRequest(errorBody: Response<InvestigationSearchResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: InvestigationSearchResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()?.string() ?: "",
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