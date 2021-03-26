package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogManageTreatmentkitDiagonosisFavouritesBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model.addFavlistDiagonosisDataModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddTestNameResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.FavAddListResponse
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.TreatAddFavRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response.TreaFavAddedResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response.TreatmentNameResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response.TreatmentNameresponseContent
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.view_model.TreatKitFavAddDialogViewModel
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.view_model.TreatmentKitFavAddViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class FavaddTreatmentKitDialog : DialogFragment() {

    private var deletefavouriteID: Int? = 0
    private var is_active: Boolean = true
    private var Str_auto_id: Int? = 0
    private var content: String? = null
    private var viewModel: TreatKitFavAddDialogViewModel? = null
    private var binding: DialogManageTreatmentkitDiagonosisFavouritesBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var favAddResponseMap = mutableMapOf<Int, String>()
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    val header: com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.Headers =
        com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.Headers()
    val detailsList: ArrayList<com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.Detail> =
        ArrayList()
    var labManageFavAdapter: TreatmentKitFavouritesAdapter? = null
    val emrFavRequestModel: TreatAddFavRequestModel = TreatAddFavRequestModel()
    private var favouriteData: FavouritesModel? = null
    var callbackfavourite: OnFavRefreshListener? = null
    private var departmentID: Int? = 0
    private var customdialog: Dialog? = null
    var status: Boolean? = false
    var updatestatus: Boolean? = false
    lateinit var drugNmae: TextView
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    private var deletePosition: Int? = 0

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
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (dialog.window != null)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window?.attributes?.windowAnimations = R.style.CardDialogAnimation
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_manage_treatmentkit_diagonosis_favourites,
            container,
            false
        )
        viewModel = TreatmentKitFavAddViewModelFactory(
            requireActivity().application
        ).create(TreatKitFavAddDialogViewModel::class.java)

        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        dropdownReferenceView = binding!!.chiefComplaintSearch
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        binding?.viewModel?.getDepartmentList(
            facility_UUID,
            templateRadiodepartmentRetrofitCallBack
        )
        /*   binding?.viewModel?.getDepartmentList(facility_UUID, FavLabdepartmentRetrofitCallBack)

           viewModel?.getTestName(facility_UUID.toString(), favAddTestNameCallBack)
   */
        labManageFavAdapter =
            TreatmentKitFavouritesAdapter(
                requireActivity(),
                ArrayList()
            )
        binding?.manageFavouritesRecyclerView?.adapter = labManageFavAdapter
        binding?.switchCheckCheif!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            is_active = isChecked
        })


        binding?.clearCardView?.setOnClickListener {
            if (!updatestatus!!) {
                binding?.chiefComplaintSearch?.setText("")
            }
            binding?.displayorderedittext?.setText("")
            binding?.displayorderedittext?.clearFocus()
            binding?.chiefComplaintSearch?.clearFocus()
            binding?.switchCheckCheif?.isChecked = true
        }

        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..
            favouriteData = args.getParcelable(AppConstants.RESPONSECONTENT)

            binding?.diagonosisAdd?.text = "Update"

            updatestatus = true
            val data: addFavlistDiagonosisDataModel = addFavlistDiagonosisDataModel()
            //data.diagonosis_description= favouriteData?.diagnosis_description
            data.diagonosisname = favouriteData?.favourite_name.toString()
            data.diagonosiscode = favouriteData?.favourite_display_order.toString()
            data.diagonosis_id = favouriteData?.favourite_id.toString()
            Str_auto_id = favouriteData?.favourite_id
            binding?.chiefComplaintSearch?.setText(favouriteData?.favourite_name.toString())
            binding?.displayorderedittext?.setText(favouriteData?.favourite_display_order?.toString())
            binding?.chiefComplaintSearch?.isEnabled = false
            labManageFavAdapter!!.addRow(data)
        }
        labManageFavAdapter?.setOnDeleteClickListener(object :
            TreatmentKitFavouritesAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesID: Int?, diagonosisname: String, position: Int) {
                deletefavouriteID = favouritesID

                Log.e("favouriteId", favouritesID.toString())
                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                }
                drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                drugNmae.text = "Are you sure you want to Delete '" + diagonosisname + "' record?"
                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    deletePosition = position
                    viewModel!!.deleteFavourite(
                        facility_UUID,
                        favouritesID, deleteRetrofitResponseCallback
                    )

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


                    viewModel!!.getTreatmentSearchResult(
                        s.toString(),
                        facility_UUID,
                        getComplaintSearchRetrofitCallBack
                    )

                }
            }
        })

        binding?.saveCardView?.setOnClickListener {

            if (!binding?.displayorderedittext?.text.toString().isEmpty()) {

                if (Str_auto_id != 0) {
                    if (status as Boolean) {
                        detailsList.clear()

                        header.is_public = true

                        header.facility_uuid = facility_UUID?.toString()!!

                        header.favourite_type_uuid = AppConstants.FAV_TYPE_ID_TREATMENTKIT

                        header.department_uuid = deparment_UUID?.toString()!!

                        header.user_uuid = userDataStoreBean?.uuid!!.toString()

                        header.display_order = binding!!.displayorderedittext.text.toString()

                        header.revision = true

                        header.is_active = is_active

                        val details: com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.Detail =
                            com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.Detail()
                        details.test_master_uuid = 0
                        details.test_master_type_uuid = 0
                        details.item_master_uuid = 0
                        details.chief_complaint_uuid = 0
                        details.vital_master_uuid = 0
                        details.drug_route_uuid = 0
                        details.drug_frequency_uuid = 0
                        details.duration = 0
                        details.duration_period_uuid = 0
                        details.drug_instruction_uuid = 0
                        details.display_order = binding!!.displayorderedittext.text.toString()
                        details.revision = true
                        details.is_active = is_active
                        details.treatment_kit_uuid = Str_auto_id!!.toInt()
                        detailsList.add(details)
                        emrFavRequestModel.headers = this.header
                        emrFavRequestModel.details = this.detailsList
                        viewModel?.AddFavourite(
                            facility_UUID!!,
                            emrFavRequestModel,
                            addFavRetrofitCallBack
                        )
                    } else {
                        //False

                        val displayOrder = binding?.displayorderedittext!!.text.toString()

                        viewModel?.getEditFavourite(
                            facility_UUID,
                            displayOrder.toInt(),
                            favouriteData?.favourite_id,
                            is_active,
                            emrposListDataFavtEditRetrofitCallback
                        )

                    }
                } else {
                    Toast.makeText(activity, "Enter valid Treatment name", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(activity, "Enter Display Order", Toast.LENGTH_LONG).show()
            }

        }
        return binding!!.root
    }


    fun setAdapter(
        responseContents: List<TreatmentNameresponseContent?>?,
        selectedSearchPosition: Int
    ) {
        val responseContentAdapter = TreatmentDiagnosisSearchResultAdapter(
            requireActivity(),
            R.layout.row_chief_complaint_search_result, responseContents
        )
        binding!!.chiefComplaintSearch.threshold = 1
        binding!!.chiefComplaintSearch.setAdapter(responseContentAdapter)
        binding!!.chiefComplaintSearch.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as TreatmentNameresponseContent?
            binding!!.chiefComplaintSearch.setText(selectedPoi?.treatment_name)
            Str_auto_id = selectedPoi?.treatment_kit_id

            //Log.e("selectedId",Str_auto_id.toString())
        }
    }

    val getComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<TreatmentNameResponseModel> {
            override fun onSuccessfulResponse(response: Response<TreatmentNameResponseModel>) {
                //responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    setAdapter(response.body()?.responseContents, 0)
                }
            }

            override fun onBadRequest(response: Response<TreatmentNameResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: TreatmentNameResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        TreatmentNameResponseModel::class.java
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
        object : RetrofitCallback<TreaFavAddedResponseModel> {
            override fun onSuccessfulResponse(response: Response<TreaFavAddedResponseModel>) {

                val responseContents = response.body()?.responseContents

                Toast.makeText(activity, response.body()?.message.toString(), Toast.LENGTH_LONG)
                    .show()
                Log.e("favAdded", responseContents?.details.toString())
                //Reused Model
                val data: addFavlistDiagonosisDataModel = addFavlistDiagonosisDataModel()

                data.diagonosisname = binding?.chiefComplaintSearch?.text.toString()
                data.diagonosiscode = binding?.displayorderedittext?.text.toString()
                data.diagonosis_id =
                    responseContents?.details!![0]?.favourite_master_uuid.toString()

                binding?.chiefComplaintSearch?.setText("")
                binding?.displayorderedittext?.setText("")

                //labManageFavAdapter?.clearadapter()
                labManageFavAdapter!!.addRow(data)
                callbackfavourite?.onRefreshList()
            }

            override fun onBadRequest(response: Response<TreaFavAddedResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: TreaFavAddedResponseModel
                try {

//                    responseModel = gson.fromJson(
//                        response.errorBody()!!.string(),
//                        TreaFavAddedResponseModel::class.java
//                    )

                    Toast.makeText(activity, response.message(), Toast.LENGTH_LONG).show()

                } catch (e: Exception) {

                    Toast.makeText(
                        activity,
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG
                    ).show()

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

            Log.e("UpdateSuccess", responseBody?.body()?.message.toString())

            Toast.makeText(activity, responseBody?.body()?.message, Toast.LENGTH_LONG).show()
            //Reused Model
            val data: addFavlistDiagonosisDataModel = addFavlistDiagonosisDataModel()

            data.diagonosisname = binding?.chiefComplaintSearch?.text.toString()
            data.diagonosiscode = binding?.displayorderedittext?.text.toString()
            data.diagonosis_id = responseBody?.body()?.requestContent?.favourite_id.toString()

            binding?.chiefComplaintSearch?.setText("")
            binding?.displayorderedittext?.setText("")

            labManageFavAdapter?.clearAdapter()
            labManageFavAdapter!!.addRow(data)

            callbackfavourite?.onRefreshList()

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

            Log.e("DeleteFav", responseBody?.body()?.message.toString())
            customdialog?.dismiss()
            labManageFavAdapter?.removeItem(deletePosition!!)

            //callbackfavourite?.onFavID(deletefavouriteID!!)
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


            val data: addFavlistDiagonosisDataModel = addFavlistDiagonosisDataModel()

            data.diagonosisname = binding?.chiefComplaintSearch?.text.toString()
            data.diagonosiscode = binding?.displayorderedittext?.text.toString()

            binding?.chiefComplaintSearch?.setText("")
            binding?.displayorderedittext?.setText("")

            labManageFavAdapter?.clearAdapter()
            labManageFavAdapter!!.addRow(data)

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


    val templateRadiodepartmentRetrofitCallBack =
        object : RetrofitCallback<FavAddResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavAddResponseModel>) {

                listDepartmentItems.add(response.body()?.responseContent)
                favAddResponseMap =
                    listDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()

                val adapter = ArrayAdapter<String>(
                    activity!!,
                    R.layout.spinner_item,
                    favAddResponseMap.values.toMutableList()
                )
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.spinnerFavdepartment!!.adapter = adapter
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