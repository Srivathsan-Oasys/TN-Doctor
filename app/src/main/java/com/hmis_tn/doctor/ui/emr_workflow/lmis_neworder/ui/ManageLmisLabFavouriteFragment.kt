package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogManageLmisLabFavouriteBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.*
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.FavAddListResponse
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.request.Detail
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.request.Headers
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.request.RequestLabFavModel
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.view_model.ManageLmisLabFavouriteViewModel
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.view_model.ManageLmisLabFavourteViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.io.IOException

class ManageLmisLabFavouriteFragment : DialogFragment() {
    private var viewModel: ManageLmisLabFavouriteViewModel? = null
    var binding: DialogManageLmisLabFavouriteBinding? = null
    var appPreferences: AppPreferences? = null
    var Lab_UUID: Int? = 0
    private var utils: Utils? = null
    val header: Headers? = Headers()
    private var ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
    private var is_active: Boolean = true
    val detailsList: ArrayList<Detail> = ArrayList()
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var favouriteData: FavouritesModel? = null
    private var deletefavouriteID: Int? = 0
    private var customdialog: Dialog? = null
    lateinit var drugNmae: TextView
    var callbackfavourite: OnFavRefreshListener? = null
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var favAddResponseMap = mutableMapOf<Int, String>()
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    private var Str_auto_id: Int? = 0
    private var typestatus: String? = ""
    val emrFavRequestModel: RequestLabFavModel? = RequestLabFavModel()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var status: Boolean? = false
    var labManageFavAdapter: ManageLabManageFavAdapter? = null


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
                R.layout.dialog_manage_lmis_lab_favourite,
                container,
                false
            )
        viewModel = ManageLmisLabFavourteViewModelFactory(
            requireActivity().application
        )
            .create(ManageLmisLabFavouriteViewModel::class.java)


        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        Lab_UUID = appPreferences?.getInt(AppConstants.LAB_UUID)

        binding?.userNameTextView?.setText(userDataStoreBean?.user_name)

        labManageFavAdapter =
            ManageLabManageFavAdapter(
                requireActivity(),
                ArrayList()
            )

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.listdatarecycleview?.layoutManager = linearLayoutManager
        binding?.listdatarecycleview?.adapter = labManageFavAdapter
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
            binding?.addFav?.text = "Update"
            binding?.editTextDisplayOrder?.setText(favouriteData!!.favourite_display_order?.toString())
            binding?.autoCompleteTextViewTestName!!.setText(favouriteData?.test_master_name)
            binding?.autoCompleteTextViewTestName!!.isFocusable = false
            labManageFavAdapter?.setFavAddItem(ResponseContantSave)
        }
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
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
        labManageFavAdapter?.setOnDeleteClickListener(object :
            ManageLabManageFavAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesID: Int?, testMasterName: String?) {
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

                drugNmae.text = "${drugNmae.text} '" + testMasterName + "' Record ?"
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
        binding?.autoCompleteTextViewTestName!!.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteTextViewTestName?.setText(autocompleteTestResponse?.get(position)?.name)
            Str_auto_id = autocompleteTestResponse?.get(position)?.uuid
            typestatus = autocompleteTestResponse?.get(position)?.type
        }
        binding?.addFav?.setOnClickListener {
            val Str_DisplayOrder = binding?.editTextDisplayOrder?.text.toString()
            if (status as Boolean) {
                detailsList.clear()

                header?.is_public = false
                header?.facility_uuid = facility_UUID?.toString()!!
                header?.favourite_type_uuid = AppConstants.FAV_TYPE_ID_LAB
                header?.department_uuid = deparment_UUID ?: 0
                header?.user_uuid = userDataStoreBean?.uuid?.toString()!!
                header?.display_order = Str_DisplayOrder
                header?.revision = true
                header?.is_active = is_active
                header?.lab_uuid = Lab_UUID!!

                val details: Detail = Detail()

                if (typestatus.equals("test_master")) {
                    //testmethod
                    details.test_master_uuid = Str_auto_id!!.toInt()
                    details.is_profile = false
                    details.profile_uuid = 0
                } else {
                    //profileuuid
                    details.test_master_uuid = null
                    details.profile_uuid = Str_auto_id!!.toInt()
                    details.is_profile = true
                }
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



        return binding!!.root
    }

    /*   val FavLabdepartmentRetrofitCallBack =
           object : RetrofitCallback<FavAddResponseModel> {
               override fun onSuccessfulResponse(response: Response<FavAddResponseModel>) {

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

            labManageFavAdapter?.setFavAddItem(responseBody?.body()?.responseContents)
            Toast.makeText(requireContext(), "Favorites successfully added", Toast.LENGTH_LONG)
                ?.show()
            //Callback
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

    fun setOnFavRefreshListener(callback: OnFavRefreshListener) {
        this.callbackfavourite = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnFavRefreshListener {

        fun onRefreshList()
    }

    val emrposListDataFavtEditRetrofitCallback = object : RetrofitCallback<FavEditResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavEditResponse>?) {

            ResponseContantSave?.favourite_id = responseBody?.body()?.requestContent?.favourite_id
            ResponseContantSave?.favourite_display_order =
                responseBody?.body()?.requestContent?.favourite_display_order
            ResponseContantSave?.test_master_name = responseBody?.body()?.requestContent?.testname
            labManageFavAdapter?.clearadapter()

            labManageFavAdapter?.setFavAddItem(ResponseContantSave)
            Toast.makeText(requireContext(), "Favorites Updated successfully", Toast.LENGTH_LONG)
                ?.show()
            callbackfavourite!!.onRefreshList()
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
}