package com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.ui

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
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogManageChiefComplaintFavouritesBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ComplaintSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.ChiefComplaintSearchResultAdapter
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.ChiefComplaintFavAddresponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.addFavlistDataModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.request.ChiefCompliantAddRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.request.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.request.Headers
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.viewmodel.ChiefcomplaintDialogViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.viewmodel.ChiefcomplaintDialogViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddTestNameResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavAddListResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.io.IOException

class FavaddCheifcomplaintDialog : DialogFragment() {
    private var deletefavouriteID: Int? = 0
    private var is_active: Boolean = true
    private var Str_auto_id: Int? = 0
    private var content: String? = null
    private var viewModel: ChiefcomplaintDialogViewModel? = null

    var binding: DialogManageChiefComplaintFavouritesBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    var drugNmae: TextView? = null
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var favAddResponseMap = mutableMapOf<Int, String>()
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    val header: Headers = Headers()
    val detailsList: ArrayList<Detail> = ArrayList()
    var labManageFavAdapter: ChiefManageFavAdapter? = null
    val emrFavRequestModel: ChiefCompliantAddRequestModel = ChiefCompliantAddRequestModel()
    private var favouriteData: FavouritesModel? = null
    var callbackfavourite: OnFavRefreshListener? = null
    private var departmentID: Int? = 0
    private var customdialog: Dialog? = null
    var status: Boolean? = false
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView

    var displayOrder: ArrayList<Int> = ArrayList()


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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_manage_chief_complaint_favourites,
            container,
            false
        )
        viewModel = ChiefcomplaintDialogViewModelFactory(
            requireActivity().application
        ).create(ChiefcomplaintDialogViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        dropdownReferenceView = binding!!.chiefComplaintSearch
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        /*   binding?.viewModel?.getDepartmentList(facility_UUID, FavLabdepartmentRetrofitCallBack)

           viewModel?.getTestName(facility_UUID.toString(), favAddTestNameCallBack)
   */
        labManageFavAdapter =
            ChiefManageFavAdapter(
                requireActivity(),
                ArrayList()
            )
        binding?.manageFavouritesRecyclerView?.adapter = labManageFavAdapter
        binding?.manageFavouritesMobileRecyclerView?.adapter = labManageFavAdapter
        binding?.switchCheckCheif!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            is_active = isChecked
        })


        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..

            status = args.getBoolean("status")

            displayOrder = args.getIntegerArrayList("DisplayOrder")!!


            if (status!!) {


            } else {

                binding?.cheifAdd?.text = "Update"
                favouriteData = args.getParcelable(AppConstants.RESPONSECONTENT)
                Log.i("", "" + favouriteData)
                val data: addFavlistDataModel = addFavlistDataModel()
                data.chiefComplaintId = favouriteData?.favourite_id!!
                data.chiefComplaintName = favouriteData?.chief_complaint_name!!
                data.displayOrder = favouriteData?.favourite_display_order?.toInt()!!

                binding?.chiefComplaintSearch?.setText(favouriteData?.chief_complaint_name)
                binding?.displayorderedittext?.setText(favouriteData?.favourite_display_order?.toString())
                binding?.chiefComplaintSearch?.isEnabled = false
                labManageFavAdapter!!.addRow(data)

            }
        }



        labManageFavAdapter?.setOnDeleteClickListener(object :
            ChiefManageFavAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesID: Int?, chiefComplaintName: String) {
                deletefavouriteID = favouritesID

                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                }
                drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                drugNmae!!.text = "${drugNmae!!.text} '" + chiefComplaintName + "' Record ?"
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
                }
                customdialog!!.show()
            }

        })

//        viewModel!!.getFavourites(departmentID,getFavouritesRetrofitCallBack)

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.cancelCardView?.setOnClickListener {
            dialog?.dismiss()
        }



        binding?.chiefComplaintSearch?.addTextChangedListener { }

        binding?.chiefComplaintSearch?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.length > 2 && s.length < 5) {

                    Log.i("textsixe", "" + s.length)

                    viewModel!!.getComplaintSearchResult(
                        s.toString(),
                        getComplaintSearchRetrofitCallBack
                    )

                }
            }
        })

        binding?.cheifAdd?.setOnClickListener {
            val Str_DisplayOrder = binding?.displayorderedittext?.text.toString()


            if (Str_DisplayOrder.isNullOrEmpty()) {

                binding?.displayorderedittext?.error = "Please Enter Display Order"

                return@setOnClickListener
            }


            if (displayOrder.contains(Str_DisplayOrder.toInt())) {
                Toast.makeText(requireContext(), "Display Order Already Exists", Toast.LENGTH_LONG)
                    .show()
                binding?.displayorderedittext?.error = "Display Order Already Exists"

                return@setOnClickListener
            }

            if (status as Boolean) {
                if (Str_auto_id != 0) {
                    detailsList.clear()

                    header.is_public = true

                    header.facility_uuid = facility_UUID?.toString()!!

                    header.favourite_type_uuid = AppConstants.FAV_TYPE_ID_CHIEF

                    header.department_uuid = deparment_UUID?.toString()!!

                    header.user_uuid = userDataStoreBean?.uuid!!

                    header.display_order = binding!!.displayorderedittext.text.toString()

                    header.revision = true

                    header.ticksheet_type_uuid = 1

                    if (is_active) {

                        header.is_active = "true"

                    } else {
                        header.is_active = "false"
                    }

                    val details: Detail = Detail()
                    details.test_master_uuid = 1
                    details.test_master_type_uuid = 0
                    details.item_master_uuid = 0
                    details.chief_complaint_uuid = Str_auto_id!!.toInt()
                    details.vital_master_uuid = 0
                    details.drug_route_uuid = 0
                    details.drug_frequency_uuid = 0
                    details.duration = 0
                    details.duration_period_uuid = 0
                    details.drug_instruction_uuid = 0
                    details.display_order = binding!!.displayorderedittext.text.toString()
                    details.revision = true
                    if (is_active) {
                        details.is_active = "true"
                    } else {
                        details.is_active = "false"
                    }
                    details.diagnosis_uuid = 0
                    detailsList.add(details)
                    emrFavRequestModel.headers = this.header
                    emrFavRequestModel.details = this.detailsList
                    viewModel?.AddFavourite(
                        facility_UUID!!,
                        emrFavRequestModel,
                        addFavRetrofitCallBack
                    )
                } else {
                    Toast.makeText(requireContext(), "Invalid item", Toast.LENGTH_LONG).show()
                }


            } else {
                //False
                viewModel?.getEditFavourite(
                    facility_UUID,
                    favouriteData?.chief_complaint_id,
                    favouriteData?.favourite_id,
                    Str_DisplayOrder,
                    is_active,
                    emrposListDataFavtEditRetrofitCallback
                )

            }

        }

        binding?.clearTextView?.setOnClickListener {
            binding?.displayorderedittext?.setText("")
            binding?.chiefComplaintSearch?.setText("")
            binding?.displayorderedittext?.clearFocus()
            binding?.chiefComplaintSearch?.clearFocus()
        }
        return binding!!.root
    }


    fun setAdapter(
        responseContents: ArrayList<ResponseContent>,
        selectedSearchPosition: Int
    ) {
        val responseContentAdapter = ChiefComplaintSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.chiefComplaintSearch.threshold = 1
        binding!!.chiefComplaintSearch.setAdapter(responseContentAdapter)
        binding!!.chiefComplaintSearch.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as ResponseContent?
            binding!!.chiefComplaintSearch.setText(selectedPoi?.name)
            Str_auto_id = selectedPoi?.uuid
        }
    }

    val getComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<ComplaintSearchResponseModel> {
            override fun onSuccessfulResponse(response: Response<ComplaintSearchResponseModel>) {
                //responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    setAdapter(response.body()?.responseContents!!, 0)
                }
            }

            override fun onBadRequest(response: Response<ComplaintSearchResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: ComplaintSearchResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        ComplaintSearchResponseModel::class.java
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

    val addFavRetrofitCallBack =
        object : RetrofitCallback<ChiefComplaintFavAddresponseModel> {
            override fun onSuccessfulResponse(response: Response<ChiefComplaintFavAddresponseModel>) {
                Toast.makeText(requireContext(), response.body()?.message ?: "", Toast.LENGTH_SHORT)
                    .show()


                binding?.displayorderedittext?.setText("")

                val responseContents = response.body()?.responseContents
                viewModel?.getAddListFav(
                    facility_UUID,
                    responseContents?.headers?.uuid,
                    emrposListDataFavtRetrofitCallback
                )


            }

            override fun onBadRequest(response: Response<ChiefComplaintFavAddresponseModel>) {
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

    val emrposListDataFavtEditRetrofitCallback = object : RetrofitCallback<FavEditResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavEditResponse>?) {
            Log.i("", "" + responseBody?.body()?.requestContent)

            viewModel?.getAddListFav(
                facility_UUID,
                responseBody?.body()?.requestContent?.favourite_id,
                emrposListDataFavtRetrofitCallback
            )
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )

            callbackfavourite?.onRefreshList()

            dialog!!.dismiss()

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
    val getFavouritesRetrofitCallBack =
        object : RetrofitCallback<FavouritesResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavouritesResponseModel>) {
                val responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {


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

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            labManageFavAdapter?.clearadapter()

            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )

//            callbackfavourite?.onFavID(deletefavouriteID!!)
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

    val emrposListDataFavtRetrofitCallback = object : RetrofitCallback<FavAddListResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavAddListResponse>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            Toast.makeText(requireContext(), responseBody?.body()?.message, Toast.LENGTH_SHORT)
                .show()

            val data: addFavlistDataModel = addFavlistDataModel()
            data.chiefComplaintId = responseBody?.body()?.responseContents?.favourite_id!!
            data.chiefComplaintName = responseBody.body()?.responseContents?.chief_complaint_name!!
            data.displayOrder = responseBody.body()?.responseContents?.favourite_display_order!!
            binding?.chiefComplaintSearch?.setText("")
//            binding?.displayorderedittext?.setText(responseBody.body()?.responseContents?.favourite_display_order?.toString())
            labManageFavAdapter?.clearadapter()
            labManageFavAdapter!!.addRow(data)
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
            callbackfavourite?.onFavID(responseBody.body()?.responseContents?.favourite_id!!)
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