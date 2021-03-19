package com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.ui

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogManageSpecialitySketchFavouritesBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.DietFavMangeResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request.RequestDietFavModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavAddListResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.request.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.request.Headers
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.model.SpecialitySketchFavMangeResponseContents
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.model.SpecialitySketchFavMangeResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.viewmodel.ManageSpecialitySketchFavouriteViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.viewmodel.ManageSpecialitySketchFavouriteViewModelFactory
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.io.IOException

class ManageSpecialitySketchFavouriteFragment : DialogFragment() {
    private var content: String? = null
    private var viewModel: ManageSpecialitySketchFavouriteViewModel? = null
    var binding: DialogManageSpecialitySketchFavouritesBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var autocompleteTestResponse: List<SpecialitySketchFavMangeResponseContents>? = null
    private var customProgressDialog: CustomProgressDialog? = null
    var drugNAme: String = ""
    var status: Boolean? = false
    val header: Headers? = Headers()
    val detailsList: ArrayList<Detail> = ArrayList()
    private var is_active: Boolean = true
    val emrFavRequestModel: RequestDietFavModel? = RequestDietFavModel()
    var specialitySketchManageFavAdapter: SpecialitySketchManageFavAdapter? = null
    var speciality_sketch_uuid: Int = 0
    private var deletefavouriteID: Int? = 0
    private var customdialog: Dialog? = null

    var callbackfavourite: OnFavRefreshListener? = null

    lateinit var specialitySketchName: TextView

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
                R.layout.dialog_manage_speciality_sketch_favourites,
                container,
                false
            )
        viewModel = ManageSpecialitySketchFavouriteViewModelFactory(
            requireActivity().application
        ).create(ManageSpecialitySketchFavouriteViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        customProgressDialog = CustomProgressDialog(requireContext())

        val args = arguments
        if (args == null) {
            status = true

        } else {
            status = false
            favouriteData = args.getParcelable(AppConstants.RESPONSECONTENT)
            Log.i("", "" + favouriteData)
            ResponseContantSave?.favourite_id = favouriteData?.favourite_id
            ResponseContantSave?.favourite_display_order = favouriteData!!.favourite_display_order
            ResponseContantSave?.favourite_name = favouriteData!!.favourite_name.toString()
            binding?.buttonstatus?.text = "Update"

            binding?.displayOrderTxt?.setText(favouriteData!!.favourite_display_order?.toString())
            binding?.autoCompleteTextName?.setText(favouriteData?.favourite_name.toString())
            binding?.autoCompleteTextName!!.isFocusable = false

        }


        //Recycleview adapter init

        specialitySketchManageFavAdapter =
            SpecialitySketchManageFavAdapter(
                requireActivity(),
                ArrayList()
            )

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.listData?.layoutManager = linearLayoutManager
        binding?.listData?.adapter = specialitySketchManageFavAdapter

        if (status == true) {
            binding?.listLayout?.visibility = View.GONE
        } else {
            specialitySketchManageFavAdapter?.setFavAddItem(ResponseContantSave)
            binding?.listLayout?.visibility = View.VISIBLE
        }


        specialitySketchManageFavAdapter?.setOnDeleteClickListener(object :
            SpecialitySketchManageFavAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesID: Int?, testMasterName: String?) {


                drugNAme = testMasterName ?: ""
                deletefavouriteID = favouritesID

                isLoading(true)

                viewModel!!.deleteFavourite(
                    facility_UUID,
                    favouritesID, deleteRetrofitResponseCallback
                )


                /* customdialog = Dialog(requireContext())
                 customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                 customdialog!!.setCancelable(false)
                 customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                 val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView

                 closeImageView.setOnClickListener {
                     customdialog?.dismiss()
                 }
                 specialitySketchName = customdialog!!.findViewById(R.id.addDeleteName) as TextView

                 specialitySketchName.text =
                     "${specialitySketchName.text} '" + testMasterName + "' Record ?"
                 val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                 val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                 yesBtn.setOnClickListener {

                     customdialog!!.dismiss()
                 }
                 noBtn.setOnClickListener {
                     customdialog!!.dismiss()
                 }
                 customdialog!!.show()
 */
            }

        })


        binding?.switchCheck!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            is_active = isChecked
        })


        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.autoCompleteTextName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }


            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    viewModel?.getSpecialitySketchName(
                        s.toString(),
                        favtSpecialitySketchNameCallBack
                    )

                }
            }
        })


        binding?.autoCompleteTextName!!.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteTextName?.setText(autocompleteTestResponse?.get(position)?.s_name)

            Log.i("", "" + autocompleteTestResponse!!.get(position).s_name)
            speciality_sketch_uuid = autocompleteTestResponse!!.get(position).s_uuid

        }

        binding?.add?.setOnClickListener {
            validationData()
        }

        binding?.clear?.setOnClickListener {
            binding?.displayOrderTxt?.setText("")
            binding?.autoCompleteTextName?.setText("")
        }


        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        return binding!!.root
    }


    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            specialitySketchManageFavAdapter?.adapterrefresh(deletefavouriteID)
            binding?.displayOrderTxt?.setText("")
            binding?.autoCompleteTextName?.setText("")
            Toast.makeText(context!!, "$drugNAme Deleted Successfully", Toast.LENGTH_LONG).show()
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

            isLoading(false)

        }

    }


    private fun validationData() {

        if (isNullOrEmpty(binding?.autoCompleteTextName?.text.toString())) {

            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "Please enter Speciality name"
            )

        } else if (isNullOrEmpty(binding?.displayOrderTxt?.text.toString())) {

            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "Please Enter Display order"
            )
        } else {
            createFav()
        }


    }

    private fun createFav() {

        //status differ add or edit
        if (status as Boolean) {
            detailsList.clear()
            header?.is_public = false
            header?.facility_uuid = facility_UUID?.toString()!!
            header?.favourite_type_uuid = AppConstants.FAV_TYPE_ID_SPECIALITY_SKETCH
            header?.department_uuid = deparment_UUID ?: 0
            header?.user_uuid = userDetailsRoomRepository?.getUserDetails()?.uuid?.toString()!!
            header?.display_order = binding?.displayOrderTxt?.text.toString()
            header?.revision = true
            header?.is_active = is_active

            val details: Detail = Detail()
            details.test_master_uuid = 0

            details.item_master_uuid = 0
            details.chief_complaint_uuid = 0
            details.vital_master_uuid = 0
            details.drug_route_uuid = 0
            details.drug_frequency_uuid = 0
            details.duration = 0
            details.duration_period_uuid = 0
            details.drug_instruction_uuid = 0
            details.display_order = binding?.displayOrderTxt?.text.toString()

            details.revision = true
            details.is_active = is_active
            details.diagnosis_uuid = 0
            details.diet_master_uuid = 0
            details.treatment_kit_uuid = 0
            details.diet_category_uuid = 0
            details.diet_frequency_uuid = 0
            details.quantity = "0"
            details.speciality_sketch_uuid = speciality_sketch_uuid
            detailsList.add(details)
            emrFavRequestModel?.headers = this.header!!
            emrFavRequestModel?.details = this.detailsList

            val jsonrequest = Gson().toJson(emrFavRequestModel)

            isLoading(true)
            viewModel?.getADDFavourite(
                facility_UUID,
                emrFavRequestModel!!,
                SpecialityketchAddRetrofitCallback
            )
        } else {
            //False
            isLoading(true)
            viewModel?.getEditFavourite(
                facility_UUID,
                favouriteData?.favourite_id,
                binding?.displayOrderTxt?.text.toString(),
                is_active,
                specialitySketchEditRetrofitCallback
            )
        }
    }


    val SpecialityketchAddRetrofitCallback = object : RetrofitCallback<DietFavMangeResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DietFavMangeResponseModel>?) {

            binding?.displayOrderTxt?.setText("")
            binding?.autoCompleteTextName?.setText("")
            viewModel?.getFavrtList(
                facility_UUID,
                responseBody?.body()?.responseContents?.details?.get(0)?.favourite_master_uuid,
                responseBody?.body()?.responseContents?.headers?.favourite_type_uuid!!,
                manageFvrtListRetrofitCallback
            )



            Toast.makeText(requireContext(), "Favorite Added successfully", Toast.LENGTH_SHORT)
                .show()

        }

        override fun onBadRequest(response: Response<DietFavMangeResponseModel>?) {

            val gson = GsonBuilder().create()
            var mError = ErrorAPIClass()

            mError = gson.fromJson(response!!.errorBody()!!.string(), ErrorAPIClass::class.java)
            Toast.makeText(
                context,
                mError.message,
                Toast.LENGTH_LONG
            ).show()

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

            isLoading(false)
        }


    }


    val manageFvrtListRetrofitCallback = object : RetrofitCallback<FavAddListResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavAddListResponse>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)
            specialitySketchManageFavAdapter?.setFavAddItem(responseBody?.body()?.responseContents)
            if (responseBody?.body()?.responseContents != null) {
                binding?.listLayout!!.visibility = View.VISIBLE
            } else {
                binding?.listLayout!!.visibility = View.GONE
            }
            callbackfavourite?.onRefreshList()

            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )

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


    val specialitySketchEditRetrofitCallback = object : RetrofitCallback<FavEditResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavEditResponse>?) {
            Log.i("", "" + responseBody?.body()?.requestContent)
            binding?.displayOrderTxt?.setText("")
            binding?.autoCompleteTextName?.setText("")
            ResponseContantSave?.favourite_id = responseBody?.body()?.requestContent?.favourite_id
            ResponseContantSave?.favourite_display_order =
                responseBody?.body()?.requestContent?.favourite_display_order
            specialitySketchManageFavAdapter?.clearadapter()

            specialitySketchManageFavAdapter?.setFavAddItem(ResponseContantSave)
            binding?.listLayout?.visibility = View.VISIBLE
            Toast.makeText(context!!, responseBody?.body()?.message, Toast.LENGTH_LONG).show()
            callbackfavourite?.onRefreshList()

        }

        override fun onBadRequest(response: Response<FavEditResponse>?) {

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
            isLoading(false)
        }


    }

    fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.trim().isEmpty())
            return false
        return true
    }


    val favtSpecialitySketchNameCallBack =
        object : RetrofitCallback<SpecialitySketchFavMangeResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<SpecialitySketchFavMangeResponseModel>?) {
                Log.i("", "" + responseBody?.body()?.responseContents)

                autocompleteTestResponse = responseBody?.body()?.responseContents
                val responseContentAdapter = SpecialityNameSearchResultAdapter(
                    context!!,
                    R.layout.row_chief_complaint_search_result,
                    responseBody?.body()?.responseContents!!
                )
                binding?.autoCompleteTextName?.threshold = 1
                binding?.autoCompleteTextName?.setAdapter(responseContentAdapter)
                binding?.autoCompleteTextName?.showDropDown()

            }

            override fun onBadRequest(errorBody: Response<SpecialitySketchFavMangeResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: SpecialitySketchFavMangeResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody?.errorBody()!!.string(),
                        SpecialitySketchFavMangeResponseModel::class.java
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
        Log.e("setOnFavRefreshListener", "inside")
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnFavRefreshListener {
        fun onFavID(position: Int)
        fun onRefreshList()
    }


    fun isLoading(st: Boolean) {

        if (st) {

            customProgressDialog!!.show()

        } else {

            customProgressDialog!!.dismiss()
        }


    }

}