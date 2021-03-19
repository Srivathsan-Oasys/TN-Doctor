package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view

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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogManageDiagonosisFavouritesBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.request.ChiefCompliantAddRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.request.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.request.Headers
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.addFavlistDiagonosisDataModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.diagonosissearch.DiagonosisSearchResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.diagonosissearch.ResponseContentsdiagonosissearch
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view_model.DiagonosisDialogViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view_model.DiagonosisDialogViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddTestNameResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabFavManageResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavAddListResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.io.IOException

class FavaddDiagonosisDialog : DialogFragment() {
    private var deletefavouriteID: Int? = 0
    private var is_active: Boolean = true
    private var Str_auto_id: Int? = 0
    private var content: String? = null
    private var drugName: String? = ""
    private var viewModel: DiagonosisDialogViewModel? = null
    var binding: DialogManageDiagonosisFavouritesBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var favAddResponseMap = mutableMapOf<Int, String>()
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    val header: Headers = Headers()
    val detailsList: ArrayList<Detail> = ArrayList()
    var labManageFavAdapter: DiagnosisFavAdapter? = null
    var labManageFavMobAdapter: DiagnosisFavMobileAdapter? = null
    val emrFavRequestModel: ChiefCompliantAddRequestModel = ChiefCompliantAddRequestModel()
    private var favouriteData: FavouritesModel? = null
    var callbackfavourite: OnFavRefreshListener? = null
    private var departmentID: Int? = 0
    private var customdialog: Dialog? = null
    var status: Boolean? = false
    var IsTablet: Boolean = false
    lateinit var drugNmae: TextView
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView

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
            R.layout.dialog_manage_diagonosis_favourites,
            container,
            false
        )
        viewModel = DiagonosisDialogViewModelFactory(
            requireActivity().application
        ).create(DiagonosisDialogViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        dropdownReferenceView = binding!!.chiefComplaintSearch!!
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        IsTablet = resources.getBoolean(R.bool.isTablet)

        /*   binding?.viewModel?.getDepartmentList(facility_UUID, FavLabdepartmentRetrofitCallBack)

           viewModel?.getTestName(facility_UUID.toString(), favAddTestNameCallBack)
   */

        if (IsTablet) {

            labManageFavAdapter =
                DiagnosisFavAdapter(
                    requireActivity(),
                    ArrayList()
                )

            binding?.manageFavouritesRecyclerView?.adapter = labManageFavAdapter

        } else {

            labManageFavMobAdapter =
                DiagnosisFavMobileAdapter(
                    requireActivity(),
                    ArrayList()
                )

            binding?.manageFavouritesRecyclerView?.adapter = labManageFavMobAdapter


        }
        binding?.switchCheckCheif!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            is_active = isChecked
        })
        binding?.clearCardView?.setOnClickListener {
            if (this.status!!) {
                binding?.chiefComplaintSearch?.setText("")
                binding?.displayorderedittext?.setText("")
            } else {
                binding?.displayorderedittext?.setText("")
                binding?.chiefComplaintSearch?.isFocusable = true
                binding?.chiefComplaintSearch?.isFocusableInTouchMode =
                    true // user touches widget on phone with touch screen
                binding?.chiefComplaintSearch?.isClickable = true //

            }

        }

        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..
            favouriteData = args.getParcelable(AppConstants.RESPONSECONTENT)
            Log.i("", "" + favouriteData)
            binding?.diagonosisAdd?.text = "Update"

            val data: addFavlistDiagonosisDataModel = addFavlistDiagonosisDataModel()
            data.diagonosis_description = favouriteData?.diagnosis_description
            data.diagonosisname = favouriteData?.diagnosis_name!!
            data.diagonosiscode = favouriteData?.diagnosis_code
            data.diagonosis_id = favouriteData?.favourite_id!!.toString()
            data.display_order = favouriteData?.favourite_display_order?.toString()
            Str_auto_id = favouriteData?.diagnosis_id?.toInt()

            binding?.chiefComplaintSearch?.setText(favouriteData?.diagnosis_name)
            binding?.displayorderedittext?.setText(favouriteData?.favourite_display_order?.toString())
            binding?.chiefComplaintSearch?.isEnabled = false
            if (IsTablet)
                labManageFavAdapter!!.setFavAddItem(data)
            else
                labManageFavMobAdapter!!.setFavAddItem(data)
        }


        labManageFavMobAdapter?.setOnDeleteClickListener(object :
            DiagnosisFavMobileAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesID: Int?, diagonosisname: String) {
                deletefavouriteID = favouritesID

                drugName = diagonosisname

                viewModel!!.deleteFavourite(
                    facility_UUID,
                    favouritesID, deleteRetrofitResponseCallback
                )

                /*   customdialog = Dialog(requireContext())
                   customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
                   customdialog!! .setCancelable(false)
                   customdialog!! .setContentView(R.layout.delete_cutsom_layout)
                   val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView

                   closeImageView.setOnClickListener {
                       customdialog?.dismiss()
                   }
                   drugNmae = customdialog!! .findViewById(R.id.addDeleteName) as TextView
                   drugNmae.text ="${drugNmae.text.toString()} '"+diagonosisname+"' Record ?"

                   val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView
                   val noBtn = customdialog!! .findViewById(R.id.no) as CardView
                   yesBtn.setOnClickListener {


                   }
                   noBtn.setOnClickListener {
                       customdialog!! .dismiss()
                   }
                   customdialog!! .show()
   */
            }

        })
        labManageFavAdapter?.setOnDeleteClickListener(object :
            DiagnosisFavAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesID: Int?, diagonosisname: String) {
                deletefavouriteID = favouritesID

                drugName = diagonosisname

                viewModel!!.deleteFavourite(
                    facility_UUID,
                    favouritesID, deleteRetrofitResponseCallback
                )
/*
                customdialog = Dialog(requireContext())
                customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!! .setCancelable(false)
                customdialog!! .setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                }
                drugNmae = customdialog!! .findViewById(R.id.addDeleteName) as TextView
                drugNmae.text ="${drugNmae.text.toString()} '"+diagonosisname+"' Record ?"

                val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView
                val noBtn = customdialog!! .findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {


                }
                noBtn.setOnClickListener {
                    customdialog!! .dismiss()
                }
                customdialog!! .show()*/

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
                        facility_UUID,
                        getComplaintSearchRetrofitCallBack
                    )

                }
            }
        })

        binding?.diagonosisAdd?.setOnClickListener {
            val Str_DisplayOrder = binding?.displayorderedittext?.text.toString()

            if (Str_auto_id == 0) {
                Toast.makeText(requireContext(), "Please select diagnosis name", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (Str_DisplayOrder.isEmpty()) {
                Toast.makeText(requireContext(), "Please select display order", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (status as Boolean) {
                detailsList.clear()
                header.is_public = true
                header.facility_uuid = facility_UUID?.toString()!!
                header.favourite_type_uuid = AppConstants.FAV_TYPE_ID_DIAGNOSIS
                header.department_uuid = deparment_UUID?.toString()!!
                header.user_uuid = userDataStoreBean?.uuid!!
                header.display_order = binding!!.displayorderedittext?.text.toString()
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
                details.chief_complaint_uuid = 0
                details.vital_master_uuid = 0
                details.drug_route_uuid = 0
                details.drug_frequency_uuid = 0
                details.duration = 0
                details.duration_period_uuid = 0
                details.drug_instruction_uuid = 0
                details.display_order = binding!!.displayorderedittext?.text.toString()
                details.revision = true
                if (is_active) {
                    details.is_active = "true"
                } else {
                    details.is_active = "false"
                }
                details.diagnosis_uuid = Str_auto_id!!.toInt()
                detailsList.add(details)
                emrFavRequestModel.headers = this.header
                emrFavRequestModel.details = this.detailsList
                viewModel?.AddFavourite(facility_UUID!!, emrFavRequestModel, addFavRetrofitCallBack)
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
        return binding!!.root
    }


    fun setAdapter(
        responseContents: List<ResponseContentsdiagonosissearch?>?,
        selectedSearchPosition: Int
    ) {
        val responseContentAdapter = DianosisSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.chiefComplaintSearch?.threshold = 1
        binding!!.chiefComplaintSearch?.setAdapter(responseContentAdapter)
        binding!!.chiefComplaintSearch?.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as ResponseContentsdiagonosissearch?
            binding!!.chiefComplaintSearch?.setText(selectedPoi?.name)
            Str_auto_id = selectedPoi?.uuid
        }
    }

    val getComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<DiagonosisSearchResponse> {
            override fun onSuccessfulResponse(response: Response<DiagonosisSearchResponse>) {
                //responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    setAdapter(response.body()?.responseContents, 0)
                }
            }

            override fun onBadRequest(response: Response<DiagonosisSearchResponse>) {
                val gson = GsonBuilder().create()
                val responseModel: DiagonosisSearchResponse
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        DiagonosisSearchResponse::class.java
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

    val addFavRetrofitCallBack =
        object : RetrofitCallback<LabFavManageResponseModel> {
            override fun onSuccessfulResponse(response: Response<LabFavManageResponseModel>) {
                binding?.displayorderedittext?.setText("")
                binding?.chiefComplaintSearch?.setText("")
                val responseContents = response.body()?.responseContents
                viewModel?.getAddListFav(
                    facility_UUID,
                    responseContents?.headers?.uuid,
                    emrposListDataFavtRetrofitCallback
                )

                toast("Favoite Added Successfully")

            }

            override fun onBadRequest(response: Response<LabFavManageResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: LabFavManageResponseModel

                if (response.code() == 400) {
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

    private fun toast(s: String) {

        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
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

            var rasponse = responseBody?.body()?.message
            customdialog?.dismiss()
            if (IsTablet)
                labManageFavAdapter?.clearadapter()
            else
                labManageFavMobAdapter?.clearadapter()


            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "$drugName deleted Successfully"
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
            if (!status!!) {

                if (IsTablet)
                    labManageFavAdapter?.clearadapter()
                else
                    labManageFavMobAdapter?.clearadapter()
            }
            Toast.makeText(requireContext(), "Favorites added successfully", Toast.LENGTH_LONG)
                .show()

            val data: addFavlistDiagonosisDataModel = addFavlistDiagonosisDataModel()
            data.diagonosis_description =
                responseBody?.body()?.responseContents?.diagnosis_description!!
            data.diagonosisname = responseBody.body()?.responseContents?.diagnosis_name!!
            data.diagonosiscode = responseBody.body()?.responseContents?.diagnosis_code!!
            data.diagonosis_id = responseBody.body()?.responseContents?.favourite_id?.toString()!!
            data.display_order =
                responseBody.body()?.responseContents?.favourite_display_order?.toString()
            binding?.displayorderedittext?.setText("")
//            binding?.displayorderedittext?.setText(responseBody.body()?.responseContents?.favourite_display_order?.toString())

            if (IsTablet)
                labManageFavAdapter!!.setFavAddItem(data)
            else
                labManageFavMobAdapter!!.setFavAddItem(data)

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