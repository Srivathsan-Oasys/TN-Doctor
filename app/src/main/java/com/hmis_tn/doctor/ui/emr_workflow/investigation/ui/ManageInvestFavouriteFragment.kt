package com.hmis_tn.doctor.ui.emr_workflow.investigation.ui


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
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.isTablet
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogManageInvestigationFavouritesBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.*
import com.hmis_tn.doctor.ui.emr_workflow.investigation.view_model.ManageInvestFavourteViewModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.view_model.ManageInvestFavourteViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.*
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.io.IOException

class ManageInvestFavouriteFragment : DialogFragment() {
    private var content: String? = null
    private var viewModel: ManageInvestFavourteViewModel? = null
    var binding: DialogManageInvestigationFavouritesBinding? = null
    private var Itemname: String? = ""
    private var drugName: String? = ""
    private var is_active: Boolean = true
    private var Str_auto_id: Int? = 0


    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var mAdapter: ManageFavInstituteAdapter? = null
    private var mAdapterMob: ManageFavInstituteMobAdapter? = null
    private var favouriteData: FavouritesModel? = null
    private var ResponseContantSave: ManageFavAddResponseContents? = ManageFavAddResponseContents()
    private var deletefavouriteID: Int? = 0
    private var customdialog: Dialog? = null
    lateinit var drugNmae: TextView

    var displayOrder: ArrayList<Int> = ArrayList()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var favAddResponseMap = mutableMapOf<Int, String>()
    private var autocompleteTestResponse: List<InvestigationSearchResponseContent>? = null
    var callbackfavourite: OnFavRefreshListener? = null

    var status: Boolean? = false

    val header: HeadersX? = HeadersX()
    val detailsList: ArrayList<DetailX> = ArrayList()
    val emrFavRequestModel: InvestigationFavRequestModel? = InvestigationFavRequestModel()


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
                R.layout.dialog_manage_investigation_favourites,
                container,
                false
            )
        viewModel = ManageInvestFavourteViewModelFactory(
            requireActivity().application
        )
            .create(ManageInvestFavourteViewModel::class.java)

        hideSoftKeyboard(view)


        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
//        binding?.userNameTextVIew?.setText(userDataStoreBean?.user_name?:"")
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        binding?.userNameTextVIew?.setText(userDataStoreBean?.title?.name + ". " + userDataStoreBean?.first_name)


//        binding?.viewModel?.getDepartmentList(facility_UUID, FavLabdepartmentRetrofitCallBack)
        binding?.viewModel?.getAllDepartment(facility_UUID, favLabAddAllDepartmentCallBack)

        if (isTablet(requireContext())) {
            mAdapter = ManageFavInstituteAdapter(requireContext(), ArrayList())
            binding?.manageInstituteData!!.adapter = mAdapter

            mAdapter =
                ManageFavInstituteAdapter(
                    requireActivity(),
                    ArrayList()
                )

            val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding?.manageInstituteData?.layoutManager = linearLayoutManager
            binding?.manageInstituteData?.adapter = mAdapter

            mAdapter?.setOnDeleteClickListener(object :
                ManageFavInstituteAdapter.OnDeleteClickListeners {
                override fun onDeleteClick(favouritesID: Int?, testMasterName: String?) {
                    deletefavouriteID = favouritesID

                    drugName = testMasterName

                    viewModel!!.deleteFavourite(
                        facility_UUID,
                        favouritesID, deleteRetrofitResponseCallback
                    )
                    /*

                    if (customdialog == null) {
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
        } else {
            mAdapterMob = ManageFavInstituteMobAdapter(requireContext(), ArrayList())
            binding?.manageInstituteData!!.adapter = mAdapter

            mAdapterMob =
                ManageFavInstituteMobAdapter(
                    requireActivity(),
                    ArrayList()
                )


            val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding?.manageInstituteData?.layoutManager = linearLayoutManager
            binding?.manageInstituteData?.adapter = mAdapterMob

            mAdapterMob?.setOnDeleteClickListener(object :
                ManageFavInstituteMobAdapter.OnDeleteClickListeners {
                override fun onDeleteClick(favouritesID: Int?, testMasterName: String?) {
                    deletefavouriteID = favouritesID

                    drugName = testMasterName

                    viewModel!!.deleteFavourite(
                        facility_UUID,
                        favouritesID,
                        deleteRetrofitResponseCallback
                    )
                    /*

                    if (customdialog == null) {
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
        }
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.cancel?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.clear?.setOnClickListener {
            binding?.dispalyOrder?.setText("")
            binding?.autoCompleteTextViewTestName?.setText("")
        }



        binding?.switchCheckCheif!!.setOnCheckedChangeListener { buttonView, isChecked ->
            is_active = isChecked
        }

        val args = arguments

        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..
            status = args.getBoolean("status")
            displayOrder = args.getIntegerArrayList("DisplayOrder")!!

            if (status!!) {
                favouriteData = args.getParcelable(AppConstants.RESPONSECONTENT)
                Log.i("", "" + favouriteData)
                ResponseContantSave?.favourite_id = favouriteData?.favourite_id!!
                ResponseContantSave?.favourite_display_order =
                    favouriteData!!.favourite_display_order!!
                ResponseContantSave?.test_master_name = favouriteData!!.test_master_name!!
                Str_auto_id = favouriteData!!.test_master_id
                binding?.cheifAdd?.text = "Update"


                binding?.dispalyOrder?.setText(favouriteData!!.favourite_display_order?.toString())
                // binding?.displayOrderTextView?.text = favouriteData!!.favourite_display_order.toString()
                binding?.autoCompleteTextViewTestName!!.setText(favouriteData?.test_master_name)
                binding?.autoCompleteTextViewTestName!!.isFocusable = false

                if (isTablet(requireContext()))
                    mAdapter?.setFavAddItem(ResponseContantSave)
                else
                    mAdapterMob?.setFavAddItem(ResponseContantSave)
            }
        }

        binding?.addCardView?.setOnClickListener {
            val Str_DisplayOrder = binding?.dispalyOrder?.text.toString()
            if (Str_DisplayOrder.isEmpty()) {
                Toast.makeText(requireContext(), "Please Enter Display Order", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (displayOrder.contains(Str_DisplayOrder.toInt())) {
                Toast.makeText(requireContext(), "Display Order Already Exists", Toast.LENGTH_LONG)
                    .show()
                binding?.dispalyOrder?.error = "Display Order Already Exists"
                return@setOnClickListener
            }

            if (Str_auto_id == 0) {
                Toast.makeText(requireContext(), "Please Enter Test name", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!status!!) {
                detailsList.clear()
                header?.is_public = false
                header?.facility_uuid = facility_UUID?.toString()!!
                header?.favourite_type_uuid = AppConstants.FAV_TYPE_ID_INVESTIGATION
                header?.department_uuid = deparment_UUID?.toString()!!
                header?.user_uuid = userDataStoreBean?.uuid?.toString()!!
                header?.display_order = Str_DisplayOrder
                header?.revision = true
                header?.is_active = is_active

                val details = DetailX()
                details.test_master_uuid = Str_auto_id!!.toInt()
                details.test_master_type_uuid = AppConstants.INVESTIGATION_TESTMASTER_UUID
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
                    facility_UUID, emrFavRequestModel!!, emrposFavtRetrofitCallback
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
                    binding?.autoCompleteTextViewTestName?.text.toString(),
                    emrposListDataFavtEditRetrofitCallback
                )
            }
        }

        /*       binding?.spinnerFavInvestdepartment?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
                   binding?.viewModel?.getAllDepartment(facility_UUID, favLabAddAllDepartmentCallBack)
                   false
               })*/
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
            Str_auto_id = autocompleteTestResponse?.get(position)?.uuid
            Log.i("", "" + autocompleteTestResponse!!.get(position).name)
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
                binding?.spinnerFavInvestdepartment!!.adapter = adapter

            }

            override fun onBadRequest(response: Response<FavAddResponseModel>) {

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
            binding?.spinnerFavInvestdepartment!!.adapter = adapter


            for (i in listAllAddDepartmentItems.indices) {

                if (listAllAddDepartmentItems[i]!!.uuid == deparment_UUID) {

                    binding?.spinnerFavInvestdepartment!!.setSelection(i)

                    break

                }

            }

        }

        override fun onBadRequest(errorBody: Response<FavAddAllDepatResponseModel>?) {

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
    val emrposFavtRetrofitCallback =
        object : RetrofitCallback<InvestigationFavManageResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<InvestigationFavManageResponseModel>?) {
                Log.i(
                    "",
                    "investigation" + responseBody?.body()?.responseContents?.headers?.favourite_type_uuid
                )

                viewModel?.getAddListFav(
                    facility_UUID,
                    responseBody?.body()?.responseContents?.headers?.uuid,
                    responseBody?.body()?.responseContents?.headers?.favourite_type_uuid,
                    emrposListDataFavtRetrofitCallback
                )

                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Favorite Added successfully"
                )
            }

            override fun onBadRequest(response: Response<InvestigationFavManageResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: InvestigationFavManageResponseModel
                if (response!!.code() == 400) {
                    val gson = GsonBuilder().create()
                    var mError = ErrorAPIClass()
                    try {
                        mError =
                            gson.fromJson(
                                response.errorBody()!!.string(),
                                ErrorAPIClass::class.java
                            )
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

    val emrposListDataFavtRetrofitCallback = object : RetrofitCallback<ManageFavAddResponse> {
        override fun onSuccessfulResponse(responseBody: Response<ManageFavAddResponse>?) {
            Toast.makeText(
                requireContext(),
                responseBody?.body()?.message ?: "",
                Toast.LENGTH_SHORT
            ).show()
            if (is_active) {

                if (isTablet(requireContext()))
                    mAdapter?.setFavAddItem(responseBody?.body()?.responseContents)
                else
                    mAdapterMob?.setFavAddItem(responseBody?.body()?.responseContents)
            }

            binding!!.autoCompleteTextViewTestName.setText("")
            binding!!.dispalyOrder.setText("")

            Toast.makeText(context, "Favorites added successfully", Toast.LENGTH_SHORT).show()

            callbackfavourite?.onRefreshList()


        }

        override fun onBadRequest(response: Response<ManageFavAddResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: ManageFavAddResponse
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
            if (isTablet(requireContext()))
                mAdapter?.adapterrefresh(deletefavouriteID)
            else
                mAdapterMob?.adapterrefresh(deletefavouriteID)

            Toast.makeText(requireContext(), "$drugName Deleted Successfully", Toast.LENGTH_SHORT)
                .show()

            callbackfavourite?.onRefreshList()

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
            Toast.makeText(
                requireContext(),
                responseBody?.body()?.message ?: "",
                Toast.LENGTH_SHORT
            ).show()

            ResponseContantSave?.favourite_id = responseBody?.body()?.requestContent?.favourite_id!!
            ResponseContantSave?.favourite_display_order =
                responseBody.body()?.requestContent?.favourite_display_order!!
            ResponseContantSave?.test_master_name = responseBody.body()?.requestContent?.testname!!

/*
            alertDialog(responseBody?.body()?.message ?: "")
*/
            Log.i("", "" + responseBody.body()?.requestContent)


            if (isTablet(requireContext())) {
                mAdapter?.clearadapter()
                mAdapter?.setFavAddItem(ResponseContantSave)
            } else {


                mAdapterMob?.clearadapter()
                mAdapterMob?.setFavAddItem(ResponseContantSave)
            }

/*
            viewModel?.getAddListFav(facility_UUID, responseBody?.body()?.requestContent?.favourite_id,AppConstants.FAV_TYPE_ID_INVESTIGATION,emrposListDataFavtRetrofitCallback)
*/
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody.body()?.message!!
            )

/*
            callbackfavourite?.onRefreshList()
*/
            callbackfavourite?.onFavID(responseBody.body()?.requestContent?.favourite_id!!)


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


    fun setOnFavRefreshListener(callback: OnFavRefreshListener) {
        this.callbackfavourite = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnFavRefreshListener {
        fun onFavID(position: Int)
        fun onRefreshList()
    }

/*
    interface OnFavRefreshListener {
        fun onFavRadiologyID(position: Int)
        fun onRefreshList()
    }
*/


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