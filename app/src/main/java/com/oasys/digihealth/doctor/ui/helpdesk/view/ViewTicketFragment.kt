package com.oasys.digihealth.doctor.ui.helpdesk.view


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogFragmentViewTicketBinding
import com.oasys.digihealth.doctor.ui.helpdesk.model.*
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.EditTicketViewModel
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.EditTicketViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.util.*

class ViewTicketFragment : DialogFragment() {
    var binding: DialogFragmentViewTicketBinding? = null
    private var viewModel: EditTicketViewModel? = null
    var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var facility_id: Int = 0
    private var department_id: Int = 0

    private var loginType: String? = null
    private var userUUID: Int? = 0
    private var userTypeUUID: Int? = 0
    private var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var categorylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var FilterCategoryResponseMap = mutableMapOf<Int, String>()
    private var selectedCategoryid: Int = 0


    private var prioritylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var FilterPriorityResponseMap = mutableMapOf<Int, String>()
    private var selectedPriorityid: Int = 0

    private var statuslistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var FilterStatusResponseMap = mutableMapOf<Int, String>()
    private var selectedStatusid: Int = 0

    private var selectdAssetUUID: Int? = 0
    private var selectdCategoryUUID: Int? = 0
    private var selectdAssetCode: String? = null
    private var autocompleteTestResponse: List<AssetResponseContent?>? = null

    var status: Boolean? = false
    var detailList: TicketListResponseContent? = null

    var ticketActivityAdapter: TicketActivityAdapter? = null

    private var subCategorylistfilteritem: ArrayList<CategoryResponseContent?>? = ArrayList()
    private var FilterSubCategoryResponseMap = mutableMapOf<Int, String>()
    private var selectedSubCategoryid: Int = 0

    private var selectedMake: String = ""
    private var selectedModel: String = ""
    private var selectedSerial: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_fragment_view_ticket,
                container,
                false
            )


        viewModel = EditTicketViewModelFactory(
            requireActivity().application
        )
            .create(EditTicketViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        utils = Utils(requireContext())

        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..
            status = false
            var uuid = args.getInt("uuid")
            Log.e("selected uuid", uuid.toString())
            getDetails(uuid)
        }


        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        loginType = appPreferences?.getString(AppConstants.LOGINTYPE)!!
        userUUID = userDataStoreBean.uuid!!



        ticketActivityAdapter =
            TicketActivityAdapter(
                requireActivity(), ArrayList()
            )
        binding.prescriptionListRecyclerView.adapter = ticketActivityAdapter


        return binding!!.root
    }

    private fun setCategory() {

        if (status as Boolean) {

        } else {

            for (i in categorylistfilteritem!!.indices)
                if (categorylistfilteritem?.get(i)?.uuid == detailList?.category_uuid) {
                    binding.edCategory.setText(categorylistfilteritem?.get(i)?.name)
                }

        }

    }

    private fun setSubCategory() {

        if (status as Boolean) {
            // binding?.categorySpinner!!.setSelection(1)
        } else {

            Log.e("subcategory_uuid", "" + detailList?.subcategory_uuid)
            for (i in subCategorylistfilteritem!!.indices)

                if (subCategorylistfilteritem?.get(i)?.uuid == detailList?.subcategory_uuid) {

                    binding.edSubCategory.setText(subCategorylistfilteritem?.get(i)?.name)

                }
        }

    }


    private fun setPriority() {

        if (status as Boolean) {

        } else {


            for (i in prioritylistfilteritem!!.indices)

                if (prioritylistfilteritem?.get(i)?.uuid == detailList?.priority_uuid) {

                    binding.edPriority.setText(prioritylistfilteritem?.get(i)?.name)

                }
        }

    }

    val updateTicketRetrofitCallback =
        object {

            override fun onSuccessfulResponse(response: Response<TicketInstitutionResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)
                Toast.makeText(context!!, response.body()?.msg, Toast.LENGTH_LONG).show()
                dismiss()
            }

            override fun onBadRequest(response: Response<TicketInstitutionResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: TicketInstitutionResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        TicketInstitutionResponseModel::class.java
                    )

                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    val categorySpinnerRetrofitCallback =
        object {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)

                categorylistfilteritem?.add(CategoryResponseContent())
                categorylistfilteritem?.addAll(response.body()?.responseContents!!)

                FilterCategoryResponseMap =
                    categorylistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterCategoryResponseMap.put(0, "Category")

                setCategory()

                viewModel?.getSubCategoryList(
                    selectedCategoryid,
                    subCategorySpinnerRetrofitCallback
                )
            }

            override fun onBadRequest(response: Response<CategoryListResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: CategoryListResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        CategoryListResponseModel::class.java
                    )

                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    private fun getDetails(uuid: Int) {
        val jsonBody = JSONObject()
        try {
            jsonBody.put("Id", uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        viewModel?.getTicketById(jsonBody, ticketByIdResponseRetrofitCallback)

    }


    val ticketByIdResponseRetrofitCallback = object {
        override fun onSuccessfulResponse(responseBody: Response<TicketIdResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()?.responseContents)
            if (responseBody?.body()?.responseContents != null) {
                detailList = responseBody.body()?.responseContents

                facility_id = detailList?.facility_uuid!!
                department_id = detailList?.department_uuid!!
                selectedCategoryid = detailList?.category_uuid!!
                selectedSubCategoryid = detailList?.subcategory_uuid!!
                selectedPriorityid = detailList?.ticket_detail_priority_uuid!!
                userUUID = detailList?.application_user_uuid!!
                selectedMake = detailList?.make!!
                selectedModel = detailList?.model!!
                selectedSerial = detailList?.serial!!
                userTypeUUID = detailList?.user_type_uuid!!
                selectdAssetUUID = detailList?.assest_uuid!!
                selectdAssetCode = detailList?.asset_code!!


                ticketActivityAdapter!!.addAll(detailList?.ticket_details)


            }
            viewModel?.getCategoryList(categorySpinnerRetrofitCallback)

        }

        override fun onBadRequest(errorBody: Response<TicketIdResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: TicketIdResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    TicketIdResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    val prioritySpinnerRetrofitCallback =
        object {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)

                prioritylistfilteritem?.add(CategoryResponseContent())
                prioritylistfilteritem?.addAll(response.body()?.responseContents!!)

                FilterPriorityResponseMap =
                    prioritylistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterPriorityResponseMap.put(0, "Priority")

                setPriority()

                viewModel?.getStatusList(statuspinnerRetrofitCallback)

            }

            override fun onBadRequest(response: Response<CategoryListResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: CategoryListResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        CategoryListResponseModel::class.java
                    )

                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    val statuspinnerRetrofitCallback =
        object {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)

                statuslistfilteritem?.add(CategoryResponseContent())
                statuslistfilteritem?.addAll(response.body()?.responseContents!!)

                FilterStatusResponseMap =
                    statuslistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterStatusResponseMap.put(0, "Status")

                //      binding?.edStatus?.setText(FilterStatusResponseMap[detailList.s])

            }

            override fun onBadRequest(response: Response<CategoryListResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: CategoryListResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        CategoryListResponseModel::class.java
                    )

                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    val subCategorySpinnerRetrofitCallback =
        object {

            override fun onSuccessfulResponse(response: Response<CategoryListResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)

                subCategorylistfilteritem?.clear()
                subCategorylistfilteritem?.add(CategoryResponseContent())
                subCategorylistfilteritem?.addAll(response.body()?.responseContents!!)

                FilterSubCategoryResponseMap.clear()
                FilterSubCategoryResponseMap =
                    subCategorylistfilteritem!!.filter { it?.uuid != null && it.name != null }
                        .map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                FilterSubCategoryResponseMap.put(0, "Sub Category")

                setSubCategory()

                viewModel?.getPriorityList(prioritySpinnerRetrofitCallback)

            }

            override fun onBadRequest(response: Response<CategoryListResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: CategoryListResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        CategoryListResponseModel::class.java
                    )

                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

}